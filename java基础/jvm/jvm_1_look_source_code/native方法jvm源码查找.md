到底怎么看jvm的源码，怎么找到对应的native方法的实现逻辑呢？

<http://hg.openjdk.java.net/> 找你要看的对应的jdk的版本

* 以jdk8u为例：

* 下面有很多的版本，我随便选了一个jdk8u60；

* ( 两部分~hotspot 源码和jdk源码，都下载下来（怎么下载？点击进去，然后点击左边的zip或者gz或者bz2，然后就开始下载了）

* 下载完毕以后，你需要一个编辑器，比如vscode，CLion.....

* 然后导入jdk的源码，拿Object.java来说，对应的就是src/share/native/java/lang/Object.c

* 然后你就可以看到如下

  ![jvm_source_look_1](/Users/jzue/Desktop/blog_file/jvm_source_look_1.png)

* 然后假设我们现在要找hashCode的源码，然后我们只需要复制JVM_IHashCode

* 然后导入hotspot的源码，全局搜索

  ![jvm_source_look_3](/Users/jzue/Desktop/blog_file/jvm_source_look_3.png)

发现在jvm.h这个头文件中找到了一个此方法，然后顺藤摸瓜，看看jvm.cpp;

当然，此处还有一个方法，直接在hotspot源码的根目录下 grep来全局查找，但是十分慢。如果你实在是查不到了，就可以试一下grep；

然后再jvm.cpp中找到了其实现逻辑

![jvm_source_look_3](/Users/jzue/Desktop/blog_file/jvm_source_look_3.png)

```
先看Object的句柄是不是null,如果为null，就调用了ObjectSynchronizer的FastHashCode这个方法
```

```c++

intptr_t ObjectSynchronizer::FastHashCode (Thread * Self, oop obj) {
  if (UseBiasedLocking) {
    // NOTE: many places throughout the JVM do not expect a safepoint
    // to be taken here, in particular most operations on perm gen
    // objects. However, we only ever bias Java instances and all of
    // the call sites of identity_hash that might revoke biases have
    // been checked to make sure they can handle a safepoint. The
    // added check of the bias pattern is to avoid useless calls to
    // thread-local storage.
    if (obj->mark()->has_bias_pattern()) {
      // Box and unbox the raw reference just in case we cause a STW safepoint.
      Handle hobj (Self, obj) ;
      // Relaxing assertion for bug 6320749.
      assert (Universe::verify_in_progress() ||
              !SafepointSynchronize::is_at_safepoint(),
             "biases should not be seen by VM thread here");
      BiasedLocking::revoke_and_rebias(hobj, false, JavaThread::current());
      obj = hobj() ;
      assert(!obj->mark()->has_bias_pattern(), "biases should be revoked by now");
    }
  }

  // hashCode() is a heap mutator ...
  // Relaxing assertion for bug 6320749.
  assert (Universe::verify_in_progress() ||
          !SafepointSynchronize::is_at_safepoint(), "invariant") ;
  assert (Universe::verify_in_progress() ||
          Self->is_Java_thread() , "invariant") ;
  assert (Universe::verify_in_progress() ||
         ((JavaThread *)Self)->thread_state() != _thread_blocked, "invariant") ;

  ObjectMonitor* monitor = NULL;
  markOop temp, test;
  intptr_t hash;
  markOop mark = ReadStableMark (obj);

  // object should remain ineligible for biased locking
  assert (!mark->has_bias_pattern(), "invariant") ;

  if (mark->is_neutral()) {
    hash = mark->hash();              // this is a normal header
    if (hash) {                       // if it has hash, just return it
      return hash;
    }
    hash = get_next_hash(Self, obj);  // allocate a new hash code
    temp = mark->copy_set_hash(hash); // merge the hash code into header
    // use (machine word version) atomic operation to install the hash
    test = (markOop) Atomic::cmpxchg_ptr(temp, obj->mark_addr(), mark);
    if (test == mark) {
      return hash;
    }
    // If atomic operation failed, we must inflate the header
    // into heavy weight monitor. We could add more code here
    // for fast path, but it does not worth the complexity.
  } else if (mark->has_monitor()) {
    monitor = mark->monitor();
    temp = monitor->header();
    assert (temp->is_neutral(), "invariant") ;
    hash = temp->hash();
    if (hash) {
      return hash;
    }
    // Skip to the following code to reduce code size
  } else if (Self->is_lock_owned((address)mark->locker())) {
    temp = mark->displaced_mark_helper(); // this is a lightweight monitor owned
    assert (temp->is_neutral(), "invariant") ;
    hash = temp->hash();              // by current thread, check if the displaced
    if (hash) {                       // header contains hash code
      return hash;
    }
    // WARNING:
    //   The displaced header is strictly immutable.
    // It can NOT be changed in ANY cases. So we have
    // to inflate the header into heavyweight monitor
    // even the current thread owns the lock. The reason
    // is the BasicLock (stack slot) will be asynchronously
    // read by other threads during the inflate() function.
    // Any change to stack may not propagate to other threads
    // correctly.
  }

  // Inflate the monitor to set hash code
  monitor = ObjectSynchronizer::inflate(Self, obj);
  // Load displaced header and check it has hash code
  mark = monitor->header();
  assert (mark->is_neutral(), "invariant") ;
  hash = mark->hash();
  if (hash == 0) {
    hash = get_next_hash(Self, obj);
    temp = mark->copy_set_hash(hash); // merge hash code into header
    assert (temp->is_neutral(), "invariant") ;
    test = (markOop) Atomic::cmpxchg_ptr(temp, monitor, mark);
    if (test != mark) {
      // The only update to the header in the monitor (outside GC)
      // is install the hash code. If someone add new usage of
      // displaced header, please update this code
      hash = test->hash();
      assert (test->is_neutral(), "invariant") ;
      assert (hash != 0, "Trivial unexpected object/monitor header usage.");
    }
  }
  // We finally get the hash
  return hash;
}


```

这个方法很长，逐行的去解释的话，比较麻烦，因为涉及到别的方法的调用，（更重要的是，我也没看太懂，不敢胡说八道）主要看看hash值怎么来的    hash = get_next_hash(Self, obj);  很明显，hash值来自于这个方法的调用；

```c++

static inline intptr_t get_next_hash(Thread * Self, oop obj) {
  intptr_t value = 0 ;
  if (hashCode == 0) {
     // This form uses an unguarded global Park-Miller RNG,
     // so it's possible for two threads to race and generate the same RNG.
     // On MP system we'll have lots of RW access to a global, so the
     // mechanism induces lots of coherency traffic.
     //此表单使用无保护的全球公园米勒RNG，
	 //所以两个线程可以竞争并生成相同的RNG。
	 //在MP系统上，我们将有很多对全局的rw访问，因此该机制导致大量的一致性流量。(一致性流量来自于百度翻译，我也不知道怎么翻译coherency traffic)
     value = os::random() ;
  } else
  if (hashCode == 1) {
     // This variation has the property of being stable (idempotent)
     // between STW operations.  This can be useful in some of the 1-0
     // synchronization schemes.
     intptr_t addrBits = cast_from_oop<intptr_t>(obj) >> 3 ;
     value = addrBits ^ (addrBits >> 5) ^ GVars.stwRandom ;
  } else
  if (hashCode == 2) {
     value = 1 ;            // for sensitivity testing
  } else
  if (hashCode == 3) {
     value = ++GVars.hcSequence ;
  } else
  if (hashCode == 4) {
     value = cast_from_oop<intptr_t>(obj) ;
  } else {
     // Marsaglia's xor-shift scheme with thread-specific state
     // This is probably the best overall implementation -- we'll
     // likely make this the default in future releases.
     unsigned t = Self->_hashStateX ;
     t ^= (t << 11) ;
     Self->_hashStateX = Self->_hashStateY ;
     Self->_hashStateY = Self->_hashStateZ ;
     Self->_hashStateZ = Self->_hashStateW ;
     unsigned v = Self->_hashStateW ;
     v = (v ^ (v >> 19)) ^ (t ^ (t >> 8)) ;
     Self->_hashStateW = v ;
     value = v ;
  }

  value &= markOopDesc::hash_mask;
  if (value == 0) value = 0xBAD ;
  assert (value != markOopDesc::no_hash, "invariant") ;
  TEVENT (hashCode: GENERATE) ;
  return value;
}
```

在这个方法中具体的hashCode就体现出来了；6中策略

* hashCode==0（策略一）  取了一个随机数
* hashCode==1（策略二）将对象的内存地址，做移位运算后与一个随机数进行异或得到结果
* hashCode==2（策略三）value=1
* hashCode==3（策略四）返回一个自增序列的当前值
* hashCode==4（策略五）返回当前对象的内存地址
* else（策略六）具有线程特定状态的Marsaglia的XOR移位方案,这可能是最好的总体实现-将很可能在将来的版本中将其设为默认值。（这话不是我说的，是上面那三行英文注释的翻译）
* 最后我查了一下资料，没有找到具体的默认的是哪一种，作为遗留问题，留在此处(<http://www.it1352.com/993557.html> 此篇中指出jdk7使用的是默认的策略一)

**JVM启动参数中添加-XX:hashCode=0，改变默认的hashCode计算方式。**
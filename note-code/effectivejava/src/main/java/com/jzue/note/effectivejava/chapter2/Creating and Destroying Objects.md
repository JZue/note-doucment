## Chapter 2  创建和销毁对象
#### Item1:考虑用静态工厂方法代替构造器
- https://www.cnblogs.com/honoka/p/4858416.html
- 优点
  - 可读性强
  - 调用的时候不需要每次都创建一个新对象
  - 可以返回原返回类型的任何子类型的对象
  - 代码更简洁
- 缺点
  - 如果类不含public 或者protect的构造方法，将不能被继承
  - 与其他普通静态方法没有区别，没有明确的表示一个静态方法用于实例化类，就是说别人并不清楚你这个方法就是用来获取类的实例化对象的，所以一般一个静态工厂方法需要有详细的注释，遵守标准的命名，如使用getInstance、valueOf、newInstance等方法名；
#### Item2:遇到多个构造器参数时要考虑用构建器 
- https://www.jianshu.com/p/ab91dfe050ba  
- 
  - dsad 
    
  
  
  
  
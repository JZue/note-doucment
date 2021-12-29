## 系统自带的注解
 @Deprecated
    使用场景：
        现在的发现某个类定义的一点小问题，然后别人已经在使用了，
        然后就用这个注解，表示这个方法过时了
 
 @SuppressWarnings("xxx")     忽略警告
    @SuppressWarning 是一个注解，它的作用是抑制编译时的警告，
    可以用于标记整个类、某个方法、某个属性或者某个参数，用于告诉编译器这个代码是安全的，不必警告。
     
     可用参数
             all : 抑制所有警告
             boxing : 抑制装箱、拆箱相关的警告
             cast : 抑制强转相关的警告
             dep-ann : 抑制过时注解相关的警告
             fallthrough : 抑制没有 break 的 switch 语句的警告
             finally : 抑制 finally 块没有 return 的警告
             hiding : 抑制关于隐藏的本地变量的警告
             incomplete-switch : 抑制 switch 语句中 case 不完整的警告（当 case 是枚举时）
             nls : 抑制创建无法翻译的字符串的警告 （nls : National Language Support）
             null : 抑制关于可能为空的警告
             rawtypes : 抑制使用泛型作为类参数时没有指明参数类型的警告
             restriction : 抑制使用不建议或者禁止的引用的警告
             serial : 抑制一个可序列化类中没有 serialVersionUID 的警告
             static-access : 抑制一个不正确的静态访问相关的警告
             synthetic-access : 抑制未优化的内部类访问相关的警告
             unchecked : 抑制未经检查的操作（比如强转）的警告
             unqualified-field-access : 抑制不合格的属性访问的警告
             unused : 抑制未使用代码相关的警告
             FieldCanBeLocal ：抑制全局变量只使用一次，可以被当做局部变量的警告
         
 @Override 重写
 
## 自定义注解

    
      public @interface TestAnnotation {
      }  声明定义的是一个注解
     
      String desc();
      成员以无参无异常的方式声明
     
     
      int age() default 18;
      可以用default为成员指定一个默认值
     
     注解可以没有成员==》标识注解
     注解如果只有一个成员则这个成员必须是value,约定俗成的，不是必须的
     @Taeget（{xxxx}）作用域 ==》
            注解可以用于构造方法
            字段声明
            局部变量声明
            方法声明
            包声明
            参数声明
            类、接口                
     @Retention()  生命周期
     @Inherited   允许继承
     @Documented  生成javadoc的时候回包含注解信息
 ## 解析注解
    通过反射获取类，函数或者成员上的运行时注解信息，从而实现动态控制程序逻辑
    
    继承的时候只能继承到类上的注解，方法上的注解继承不到
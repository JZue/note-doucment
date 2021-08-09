## 为什么要有Type接口？

JDK5开始，java中引入泛型，但是java中的泛型是伪泛型。为什么说是伪泛型呢？Java在编译期间，所有的泛型信息都会被擦掉。这时候就有了泛型擦除的概念，Java的泛型基本上都是在编译器这个层次上实现的，在生成的字节码中是不包含泛型中的类型信息的，使用泛型的时候加上类型参数，在编译器编译的时候会去掉，这个过程成为【类型擦除】[泛型擦除详解参考文章](https://www.cnblogs.com/wuqinglong/p/9456193.html)

随之也引入了Type这个接口，其引入主要是为了泛型，没有泛型的之前，只有所谓的原始类型。此时，所有的原始类型都通过字节码文件类Class类进行抽象。Class类的一个具体对象就代表一个指定的原始类型。 
这个时候存在一个问题,
原始类型会生成字节码文件对象，而泛型类型相关的类型并不会生成与其相对应的字节码文件(因为泛型类型将会被擦除)，因此，无法将泛型相关的新类型与class相统一。因此，为了程序的扩展性以及为了开发需要去反射操作这些类型，就引入了Type这个类型，并且新增了**ParameterizedType(参数化类型)、TypeVariable(类型变量类型)、GenericArrayType(泛型数组类型)、WildcardType(通配符类型)**四个表示泛型相关的类型，再加上Class，这样就可以用Type类型的参数来接受以上五种子类的实参或者返回值类型就是Type类型的参数。统一了与泛型有关的类型和原始类型Class。而且这样一来，我们也可以通过反射获取泛型类型参数。

## ParameterizedType

参数化类型，即泛型；形如：List<T>、Map<K,V>等带有参数化的对象;

#### 核心方法详解

```java
  public interface ParameterizedType extends Type {
    // 获取<>中实际类型  例如List<Long>  返回Long.class
    Type[] getActualTypeArguments();
    // 获取<>前实际类型，比如List<Long>  返回Long.class
  	Type getRawType();
    // 一般针对成员类、成员接口，返回其所属的类，
    // 例如Map.Entry<K,V> 返回的就是Map，表示Entry是Map成员之一的类型。如果此类型是顶层类型，则返回 null
  	Type getOwnerType();
  }

```

#### ParameterizedType#DEMO代码

```java
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xuejunze
 * @date 2021/7/19 2:32 下午
 **/
public class ParameterizedTypeDemo<T> {

    public static void main(String[] args)  {
        Method[] methods = com.note.other.typeinterface.ParameterizedTypeDemo.class.getMethods();
        Method demoMethod = methods[0];
        Type[] genericParameterTypes = demoMethod.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            System.out.println("========"+genericParameterType.getClass().getSimpleName()+" 【type】 "+genericParameterType);
            if (genericParameterType instanceof ParameterizedType) {
                ParameterizedType pa = (ParameterizedType) genericParameterType;
                System.out.println("【getActualTypeArguments】"+Arrays.toString(pa.getActualTypeArguments()));
                System.out.println("【getOwnerType】"+pa.getOwnerType());
                System.out.println("【getRawType】"+pa.getRawType().getTypeName());
            }
        }

    }

    public <K,V> void testGenericArrayType(List<Long> list,
                                     ParameterizedTypeDemo<T> test,
                                     List<List<String>> ppTypeList,
                                     Map.Entry<K,V> map) {
    }

}

```

#### ParameterizedType#DEMO执行结果

```
========ParameterizedTypeImpl 【type】 java.util.List<java.lang.Long>
【getActualTypeArguments】[class java.lang.Long]
【getOwnerType】null
【getRawType】java.util.List
========ParameterizedTypeImpl 【type】 com.note.other.typeinterface.ParameterizedTypeDemo<T>
【getActualTypeArguments】[T]
【getOwnerType】null
【getRawType】com.note.other.typeinterface.ParameterizedTypeDemo
========ParameterizedTypeImpl 【type】 java.util.List<java.util.List<java.lang.String>>
【getActualTypeArguments】[java.util.List<java.lang.String>]
【getOwnerType】null
【getRawType】java.util.List
========ParameterizedTypeImpl 【type】 java.util.Map$Entry<K, V>
【getActualTypeArguments】[K, V]
【getOwnerType】interface java.util.Map
【getRawType】java.util.Map$Entry

```



## java.lang.reflect.TypeVariable

类型变量，即泛型中的变量；例如：T、K、V等变量，可以表示任何类；在这需要强调的是，TypeVariable代表着泛型中的变量，而ParameterizedType则代表整个泛型；

####TypeVariable核心方法讲解 

```java
public interface TypeVariable<D extends GenericDeclaration> extends Type {
   // 获得泛型的上限，若未明确声明上边界则默认为Object
   // 例如：TypeVariableDemo<K extends List<V>,V,T> 它的上边界就是List<V>，说的简单点，其中的泛型extends 的值。也可以理解为，它最大粒度至少是个List<V>
    Type[] getBounds();
    //获取声明该类型变量实体(即获得类、方法或构造器名),例如上面例子，就是TypeVariableDemo.class声明的.
    /* 在方法上的例如， <M> void   method1(M t){}
  	 */
  	/*在构造方法上的例如
  		class MyClass<E>{
				public <T> MyClass(T t){
				System.out.println("t value is "+t);
				}
			}
  	 */
    D getGenericDeclaration();
    //获得名称，即K、V、E之类名称
    String getName();
    // 获得注解类型的上限，若未明确声明上边界则默认为长度为0的数组
    AnnotatedType[] getAnnotatedBounds()
}
```

#### TypeVariable#DEMO

```java
import javax.annotation.Resource;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xuejunze
 * @date 2021/7/19 4:48 下午
 **/
public class TypeVariableDemo<K extends List<V>,V extends Resource ,T> {
    K k;
    V v;
    List<V> vList;
    Map<V,T> tMap;
    public static void main(String[] args)  {
        Field[] fields = TypeVariableDemo.class.getDeclaredFields();
        for (Field field : fields) {
            Type type = field.getGenericType();
            System.out.println(">>>>>>>>>>>>>"+type.getClass().getSimpleName()+" 【type】 "+type);
            if (type instanceof TypeVariable) {
                TypeVariable typeVariable=((TypeVariable) type);
                System.out.println("【getBounds】"+ Arrays.toString(typeVariable.getBounds()));
                AnnotatedType[] annotatedBounds = typeVariable.getAnnotatedBounds();
                if (annotatedBounds[0].getAnnotations().length>0) {
                    System.out.println("【getAnnotatedBounds】"+ annotatedBounds[0].getAnnotations()[0].annotationType());
                }

                System.out.println("【getAnnotatedBounds.size】"+ typeVariable.getAnnotatedBounds().length);
                System.out.println("【getName】"+typeVariable.getName());
                System.out.println("【getGenericDeclaration】"+typeVariable.getGenericDeclaration());
            }
        }
    }
}
```

#### TypeVariable#DEMO 执行结果

```
>>>>>>>>>>>>>TypeVariableImpl 【type】 K
【getBounds】[java.util.List<V>]
【getAnnotatedBounds.size】1
【getName】K
【getGenericDeclaration】class com.note.other.typeinterface.TypeVariableDemo
>>>>>>>>>>>>>TypeVariableImpl 【type】 V
【getBounds】[interface javax.annotation.Resource]
【getAnnotatedBounds.size】1
【getName】V
【getGenericDeclaration】class com.note.other.typeinterface.TypeVariableDemo
>>>>>>>>>>>>>ParameterizedTypeImpl 【type】 java.util.List<V>
>>>>>>>>>>>>>ParameterizedTypeImpl 【type】 java.util.Map<V, T>
```







## java.lang.reflect.GenericArrayType

#### 方法

```java
// 返回泛型数组中元素的Type类型，即List<String>[] 中的 List<String>
Type getGenericComponentType();
```

 简单来说就是：范型数组,组成数组的元素中有范型则实现了该接口; 它的组成元素是 ParameterizedType 或 TypeVariable 类型。

描述的是形如：A< T>[]或T[] 

#### GenericArrayType#DEMO

```java
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author xuejunze
 * @date 2021/6/8 12:32 下午
 **/
public class GenericArrayTypeDemo<T> {
    public static void main(String[] args)  {
        Method[] methods = GenericArrayTypeDemo.class.getMethods();
        Method demoMethod = methods[0];
        Type[] genericParameterTypes = demoMethod.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            System.out.println(genericParameterType.getClass().getSimpleName()+" 【type】 "+genericParameterType);
        }

    }

    public void testGenericArrayType(List<String>[] pTypeArray, T[] vTypeArray
            , List<String> list, String[] strings, GenericArrayTypeDemo<T>[] test, List<List<String>>[] ppTypeList) {
    }
}
```



#### GenericArrayType#DEMO执行结果

```
GenericArrayTypeImpl 【type】 java.util.List<java.lang.String>[]
GenericArrayTypeImpl 【type】 T[]
ParameterizedTypeImpl 【type】 java.util.List<java.lang.String>
Class 【type】 class [Ljava.lang.String;
GenericArrayTypeImpl 【type】 com.note.other.markdown.TypeDemo<T>[]
ParameterizedTypeImpl 【type】 java.util.List<java.util.List<java.lang.String>>
```



WildcardType

泛型表达式（或者通配符表达式），即`? extends Number`这样的表达式;WildcardType虽然是Type的子接口，但却不是Java类型中的一种

```java
public interface WildcardType extends Type {
    // 获取泛型表达式上界（上限extends）
    Type[] getUpperBounds();
    // 获取泛型表达式下界（下限super）
    Type[] getLowerBounds();
}
```





1. 
2. 
3. 
4. 
5. Class


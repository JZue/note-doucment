DDD 分层：https://blog.csdn.net/qq_26023163/article/details/114540055

![三层架构与DDD架构](https://file.xjzspace.com/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI2MDIzMTYz,size_16,color_FFFFFF,t_70.png)



调用关系：https://zhuanlan.zhihu.com/p/135944454

**贫血模型、充血模型的取舍**

https://www.cnblogs.com/kaleidoscope/p/15006213.html

**文章**

https://my.oschina.net/liudonghui/blog/5154994

**代码**

https://github.com/maclstudio/dddbook



```
工具MapStruct
```



应用分层

| 层级           | 构成                                       | 职责                                                         | 备注                                                         |
| -------------- | ------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Interface      | facade+dto+assembler                       | DTO 就是我们接口返回值对象；assembler承担着将domain 转换成DTO的职责；facade下就是接口 |                                                              |
| Application    | service                                    | transactionscript风格业务逻辑主要在service中实现，而在领域驱动设计的架构里，service只负责协调并委派业务逻辑给领域对象进行处理 | 我们可以考察这一点来识别系统是transaction script架构还是domain model架构。 |
| Domain         | entity+valueobject+domain event+repository | 面向对象、建模                                               |                                                              |
| Infrastructure |                                            | 半固件职责                                                   |                                                              |

#### 什么是聚合、聚合根、边界(aggregate/aggregate root)

https://www.cnblogs.com/powerzhang/archive/2013/05/25/3099112.html

**每个聚合都会有一个聚合根和聚合的边界Boundary**

```
聚合和聚合根是领域模型里面很重要的一个概念，其实我们在从真实世界对业务对象进行识别和概念建模的时候，关注的就是聚合根，这才是我们真正要管理的业务对象。 一个对象可能有多个层次，也可能有多个子实体，但是这些子实体都不可能孤立存在，它们必须依附于一个聚合根存在，它们和根节点具有同样的生命周期。

　　如果一个客户消亡，客户联系方式，客户的多张银行账户信息将不再有任何意义。如果一张采购订单头消失，那么采购订单明细没有任何存在的意义。客户，采购订单，发票这些从真实业务中转化过来的业务对象才是真正的领域核心对象。这些对象可能在领域建模的时候会分解到多个Entity或Value Object，但是一定要意识到实际的聚合在哪里？我们真正关注的业务对象实体究竟有哪些？
```

　聚合Aggregate就是一组相关对象的集合，我们把它作为数据修改和访问的单元。每个聚合都会有一个聚合根和聚合的边界Boundary，边界定义了在一个聚合里面内部应该有哪些实体，哪些子实体对象。定义边界的原因是我们期望对一个聚合的访问是通过聚合根点进行的，聚合里面的子实体对外界是完全封闭的。对于外部对象不应该去访问到一个聚合边界里面的子实体

#### 什么是值对象(value object)

**将隐性的概念显性化**

电话号仅仅是用户的一个参数，属于隐形概念，但实际上电话号的区号才是真正的业务逻辑，而我们需要将电话号的概念显性化，通过写一个Value Object

```java
public class PhoneNumber {
  
    private final String number;
    public String getNumber() {
        return number;
    }

    public PhoneNumber(String number) {
        if (number == null) {
            throw new ValidationException("number不能为空");
        } else if (isValid(number)) {
            throw new ValidationException("number格式错误");
        }
        this.number = number;
    }
  
    public static boolean isValid(String number) {
        String pattern = "^0?[1-9]{2,3}-?\\d{8}$";
        return number.matches(pattern);
    }
}   
```

#### 实体(Entity)

数据+行为抽象出的对应现实世界业务个体的对象



#### 仓储(repositories)

用来管理实体的集合，是对于dao到entity的领域组件。dao方法是细粒度的，更接近DB，而repository方法的粒度粗一些，而且更接近领域。

#### 服务（services）

所有的service只负责协调并委派业务逻辑给领域对象进行处理，其本身并真正实现业务逻辑，绝大部分的业务逻辑都由领域对象承载和实现了

#### 领域事件（ domain event）

Martin Fowler对领域事件的定义是：“重要的事件肯定会在系统其它地方引起反应，因此理解为什么会有这些反应同样也很重要。”

一个领域事件可以理解为是发生在一个特定领域中的事件，是你希望在同一个领域中其他部分知道并产生后续动作的事件。但是并不是所有发生过的事情都可以成为领域事件。一个领域事件必须对业务有价值，有助于形成完整的业务闭环，也即一个领域事件将导致进一步的业务操作。

#### 限定上下文（bounded context）

系统的边界，或者是moudule的边界

```
https://zhuanlan.zhihu.com/p/31985410
```

#### CQRS

CQRS，中文名为命令查询职责分离。







#### 怎么判断一个实体是否是聚合根

```
该实体的创建过程是否是完全独立的，而非依赖与其他实体的生命周期；
是否有全局唯一的ID,是否可以创建或者修改其他对象，是否有专门的模块管理。
（2）（3）只是作为一个辅助参考，最关键的还是（1），如果（1）能遵守了，十之八九可以定义为一个独立的聚合了。
https://blog.csdn.net/chengzang3159/article/details/100839365
```

哈哈，主要还是ddd需要从实际业务出发可能不太好举例吧，毕竟是思想性的东西已设计出发主导的, 不过还是算不错的课程了，代码落地的确水了点。
推荐给你一些具体的比较详细的项目
[微服务大师 Chris Richardson《微服务架构设计模式》的作者的示例集合](https://eventuate.io/exampleapps.html)
[拉钩一个成富大佬的示例《快乐出行》](https://github.com/alexcheng1982/happyride)

#### 不过以上示例项目都只是涉及ddd, 并不是专注ddd，微服务的合理拆分还是一个难题。

------

2021年07月19日15:10:59 补充
IDDD(实现领域驱动设计)[译者滕云](https://github.com/davenkin)的[示例项目https://github.com/e-commerce-sample](https://github.com/e-commerce-sample)
IDDD(实现领域驱动设计)[译者滕云个人文章集合](https://insights.thoughtworks.cn/author/tengyun/)







所谓的依赖倒置原则指的是：高层模块不应该依赖于低层模块，两者都应该依赖于抽象，抽象不应该依赖于细节，细节应该依赖于抽象。



斗鱼开发者平台：https://open.douyu.com/source/doc/newGuide

虎牙：https://www.huya.com/l 全部直播 爬虫实现

//live-cover.msstatic.com/huyalive/94525224-2583571962-11096357083652554752-3503038830-10057-A-0-1-imgplus/20211215123013.jpg?streamName=94525224-2583571962-11096357083652554752-3503038830-10057-A-0-1-imgplus&interval=10&x-oss-process=image/resize,limit_0,m_fill,w_338,h_190/sharpen,80/format,jpg/interlace,1/quality,q_90

企鹅电竞



淘宝直播没有网页版，如何实现是个问题



小红书：https://www.jb51.net/article/204387.htm



直播信息/网红信息/以及视频更新信息=>用户增长=>这些用户都是粉丝



1）=>邀请主播上线更新=>成立互动区=>购物平台

2）走广告路线 https://zhuanlan.zhihu.com/p/358406950


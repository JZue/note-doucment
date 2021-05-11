com.jzue.tomcat.lifecycle


 Common interface for component life cycle methods.  Catalina components
 may implement this interface (as well as the appropriate interface(s) for
 the functionality they support) in order to provide a consistent mechanism
 to start and stop the component.
 The valid state transitions for components that support {@link Lifecycle}
 are:
 <pre>
            start()
  -----------------------------
  |                           |
  | init()                    |
 NEW -»-- INITIALIZING        |
 | |           |              |     ------------------«-----------------------
 | |           |auto          |     |                                        |
 | |          \|/    start() \|/   \|/     auto          auto         stop() |
 | |      INITIALIZED --»-- STARTING_PREP --»- STARTING --»- STARTED --»---  |
 | |         |                                                            |  |
 | |destroy()|                                                            |  |
 | --»-----«--    ------------------------«--------------------------------  ^
 |     |          |                                                          |
 |     |         \|/          auto                 auto              start() |
 |     |     STOPPING_PREP ----»---- STOPPING ------»----- STOPPED -----»-----
 |    \|/                               ^                     |  ^
 |     |               stop()           |                     |  |
 |     |       --------------------------                     |  |
 |     |       |                                              |  |
 |     |       |    destroy()                       destroy() |  |
 |     |    FAILED ----»------ DESTROYING ---«-----------------  |
 |     |                        ^     |                          |
 |     |     destroy()          |     |auto                      |
 |     --------»-----------------    \|/                         |
 |                                 DESTROYED                     |
 |                                                               |
 |                            stop()                             |
 ----»-----------------------------»------------------------------

 Any state can transition to FAILED.

 Calling start() while a component is in states STARTING_PREP, STARTING or
 STARTED has no effect.

 Calling start() while a component is in state NEW will cause init() to be
 called immediately after the start() method is entered.

 Calling stop() while a component is in states STOPPING_PREP, STOPPING or
 STOPPED has no effect.

 Calling stop() while a component is in state NEW transitions the component
 to STOPPED. This is typically encountered when a component fails to start and
 does not start all its sub-components. When the component is stopped, it will
 try to stop all sub-components - even those it didn't start.

 Attempting any other transition will throw {@link LifecycleException}.

 The {@link LifecycleEvent}s fired during state changes are defined in the
 methods that trigger the changed. No {@link LifecycleEvent}s are fired if the
 attempted transition is not valid.


### 生命周期管理机制-----观察者模式（发布订阅）
所谓的观察者模式，实际上就是通过被观察去基于事件去提醒观察者列表中的所有观察者，然后所有的观察者再做出相应的响应
* com.jzue.tomcat.lifecycle.Lifecycle 是生命周期的基本接口
* com.jzue.tomcat.lifecycle.LifecycleBase 是Lifecycle的基本实现类

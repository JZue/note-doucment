#### linux的进程

1.  通俗来说，进程就是正在运行的一个程序
2.  内核观点：担当分配系统资源（CPU时间，内存）的实体
3.  操作系统的角度来说，进程描述为一个结构体---->PCB（在Linux下PCB位task_struct）
4.  进程的创建是有限的



进程创建是操作系统执行程序的需要或者用户或进程要求创建一个新的进程。进程创建首先是在进程表中为进程建立一个进程控制块PCB，采用fork()系统调用将复制执行进程的PCB块 



#### 进程控制块PCB



pcb：process control block   存放进程的管理和控制信息的数据结构称为进程控制块,

os对进程进行控制和管理围绕pcb进行



重要性：

​	进程控制快是进程存在的唯一标志、是进程管理和控制的最重要的数据结构.一个进程只可以有一个pcb.

 进程控制块，实际是一个结构体，放在sched.h文件中，Linux下可以通过whereis sched.h命令查看具体路径

 https://blog.csdn.net/qq_33883085/article/details/88780577 

该结构体主要包含： 

1. **进程ID**
2. **进程的状态：就绪、运行、挂起、停止** 
3.  **进程切换时需要保存和恢复的一些CPU寄存器** 
4. **描述虚拟地址空间的信息** 
5. **描述控制终端的信息** 
6.  **当前工作目录** 
7.  **umask掩码** 
8.  **文件描述符表（file_struct）**
9.  **和信号相关的信息** 
10.  **用户id和组id** 
11.  **会话和进程组** 
12.  **进程可以使用的资源上限** 



####  文件描述表

 在linux内核中通常会有个task_struct结构体来维护进程相关的表，叫进程控制块，这个块里面会有指针**指向**file_struct （文件结构体) 的结构体，称为文件描述表，文件描述符就是这个表的索引（其实就是数组index）。 

对于一个文件描述符表，默认会打开3个文件描述符，分别是：

* 标准输入（ standard input 0 ）
* 正确输出（standard input 1）
* 错误输出（error output 2）

对应的设备**一般**是键盘，显示器，显示器，也就是说一般的文件描述符表的前三个分别是**标准输入（ standard input 0 ）**、**正确输出（standard input 1）**、**错误输出（error output 2）**。



#### file_struct （ 文件结构体 ）

 https://www.cnblogs.com/hanxiaoyu/p/5677677.html 

 https://blog.csdn.net/gmy2016wiw/article/details/72594093 



![931732-20160717011845420-65589024](http://file.xjzspace.com/20210720210503.png)

文件描述符就是从0开始的小整数。当我们打开文件时，操作系统在内存中要创建相应的数据结构来描述目标文件，于是就有了file结构体(file_struct)。表示一个已经打开的文件对象。而进程执行open系统调用，所以必须让进程和文件关联起来。每个进程都有一个指针*file，指向一张表files_struct,该表最重要的部分就是包含一个指针数组，每个元素都是一个指向打开文件的指针！所以，本质上，文件描述符就是该数组的下标。所以只要拿着文件描述符就可以找到对应的文件。


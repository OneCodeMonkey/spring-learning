## Java 并发笔记

##### 1.实现两个线程交替打印 0～100的数值：(2024-12-14)
参考： JavaConcurrentProgramming@testAlternativelyPrint1()
JavaConcurrentProgramming@testAlternativelyPrint2()
两种方式：
1. 实现方式一：基于共享变量，交替改变其状态值
2. 实现方式二：基于synchonized 锁机制，通过 notify() 和 wait() 来交替获取锁且执行任务

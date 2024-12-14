package com.liuyang1.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

//@SpringBootTest
@Slf4j
public class JavaConcurrentProgramming {
    static volatile boolean flag = true;
    static AtomicInteger val1 = new AtomicInteger(0);

    /**
     * 1. 两个线程交替打印 0～100的数值
     * 实现方式一：基于共享变量，交替改变其状态值
     */
    @Test
    public void testAlternativelyPrint1() {
        Thread print1 = new Thread(() -> {
            while (val1.get() <= 100) {
                if (flag) {
                    System.out.println("Thread 1: " + val1.getAndIncrement());
                    flag = false;
                }
            }
        });
        Thread print2 = new Thread(() -> {
            while (val1.get() <= 100) {
                if (!flag) {
                    System.out.println("Thread 2: " + val1.getAndIncrement());
                    flag = true;
                }
            }
        });
        print1.start();
        print2.start();
    }

    /**
     * 1. 两个线程交替打印 0～100的数值
     * 实现方式二：基于synchonized 锁机制，通过 notify() 和 wait() 来交替获取锁且执行任务
     */
    @Test
    public void testAlternativelyPrint2() {
        Thread print1 = new Thread(() -> {
            while (val1.get() <= 100) {
                synchronized (this) {
//                    System.out.println(this.getClass());
                    this.notify();
                    System.out.println("Thread 1: " + val1.getAndIncrement());
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        Thread print2 = new Thread(() -> {
            while (val1.get() <= 100) {
                synchronized (this) {
//                    System.out.println(this.getClass());
                    this.notify();
                    System.out.println("Thread 2: " + val1.getAndIncrement());
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        print1.start();
        print2.start();
    }
}

package org.jretty.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author zollty
 * @since 2018年5月25日
 */
public class NamedThreadTest {
    
    public static void main(String[] args) throws InterruptedException {
        
        NamedThreadFactory factory = new NamedThreadFactory("apollo", true);
        
        ExecutorService es = Executors.newFixedThreadPool(10, factory);
        
        Runnable r = new NamedRunnable() {
            
            @Override
            public void run() {
                ThreadUtils.sleepThread(15000);
                System.out.println(Thread.currentThread().getName() + " done!");
            }

            @Override
            public String getName() {
                return "apolloTask";
            }
        };
        es.execute(r);
        ThreadUtils.sleepThread(1000);
        es.execute(r);
        ThreadUtils.sleepThread(1000);
        es.execute(r);
        ThreadUtils.sleepThread(1000);
        es.execute(r);
        ThreadUtils.sleepThread(1000);
        es.execute(r);
        
        factory = new NamedThreadFactory("AsyncRunAfterStart_Thread", false);
        factory.newThread(new Runnable() {
            @Override
            public void run() {
                ThreadUtils.sleepThread(15000);
            }
        }).start();
        
        
        test2();
        
        es.awaitTermination(20000, TimeUnit.MILLISECONDS);
        es.shutdownNow();
    }
    
    public static void test2() throws InterruptedException {
        NamedThreadFactory factory = new NamedThreadFactory("apollo", true);
        factory.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("--------------" + e.toString());
            }
        });
        factory.newThread(new NamedRunnable() {
            
            @Override
            public void run() {
                ThreadUtils.sleepThread(15000);
                System.out.println(Thread.currentThread().getName() + " done!");
                throw new IllegalAccessError();
            }

            @Override
            public String getName() {
                return "apolloTask";
            }
        }).start();
    }

}

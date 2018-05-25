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
            }

            @Override
            public String getName() {
                return "apolloTask";
            }
        };
        es.execute(r);
        es.execute(r);
        es.execute(r);
        es.execute(r);
        es.execute(r);
        
        factory = new NamedThreadFactory("apollo", true);
        factory.newThread(r).start();
        
        factory = new NamedThreadFactory("AsyncRunAfterStart_Thread", false);
        factory.newThread(new Runnable() {
            @Override
            public void run() {
                ThreadUtils.sleepThread(15000);
            }
        }).start();
        
        
        es.awaitTermination(20000, TimeUnit.MILLISECONDS);
        es.shutdownNow();
    }

}

package org.jretty.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BatchTakeBlockQueueTest {
    
    
 // 队列最大容量
    public static final int Q_SIZE = 10240;
    // 生产者/消费者线程数
    public static final int THREAD_NUM = 1;
    public static final int P_THREAD_NUM = 2;
    
    private static final int DEFAULT_RANGE_FOR_SLEEP = 10;
    
    public static final long RUN_TIME = 20000;
    
    
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    
    public static void test1() throws InterruptedException {
        
        final AnotherBatchTakeBlockQueue<String> dp = new AnotherBatchTakeBlockQueue<String>();
        final AtomicLong count = new AtomicLong();
        final AtomicLong countP = new AtomicLong();
        final CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        final CountDownLatch Platch = new CountDownLatch(P_THREAD_NUM);
        // 生产者线程
        class Producer implements Runnable {
            @Override
            public void run() {
                Random r = new Random();
                long st = System.currentTimeMillis();
                while(System.currentTimeMillis() - st < RUN_TIME) {
                    try {
                        dp.offer("s");
                        countP.incrementAndGet();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platch.countDown();
            }

        }
        ;
        // 消费者线程
        class Consumer implements Runnable {
            @Override
            public void run() {
                LinkedList<String> pollList = new LinkedList<String>();
                LinkedList<String> ret = new LinkedList<String>();
                long st = System.currentTimeMillis();
                while(System.currentTimeMillis() - st < RUN_TIME) {
                    try {
                        dp.drainToMayWait(pollList);
                        count.addAndGet(pollList.size());
                        
                        ret.addAll(pollList);
                        if(ret.size()>=100) {
                            handleData(ret);
                        }
                        
                        pollList.clear();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        count.addAndGet(dp.size());
                    }
                }
                latch.countDown();
            }
        }
        ;
        // 创建生产者
        Thread[] arrProducerThread = new Thread[P_THREAD_NUM];
        for (int i = 0; i < P_THREAD_NUM; i++) {
            arrProducerThread[i] = new Thread(new Producer());
        }
        // 创建消费者
        Thread[] arrConsumerThread = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i] = new Thread(new Consumer());
        }
        // go!
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < P_THREAD_NUM; i++) {
            arrProducerThread[i].start();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i].start();
        }
        
        Platch.await();
        System.out.println("producer shutdown.");
        System.out.println("produce size = " + countP.get());
        latch.await(5000, TimeUnit.MILLISECONDS);
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i].interrupt();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i].interrupt();
        }
        latch.await();
        System.out.println("remain size = "+dp.size());
        count.addAndGet(dp.size());
        System.out.println("consumer shutdown.");
        System.out.println("consumer size = " + count.get());
        
        long t2 = System.currentTimeMillis();
        System.out.println(" cost : " + (t2 - t1));
    
    }
    
    public static void handleData(List<String> data) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //System.out.println(sdf.format(new Date()) + " - drainTo one times. size = " + data.size());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        data.clear();
    }
    
    public static void test2() throws InterruptedException {
        
        final BatchTakeBlockQueue<String> dp = new BatchTakeBlockQueue<String>();
        final AtomicLong count = new AtomicLong();
        final AtomicLong countP = new AtomicLong();
        final CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        final CountDownLatch Platch = new CountDownLatch(P_THREAD_NUM);
        // 生产者线程
        class Producer implements Runnable {
            @Override
            public void run() {
                Random r = new Random();
                long st = System.currentTimeMillis();
                while(System.currentTimeMillis() - st < RUN_TIME) {
                    try {
                        dp.offer("s");
                        countP.incrementAndGet();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platch.countDown();
            }

        }
        ;
        // 消费者线程
        class Consumer implements Runnable {
            @Override
            public void run() {
                LinkedList<String> pollList = new LinkedList<String>();
                long st = System.currentTimeMillis();
                while(System.currentTimeMillis() - st < RUN_TIME) {
                    dp.drainToMayWait(pollList);
                    count.addAndGet(pollList.size());
                    handleData(pollList);
                }
                latch.countDown();
            }
        }
        ;
        // 创建生产者
        Thread[] arrProducerThread = new Thread[P_THREAD_NUM];
        for (int i = 0; i < P_THREAD_NUM; i++) {
            arrProducerThread[i] = new Thread(new Producer());
        }
        // 创建消费者
        Thread[] arrConsumerThread = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i] = new Thread(new Consumer());
        }
        // go!
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < P_THREAD_NUM; i++) {
            arrProducerThread[i].start();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i].start();
        }
        
        Platch.await();
        System.out.println("producer shutdown.");
        System.out.println("produce size = " + countP.get());
        latch.await(5000, TimeUnit.MILLISECONDS);
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i].interrupt();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i].interrupt();
        }
        latch.await();
        System.out.println("remain size = "+dp.size());
        count.addAndGet(dp.size());
        System.out.println("consumer shutdown.");
        System.out.println("consumer size = " + count.get());
        
        long t2 = System.currentTimeMillis();
        System.out.println(" cost : " + (t2 - t1));
    
    }

}

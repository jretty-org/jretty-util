package org.jretty.util;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可以批量take数据的 LinkedBlockingQueue <br>
 * 
 * 使用场景：<br>
 *   多个producer往同一个BlockingQueue存放数据，一个或者较少数的consumer取出数据去处理。<br>
 *   这种场景下，调用offer(string)添加数据，调用drainToMayWait(List)取出数据，
 *   当BlockingQueue缓存中没有数据时，consumer线程处于wait状态，对CPU和内存压力很小。
 *   （传统做法是没有数据则Thread.sleep（），这种做法CPU占用较高）
 *   
 * 
 * @author zollty
 * @since 2016-11-26
 * @param <T>
 */
public class BatchTakeBlockQueue<T> {

    private LinkedBlockingQueue<T> dataCache;

    /**
     * 最大缓存的数据条数
     */
    private final int maxCacheSize;
    
    private final static int DEFAULT_MAX_CACHE_SIZE = 102400;

    /**
     * 每次批量取数据的最小条数
     */
    private int batchMinSize = 100;

    /**
     * 如果使用offerMayWait，则必须设置offerTimeoutMs值。
     */
    private Long offerTimeoutMs;

    /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();
    /** Wait queue for waiting takes */
    private final Condition notEmpty = takeLock.newCondition();
    /** for judge wait */
    private AtomicLong tmpCount;

    public BatchTakeBlockQueue() {
        maxCacheSize = DEFAULT_MAX_CACHE_SIZE;
        dataCache = new LinkedBlockingQueue<T>(maxCacheSize);
        tmpCount = new AtomicLong(0);
    }
    
    public BatchTakeBlockQueue(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize > 0 ? maxCacheSize : DEFAULT_MAX_CACHE_SIZE;
        dataCache = new LinkedBlockingQueue<T>(this.maxCacheSize);
        tmpCount = new AtomicLong(0);
    }

    public BatchTakeBlockQueue(int maxCacheSize, int batchMinSize) {
        this.maxCacheSize = maxCacheSize > 0 ? maxCacheSize : DEFAULT_MAX_CACHE_SIZE;
        dataCache = new LinkedBlockingQueue<T>(this.maxCacheSize);
        tmpCount = new AtomicLong(0);
        if (batchMinSize > 0) {
            this.batchMinSize = batchMinSize;
        }
    }

    public boolean offer(T src) throws InterruptedException {
        if (dataCache.offer(src)) {

            checkWhenAddData();

            return true;
        }
        return false;
    }

    public boolean offerMayWait(T src) throws InterruptedException {
        if (offerTimeoutMs != null) {
            if (dataCache.offer(src, offerTimeoutMs, TimeUnit.MILLISECONDS)) {

                checkWhenAddData();

                return true;
            }
            return false;
        } else {
            if (dataCache.offer(src)) {

                checkWhenAddData();

                return true;
            }
            return false;
        }
    }
    
    public boolean offerMayWait(T src, long offerTimeoutMs) throws InterruptedException {
        if (dataCache.offer(src, offerTimeoutMs, TimeUnit.MILLISECONDS)) {

            checkWhenAddData();

            return true;
        }
        return false;
    }

    private void checkWhenAddData() {
        tmpCount.getAndIncrement();
        if (tmpCount.get() >= batchMinSize) {
            final ReentrantLock takeLock = this.takeLock;
            try {
                takeLock.lockInterruptibly();
                try {
                    notEmpty.signal();
                } finally {
                    takeLock.unlock();
                }
            } catch (InterruptedException e) {
                // ignore..
            }
        }
    }

    public void drainToMayWait(Collection<? super T> c) {
        try {
            T t = dataCache.take();
            c.add(t);
        } catch (InterruptedException e) {
            return;
        }

        final ReentrantLock takeLock = this.takeLock;
        try {

            takeLock.lockInterruptibly();
            try {
                while (tmpCount.get() < batchMinSize) {
                    notEmpty.await();
                }
                notEmpty.signal();
            } catch (Exception e) {
                // ignore...
            } finally {
                takeLock.unlock();
            }

        } catch (Exception e) {
            // ignore...
        }

        dataCache.drainTo(c);

        int size = c.size();
        for (; size-- > 0;) {
            tmpCount.decrementAndGet();
        }

    }

    public int size() {
        return dataCache.size();
    }
    
    public BlockingQueue<T> getDataCache() {
        return dataCache;
    }

    public int getBatchMinSize() {
        return batchMinSize;
    }

    public void setBatchMinSize(int batchMinSize) {
        this.batchMinSize = batchMinSize;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public Long getOfferTimeoutMs() {
        return offerTimeoutMs;
    }

    public void setOfferTimeoutMs(Long offerTimeoutMs) {
        this.offerTimeoutMs = offerTimeoutMs;
    }


}
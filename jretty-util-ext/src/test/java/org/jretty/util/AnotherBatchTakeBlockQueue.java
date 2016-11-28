package org.jretty.util;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AnotherBatchTakeBlockQueue<T> {

    private int maxCacheSize = 102400;

    private LinkedBlockingQueue<T> dataCache;

    private Long offerTimeoutMs;

    public AnotherBatchTakeBlockQueue() {
        dataCache = new LinkedBlockingQueue<T>(maxCacheSize);
    }

    public boolean offer(T src) throws InterruptedException {
        return dataCache.offer(src);
    }

    public boolean offerMayWait(T src) throws InterruptedException {
        if (offerTimeoutMs != null) {
            return dataCache.offer(src, offerTimeoutMs, TimeUnit.MILLISECONDS);
        } else {
            return dataCache.offer(src);
        }
    }

    public void drainToMayWait(Collection<? super T> c) throws InterruptedException {
        T t = dataCache.take();
        c.add(t);

        dataCache.drainTo(c);
    }
    
    public int size() {
        return dataCache.size();
    }
    
    public BlockingQueue<T> getDataCache() {
        return dataCache;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public Long getOfferTimeoutMs() {
        return offerTimeoutMs;
    }

    public void setOfferTimeoutMs(Long offerTimeoutMs) {
        this.offerTimeoutMs = offerTimeoutMs;
    }

}
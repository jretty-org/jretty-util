package org.jretty.util;

/**
 * 可变的整数，API同AtomicInteger，但非线程安全，效率比AtomicInteger高得多。
 * 
 * @author zollty
 * @since 2016-09-14
 */
public class MutableInteger {

    private int value;

    public MutableInteger() {
    }

    public MutableInteger(int n) {
        value = n;
    }

    /**
     * 获取当前的值
     * @return
     */
    public final int get() {
        return value;
    }

    /**
     * 取当前的值，并设置新的值
     * @param newValue
     * @return
     */
    public final int getAndSet(int newValue) {
        int ret = value;
        value = newValue;
        return ret;
    }

    /**
     * 获取当前的值，并自增
     * @return
     */
    public final int getAndIncrement() {
        return value++;
    }

    /**
     * 获取当前的值，并自减
     * @return
     */
    public final int getAndDecrement() {
        return value--;
    }

    /**
     * 获取当前的值，并加上预期的值
     * @param delta
     * @return
     */
    public final int getAndAdd(int delta) {
        int ret = value;
        value += delta;
        return ret;
    }

}
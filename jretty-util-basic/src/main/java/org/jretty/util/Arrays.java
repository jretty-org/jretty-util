package org.jretty.util;

/* 
 * Copyright (C) 2015-2020 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2015-6-15 (http://blog.zollty.com/, zollty@163.com)
 */
import java.lang.reflect.Array;

/**
 * 注意：通常建议 首选将数组转换成集合Connection来操作，不建议 直接操作数组
 * @see CollectionUtils
 * 
 * @author zollty
 * @since 2015-6-15
 */
@SuppressWarnings("rawtypes")
public class Arrays {

    private Object arr;
    private int arrLen;
    private Object params;
    private int paramLen;
    private Class type;
    private boolean needClone = true;
    
    protected Arrays() {}
    
    public boolean isNotEmpty() {
        return arr != null && arrLen != 0;
    }

    public boolean isEmpty() {
        return arr == null || arrLen == 0;
    }
    
    public Arrays add() {
        Object joinedArray = Array.newInstance(type, arrLen + paramLen);
        System.arraycopy(arr, 0, joinedArray, 0, arrLen);
        System.arraycopy(params, 0, joinedArray, arrLen, paramLen);
        updateArr(joinedArray, arrLen + paramLen);
        return this;
    }
    
    public Arrays remove() {
        int index = ((int[]) params)[0];
        Object result = Array.newInstance(type, arrLen - 1);
        System.arraycopy(arr, 0, result, 0, index);
        if (index < arrLen - 1) {
            System.arraycopy(arr, index + 1, result, index, arrLen - index - 1);
        }
        updateArr(result, arrLen - 1);
        return this;
    }
    
    public String toString() {
        return toString(Const.COMMA);
    }
    
    public String toString(final String delim) {
        if (arr == null || arrLen == 0) {
            return "";
        }
        if (arrLen == 1) {
            return String.valueOf(Array.get(this.arr, 0));
        }
        final StringBuilder sb = new StringBuilder();
        if (type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    if (i != 0) {
                        sb.append(delim);
                    }
                    sb.append(ele);
                    return true;
                }
            });
        } else {
            Object[] objArr =  (Object[]) arr;
            boolean append = false;
            for (int i = 0; i < arrLen; i++) {
                if (append) {
                    sb.append(delim);
                } else {
                    append = true;
                }
                sb.append(objArr[i]);
            }
        }
        return sb.toString();
    }
    
    
    public static Arrays from(Object[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = array.getClass().getComponentType();
        return that;
    }

    public static Arrays from(int[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = int.class;
        return that;
    }

    public static Arrays from(byte[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = byte.class;
        return that;
    }

    public static Arrays from(long[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = long.class;
        return that;
    }

    public static Arrays from(double[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = double.class;
        return that;
    }

    public static Arrays from(float[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = float.class;
        return that;
    }

    public static Arrays from(short[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = short.class;
        return that;
    }

    public static Arrays from(char[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = char.class;
        return that;
    }

    public static Arrays from(boolean[] array) {
        Arrays that = new Arrays();
        if (array == null) {
            return that;
        }
        that.setArr(array, array.length);
        that.type = boolean.class;
        return that;
    }
    
    
    @SuppressWarnings("unchecked")
    public <T> Arrays param(T... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(String... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Integer... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Long... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Double... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Byte... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Character... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Boolean... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Short... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    public Arrays param(Float... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    
    public Arrays param(int... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(byte... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(long... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(double... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(float... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(short... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(char... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }

    public Arrays param(boolean... array) {
        if (array == null) {
            return this;
        }
        setParams(array, array.length);
        return this;
    }
    
    
    public Arrays param(int p) {
        setParams(new int[] {p}, 1);
        return this;
    }
    
    
    public interface Action {
        
        boolean use(Object ele, int index);
    }
    
    protected void loopPrimObject(Action action) {
        // boolean, byte, char, short, int, long, float, and double
        if(type==int.class) {
            int[] ta = (int[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==long.class) {
            long[] ta = (long[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==byte.class) {
            byte[] ta = (byte[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==char.class) {
            char[] ta = (char[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==boolean.class) {
            boolean[] ta = (boolean[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==double.class) {
            double[] ta = (double[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==short.class) {
            short[] ta = (short[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        } else if(type==float.class) {
            float[] ta = (float[]) arr;
            for (int i = 0; i < arrLen; i++) {
                if (!action.use(ta[i], i)) {
                    break;
                }
            }
        }
    }
    

    public Integer[] toIntegerArray() {
        final Integer[] arr0 = new Integer[arrLen];
        if(type==Integer.class) {
            return needClone ? ((Integer[]) arr).clone() : (Integer[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).intValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).intValue();
            }
            return arr0;
        }
    }

    public int[] tointArray() {
        final int[] arr0 = new int[arrLen];
        if (type == int.class) {
            return needClone ? ((int[]) arr).clone() : (int[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).intValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).intValue();
            }
            return arr0;
        }
    }

    public Long[] toLongArray() {
        final Long[] arr0 = new Long[arrLen];
        if (type == Long.class) {
            return needClone ? ((Long[]) arr).clone() : (Long[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).longValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).longValue();
            }
            return arr0;
        }
    }

    public long[] tolongArray() {
        final long[] arr0 = new long[arrLen];
        if (type == long.class) {
            return needClone ? ((long[]) arr).clone() : (long[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).longValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).longValue();
            }
            return arr0;
        }
    }

    public Double[] toDoubleArray() {
        final Double[] arr0 = new Double[arrLen];
        if (type == Double.class) {
            return needClone ? ((Double[]) arr).clone() : (Double[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).doubleValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).doubleValue();
            }
            return arr0;
        }
    }

    public double[] todoubleArray() {
        final double[] arr0 = new double[arrLen];
        if (type == double.class) {
            return needClone ? ((double[]) arr).clone() : (double[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).doubleValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).doubleValue();
            }
            return arr0;
        }
    }
    
    public Float[] toFloatArray() {
        final Float[] arr0 = new Float[arrLen];
        if (type == Float.class) {
            return needClone ? ((Float[]) arr).clone() : (Float[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).floatValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).floatValue();
            }
            return arr0;
        }
    }

    public float[] tofloatArray() {
        final float[] arr0 = new float[arrLen];
        if (type == float.class) {
            return needClone ? ((float[]) arr).clone() : (float[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).floatValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).floatValue();
            }
            return arr0;
        }
    }
    
    public Short[] toShortArray() {
        final Short[] arr0 = new Short[arrLen];
        if (type == Short.class) {
            return needClone ? ((Short[]) arr).clone() : (Short[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).shortValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).shortValue();
            }
            return arr0;
        }
    }

    public short[] toshortArray() {
        final short[] arr0 = new short[arrLen];
        if (type == short.class) {
            return needClone ? ((short[]) arr).clone() : (short[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).shortValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).shortValue();
            }
            return arr0;
        }
    }
    
    public Byte[] toByteArray() {
        final Byte[] arr0 = new Byte[arrLen];
        if (type == Byte.class) {
            return needClone ? ((Byte[]) arr).clone() : (Byte[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).byteValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).byteValue();
            }
            return arr0;
        }
    }

    public byte[] tobyteArray() {
        final byte[] arr0 = new byte[arrLen];
        if (type == byte.class) {
            return needClone ? ((byte[]) arr).clone() : (byte[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ((Number) ele).byteValue();
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = ((Number) ta[i]).byteValue();
            }
            return arr0;
        }
    }
    
    public Character[] toCharacterArray() {
        final Character[] arr0 = new Character[arrLen];
        if (type == Character.class) {
            return needClone ? ((Character[]) arr).clone() : (Character[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = (Character) ele;
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = (Character) ta[i];
            }
            return arr0;
        }
    }

    public char[] tocharArray() {
        final char[] arr0 = new char[arrLen];
        if (type == char.class) {
            return needClone ? ((char[]) arr).clone() : (char[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = (char) ele;
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = (char) ta[i];
            }
            return arr0;
        }
    }

    public Boolean[] toBooleanArray() {
        final Boolean[] arr0 = new Boolean[arrLen];
        if (type == Boolean.class) {
            return needClone ? ((Boolean[]) arr).clone() : (Boolean[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = (Boolean) ele;
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = (Boolean) ta[i];
            }
            return arr0;
        }
    }

    public boolean[] tobooleanArray() {
        final boolean[] arr0 = new boolean[arrLen];
        if (type == boolean.class) {
            return needClone ? ((boolean[]) arr).clone() : (boolean[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = (boolean) ele;
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = (boolean) ta[i];
            }
            return arr0;
        }
    }
    
    public String[] toStringArray() {
        final String[] arr0 = new String[arrLen];
        if (type == String.class) {
            return needClone ? ((String[]) arr).clone() : (String[]) arr;
        } else if(type.isPrimitive()) {
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = String.valueOf(ele);
                    return true;
                }
            });
            return arr0;
        } else {
            Object[] ta = (Object[]) arr;
            for (int i = 0; i < arr0.length; i++) {
                arr0[i] = String.valueOf(ta[i]);
            }
            return arr0;
        }
    }
    
    public Object[] toObjectArray() {
        if (!type.isPrimitive()) {
            return needClone ? ((Object[]) arr).clone() : (Object[]) arr;
        } else {
            final Object[] arr0 = new Object[arrLen];
            loopPrimObject(new Action() {
                @Override
                public boolean use(Object ele, int i) {
                    arr0[i] = ele;
                    return true;
                }
            });
            return arr0;
        }
    }
    
    /**
     * 将原始数组重新随机排序（=洗牌）
     */
    public Arrays shuffle() {
        java.util.Random rand = new java.util.Random();
        int pos;
        if(type.isPrimitive()) {
            // boolean, byte, char, short, int, long, float, and double
            if(type==int.class) {
                int[] ta = (int[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    // 0 ~ r
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    // [pos]已使用，与最后那个未使用的交换
                    int temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==long.class) {
                long[] ta = (long[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    long temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==byte.class) {
                byte[] ta = (byte[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    byte temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==char.class) {
                char[] ta = (char[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    char temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==boolean.class) {
                boolean[] ta = (boolean[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    boolean temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==double.class) {
                double[] ta = (double[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    double temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==short.class) {
                short[] ta = (short[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    short temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            } else if(type==float.class) {
                float[] ta = (float[]) arr;
                for (int r = arrLen - 1; r > 0; r--) {
                    pos = Math.abs(rand.nextInt()) % (r + 1);
                    float temp = ta[pos];
                    ta[pos] = ta[r];
                    ta[r] = temp;
                }
            }
        } else {
            Object[] ta = (Object[]) arr;
            for (int r = arrLen - 1; r > 0; r--) {
                pos = Math.abs(rand.nextInt()) % (r + 1);
                Object temp = ta[pos];
                ta[pos] = ta[r];
                ta[r] = temp;
            }
        }
        return this;
    }
    
    protected Object getArr() {
        return arr;
    }
    
    protected void updateArr(Object arr, int arrLen) {
        setArr(arr, arrLen);
        needClone = false;
    }
    
    protected void setArr(Object arr, int arrLen) {
        this.arr = arr;
        this.arrLen = arrLen;
    }

    protected int getArrLen() {
        return arrLen;
    }

    protected Object getParams() {
        return params;
    }

    protected void setParams(Object params, int paramLen) {
        this.params = params;
        this.paramLen = paramLen;
    }

    protected int getParamLen() {
        return paramLen;
    }

    protected Class getType() {
        return type;
    }

    protected void setType(Class type) {
        this.type = type;
    }

}

package org.jretty.util;

import java.lang.reflect.Array;

/**
 * 注意：通常建议 首选将数组转换成集合Connection来操作，不建议 直接操作数组
 * @see CollectionUtils
 * 
 * @author zollty
 * @since 2015-6-15
 */
public class ArrayUtils {
    
    public static boolean isEquals(Object array1, Object array2) {
        return new EqualsBuilder().append(array1, array2).isEquals();
    }

    /**
     * Convenience method to return a String array as a delimited (e.g. CSV)
     * String. E.g. useful for <code>toString()</code> implementations.
     * @param arr the array to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String (if null return "")
     */
    public static String toString(Object[] arr, String delim) {
        if ( arr == null || arr.length == 0 ) {
            return "";
        }
        if (arr.length == 1) {
            return String.valueOf(arr[0]);
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < arr.length; i++) {
            if(!first) {
                sb.append(delim);
            } else {
                first = false;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a String array as a CSV String.
     * E.g. useful for <code>toString()</code> implementations.
     * @param arr the array to display
     * @return the delimited String (if null return "")
     */
    public static String toString(Object[] arr) {
        return toString(arr, ",");
    }
    
    /**
     * Checks if the array is null or empty (size==0).
     * @return true if is null or empty
     */
    public static boolean isNullOrEmpty(Object[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }
    
    /**
     * Checks if the array is null or empty (size==0).
     * 
     * @return true if is not null and not empty
     */
    public static boolean isNotEmpty(Object[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(int[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(int[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(byte[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(byte[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(long[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(long[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(double[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(double[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(float[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(float[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }
    
    public static boolean isNullOrEmpty(short[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(short[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(char[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(char[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    public static boolean isNullOrEmpty(boolean[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    public static boolean isNotEmpty(boolean[] objs) {
        return (objs != null && objs.length != 0) ? true : false;
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // 以下几个方法 为Array Object type 和 Primitive type 相互转换
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

    public static Integer[] changeType(int[] array) {
        if (array == null) {
            return null;
        }
        Integer[] arr = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static int[] changeType(Integer[] array) {
        if (array == null) {
            return null;
        }
        int[] arr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Long[] changeType(long[] array) {
        if (array == null) {
            return null;
        }
        Long[] arr = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static long[] changeType(Long[] array) {
        if (array == null) {
            return null;
        }
        long[] arr = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Double[] changeType(double[] array) {
        if (array == null) {
            return null;
        }
        Double[] arr = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static double[] changeType(Double[] array) {
        if (array == null) {
            return null;
        }
        double[] arr = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Float[] changeType(float[] array) {
        if (array == null) {
            return null;
        }
        Float[] arr = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static float[] changeType(Float[] array) {
        if (array == null) {
            return null;
        }
        float[] arr = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Short[] changeType(short[] array) {
        if (array == null) {
            return null;
        }
        Short[] arr = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static short[] changeType(Short[] array) {
        if (array == null) {
            return null;
        }
        short[] arr = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Byte[] changeType(byte[] array) {
        if (array == null) {
            return null;
        }
        Byte[] arr = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static byte[] changeType(Byte[] array) {
        if (array == null) {
            return null;
        }
        byte[] arr = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Character[] changeType(char[] array) {
        if (array == null) {
            return null;
        }
        Character[] arr = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static char[] changeType(Character[] array) {
        if (array == null) {
            return null;
        }
        char[] arr = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Boolean[] changeType(boolean[] array) {
        if (array == null) {
            return null;
        }
        Boolean[] arr = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static boolean[] changeType(Boolean[] array) {
        if (array == null) {
            return null;
        }
        boolean[] arr = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    /**
     * 模拟list.add()方法，Append the given String to the given String array, 
     * returning a new array consisting of the input array contents plus the given String.
     * 
     * @param array
     *            the array to append to (can be <code>null</code>)
     * @param str
     *            the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] add(String[] array, String str) {
        if (null == array || array.length == 0) {
            return new String[] { str };
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] array, T element) {
        Class<?> type;
        if (array != null) {
            type = array.getClass();
        }
        else {
            if (element != null)
                type = element.getClass();
            else
                throw new IllegalArgumentException("Arguments cannot both be null");
        }
        T[] newArray = (T[]) copyArrayGrow1(array, type);
        newArray[(newArray.length - 1)] = element;
        return newArray;
    }

    private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
        if (array != null) {
            int arrayLength = Array.getLength(array);
            Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    public static boolean[] add(boolean[] array, boolean integer) {
        if (null == array || array.length == 0) {
            return new boolean[] { integer };
        }
        boolean[] newArr = new boolean[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = integer;
        return newArr;
    }

    public static char[] add(char[] array, char cchar) {
        if (null == array || array.length == 0) {
            return new char[] { cchar };
        }
        char[] newArr = new char[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = cchar;
        return newArr;
    }

    public static byte[] add(byte[] array, byte cchar) {
        if (null == array || array.length == 0) {
            return new byte[] { cchar };
        }
        byte[] newArr = new byte[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = cchar;
        return newArr;
    }

    public static short[] add(short[] array, short cchar) {
        if (null == array || array.length == 0) {
            return new short[] { cchar };
        }
        short[] newArr = new short[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = cchar;
        return newArr;
    }

    public static int[] add(int[] array, int integer) {
        if (null == array || array.length == 0) {
            return new int[] { integer };
        }
        int[] newArr = new int[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = integer;
        return newArr;
    }

    public static long[] add(long[] array, long integer) {
        if (null == array || array.length == 0) {
            return new long[] { integer };
        }
        long[] newArr = new long[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = integer;
        return newArr;
    }

    public static float[] add(float[] array, float cchar) {
        if (null == array || array.length == 0) {
            return new float[] { cchar };
        }
        float[] newArr = new float[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = cchar;
        return newArr;
    }

    public static double[] add(double[] array, double cchar) {
        if (null == array || array.length == 0) {
            return new double[] { cchar };
        }
        double[] newArr = new double[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = cchar;
        return newArr;
    }
    
    public static String[] addAll(String[] array1, String[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        String[] joinedArray = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] addAll(T[] array1, T[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        Class<?> type1 = array1.getClass().getComponentType();

        T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        }
        catch (ArrayStoreException ase) {
            Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
            }

            throw ase;
        }
        return joinedArray;
    }

    public static boolean[] addAll(boolean[] array1, boolean[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        boolean[] joinedArray = new boolean[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static char[] addAll(char[] array1, char[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        char[] joinedArray = new char[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static byte[] addAll(byte[] array1, byte[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static short[] addAll(short[] array1, short[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        short[] joinedArray = new short[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static int[] addAll(int[] array1, int[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        int[] joinedArray = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static long[] addAll(long[] array1, long[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        long[] joinedArray = new long[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static float[] addAll(float[] array1, float[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        float[] joinedArray = new float[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static double[] addAll(double[] array1, double[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return array2.clone();
        }
        if (array2 == null) {
            return array1.clone();
        }
        double[] joinedArray = new double[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
    
    public static String[] remove(String[] array, int index) {
        if (array == null) {
            return null;
        }
        if ((index < 0) || (index >= array.length)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        String[] result = new String[array.length - 1];

        System.arraycopy(array, 0, result, 0, index); // 前半部分
        if (index < array.length - 1) {
            System.arraycopy(array, index + 1, result, index, array.length - index - 1);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] array, int index) {
        return (T[]) doRemove(array, index);
    }

    public static boolean[] remove(boolean[] array, int index) {
        return (boolean[]) doRemove(array, index);
    }

    public static byte[] remove(byte[] array, int index) {
        return (byte[]) doRemove(array, index);
    }

    public static char[] remove(char[] array, int index) {
        return (char[]) doRemove(array, index);
    }

    public static double[] remove(double[] array, int index) {
        return (double[]) doRemove(array, index);
    }

    public static float[] remove(float[] array, int index) {
        return (float[]) doRemove(array, index);
    }

    public static int[] remove(int[] array, int index) {
        return (int[]) doRemove(array, index);
    }

    public static long[] remove(long[] array, int index) {
        return (long[]) doRemove(array, index);
    }

    public static short[] remove(short[] array, int index) {
        return (short[]) doRemove(array, index);
    }

    private static Object doRemove(Object array, int index) {
        if (array == null) {
            return null;
        }

        int length = Array.getLength(array);

        if ((index < 0) || (index >= length)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }
        return result;
    }
     
}

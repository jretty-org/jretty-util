package org.jretty.util;

import java.math.BigDecimal;

/**
 * Lookup: 
 * if ( null == lhs == rhs ) return true
 */
public class EqualsBuilder {
    
    private boolean isEquals = true;

    public EqualsBuilder appendSuper(boolean superEquals) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = superEquals;
        return this;
    }

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        Class<?> lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            if ((lhs instanceof BigDecimal)) {
                this.isEquals = (((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0);
            }
            else {
                this.isEquals = lhs.equals(rhs);
            }
        }
        else if (lhs.getClass() != rhs.getClass()) {
            setEquals(false);
        }
        else if ((lhs instanceof long[])) {
            append((long[]) lhs, (long[]) rhs);
        }
        else if ((lhs instanceof int[])) {
            append((int[]) lhs, (int[]) rhs);
        }
        else if ((lhs instanceof short[])) {
            append((short[]) lhs, (short[]) rhs);
        }
        else if ((lhs instanceof char[])) {
            append((char[]) lhs, (char[]) rhs);
        }
        else if ((lhs instanceof byte[])) {
            append((byte[]) lhs, (byte[]) rhs);
        }
        else if ((lhs instanceof double[])) {
            append((double[]) lhs, (double[]) rhs);
        }
        else if ((lhs instanceof float[])) {
            append((float[]) lhs, (float[]) rhs);
        }
        else if ((lhs instanceof boolean[])) {
            append((boolean[]) lhs, (boolean[]) rhs);
        }
        else {
            append((Object[]) lhs, (Object[]) rhs);
        }
        return this;
    }

    public EqualsBuilder append(long lhs, long rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(int lhs, int rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(short lhs, short rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(char lhs, char rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(byte lhs, byte rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(double lhs, double rhs) {
        if (!this.isEquals) {
            return this;
        }
        return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }

    public EqualsBuilder append(float lhs, float rhs) {
        if (!this.isEquals) {
            return this;
        }
        return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }

    public EqualsBuilder append(boolean lhs, boolean rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(long[] lhs, long[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(int[] lhs, int[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(short[] lhs, short[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(char[] lhs, char[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(double[] lhs, double[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(float[] lhs, float[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (this.isEquals); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    protected void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }
    
}
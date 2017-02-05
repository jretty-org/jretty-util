/* 
 * Copyright (C) 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-6-13 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化工具类（高效的、线程安全的）
 * <br>
 * 1、可以直接用静态方法format，原生底层算法实现，参见 {@link #format()}
 * <br>
 * 2、可以自己new一个，例如new DateFormatUtils(yyyy_MM_dd)并保存为static常量，
 * new的时候可指定格式，后面就可以随时调用format和parse方法，线程安全.
 * <br>
 * 3、附带了一个[获取唯一时间字符串]的方法, 参见 {@link #getUniqueDatePattern_TimeMillis()}
 * <br>
 *  NO SONAR！
 * @author zollty 高效的算法保证
 * @since 2013-6-13
 */
public class DateFormatUtils {

    // 常用常量定义
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss,SSS";

    public static final String dd_MM_yy = "dd-MM-yy";
    public static final String dd_MM_yyyy = "dd-MM-yyyy";
    public static final String dd_MM_yyyy_HH_mm = "dd-MM-yyyy HH:mm";
    public static final String dd_MM_yyyy_HH_mm_ss = "dd-MM-yyyy HH:mm:ss";
    public static final String dd_MM_yyyy_HH_mm_ss_SSS = "dd-MM-yyyy HH:mm:ss,SSS";

    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String HH_mm_ss_SSS = "HH:mm:ss,SSS";

    // 其他格式 可调用 format() 和 formatDate(Date) 方法 自行组装

    /**
     * 自定义日期格式
     * 
     * @param datePattern
     */
    public DateFormatUtils(final String datePattern) {
        threadLocal = new ThreadLocal<SimpleDateFormat>() {
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(datePattern);
            }
        };
    }

    /**
     * 默认为：yyyy-MM-dd HH:mm:ss格式
     */
    public DateFormatUtils() {
        threadLocal = new ThreadLocal<SimpleDateFormat>() {
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
            }
        };
    }

    /**
     * 获取线程安全的DateFormat对象
     */
    public DateFormat getFormat() {
        return (DateFormat) threadLocal.get();
    }

    /**
     * 调用DateFormat对date进行format
     */
    public String format(Date date) {
        return getFormat().format(date);
    }

    /**
     * 调用DateFormat将dateStr解析成Date对象
     */
    public Date parse(String dateStr) {
        try {
            return getFormat().parse(dateStr);
        }
        catch (ParseException e) {
            throw new NestedRuntimeException(e);
        }
    }

    /**
     * 获得一个含详细时间信息的对象，可以根据需要自由组合。
     */
    public static DateInfo format() {
        return uniformat(new Date());
    }

    /**
     * 获得一个含详细时间信息的对象，可以根据需要自由组合。 
     * 例如 组合成 yyyyMMdd 格式： dateInfo.getYyyy()+dateInfo.getMM()+dateInfo.getDd() 
     * 
     * 也可以用如下一些现成的方法：
     * 
     * @see #format_yyyy_MM_dd(Date)
     * @see #format_yyyy_MM_dd_HH_mm_ss(Date)
     * @see #format_yyyy_MM_dd_HH_mm_ss_SSS(Date) 
     * 需要其他格式，请自行添加(参照如上的写法)
     */
    public static DateInfo formatDate(Date date) {
        return uniformat(date);
    }

    /**
     * format成 yyyy-MM-dd 格式
     */
    public static String format_yyyy_MM_dd(Date date) {
        return format_yyyy_MM_dd(date, DateInfo._SEP_MS);
    }

    /**
     * @param spitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
     */
    public static String format_yyyy_MM_dd(Date date, String spitChar) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(10);
        sbu.append(dinfo.year);
        sbu.append(spitChar);
        sbu.append(getMonth(dinfo.month));
        sbu.append(spitChar);
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);
        return sbu.toString();
    }

    public static String format_yyyy_MM_dd_HH_mm(Date date) {
        return format_yyyy_MM_dd_HH_mm_ss(date, DateInfo._SEP_MS);
    }

    /**
     * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
     */
    public static String format_yyyy_MM_dd_HH_mm(Date date, String dateSpitChar) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(20);
        sbu.append(dinfo.year);
        sbu.append(dateSpitChar);
        sbu.append(getMonth(dinfo.month));
        sbu.append(dateSpitChar);
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);

        sbu.append(" ");
        if (dinfo.hour < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.hour);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.mins < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.mins);

        return sbu.toString();
    }

    public static String format_yyyy_MM_dd_HH_mm_ss(Date date) {
        return format_yyyy_MM_dd_HH_mm_ss(date, DateInfo._SEP_MS);
    }

    /**
     * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
     */
    public static String format_yyyy_MM_dd_HH_mm_ss(Date date, String dateSpitChar) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(20);
        sbu.append(dinfo.year);
        sbu.append(dateSpitChar);
        sbu.append(getMonth(dinfo.month));
        sbu.append(dateSpitChar);
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);

        sbu.append(" ");
        if (dinfo.hour < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.hour);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.mins < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.mins);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.secs < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.secs);

        return sbu.toString();
    }

    public static String format_yyyy_MM_dd_HH_mm_ss_SSS(Date date) {
        return format_yyyy_MM_dd_HH_mm_ss_SSS(date, DateInfo._SEP_MS);
    }

    /**
     * @param spitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
     */
    public static String format_yyyy_MM_dd_HH_mm_ss_SSS(Date date, String dateSpitChar) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(24);
        sbu.append(dinfo.year);
        sbu.append(dateSpitChar);
        sbu.append(getMonth(dinfo.month));
        sbu.append(dateSpitChar);
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);

        sbu.append(" ");
        if (dinfo.hour < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.hour);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.mins < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.mins);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.secs < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.secs);

        sbu.append(',');
        long now = dinfo.date.getTime();
        int millis = (int) (now % 1000L);
        if (millis < 100)
            sbu.append('0');
        if (millis < 10) {
            sbu.append('0');
        }
        sbu.append(millis);

        return sbu.toString();
    }

    public static String format_dd_MM_yyyy(Date date) {
        return format_dd_MM_yyyy(date, DateInfo._SEP_MS);
    }

    /**
     * @param spitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
     */
    public static String format_dd_MM_yyyy(Date date, String spitChar) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(10);
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);
        sbu.append(spitChar);
        sbu.append(getMonth(dinfo.month));
        sbu.append(spitChar);
        sbu.append(dinfo.year);
        return sbu.toString();
    }

    public static String format_dd_MM_yyyy_HH_mm_ss(Date date) {
        return format_dd_MM_yyyy_HH_mm_ss(date, DateInfo._SEP_MS);
    }

    /**
     * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
     */
    public static String format_dd_MM_yyyy_HH_mm_ss(Date date, String dateSpitChar) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(20);
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);
        sbu.append(dateSpitChar);
        sbu.append(getMonth(dinfo.month));
        sbu.append(dateSpitChar);
        sbu.append(dinfo.year);

        sbu.append(" ");
        if (dinfo.hour < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.hour);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.mins < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.mins);

        sbu.append(DateInfo._SEP_CO);
        if (dinfo.secs < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.secs);

        return sbu.toString();
    }

    /**
     * 生成一个独一无二的字符串（适用于单服务器），格式为yyMMdd_HHmmssSSS，长度为16，按时间先后. <br>
     * 要想适用于多服务器的话，要加上服务器标识（可为每个服务器设置不同的JVM参数作为“服务器标识”）
     */
    public static String getUniqueDatePattern_TimeMillis() {
        return uniqueDatePattern(new Date(genUniqueDate_TimeMillis()), true);
    }

    /**
     * 生成一个独一无二的字符串（适用于单服务器），格式为yyMMddHHmmssSSS，长度为15，按时间先后. <br>
     * 要想适用于多服务器的话，要加上服务器标识（可为每个服务器设置不同的JVM参数作为“服务器标识”）
     */
    public static String getUniqueDatePattern_TimeMillis_noSplit() {
        return uniqueDatePattern(new Date(genUniqueDate_TimeMillis()), false);
    }

    private static long lastTime = System.currentTimeMillis();
    private static byte[] synO4GUDT = new byte[0];

    /**
     * 产生一个独一无二的long类型的TimeMillis，长度为13，按时间先后 依赖于系统时间的不可重复性（系统时间可以延后，但是不能提前）。
     */
    public static long genUniqueDate_TimeMillis() {
        synchronized (synO4GUDT) {
            long nextTime = System.currentTimeMillis();
            if (nextTime <= lastTime) {
                nextTime = lastTime + 1;
            }
            lastTime = nextTime;
            return nextTime;
        }
    }

    /**
     * 生成一个独一无二的字符串（适用于单服务器），格式为MMddHHmmssSS，长度为12，按时间先后. <br>
     * 要想适用于多服务器的话，要加上服务器标识（可为每个服务器设置不同的JVM参数作为“服务器标识”）
     */
    public static String getShortUniqueDate_TimeMillis() {
        Date date = new Date(genShortUniqueTimeMillis());
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(12);
        sbu.append(getMonth(dinfo.month));
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);
        if (dinfo.hour < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.hour);

        if (dinfo.mins < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.mins);

        if (dinfo.secs < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.secs);

        long now = dinfo.date.getTime();
        int millis = (int) (now % 1000L);
        millis = millis / 10;
        if (millis < 10) {
            sbu.append('0');
            sbu.append(millis);
        }
        else {
            sbu.append(millis);
        }
        return sbu.toString();
    }

    private static long lastTime2 = System.currentTimeMillis();
    private static byte[] synO4GUDT2 = new byte[0];

    /**
     * 产生一个独一无二的long类型的TimeMillis，长度为13，按时间先后
     */
    public static long genShortUniqueTimeMillis() {
        synchronized (synO4GUDT2) {
            long nextTime = System.currentTimeMillis();
            lastTime2 = lastTime2 + 10;
            if (nextTime <= lastTime2) {
                nextTime = lastTime2;
            }
            lastTime2 = nextTime;
            return nextTime;
        }
    }

    ////////////////////////下面是内部实现方法/////////////////////////////////
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected ThreadLocal<SimpleDateFormat> threadLocal;

    ///////////////////////////////////////////////////////////////////////////////
    // 缓存calendar对象，但是使用时要线程同步，实测比每次都new calendar效率要高很多
    private static Calendar calendar = Calendar.getInstance();

    protected static DateInfo uniformat(Date date) {
        DateInfo dinfo = new DateInfo();
        synchronized (calendar) { // 如此同步效率高得多，同步块内没有复杂的运算过程
            calendar.setTime(date);
            dinfo.year = calendar.get(1);
            dinfo.month = calendar.get(2);
            dinfo.day = calendar.get(5);
            dinfo.hour = calendar.get(11);
            dinfo.mins = calendar.get(12);
            dinfo.secs = calendar.get(13);
        }
        dinfo.date = date;
        return dinfo;
    }

    public static String getMonth(int m) {
        String month = null;
        switch (m) {
        case 0:
            month = "01";
            break;
        case 1:
            month = "02";
            break;
        case 2:
            month = "03";
            break;
        case 3:
            month = "04";
            break;
        case 4:
            month = "05";
            break;
        case 5:
            month = "06";
            break;
        case 6:
            month = "07";
            break;
        case 7:
            month = "08";
            break;
        case 8:
            month = "09";
            break;
        case 9:
            month = "10";
            break;
        case 10:
            month = "11";
            break;
        case 11:
            month = "12";
            break;
        default:
            month = "NA";
        }
        return month;
    }

    /**
     * yyMMdd_HHmmssSSS 定制格式，主要用于获取唯一的时间字符串
     */
    protected static String uniqueDatePattern(Date date, boolean split) {
        DateInfo dinfo = uniformat(date);
        StringBuilder sbu = new StringBuilder(16);
        sbu.append(dinfo.year % 100);
        sbu.append(getMonth(dinfo.month));
        if (dinfo.day < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.day);
        if (split)
            sbu.append(DateInfo._SEP_MS);
        if (dinfo.hour < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.hour);

        if (dinfo.mins < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.mins);

        if (dinfo.secs < 10) {
            sbu.append('0');
        }
        sbu.append(dinfo.secs);

        long now = dinfo.date.getTime();
        int millis = (int) (now % 1000L);
        if (millis < 100)
            sbu.append('0');
        if (millis < 10) {
            sbu.append('0');
        }
        sbu.append(millis);

        return sbu.toString();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * 存放最原始、最详细的时间信息，可根据需要进行自由组合 例如 组合成 yyyyMMdd 格式： getYyyy()+getMM()+getDd()
     * 
     * @author zollty
     */
    public static class DateInfo {
        public static final String _SEP_MS = "-";
        public static final String _SEP_SL = "/";
        public static final String _SEP_CO = ":";

        public int year;
        public int month;
        public int day;
        public int hour;
        public int mins;
        public int secs;
        public Date date;

        private String yyyy;
        private String MM;
        private String dd;
        private String HH;
        private String mm;
        private String ss;
        private String sss;

        /**
         * @return the yyyy
         */
        public String getYyyy() {
            yyyy = Integer.toString(year);
            return yyyy;
        }

        /**
         * @return the mM
         */
        public String getMM() {
            MM = DateFormatUtils.getMonth(month);
            return MM;
        }

        /**
         * @return the dd
         */
        public String getDd() {
            if (day < 10) {
                dd = "0" + Integer.toString(day);
            }
            else {
                dd = Integer.toString(day);
            }
            return dd;
        }

        /**
         * @return the hH
         */
        public String getHH() {
            if (hour < 10) {
                HH = "0" + Integer.toString(hour);
            }
            else {
                HH = Integer.toString(hour);
            }
            return HH;
        }

        /**
         * @return the mm
         */
        public String getMm() {
            if (mins < 10) {
                mm = "0" + Integer.toString(mins);
            }
            else {
                mm = Integer.toString(mins);
            }
            return mm;
        }

        /**
         * @return the ss
         */
        public String getSs() {
            if (secs < 10) {
                ss = "0" + Integer.toString(secs);
            }
            else {
                ss = Integer.toString(secs);
            }
            return ss;
        }

        /**
         * @return the sSS
         */
        public String getSss() {
            long now = date.getTime();
            int millis = (int) (now % 1000L);
            sss = "";
            if (millis < 100) {
                sss = "0";
            }
            if (millis < 10) {
                sss = sss + "0";
            }
            sss = sss + Integer.toString(millis);
            return sss;
        }
    }

}
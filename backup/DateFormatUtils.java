/**
 * format成 yyyy-MM-dd 格式
 */
public static String formatYyyyMmDd(long date) {
    return formatYyyyMmDd(date, DateInfo.SEP_MS);
}
}

public static String formatYyyyMmDd(Date date) {
    return formatYyyyMmDd(date.getTime(), DateInfo.SEP_MS);
}

/**
 * @param spitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
 */
public static String formatYyyyMmDd(long date, String spitChar) {
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

public static String formatYyyyMmDdHhMm(long date) {
    return formatYyyyMmDdHhMm(date, DateInfo.SEP_MS);
}

public static String formatYyyyMmDdHhMm(Date date) {
    return formatYyyyMmDdHhMm(date.getTime(), DateInfo.SEP_MS);
}

/**
 * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
 */
public static String formatYyyyMmDdHhMm(long date, String dateSpitChar) {
    return format_yyyy_MM_dd_HH_mm(date, dateSpitChar);
}

public static String formatYyyyMmDdHhMmSs(long date) {
    return formatYyyyMmDdHhMmSs(date, DateInfo.SEP_MS);
}

public static String formatYyyyMmDdHhMmSs(Date date) {
    return formatYyyyMmDdHhMmSs(date.getTime(), DateInfo.SEP_MS);
}

/**
 * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
 */
public static String formatYyyyMmDdHhMmSs(long date, String dateSpitChar) {
    return format_yyyy_MM_dd_HH_mm_ss(date, dateSpitChar);
}

public static String formatYyyyMmDdHhMmSsSss(long date) {
    return format_yyyy_MM_dd_HH_mm_ss_SSS(date, DateInfo.SEP_MS);
}

public static String formatYyyyMmDdHhMmSsSss(Date date) {
    return format_yyyy_MM_dd_HH_mm_ss_SSS(date.getTime(), DateInfo.SEP_MS);
}

/**
 * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
 */
public static String formatYyyyMmDdHhMmSsSss(long date, String dateSpitChar) {
    return format_yyyy_MM_dd_HH_mm_ss_SSS(date, dateSpitChar);
}

public static String formatDdMmYyyy(long date) {
    return formatDdMmYyyy(date, DateInfo.SEP_MS);
}

public static String formatDdMmYyyy(Date date) {
    return formatDdMmYyyy(date.getTime(), DateInfo.SEP_MS);
}

/**
 * @param spitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
 */
public static String formatDdMmYyyy(long date, String spitChar) {
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

public static String formatDdMmYyyyHhMmSs(Date date) {
    return formatDdMmYyyyHhMmSs(date.getTime(), DateInfo.SEP_MS);
}

public static String formatDdMmYyyyHhMmSs(long date) {
    return formatDdMmYyyyHhMmSs(date, DateInfo.SEP_MS);
}

/**
 * @param dateSpitChar 指定分割年月日的字符，常用的分隔符有"-"、"/"
 */
public static String formatDdMmYyyyHhMmSs(long date, String dateSpitChar) {
    return format_dd_MM_yyyy_HH_mm_ss(date, dateSpitChar);
}
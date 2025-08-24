package org.jretty.util;

/**
 * 代替 System.out等方法打印日志。
 * 
 * <p> 因为System.out虽然方便，但是不可控，而且在Eclipse等控制台打印时字符串超长还无法显示（参见：https://bugs.eclipse.org/bugs/show_bug.cgi?id=547393）。
 * <p> 使用本工具类，可以有全局开关，而且可以重定向输出到Log4j等日志库。
 * 
 * @author zollty
 * @since 2021年6月17日
 */
public class SimpleLogger {
    public static final LogImpl SYSOUT = new LogImpl() {
        @Override
        public void log(String str, boolean isError, boolean newLine) {
            if (newLine) {
                if (!isError) {
                    System.out.println(str);
                } else {
                    System.err.println(str);
                }
            } else {
                System.out.print(str);
            }
        }
    };
    protected boolean closed = false;
    private static final String MSG_SPLIT = " |- ";
    private String classNameLayout;
    private boolean showThread;
    private int limit = 32000;
    protected boolean addMeta;
    private LogImpl logImpl = SYSOUT;
    private final String className;

    public SimpleLogger(String className) {
        this.className = className;
        this.addMeta = true;
    }

    public SimpleLogger(String className, boolean addMeta) {
        this.className = className;
        this.addMeta = addMeta;
    }

    public SimpleLogger() {
        this.className = null;
        this.addMeta = false;
    }

    public void close() {
        this.closed = true;
    }
    
    public boolean isEnabled() {
        return !this.closed;
    }
    
    public static interface LogImpl {
        void log(String str, boolean isError, boolean newLine);
    }

    private void add(Object message, Throwable throwable) {
        add(message, false, true, throwable);
    }

    private void addError(Object message, Throwable throwable) {
        add(message, true, true, throwable);
    }

    private void addOneLine(Object message, Throwable throwable) {
        add(message, false, false, throwable);
    }

    protected void add(Object message, boolean isError, boolean newLine, Throwable throwable) {
        if (closed) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (!addMeta) {
            if (message != null) {
                if (throwable != null) {
                    sb.append(message.toString()).append(MSG_SPLIT).append(ExceptionUtils.getStackTraceStr(throwable));
                } else {
                    sb.append(message.toString());
                }
            } else if (throwable != null) {
                sb.append(ExceptionUtils.getStackTraceStr(throwable));
            } else {
                sb.append(Const.STRING_EMPTY);
            }
        } else {
            sb.append(DateFormatUtils.format_dd_MM_yyyy_HH_mm_ss(System.currentTimeMillis(), DateFormatUtils.DateInfo.SEP_MS));
            if (showThread) {
                sb.append(" [");
                sb.append(Thread.currentThread().getName());
                sb.append("]");
            }
            if ("simple".equals(classNameLayout)) {
                sb.append(stripToSimpleClassName(className));
            } else {
                sb.append(className);
            }
            // sb.append(" - ");
            if (message != null) {
                sb.append(MSG_SPLIT);
                sb.append(message.toString());
            }
            if (throwable != null) {
                sb.append(MSG_SPLIT);
                sb.append(ExceptionUtils.getStackTraceStr(throwable));
            }
        }
        if (sb.length() <= limit) {
            // System.out.println(sb.toString());
            logImpl.log(sb.toString(), isError, newLine);
        } else {
            // System.out.println(sb.replace(limit - 4, sb.length(), "....").toString());
            logImpl.log(sb.replace(limit - 32, sb.length(), "....(len="+sb.length()+")").toString(), isError, newLine);
        }
    }

    public String getClassNameLayout() {
        return classNameLayout;
    }

    public void setClassNameLayout(String classNameLayout) {
        this.classNameLayout = classNameLayout;
    }

    public void setShowThread(boolean showThread) {
        this.showThread = showThread;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setAddMeta(boolean addMeta) {
        this.addMeta = addMeta;
    }
    
    public LogImpl getLogImpl() {
        return logImpl;
    }
    
    public void setLogImpl(LogImpl logImpl) {
        this.logImpl = logImpl;
    }

    // --------- helper methods for this class-------
    private static String stripToSimpleClassName(String className) {
        int pos = className.lastIndexOf(".") + 1;
        return className.substring(pos, className.length());
    }

    public void print(String message) {
        addOneLine(message, null);
    }

    public void log(Object message) {
        add(message, null);
    }

    public void log(Throwable e) {
        add(null, e);
    }

    public void log(Throwable e, Object message) {
        add(message, e);
    }

    public void log(Object message, Object... msgParams) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, msgParams), null);
    }

    public void log(Throwable e, Object message, Object... msgParams) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, msgParams), e);
    }

    public void log(Object message, Object p0) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0), null);
    }

    public void log(Object message, Object p0, Object p1) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1), null);
    }

    public void log(Object message, Object p0, Object p1, Object p2) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2), null);
    }

    public void log(Object message, Object p0, Object p1, Object p2, Object p3) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3), null);
    }

    public void log(Object message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4), null);
    }

    public void log(Object message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4, p5), null);
    }

    public void log(Object message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4, p5, p6), null);
    }

    public void log(Object message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
            Object p7) {
        add(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4, p5, p6, p7),
                null);
    }

    public void err(Object message) {
        addError(message, null);
    }

    public void err(Throwable e) {
        addError(null, e);
    }

    public void err(Throwable e, Object message) {
        addError(message, e);
    }

    public void err(Object message, Object... msgParams) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, msgParams), null);
    }

    public void err(Throwable e, Object message, Object... msgParams) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, msgParams), e);
    }

    public void err(Object message, Object p0) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0), null);
    }

    public void err(Object message, Object p0, Object p1) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1), null);
    }

    public void err(Object message, Object p0, Object p1, Object p2) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2), null);
    }

    public void err(Object message, Object p0, Object p1, Object p2, Object p3) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3), null);
    }

    public void err(Object message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4), null);
    }

    public void err(Object message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4, p5), null);
    }

    public void err(Object message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4, p5, p6),
                null);
    }

    public void err(Object message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
            Object p7) {
        addError(StringUtils.replaceParams(message != null ? message.toString() : null, p0, p1, p2, p3, p4, p5, p6, p7),
                null);
    }

}

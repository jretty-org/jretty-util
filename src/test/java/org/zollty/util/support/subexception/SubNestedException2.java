package org.zollty.util.support.subexception;

import org.zollty.util.NestedCheckedException;

public class SubNestedException2 extends NestedCheckedException {

    private static final long serialVersionUID = -7351301908998106319L;
    
    public static final String EXCEPTION_PRIFIX = "org.zollty.SubNestedException222: ";

    public SubNestedException2(Throwable e) {
        super(e);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

    /**
     * @param e
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public SubNestedException2(Throwable e, String message, String... args) {
        super(e, message, args);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

    /**
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public SubNestedException2(String message, String... args) {
        super(message, args);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

}

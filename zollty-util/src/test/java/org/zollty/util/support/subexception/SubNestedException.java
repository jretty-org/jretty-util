package org.zollty.util.support.subexception;

import org.zollty.util.NestedCheckedException;

public class SubNestedException extends NestedCheckedException {

    private static final long serialVersionUID = -4736985020926462922L;
    
    public static final String EXCEPTION_PRIFIX = "org.zollty.SubNestedException: ";

    public SubNestedException(Throwable e) {
        super(e);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

    /**
     * @param e
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public SubNestedException(Throwable e, String message, String... args) {
        super(e, message, args);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

    /**
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public SubNestedException(String message, String... args) {
        super(message, args);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

}

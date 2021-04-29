package org.jretty.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;

public class DateFormatUtilsTest {

    public static void main(String[] args) throws Exception {
        final Date date = new Date();
        // 效率最高，且高得多，而且线程安全
        System.out.println(DateFormatUtils.format_yyyy_MM_dd_HH_mm_ss(date));
    }
    
    public static void main1(String[] args) throws Exception {
        final Date date = new Date();

        // 效率最高，且高得多，而且线程安全
        System.out.println(DateFormatUtils.format_yyyy_MM_dd_HH_mm_ss(date));
        
        // 效率最低
        System.out.println(new SimpleDateFormat(DateFormatUtils.yyyy_MM_dd_HH_mm_ss).format(date));
        
        System.out.println(new DateFormatUtils().format(date));
        
        loopExecute(100); // 预热CPU

        loopExecute(100000); // 正式测试
    }
    
    // 循环执行
    static void loopExecute(final int times) throws Exception {
        final Date date = new Date();
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                DateFormatUtils.format_yyyy_MM_dd_HH_mm_ss(date);
            }
        }, "DateFormatUtils-01");
        
        final SimpleDateFormat sformat = new SimpleDateFormat(DateFormatUtils.yyyy_MM_dd_HH_mm_ss);
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                sformat.format(date);
            }
        }, "SimpleDateFormat-01");

        final DateFormatUtils format = new DateFormatUtils();
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                format.format(date);
            }
        }, "DateFormatUtils-02");
    }

}
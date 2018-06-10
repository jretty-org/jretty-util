/**
 * 
 */
package org.jretty.indexIgnoreCase;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 测试结论：
 *  Str03.indexIgnoreCase 速度最快、最稳定
 * @author zollty
 * @since 2018年5月29日
 */
@Ignore
public class Tests30 {
    static StringBuilder LONG_STR = Tests.LONG_STR;

    @Test
    public void t00() throws Exception {
        String t = "aaaaaaaaaa<table></table>aaa<table></table>MySqlE";
        String s = "<table";
        doTest(1000000, t, s);
        System.out.println("-------------------------t00");
    }

    @Test
    public void t01() throws Exception {
        String t = "aaaaaaaaaa<table></table>aaa<table></table>" + LONG_STR;
        String s = "<table";// "<TABLE";
        doTest(1000, t, s);
        System.out.println("-------------------------t01");
    }
    
    @Test
    public void t02() throws Exception {
        String t = "aaaaaaaaaa<table></table>aaa<table></table>" + LONG_STR;
        String s = "<TABLE";
        doTest(1000, t, s);
        System.out.println("-------------------------t02");
    }
    
    @Test
    public void t03() throws Exception {
        String t = "aaaaaaaaaa<table></table>aaa<table></table>" 
                + LONG_STR + "MySqlE";
        String s = "mysqle";
        doTest(100000, t, s);
        System.out.println("-------------------------t03");
    }
    
    @Test
    public void t04() throws Exception {
        String t = "aaaaaaaaaa<table></table>aaa<table></table>" 
                + LONG_STR + "MySqlE";
        String s = "xXxXxXxX";
        doTest(1000, t, s);
        System.out.println("-------------------------t04");
    }
    
    
    static void doTest(final int n, final String t, final String s) throws Exception {
        System.out.println(t.length());
        System.out.println(t.lastIndexOf(s));
        System.out.println(Str021.lastIndexIgnoreCase(t, s));
        System.out.println(Str031.lastIndexIgnoreCase(t, s));
        System.out.println(Str041.lastIndexIgnoreCase(t, s));

        TestTools.loopExecute(new LoopExecute() {
            @Override
            public int getLoopTimes() {
                return n;
            }
            @Override
            public void execute() throws Exception {
                Str041.lastIndexIgnoreCase(t, s);
            }
        });
        TestTools.loopExecute(new LoopExecute() {
            @Override
            public int getLoopTimes() {
                return n;
            }
            @Override
            public void execute() throws Exception {
                Str021.lastIndexIgnoreCase(t, s);
            }
        });
        TestTools.loopExecute(new LoopExecute() {
            @Override
            public int getLoopTimes() {
                return n;
            }
            @Override
            public void execute() throws Exception {
                Str031.lastIndexIgnoreCase(t, s);
            }
        });
    }

}

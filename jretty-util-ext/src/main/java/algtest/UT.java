/**
 * 
 */
package algtest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 
 * @author zollty
 * @since 2021-1-9
 */
public class UT {
    
    public static void main(String[] args) {
//        testGj1();
// testKaiJuShangHai2();
testKaiJuShangHai3();
//        test0();
    }
    
    public static double round(double f, int scale) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(scale, RoundingMode.HALF_UP).doubleValue();
        return f1;
    }
    
    // 测试对象：帕米拉
    static void testHp1() {
        long totalSxHp = totalSxHp(1117699, (55138 + 32574), (52449 + 32367), 0, 9240);
        double otherBfbHp = otherBfbHp(0.5, 0.32, 0.14, 0.26, 0, 0);
        long curHp = curHp(totalSxHp, 0.35, otherBfbHp);
        System.out.println(curHp);
        long realVal = 3894501;
        System.out.println("误差：" + (curHp-realVal));
    }
    
    // 测试对象：帕米拉
    static void testGj1() {
        long totalSxGj = totalSxGj(53943, (4521 + 1659), (3704 + 2469), 0, 0);
        double otherBfbGj = otherBfbGj(0.65, 0, 0.18, 0.26, 0, 0.0);
        long curGj = curGj(totalSxGj, 0.38, otherBfbGj);
        System.out.println(curGj);
        long realVal = 191210;
        System.out.println("误差：" + (curGj-realVal));
    }
    
    /**
     * 英雄开局攻击力=（面板攻击力+战车攻击数值*技能及天赋加成）* 家园科技加成 * 阵容光环加成。
     * （换成血量也一样，面板攻击力 的计算公式见上文）
     */
    public static long shijiGj(long curGj, double jntfBfb, long zhancheGJZ, double jiayuanBfb, double zhenrGuanghuan) {
        return (long) Math.floor((curGj + zhancheGJZ * jntfBfb) * (1 + jiayuanBfb) * (1 + zhenrGuanghuan));
    }
    
    /**
     * 面板总血量  = 
     * （英雄初始属性血量+基地属性加成血量+装备属性基础血量+宝石属性基础血量+其他属性基础血量） * 
     *      （技能、天赋血量百分比加成 之和） * 
     *          （公会科技、基地、宝石、饰品、装备、皮肤等血量百分比加成 之和）
     */
    public static long curHp(long totalShuxing, double jntfBfb, double otherBfb) {
        return (long) Math.floor(totalShuxing * (1+jntfBfb) * (1+otherBfb));
    }
    
    public static long totalSxHp(long chushizhi, long jidi, long zhuangbei, long baoshi, long shipin) {
        return chushizhi + jidi + zhuangbei + baoshi + shipin;
    }
    
    public static double otherBfbHp(double gongh, double baos, double ship, double zhuangb, double pif, double toux) {
        return gongh + baos + ship + zhuangb + pif + toux;
    }
    
    /**
     * @see #curHp(long, double, double)
     */
    public static long curGj(long totalShuxing, double jntfBfb, double otherBfb) {
        return (long) Math.floor(totalShuxing * (1+jntfBfb) * (1+otherBfb));
    }
    
    public static long totalSxGj(long chushizhi, long jidi, long zhuangbei, long baoshi, long shipin) {
        return chushizhi + jidi + zhuangbei + baoshi + shipin;
    }
    
    public static double otherBfbGj(double gongh, double baos, double ship, double zhuangb, double pif, double toux) {
        return gongh + baos + ship + zhuangb + pif + toux;
    }
    
    // 测试对象：帕米拉
    static void testKaiJuShangHai1() {
        long curGj = 191210; // 192125
        long shijiGj = shijiGj(curGj, 0.38, 10547, 0.2, 0.0);
        System.out.println(shijiGj);
        shijiGj = shijiGj(curGj, 0.0, 0, 0.2, 0.0);
        System.out.println(shijiGj);
        
        //474999 445078
        //478957
        
        //475273
        // 720245
        //714293
        double dJS = dJS(0.05, 1973, 300, 0.12);
        System.out.println(dJS);
        
        long putongSH = putongSH(2.0, shijiGj, dJS, 0, 0.3, 0, false);
        long realVal=445078;//计算结果偏低
        System.out.println(putongSH);
        System.out.println("误差：" + (putongSH-realVal));
    }
    
    // 测试对象：帕米拉 vs 小黄人
    static void testKaiJuShangHai2() {
        long curGj = 191210; // 192125
        long shijiGj = shijiGj(curGj, 0.0, 0, 0.2, 0.0);
        System.out.println(shijiGj);
        shijiGj = 229452 - 8125;
        
        double dJS = dJS(0.0, 1912, 300, 0.12);
        
        long putongSH = putongSH(2.0, shijiGj, dJS, 0, 0.3, 0, false);
        long realVal=455709;
        System.out.println(putongSH);  // 451149
        System.out.println("误差：" + (putongSH-realVal));
    }
    
    // 测试对象：莉莉丝 vs 小黄人
    static void testKaiJuShangHai3() {
        long curGj = 180492;
        long shijiGj = shijiGj(curGj, 0.0, 0, 0.2, 0.0);
        System.out.println(shijiGj);
        shijiGj = 216590 + 6589;
        
        double dJS = dJS(0.0, 1912, 300, 0.0);
        
        long putongSH = putongSH(1.0, shijiGj, dJS, 0, 0.2, 0, false);
        long realVal=243846;
        System.out.println(putongSH);  // 236216
        System.out.println("误差：" + (putongSH-realVal));
    }
    
    /**
     * 例如船锚科迈罗打圣堂
科迈罗的当前回合实际攻击力为20万，技伤280%，暴伤15%，破甲100%，能量160%，真伤60%，
圣堂护甲减伤率30%，自身减伤率55%。
那么科迈罗暴击普攻伤害为20*（1-30%（1-100%））*（1-55%）*160%*（2+15%）+20*60%*2+20*（1-30%（1-100%））*（1-55%）*160%*（2+15%）*30%=62.248万。
     */
    static void test0() {
        double dJS = dJS(0.12, 664, 100, 0.15);
        System.out.println(dJS);
        double pgSHL=1.0;
        long curGj=6976;
        long shijiGj = shijiGj(curGj, 0.0, 0, 0.2, 0.0);
        double nenglYichu=0.0;
        double realSHL = 0.12;
        long zhiyeZsl =0;
        boolean zyXK=false;
        long ret = putongSH(pgSHL, shijiGj, dJS, nenglYichu, realSHL, zhiyeZsl, zyXK);
        
        System.out.println(ret);
        
        //ret = putongSHWithBaoj(0.15, pgSHL, shijiGj, dJS, nenglYichu, realSHL, zhiyeZsl, zyXK);

        //System.out.println(ret);
    }
    
    /**
     * 例如船锚科迈罗打圣堂
科迈罗的当前回合实际攻击力为20万，技伤280%，暴伤15%，破甲100%，能量160%，真伤60%，
圣堂护甲减伤率30%，自身减伤率55%。
那么科迈罗暴击普攻伤害为20*（1-30%（1-100%））*（1-55%）*160%*（2+15%）+20*60%*2+20*（1-30%（1-100%））*（1-55%）*160%*（2+15%）*30%=62.248万。
     */
    static void test1() {
        double selfJS=0.55;
        double hjJS = 0.3; 
        double pojia=1;
        double dJS = dJS(selfJS, hjJS, pojia);
        System.out.println(dJS);
        double pgSHL=1;
        long shijiGj=200000;
        double nenglYichu=0.6;
        double realSHL = 0.6;
        long zhiyeZsl =0;
        boolean zyXK=true;
        long ret = putongSH(pgSHL, shijiGj, dJS, nenglYichu, realSHL, zhiyeZsl, zyXK);
        
        System.out.println(ret);
        
        ret = putongSHWithBaoj(0.15, pgSHL, shijiGj, dJS, nenglYichu, realSHL, zhiyeZsl, zyXK);

        System.out.println(ret);
    }
    
    public static long putongSHWithBaoj(double baoSl, double pgSHL, long shijiGj, double dJS, double nenglYichu, double realSHL, long zhiyeZsl, boolean zyXK) {
        double jichuSH = jichuSH(shijiGj, dJS, nenglYichu) * (2 + baoSl);
        return (long) Math.floor(jichuSH * pgSHL + fujiaSH(jichuSH, shijiGj, realSHL, zhiyeZsl, zyXK));
    }
    
    public static double dJS(double selfJS, double hjJS, double pojia) {
        return (1 - selfJS) * (1 - hjJS * (1 - pojia));
    }
    
    /**
     * 总减伤率 =（1－D自身减伤率）*（1－D护甲减伤率*（1-A破甲率））
     * 
     * @param selfJS 自身减伤率，即面板的减伤率，包括技能、天赋、饰品和装备的减伤率。和护甲减伤是单独计算。
     * @param hujia 护甲值
     * @param lv 英雄等级
     * @param duiShouPojia 破甲率，固定百分比抵消目标值的护甲，破甲达到100%以上也有效，会增伤。
     */
    public static double dJS(double selfJS, long hujia, int lv, double duiShouPojia) {
        // 护甲减伤率 = 护甲 / （200+20*（英雄等级-1））
        double hjJS = hujia * 1.0 / (200 + 20 * (lv - 1));
        // double hjJS = hujia * 1.0 / (220 + 20 * lv);
        // System.out.println("hjJS=" + hjJS);
        double ret = (1 - selfJS) * (1 - hjJS * (1 - duiShouPojia));
        // ret = 0.725845;
        // 0.7277411003236246
        // 0.6930384
        // System.out.println("dJS=" + ret);
        return ret;
    }
    
    /**
     * 大招实际伤害 ＝ 附加伤害 + 基础伤害*技能伤害率
     * @param dzSHL 大招伤害率
     */
    public static long dazhaoSH(double dzSHL, long shijiGj, double dJS, double nenglYichu, double realSHL, long zhiyeZsl, boolean zyXK) {
        double jichuSH = jichuSH(shijiGj, dJS, nenglYichu);
        return (long) Math.floor(jichuSH * dzSHL + fujiaSH(jichuSH, shijiGj, realSHL, zhiyeZsl, zyXK));
    }
    
    /**
     * 普攻实际伤害 ＝ 附加伤害 + 基础伤害*普攻伤害率
     * @param pgSHL 普攻伤害率
     */
    public static long putongSH(double pgSHL, long shijiGj, double dJS, double nenglYichu, double realSHL, long zhiyeZsl, boolean zyXK) {
        double jichuSH = jichuSH(shijiGj, dJS, nenglYichu);
        return (long) Math.floor(jichuSH * pgSHL + fujiaSH(jichuSH, shijiGj, realSHL, zhiyeZsl, zyXK));
    }

    /**
     * 基础伤害 = A当前实际攻击力 * D减伤 * 能量溢出百分比
     * 
     * @param shijiGj 实际攻击力
     * @param dJS D减伤（防御方总减伤率）
     * @param nenglYichu 能量溢出百分比
     */
    public static double jichuSH(long shijiGj, double dJS, double nenglYichu) {
        return (shijiGj * dJS * (1 + nenglYichu));
    }
    
    /**
     * 附加伤害 ＝ 真实伤害 + 阵营相克伤害 + 职业相克伤害 
     * <p>真实伤害 = A当前实际攻击力 * 2 * 真伤百分比 
     * <p>阵营相克伤害 = 基础伤害 * 30% 
     * <p>职业相克伤害 = 基础伤害 * 职业相克率
     * 
     * @param jichuSH 基础伤害
     * @param shijiGj 实际攻击力
     * @param realSHL 真实伤害百分比
     * @param zhiyeZsl 职业增伤率 （饰品+默认+15%命中+30%伤害）混沌 > 秩序 > 邪恶 > 守护 > 混沌
     */
    public static double fujiaSH(double jichuSH, long shijiGj, double realSHL, long zhiyeZsl, boolean zyXK) {
        double zyXKl = zyXK ? 0.3 : 0;
        return (shijiGj * 2 * realSHL + jichuSH * (zyXKl + zhiyeZsl));
    }

}

package org.jretty.util;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zollty
 * @since 2014-6-12
 */
public class ObjectUtilsTest {

    @Test
    public void testInitValue() {
        Bar1 obj = new Bar1();
        ObjectUtils.initValue(obj);
        Assert.assertEquals("", obj.getHadValue());
        //System.out.println(obj);
        
        obj = new Bar1();
        ObjectUtils.initValue(obj, false);
        Assert.assertEquals("defaultval", obj.getHadValue());
        
        //System.out.println(obj);
        
        
        Bar2 bar2 = new Bar2();
        
        try {
            ObjectUtils.initValue(bar2);
            Assert.fail("assert fail but success!");
        } catch (Exception e) {
        }
       
        try {
            ObjectUtils.initValue(bar2, true, true);
            //System.out.println(bar2);
        } catch (Exception e) {
            Assert.fail("assert success but fail!");
        }
        
    }
    
    
    class Bar2 {
        private String aa;
        private Integer bb;
        
        String nosetter;
        public String getNosetter() {
            return nosetter;
        }
        

        public String getAa() {
            return aa;
        }

        public void setAa(String aa) {
            this.aa = aa;
        }

        public Integer getBb() {
            return bb;
        }

        public void setBb(Integer bb) {
            this.bb = bb;
        }

        @Override
        public String toString() {
            return "Bar2 [" + (aa != null ? "aa=" + aa + ", " : "") + (bb != null ? "bb=" + bb + ", " : "")
                    + (nosetter != null ? "nosetter=" + nosetter : "") + "]";
        }
        
    }
    
    class Foo { // for test
        private Long foolong;
        protected Byte foobbb;
        public Long getFoolong() {
            return foolong;
        }
        public void setFoolong(Long foolong) {
            this.foolong = foolong;
        }
    }
    
    class Bar1 extends Foo {
        String aa;

        private Integer bb;
        private Long cc;
        private Byte dd;
        private Short ee;
        private Float ff;
        private Double gg;
        private Byte hh;
        private Boolean ii;

        private Date date;

        private BigDecimal dec;
        
        private boolean test;
        private byte by;
        
        private String hadValue = "defaultval";
        protected Date nosetterAndGetter; // ignore no setter and getter.

        private Date nogetter; // allow no getter
        public void setNogetter(Date nogetter) {
            this.nogetter = nogetter;
            if(this.nogetter==null);
        }
        
        public void getAaa(){ // 干扰method
            // for test
        }
        
        void getBbb(){ // 干扰method
            // for test
        }
        
        protected void setCcc(String ccc){ // 干扰method
            // for test
        }
        
        public Byte getFoobbb() {
            return foobbb;
        }
        public void setFoobbb(Byte foobbb) {
            this.foobbb = foobbb;
        }
        

        public void setAa(String aa) {
            this.aa = aa;
        }

        public void setBb(Integer bb) {
            this.bb = bb;
        }

        public void setCc(Long cc) {
            this.cc = cc;
        }

        public void setDd(Byte dd) {
            this.dd = dd;
        }

        public void setEe(Short ee) {
            this.ee = ee;
        }

        public void setFf(Float ff) {
            this.ff = ff;
        }

        public void setGg(Double gg) {
            this.gg = gg;
        }

        public void setHh(Byte hh) {
            this.hh = hh;
        }

        public void setIi(Boolean ii) {
            this.ii = ii;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setDec(BigDecimal dec) {
            this.dec = dec;
        }

        public String getAa() {
            return aa;
        }

        public Integer getBb() {
            return bb;
        }

        public Long getCc() {
            return cc;
        }

        public Byte getDd() {
            return dd;
        }

        public Short getEe() {
            return ee;
        }

        public Float getFf() {
            return ff;
        }

        public Double getGg() {
            return gg;
        }

        public Byte getHh() {
            return hh;
        }

        public Boolean getIi() {
            return ii;
        }

        public Date getDate() {
            return date;
        }

        public BigDecimal getDec() {
            return dec;
        }

        public boolean isTest() {
            return test;
        }

        public void setTest(boolean test) {
            this.test = test;
        }

        public byte getBy() {
            return by;
        }

        public void setBy(byte by) {
            this.by = by;
        }

        public String getHadValue() {
            return hadValue;
        }
        public void setHadValue(String hadValue) {
            this.hadValue = hadValue;
        }
        @Override
        public String toString() {
            return "Bar1 [" + (aa != null ? "aa=" + aa + ", " : "") + (bb != null ? "bb=" + bb + ", " : "")
                    + (cc != null ? "cc=" + cc + ", " : "") + (dd != null ? "dd=" + dd + ", " : "")
                    + (ee != null ? "ee=" + ee + ", " : "") + (ff != null ? "ff=" + ff + ", " : "")
                    + (gg != null ? "gg=" + gg + ", " : "") + (hh != null ? "hh=" + hh + ", " : "")
                    + (ii != null ? "ii=" + ii + ", " : "") + (date != null ? "date=" + date + ", " : "")
                    + (dec != null ? "dec=" + dec + ", " : "") + "test=" + test + ", by=" + by + ", "
                    + (hadValue != null ? "hadValue=" + hadValue + ", " : "")
                    + (getFoolong() != null ? "foolong=" + getFoolong() + ", " : "") + (foobbb != null ? "foobbb=" + foobbb : "")
                    + "]";
        }

    }
        

}
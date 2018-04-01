package org.jretty.test;

import java.math.BigDecimal;
import java.util.Date;

public class Bar extends Foo {
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
        return "Bar [" + (aa != null ? "aa=" + aa + ", " : "") + (bb != null ? "bb=" + bb + ", " : "")
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

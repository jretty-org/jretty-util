/*
 * @(#)ReflectionUtilsTest.java
 * Travelsky Report Engine (TRE) Source Code, Version 2.0
 * Author(s): 
 * Zollty Tsou (http://blog.csdn.net/zollty, zouty@travelsky.com)
 * Copyright (C) 2014-2015 Travelsky Technology. All rights reserved.
 */
package org.zollty.util;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;

/**
 * @author zollty
 * @since 2014-6-1
 */
public class ReflectionUtilsTest {
    
    private static final Logger LOG = LogFactory.getLogger();
    
    static interface Ba {
        public int getAge();
        public void setAge(int age);
    }
    
    static class BasicAttr implements Ba {
        private int age;
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }
    
    static class Parent {
        private double money;
        private String name;
        private Ba ba;
        public double getMoney() {
            return money;
        }
        public void setMoney(double money) {
            this.money = money;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Ba getBa() {
            return ba;
        }
        public void setBa(Ba ba) {
            this.ba = ba;
        }
    }
    
    static class Son extends Parent {
        String name;
    }
    
    static class Ea {
        private double money;
        private String name;
        private Ba ba;
        public double getMoney() {
            return money;
        }
        public void setMoney(double money) {
            this.money = money;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Ba getBa() {
            return ba;
        }
        public void setBa(Ba ba) {
            this.ba = ba;
        }
    }
    
    public static void main(String[] args) throws Exception {
        test4AttributeClone();
    }
    
    public static void test4SetAttributeValue() throws Exception{
      Son son = new Son();
      BasicAttr ba = new BasicAttr(); ba.setAge(37);
      ReflectionUtils.setAttributeValue(son, "name", "son Li");
      ReflectionUtils.setAttributeValue(son, "ba", ba);
    }
    
    
    public static void test4GetAttributeValue() throws Exception{
        Son son = new Son();
        son.setMoney(66);
        LOG.info(ReflectionUtils.getAttributeValue(son, "money"));
      }
    
    
    public static void test4AttributeClone() throws Exception{
        Parent parent = new Parent(); 
        BasicAttr ba = new BasicAttr(); ba.setAge(37);
        parent.setName("parent Li"); parent.setMoney(98); parent.setBa(ba);
        Son son = new Son();
        son.setName("son Li");
        son.setMoney(66);
        ReflectionUtils.attributeClone(parent, son);
        LOG.info(son.name+"-or-"+son.getName()+" have money "+son.getMoney()); //儿子继承了父亲的属性
        LOG.info(son.getBa().getAge());
        //---------------
        LOG.info("====================================");
        Ea ea = new Ea();
        ea.setName(" ea ");
        ReflectionUtils.attributeClone(son, ea);
        LOG.info(ea.getName()+" have money "+ea.getMoney()); //儿子继承了父亲的属性
        LOG.info(ea.getBa().getAge());
    }

}

/* 
 * Copyright (C) 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2014-6-01 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * @author zollty
 * @since 2014-6-01
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

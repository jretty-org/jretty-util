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
package org.jretty.reflection;

import java.util.HashMap;
import java.util.Map;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;
import org.jretty.util.ReflectionUtils;
import org.junit.Test;

/**
 * @author zollty
 * @since 2014-6-01
 */
public class ReflectionFieldCloneTest {

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

        @Override
        public String toString() {
            return "BasicAttr [age=" + age + "]";
        }
    }

    static class Parent {
        private float money;
        private Double width;
        private String name;
        private Ba ba;

        public double getMoney() {
            return money;
        }

        public void setMoney(float money) {
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

        public Double getWidth() {
            return width;
        }

        public void setWidth(Double width) {
            this.width = width;
        }

        @Override
        public String toString() {
            return "Parent [money=" + money + ", " + (width != null ? "width=" + width + ", " : "")
                    + (name != null ? "name=" + name + ", " : "") + (ba != null ? "ba=" + ba : "") + "]";
        }
    }

    static class Son extends Parent {
        String name;
    }

    static class FooFather {
        protected Double f1;
        protected Float f2;
        protected static Long f3 = 256L;
    }

    @SuppressWarnings("unused")
    static class Foo extends FooFather {

        private int f4;
        private short f5;
        private final byte f6= 124;
        private String s;
        private Ba ba;
        private int aa;
        private int bb;

        @Override
        public String toString() {
            return "Foo [" + (f1 != null ? "f1=" + f1 + ", " : "") + (f2 != null ? "f2=" + f2 + ", " : "")
                    + (f3 != null ? "f3=" + f3 + ", " : "") + "f4=" + f4 + ", f5=" + f5 + ", f6=" + f6 + ", "
                    + (s != null ? "s=" + s + ", " : "") + (ba != null ? "ba=" + ba + ", " : "") + "aa=" + aa
                    + "]";
        }
    }

    @SuppressWarnings("unused")
    static class BarFather {
        private Float f1 = 100.23f;
        private Long f2 = 10248633L;
        private Integer f3 = 1024;
    }

    @SuppressWarnings("unused")
    static class Bar extends BarFather {
        private final short f4 = 118;
        private final byte f5 = 64;
        private Byte f6 = 32;
        private final String s = "nnnnnnnnnnnn";
        private Ba ba;
        private long aa = 517;
        private int xxx = 89;
    }

    @Test
    public void fieldCloneByNameTest() {
        Bar source = new Bar();
        Ba ba = new BasicAttr();
        ba.setAge(18);
        source.ba = ba;
        Foo target = new Foo();
        // 支持数值类型 把小范围的值 赋值给 大范围的值，比如Float赋值给Double
        ReflectionUtils.fieldCloneByName(source, target);
//        System.out.println(target);
    }

    @Test
    public void fieldCloneByNameTest2() {
        Son source = new Son();
        Ba ba = new BasicAttr();
        ba.setAge(19);
        source.setBa(ba);
        source.setMoney(12.85f);
        source.setName("Lils");
        source.name = "Bob";
        source.setWidth(102.36);

        Parent target = new Parent();
        ReflectionUtils.fieldCloneByName(source, target);
//        System.out.println(target);
    }
    
    @Test
    public void fieldCloneByNameTest3() {
        Ba ba = new BasicAttr();
        ba.setAge(18);
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("f4", 65535);
        sourceMap.put("f5", (short) 23);
        sourceMap.put("s", "sdsdjskjkds");
        sourceMap.put("ba", ba);
        
        Foo target = new Foo();
        ReflectionUtils.fieldCloneByName(sourceMap, target);
//        System.out.println(target);
        
        Foo fan = new Foo();
        ReflectionUtils.fieldCloneByName(target, fan);
    }

    @Test
    public void fieldCloneByInheritTest() {
        Parent source = new Parent();
        Ba ba = new BasicAttr();
        ba.setAge(19);
        source.setBa(ba);
        source.setMoney(12.85f);
        source.setName("Lils");
        source.setWidth(102.36);

        Son target = new Son();
        target.name = "bob";
        ReflectionUtils.fieldCloneByInherit(source, target);
        //System.out.println(target);
        //System.out.println(target.name);
    }

    /**
     *  fieldCloneByName : fieldCloneByInherit
     *   0.00271 ms/n    :  0.00146 ms/n
     *          1.8      :     1
     * 每毫秒可以执行370次  :   680次
     */
//     @Test
    public void perfTest() throws Exception {
        final Parent source = new Parent();
        Ba ba = new BasicAttr();
        ba.setAge(19);
        source.setBa(ba);
        source.setMoney(12.85f);
        source.setName("Lils");
        source.setWidth(102.36);

        final Son target = new Son();
        target.name = "bob";

        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return 100;
            }

            @Override
            public void execute() throws Exception {
                ReflectionUtils.fieldCloneByInherit(source, target);
            }
        });

        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return 100;
            }

            @Override
            public void execute() throws Exception {
                ReflectionUtils.fieldCloneByName(source, target);
            }
        });

        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return 100000;
            }

            @Override
            public void execute() throws Exception {
                ReflectionUtils.fieldCloneByInherit(source, target);
            }
        });

        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return 100000;
            }

            @Override
            public void execute() throws Exception {
                ReflectionUtils.fieldCloneByName(source, target);
            }
        });
    }

}
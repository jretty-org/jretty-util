package org.jretty.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class JSONParserTest {
    
    @Test
    public void parseMap() {
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", "11");
        map.put("bb", innerMap);
        
        // Map嵌套
        JSONParser parser = new JSONParserDirector("{\"aa\":\"11\", \"bb\":{\"aa\":\"11\"}}");
        Map<String, Object> obj = parser.parseMap();
        
        Assert.assertEquals(map, obj);
        
    }
    
    @Test
    public void parseMapWithList() {
        List<String> innerList = new ArrayList<String>();
        innerList.add("22");
        innerList.add("33");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", "11");
        map.put("bb", innerList);
        
        // Map嵌套
        JSONParser parser = new JSONParserDirector(
                "{\"aa\":\"11\", \"bb\":[\"22\",\"33\"]}");
        Map<String, Object> obj = parser.parseMap();
        
        Assert.assertEquals(map, obj);
    }
    
    @Test
    public void parseArray() {
        List<Object> ary = new ArrayList<Object>();
        ary.add("特殊字符" + "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201");
        ary.add("-----");
        
        // Map嵌套
        JSONParser parser = new JSONParserDirector("[\"特殊字符\\n=\\r=\\t='=\\\"=\\b=\\f=\\\\=/=刁\",\"-----\"]");
        List<Object> obj = parser.parseArray();
        
        Assert.assertEquals(ary, obj);
    }
    
    @Test
    public void parseObject() {
        Ba ba = new BasicAttr();
        ba.setAge(19);
        List<Integer> innerList = new ArrayList<Integer>();
        innerList.add(22);
        innerList.add(33);
        
        Foo source = new Foo();
        source.f4 = 23;
        source.s = "zollty";
        source.f5 = 88;
        source.cc = innerList;
        
        Foo pp = JSONUtils.parse(
                "{\"f4\":23, \"s\":\"zollty\", \"f5\":88, \"cc\":[22,33]}", 
                Foo.class);
        
        Assert.assertEquals(source.toString(), pp.toString());
    }
    
    
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

    public static class Son extends Parent {
        String name;
    }
    
    static class Parent {
        private float money;
        private Double width;
        private String name;
        private Ba ba;
        private List<Integer> cc;

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
        
        public List<Integer> getCc() {
            return cc;
        }

        public void setCc(List<Integer> cc) {
            this.cc = cc;
        }

        @Override
        public String toString() {
            return "Parent [money=" + money + ", " + (width != null ? "width=" + width + ", " : "")
                    + (name != null ? "name=" + name + ", " : "") + (ba != null ? "ba=" + ba : "") 
                    + (cc != null ? "cc=" + cc : "") + "]";
        }
    }

    static class FooFather {
        protected Double f1;
        protected Float f2;
        protected static Long f3 = 256L;
    }

    @SuppressWarnings("unused")
    public static class Foo extends FooFather {

        private int f4;
        private short f5;
        private final byte f6= 124;
        private String s;
        private Ba ba;
        private int aa;
        private int bb;
        
        private List<Integer> cc;

        @Override
        public String toString() {
            return "Foo [" + (f1 != null ? "f1=" + f1 + ", " : "") + (f2 != null ? "f2=" + f2 + ", " : "")
                    + (f3 != null ? "f3=" + f3 + ", " : "") + "f4=" + f4 + ", f5=" + f5 + ", f6=" + f6 + ", "
                    + (s != null ? "s=" + s + ", " : "") + (ba != null ? "ba=" + ba + ", " : "") + "aa=" + aa
                    + (cc != null ? ", cc=" + cc : "")
                    + "]";
        }
    }
    

}

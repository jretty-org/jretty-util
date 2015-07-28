package org.zollty.util.json;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.zollty.util.json.support.DateWriter;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author zollty
 * @since 2015-6-18
 */
public class JSONWriterTest {
    
    
    @Test
    public void writeMap() {
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", new Date());
        map.put("bb", innerMap);
        
        JSONWriter writer = new JSONWriterDirector(Date.class, new DateWriter(true));
        writer.writeMap(map);
        String ret = writer.toString();
        
        Assert.assertEquals(
                JSON.toJSONString(map), 
                ret
                );
    }
    
    @Test
    public void writeArray() {
        String[] ary = new String[]{"特殊字符" + "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201", "-----"};
        
        JSONWriter writer = new JSONWriterDirector();
        writer.writeArray(ary);
        String ret = writer.toString();
        
        Assert.assertEquals(
                JSON.toJSONString(ary), 
                ret
                );
    }
    
    
    @Test
    public void writeError() {
        JSONWriter writer = new JSONWriterDirector();
        writer.writeError(new Exception());
        String ret = writer.toString();
        //{"Class":"java.lang.Exception","Message":null,"StackTrace":"java.lang.Exception\n\tat com.travelsky.mlf4j.base.json.JSONWriterTest2.writeError(JSONWriterTest2.java:44)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\n\t... 1 more\n\tat java.lang.reflect.Method.invoke(Method.java:606)\n\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)\n\tat org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)\n\tat org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)\n\tat org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)\n\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)\n\tat org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)\n\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)\n\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)\n\tat org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)\n\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)\n\tat org.junit.runners.ParentRunner.run(ParentRunner.java:309)\n\tat org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:50)\n\tat org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:459)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:675)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:382)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:192)\n"}
        //System.out.println(ret);
        Assert.assertNotNull(ret);
    }
    

}

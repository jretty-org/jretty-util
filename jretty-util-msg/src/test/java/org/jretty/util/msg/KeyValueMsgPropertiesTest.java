package org.jretty.util.msg;

import java.util.Collections;

import org.junit.Test;

public class KeyValueMsgPropertiesTest {
    
    @Test
    public void test() {
        KeyValueMsgProperties keyValueMsg = new KeyValueMsgProperties();
        keyValueMsg.setModulePath("org/jretty/util/msg/message-zh_CN");
        keyValueMsg.setDefaultFile(Collections.singletonList("org/jretty/util/msg/zollty-log-spec.properties"));
        
//        org.junit.Assert.assertNotNull(keyValueMsg.getString("rootLogger"));
//        org.junit.Assert.assertNotNull(keyValueMsg.getByte("test.byte"));
//        org.junit.Assert.assertNotNull(keyValueMsg.getFloat("test.float"));
        org.junit.Assert.assertNull(null, keyValueMsg.getFloat("test.xxx"));
    }

}

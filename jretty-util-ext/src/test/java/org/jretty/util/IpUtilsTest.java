package org.jretty.util;

public class IpUtilsTest {
    
    public static void main(String[] args) {
        System.out.println(IpUtils.getHostName());
        System.out.println(IpUtils.getLocalIP());
        System.out.println(IpUtils.getLocalIP("eth0"));
        
        System.out.println(IpUtils.getDefaultHostAddress());
        System.out.println(IpUtils.getSpecialHostAddress("localhost"));
        
        System.out.println(IpUtils.getSpecialHostAddress("127.0.0.1"));
    }
    
}

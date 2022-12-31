package org.jretty.util;
import java.net.InetAddress;

public class InetAddressHelperTest {
    
    
    public static void main(String[] args) {

        InetAddress ia = new InetAddressHelper("www.baidu.com").startJoin(2000).getInetAddress();

        System.out.println(ia != null ? ia.getHostAddress() : null);

        String host = new InetAddressHelper(ia).startJoin(2000).getHostName();
        System.out.println(host);

    }

}

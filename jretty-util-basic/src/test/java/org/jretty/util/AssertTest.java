package org.jretty.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;


public class AssertTest {
    
    
    @Test
    public void assertExceptionTest() {
        int line = 25; // 对应 doService第三行的行号
        try {
            doService(null, "assert it's true but false....");
        } catch(Exception e) {
            StackTraceElement[] st = e.getStackTrace();
            // e.printStackTrace();
            org.junit.Assert.assertEquals(
                    "org.jretty.util.AssertTest.doService(AssertTest.java:"+line+")",
                    st[0].toString());
        }
    }
    
    public static void doService(String expression, String message) {
        System.out.println("pass..");
        Assert.hasLength(expression); //line number
        System.out.println("OK");
    }
    
    
    
    
    /**
     * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
     * if the test result is <code>false</code>.
     * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if expression is <code>false</code>
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            IllegalArgumentException e = new IllegalArgumentException(message);
            
            StackTraceElement[] st = e.getStackTrace();
            st = ArrayUtils.remove(st, 0);
            e.setStackTrace(st);
            //e.printStackTrace();
            throw e;
        }
    }
    
    public static void hasLength(String text) {
//        try {
        hasText(text,
                "this exception is just for unit test. [Assertion failed] - this String argument must have length; it must not be null or empty");
//        } catch(IllegalArgumentException e) {
//            throw changeIAE(e);
//        }
    }
    
    public static RuntimeException changeIAE(RuntimeException e){
        StackTraceElement[] st = e.getStackTrace();
        st = ArrayUtils.remove(st, 0);
        e.setStackTrace(st);
        return e;
    }

    /**
     * Assert that the given String has valid text content; that is, it must not
     * be <code>null</code> and must contain at least one non-whitespace character.
     * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @see StringUtils#hasText
     */
    public static void hasText(String text, String message) {
//        try {
//        notNull(text);
//        } catch(IllegalArgumentException e) {
//            throw changeIAE(e);
//        }
//        if (StringUtils.isBlank(text)) {
//            throw changeIAE(new IllegalArgumentException(message));
//        }
        
        notNull(text);
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }
    
    
    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz);</pre>
     * @param object the object to check
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object) {
//        try {
//        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
//        } catch(IllegalArgumentException e) {
//            throw changeIAE(e);
//        }
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
//            throw changeIAE(new IllegalArgumentException(message));
            throw new IllegalArgumentException(message);
        }
    }

    public static class IpUtilsTest {

        public static String getLocalMacHex() {
            byte[] mac = getLocalMac();
            if (mac == null) {
                return null;
            }
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(":");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        }

        private static String toHexMac(byte[] mac) {
            if (mac == null) {
                return null;
            }
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(":");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        }

        public static byte[] getLocalMac() {
            InetAddress ia = null;
            try {
                ia = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                String hostName = getHostName();
                if (hostName != null) {
                    try {
                        ia = InetAddress.getByName(hostName);
                    } catch (UnknownHostException ue) {
                        e.printStackTrace();
                    }
                }
            }
            byte[] mac = null;
            if (ia != null) {
                try {
                    NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
                    System.out.println(ia + " - get NetworkInterface1:  " + ni);
                    mac = ni == null ? null : ni.getHardwareAddress();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
            if (mac == null) {
                ia = findRealIP();
                try {
                    NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
                    System.out.println(ia + " - get NetworkInterface2:  " + ni);
                    mac = ni == null ? null : ni.getHardwareAddress();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
            return mac;
        }

        /**
         * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统<br>
         * @see {@link #findRealIP()}
         */
        public static String getLocalIP() {
            InetAddress ip = null;
            try {
                if (isWindowsOS()) {
                    ip = InetAddress.getLocalHost();
                } else {
                    ip = findRealIP();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return null != ip ? ip.getHostAddress() : null;
        }

        /**
         * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统
         * 注意：Local IP标准的定义：（参见java.net.InetAddress.isSiteLocalAddress()）
         * // refer to RFC 1918
         * // 10/8 prefix
         * // 172.16/12 prefix
         * // 192.168/16 prefix
         * 但是，这个方法扩大了这个定义的范围，只要是xxx.xxx.xxx.xxx（xxx为数字）格式的，都是本机IP
         *
         * @return String IP地址，比如 172.16.14.160,或者null
         */
        public static String getLocalIP(String networkInterfaceName) {
            InetAddress ip = null;
            try {
                // 如果是Windows操作系统
                if (isWindowsOS()) {
                    ip = InetAddress.getLocalHost();
                }
                // 如果是Linux操作系统
                else {
                    Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>)
                            NetworkInterface.getNetworkInterfaces();
                    boolean bFindIP = false;
                    while (netInterfaces.hasMoreElements()) {
                        if (bFindIP) {
                            break;
                        }
                        NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                        // ----------特定情况，可以考虑用ni.getName判断
                        if (!ni.getName().equals(networkInterfaceName)) {
                            continue;
                        }
                        // 遍历所有ip
                        Enumeration<InetAddress> ips = ni.getInetAddresses();
                        InetAddress tmp;
                        while (ips.hasMoreElements()) {
                            tmp = (InetAddress) ips.nextElement();
                            if (!tmp.isLoopbackAddress() // 127.开头的都是lookback地址
                                    && tmp.getHostAddress().indexOf(":") == -1) {
                                ip = tmp;
                                bFindIP = true;
                                break;
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String sIP = null;
            if (null != ip) {
                sIP = ip.getHostAddress();
            }
            return sIP;
        }


        public static String getSocketIp(final String host, final int port) {
            Socket socket = null;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 500);
                socket.setSoTimeout(1000);
                return socket.getLocalAddress().toString().substring(1);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }

        /**
         * 获取HostName，并转换成大写 (toUpperCase)
         * @return String HostName (toUpperCase) or null
         */
        public static String getHostName() {
            String hostName = null;
            if (System.getenv("COMPUTERNAME") != null) {
                hostName = System.getenv("COMPUTERNAME");
                System.out.println("have COMPUTERNAME: " + hostName);
            } else {
                try {
                    hostName = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException uhe) {
                    String host = uhe.getMessage();
                    if (host != null) {
                        int colon = host.indexOf(':');
                        if (colon > 0) {
                            hostName = host.substring(0, colon);
                        }
                    } else {
                        uhe.printStackTrace();
                    }
                }
            }
            // 转换成大写，以避免大小写问题
            return hostName != null ? hostName.toUpperCase() : null;
        }

        /**
         * 获取本机hostname对应的IP地址（hostname映射的IP地址可以在hosts文件中配置） <br>
         * 如果没有配置hostname，则以“localhost”为默认hostname。<br>
         * 例如本机hostname配置为lightning-push-tomcat，hosts文件如下：
         * <br>127.0.0.1       localhost
         * <br>172.16.1.41     lightning-push-tomcat
         * <br>那么取出来的地址为：172.16.1.41。假如没配置hostname，则取出来的是“localhost”对应的地址，即127.0.0.1（也可以修改）
         */
        public static String getDefaultHostAddress() {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return null;
            }
        }

        /**
         * 可以读取在hosts文件中映射的IP地址，和ping hostName的结果相同
         * @param hostName host名称，忽略大小写
         * @return host对应的IP地址
         */
        public static String getSpecialHostAddress(String hostName) {
            InetAddress ia = new InetAddressHelper(hostName).startJoin(1000).getInetAddress();
            if (ia != null) {
                return ia.getHostAddress();
            }
            return null;
        }

        /**
         * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统<br>
         * 如果是Linux系统，则只取eth0网卡的ip
         * @see {@link #getLocalIP(String)}
         * @return
         */
        public static String getNormalLocalIP() {
            return getLocalIP("eth0");
        }

        /**
         * check if on the WINDOWS system.
         * @return true if on windows system.
         */
        public static boolean isWindowsOS() {
            boolean isWindowsOS = false;
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().indexOf("windows") > -1) {
                isWindowsOS = true;
            }
            return isWindowsOS;
        }


        /**
         * 有关NetworkInterface等API的说明：https://www.cnblogs.com/guangshan/p/4712550.html
         * @return
         */
        public static String getLocalIPs() {
            InetAddress ip = null;
            try {
                // 如果是Windows操作系统
    //            if (isWindowsOS()) {
    //                ip = InetAddress.getLocalHost();
    //            }
                // 如果是Linux操作系统
    //            else {
                    Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>)
                            NetworkInterface.getNetworkInterfaces();
                    boolean bFindIP = false;
                    while (netInterfaces.hasMoreElements()) {
                        if (bFindIP) {
                            break;
                        }
                        NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                        // 遍历所有ip
                        Enumeration<InetAddress> ips = ni.getInetAddresses();
                        InetAddress tmp;
                        while (ips.hasMoreElements()) {
                            tmp = (InetAddress) ips.nextElement();
                            if (ni.isUp() && !tmp.isLoopbackAddress() // 127.开头的都是lookback地址
                                    && tmp.getHostAddress().indexOf(":") == -1) {
                                ip = tmp;
                                System.out.println(ni + " - " + ni.getMTU()
                                + " isVirtual: " + ni.isVirtual()
                                + " Multicast: " + ni.supportsMulticast()
                                + " Parent: " + ni.getParent());
                                System.out.println(ni.getName() + " mac: " + toHexMac(ni.getHardwareAddress()) + " -find- " + ip.getHostAddress());
                            }
                        }

                    }
    //            }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String sIP = null;
            if (null != ip) {
                sIP = ip.getHostAddress();
            }
            return sIP;
        }

        public static String getRealIP() {
            String ip = getDefaultHostAddress();
            if (ip != null) {
                if (!"127.0.0.1".equals(ip)) { // 配置了host ip （/etc/hosts文件中）
                    return ip;
                } else { // 没有配置 host ip
                    ip = null;
                }
            } else {
                String hostName = getHostName();
                if (hostName != null) {
                    ip = getSpecialHostAddress(hostName);
                }
            }

            if (ip == null) {
                ip = getSocketIp("114.114.114.114", 80);
            }

            if (ip == null) {
                // 根据 findRealIP（）查找最有可能的ip
                ip = findRealIP().getHostAddress();
            }

            return ip;
        }


        /**
         * 智能获取本机IP地址（针对linux/unix系统）
         * @return
         */
        public static InetAddress findRealIP() {
            InetAddress ip = null;
            try {

                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                        .getNetworkInterfaces();
                boolean bFindIP = false;
                Map<NetworkInterface, InetAddress> map = new HashMap<NetworkInterface, InetAddress>();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    if (!ni.isUp()) { // 跳过没有启用的网卡
                        continue;
                    }
                    System.out.println("网卡：" + ni.getName() + " mac: " + toHexMac(ni.getHardwareAddress()));

                    // 遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    InetAddress tmp;
                    while (ips.hasMoreElements()) {
                        tmp = (InetAddress) ips.nextElement();
                        if (!tmp.isLoopbackAddress() // 127.开头的都是lookback地址
                                && tmp.getHostAddress().indexOf(":") == -1) {
                            ip = tmp;
                            System.out.println(ni + " - " + ni.getMTU() + " isVirtual: " + ni.isVirtual() + " Multicast: "
                                    + ni.supportsMulticast() + " Parent: " + ni.getParent());
                            System.out.println(ni.getName() + " mac: " + toHexMac(ni.getHardwareAddress()) + " -find- "
                                    + ip.getHostAddress());
                            System.out.println("-----------------------------------" + ip.getClass().getName());
                            System.out.println(new InetAddressHelper(ip).startJoin(2000).getHostName()); //ip.getCanonicalHostName()
                            System.out.println(InetAddress.getLocalHost());
                            System.out.println(ip.isSiteLocalAddress());
                            System.out.println(ip.getHostAddress());
                            System.out.println("+++++");
                            System.out.println(ip.isLinkLocalAddress());
                            System.out.println(ip.isAnyLocalAddress());
                            System.out.println(ip.isMCGlobal());
                            System.out.println(ip.isMCLinkLocal());
                            System.out.println(ip.isMCNodeLocal());
                            System.out.println(ip.isMulticastAddress());
                            System.out.println(ip.isReachable(100));
                            System.out.println("-----------------------------------");

                            map.put(ni, ip);
                            break;
                        }
                    }

                }

                if (map.size() == 1) {
                    ip = map.values().iterator().next();
                } else {

                    Set<NetworkInterface> org = map.keySet();
                    for (NetworkInterface ni : org) {
                        if ("eth0".equals(ni.getName())) {
                            ip = map.get(ni);
                            break;
                        }
                    }

                    Set<NetworkInterface> two = findByMulticast(org);
                    if (two.size() == 1) {
                        ip = map.get(two.iterator().next());

                    } else if (two.size() == 0) {

                        two = findByMtu(org);

                        if (two.size() == 0) {
                            ip = map.get(org.iterator().next());
                        } else if (two.size() == 1) {
                            ip = map.get(two.iterator().next());
                        } else {
                            ip = map.get(two.iterator().next());
                        }

                    } else {

                        Set<NetworkInterface> three = findByMtu(two); // 再次过滤

                        if (three.size() == 0) {
                            ip = map.get(two.iterator().next());
                        } else if (three.size() == 1) {
                            ip = map.get(three.iterator().next());
                        } else {
                            ip = map.get(three.iterator().next());
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return ip;
        }

        private static Set<NetworkInterface> findByMulticast(Set<NetworkInterface> sel) throws SocketException {
            Set<NetworkInterface> sel2 = new HashSet<NetworkInterface>();
            for (NetworkInterface ni : sel) {
                if (ni.supportsMulticast()) {
                    sel2.add(ni);
                }
            }
            return sel2;
        }

        private static Set<NetworkInterface> findByMtu(Set<NetworkInterface> sel) throws SocketException {
            Set<NetworkInterface> sel2 = new HashSet<NetworkInterface>();
            for (NetworkInterface ni : sel) {
                if (ni.getMTU() == 1500) {
                    sel2.add(ni);
                }
            }
            return sel2;
        }

        public static void main(String[] args) throws IOException {
            System.out.println("0.exit");
            System.out.println("1.getHostName()");
            System.out.println("2.getLocalMacHex()");
            System.out.println("3.getLocalIP()");
            System.out.println("4.getRealIP()");
            System.out.println("5.getSocketIp(\"114.114.114.114\", 80)");
            System.out.println("6.getSocketIp(\"10.1.10.80\", 80)");
            System.out.println("7.getSpecialHostAddress(\"localhost\")");
            System.out.println("8.getSpecialHostAddress(\"127.0.0.1\")");
            System.out.println("9.findRealIP()");
            System.out.println("10.getNormalLocalIP()");
            System.out.println("11.getLocalIP(\"eth0\")");
            System.out.println("12.getDefaultHostAddress()");
            System.out.println("13.getSpecialHostAddress(\"www.baidu.com\")");
            System.out.println("14.printWangKaInfo()");
            while (true) {
              System.out.print("> ");
              String input = new BufferedReader(new InputStreamReader(System.in, "UTF-8")).readLine();
              if (input == null || input.length() == 0) {
                continue;
              }
              input = input.trim();
              Byte num = null;
              try {
                  num = Byte.parseByte(input);
              } catch (Exception e) {
                  System.out.println("> Please input the right num!");
                  continue;
              }
              long start = System.currentTimeMillis();
              switch(num) {
                  case 0: {
                      System.exit(0);
                      break;
                  }
                  case 1: {
                      System.out.println("Result > " + getHostName() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 2: {
                      System.out.println("Result > " + getLocalMacHex() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 3: {
                      System.out.println("Result > " + getLocalIP() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 4: {
                      System.out.println("Result > " + getRealIP() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 5: {
                      System.out.println("Result > " + getSocketIp("114.114.114.114", 80) +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 6: {
                      System.out.println("Result > " + getSocketIp("10.1.10.80", 80) +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 7: {
                      System.out.println("Result > " + getSpecialHostAddress("localhost") +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 8: {
                      System.out.println("Result > " + getSpecialHostAddress("127.0.0.1") +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 9: {
                      System.out.println("Result > " + findRealIP() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 10: {
                      System.out.println("Result > " + getNormalLocalIP() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 11: {
                      System.out.println("Result > " + getLocalIP("eth0") +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 12: {
                      System.out.println("Result > " + getDefaultHostAddress() +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 13: {
                      System.out.println("Result > " + getSpecialHostAddress("www.baidu.com") +" (cost " + (System.currentTimeMillis()-start) + " ms)");
                      break;
                  }
                  case 14: {
                      printWangKaInfo();
                      break;
                  }
              }
            }



            /**
             * 有eth0的输出结果
    MAINGEAR
    192.168.10.194
    192.168.10.194
    192.168.10.194
    192.168.10.194
    127.0.0.1
    127.0.0.1
    34-E6-AD-94-7F-05
             */

            /**
             * 没有eth0的输出结果1
    LOCALHOST.LOCALDOMAIN
    null ---eth0
    null ---eth0
    127.0.0.1 ---DefaultHostAddress
    10.2.10.23 ---sockect ip比较靠谱。
    127.0.0.1 ----localhost
    127.0.0.1 ----127.0.0.1
    null ---mac
             */

            /**
             * 没有eth0的输出结果2
    BOGON
    null
    null
    192.168.11.242
    192.168.11.242
    127.0.0.1
    127.0.0.1
    00-50-56-8E-57-B6
             */

            /**
             * 没有eth0的输出结果3
    CSB-BROKER-LB
    null
    null
    10.1.10.95
    java.net.UnknownHostException: www.baidu.com
        at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:178)
        at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:391)
        at java.net.Socket.connect(Socket.java:579)
        at java.net.Socket.connect(Socket.java:528)
        at java.net.Socket.<init>(Socket.java:425)
        at java.net.Socket.<init>(Socket.java:208)
        at org.jretty.util.AssertTest.IpUtilsTest.getSocketIp(org.jretty.util.AssertTest.IpUtilsTest.java:153)
        at org.jretty.util.AssertTest.IpUtilsTest.main(org.jretty.util.AssertTest.IpUtilsTest.java:296)
    null
    127.0.0.1
    127.0.0.1
    00-50-56-8A-61-24
             */
        }


        /**
         * 打印本机网卡信息（针对linux/unix系统）
         */
        public static void printWangKaInfo() {
            try {
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                        .getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    if (!ni.isUp()) { // 跳过没有启用的网卡
                        System.out.println("未启用的网卡：" + ni.getName() + " mac: " + toHexMac(ni.getHardwareAddress()));
                        // continue;
                    }
                    System.out.println("网卡：" + ni.getName() + " mac: " + toHexMac(ni.getHardwareAddress()));

                    // 遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    InetAddress ip;
                    while (ips.hasMoreElements()) {
                        ip = (InetAddress) ips.nextElement();
                        if (!ip.isLoopbackAddress() // 127.开头的都是lookback地址
                                && ip.getHostAddress().indexOf(":") == -1) {
                            System.out.println(ni.getName() + " mac: " + toHexMac(ni.getHardwareAddress())
                                    + " -ip- " + ip.getHostAddress() + " - " + ni.getMTU() + " isVirtual: " + ni.isVirtual()
                                    + " Multicast: " + ni.supportsMulticast() + " Parent: " + ni.getParent());
                            System.out.println("ip.getClass-----------------------------------" + ip.getClass().getName());
                            System.out.println("getHostName" + ": " + new InetAddressHelper(ip).startJoin(2000).getHostName());
                            System.out.println("getHostAddress" + ": " + ip.getHostAddress());
                            System.out.println("isSiteLocalAddress" + ": " + ip.isSiteLocalAddress());
                            System.out.println("isLinkLocalAddress" + ": " + ip.isLinkLocalAddress());
                            System.out.println("isAnyLocalAddress" + ": " + ip.isAnyLocalAddress());
                            System.out.println("isMCGlobal" + ": " + ip.isMCGlobal());
                            System.out.println("isMCLinkLocal" + ": " + ip.isMCLinkLocal());
                            System.out.println("isMCNodeLocal" + ": " + ip.isMCNodeLocal());
                            System.out.println("isMulticastAddress" + ": " + ip.isMulticastAddress());
                            System.out.println("isReachable(100)" + ": " + ip.isReachable(100));
                            System.out.println("-----------------------------------");
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static class InetAddressHelper extends Thread {

            private String hostName;

            private InetAddress inetAddress;

            /* What will be run. */
            private Runnable target;

            public InetAddressHelper(InetAddress inetAddress) {
                this.inetAddress = inetAddress;
                this.target = new getHostName();
            }

            public InetAddressHelper(String hostName) {
                this.hostName = hostName;
                this.target = new GetByName();
            }

            public InetAddressHelper startJoin(long waitMs) {
                start();
                try {
                    join(waitMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.toString());
                }
                return this;
            }

            public void run() {
                target.run();
            }

            public class GetByName implements Runnable {
                @Override
                public void run() {
                    InetAddressHelper.this.inetAddress = InetAddressHelper.getByName(InetAddressHelper.this.hostName);
                }
            }

            public class getHostName implements Runnable {
                @Override
                public void run() {
                    InetAddressHelper.this.hostName = InetAddressHelper.this.inetAddress.getHostName();
                }
            }

            protected static InetAddress getByName(String host) {
                try {
                    return InetAddress.getByName(host);
                } catch (UnknownHostException e) {
                    return null;
                }
            }

            protected static InetAddress[] getAllByName(String host) {
                try {
                    return InetAddress.getAllByName(host);
                } catch (UnknownHostException e) {
                    return null;
                }
            }

            public String getHostName() {
                return hostName;
            }

            public String getCanonicalHostName() {
                return hostName;
            }

            public void setHostName(String hostName) {
                this.hostName = hostName;
            }

            public InetAddress getInetAddress() {
                return inetAddress;
            }

            public void setInetAddress(InetAddress inetAddress) {
                this.inetAddress = inetAddress;
            }
        }

    }
}



import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IpUtilsTest {
    
    /**
     * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统<br>
     * 如果是Linux系统，则只取eth0网卡的ip
     * @see {@link #getLocalIP(String)}
     * @return
     */
    public static String getLocalIP() {
        return getLocalIP("eth0");
    }
    
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
            socket.connect(new InetSocketAddress(host, port), 1000);
            socket.setSoTimeout(3000);
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
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
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
            ip = getSocketIp("www.baidu.com", 80);
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
                        System.out.println("-----------------------------------");
                        System.out.println(ip.getHostName());
                        System.out.println(InetAddress.getLocalHost());
                        System.out.println(ip.getCanonicalHostName());
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
    
    public static void main(String[] args) {
        System.out.println(getHostName());
        System.out.println(getLocalIP());
        System.out.println(getLocalIP("eth0"));
        
        System.out.println(getDefaultHostAddress());
        System.out.println(getSocketIp("www.baidu.com", 80));
        System.out.println(getSocketIp("10.1.10.80", 80));
        System.out.println(getSpecialHostAddress("localhost"));
        
        System.out.println(getSpecialHostAddress("127.0.0.1"));
        
        System.out.println("------------------------------");
//        getLocalIPs();
        System.out.println(",,,,,,,,,,,,,,,,,,,," + findRealIP());
        System.out.println(getRealIP());
        System.out.println(getLocalMacHex());
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
    at IpUtilsTest.getSocketIp(IpUtilsTest.java:153)
    at IpUtilsTest.main(IpUtilsTest.java:296)
null
127.0.0.1
127.0.0.1
00-50-56-8A-61-24
         */
    }
    
}

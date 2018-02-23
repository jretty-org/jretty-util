package org.jretty.util;

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

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * IP utils
 * 
 * @author zollty  高效的算法保证
 * @since 2016-7-28
 * @see InetAddressHelper
 */
public class IpUtils {
    
    private static final Logger LOG = LogFactory.getLogger(IpUtils.class);
    
    private IpUtils() {}

    /**
     * 智能分析，精确获取本机网卡，并转换成十六进制
     */
    public static String getLocalMacHex() {
        byte[] mac = getLocalMac();
        return toHexMac(mac);
    }
    
    /**
     * 智能分析，精确获取本机网卡
     */
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
                    LOG.warn(e, "get InetAddress fail due to: ");
                }
            }
        }
        byte[] mac = null;
        if (ia != null) {
            try {
                NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
                mac = ni == null ? null : ni.getHardwareAddress();
            } catch (SocketException e) {
                LOG.warn(e, "parse Address error: " + ia);
            }
        }
        if (mac == null) {
            ia = findRealIP();
            try {
                NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
                mac = ni == null ? null : ni.getHardwareAddress();
            } catch (SocketException e) {
                LOG.warn(e, "parse Address error: " + ia);
            }
        }
        return mac;
    }
    
    /**
     * 智能分析，精确获取本机Local ip（内网）地址
     */
    public static String getLocalIP() {
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
     * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统<br>
     * @see {@link #findRealIP()}
     */
    static String getLocalIP0() {
        InetAddress ip = null;
        try {
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            } else {
                ip = findRealIP();
            }
        } catch (UnknownHostException e) {
            LOG.error(e, "parse IP Address error.");
        }
        return null != ip ? ip.getHostAddress() : null;
    }
    
    /**
     * 智能分析，精准获取本机IP地址（针对linux/unix系统）
     */
    static InetAddress findRealIP() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = 
                    (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
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
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                InetAddress tmp;
                while (ips.hasMoreElements()) {
                    tmp = (InetAddress) ips.nextElement();
                    if (!tmp.isLoopbackAddress() // 127.开头的都是lookback地址
                            && tmp.getHostAddress().indexOf(":") == -1) {
                        ip = tmp;
                        
                        if(LOG.isDebugEnabled()) {
                            LOG.debug(ni + " - " + ni.getMTU() + 
                                    " isVirtual: " + ni.isVirtual() + 
                                    " Multicast: " + ni.supportsMulticast() + 
                                    " Parent: " + ni.getParent());
                            LOG.debug(ni.getName() + 
                                    " mac: " + toHexMac(ni.getHardwareAddress()) + 
                                    " -ip- " + ip.getHostAddress());
                        }

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
            LOG.error(e, "parse IP Address error..");
        }

        return ip;
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
                Enumeration<NetworkInterface> netInterfaces = 
                        (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
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
            LOG.warn(e, "get ip fail due to: ");
        }

        String sIP = null;
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }
    
    /**
     * 根据Socket方式，获取本机出口IP
     * @param host 远程通信地址
     * @param port 通信端口
     * @return 本机IP
     */
    public static String getSocketIp(final String host, final int port) {
        Socket socket = null;
        try {
            // socket = new Socket(host, port);
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 500);
            socket.setSoTimeout(1000);
            return socket.getLocalAddress().toString().substring(1);
        } catch (Exception e) {
            LOG.warn(e, "can not get ip,");
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
     * 获取HostName，并转换成大写 (toUpperCase)。此方法靠谱。
     * @return String HostName (toUpperCase) or null
     */
    public static String getHostName() {
        String hostName = null;
        if (System.getenv("COMPUTERNAME") != null) {
            hostName = System.getenv("COMPUTERNAME");
        } else {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                String host = e.getMessage();
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        hostName = host.substring(0, colon);
                    }
                } else {
                    LOG.warn(e, "get getHostName fail due to: ");
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
    static String getDefaultHostAddress() {
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
     * check if on the WINDOWS system.
     * @return true if on windows system.
     */
    static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
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
    
}
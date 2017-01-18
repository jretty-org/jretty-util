package org.jretty.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * IP utils
 * 
 * @author zollty
 * @since 2016-7-28
 */
public class IpUtils {
	
	private static final Logger LOG = LogFactory.getLogger(IpUtils.class);
	
	private IpUtils() {}

	/**
	 * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统<br>
	 * 如果是Linux系统，则只取eth0网卡的ip
	 * @see {@link #getLocalIP(String)}
	 * @return
	 */
	public static String getLocalIP() {
		return getLocalIP("eth0");
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
			LOG.warn("get ip fail due to: ", e);
		}

		String sIP = null;
		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}
	
	/**
     * 获取HostName，并转换成大写 (toUpperCase)
     * @return String HostName (toUpperCase) or null
     */
    public static String getHostName() {
        String hostName = null;
        if (System.getenv("COMPUTERNAME") != null) {
            hostName = System.getenv("COMPUTERNAME");
        } else {
            try {
                hostName = (InetAddress.getLocalHost()).getHostName();
            } catch (UnknownHostException uhe) {
                String host = uhe.getMessage();
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        hostName = host.substring(0, colon);
                    }
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

}
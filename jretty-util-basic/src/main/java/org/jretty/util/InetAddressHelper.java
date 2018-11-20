package org.jretty.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 解决InetAddress.getByName() 的DNS查询无法设置超时问题
 * 
 * <p>通过线程，巧妙实现了添加超时限制的功能。
 * <p>使用方法：
 * 
 * <p>1.根据hostName获取InetAddress对象：
 * InetAddress ia = new InetAddressHelper(hostName).startJoin(1000).getInetAddress();
 * 
 * <p>2.根据InetAddress对象获取hostName：
 * String hostName = new InetAddressHelper(ip).startJoin(2000).getHostName()
 * 
 * @author zollty
 * @since 2018年1月22日
 */
public class InetAddressHelper extends Thread {
    
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

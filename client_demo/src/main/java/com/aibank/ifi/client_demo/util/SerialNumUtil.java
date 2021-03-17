package com.aibank.ifi.client_demo.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;



public class SerialNumUtil {
	private static String localIp="";
	private static long order = 1L;
	private static String lastDate = "";
	// 生成流水号，加锁防止生成同样的流水号
	public synchronized static String creatSerailNum(String appId) {
		String defaultIp = "";
		try {
			String hostAddress = getLocalIP();
			Pattern patter = Pattern.compile("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.");
			Matcher matcher = patter.matcher(hostAddress);
			if (matcher.find()) {
				String group = matcher.group();
				defaultIp = StringUtils.leftPad(hostAddress.replace(group, ""), 2, "0");
			}
		} catch (Exception e) {
			// 获取被你ip失败。不影响生成序列号
			defaultIp = "warn";
		}
		String seriaNum = appId + defaultIp+order();
		return seriaNum;
	}

	public static String order() {
		DecimalFormat df1 = new DecimalFormat("000");
		DateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String nowDate = f.format(new Date());
		if (nowDate.compareTo(lastDate) == 0) {
			order++;
		} else {
			order = 1L;
			lastDate = nowDate;
		}
		return nowDate + df1.format(order);
	}

	/**
	 * 获得主机IP
	 *
	 * @return String
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
	 * 获取本机ip地址，并自动区分Windows还是linux操作系统
	 * 
	 * @return String
	 */
	public static String getLocalIP() throws Exception {
		if(!StringUtils.isEmpty(localIp)) {
			return localIp;
		}
		String sIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			throw new Exception("获取ip失败", e);
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		if (StringUtils.isEmpty(sIP)) {
			sIP = "0.0.0.0";
		}
		localIp = sIP;
		return sIP;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for(int i =0;i<10000;i++){
			cachedThreadPool.execute(new Runnable() {
              public void run() { 	System.out.println(SerialNumUtil.creatSerailNum(""));
											 }});
		}

	}
}

package com.aibank.ifi.client_demo.util;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static final String encrypt(byte[] btInput) {
		char[] md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			mdInst.update(btInput);

			byte[] md = mdInst.digest();

			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; ++i) {
				byte byte0 = md[i];
				str[(k++)] = md5String[(byte0 >>> 4 & 0xF)];
				str[(k++)] = md5String[(byte0 & 0xF)];
			}

			return new String(str);
		} catch (Exception localException) {
		}
		return null;
	}

	private static String MD52(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte[] b = md.digest();

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; ++offset) {
				int i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
			System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	public static void main(String[] args) {
		String msg = "{\"returnCode\":\"BCEUPP04002\",\"bizType\":null,\"sysNo\":null,\"cusTranNo\":null,\"returnMessage\":\"报文体中version不能为空\",\"dealResult\":\"FAIL\",\"productNo\":null}";
		try {
			msg = encrypt(msg.getBytes());
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.aibank.ifi.client_demo.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.xml.bind.DatatypeConverter;

public class SimpleSignerUtil {
	public static void main(String[] args) {
	}

	public static String sign(byte[] data, String aturealgo, PrivateKey privateKey) throws Exception {
		// long startTime = new Date().getTime();
		try {
			Signature s = Signature.getInstance(aturealgo);
			s.initSign(privateKey);
			s.update(data);
			byte[] result = s.sign();
			return DatatypeConverter.printBase64Binary(result);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("不支持算法[" + aturealgo + "]", e);
		} catch (InvalidKeyException e) {
			throw new Exception("私钥无效[" + aturealgo + "]", e);
		} catch (SignatureException e) {
			throw new Exception("无法签名", e);
		}

	}

	public static String sign(byte[] data, String aturealgo, String privateKeyBase64) throws Exception {
		PrivateKey privateKey = KeyUtil.getPrivateKeyByBase64String(privateKeyBase64);
		return sign(data, aturealgo, privateKey);
	}

	public static void verify(byte[] updateData, String sigedText, String aturealgo, PublicKey publicKey)
			throws Exception {
		byte[] siged = DatatypeConverter.parseBase64Binary(sigedText);

		boolean verifies = false;
		// long startTime = new Date().getTime();
		try {
			Signature rsa = Signature.getInstance(aturealgo);
			rsa.initVerify(publicKey);
			rsa.update(updateData);
			verifies = rsa.verify(siged);
			// long endTime = new Date().getTime();
			// System.out.println("������ǩ���Ѻ�����Ϊ[" + (endTime - startTime) + "]");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("不支持算法[" + aturealgo + "]", e);
		} catch (InvalidKeyException e) {
			throw new Exception("公钥无效[" + aturealgo + "]", e);
		} catch (SignatureException e) {
			throw new Exception("无法验签", e);
		}
		// Signature rsa;
		if (verifies) {
			// System.out.println("Verify is done!");
		} else {
			// System.out.println("verify is not successful");
			throw new Exception("签名无效");
		}
	}

	public static void verify(byte[] updateData, String sigedText, String aturealgo, String publicKeyBase64)
			throws Exception {
		PublicKey publicKey = KeyUtil.getPublicKeyByBase64String(publicKeyBase64);
		verify(updateData, sigedText, aturealgo, publicKey);
	}
}
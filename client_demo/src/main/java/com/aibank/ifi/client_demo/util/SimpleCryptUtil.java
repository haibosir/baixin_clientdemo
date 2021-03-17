package com.aibank.ifi.client_demo.util;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class SimpleCryptUtil {

	public static String encodeByAlgorithm(String content, String charset, String algorithm,String key)
			throws Exception {
		String result;
		if ("SM4".equals(algorithm.toUpperCase())) {
			result = DatatypeConverter.printBase64Binary(SM4Util.encrypt(content.getBytes(Charset.forName(charset)), key.getBytes(Charset.forName(charset))));
		} else {
			result = DatatypeConverter.printBase64Binary(desEncrypt(content.getBytes(Charset.forName(charset)),algorithm,key));
					;
		}
		return result;
	}

	public static String decodeByAlgorithm(String content, String charset, String algorithm,String key)
			throws Exception {
		String result = null;
		if ("SM4".equals(algorithm.toUpperCase())) {
			result = new String(SM4Util.decrypt(DatatypeConverter.parseBase64Binary(content), key.getBytes(Charset.forName(charset))),Charset.forName(charset));
		} else {
			result = new String(desDecrypt(DatatypeConverter.parseBase64Binary(content),algorithm,key),Charset.forName(charset));
		}
		return result;
	}

	private static byte[] desEncrypt(byte[] inputBytes, String algorithm,String key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, KeyUtil.getSecretKeyByBase64String(key, algorithm));
			return cipher.doFinal(inputBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("不支持算法[" + algorithm + "]", e);
		} catch (NoSuchPaddingException e) {
			throw new Exception("NoSuchPaddingException", e);
		} catch (InvalidKeyException e) {
			throw new Exception("密钥无效", e);
		} catch (IllegalBlockSizeException e) {
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			throw new Exception("BadPaddingException", e);
		}

	}

	private static byte[] desDecrypt(byte[] encryptedBytes, String algorithm,String key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, KeyUtil.getSecretKeyByBase64String(key, algorithm));
			byte[] recoveredBytes = cipher.doFinal(encryptedBytes);
			return recoveredBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("不支持算法[" + algorithm + "]", e);
		} catch (NoSuchPaddingException e) {
			throw new Exception("NoSuchPaddingException", e);
		} catch (InvalidKeyException e) {
			throw new Exception("密钥无效", e);
		} catch (IllegalBlockSizeException e) {
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			throw new Exception("BadPaddingException", e);
		}

	}


}
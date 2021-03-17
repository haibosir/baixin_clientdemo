package com.aibank.ifi.client_demo.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.DatatypeConverter;

public class KeyUtil {
	public static String getBase64StringKey(Key key) {
		return DatatypeConverter.printBase64Binary(key.getEncoded());
	}

	public static PrivateKey getPrivateKeyByBase64String(String base64String) {
		byte[] privateKeyByte = DatatypeConverter.parseBase64Binary(base64String);
		PKCS8EncodedKeySpec x509 = new PKCS8EncodedKeySpec(privateKeyByte);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		PrivateKey privateKey = null;
		try {
			privateKey = keyFactory.generatePrivate(x509);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static PublicKey getPublicKeyByBase64String(String base64String) {
		byte[] publicKeyByte = DatatypeConverter.parseBase64Binary(base64String);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(publicKeyByte);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		PublicKey publicKey = null;
		try {
			publicKey = keyFactory.generatePublic(x509);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static SecretKey getSecretKeyByBase64String(String base64String,String algorithm) {
		byte[] keyByte = DatatypeConverter.parseBase64Binary(base64String);
		try {
			DESedeKeySpec dks = new DESedeKeySpec(keyByte);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = keyFactory.generateSecret(dks);
			return secretKey;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String createSecretKey() throws Exception {
		Key mykey = KeyGenerator.getInstance("DESede").generateKey();

		return getBase64StringKey(mykey);
	}
}
package com.aibank.ifi.client_demo.util;

import java.nio.charset.Charset;

public class ApiProperties {
	private String appId;//商户id，由百信提供
	private String appPrivateKey;//商户私钥，由商户自主生成，商户自己保存，注意保密。
	private String appPublicKey;//商户公钥，由商户自主生成，提供给百信
	private String appPassword;//商户密码，默认123456，可申请变更
	private String aibankIp;//百信的ip，域名，各个环境不同。由百信提供
	private String aibankSigAlgName;//百信的加签方法，默认：MD5withRSA
	private String aibankCryptAlgName;//百信的加密方法，默认：SM4，
	private String aibankPublicKey;//百信的公钥，默认
	private String aibankBodySecretKey;//加密body的密钥，由百信提供，可申请变更
	private String aibankPasswordSecreKey;//加密password的密钥，由百信提供、
	private String charSet=ClientConstant.DEFAULT_CHARSET;
	private String format="json";
	public ApiProperties(String appId,String appPrivateKey,String appPublicKey,String appPassword,String aibankIp,String aibankSigAlgName,String aibankCryptAlgName,
			String aibankPublicKey,String aibankBodySecretKey,String aibankPasswordSecreKey) {
		this.appId = appId;
		this.appPrivateKey=appPrivateKey;
		this.appPublicKey =appPublicKey;
		this.appPassword=appPassword;
		this.aibankIp=aibankIp;
		this.aibankSigAlgName=aibankSigAlgName;
		this.aibankCryptAlgName=aibankCryptAlgName;
		this.aibankPublicKey=aibankPublicKey;
		this.aibankBodySecretKey =aibankBodySecretKey;
		this.aibankPasswordSecreKey=aibankPasswordSecreKey;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppPrivateKey() {
		return appPrivateKey;
	}
	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}
	public String getAppPublicKey() {
		return appPublicKey;
	}
	public void setAppPublicKey(String appPublicKey) {
		this.appPublicKey = appPublicKey;
	}
	public String getAppPassword() {
		return appPassword;
	}
	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	public String getAibankIp() {
		return aibankIp;
	}
	public void setAibankIp(String aibankIp) {
		this.aibankIp = aibankIp;
	}
	public String getAibankSigAlgName() {
		return aibankSigAlgName;
	}
	public void setAibankSigAlgName(String aibankSigAlgName) {
		this.aibankSigAlgName = aibankSigAlgName;
	}
	public String getAibankCryptAlgName() {
		return aibankCryptAlgName;
	}
	public void setAibankCryptAlgName(String aibankCryptAlgName) {
		this.aibankCryptAlgName = aibankCryptAlgName;
	}
	public String getAibankPublicKey() {
		return aibankPublicKey;
	}
	public void setAibankPublicKey(String aibankPublicKey) {
		this.aibankPublicKey = aibankPublicKey;
	}
	public String getAibankBodySecretKey() {
		return aibankBodySecretKey;
	}
	public void setAibankBodySecretKey(String aibankBodySecretKey) {
		this.aibankBodySecretKey = aibankBodySecretKey;
	}
	public String getAibankPasswordSecreKey() {
		return aibankPasswordSecreKey;
	}
	public void setAibankPasswordSecreKey(String aibankPasswordSecreKey) {
		this.aibankPasswordSecreKey = aibankPasswordSecreKey;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	
}

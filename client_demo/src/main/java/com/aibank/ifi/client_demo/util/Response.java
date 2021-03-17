package com.aibank.ifi.client_demo.util;

import java.util.List;

/**
 * 
 * 
 * @version
 * @description 智融inside的响应
 * @author linyixing
 * @createTime 2018年10月24日
 * @codeReviewer
 */
public class Response {
	private String rspCode; // 六个0代表连接成功，其他代表连接有问题。
	private String rspMsg;
	private String retStatus;
	private List<Result> retResult;
	private String token;
	private String sign;
	private String encryptData;
	private String data;
	public String getRspCode() {
		return rspCode;
	}
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspMsg() {
		return rspMsg;
	}
	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	public String getRetStatus() {
		return retStatus;
	}
	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}
	public List<Result> getRetResult() {
		return retResult;
	}
	public void setRetResult(List<Result> retResult) {
		this.retResult = retResult;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getEncryptData() {
		return encryptData;
	}
	public void setEncryptData(String encryptData) {
		this.encryptData = encryptData;
	}
	
	
}

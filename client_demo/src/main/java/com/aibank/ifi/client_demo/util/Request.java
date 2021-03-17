package com.aibank.ifi.client_demo.util;

public class Request {
	private String appId;
	private String transCode;
	private String server;
	private String data;
	private String txnSrlNo; //请求流水号，唯一码
	private String msgType="01"; //默认为正常交易
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getTxnSrlNo() {
		return txnSrlNo;
	}
	public void setTxnSrlNo(String txnSrlNo) {
		this.txnSrlNo = txnSrlNo;
	}
	
	
}

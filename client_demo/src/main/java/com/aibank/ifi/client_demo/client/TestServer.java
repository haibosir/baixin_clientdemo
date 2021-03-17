package com.aibank.ifi.client_demo.client;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.aibank.ifi.client_demo.util.ApiClient;
import com.aibank.ifi.client_demo.util.ApiProperties;
import com.aibank.ifi.client_demo.util.Business;
import com.aibank.ifi.client_demo.util.Request;
import com.alibaba.fastjson.JSONObject;

public class TestServer {
	public static final ResourceBundle CONFIG_RESOURCE_BUNDLE = ResourceBundle.getBundle("config");
	public static void main(String[] args) {
		//模拟服务端
		String requstString="{\"head\":{\"App_Id\":\"AQY_001\",\"Format\":\"JSON\",\"Charest\":null,\"Version\":null,\"App_Auth_Token\":\"token\",\"Sign_Type\":null,\"Charset\":\"UTF-8\",\"Sign\":\"vqU9e5mPPPaIroNZqaGfSASQGgR/HGzw1ATbO1V87KJsEdjyZ7zZVM99p4dhj+aHvldfTVI0zrZGcwfUHPNKotUgcsjL3TkMCFGJU200Xg5ghJoW2XSefOo7VfiNjzgWy7/pyne7+IV+7m2Zm17fKz6s+Hg4Iwk0lp140l0nMT4=\",\"Tx_CD\":\"WMT0010012\",\"Tx_Dt\":null,\"TxnSrlNo\":null,\"To_SysTem_ID\":\"AQY_001\"},\"body\":{\"msg_content\":\"OmRaoqJ4XpzHBwV4Qwey5LEvV5KLdiLGmY+FV1p6u1cd5BE4tJr6j8noNaoJ1shkMnRMNJyiadglyocj8FLkAQMR7XW7VUSgjDO9Ehm3Z+k43ry0FYkZ6TV69HkAEcZj+8aPhqFYqSsTmaYZEknRi6llmOGFigkY6cpCwJ68XCijtuljpnDKDznanylx+7xx+ArksiUPTvanq/a1eNSAvM1mWIEKJYEuyMP1y/uqs9eOm6agz6l6z0SE7bzpIcalZ4GYzbrjf62nbCJyxnq7HuLAfkg6STvTEguFcJwmlGXx3Rl62V6uKMc+9Hgv5IdiSfNFJ69HzIJaKH0BnLefQUviTb16x6uNmi3QKspvBWmuhVYGy9YoCakC+6lhCkwEyHv0KS4W17b2u5MuI3iHHTFgWYvURux/K9mQaYg4ZWgOaEY1YMKt/hBIL02Hsa1ANR4kHi29EvwLJVwEJgcDVpaIRD7LAaFX7OXKTm6OYw0cvvRTumfnyVLvJ/jf94GiFQraquYJL9Dy38c+RN4A8LvaEM0LY/D7oFl87ezwObI=\"}}";
		String response = serverTest(requstString,new Business() {
			public String excute(String request) {
				//TODO 做业务
				System.out.println("请求为："+request);
				Map<String,String> result = new HashMap<String,String>();
				result.put("returnCode", "000000");
				result.put("returnMsg", "通知成功");
				return JSONObject.toJSONString(result);
			}
		});
		System.out.println(response);
	}
	
	/**
	 * 模拟接收到百信银行的请求
	 * @param data  接收到的百信发送的报文
	 * @return  响应给百信的密文
	 * @throws Exception 
	 */
	private static String serverTest(String requstString,Business business){
		String appId= CONFIG_RESOURCE_BUNDLE.getString("app.id");//商户id，由百信提供
		String appPrivateKey= CONFIG_RESOURCE_BUNDLE.getString("app.privateKey");//商户私钥，由商户自主生成，商户自己保存，注意保密。
		String appPublicKey= CONFIG_RESOURCE_BUNDLE.getString("app.publicKey");//商户公钥，由商户自主生成，提供给百信
		String appPassword= CONFIG_RESOURCE_BUNDLE.getString("app.password");//商户密码，默认123456，可申请变更
		String aibankIp= CONFIG_RESOURCE_BUNDLE.getString("aibank.ip");//百信的ip，域名，各个环境不同。由百信提供
		String aibankSigAlgName= CONFIG_RESOURCE_BUNDLE.getString("aibank.sigAlgName");//百信的加签方法，默认：MD5withRSA
		String aibankCryptAlgName= CONFIG_RESOURCE_BUNDLE.getString("aibank.cryptAlgName");//百信的加密方法，默认：SM4，
		String aibankPublicKey= CONFIG_RESOURCE_BUNDLE.getString("aibank.publicKey");//百信的公钥，默认
		String aibankBodySecretKey= CONFIG_RESOURCE_BUNDLE.getString("aibank.body.secretKey");//加密body的密钥，由百信提供，可申请变更
		String aibankPasswordSecreKey= CONFIG_RESOURCE_BUNDLE.getString("aibank.password.secreKey");//加密password的密钥，由百信提供
		ApiProperties properties = new ApiProperties(appId,appPrivateKey,appPublicKey,appPassword,aibankIp,aibankSigAlgName,aibankCryptAlgName,aibankPublicKey,aibankBodySecretKey,aibankPasswordSecreKey);
		String result="";
		Request request = null;
		
		try {
			request = ApiClient.handleRequest(requstString, properties); // 将百信的请求转成Request,data为已解密的请求报文
		} catch (Exception e) {
			result="客户端请求报文解析异常："+e.getMessage();
		}
		if(StringUtils.isEmpty(result)&&!(request==null)) {
			
			result  = business.excute(request.getData()); //做业务处理，可自行调用，自行处理
		}
		
		String allData="";
		try {
			allData = ApiClient.machiningResponse(properties, result); //对响应内容进行加密加签
		} catch (Exception e) {
			allData="{\"App_Id\":\""+properties.getAppId()+"\",\"Charset\":\"UTF-8\",\"rsp_code\":\"999999\",\"rsp_msg\":\"客户端响应报文组装失败\"}";
		}
		return allData;

	}
}

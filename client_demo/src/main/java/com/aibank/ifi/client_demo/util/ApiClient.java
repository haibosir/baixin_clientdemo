package com.aibank.ifi.client_demo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;


/**
 * 
 * 
 * @version ifi客户端
 * @description 
 * @author linyixing
 * @createTime 2018年10月24日
 * @codeReviewer
 */
public class ApiClient {
	private static String token;
	public static final ResourceBundle CONFIG_RESOURCE_BUNDLE = ResourceBundle.getBundle("config");
	/**
	 * 
	 * @param data  商户报文json串
	 * @param properties 商户的公私钥和百信的公钥，密钥等信息
	 * @param appId 商户id
	 * @param aibankServer 百信业务系统
	 * @param transCode 接口交易码
	 * @return 调用智融inside返回的结果
	 */
	public static Response callApi(Request request, ApiProperties properties) {
		Response resp=null;
		try {
			//获取token
			if(StringUtils.isEmpty(token)) {
				resp = getToken(properties);
			}
			if(StringUtils.isEmpty(token)|| ClientConstant.DEFALUT_TOKEN.equals (token)) {
				resp.setRspMsg("获取token异常："+resp.getRspMsg());
				return resp;
			}
			else{
				//进行交易
				resp  = trans(properties, request,token);
				// 如果是token失效导致交易异常。重新获取token
				if(ClientConstant.PROCESS_TOKEN_FAIL.equals(resp.getRspCode())){
					getToken(properties);
					resp  = trans(properties, request,token);
				}
			}
		}catch(Exception exception) {
			resp = new Response();
			resp.setRetStatus(ClientConstant.PROCESS_STATUS_F);
			resp.setRspCode(ClientConstant.PROCESS_CLIENT_FAIL);
			resp.setRspMsg("客户端异常:"+exception.getMessage());
		}
		return resp;
		
	}
	/**
	 * 
	 * @param data  商户报文json串
	 * @param properties 默认从config文件里面取
	 * @param appId 商户id
	 * @param aibankServer 百信业务系统
	 * @param transCode 接口交易码
	 * @return 调用智融inside返回的结果
	 */
	public static Response callApi(Request request,String configPath) {
		// 配置项获取
		ResourceBundle configBundle = ResourceBundle.getBundle(configPath);
		String appId= configBundle.getString("app.id");//商户id，由百信提供
		String appPrivateKey= configBundle.getString("app.privateKey");//商户私钥，由商户自主生成，商户自己保存，注意保密。
		String appPublicKey= configBundle.getString("app.publicKey");//商户公钥，由商户自主生成，提供给百信
		String appPassword= configBundle.getString("app.password");//商户密码，默认123456，可申请变更
		String aibankIp= configBundle.getString("aibank.ip");//百信的ip，域名，各个环境不同。由百信提供
		String aibankSigAlgName= configBundle.getString("aibank.sigAlgName");//百信的加签方法，默认：MD5withRSA
		String aibankCryptAlgName= configBundle.getString("aibank.cryptAlgName");//百信的加密方法，默认：SM4，
		String aibankPublicKey= configBundle.getString("aibank.publicKey");//百信的公钥，默认
		String aibankBodySecretKey= configBundle.getString("aibank.body.secretKey");//加密body的密钥，由百信提供，可申请变更
		String aibankPasswordSecreKey= configBundle.getString("aibank.password.secreKey");//加密password的密钥，由百信提供
		ApiProperties properties = new ApiProperties(appId,appPrivateKey,appPublicKey,appPassword,aibankIp,aibankSigAlgName,aibankCryptAlgName,aibankPublicKey,aibankBodySecretKey,aibankPasswordSecreKey);
		return ApiClient.callApi(request,properties);
	}
	/**
	 * 
	 * @param request
	 * @return 调用智融inside返回的结果
	 */
	public static Response callApi(Request request) {
		// 配置项获取
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
		return ApiClient.callApi(request,properties);
	}
	/**
     * 根据应用生成token
     * @param properties
     * @return
     */
	private static Response getToken(ApiProperties properties)throws  Exception{
		//对password进行加密
		String encryptPassword =  SimpleCryptUtil.encodeByAlgorithm(properties.getAppPassword(),ClientConstant.DEFAULT_CHARSET,ClientConstant.DEFAULT_DECRYPT,properties.getAibankPasswordSecreKey());
		//将appid和password序列化成string
		Map<String,String> userMap = new HashMap<String,String>();


		userMap.put(ClientConstant.API_CLIENT_NAME,properties.getAppId());
		userMap.put(ClientConstant.API_CLIENT_PASSWORD, encryptPassword);
		String userJson = JSONObject.toJSONString(userMap);
		Request request = new Request();
		request.setAppId(properties.getAppId());
		request.setTransCode(ClientConstant.TOKEN_TRANSCODE);
		request.setServer(ClientConstant.DEFAULT_SERVER);
		request.setData(userJson);
		request.setAppId(properties.getAppId());
		request.setMsgType(ClientConstant.TOKEN_MSG_TYPE);
		request.setTxnSrlNo(SerialNumUtil.creatSerailNum(request.getAppId()));
		//data生成签名
		Response resp = trans(properties, request,ClientConstant.DEFALUT_TOKEN);
		token=resp.getToken();
		return resp;
	}
	private static Response trans(ApiProperties properties, Request request,String token)
			throws Exception {
		//获取签名串
		String sign = getSign(request.getData(),properties);
		//data密文
		String encrypt = getEncrypt(request.getData(),properties);
		//加入报文头，生成了完整报文
		String allData = addHead(request,sign,encrypt,token,properties);
		//生成url
		String apiURL = buildUrl(properties,request);
		System.out.println("密文："+allData);
		//调智融inside
		String result = send(apiURL,allData);

		Response resp = buildResponse(result,properties); 
		if(ClientConstant.PROCESS_CLIENT_SUCCESS.equals(resp.getRspCode())&&resp.getEncryptData()!=null) {
			String decryptResponse = getDecrypt(resp.getEncryptData(), properties);
			if(verifySign(decryptResponse,resp.getSign(),properties)) {//验签
				resp.setData(decryptResponse);
			}
			else{
				resp.setRetStatus(ClientConstant.PROCESS_STATUS_F);
				resp.setRspCode(ClientConstant.PROCESS_CLIENT_FAIL);
				resp.setRspMsg("响应验签异常");
			}
		}
		return resp;
	}
	
	public static String addHead(Request request, String sign, String encrypt, String token,ApiProperties properties) {
		Map<String,Object> root = new HashMap<String,Object>();
		Map<String,Object> head = new HashMap<String,Object>();
		Map<String,Object> body = new HashMap<String,Object>();
		root.put(ClientConstant.HEAD_NODE, head);
		head.put(ClientConstant.HEAD_APP_ID_NODE, request.getAppId());
		head.put(ClientConstant.HEAD_MSG_TYPE_NODE, request.getMsgType());
		head.put(ClientConstant.HEAD_SIGN_NODE, sign);
		head.put(ClientConstant.HEAD_APP_AUTH_TOKEN_NODE,token );
		head.put(ClientConstant.HEAD_CHARSET_NODE, properties.getCharSet());
		head.put(ClientConstant.HEAD_FORMAT_NODE, properties.getFormat());
		head.put(ClientConstant.HEAD_TXNSRLNO_NODE, request.getTxnSrlNo());
		head.put(ClientConstant.HEAD_TO_SYSTEM_ID_NODE, request.getServer());
		head.put(ClientConstant.HEAD_TX_CD_NODE, request.getTransCode());
		body.put(ClientConstant.BODY_MSG_CONTENT_NODE, encrypt);
		root.put(ClientConstant.BODY_NODE, body);
		return JSONObject.toJSONString(root);
	}
	/**
	 * 组装响应
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	private static Response buildResponse(String result , ApiProperties properties) throws Exception {
		Response response = new Response();
		JSONObject root = JSONObject.parseObject(result);
		JSONObject head = root.getJSONObject(ClientConstant.HEAD_NODE);
		JSONObject body = root.getJSONObject(ClientConstant.BODY_NODE);
		response.setRetStatus(head.getString(ClientConstant.HEAD_RETSTATUS_NODE));
		response.setRspCode(head.getString(ClientConstant.HEAD_RSP_CODE_NODE));
		response.setRspMsg(head.getString(ClientConstant.HEAD_RSP_MSG_NODE));
		response.setToken(head.getString(ClientConstant.HEAD_APP_AUTH_TOKEN_NODE));
		response.setSign(head.getString(ClientConstant.HEAD_SIGN_NODE));
		String retResults = head.getString(ClientConstant.HEAD_RETRESULT_NODE);
		if(!StringUtils.isEmpty(retResults)) {
			List<Result> retResult = JSONObject.parseArray(retResults,Result.class);
			response.setRetResult(retResult);
		}
		if(body!=null) {
			response.setEncryptData(body.getString(ClientConstant.BODY_MSG_CONTENT_NODE));
		}
		return response;
	}
	
	public static boolean verifySign(String decryptResponse, String sign, ApiProperties properties) throws UnsupportedEncodingException {
		byte[] msgContent = decryptResponse.getBytes(properties.getCharSet());
		String md5 = MD5Util.encrypt(msgContent);
		try {
			SimpleSignerUtil.verify(md5.getBytes(properties.getCharSet()), sign, properties.getAibankSigAlgName(),
					properties.getAibankPublicKey());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 组装访问的ip
	 * @param properties
	 * @param request
	 * @return
	 */
	
	private static String buildUrl(ApiProperties properties,Request request) {
		return properties.getAibankIp()+"/"+request.getAppId()+"/"+request.getServer()+"/"+request.getTransCode();
	}
	/**
	 * 根据请求报文生成签名
	 * @param data
	 * @param properties
	 * @return
	 * @throws Exception 
	 */
	public static String getEncrypt(String data, ApiProperties properties) throws Exception{
		try {
			return SimpleCryptUtil.encodeByAlgorithm(data, ClientConstant.DEFAULT_CHARSET, properties.getAibankCryptAlgName(), properties.getAibankBodySecretKey());
		} catch (Exception e) {
			throw new Exception("加密异常:"+e.getMessage(), e);
		}
	}
	/**
	 * 根据请求报文生成签名
	 * @param data
	 * @param properties
	 * @return
	 * @throws Exception 
	 */
	public static String getDecrypt(String data, ApiProperties properties) throws Exception{
		try {
			return SimpleCryptUtil.decodeByAlgorithm(data, ClientConstant.DEFAULT_CHARSET, properties.getAibankCryptAlgName(), properties.getAibankBodySecretKey());
		} catch (Exception e) {
			throw new Exception("解密异常:"+e.getMessage(), e);
		}
	}
	/**
	 * 根据请求生成签名串,先MD5摘要，再进行MD5RSA加签
	 * @param data
	 * @param properties
	 * @return
	 */
	public static String getSign(String data, ApiProperties properties) throws Exception{
    	String sign = "";
		try {
			String md5 = MD5Util.encrypt(data.getBytes(properties.getCharSet()));
			String signAlgName = properties.getAibankSigAlgName();
			sign = SimpleSignerUtil.sign(md5.getBytes(properties.getCharSet()), signAlgName,
					properties.getAppPrivateKey());
		}  catch (Exception e) {
			throw new Exception("加签失败",e);
		}
		return sign;
	}
    /**
     * 发送报文
     * @param apiURL
     * @param data
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static String send(String apiURL,String data) throws IOException, GeneralSecurityException{
    	//创建SSLContext对象，并使用我们指定的信任管理器初始化
    	if(apiURL.indexOf(ClientConstant.HTTPS_PROTOCAL)>-1){
    		URL url = new URL(apiURL);
    		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    		// 创建SSLContext对象，并使用我们指定的信任管理器初始化 
    		connection.setDoOutput(true);
    		connection.setDoInput(true);
    		connection.setRequestMethod(ClientConstant.HTTP_METHOD_POST);
    		connection.setRequestProperty(ClientConstant.HTTP_CONTENT_TYPE, ClientConstant.HTTP_APPLICATION_JSON);
    		connection.setConnectTimeout(ClientConstant.TIME_OUT_MILLSECONDS);
    		connection.setReadTimeout(ClientConstant.TIME_OUT_MILLSECONDS);
    		connection.connect();
    		stringToOutputStream(connection.getOutputStream(),data,ClientConstant.DEFAULT_CHARSET);
    		
    		InputStream inputStream = connection.getInputStream();
    		
    		return inputStreamToString(inputStream,ClientConstant.DEFAULT_CHARSET);
    	}
    	else{
    		URL url = new URL(apiURL);
      		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      		connection.setDoOutput(true);
      		connection.setDoInput(true);
      		connection.setRequestMethod(ClientConstant.HTTP_METHOD_POST);
      		connection.setRequestProperty(ClientConstant.HTTP_CONTENT_TYPE, ClientConstant.HTTP_APPLICATION_JSON);
      		connection.setConnectTimeout(ClientConstant.TIME_OUT_MILLSECONDS);
    		connection.setReadTimeout(ClientConstant.TIME_OUT_MILLSECONDS);
      		connection.connect();
      		stringToOutputStream(connection.getOutputStream(),data,ClientConstant.DEFAULT_CHARSET);
      		InputStream inputStream = connection.getInputStream();
      		return inputStreamToString(inputStream,ClientConstant.DEFAULT_CHARSET);
    	}

    }

    private static  void stringToOutputStream(OutputStream outStrm, String data, String charset) throws IOException{
        ByteArrayInputStream in = new ByteArrayInputStream(
                data.getBytes(charset));
        int count = -1;
        byte[] buffer = new byte[1024];
        while ((count = in.read(buffer)) != -1)
            outStrm.write(buffer, 0, count);

        outStrm.flush();
        outStrm.close();
    }
    private static  String inputStreamToString(InputStream in, String charset) throws IOException{
        int bufferSize = 1024;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[bufferSize];
        int count = -1;
        while((count = in.read(data,0,bufferSize)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),charset);
    }
    
    /**
     * 解析请求
     * @param requstString
     * @param properties
     * @return Request
     */
    public static Request handleRequest(String requstString, ApiProperties properties) throws Exception{
		JSONObject requestStr = JSONObject.parseObject(requstString);
		JSONObject head = requestStr.getJSONObject(ClientConstant.HEAD_NODE);
		JSONObject body = requestStr.getJSONObject(ClientConstant.BODY_NODE);
		//获取签名串
		String sign="";
		if(head!=null) {
			sign = head.getString(ClientConstant.HEAD_SIGN_NODE);
		}
		//data密文
		String encrypt="";
		if(body!=null) {
			encrypt = body.getString(ClientConstant.BODY_MSG_CONTENT_NODE);
		}
		//解密
		String decryptData="";
		try {
			decryptData = ApiClient.getDecrypt(encrypt, properties);
		} catch (Exception e) {
			throw new Exception("请求解密异常",e);
		}
		try {
			if(!ApiClient.verifySign(decryptData, sign, properties)) {
				throw new Exception("请求验签失败");
			}
		} catch (UnsupportedEncodingException e) {
			throw new Exception("请求验签异常",e);
		}
		Request request = new Request();
		request.setAppId(head.getString(ClientConstant.HEAD_APP_ID_NODE));
		request.setTransCode(head.getString(ClientConstant.HEAD_TX_CD_NODE));
		request.setTxnSrlNo(head.getString(ClientConstant.HEAD_TXNSRLNO_NODE));
		request.setData(decryptData);//请求报文
		return request;
	}
    /**
     * 组装响应
     * @param properties
     * @param result
     * @return
     */
    public static String machiningResponse(ApiProperties properties, String result) throws Exception{
		//获取签名串
		String responseSign="";
		try {
			responseSign = ApiClient.getSign(result,properties);
		} catch (Exception e) {
			throw new Exception("响应加签异常",e);
		}
		//data密文
		String responseEncrypt="";
		try {
			responseEncrypt = ApiClient.getEncrypt(result,properties);
		} catch (Exception e) {
			throw new Exception("响应加密异常",e);
		}
		Request response = new Request();
		response.setAppId(properties.getAppId());
		response.setData(responseEncrypt);
		//加入报文头，生成了完整报文
		String allData = ApiClient.addHead(response,responseSign,responseEncrypt,ClientConstant.DEFALUT_TOKEN,properties);
		return allData;
	}

	public static String callApi(String apiURL,String data) throws IOException, GeneralSecurityException{
			URL url = new URL(apiURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
//      		connection.setRequestProperty("Content-Type", "text/xml");
			connection.connect();
			stringToOutputStream(connection.getOutputStream(),data,"UTF-8");
			InputStream inputStream = connection.getInputStream();
			return inputStreamToString(inputStream,"utf-8");
		}
}

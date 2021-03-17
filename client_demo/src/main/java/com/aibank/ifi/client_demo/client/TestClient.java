package com.aibank.ifi.client_demo.client;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.aibank.ifi.client_demo.util.ApiClient;
import com.aibank.ifi.client_demo.util.ApiProperties;
import com.aibank.ifi.client_demo.util.Request;
import com.aibank.ifi.client_demo.util.Response;
import com.aibank.ifi.client_demo.util.ResponseHandle;
import com.aibank.ifi.client_demo.util.SerialNumUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @version
 * @description demo客户端，需要客户提供
 * @author linyixing
 * @createTime 2018年10月27日
 * @codeReviewer
 */
public class TestClient {
	public static final ResourceBundle CONFIG_RESOURCE_BUNDLE = ResourceBundle.getBundle("config");
	public static void main(String[] args) {

		//模拟客户端
		String aibankServer= "AI_UCC_001";//百信业务系统编号，由百信提供,此为测试数据
		String transCode= "CAC0010009";//每个交易不一样,由百信提供一组交易码集合,此为测试数据
		
		//业务报文组装,测试数据
		Map<String,Object> data = new HashMap<String,Object>();//请求报文,
		data.put("version","V2");
		data.put("messageCode","12002001");
		data.put("sourcePlatformId","P00012");
		data.put("sourceAppId","P00012A001");
		data.put("tradeSeqNo","181011P00099A001zzzj79710");
		data.put("acctName","黄花四");
		data.put("regPhone","18910966722");
		data.put("identifyType","101");
		data.put("identifyNumber","110101199003073992");
		data.put("productType","CD8000000005");
		data.put("linkedBankCode","102100099996");
		data.put("linkedBankName","中国工商银行");
		data.put("linkedAcctType","03");
		data.put("linkedAcctNo","6215590888771234072");
		data.put("linkedPhone","17877770000");
		data.put("occupationType","y");
		data.put("idExpiryDate","21000101");
		data.put("address","北京市");
		String datas = JSONObject.toJSONString(data);//交易报文json串,由商户组装
		clientTest(datas,transCode,aibankServer,new ResponseHandle() {
			public void excute(Response response) {
				if("000000".equals(response.getRspCode())) {
					//TODO 连接成功处理，进行业务处理
					System.out.println("百信智融inside处理成功:"+response.getData());
				}
				else {
					//TODO 连接失败
					System.out.println("百信智融inside处理失败:"+response.getRspMsg());
				}
			}
			
		});
	}
	/**
	 * 模拟请求智融inside
	 * @param data
	 * @param transCode
	 * @param aibankServer
	 * @return
	 */
	private static void clientTest(String datas,String transCode,String aibankServer,ResponseHandle responseHandle) {
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
		
		// 组装完整请求
		Request request=new Request();
		request.setAppId(appId);
		request.setData(datas);
		request.setServer(aibankServer);
		request.setTransCode(transCode);
		request.setTxnSrlNo(SerialNumUtil.creatSerailNum(request.getAppId()));
		//调用百信服务
		Response response = ApiClient.callApi(request,properties);
		//对百信的响应做业务处理,可执行处理
		responseHandle.excute(response); 
	}
}

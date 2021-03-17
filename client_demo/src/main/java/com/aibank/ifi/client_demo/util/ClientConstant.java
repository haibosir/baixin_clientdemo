package com.aibank.ifi.client_demo.util;

/**
 * 
 * @description 客户端常量类
 * @author linyixing
 * @createTime 2018年10月29日
 * @codeReviewer
 */
public class ClientConstant {
	/**
	 * 流程客户端错误
	 */
	public final static String PROCESS_CLIENT_FAIL = "999998";
	/**
	 * 流程客户端错误
	 */
	public final static String PROCESS_CLIENT_SUCCESS = "000000";

	/**
	 * 流程客户端错误
	 */
	public final static String PROCESS_TOKEN_FAIL = "999999";

	/**
	 * 流程失败
	 */
	public final static String PROCESS_STATUS_F = "F";
	/**
	 * 默认字符集
	 */
	public final static String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 *默认加密算法,DESede 
	 */
	public final static String DEFAULT_DECRYPT= "DESede";
	
	/**
	 * 客户端用户名
	 */
	public final static String API_CLIENT_NAME= "name";
	/**
	 * 客户端用户名
	 */
	public final static String API_CLIENT_PASSWORD= "password";
	/**
	 * 客户端用户名
	 */
	public final static String TOKEN_TRANSCODE= "TOKEN001";
	/**
	 * 客户端用户名
	 */
	public final static String DEFAULT_SERVER= "AIBANK";
	
	/**
	 * 获取token报文
	 */
	public final static String TOKEN_MSG_TYPE= "02";
	
	/**
	 * 正常交易报文
	 */
	public final static String TRANS_MSG_TYPE= "01";
	
	/**
	 * 默认token
	 */
	public final static String DEFALUT_TOKEN= "TOKEN";
	
	/**
	 * head节点
	 */
	public final static String HEAD_NODE= "head";
	/**
	 * body节点
	 */
	public final static String BODY_NODE= "body";
	
	/**
	 * head中的App_Id节点
	 */
	public final static String HEAD_APP_ID_NODE="App_Id";
	
	/**
	 * head中的msg_type节点
	 */
	public final static String HEAD_MSG_TYPE_NODE="msg_type";
	
	/**
	 * head中的msg_type节点
	 */
	public final static String HEAD_SIGN_NODE="Sign";
	
	/**
	 * head中的msg_type节点
	 */
	public final static String HEAD_APP_AUTH_TOKEN_NODE="App_Auth_Token";
	
	/**
	 * head中的Charset节点
	 */
	public final static String HEAD_CHARSET_NODE="Charset";
	
	/**
	 * head中的Format节点
	 */
	public final static String HEAD_FORMAT_NODE="Format";
	/**
	 *  head中的TxnSrlNo节点
	 */
	public final static String HEAD_TXNSRLNO_NODE="TxnSrlNo";
	
	/**
	 *  head中的To_SysTem_ID节点
	 */
	public final static String HEAD_TO_SYSTEM_ID_NODE="To_SysTem_ID";
	/**
	 *  head中的Tx_CD节点
	 */
	public final static String HEAD_TX_CD_NODE="Tx_CD";
	/**
	 *  head中的retStatus节点
	 */
	public final static String HEAD_RETSTATUS_NODE="retStatus";
	/**
	 *  head中的rsp_code节点
	 */
	public final static String HEAD_RSP_CODE_NODE="rsp_code";
	/**
	 *  head中的rsp_msg节点
	 */
	public final static String HEAD_RSP_MSG_NODE="rsp_msg";
	
	/**
	 *  head中的retResult节点
	 */
	public final static String HEAD_RETRESULT_NODE="retResult";
	
	/**
	 *  body中的msg_content节点
	 */
	public final static String BODY_MSG_CONTENT_NODE="msg_content";
	
	/**
	 *  body中的msg_content节点
	 */
	public final static String HTTP_APPLICATION_JSON="application/json";
	/**
	 *  https协议头
	 */
	public final static String HTTPS_PROTOCAL="https";
	
	/**
	 *  http的post方法
	 */
	public final static String HTTP_METHOD_POST="POST";
	/**
	 *  http的报文类型
	 */
	public final static String HTTP_CONTENT_TYPE="Content-Type";
	
	public final static int TIME_OUT_MILLSECONDS=45000;
}

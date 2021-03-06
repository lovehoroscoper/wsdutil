package com.godtips.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * 
 * tip:2012-01-11 如果有卡密形式，卡密保护=号 那么 String[][] 和map 转换有问题，
 * 
 * <p>
 * 应用系统动态密钥安全操作类
 * </p>
 * 
 * 创建人： 创建日期： MSN: Email:
 * 
 * copy by weisd 拿来主义
 * 
 * 是否针对不同的销售平台是否有不同的appKey 是否加密url后面的字段都是verifystring
 * 
 * 
 * 注意：a=xxxx&b=xxx&c=xxx xxx 是否能是中文还是需要在外部转码还未确定 对参数名按照ASCII升序排序 ，这个需要统一
 * 现在是都必须加密，否则需要另修改if (validResponseMap(hm, appKey)) {
 * 
 * 
 * 
 */

public class HttpSignServiceV2 {
	private static Logger log = Logger.getLogger(HttpSignServiceV2.class);

	/**
	 * 
	 * @param resMap
	 *            参数
	 * @param appKeyName
	 *            加密字段
	 * @param appKey
	 *            加密内容
	 * @return
	 */
	public static boolean validResponseString(String resStr, String splitStr, String appKeyName, String appKey) {
		Map<String, String> resMap = createResultMapByStringReqV2(resStr, splitStr);
		return validResponseMap(resMap, appKeyName, appKey);
	}

	/**
	 * 
	 * @param resMap
	 *            参数
	 * @param appKeyName
	 *            加密字段
	 * @param appKey
	 *            加密内容
	 * @return
	 */
	public static boolean validResponseMap(Map<String, String> resMap, String appKeyName, String appKey) {
		// 首先取出密钥，然后把密钥去除
		String verifystring = resMap.get(appKeyName);
		// 先删除
		resMap.remove(appKeyName);
		if (null == verifystring || "".equals(verifystring.trim())) {
			return false;
		}
		//resMap.remove(appKeyName);
		try {
			// 对参数名按照ASCII升序排序
			Object[] key = resMap.keySet().toArray();
			Arrays.sort(key);
			// 生成加密原串
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) {
				if (resMap.get(key[i]) instanceof String) {
					res.append(key[i] + "=" + resMap.get(key[i]) + "&");
				}
			}
			String rStr = res.substring(0, res.length() - 1);
			log.debug("加密原串 : " + rStr);
			// log.info("加密key : " + appKey);
			if (StringUtils.isEmptyOrNullNotTrim(appKey)) {
				return false;
			}
			return verifystring.equals(KeyedDigestMD5.getKeyedDigest(rStr, appKey));
		} catch (Exception e) {
			log.error("creatResSignMap 异常", e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成待发送字符串
	 * 
	 * @param resParam
	 *            请求所有参数
	 * @param appKeyName
	 *            :verifystring
	 * 
	 * @param appKey
	 *            商家静态KEY
	 * @return 需要发送的所有字符串(这里添加了密钥)
	 */
	private static String creatResSign(String[][] resParam, String appKeyName, String appKey) {
		try {
			// 对参数名按照ASCII升序排序
			Arrays.sort(resParam, new Comparator<Object>() {
				public int compare(final Object o1, final Object o2) {
					return ((String[]) o1)[0].compareTo(((String[]) o2)[0]);
				}
			});
			StringBuffer res = new StringBuffer(128);
			for (int m = 0; m < resParam.length; m++) {
				res.append(resParam[m][0] + "=" + resParam[m][1] + "&");
			}
			int length = res.length();
			String rStr = "";
			if (length <= 0) {
				rStr = "";
			} else {
				rStr = res.substring(0, res.length() - 1);
			}
			log.debug("rStr:" + rStr);

			if (StringUtils.isEmptyOrNullByTrim(appKeyName)) {
				return rStr;
			} else {
				if (StringUtils.isEmptyOrNullNotTrim(appKey)) {
					return rStr + "&" + appKeyName.trim() + "=";
				}
				return rStr + "&" + appKeyName.trim() + "=" + KeyedDigestMD5.getKeyedDigest(rStr, appKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 生成待发送字符串
	 * 
	 * @param resParam
	 *            请求所有参数 直接一个字符串
	 * @param appKeyName
	 *            :verifystring
	 * 
	 * @param appKey
	 *            商家静态KEY
	 * @return 需要发送的所有字符串(这里添加了密钥)
	 */
	public static String creatResSign(String rStr, String appKeyName, String appKey) {
		try {
			if (StringUtils.isEmptyOrNullByTrim(appKeyName)) {
				return rStr;
			} else {
				if (StringUtils.isEmptyOrNullNotTrim(appKey)) {
					return rStr + "&" + appKeyName.trim() + "=";
				}
				return rStr + "&" + appKeyName.trim() + "=" + KeyedDigestMD5.getKeyedDigest(rStr, appKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 通过String构造一个请求参数 xxx=aaa&xxx2=bbb
	 * 
	 * @param map
	 * @return
	 */
	public static String creatHttpParamLineString(String resStr, String splitStr, String appKeyName, String appKey) {
		String[][] resParam = createResultArrayByStringReqV2(resStr, splitStr);
		return creatResSign(resParam, appKeyName, appKey);
	}
	
	/**
	 * 通过String构造一个请求参数 xxx=aaa&xxx2=bbb。resStr 中可能已经包含了appKeyName
	 * @param resStr
	 * @param splitStr
	 * @param appKeyName
	 * @param appKey
	 * @param removeStrAppKeyName  resStr 中可能已经包含了appKeyName
	 * @return
	 */
	public static String creatHttpParamLineString(String resStr, String splitStr, String appKeyName, String appKey, String removeStrAppKeyName) {
		Map<String, String> hm = createResultMapByStringReqV2(resStr, splitStr);
		if(hm != null && !StringUtils.isEmptyOrNullByTrim(removeStrAppKeyName)){
			hm.remove(removeStrAppKeyName);
		}
		return creatHttpParamLineString(hm, appKeyName, appKey);
	}

	/**
	 * 通过Map构造一个请求参数 xxx=aaa&xxx2=bbb
	 * 
	 * @param map
	 * @return
	 */
	public static String creatHttpParamLineString(Map<String, String> map, String appKeyName, String appKey) {
		String[][] param = new String[map.size()][2];
		int index = 0;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			param[index][0] = (String) entry.getKey();
			param[index][1] = (String) entry.getValue();
			index++;
		}
		String req = creatResSign(param, appKeyName, appKey);
		return req;
	}

	/**
	 * 传入一个字符串的请求参数，拆分成为 键值对应的map
	 * 
	 * v2 开门包含 多个=
	 * 
	 * @param resStr
	 *            a=xxxx&b=xxx&c=xxx
	 * @param splitStr
	 *            "&"
	 * @return
	 */
	public static Map<String, String> createResultMapByStringReqV2(String resStr, String splitStr) {
		if (null == splitStr || "".equals(splitStr.trim())) {
			splitStr = "&";
		}
		HashMap<String, String> hm = new HashMap<String, String>();
		if (null == resStr || "".equals(resStr.trim())) {
			//
		} else {
			String[] para = resStr.split(splitStr);
			for (int i = 0; i < para.length; i++) {
				// 判断=号的位置
				int start = para[i].indexOf("=");
				if (start > -1) {
					hm.put(para[i].substring(0, start), para[i].substring(start + 1, para[i].length()));
				} else {
					hm.put(para[i], "");
				}
			}
		}
		return hm;
	}

	/**
	 * 传入一个字符串的请求参数，拆分成为 键值对应的数组 String[][]
	 * 
	 * @param resStr
	 *            a=xxxx&b=xxx&c=xxx
	 * @param splitStr
	 *            "&"
	 * @return
	 */
	public static String[][] createResultArrayByStringReqV2(String resStr, String splitStr) {
		if (null == splitStr || "".equals(splitStr.trim())) {
			splitStr = "&";
		}
		String[][] param = null;
		if (null == resStr || "".equals(resStr.trim())) {
			param = new String[][] {};
		} else {
			String[] para = resStr.split(splitStr);
			param = new String[para.length][2];
			for (int i = 0; i < para.length; i++) {
				// 判断=号的位置
				int start = para[i].indexOf("=");
				if (start > -1) {
					param[i][0] = para[i].substring(0, start);
					param[i][1] = para[i].substring(start + 1, para[i].length());
				} else {
					param[i][0] = para[i];
					param[i][1] = "";
				}
			}
		}
		return param;
	}

	/**
	 * 发送信息给,并且接收返回信息
	 * 
	 * @param map
	 *            包含接口中所有参数的HashMap 不包含“接口加密摘要” 字段，其对应默认的key为：verifystring
	 * @param url
	 *            接口请求地址
	 * @return 如果成功并且验证通过就返回相应的Map 里面包含所有相应的信息，否则返回null
	 */
	public Map<String, String> sendResponse(Map<String, String> map, String url, int readTimeout, int connectTimeout, String appKeyName, String appKey, boolean isValidResult) {
		String[][] param = new String[map.size()][2];
		int index = 0;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			param[index][0] = (String) entry.getKey();
			param[index][1] = (String) entry.getValue();
			index++;
		}
		return sendResponseNow(param, url, readTimeout, connectTimeout, appKeyName, appKey, isValidResult);
	}

	/**
	 * 返回字符串形式,不对结果进行验证
	 * 
	 * @param map
	 * 
	 * @param url
	 * 
	 * @return 字符串
	 */
	public String sendResponseReString(Map<String, String> map, String url, int readTimeout, int connectTimeout, String appKeyName, String appKey, boolean isValidResult) {
		String[][] param = new String[map.size()][2];
		int index = 0;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			param[index][0] = (String) entry.getKey();
			param[index][1] = (String) entry.getValue();
			index++;
		}
		return sendResponseReString(param, url, readTimeout, connectTimeout, appKeyName, appKey, isValidResult);
	}

	/**
	 * 发送信息给,并且接收返回信息
	 * 
	 * @param paramStr
	 *            包含接口中所有参数的string
	 * @param url
	 *            接口请求地址
	 * @param readTimeout
	 *            秒
	 * @param connectTimeout
	 * @param appKeyName
	 * @param appKey
	 * @param isValidResult
	 *            是否验证结果
	 * @return 如果成功并且验证通过就返回相应的Map 里面包含所有相应的信息，否则返回null
	 */
	public Map<String, String> sendResponseByString(String paramStr, String url, int readTimeout, int connectTimeout, String appKeyName, String appKey, boolean isValidResult) {
		String[][] param = createResultArrayByStringReqV2(paramStr, "&");
		return sendResponseNow(param, url, readTimeout, connectTimeout, appKeyName, appKey, isValidResult);
	}

	/**
	 * 最终的接口实现发送
	 * 
	 * TODO 所有用到这个类的都要更新
	 * 
	 * @param param
	 * @param url
	 * @param readTimeout
	 * @param connectTimeout
	 * @param appKeyName
	 * @param appKey
	 * @param isValidResult
	 * @return
	 */
	private Map<String, String> sendResponseNow(String[][] param, String url, int readTimeout, int connectTimeout, String appKeyName, String appKey, boolean isValidResult) {
		String reqStr = creatResSign(param, appKeyName, appKey);
		log.debug("发送字符串:" + reqStr);
		// 这里发送数据并且接收数据
		String resStr = exe(readTimeout, connectTimeout, reqStr, url);// 调用接口
		log.debug("返回字符串:" + resStr);
		String[] para = resStr.split("&");
		HashMap<String, String> hm = new HashMap<String, String>();
		for (int i = 0; i < para.length; i++) {
			// 判断=号的位置
			int start = para[i].indexOf("=");
			if (start > -1) {
				hm.put(para[i].substring(0, start), para[i].substring(start + 1, para[i].length()));
			} else {
				hm.put(para[i], "");
			}
		}
		if (isValidResult) {
			// 获取所有回执参数信息，然后验证结果，这样如果调用该类的方法都是必须加密的
			if (validResponseMap(hm, appKeyName, appKey)) {
				return hm;
			}
		} else {
			return hm;
		}
		return null;
	}

	/**
	 * 
	 * 返回字符串形式,不对结果进行验证
	 * 
	 * @param param
	 * @param url
	 * @param readTimeout
	 * @param connectTimeout
	 * @param appKeyName
	 * @param appKey
	 * @param isValidResult
	 * @return
	 */
	private String sendResponseReString(String[][] param, String url, int readTimeout, int connectTimeout, String appKeyName, String appKey, boolean isValidResult) {
		String reqStr = creatResSign(param, appKeyName, appKey);
		log.debug("发送字符串:" + reqStr);
		String resStr = exe(readTimeout, connectTimeout, reqStr, url);// 调用接口
		log.debug("返回字符串:" + resStr);
		return resStr;
	}
	
	/**
	 * 通知接口的MD5发送
	 * @param paramStr
	 * @param url
	 * @param readTimeout
	 * @param connectTimeout
	 * @param appKeyName
	 * @param appKey
	 * @param isValidResult
	 * @return
	 */
	public String sendResponseNotice(String paramStr, String url, int readTimeout, int connectTimeout, String appKeyName, String appKey, boolean isValidResult) {
		log.debug("发送字符串:" + paramStr);
		String resStr = exe(readTimeout, connectTimeout, paramStr, url);// 调用接口
		log.debug("返回字符串:" + resStr);
		return resStr;
	}

	/**
	 * 
	 * @param readTimeout
	 *            秒
	 * @param connectTimeout
	 *            秒
	 * @param str
	 *            a=xxxx&b=xxx&c=xxx
	 * @param url
	 *            http://asdfasdf.do?wwww=xxxx
	 * @return
	 */
	private String exe(int readTimeout, int connectTimeout, String str, String url) {
		log.debug("Send str:" + str);
		StringBuffer responseMessage = null;
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;
		try {
			responseMessage = new StringBuffer(64);
			reqUrl = new java.net.URL(url);
			// JDK
			// 1.5以前的版本，只能通过设置这两个系统属性来控制网络超时。在1.5中，还可以使用HttpURLConnection的父类URLConnection的以下两个方法：
			// setConnectTimeout：设置连接主机超时（单位：毫秒）
			// setReadTimeout：设置从主机读取数据超时（单位：毫秒）
			connection = (java.net.HttpURLConnection) reqUrl.openConnection();
			connection.setReadTimeout(readTimeout * 1000);// 50000
			connection.setConnectTimeout(connectTimeout * 1000);// 100000
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			reqOut = new OutputStreamWriter(connection.getOutputStream());
			reqOut.write(str);
			reqOut.flush();
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((charCount = br.read()) != -1) {
				responseMessage.append((char) charCount);
			}
		} catch (Exception e) {
			log.error("HttpSignService send message has error:" + e);
			if (responseMessage != null) {
				return responseMessage.toString();
			} else {
				return "";
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (reqOut != null) {
					reqOut.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseMessage.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String res = "comm=8001&version=1.0&onlineid=107620";
//		String res = "comm=8101&onlineid=107620&resultno=0000&verifystring=12323";
//		String cr = creatHttpParamLineString(res,"&","verifystring","123456");
//		String cr2 = creatHttpParamLineString(res,"&","verifystring","123456","verifystring");
//		System.out.println(cr);
//		System.out.println(cr2);
		
//		String resStr = "comm=800=1&version=1.0&onlineid=107620";
//		String resStr = "";
//		String[] para = resStr.split("&");
//		HashMap<String, String> hm = new HashMap<String, String>();
//		for (int i = 0; i < para.length; i++) {
//			// 判断=号的位置
//			int start = para[i].indexOf("=");
//			if (start > -1) {
//				hm.put(para[i].substring(0, start), para[i].substring(start + 1, para[i].length()));
//			} else {
//				hm.put(para[i], "");
//			}
//		}
//		System.out.println(hm);
	}
}

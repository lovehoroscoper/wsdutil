package com.weisd.apache.httpclient.wsd;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.hisunsray.commons.res.Config;
import com.junbao.hf.utils.common.DateUtils;
import com.weisd.http.MD5Util;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-3 下午1:25:51
 */
public class TestTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String mobilenum = "15201386005";// 电话号码
		String amount = "0";
		String agentid = "DE201109221047570860";
		String source = "2";
		String key = "0630888623174983b1af1d1f4f0d0e4089d2c0d3a6e64feaab4d489a80f2d6fe96aecd8b0dd24132b0dae9e73b932988a1ac4a5c0a4943afb7247b3aace0516e";
  		StringBuffer verifystring2 = new StringBuffer();
		verifystring2.append("agentid=").append(agentid)
					 .append("&amount=").append(amount)
					 .append("&mobilenum=").append(mobilenum)
					 .append("&source=").append(source)
					 .append("&merchantKey=").append(key);
		
		String verifystring = MD5Util.getKeyedDigest(verifystring2.toString(),"");
//		String host = "http://172.25.25.161:8088/esales/product/directProductEbm.do?";
		String host = "http://172.25.25.123:8080/esales/product/directProductEbm.do?";
		String req = "agentid=" + agentid + "&amount=" + amount + "&mobilenum="+ mobilenum +"&source=" + source + "&verifystring=" + verifystring;

		String url = host + req;
		
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			// 请求超时 
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			// 读取超时
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			HttpGet httpget_vcode = new HttpGet(url);
			HttpResponse response_vcode;
			try {
				System.out.println(DateUtils.getFormatCurrDate("yyyy-MM-dd HH:mm:ss"));
				response_vcode = httpclient.execute(httpget_vcode);
				HttpEntity entity_recharge = response_vcode.getEntity();
				String htmlStr_recharge = EntityUtils.toString(entity_recharge, "UTF-8"); // 获取一个字段
				System.out.println("输出:" + htmlStr_recharge);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(DateUtils.getFormatCurrDate("yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

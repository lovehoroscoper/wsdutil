package com.weisd.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-14 上午9:36:27
 */
public class TestGetProd {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

//		String mobilenum = "15201386003";// 电话号码
//		String mobilenum = "15982271111";// 电话号码
//		String mobilenum = "13982271111";// 电话号码
//		String mobilenum = "15982371111";// 电话号码
		String mobilenum = "15201386001";// 电话号码
		String amount = "0";
		String agentid = "DE201109221047570860";
//		String agentid = "DE201109221047570862";
		String source = "2";
//		String key = "0630888623174983b1af1d1f4f0d0e4089d2c0d3a6e64feaab4d489a80f2d6fe96aecd8b0dd24132b0dae9e73b932988a1ac4a5c0a4943afb7247b3aace0516e";
		String key = "0630888623174983b1af1d1f4f0d0e4089d2c0d3a6e64feaab4d489a80f2d6fe96aecd8b0dd24132b0dae9e73b932988a1ac4a5c0a4943afb7247b3aace0516e";

  		StringBuffer verifystring2 = new StringBuffer();
		verifystring2.append("agentid=").append(agentid)
					 .append("&amount=").append(amount)
					 .append("&mobilenum=").append(mobilenum)
					 .append("&source=").append(source)
					 .append("&merchantKey=").append(key);
		
		String verifystring = MD5Util.getKeyedDigest(verifystring2.toString(),"");
		
//		String host = "http://172.25.25.94:8086/hforder/acquiring/acquire_acquire.do";
//		String host = "http://172.25.25.94:8080/esales/product/directProductEbm.do";
//		String host = "http://172.25.25.123:8080/esales/product/directProductEbm.do";
		String host = "http://172.25.25.161:8088/esales/product/directProductEbm.do";
//		String host = "http://172.25.25.123:8080/esales/product/directProductEbm.do";
		String req = "agentid=" + agentid + "&amount=" + amount + "&mobilenum="+ mobilenum +"&source=" + source + "&verifystring=" + verifystring;


		StringBuffer responseMessage = null;
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;

		responseMessage = new StringBuffer(64);
		reqUrl = new java.net.URL(host);

		connection = (java.net.HttpURLConnection) reqUrl.openConnection();
		connection.setReadTimeout(50000);
		connection.setConnectTimeout(100000);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
//		connection.setRequestMethod("GET");
		reqOut = new OutputStreamWriter(connection.getOutputStream());
		reqOut.write(req);
		reqOut.flush();

		in = connection.getInputStream();
		br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		while ((charCount = br.read()) != -1) {
			responseMessage.append((char) charCount);
		}
		String ss = responseMessage.toString();
		System.out.println(ss);

		// System.out.println(index);

	}

}

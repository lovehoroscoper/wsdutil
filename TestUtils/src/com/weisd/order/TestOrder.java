package com.weisd.order;

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
public class TestOrder {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub 
		String mobilenum = "15999561234";// 电话号码
		String onlineid = "106994";
		String agentid = "weisd";
		String orderid = "aas1aaaaa3aaa7";
		String chargeamount = "30";
		String payamount = "30";
		String hforderid = "JB011108141100466888";

		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");// yyyyMMddHHmmss
		String ordertime = f.format(new Date());// 下单时间
		 String host = "http://172.25.25.161:8181/hforder/acquiring/acquire_acquire.do";
//		String host = "http://172.25.53.86:8180/hforder/acquiring/acquire_acquire.do";
		String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=1&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
				+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
//		String reqX = "comm=8010&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=1&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//				+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
//		String reqS = "comm=8011&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=1&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//				+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test&hforderid=" + hforderid;
//		String reqC = "comm=8002&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=1&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//				+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";

//		System.out.println(reqX);
		
//		String req = "";
//		req = reqX;
//		req = reqS;
//		req = reqC;

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

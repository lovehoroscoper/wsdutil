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

	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub 
		String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
		int c = (int) (Math.random() * 100 + 10);
		if(c > 99){
			c = c - 10;
		}
		
		//{
//		chargeamount=50.0, 
//				comm=8001, 
//				onlineid=101090, 
//				verifystring=, 
//				mobilenum=13710563512, 
//				payamount=49.33, 
//				mark=, 
//				ordertime=20111128002048, 
//				orderid=2011112810375074, 
//				ordersource=2, 
//				agentid=ebaimi_jquery_PHONE, 
//				version=1.0}
		
//		String mobilenum = "186517701" + c;// 电话号码江苏
//		String onlineid = "108720";
//		String agentid = "PHONE";
//		String orderid = ordertime ;
//		String chargeamount = "1";
//		String payamount = "1.1";
//		String ordersource = "2";
		
		String mobilenum = "130236100" + c;// 电话号码江苏
		String onlineid = "108721";
		String agentid = "PHONE";
		String orderid = ordertime ;
		String chargeamount = "1";
		String payamount = "1.1";
		String ordersource = "2";

		 String host = "http://172.25.25.161:8181/hforder/acquiring/acquire_acquire.do";
//		String host = "http://172.25.53.86:8180/hforder/acquiring/acquire_acquire.do";
		String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
				+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=充值完成222222222";
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

package com.weisd.thread.manyClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;


public class TestOrderRunnable implements Runnable {
	private static Logger logger = Logger.getLogger(TestOrderRunnable.class);

	private int index;

	private Semaphore semp;

	// final int NO = index;

	public TestOrderRunnable() {
	}

	public TestOrderRunnable(int index, Semaphore semp) {
		this.index = index;
		this.semp = semp;
	}

	public void run() {
		//logger.info("第：" + index + " 个 result |||" );
		try {

			// 获取许可
//			String orderid = "" + (index);
			String orderid = "JB0000" + (index+1);

			semp.acquire();
//			System.out.println("Thread:" + index);
//			String host = "http://127.0.0.1:8086/hforder/acquiring/acquire!weisdTest";
//			String host = "http://192.168.2.232:8086/hforder/acquiring/acquire!acquire";
			String host = "http://127.0.0.1:8086/hforder/acquiring/acquire!acquire";
//			String host = "http://192.168.2.201:8085/hforder/acquiring/acquire!acquire";
			//String host = "http://192.168.2.96:8580/hforder/acquiring/acquire!acquire";
//			String host = "http://192.168.2.201:9020/hforder/acquiring/acquire!acquire";

			String req = "comm=8001&version=1.0&onlineid=1&agentid=-1&ordersource=1&orderid=" + orderid + "&mobilenum=13064524444&chargeamount=100.00&payamount=100.00&ordertime=201105311802&mark=";

			
			
//			System.out.println(host + req);

//			URL url = new URL(host);// 此处填写供测试的url
//
////			HttpURLConnection connection = (HttpURLConnection) url
//
//			.openConnection();

			
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
//			connection.setRequestMethod("GET");
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
			
			
//			System.out.println(ss);
			
			// connection.setRequestMethod("POST");

			// connection.setRequestProperty("Proxy-Connection",

			// "Keep-Alive");


//			System.out.println("第：" + index + " 个");
			logger.info("第：" + index + " 个 result |||" + ss);

			semp.release();

		} catch (Exception e) {

			//e.printStackTrace();
			logger.error("第：" + index + " 个 result |||" + e);

		}

	}
	
	public void run2() {

		StringBuffer responseMessage = null;
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;
//		String host = "http://192.168.2.96:8580/hforder/acquiring/acquire!acquire";
		String host = "http://192.168.2.201:8085/hforder/acquiring/acquire!acquire";
		
		String orderid = "JB00000" + index;
		String req = "comm=8001&version=1.0&onlineid=1&agentid=weisd&ordersource=1&orderid=" + orderid + "&mobilenum=15201386005&chargeamount=1.00&payamount=1.00&ordertime=20110525&mark=";

		try {
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
		} catch (Exception e) {
			e.printStackTrace();
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

		String ss = responseMessage.toString();
		System.out.println(ss);

	}


}
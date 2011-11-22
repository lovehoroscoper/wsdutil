package com.weisd.esales;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-9 上午10:29:50
 */
public class TestOrderToEsales {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 20; i < 40; i++) {
			String num = i + "";
			try {
				String host = "http://172.25.25.123:8080/esales/recharge/directrecharge.do";
//				String host = "http://172.25.25.161:8088/esales/directrecharge.do";
				String agentid = "DE201107261541540282";
				String miyao = "7eec863d72344603aa13ec98a9aad12741c4f5b3f70b43febdccc1e36f5aa72536185700737a487eb9708f6b89e9e29a9286302b4e334aa783ff5f15787e9a4f";
				String productid = "107504";//北京移动30
				//String productid = "108621";//北京移动100
				String mobilenum = "152013860" + i;
				String returntype = "2";
				String orderId = "weisd" + Math.random() * 100;
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
				String  dateTime= simpleDate.format(new Date());

				StringBuffer sb = new StringBuffer();
				sb.append("prodid=").append(productid)
				.append("&agentid=").append("DE201107261541540282")  
				.append("&backurl=").append("")
				.append("&returntype=").append("2")
				.append("&orderid=").append(orderId == null ? "" :orderId)
				.append("&mobilenum=").append(mobilenum)
				.append("&datetime=").append(dateTime == null ? "" :dateTime)
				.append("&source=").append("esales")
				.append("&mark=")
				.append("&merchantKey=").append(miyao);  
				String  verString = KeyedDigestMD5.getKeyedDigest(sb.toString(), "");
				
				
				StringBuffer sb2 = new StringBuffer();
				sb2.append("prodid=").append(productid)
				.append("&agentid=").append("DE201107261541540282")  
				.append("&backurl=").append("")
				.append("&returntype=").append("2")
				.append("&orderid=").append(orderId == null ? "" :orderId)
				.append("&mobilenum=").append(mobilenum)
				.append("&datetime=").append(dateTime == null ? "" :dateTime)
				.append("&source=").append("esales")
				.append("&mark=")
				.append("&verifystring=").append(verString);  
				
				

				// System.out.println(host + req);

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
				reqOut.write(sb2.toString());
				reqOut.flush();

				in = connection.getInputStream();
				br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				while ((charCount = br.read()) != -1) {
					responseMessage.append((char) charCount);
				}
				String ss = responseMessage.toString();
				System.out.println(ss);

			} catch (Exception e) {

				e.printStackTrace();

			}
		}
	}

}

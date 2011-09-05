package com.weisd.esales;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-9 上午10:29:50
 */
public class TestOrder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String host = "http://172.25.25.161:8686/recharge/recharge/recharge.do";
//			String host = "http://172.25.53.87/esales/recharge/recharge.do";
			
//			String host =  "http://172.25.53.87/esales/recharge/directrecharge.do";

			
//			String agentid = "DE201108041306540420";
//			String miyao = "abcee8c1242b44a29a7865b04d76ccb0214d964c3b184dd2bf530472ba6204f08b3cfe4783fc4237823a768fdde9bfd767e297d7e751440cad11dda8cd6c0bd7";
			String agentid = "DE201107261509020281";
			String miyao = "19a687c54b13436297428a38db9fc202e2a74534ac7142c5bb7ba790a19aa50863c94fc4363e416784368f29e716ac607409a0e080fa4b08b08fbd45f2d9576e";
//			String agentid = "DE201108041306540420";
//			String miyao = "abcee8c1242b44a29a7865b04d76ccb0214d964c3b184dd2bf530472ba6204f08b3cfe4783fc4237823a768fdde9bfd767e297d7e751440cad11dda8cd6c0bd7";
//			String productid = "106541";//广西 全国
//			String mobilenum = "13407845521";
//			String productid = "106928";//天津移动  成功
//			String mobilenum = "13512400010";
			String productid = "106998";//北京移动
			String mobilenum = "15201386006";
//			String productid = "101826";//广东湛江联通
//			String mobilenum = "13512050000";
//			String productid = "13225011231";//安徽移动
//			String mobilenum = "13225011232";
//			String productid = "106910";//安徽移动
//			String mobilenum = "18255435555";
//			String productid = "107109";//广东联通  成功
//			String mobilenum = "15521013333";
//			String productid = "106929";//浙江移动
//			String mobilenum = "13588771142";
//			String productid = "107297";//浙江电信 
//			String mobilenum = "13306562222";
//			String productid = "106597";//浙江联通  30 走接引
//			String mobilenum = "15505750003";
//			String productid = "107069";//浙江联通   --终于找到一笔无渠道
//			String mobilenum = "15505750009";
//			String productid = "106598";//安徽联通 
//			String mobilenum = "15505080003";
//			String productid = "106684";//
//			String mobilenum = "15999730002";
//			String productid = "107012";//广西移动
//			String mobilenum = "18260900003";
//			String productid = "106840";//新疆联通
//			String mobilenum = "15509070005";
//			String productid = "107104";//新疆联通
//			String mobilenum = "15527009999";
//			String productid = "107104";//新疆联通
//			String mobilenum = "15527001112";
			String returntype = "2";

			String req = "agentid=" + agentid + "&miyao=" + miyao + "&productid=" + productid +  
					"&mobilenum=" + mobilenum + "&returntype="  + returntype;

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
			reqOut.write(req);
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

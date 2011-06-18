package com.weisd.thread.manyClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TestOrder {

	private static int thread_num = 50;

//	private static int client_num = 460;
	private static int client_num = 100;

	private static Map keywordMap = new HashMap();

	static {

		try {

			InputStreamReader isr = new InputStreamReader(new FileInputStream(

			new File("D:/log/clicks.txt")), "GBK");

			BufferedReader buffer = new BufferedReader(isr);

			String line = "";

			while ((line = buffer.readLine()) != null) {

				keywordMap.put(line.substring(0, line.lastIndexOf(":")), "");

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
		TestOrder t = new TestOrder();

		
		int size = keywordMap.size();

		// TODO Auto-generated method stub

		ExecutorService exec = Executors.newCachedThreadPool();

		// 50个线程可以同时访问

		final Semaphore semp = new Semaphore(thread_num);

		// 模拟2000个客户端访问

		for (int index = 0; index < client_num; index++) {

			// final int NO = index;

			TestOrderRunnable run = new TestOrderRunnable(index+1, semp);

			exec.execute(run);

		}

		// 退出线程池

		exec.shutdown();
		
//		t.test();

	}

	private static String getRandomSearchKey(final int no) {

		String ret = "";

		int size = keywordMap.size();

		// int wanna = (int) (Math.random()) * (size - 1);

		ret = (keywordMap.entrySet().toArray())[no].toString();

		ret = ret.substring(0, ret.lastIndexOf("="));

		System.out.println("\t" + ret);

		return ret;

	}
	
	public void test() {
		StringBuffer responseMessage = null;
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;
		String host = "http://192.168.2.96:8580/hforder/acquiring/acquire!acquire2";
		String orderid = "JB00010";
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


	public static String getOrderString() {

		return null;

		// <tr>
		// <td align="center" nowrap="nowrap"><strong>产品货架id：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="onlineid" type="text" name="onlineid" value="1"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>代理商id：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="agentid" type="text" name="agentid" value="00000001"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>订单来源ID：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="ordersource" type="text" name="ordersource" value="1"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>销售平台订单号：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="orderid" type="text" name="orderid" value="00000001"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>充值号码：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="mobilenum" type="text" name="mobilenum" value="18905718888"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>充值金额：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="chargeamount" type="text" name="chargeamount" value="50"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>订单支付金额：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="payamount" type="text" name="payamount" value="50"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>销售平台订单时间：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="ordertime" type="text" name="ordertime" value="${time}"/></td>
		// </tr>
		// <tr>
		// <td align="center" nowrap="nowrap"><strong>预留字段：</strong></td>
		// <td align="left" nowrap="nowrap"><input id="mark" type="text" name="mark" value="mark" /></td>
		// </tr>

		// String req = "comm=8001&version=1.0&onlineid=1&agentid=weisd&ordersource=1&orderid=JB000001&mobilenum=15201386005&chargeamount=1.00&payamount=1.00&ordertime=20110525&mark="
		// ;

	}

}

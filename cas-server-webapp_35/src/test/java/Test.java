import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jasig.cas.client.util.CommonUtils;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-2-18 下午7:05:19
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://ssl.kankan21.net/serviceValidate?ticket=ST-3-V41iEH7Bovt0psNPv5VX-ssl.kankan21.net&service=http%3A%2F%2Fssl.kankan21.net%2Fservices%2Fj_acegi_cas_security_check";

		// System.out.println(CommonUtils.getResponseFromServer(url, "UTF-8"));

		Test t = new Test();
		System.out.println(t.exe(50, 50, "", url));

	}

//	public String ett() {
//		String url = "http://www.newsmth.net/bbslogin2.php";
//		PostMethod postMethod = new PostMethod(url);
//		// 填入各个表单域的值
//		NameValuePair[] data = { new NameValuePair("id", "youUserName"), new NameValuePair("passwd", "yourPwd") };
//		// 将表单的值放入postMethod中
//		postMethod.setRequestBody(data);
//		// 执行postMethod
//		int statusCode = httpClient.executeMethod(postMethod);
//		// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
//		// 301或者302
//		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
//			// 从头中取出转向的地址
//			Header locationHeader = postMethod.getResponseHeader("location");
//			String location = null;
//			if (locationHeader != null) {
//				location = locationHeader.getValue();
//				System.out.println("The page was redirected to:" + location);
//			} else {
//				System.err.println("Location field value is null.");
//			}
//		}
//	}

	private String exe(int readTimeout, int connectTimeout, String str, String url) {
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
}

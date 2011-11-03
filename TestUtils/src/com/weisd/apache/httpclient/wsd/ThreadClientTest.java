/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.weisd.apache.httpclient.wsd;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class ThreadClientTest {

	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 1; i++) {
		for (int i = 0; i < 3; i++) {
			MyThreadT m = new MyThreadT(i);
			Thread t = new Thread(m);
			t.start();
		}

	}

}

class MyThreadT implements Runnable {

	private int n;

	public MyThreadT(int n) {
		this.n = n;

	}

	public MyThreadT() {

	}

	@Override
	public void run() {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {

			//
			HttpGet httpget = new HttpGet("http://www.iteye.com/login");
			HttpResponse response0 = httpclient.execute(httpget);
			BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(response0.getEntity().getContent()));
			// StringBuffer temp = new StringBuffer();
			// String line = bufferedReader.readLine();
			// while (line != null) {
			// temp.append(line).append("\r\n");
			// line = bufferedReader.readLine();
			// }
			// bufferedReader.close();
			//
			// System.out.println(temp.toString());

			// 登录界面
			OutputStreamWriter osw0 = new OutputStreamWriter(new FileOutputStream("D:\\http\\tologin" + n + ".html"));
			String line0 = bufferedReader0.readLine();
			while (line0 != null) {
				osw0.write(line0 + "\r\n");
				line0 = bufferedReader0.readLine();
			}
			bufferedReader0.close();
			osw0.flush();
			
			
			CookieStore cs =  httpclient.getCookieStore();
			List<Cookie> list = cs.getCookies();
//			System.out.println(httpclient.getCookieStore());
			for (int i = 0; i < list.size(); i++) {
				Cookie c = (Cookie)list.get(i);
				System.out.println(n + "--cook开始----->" + c.getName() + "||" + c.getValue());
			}

//			if(n == 0){
			if(n < 5 ){
				// 登录
				HttpPost httpost = new HttpPost("http://www.iteye.com/login?name=weisd007&password=weisd123456&button=%E7%99%BB%E3%80%80%E5%BD%95");
				HttpResponse response1 = httpclient.execute(httpost);
				BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
				// 登录后
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("D:\\http\\login" + n + ".html"));
				String line = bufferedReader1.readLine();
				while (line != null) {
					osw.write(line + "\r\n");
					line = bufferedReader1.readLine();
				}
				bufferedReader1.close();
				osw.flush();
				
				CookieStore cs2 =  httpclient.getCookieStore();
				List<Cookie> list2 = cs2.getCookies();
				System.out.println(httpclient.getCookieStore());
				for (int i = 0; i < list2.size(); i++) {
					Cookie c = (Cookie)list2.get(i);
					System.out.println(n + "--cook登录----->" + c.getName() + "||" + c.getValue());
				}
			}


			// 登录后
			HttpGet httpget2 = new HttpGet("http://app.iteye.com/profile");
			HttpResponse response2 = httpclient.execute(httpget2);
			BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
			OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream("D:\\http\\admin" + n + ".html"));
			String line2 = bufferedReader2.readLine();
			while (line2 != null) {
				osw2.write(line2 + "\r\n");
				line2 = bufferedReader2.readLine();
			}
			bufferedReader2.close();
			osw2.flush();


			
			CookieStore cs2 =  httpclient.getCookieStore();
			List<Cookie> list2 = cs2.getCookies();
			System.out.println(httpclient.getCookieStore());
			for (int i = 0; i < list2.size(); i++) {
				Cookie c = (Cookie)list2.get(i);
				System.out.println(n + "--cook结束----->" + c.getName() + "||" + c.getValue());
			}

			// HttpGet httpget = new
			// HttpGet("http://www.iteye.com/login");
			// HttpGet httpget = new
			// HttpGet("http://wad12302.iteye.com/admin");

			// HttpResponse response = httpclient.execute(httpget);

			// BufferedReader bufferedReader = new
			// BufferedReader(new
			// InputStreamReader(response.getEntity().getContent()));
			//
			// StringBuffer temp = new StringBuffer();
			// String line = bufferedReader.readLine();
			// while (line != null) {
			// temp.append(line).append("\r\n");
			// line = bufferedReader.readLine();
			// }
			// bufferedReader.close();
			//
			// System.out.println(temp.toString());

			// System.out.println("Initial set of cookies:");
			// List<Cookie> cookies =
			// httpclient.getCookieStore().getCookies();
			// if (cookies.isEmpty()) {
			// System.out.println("None");
			// } else {
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("- " + cookies.get(i).toString());
			// }
			// }

			// HttpPost httpost = new
			// HttpPost("https://portal.sun.com/amserver/UI/Login?"
			// +
			// "org=self_registered_users&" +
			// "goto=/portal/dt&" +
			// "gotoOnFail=/portal/dt?error=true");
			//
			// List <NameValuePair> nvps = new ArrayList
			// <NameValuePair>();
			// nvps.add(new BasicNameValuePair("IDToken1",
			// "username"));
			// nvps.add(new BasicNameValuePair("IDToken2",
			// "password"));
			//
			// httpost.setEntity(new UrlEncodedFormEntity(nvps,
			// HTTP.UTF_8));
			//
			// response = httpclient.execute(httpost);
			// entity = response.getEntity();
			//
			// System.out.println("Login form get: " +
			// response.getStatusLine());
			// EntityUtils.consume(entity);
			//
			// System.out.println("Post logon cookies:");
			// cookies = httpclient.getCookieStore().getCookies();
			// if (cookies.isEmpty()) {
			// System.out.println("None");
			// } else {
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("- " + cookies.get(i).toString());
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

}
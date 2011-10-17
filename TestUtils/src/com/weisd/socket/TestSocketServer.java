package com.weisd.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-19 下午1:17:10
 */

public class TestSocketServer {

	public TestSocketServer() {
		BufferedReader b;
		BufferedWriter c;
		OutputStreamWriter d;
		Socket socket = null;
		try {
			// so = new ServerSocket(8080);
			// incoming = so.accept();
			// System.out.println("等待客户端连接。");

			ServerSocket serverSocket = new ServerSocket(9731);
			

			while (true) {
				try {

					socket = serverSocket.accept();
					// System.out.println("已连接客户端。");
					b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					// in = new BufferedReader(new InputStreamReader(
					// socket.getInputStream()));

					String lines;
					String line = "";
					while ((lines = b.readLine()) != null) {
						line += lines;
					}

					if (null == line || "".equals(line.trim())) {
						System.out.println("空消息,睡眠5秒");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println(line);
				} catch (IOException e) {
					serverSocket.close();
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TestSocketServer();
	}

}

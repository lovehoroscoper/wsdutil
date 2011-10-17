package com.weisd.shell;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author Administrator
 * 
 */
public class UtilJavaShell {

	private static Logger logger = Logger.getLogger(UtilJavaShell.class);

	public static List<String> getLocalMAC() {
		InetAddress addr;
		List<String> macList = new ArrayList<String>();
		try {
			addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString();
			macList = getMAC(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return macList;
	}

	public static List<String> getMAC(String ipAddress) {
		List<String> macList = new ArrayList<String>();
		String address = "";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c nbtstat -a " + ipAddress;
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.indexOf("MAC") > 0) {
						int index = line.indexOf("=");
						index += 2;
						address = line.substring(index);
						macList.add(address.trim());
					}
				}
				br.close();
				return macList;
			} catch (IOException e) {
			}
		} else {
			// TODO UNIX
		}
		return macList;
	}
	
	public static void showDir() {
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c dir";
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				BufferedReader br = new BufferedReader(p.getInputStream());
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// TODO UNIX
		}
	}
	
	/**
	 * 中文解决
	 */
	public static void showDir2() {
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c dir";
//				String command = "cmd.exe /c mkdir ssddd";
				Process p = Runtime.getRuntime().exec(command);
				InputStream is = p.getInputStream();
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int c = -1;
					while ((c = is.read()) != -1) {
						bos.write(c);
					}
		            bos.flush();    //提交文件流，很关键   
					String res = new String(bos.toString("GBK").getBytes("UTF-8"));
					System.out.println(res);

				} catch (IOException e) {

					System.err.println(e);

				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// TODO UNIX
		}
	}

	public static void main(String[] args){
//		List list = UtilJavaShell.getLocalMAC();
//		if(null != list){
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println((String)list.get(i));
//			}
//		}
		
		UtilJavaShell.showDir2();
	}
}

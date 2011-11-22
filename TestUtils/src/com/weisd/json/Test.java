//package com.weisd.json;
//
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
///**
// * @desc 描述：
// *
// * @author weisd E-mail:weisd@junbao.net
// * @version 创建时间：2011-11-3 下午4:32:30
// */
//public class Test {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		String line = null;
//		StringBuffer sb = new StringBuffer();
//		try {
//			BufferedReader in = new BufferedReader(new FileReader("D:\\http\\33\\dd.html"));
//			while ((line = in.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(sb.toString());
//		
//		
//		JSONObject json = JSONObject.fromObject(sb.toString());
//		
//		System.out.println(json);
//		System.out.println(json.get("errorMsg"));
//
//	}
//
//}

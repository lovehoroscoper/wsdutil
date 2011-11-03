package com.weisd.apache.httpclient.wsd;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-10-26 下午4:07:21
 */
public class Jsoup3 {

//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		
//		//
//		// // 从文件中加载 HTML 文档
//		File input = new File("D:/http/tologin.html");
//		Document doc;
//		try {
//			doc = Jsoup.parse(input, "UTF-8");
//			Elements div = doc.select("div[id=copyright]"); // 具有 href 属性的链接
//			Element e = div.get(0);
//			System.out.println(e.text());
////			System.out.println(e.text(text));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//
		// // 从文件中加载 HTML 文档
		File input = new File("D:/http/init.html");
		Document doc;
		try {
			doc = Jsoup.parse(input, "UTF-8");
			Elements div = doc.select("input[id=secstate.state]"); // 具有 href 属性的链接
			Element e = div.get(0);
			System.out.println(e.attr("value"));
//			System.out.println(e.text(text));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

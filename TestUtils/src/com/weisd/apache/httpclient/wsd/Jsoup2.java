package com.weisd.apache.httpclient.wsd;

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
public class Jsoup2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 直接从字符串中输入 HTML 文档
		String html = "<html><head><title> 开源中国社区 </title></head>" + "<body><div id=\"divsss\">jjjj<p>333211</p></div><p> 这里是 jsoup 项目的相关文章 </p></body></html>";
		Document doc = Jsoup.parse(html);

		// 从 URL 直接加载 HTML 文档
		// Document doc = Jsoup.connect("http://www.oschina.net/").get();
		// String title = doc.title();
		//
		// Document doc = Jsoup.connect("http://www.oschina.net/").data("query",
		// "Java") // 请求参数
		// .userAgent("I ’ m jsoup") // 设置 User-Agent
		// .cookie("auth", "token") // 设置 cookie
		// .timeout(3000) // 设置连接超时时间
		// .post(); // 使用 POST 方法访问 URL
		//
		// // 从文件中加载 HTML 文档
		// File input = new File("D:/test.html");
		// Document doc = Jsoup.parse(input, "UTF-8",
		// "http://www.oschina.net/");

		// ----------------ok
		Elements div = doc.select("div[id=divsss]"); // 具有 href 属性的链接
		Element e = div.get(0);
		System.out.println(e.text());
		
//		Elements links = doc.select("a[href]"); // 具有 href 属性的链接
//		Elements pngs = doc.select("img[src$=.png]");// 所有引用 png 图片的元素
//
//		Element masthead = doc.select("div.masthead").first();
//		// 找出定义了 class=masthead 的元素
//
//		Elements resultLinks = doc.select("h3.r > a"); // direct a after h3

	}

}

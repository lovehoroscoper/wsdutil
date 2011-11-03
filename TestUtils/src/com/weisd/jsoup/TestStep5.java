package com.weisd.jsoup;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-10-31 下午4:54:12
 */
public class TestStep5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File input = new File("D:\\http\\result5.html");

		try {
			Document doc = Jsoup.parse(input, "UTF-8");
//ok
//			Elements elements2 = doc.select("#rechargeForm div[class=number_step_five number_three] ul li[class=step02 step_red]");
//			//获取到第二步显示
//			Element masthead = elements2.first();
//			System.out.println(masthead.text());
//ok  获取确认图片			
//			Elements elements2 = doc.select("#rechargeForm div.center a#confirm img");
//			//获取到第二步显示
//			Element masthead = elements2.first();
//			System.out.println(masthead.attr("src"));
			
			
//			//获取订单号 ok
//			Elements elements2 = doc.select("#rechargeForm div.center table.tab-row tbody tr:eq(1) td:eq(0)");
//			Element masthead = elements2.first();
//			System.out.println(masthead.text());
//			获取手机号 ok
//			Elements elements2 = doc.select("#rechargeForm div.center table.tab-row tbody tr:eq(1) td:eq(1)");
//			Element masthead = elements2.first();
//			System.out.println(masthead.text());
			
			// ok
			Elements elements2 = doc.select("#rechargeForm div.center table.tab-row tbody tr:eq(1)");
			Element masthead = elements2.first();
			Element c1 =  masthead.child(0);
			Element c2 =  masthead.child(1);

			
			
//			Elements links = doc.select("a[href]"); // 带有href属性的a元素
			
			
//			Element masthead = doc.select("#rechargeForm div.number_step_five").first();
//			Element masthead = doc.select("#rechargeForm div[class=number_step_five number_three]").first();
//			Elements elements = doc.select("#rechargeForm div[class=number_step_five number_three]");
			
//			Elements pngs = doc.select("form[src$=.png]");
//			// 扩展名为.png的图片
//
//			Element masthead = doc.select("div.masthead").first();
//			Element masthead = doc.select("div.masthead").first();
//			// class等于masthead的div标签

//			Elements resultLinks = doc.select("h3.r > a"); // 在h3元素之后的a元素
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

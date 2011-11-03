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
public class TestStep6 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
//			File input = new File("D:\\http\\33\\chongzhi_queren.html");
//			Document doc = Jsoup.parse(input, "UTF-8");
//			//ok
//			Elements elements2 = doc.select("div.marfixbig div[class=number_step_five number_three] ul li[class^=step03_1 step_red]");
//			//获取到第二步显示 判断数量只能等于1,且文字是一样
//			for (int i = 0; i < elements2.size(); i++) {
//				Element masthead = elements2.get(i);
//				System.out.println(masthead.text());
//			}
			

//			//ok
//			Elements elements2 = doc.select("div.marfixbig div[class=number_step_five number_three] ul li div.step_tit32");
//			//获取到第二步显示，判断是否有元素
//			for (int i = 0; i < elements2.size(); i++) {
//				Element masthead = elements2.get(i);
//				System.out.println(masthead.text());
//			}
			
			

			
			File input = new File("D:\\http\\33\\chenggong.html");
			Document doc = Jsoup.parse(input, "UTF-8");
			// ok
			Elements elements2 = doc.select("div.marfixbig div.center table.tab-row tbody tr:eq(1)");
			Element masthead = elements2.first();
			
			System.out.println(masthead.text());
			
			
			Element c0 =  masthead.child(0);//流水
			Element c1 =  masthead.child(1);//号码
			Element c2 =  masthead.child(3);//金额
			Element c3 =  masthead.child(3);//结果

			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

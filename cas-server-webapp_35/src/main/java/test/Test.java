package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gonetbar.ssa.util.CommUtils;

public class Test {

	public static void main(String args[]) {
		// System.out.println("http://www.aaa.com/bbb/123/345.htm".split("/")[2]);
		// System.out.println("http://www.aaa.com?aaa.htm".split("/")[2]);

		// String str = "http://www.aaa.com/bbb/123/345.htm";
		// String str = "http://www.aaa.com?bb=123";
		// String s = str.replaceAll("http://(.+?)/.*", "$1");
		// System.out.println(s);

		// String url = "http://www.aaa.com/bbb/123/345.htm";
//		String url = "http://www.aaa.com?bb=12?3";
		// String url = "http://www.aaa.com/d?aaaa";
		// String url = "http://172.25.25.61:8080/d?aaaa";
		// String url = "https://172.25.25.61:8080/d?aaa?a";
		// String url = "/www.aaa.com?aaa?a";
//		String url2 = "bbb/123/345.htm";
//		Matcher m = Pattern.compile("^(http|https)://[^/|?]+").matcher(url);
//		while (m.find()) {
//			System.out.println(m.group());
//		}
		// String url = "http://www.aaa.com?bb=123";
		// Test t = new Test();
		// t.splitURL(url);

		// String s =
		// "topic.csdn.net/u/20120604/22/2479ec15-887a-4a7f-9ca6-042d37214302.html";
		// String s = "http://www.aaa.com/bbb/123/345.htm";
		// Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		// Matcher m = p.matcher(s);
		// if(m.find()){
		// System.out.println(m.group());
		// }

		// String str = "http://www.aaa.com/bbb/123/345.htm";
		// String s = str.replaceAll("http://(.+?)/.*", "$1");
		// System.out.println(s);

		Test t = new Test();
//		String url = "http://www.aaa.com/aaaa/aaa";
		String url = "http://127.0.0.1:8080/hfson/aaaa/aaa";
		t.splitURL(url);
	}

	private String[] splitURL(String url) {
		String[] url_arr = new String[2];
		if (!CommUtils.isEmptyOrNullByTrim(url)) {
			if (url.startsWith("http")) {
				// 第3个/ 或者第一个? 谁在前面取谁
				int idx_3 = url.indexOf("/", 1);
				int idx_1 = url.indexOf("?", 1);

				Matcher m = Pattern.compile("^(http|https)://[^/|?]+").matcher(url);
				String sub_str = "";
				if(m.find()){
					 sub_str = m.group();
					
				}else{
					sub_str = url;
				}
				System.out.println(sub_str);

			} else {

			}
		}
		return url_arr;
	}
}
package test;

import org.scribe.utils.OAuthEncoder;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-21 下午6:34:30
 */
public class TestUrld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
String ss  = "authorization_code";

System.out.println(OAuthEncoder.encode(ss));
	}

}

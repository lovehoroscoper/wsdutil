package test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-11-30 下午4:47:47
 */
public class TestUrl {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

//		String t = "http://localhost:8080/cas/login?service=http%3A%2F%2Flocalhost%3A8080%2Fcas%2Fservices%2Fj_acegi_cas_security_check";
		String t = "http://127.0.0.1:8080/cas/login?service=http%3A%2F%2F127.0.0.1%3A9999%2F";
		
//		System.out.println(URLDecoder.decode(t,"utf-8"));
		
		
	}

}

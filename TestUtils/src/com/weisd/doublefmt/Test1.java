package com.weisd.doublefmt;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-1 下午3:03:34
 */
public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dd = new Double("0").toString();
		
		System.out.println(dd);
//		
//		
//		Map map = new HashMap();
//		map.put("aaa", null);
//		
//		String ss = (String)map.get("aaa");
//		
//		if("".equals(ss)){
//			System.out.println("为“”");
//		}else if(null == ss){
//			System.out.println("null");
//		}else{
//			System.out.println("other");
//		}
		
//		String req = "JB_ZJ_LL  500115999594443    2000           00001235                                          20110901        170334JB011105301302347906                    0D0DDE4CE6A2C23E5B2998C5FFB083C2";
		
//String reqs = req.substring(0, 120);		
//		
//System.out.println(reqs.length());
//		System.out.println(req.substring(0, 120));
//		System.out.println(req.substring(120, 140));
		
//		String req1 = "JB_ZJ_LL  500115999594443    2000           0000测试交易                                          20110901        170334JB011105301302347906                    0D0DDE4CE6A2C23E5B2998C5FFB083C2";
//
//		
//		String req2 = "JB_ZJ_LL  500115999594443    2000           00001235                                          20110901        170334JB011105301302347906";
//		String req3 = "JB_ZJ_LL  500115999594443    2000           0000正在测定                                          20110901        170334";
//		System.out.println(req2.length());
//		System.out.println(req3.length());
		
		
		String s1 = "JB_ZJ_LL  500115999594443    2000           0000测试交易                                          20110901        170334JB011105301302347906   " + "就";
		
		String s2 = new String(s1.getBytes());
		System.out.println(s2);
		System.out.println(s2.length());
		System.out.println(s1.getBytes().length);
		
		System.out.println(s1.substring(110, 130));
	}

}

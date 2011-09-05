package com.weisd.hfdealer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-6-28 上午10:50:09
 * @desc 类描述：
 */
public class Test {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
//		org.enhydra.jdbc.standard.StandardXADataSource
		
//		System.out.println(RegexUtil.checkDateType("2011-02-02 12:12:12"));
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sf.parse("2011-02-29 12:12:12");
			
			System.out.println(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

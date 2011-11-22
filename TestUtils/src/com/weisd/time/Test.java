package com.weisd.time;

import java.util.Date;

import com.junbao.hf.utils.common.DateUtils;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-22 上午11:11:49
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean flag = true;
		String start1 = "20111122110500";
		try {
			Long start = DateUtils.parseDate(start1, "yyyyMMddHHmmss").getTime();
			Long end = new Date().getTime();
			long nd = 1000*24*60*60;//一天的毫秒数
			long nh = 1000*60*60;//一小时的毫秒数
			long nm = 1000*60;//一分钟的毫秒数
			long min = (end - start) % nd % nh / nm;//计算差多少分钟
			flag = flag && (min < Long.parseLong("10"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(flag);
	}

}

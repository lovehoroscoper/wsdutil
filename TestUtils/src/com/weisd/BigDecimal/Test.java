package com.weisd.BigDecimal;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-8 上午11:01:48
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();
		BigDecimal suc_d = new BigDecimal("2.15676");
		BigDecimal other_d = new BigDecimal("1");
		t.percentFmt(suc_d, other_d, "");
//		DecimalFormat dff = new DecimalFormat("###.##%");
//		System.out.println(dff.format(0.15685));
	}
	
	public String percentFmt(BigDecimal suc_d, BigDecimal other_d, String fmt) {
		if (0.00 == other_d.doubleValue()) {
			return "--";// 业务要求除以0的都是无效的 不能为0
		} else {
			BigDecimal d = suc_d.subtract(other_d).divide(other_d,4,BigDecimal.ROUND_HALF_UP);
			System.out.println(d.toString());
			String res = this.numberFmt(d, "###0.00%");
			System.out.println(res);
			return res; 
		}
	}

	/**
	 * 注意参数转成 double
	 * 
	 * @param fmt
	 *            ###0.00% ###0.00
	 * @return
	 */
	public String numberFmt(Object obj, String fmt) {
		DecimalFormat dff = new DecimalFormat(fmt);
		return dff.format(obj);
	}

}

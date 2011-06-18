package com.weisd.fmt;
import java.math.BigDecimal;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-6-1 ����04:30:28
 * @version:v1.0
 * 
 */
public class TestRMB {

	/**
	 * ��--->Ԫ
	 * 
	 * @param money
	 *            ���ٷ� "1234.12"��
	 * @return '0.00' ��'0.0'Ԫ
	 */
	public static String fmtFen2YuanRsString(String money) {
		BigDecimal bd = new BigDecimal(money);
		double dob = bd.doubleValue();
		String monStr = formatByPattern(dob, "0");
		return fmtFen2YuanToString(Integer.valueOf(monStr));

	}

	/**
	 * ��--->Ԫ
	 * 
	 * @param money
	 *            ���ٷ� "1234.12"��
	 * @return 0.00 ��0.0Ԫ
	 */
	public static double fmtFen2YuanRsDouble(String money) {
		BigDecimal bd = new BigDecimal(money);
		// double dob = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		double dob = bd.doubleValue();
		String monStr = formatByPattern(dob, "0");
		return fmtFen2YuanByInt(Integer.valueOf(monStr));

	}

	/**
	 * ��--->Ԫ
	 * 
	 * @param money
	 *            12345 ���ٷ� ��С��
	 * @return 0.00 ��0.0Ԫ
	 */
	public static double fmtFen2YuanByInt(int money) {
		String res = fmtFen2YuanToString(money);
		BigDecimal bd = new BigDecimal(res);
		double dob = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return dob;
	}

	/**
	 * ��--->Ԫ
	 * 
	 * @param money
	 *            12345 ���ٷ� ��С��
	 * @return '0.00'Ԫ
	 */
	public static String fmtFen2YuanToString(int money) {
		if (0 == money) {
			return "0.00";
		} else {
			int int_yuan = money / 100;// Ԫ
			int int_temp = money % 100;// �����
			int int_jiao = int_temp / 10;// ��
			int int_fen = int_temp % 10;// ��
			// double res = 1.00 * int_yuan + 0.1 * int_jiao + 0.01 *
			// int_fen;//10999 ����
			String res = (int_yuan + "") + "." + (int_jiao + "") + (int_fen + "");
			return res;
		}
	}

	/**
	 * Ԫ-->�� �������� int
	 * 
	 * @param moneyStr
	 *            0.00
	 * @return
	 */
	public static int fmtYuan2FenRtInt(double money) {
		String moneyStr = formatByPattern(money, "0.00");
		String str_yuan = "0";// Ԫ
		String str_jiao = "0";// ��
		String str_fen = "0";// ��
		String[] strMoneys = java.util.regex.Pattern.compile("\\.").split(moneyStr);// ��֤��С���
		str_yuan = strMoneys[0];// Ԫ
		str_jiao = strMoneys[1].substring(0, 1);// ��
		str_fen = strMoneys[1].substring(1, 2);// ��
		int int_yuan = Integer.valueOf(str_yuan);
		int int_jiao = Integer.valueOf(str_jiao);
		int int_fen = Integer.valueOf(str_fen);
		int res = int_yuan * 100 + int_jiao * 10 + int_fen * 1;
		return res;
	}

	/**
	 * Ԫ-->��  �������� long
	 * 
	 * @param moneyStr
	 *            0.00
	 * @return
	 */
	public static long fmtYuan2FenRtLong(double money) {
		String moneyStr = formatByPattern(money, "0.00");
		String str_yuan = "0";// Ԫ
		String str_jiao = "0";// ��
		String str_fen = "0";// ��
		String[] strMoneys = java.util.regex.Pattern.compile("\\.").split(moneyStr);// ��֤��С���
		str_yuan = strMoneys[0];// Ԫ
		str_jiao = strMoneys[1].substring(0, 1);// ��
		str_fen = strMoneys[1].substring(1, 2);// ��
		long long_yuan = Long.valueOf(str_yuan);
		long long_jiao = Long.valueOf(str_jiao);
		long long_fen = Long.valueOf(str_fen);
		long res = long_yuan * 100L + long_jiao * 10L + long_fen * 1L;
		return res;
	}

	/**
	 * Ԫ-->��  ����String �ַ�
	 * 
	 * @param moneyStr
	 *            0.00
	 * @return
	 */
	public static String fmtYuan2FenRtString(double money) {
		String moneyStr = formatByPattern(money, "0.00");
		String str_yuan = "0";// Ԫ
		String str_jiao = "0";// ��
		String str_fen = "0";// ��
		String[] strMoneys = java.util.regex.Pattern.compile("\\.").split(moneyStr);// ��֤��С���
		str_yuan = strMoneys[0];// Ԫ
		str_jiao = strMoneys[1].substring(0, 1);// ��
		str_fen = strMoneys[1].substring(1, 2);// ��
		long long_yuan = Long.valueOf(str_yuan);
		long long_jiao = Long.valueOf(str_jiao);
		long long_fen = Long.valueOf(str_fen);
		long res = long_yuan * 100L + long_jiao * 10L + long_fen * 1L;
		return res + "";
	}

	/**
	 * ע�� ������ 0 ��ͷ ��Ȼ���� 0110
	 * 
	 * @param money
	 *            0.0
	 * @param pattern
	 *            0.00
	 * @return '0.00'
	 */
	public static String formatByPattern(double money, String pattern) {
		java.text.DecimalFormat fmt = new java.text.DecimalFormat(pattern);
		String moneyStr = fmt.format(money);
		return moneyStr;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TestRMB t = new TestRMB();
		// // int a1 = t.test1("1");
		// // int a2 = t.test1("0");
		// // int a3 = t.test1("1.2");
		// // int a4 = t.test1("1.11");
		// int a1 = t.test1("10000.19");
		// int a2 = t.test1("0");
		// int a3 = t.test1("0.2");
		// int a4 = t.test1("1.01");
		//
		// System.out.println("end");

		// System.out.println(TestRMB.formatYuanToFen(0110.034));
		// System.out.println(TestRMB.formatYuanToFen(0110.9994999999));

		// System.out.println(TestRMB.fmtYuan2FenRtInt(0));
		// System.out.println(TestRMB.fmtYuan2FenRtLong(0.0));
		// System.out.println(TestRMB.fmtYuan2FenRtString(0.1));
		//
		// System.out.println(TestRMB.fmtYuan2FenRtInt(20));
		// System.out.println(TestRMB.fmtYuan2FenRtLong(10.024));
		// System.out.println(TestRMB.fmtYuan2FenRtString(10.126));

		// System.out.println(90 / 100);
		// System.out.println(0 % 100);
		// System.out.println(0 % 100);

		// System.out.println(TestRMB.fmtFen2YuanByInt(10999));
		// System.out.println(Double.valueOf("1.99999"));
		// System.out.println(TestRMB.fmtFen2YuanToDouble(946));

		// double f=3.100000 ;
		// BigDecimal a=new BigDecimal(f);
		// double af = a.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		// System.out.println(af);
		// DecimalFormat df = new DecimalFormat("0.00");
		// double d = 123.9078;
		// double db = df.p

		// java.text.DecimalFormat fmt = new java.text.DecimalFormat("0");
		// String moneyStr = fmt.format("12.0");
		// DecimalFormat df = new DecimalFormat("0.00");
		// df.format("");

		// BigDecimal bd = new BigDecimal("12.3");
		// double dob = bd.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
		// System.out.println(dob);

		// java.text.DecimalFormat fmt = new java.text.DecimalFormat("0");
		// String moneyStr = fmt.format("12.0");
		// System.out.println(moneyStr);

		System.out.println(TestRMB.fmtFen2YuanRsString("0.0"));
		System.out.println(TestRMB.fmtFen2YuanRsDouble("0.0"));

	}

}

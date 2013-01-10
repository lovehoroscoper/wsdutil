package com.godtips.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class UtilDate {

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static final String DATE_TIME_WITH_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";

	public static final String TIME_WITH_MINUTE_PATTERN = "HH:mm";

	/**
	 * 根据单位字段比较两个日期
	 * 
	 * @param date
	 *            日期1
	 * @param otherDate
	 *            日期2
	 * @param withUnit
	 *            单位字段，从Calendar field取值
	 * @return 等于返回0值, 大于返回大于0的值 小于返回小于0的值
	 */
	public static int compareDate(Date date, Date otherDate, int withUnit) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		Calendar otherDateCal = Calendar.getInstance();
		otherDateCal.setTime(otherDate);

		switch (withUnit) {
		case Calendar.YEAR:
			dateCal.clear(Calendar.MONTH);
			otherDateCal.clear(Calendar.MONTH);
		case Calendar.MONTH:
			dateCal.set(Calendar.DATE, 1);
			otherDateCal.set(Calendar.DATE, 1);
		case Calendar.DATE:
			dateCal.set(Calendar.HOUR_OF_DAY, 0);
			otherDateCal.set(Calendar.HOUR_OF_DAY, 0);
		case Calendar.HOUR:
			dateCal.clear(Calendar.MINUTE);
			otherDateCal.clear(Calendar.MINUTE);
		case Calendar.MINUTE:
			dateCal.clear(Calendar.SECOND);
			otherDateCal.clear(Calendar.SECOND);
		case Calendar.SECOND:
			dateCal.clear(Calendar.MILLISECOND);
			otherDateCal.clear(Calendar.MILLISECOND);
		case Calendar.MILLISECOND:
			break;
		default:
			throw new IllegalArgumentException("withUnit 单位字段 " + withUnit + " 不合法！！");
		}
		System.out.println(dateCal.getTime() + "----" + otherDateCal.getTime());
		return dateCal.compareTo(otherDateCal);
	}

	/**
	 * 根据单位字段比较两个时间
	 * 
	 * @param date
	 *            时间1
	 * @param otherDate
	 *            时间2
	 * @param withUnit
	 *            单位字段，从Calendar field取值
	 * @return 等于返回0值, 大于返回大于0的值 小于返回小于0的值
	 */
	public static int compareTime(Date date, Date otherDate, int withUnit) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		Calendar otherDateCal = Calendar.getInstance();
		otherDateCal.setTime(otherDate);

		dateCal.clear(Calendar.YEAR);
		dateCal.clear(Calendar.MONTH);
		dateCal.set(Calendar.DATE, 1);
		otherDateCal.clear(Calendar.YEAR);
		otherDateCal.clear(Calendar.MONTH);
		otherDateCal.set(Calendar.DATE, 1);
		switch (withUnit) {
		case Calendar.HOUR:
			dateCal.clear(Calendar.MINUTE);
			otherDateCal.clear(Calendar.MINUTE);
		case Calendar.MINUTE:
			dateCal.clear(Calendar.SECOND);
			otherDateCal.clear(Calendar.SECOND);
		case Calendar.SECOND:
			dateCal.clear(Calendar.MILLISECOND);
			otherDateCal.clear(Calendar.MILLISECOND);
		case Calendar.MILLISECOND:
			break;
		default:
			throw new IllegalArgumentException("withUnit 单位字段 " + withUnit + " 不合法！！");
		}
		System.out.println(dateCal.getTime() + "----" + otherDateCal.getTime());
		return dateCal.compareTo(otherDateCal);
	}

	/**
	 * 格式化指定日期为指定格式的字符串
	 * 
	 * @param date
	 *            要格式化的日期时间
	 * @param format
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}

	/**
	 * 比较两个时间大小
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param format
	 *            时间字符串格式
	 * @param minDif
	 *            结果时间必须在开始时间之后多少分钟
	 * @return 0：相等 1:开始时间大于结果时间 -1：结束时间小于开始时间
	 */
	public static boolean compare(String beginDate, String endDate, String format, int minDif) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			long bd = f.parse(beginDate).getTime();
			long ed = f.parse(endDate).getTime();
			if ((ed - bd) > 0) {
				long temp = (ed - bd) / 1000;
				if (temp >= minDif * 60)
					return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}

	/**
	 * 得到当前时间,以yyyyMMdd格式的字符串返回
	 * 
	 * @return 以yyyyMMdd 格式的字符串返回当前日期
	 */
	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(c.getTime());
	}

	/**
	 * 得到当前日期,以yyyy-MM-dd格式的字符串返回
	 * 
	 * @return 以yyyy-MM-dd 格式的字符串返回当前日期
	 */
	public static String getCurrentDate1() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}

	/**
	 * 得到当前时间,以HH:mm:ss格式的字符串返回
	 * 
	 * @return 以 HH:mm:ss 格式的字符串返回当前时间
	 */
	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(c.getTime());
	}

	public static String getCurrentDateTime() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(c.getTime());
	}

	/**
	 * 得到minute 分钟后的时间,以yyyy-MM-dd格式的字符串返回
	 * 
	 * @return 以 yyyy-MM-dd 格式的字符串返回
	 */
	public static String getAddDate(int minute) {
		java.util.Date d = new java.util.Date(System.currentTimeMillis() + minute * 60 * 1000);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}

	/**
	 * 得到hour小时后的时间,以yyyy-MM-dd HH:mm:ss格式的字符串返回
	 * 
	 * @return 以 yyyy-MM-dd HH:mm:ss 格式的字符串返回
	 */
	public static String getAddHours(int hours) {
		java.util.Calendar cal = java.util.Calendar.getInstance();

		cal.add(cal.HOUR, hours);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(cal.getTime());
		// System.out.println("过期时间是:"+time);
		return time;
	}

	/**
	 * 得到minute 分钟后的时间,以HH:mm:ss格式的字符串返回
	 * 
	 * @return 以 HH:mm:ss 格式的字符串返回
	 */
	public static String getAddTime(int minute) {
		java.util.Date d = new java.util.Date(System.currentTimeMillis() + minute * 60 * 1000);
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(d);
	}

	/**
	 * 得到昨天时间,以yyyyMMdd格式的字符串返回
	 * 
	 * @return 以yyyyMMdd 格式的字符串返回当前日期
	 */
	public static String getYesterDate() {
		java.util.Date d = new java.util.Date(System.currentTimeMillis() - 1 * 24 * 3600 * 1000);
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(d);
	}

	/**
	 * 判断当前时间是否在这个时间段内
	 * 
	 * @param t1
	 *            =时间1 格式 yyyy-MM-dd HH:mm:ss
	 * @param t2
	 *            =时间2 格式 yyyy-MM-dd HH:mm:ss
	 * @return 如果是返回true,否则false
	 */
	public static boolean ifInTime1(String start, String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");
		df.setTimeZone(zone);

		if (start == null || start.length() == 0 || end == null || end.length() == 0)
			return false;

		java.util.Date d1;
		java.util.Date d2;
		try {
			d1 = df.parse(start);
			d2 = df.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		java.util.Date now = new java.util.Date();
		boolean b = now.after(d1);
		if (!b)
			return false;

		b = now.before(d2);
		if (!b)
			return false;

		return true;
	}

	/**
	 * 判断当前时间是否在这个时间段内
	 * 
	 * @param t1
	 *            =时间1 格式 HH:mm:ss
	 * @param t2
	 *            =时间2 格式 HH:mm:ss
	 * @return 如果是返回true,否则false
	 */
	public static boolean ifInTime(String t1, String t2) {
		String ct = getCurrentTime();
		String cts[] = ct.split(":");
		int ctss = (Integer.parseInt(cts[0]) * 24 + Integer.parseInt(cts[1])) * 60 + Integer.parseInt(cts[2]);
		String t1s[] = t1.split(":");
		int t1ss = (Integer.parseInt(t1s[0]) * 24 + Integer.parseInt(t1s[1])) * 60 + Integer.parseInt(t1s[2]);
		String t2s[] = t2.split(":");
		int t2ss = (Integer.parseInt(t2s[0]) * 24 + Integer.parseInt(t2s[1])) * 60 + Integer.parseInt(t2s[2]);
		if (t1ss >= t2ss) { // 第一天到第二天,例如 23:30~6:30
			if (ctss >= t1ss || ctss <= t2ss)
				return true;
		} else {
			if (ctss >= t1ss && ctss <= t2ss)
				return true;
		}
		return false;
	}

	/**
	 * 判断输入的时间是否是同一天
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param format1
	 *            输入参数的日期格式 如"yyyy-MM-dd"
	 * @param format2
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @param dif
	 *            beginDate和endDate允许相差的天数 dif<0：不限制
	 * @return -1:时间格式输入有误，0:是同一天，1:不是同一天
	 */
	public static int checkDate(String beginDate, String endDate, String format, int dif) {
		int relt = -1;

		java.util.Date bd = new java.util.Date();
		java.util.Date ed = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			bd = f.parse(beginDate);
			ed = f.parse(endDate);
			int r = ed.compareTo(bd);
			if (r < 0)
				return -1;
			if (dif >= 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(bd);
				calendar.add(Calendar.DATE, dif);
				java.util.Date tempD = calendar.getTime();
				if (tempD.compareTo(ed) < 0)
					return -1;
			}
			if (f1.format(bd).equals(f1.format(ed)))
				relt = 0;
			else
				relt = 1;
		} catch (ParseException e) {
			relt = -1;
		}
		return relt;
	}

	/**
	 * 得到得到指定格式的日期字符串
	 * 
	 * @param dateStr
	 * @param format
	 *            输入参数的日期格式 如"yyyy-mm-dd"
	 * @return
	 */
	public static boolean chkDate1(String dateStr, String format) {
		String r = dateStr;
		java.util.Date bd = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			bd = f.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 得到得到指定格式的日期字符串
	 * 
	 * @param dateStr
	 * @param format1
	 *            输入参数的日期格式 如"yyyy-mm-dd"
	 * @param format2
	 *            输出的日期格式 如"yyyy-mm-dd"
	 * @return
	 */
	public static String getFormatDateStr(String dateStr, String format1, String format2) {
		String r = dateStr;
		java.util.Date bd = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat(format1);
		SimpleDateFormat f1 = new SimpleDateFormat(format2);
		try {
			bd = f.parse(dateStr);
			r = f1.format(bd);
		} catch (ParseException e) {
			r = dateStr;
		}
		return r;
	}

	/**
	 * 得到指定日期的前一天的yyyy-MM-dd格式字符串
	 * 
	 * @param dateStr
	 * @param format1
	 *            输入参数的日期格式 如"yyyy-MM-dd"
	 * @param format2
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static String getFormatYesterDateStr(String dateStr, String format1, String format2) {
		String r = dateStr;
		java.util.Date bd = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat(format1);
		SimpleDateFormat f1 = new SimpleDateFormat(format2);
		try {
			bd = f.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bd);
			calendar.add(Calendar.DATE, -1);
			r = f1.format(calendar.getTime());
		} catch (ParseException e) {
			r = dateStr;
		}
		return r;
	}

	/**
	 * 格式化当前日期为指定格式的字符串
	 * 
	 * @param format
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static String getFormatCurrDate(String format) {
		java.util.Date bd = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(bd);
	}

	/**
	 * 格式化指定日期前n天并指定格式的字符串
	 * 
	 * @param date
	 *            要格式化的日期时间
	 * @param format
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static String getFormatDate(java.util.Date date, int n, String format) {
		String r = "";
		SimpleDateFormat f = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, n);
		r = f.format(calendar.getTime());
		return r;
	}

	/**
	 * 格式化指定日期前n天并指定格式的字符串
	 * 
	 * @param dateStr
	 *            要格式化的日期时间
	 * @param format
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static String getFormatDate(String dateStr, int n, String inFormat, String outFormat) {
		String r = "";
		SimpleDateFormat inF = new SimpleDateFormat(inFormat);
		SimpleDateFormat outF = new SimpleDateFormat(outFormat);
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(inF.parse(dateStr));
			calendar.add(Calendar.DATE, n);
			r = outF.format(calendar.getTime());
		} catch (ParseException e) {
			r = "";
		}
		return r;
	}

	/**
	 * 得到指定日期的为星期几
	 * 
	 * @param dateStr
	 * @param format
	 *            输入参数的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static String getWeekByDay(String dateStr, String format) {
		String r = "";
		java.util.Date bd = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		SimpleDateFormat f2 = new SimpleDateFormat("EEE");
		try {
			bd = f.parse(dateStr);
			r = f2.format(bd);
		} catch (ParseException e) {
			r = "";
		}
		return r;
	}

	/**
	 * 得到指定日期格式的日期字符串所在当月的最大天数
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static int getMonthDayByDate(String dateStr, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		java.util.Date bd = new java.util.Date();
		int max = 0;
		try {
			bd = f.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bd);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			GregorianCalendar date = new GregorianCalendar(year, month, 1);
			date.add(Calendar.DATE, -1);
			return (date.get(Calendar.DAY_OF_MONTH));

		} catch (ParseException e) {
			// r = "";
		}
		return max;
	}

	/**
	 * 得到当前日期前n个月份列表
	 * 
	 * @param n
	 * @param format
	 * @return
	 */
	public static List<String> getMonthList(int n, String format) {
		SimpleDateFormat outf = new SimpleDateFormat(format);
		java.util.Date bd = new java.util.Date();
		List<String> relt = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(bd);
		for (int i = 0; i < Math.abs(n); i++) {
			if (n < 0)
				cal.add(Calendar.MONTH, -i);
			else
				cal.add(Calendar.MONTH, i);
			String month = outf.format(cal.getTime());
			// System.out.println(i+":"+month);
			relt.add(month);
			cal.setTime(bd);
		}

		return relt;

	}

	/**
	 * 得到指定格式月份的所包函的日期列表
	 * 
	 * @param month
	 * @param inFormat
	 * @param outFormat
	 * @return
	 */
	public static List<String> getDayListByMonth(String month, String inFormat, String outFormat) {
		SimpleDateFormat intf = new SimpleDateFormat(inFormat);
		SimpleDateFormat outf = new SimpleDateFormat(outFormat);
		List<String> relt = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();

		try {
			java.util.Date bd = intf.parse(month);
			int maxDay = getMonthDayByDate(outf.format(bd), outFormat);
			cal.setTime(bd);
			// System.out.println("1:" + outf.format(cal.getTime()));
			relt.add(outf.format(cal.getTime()));
			for (int i = 0; i < maxDay - 1; i++) {
				cal.add(Calendar.DATE, 1);
				String day = outf.format(cal.getTime());
				// System.out.println((i+2) + ":" + day);
				relt.add(day);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relt;

	}

	/**
	 * 得到指定日期前n月的第一天
	 * 
	 * @param dateStr
	 * @param n
	 * @param inFormat
	 * @param outFormat
	 * @return
	 */
	public static String getLastMonthFirstDayByDate(String dateStr, int n, String inFormat, String outFormat) {
		SimpleDateFormat inf = new SimpleDateFormat(inFormat);
		SimpleDateFormat outf = new SimpleDateFormat(inFormat);
		SimpleDateFormat df = new SimpleDateFormat("dd");
		java.util.Date bd = new java.util.Date();
		String relt = "";
		try {
			bd = inf.parse(dateStr);
			int currDay = Integer.valueOf(df.format(bd));
			Calendar cal = Calendar.getInstance();
			cal.setTime(bd);
			cal.add(Calendar.MONTH, n + 1);
			cal.add(Calendar.DATE, -(currDay));

			bd = inf.parse(inf.format(cal.getTime()));
			currDay = Integer.valueOf(df.format(bd));
			cal.add(Calendar.DATE, -(currDay - 1));
			relt = outf.format(cal.getTime());
		} catch (ParseException e) {
			// r = "";
		}
		return relt;
	}

	/**
	 * 得到指定日期前n月的最后一天
	 * 
	 * @param dateStr
	 * @param n
	 * @param inFormat
	 * @param outFormat
	 * @return
	 */
	public static String getLastMonthLastDayByDate(String dateStr, int n, String inFormat, String outFormat) {
		SimpleDateFormat inf = new SimpleDateFormat(inFormat);
		SimpleDateFormat outf = new SimpleDateFormat(inFormat);
		SimpleDateFormat df = new SimpleDateFormat("dd");
		java.util.Date bd = new java.util.Date();
		String relt = "";
		try {
			bd = inf.parse(dateStr);
			int currDay = Integer.valueOf(df.format(bd));
			Calendar cal = Calendar.getInstance();
			cal.setTime(bd);
			cal.add(Calendar.MONTH, n + 1);
			cal.add(Calendar.DATE, -(currDay));
			relt = outf.format(cal.getTime());
		} catch (ParseException e) {
			// r = "";
		}
		return relt;
	}

	/**
	 * 判断输入的日期是否在指定的天数内
	 * 
	 * @param format
	 *            输入参数的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public static boolean isdata(String addtime, int daynum) {
		// addtime为要判断的日期格式为"yy-mm-dd"
		// daynum 判断的天数
		int q = 0;
		for (int i = 0; i < daynum; i++) {
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -i);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String time = formatter.format(cal.getTime());
			try {
				java.util.Date cDate = formatter.parse(time);
				java.util.Calendar calcurr = java.util.Calendar.getInstance();
				calcurr.add(Calendar.DAY_OF_MONTH, 0);
				java.util.Date d1;
				d1 = formatter.parse(addtime);
				int r = d1.compareTo(cDate);
				// System.out.println("a"+r);
				if (r == 0)
					q += 1;
			} catch (ParseException e) {
			}
		}
		if (q != 0)
			return true;
		else
			return false;
	}

	/**
	 * 如果参数 Date 等于此 Date，则返回值 0； 如果此 Date 在 Date 参数之前，则返回小于 0 的值； 如果此 Date 在
	 * Date 参数之后，则返回大于 0 的值。 比较2个时间
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compToDate(String str1, String str2, String fmt) {
		int ret = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			Date d1 = sdf.parse(str1);
			Date d2 = sdf.parse(str2);
			ret = d1.compareTo(d2);
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 获取给定时间的前n月的时间
	 * 
	 * @param dateStr
	 * @param n
	 * @param inFormat
	 * @param outFormat
	 * @return
	 */
	public static String queryBeforeMonthByDate(String dateStr, int n, String inFormat, String outFormat) {
		SimpleDateFormat inf = new SimpleDateFormat(inFormat);
		SimpleDateFormat outf = new SimpleDateFormat(outFormat);
		java.util.Date bd = new java.util.Date();
		String relt = "";
		try {
			bd = inf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bd);
			// cal.add(Calendar.MONTH, n + 1);
			cal.add(Calendar.MONTH, n);
			relt = outf.format(cal.getTime());
		} catch (ParseException e) {
			// r = "";
		}
		return relt;
	}

	public static void main(String[] args) throws ParseException {
		String bd = "2011-07-11 10:20:20";
		String ed = "2011-07-11 10:20:20";
		System.out.println(compare(bd, ed, "yyyy-MM-dd HH:mm:ss", 120));
	}
}

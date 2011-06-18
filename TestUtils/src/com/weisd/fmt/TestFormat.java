package com.weisd.fmt;
import java.text.*;

import java.util.*;

public class TestFormat {

	public static void main(String[] args) {

		defaultNumberFormat();

		System.out.println();
		customNumberFormat();

		System.out.println();

		defaultDateFormat();

		System.out.println();

//		customDateFormat();

		System.out.println();

	}

	public static void defaultNumberFormat() {

		int i = 123456;

		double x = 882323.23523;

		double p = 0.528;

		double c = 52.83;

		NumberFormat nf = NumberFormat.getInstance();

		System.out.println("Integer " + i + " is displayed as " + nf.format(i));

		System.out.println("Double " + x + " is displayed as " + nf.format(x));

		NumberFormat nfInt = NumberFormat.getIntegerInstance();

		System.out.println("Integer " + i + " is displayed as " + nfInt.format(i));

		NumberFormat nfNumber = NumberFormat.getNumberInstance();

		System.out.println("Double " + x + " is displayed as " + nfNumber.format(x));

		NumberFormat nfPercent = NumberFormat.getPercentInstance();

		System.out.println("Percent " + p + " is displayed as " + nfPercent.format(p));

		NumberFormat nfCurrency = NumberFormat.getCurrencyInstance();

		System.out.println("Currency " + p + " is displayed as " + nfCurrency.format(c));

		// ����û���漰��Ӧ��parse����

	}

	public static void customNumberFormat() {

		double x = 1000.0 / 3;

		System.out.println("default output is " + x);

		patternPrint("###,###.##", x);

		patternPrint("####.##", x);

		patternPrint("####.00", x);

		patternPrint("####.0#", x);

		patternPrint("00000.##", x);

		patternPrint("$###,###.##", x);

		patternPrint("0.###E0", x);

		patternPrint("00.##%", x);

		double y = 23.0012;

		System.out.println("default output is " + y);

		patternPrint("###,###.##", y);

		patternPrint("####.##", y);

		patternPrint("####.00", y);

		patternPrint("####.0#", y);

		patternPrint("00000.##", y);

		patternPrint("$###,###.##", y);

		patternPrint("0.###E0", y);

		patternPrint("00.##%", y);

	}

	public static void patternPrint(String pattern, double x) {

		DecimalFormat df = new DecimalFormat(pattern);

		System.out.println("output for pattern " + pattern + " is " + df.format(x));

	}

	public static void defaultDateFormat() {

		Date date = new Date();

		System.out.println("simple date " + date.toLocaleString());

		DateFormat df = DateFormat.getDateTimeInstance();

		System.out.println(df.format(date));

		DateFormat dfLong = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

		System.out.println(dfLong.format(date));

		DateFormat dfMedium = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,

		DateFormat.MEDIUM);

		System.out.println(dfMedium.format(date));

		DateFormat dfShort = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

		System.out.println(dfShort.format(date));

	}

//	public static void customDateFormat() {
//
//		Date date = new Date();
//
//		patternPrint("yyyy.MM.dd HH:mm:ss z", date); // }��MM, dd�ᵼ�²���
//
//		patternPrint("yy��M��d�� HHʱmm��", date); // }��yy����ʾΪ}λ���
//
//		patternPrint("EEE, MMM d, ����yy", date);
//
//		patternPrint("h:mm a", date);
//
//		patternPrint("hh ��o����clock�� a, zzzz", date);
//
//		patternPrint("yyyyy.MMMMM.dd GGG hh:mm aaa", date);
//
//		patternPrint("EEE, d MMM yyyy HH:mm:ss Z", date);
//
//		patternPrint("yyMMddHHmmssZ", date);
//
//	}

	public static void patternPrint(String pattern, Date date) {

		SimpleDateFormat df = new SimpleDateFormat(pattern);

		System.out.println(df.format(date));

	}

}

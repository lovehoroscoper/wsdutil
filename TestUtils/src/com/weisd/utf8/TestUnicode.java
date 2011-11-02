package com.weisd.utf8;

public class TestUnicode {
//	public static void main(String[] args) {
//		String aa = toUTF("奖金");
//		System.out.println(aa);
//
//	}
	
	public static void main(String[] args) {
		
		args = new String[]{"哈哈hh"};
		
		int len = args[0].length();
		String[] s = new String[len];
		// cn -> unicode
		for (int i = 0; i < len; i++) {
			char c = args[0].charAt(i);
			s[i] = Integer.toString(c, 16);
			System.out.println(c + "\t\\u" + s[i]);
		}
		System.out.println();
		// unicode -> cn
		for (int i = 0; i < len; i++) {
			char c = (char) Integer.valueOf(s[i], 16).intValue();
//			System.out.println("\\u" + s[i] + "\t" + c);
			System.out.print("\\u" + s[i] + "\t" + c);
		}
	}
	
//	/**
//     * inParam:需要转换的gb2312中文字符 返回:该中文字符对应的UTF-8编码的字符
//     */
//    public static String toUTF(String inPara) {
//        char temChr;
//        int ascChr;
//        int i;
//        String rtStr = new String("");
//        if (inPara == null) {
//            inPara = "";
//        }
//        for (i = 0; i < inPara.length(); i++) {
//            temChr = inPara.charAt(i);
//            ascChr = temChr + 0;
//            rtStr = rtStr + "&#x" + Integer.toHexString(ascChr) + ";";
//        }
//        return rtStr;
//    }

}
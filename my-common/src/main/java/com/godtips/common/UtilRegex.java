package com.godtips.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilRegex {

	public static boolean startCheck(String reg, String string) {
		boolean tem = false;

		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);

		tem = matcher.matches();
		return tem;
	}

	/**
	 * 检验整数,适用于正整数、负整数、0，负整数不能以-0开头, 正整数不能以0开头
	 * 
	 * */
	public static boolean checkNr(String nr) {
		String reg = "^(-?)[1-9]+\\d*|0";
		return startCheck(reg, nr);
	}

	/**
	 * 手机号码验证,11位，不知道详细的手机号码段，只是验证开头必须是1和位数
	 * */
	public static boolean checkCellPhone(String cellPhoneNr) {
		String reg = "^[1][\\d]{10}";
		return startCheck(reg, cellPhoneNr);
	}

	/**
	 * 检验空白符
	 * */
	public static boolean checkWhiteLine(String line) {
		String regex = "(\\s|\\t|\\r)+";

		return startCheck(regex, line);
	}

	/**
	 * 检查EMAIL地址 用户名和网站名称必须>=1位字符 地址结尾必须是以com|cn|com|cn|net|org|gov|gov.cn|edu|edu.cn结尾
	 * */
	public static boolean checkEmailWithSuffix(String email) {
		String regex = "\\w+\\@\\w+\\.(com|cn|com.cn|net|org|gov|gov.cn|edu|edu.cn)";

		return startCheck(regex, email);
	}

	/**
	 * 检查EMAIL地址 用户名和网站名称必须>=1位字符 地址结尾必须是2位以上，如：cn,test,com,info
	 * */
	public static boolean checkEmail_0(String email) {
		String regex = "\\w+\\@\\w+\\.\\w{2,}";
		
		return startCheck(regex, email);
	}
	
	public static boolean checkEmail(String email) {
		String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

		return startCheck(regex, email);
	}

	/**
	 * 检查邮政编码(中国),6位，第一位必须是非0开头，其他5位数字为0-9
	 * */
	public static boolean checkPostcode(String postCode) {
		String regex = "^[1-9]\\d{5}";
		return startCheck(regex, postCode);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾 用户名有最小长度和最大长度限制，比如用户名必须是4-20位
	 * */
	public static boolean checkChUsername(String username, int min, int max) {
		String regex = "[\\w\u4e00-\u9fa5]{" + min + "," + max + "}(?<!_)";
		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾 有最小位数限制的用户名，比如：用户名最少为4位字符
	 * */
	public static boolean checkChUsername(String username, int min) {
		// [\\w\u4e00-\u9fa5]{2,}?
		String regex = "[\\w\u4e00-\u9fa5]{" + min + ",}(?<!_)";

		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字 最少一位字符，最大字符位数无限制，不能以"_"结尾
	 * */
	public static boolean checkChUsername(String username) {
		String regex = "[\\w\u4e00-\u9fa5]+(?<!_)";
		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_"，不能以"_"结尾 用户名有最小长度和最大长度限制，比如用户名必须是4-20位
	 * */
	public static boolean checkUsername(String username, int min, int max) {
		String regex = "[\\w]{" + min + "," + max + "}(?<!_)";
		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_"，不能以"_"结尾 有最小位数限制的用户名，比如：用户名最少为4位字符
	 * */
	public static boolean checkUsername(String username, int min) {
		// [\\w\u4e00-\u9fa5]{2,}?
		String regex = "[\\w]{" + min + ",}(?<!_)";

		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_" 最少一位字符，最大字符位数无限制，不能以"_"结尾
	 * */
	public static boolean checkUsername(String username) {
		String regex = "[\\w]+(?<!_)";
		return startCheck(regex, username);
	}

	/**
	 * 查看IP地址是否合法
	 * */
	public static boolean checkIP(String ipAddress) {
		String regex = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";

		return startCheck(regex, ipAddress);
	}

	public static boolean chcekPort(String port65535) {
		String regex = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]{1}|6553[0-5])$";

		return startCheck(regex, port65535);
	}

	/**
	 * 验证国内电话号码 格式：010-67676767，区号长度3-4位，必须以"0"开头，号码是7-8位
	 * */
	public static boolean checkPhoneNr(String phoneNr) {
		String regex = "^[0]\\d{2,3}\\-\\d{7,8}";

		return startCheck(regex, phoneNr);
	}

	/**
	 * 验证国内电话号码 格式：6767676, 号码位数必须是7-8位,头一位不能是"0"
	 * */
	public static boolean checkPhoneNrWithoutCode(String phoneNr) {
		String reg = "^[1-9]\\d{6,7}";

		return startCheck(reg, phoneNr);
	}

	/**
	 * 验证国内电话号码 格式：0106767676，共11位或者12位，必须是0开头
	 * */
	public static boolean checkPhoneNrWithoutLine(String phoneNr) {
		String reg = "^[0]\\d{10,11}";

		return startCheck(reg, phoneNr);
	}

	/**
	 * 验证国内身份证号码：15或18位，由数字组成，不能以0开头
	 * */
	public static boolean checkIdCard(String idNr) {
		String reg = "^[1-9](\\d{14}|\\d{17})";

		return startCheck(reg, idNr);
	}

	/**
	 * 网址验证<br>
	 * 符合类型：<br>
	 * http://www.test.com<br>
	 * http://163.com
	 * */
	public static boolean checkWebSite(String url) {
		// http://www.163.com
		String reg = "^(http)\\://(\\w+\\.\\w+\\.\\w+|\\w+\\.\\w+)";

		return startCheck(reg, url);
	}

	/**
	 * yyyy-MM-dd
	 * @param type
	 * @return
	 */
	public static boolean checkDateType(String type) {
		// String reg = "^(\\d{1,4})(-|\\//)(\\d{1,2})\\2(\\d{1,2})";
		// =/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})/
		String reg = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?" + "((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|"
				+ "(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" + "(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|"
				+ "([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?" + "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|"
				+ "([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		
		return startCheck(reg, type);
	}
	
	/**
	 * 验证时间格式:HHMMSS 不包括空格
	 * @param type
	 * @return
	 */
	public static boolean checkHHMMSS_1(String hhmmss) {
		//(/^([0-1]\d|2[0-3]):([0-5]\d):([0-5]\d)$/
		String reg = "^([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)";
		return startCheck(reg, hhmmss);
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param type
	 * @return
	 */
	public static boolean checkDateType_1(String type) {
		boolean flag = true;
		if(null == type || "".equals(type.trim())){
			flag = false;
		}else{
			String dateStr = "";
			String hhmmssStr = "";
			if(type.length() > 10){
				dateStr = type.substring(0, 10);
				hhmmssStr = type.substring(10, type.length()).trim();	
			}else{
				dateStr = type;
			}
			flag = flag && checkDateType(dateStr);
			flag = flag && checkHHMMSS_1(hhmmssStr);
		}
		return flag;
	}

	/**
	 * 整数
	 * 
	 * @param postCode
	 * @return
	 */
	public static boolean checkIntegerNum(String postCode) {
		String regex = "^[1-9]\\d*";
		return startCheck(regex, postCode);
	}

	public static boolean checkAllNum(String postCode) {
		String regex = "-?[0-9]+.?[0-9]+";
		return startCheck(regex, postCode);
	}

	/**
	 * 检验数字
	 * 
	 * */
	public static boolean checkNumber(String num) {
		String reg = "\\d*";// /^\d+$/ //不限制位数
		return startCheck(reg, num);
	}

	/**
	 * 4位小数 不包括负
	 * 
	 * @param postCode
	 * @return
	 */
	public static boolean checkDoubleNum(String postCode) {
		String regex = "^([0-9]\\d*(\\.[0-9]{1,4})?)$";
		return startCheck(regex, postCode);
	}
	
	/**
	 * 4位小数 不包括负
	 * 
	 * @param postCode
	 * @return
	 */
	public static boolean checkDoubleNumAll(String postCode) {
		String regex = "-?([0-9]\\d*(\\.[0-9]{1,4})?)$";
		return startCheck(regex, postCode);
	}

	/**
	 * 不包括负 验证数字
	 * 
	 * @param postCode
	 * @param type
	 *            验证 double 还是 integer
	 * @param beginNum
	 *            是否允许 0开头 为 0则是 0开头 0-9 否则1 开头 1-9
	 * @param limitNum
	 *            几位小数 小于1 无小数位
	 * @return
	 */
	public static boolean checkDoubleAndInteger(String postCode, String type, int beginNum, int limitNum) {
		int newLimitNum = 1;
		int newBeginNum = 0;
		String regex = "";
		if (0 != beginNum) {
			newBeginNum = 1;
		}
		if ("double".equals(type)) {
			if (limitNum >= 1) {// double 类型无开头限制
				newLimitNum = limitNum;
				regex = "^([0-9]\\d*(\\.[0-9]{1," + newLimitNum + "})?)$";
			} else {
				regex = "^([" + newBeginNum + "-9]\\d*)$";
			}
		} else if ("integer".equals(type)) {
			regex = "^([" + newBeginNum + "-9]\\d*)$";
		} else {
			return false;
		}
		return startCheck(regex, postCode);
	}

	/**
	 * 有负数
	 * 
	 * @param postCode
	 * @param type
	 *            验证 double 还是 integer
	 * @param beginNum
	 *            是否允许 0开头 为 0则是 0开头 0-9 否则1 开头 1-9
	 * @param limitNum
	 *            几位小数 小于1 无小数位
	 * @return
	 */
	public static boolean checkDoubleAndIntegerV2(String postCode, String type, int beginNum, int limitNum) {
		int newLimitNum = 1;
		int newBeginNum = 0;
		String regex = "";
		if (0 != beginNum) {
			newBeginNum = 1;
		}
		if ("double".equals(type)) {
			if (limitNum >= 1) {// double 类型无开头限制
				newLimitNum = limitNum;
				regex = "-?([0-9]\\d*(\\.[0-9]{1," + newLimitNum + "})?)$";
			} else {
				regex = "-?([" + newBeginNum + "-9]\\d*)$";
			}
		} else if ("integer".equals(type)) {
			regex = "^([" + newBeginNum + "-9]\\d*)$";
		} else {
			return false;
		}
		return startCheck(regex, postCode);
	}

	/**
	 * 0 0或正整数
	 * 
	 * @param postCode
	 * @return
	 */
	public static boolean checkIntegerAndZero(String postCode) {
		String regex = "^([0-9]\\d*)$";
		return startCheck(regex, postCode);
	}
	
	/**
	 * IPV6
	 * @param postCode
	 * @return
	 */
	public static boolean checkIPV6(String ipAddress) {
		//http://pfeishao.blog.163.com/blog/static/18162337020112113130453/
		String regex = "^([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:$";
		return startCheck(regex, ipAddress);
	}

	public static void main(String[] args) {
		// boolean f = RegexUtil.checkDateType("1986-14-02");
		// boolean f = RegexUtil.checkNumber("0.00000000");
		// boolean f = RegexUtil.checkNumber(null);
		// boolean f = RegexUtil.checkDateType("2007-2-29");
		// System.out.println(f+"_"+"2007-02-29".length());
		//
		// SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// f

		// boolean f = RegexUtil.checkAllNum("010.0000");
		// boolean f = RegexUtil.checkDoubleAndInteger("010.1","double",0,1);
		// boolean f = RegexUtil.chcekPort("100000");
		// boolean f = RegexUtil.checkKey(",0,,");
//		 boolean f = RegexUtil.checkEmail("123@123.com");
//		 System.out.println(f);
		
//		String type = "yyyy-MM-dd HH:mm:ss";
//		String dateStr = type.substring(0, 9);
//		String hhmmssStr = type.substring(10, type.length());	
		
//		System.out.println(dateStr);
//		System.out.println(hhmmssStr);
//		System.out.println(checkDateType_1("2011-07-25 05:05:60"));
//		System.out.println(checkHHMMSS_1("23:60:05"));
//		System.out.println(checkEmail("18608415658@wo.com.cn"));
//		System.out.println(checkDoubleNumAll("2-0.0000"));
		
//		String ipAddress = "localhost";
//		String ipAddress = "1.1.1.1";
//		String ipAddress = "256.11.11.11";
//		String ipAddress = "0.11.11.11";
//		System.out.println(RegexUtil.checkIP(ipAddress));
//		System.out.println(RegexUtil.checkIPV4(ipAddress));
//		String ipAddress = "5e:0:0:023:0:0:5668:eeee";
//		String ipAddress = "55555:5e:0:0:0:0:0:5668:eeee";
//		String ipAddress = "ee:ee::11.11.11.125";
//		System.out.println(RegexUtil.checkIPV6(ipAddress));

//		public static final String IPV4_REGEX = "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z"; 
//		public static final String IPV6_HEX4DECCOMPRESSED_REGEX = "\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?) ::((?:[0-9A-Fa-f]{1,4}:)*)(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z"; 
//		public static final String IPV6_6HEX4DEC_REGEX = "\\A((?:[0-9A-Fa-f]{1,4}:){6,6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z"; 
//		public static final String IPV6_HEXCOMPRESSED_REGEX = "\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)\\z"; 
//		public static final String IPV6_REGEX = "\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z"; 
		
//		System.out.println(UtilRegex.checkDoubleAndIntegerV2("100.123", "double", 0, 4));
		System.out.println(UtilRegex.checkEmail("xiya@ngdewuse@163.com"));

	}

}

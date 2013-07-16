package com.test.mina;

import org.apache.log4j.Logger;

import com.junbao.hf.utils.common.CheckUtil;
import com.junbao.hf.utils.common.DateUtils;
import com.junbao.hf.utils.common.StringUtils;

public class MessageResponseDone {

	private static Logger logger = Logger.getLogger(MessageResponseDone.class);



	/**
	 * 拼装返回核心的
	 * 
	 * @param sendMsg
	 *            原消息
	 * @param channelserialid
	 *            上游的订单号
	 * @param core_resultno
	 *            返回核心的结果码
	 * @param core_errorcode
	 * @param core_resultmsg
	 * @param core_balance
	 *            余额
	 * @return
	 */
	public static String createResultMsg(String oldMsg, String channelserialid, String core_resultno, String core_errorcode, String core_resultmsg, String core_balance, String core_finishmoney,
			String core_sendserialid) {
		StringBuffer sb = new StringBuffer();
		String dealtime = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
		String hfserialid = CheckUtil.getSubStringStartsWith(oldMsg, "hfserialid", "=", "&");
		// 异步回调的
		sb.append("comm=");
		sb.append("8101");
		sb.append("&");
		sb.append("version=");
		sb.append("1.0");
		sb.append("&");
		sb.append("hfserialid=");
		sb.append(hfserialid);
		sb.append("&");
		sb.append("channelserialid=");
		sb.append(StringUtils.getStringFromEmpty(channelserialid));
		sb.append("&");
		sb.append("sendserialid=");
		sb.append(core_sendserialid);
		sb.append("&");
		sb.append("dealtime=");
		sb.append(dealtime);
		sb.append("&");
		sb.append("dealamount=");
		sb.append(StringUtils.getStringFromEmpty(core_finishmoney));
		sb.append("&");
		sb.append("resultno=");// 结果处理
		sb.append(StringUtils.getStringFromEmpty(core_resultno));
		sb.append("&");
		sb.append("errorcode=");// 详细结果吗
		sb.append(StringUtils.getStringFromEmpty(core_errorcode));
		sb.append("&");
		sb.append("resultmsg=");// 中文
		sb.append(fmtMsg(core_resultmsg));
		sb.append("&");
		sb.append("errormsgother=");// 其他
		sb.append(fmtMsg(core_resultmsg));
		sb.append("&");
		sb.append("balance=");// 余额
		sb.append(core_balance);
		return sb.toString();
	}

	/**
	 * 拼装返回核心的
	 * 
	 * @param sendMsg
	 *            原消息
	 * @param channelserialid
	 *            上游的订单号
	 * @param core_resultno
	 *            返回核心的结果码
	 * @param core_errorcode
	 * @param core_resultmsg
	 * @param core_balance
	 *            余额
	 * @return
	 */
	public static String createResultMsgSearch(String sendMsg, String core_resultno, String core_chargestatus, String core_finishmoney, String core_channelserialid, String core_sendserialid,
			String core_errorcode, String core_resultmsg) {
		// 8002 正规查询
		StringBuffer sb = new StringBuffer();
		String dealtime = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
		String hfserialid = CheckUtil.getSubStringStartsWith(sendMsg, "hfserialid", "=", "&");
		sb.append("comm=");
		sb.append("8102");
		sb.append("&");
		sb.append("version=");
		sb.append("1.0");
		sb.append("&");
		sb.append("hfserialid=");
		sb.append(hfserialid);
		sb.append("&");
		sb.append("resultno=");
		sb.append(StringUtils.getStringFromEmpty(core_resultno));
		sb.append("&");
		sb.append("chargestatus=");
		sb.append(StringUtils.getStringFromEmpty(core_chargestatus));
		sb.append("&");
		sb.append("dealtime=");
		sb.append(dealtime);
		sb.append("&");
		sb.append("trantype=");
		sb.append("01");
		sb.append("&");
		sb.append("dealamount=");
		sb.append(core_finishmoney);
		sb.append("&");
		sb.append("channelserialid=");
		sb.append("-1".equals(core_channelserialid) ? "" : StringUtils.getStringFromEmpty(core_channelserialid));
		sb.append("&");
		sb.append("sendserialid=");
		sb.append("");
		sb.append("&");
		sb.append("errorcode=");
		sb.append(core_errorcode);
		sb.append("&");
		sb.append("resultmsg=");
		sb.append(fmtMsg(core_resultmsg));
		return sb.toString();
	}

	/**
	 * 删除中文描述中的特殊字符
	 */
	public static String fmtMsg(String msg) {
		if (null != msg) {
			return msg.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("\\?", "").replace("&", "").replace("=", "").replace("。", "").replace("-", "").replaceAll(" ", "").trim();
		} else {
			return "";
		}
	}

	public static void main(String[] args) {
		String ss = "交&易=成?功";
		// System.out.println(MessageResponseDone.fmtMsg(ss));
		System.out.println(ss.replaceAll("[?]", ""));
	}

}

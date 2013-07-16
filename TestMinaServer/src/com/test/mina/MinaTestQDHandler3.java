package com.test.mina;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.CheckUtil;
import com.junbao.hf.utils.common.DateUtils;
import com.junbao.hf.utils.common.HttpSignServiceV2;

/**
 * 根据手机号码结尾做判断 如果是0则成功，1失败 其他正在处理
 * 
 * @author Administrator
 * 
 */
public class MinaTestQDHandler3 extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(MinaTestQDHandler3.class);
	
	public static int count = 1;
	
	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为String:
		String req = (String) message;
		if (null == req || "".equals(req.trim()) || "HEARTBEAT_REQUEST".equals(req.trim())) {
			session.write("HEARTBEAT_RESPONSE");
		} else {
			logger.info("接收充值信息0则成功，1失败其他正在处理:" + req);
			
			String comm = CheckUtil.getSubStringStartsWith(req, "comm", "=", "&");
			String accnum = CheckUtil.getSubStringStartsWith(req, "accnum", "=", "&");
			String res = "";
			StringBuffer sb = new StringBuffer();
			String resultno = "";
			if("60003".equals(comm)){
				String n2 = accnum.substring(9, 10);
				if("0".equals(n2)){//倒数2位
					resultno = "0037";
				}else if("1".equals(n2)){
					resultno = "0038";
				}else if("2".equals(n2)){
					resultno = "0036";
				}else{
					resultno = "0001";
				}
				String ss = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
				sb.append("").append("comm=").append("61003");
				sb.append("&").append("resultno=").append(resultno);
				sb.append("&").append("czserialid=").append(CheckUtil.getSubStringStartsWith(req, "czserialid", "=", "&"));
				sb.append("&").append("hfserialid=").append(CheckUtil.getSubStringStartsWith(req, "hfserialid", "=", "&"));
				sb.append("&").append("qdczserialid=").append(ss);
				sb.append("&").append("dealtime=").append(ss);
				sb.append("&").append("dealamount=").append("1000");
				
				res = sb.toString();
				
				if("0".equals(n2) || "1".equals(n2) || "2".equals(n2)){//倒数2位
					
				}else{
					Thread.sleep(45000);
				}
				
			}else if("60016".equals(comm)){
				String n3 = accnum.substring(8, 9);
				if("0".equals(n3)){//倒数2位
					resultno = "0037";
				}else if("1".equals(n3)){
					resultno = "0038";
				}else if("2".equals(n3)){
					resultno = "0036";
				}else{
					resultno = "0001";
				}
				String ss = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
				sb.append("").append("comm=").append("61016");
				sb.append("&").append("resultno=").append(resultno);
				sb.append("&").append("czserialid=").append(CheckUtil.getSubStringStartsWith(req, "czserialid", "=", "&"));
				sb.append("&").append("hfserialid=").append(CheckUtil.getSubStringStartsWith(req, "hfserialid", "=", "&"));
				sb.append("&").append("qdczserialid=").append(ss);
				sb.append("&").append("dealtime=").append(ss);
				sb.append("&").append("dealamount=").append("10000");
				
				res = sb.toString();
				
				if("0".equals(n3) || "1".equals(n3) || "2".equals(n3)){//倒数2位
					
				}else{
					Thread.sleep(45000);
				}
			}else{
				String hfserialid = CheckUtil.getSubStringStartsWith(req, "hfserialid", "=", "&");
				String channelserialid = "";
				String dealtime = getFormatDate();
				resultno = result(accnum);
				Thread.sleep(5 * 1000);
				res = "comm=61001&version=1.0&hfserialid=" + hfserialid + "&channelserialid=" + channelserialid + "&dealtime=" + dealtime + "&dealamount=&sendserialid=&resultno=" + resultno;
			}
			String messageDigest = HttpSignServiceV2.creatHttpParamLineString(res.trim(), "&", "verifystring", "123456");
			logger.info("返回信息:" + messageDigest);
			session.write(messageDigest);
		}
	}
	
	public String getFormatDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(new Date());
	}
	
	public String getFormatDate2() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(new Date());
	}

	public String result(String ss) {
		String end = ss.substring(ss.length() - 1, ss.length());
		int a = Integer.valueOf(end);
		String r = "";
		if (a == 0) {
			r = "0000";// <!-- 渠道返回充值成功 -->
		} else if (a == 1) {
			r = "1006";
		} else {
			r = "2006";
		}
		return r;
	}

}

package com.test.client.jbcz;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.junbao.hf.utils.common.HttpSignServiceV2;
import com.test.client.jbcz.handler.MinaHandler8001_JBCZ;

/**
 * 直接发送渠道socket
 * 
 * @author Administrator
 * 
 */
public class Client8016_JBCZ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int k = 10; k < 11; k++) {
			NioSocketConnector connector = new NioSocketConnector();
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
			TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("GBK"));
			textLineCodecFactory.setDecoderMaxLineLength(4000);
			chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
			// 读写通道10秒内无操作进入空闲状态
			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
			connector.setHandler(new MinaHandler8001_JBCZ());
			connector.setConnectTimeoutMillis(15000);

			String ip = "172.25.61.79";
			// String ip = "172.25.61.151";
			ConnectFuture cf = connector.connect(new InetSocketAddress(ip, 9990));

			for (int i = 0; i < 1; i++) {
				String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
				int c = (int) (Math.random() * 100 + 10);
				if (c > 99) {
					c = c - 10;
				}
				// String mobilenum = "186517701" + c;// 电话号码江苏
				// String mobilenum = "13301106543";// 电话号码江苏
				// String mobilenum = "152013860" + c;// 电话号码江苏
				//String mobilenum = "15201386006";// 电话号码江苏
				// String mobilenum = "057187654321";// 电话号码江苏
				// String mobilenum = "13302001234";// 电话号码江苏
				String orderid = "TJB" + ordertime + i + k;
				// String hfserialid = "THF20120730151744010";
				// String chargeamount = k * 20 + "00";
//				String chargeamount = "10000";
				
				String chargeamount = "3000";
				String provinceid = "1";//北京1
				String ispid = "1";
				String mobilenum = "15201386001";
				
//				String chargeamount = "5000";
//				String provinceid = "20";// 广东 20
//				String ispid = "1";
//				String mobilenum = "15999571231";
				
				
				String czserialid = "CZ" + ordertime + i + k;
//				String hfserialid = "THF" + ordertime + i + k;
				String hfserialid = "THF20120807110540010";
				
				String cardnum = "nuordertime";
				String cardpwd = "";
				
//				comm
//				czserialid
//				hfserialid
//				hforderid
//				channelserialid
//				sendserialid
//				ordersource
//				ispid
//				channelid
//				accnum
//				amount
//				provinceid
//				citycode
//				finishtime

//				sb.append("").append("comm=").append(InterfaceCode.CHARGE_SEARCH);
//				sb.append("&").append("czserialid=").append(reverseInfo.getCzserialid());
//				sb.append("&").append("hfserialid=").append(reverseInfo.getHfserialid());
//				sb.append("&").append("hforderid=").append(reverseInfo.getHforderid());
//				sb.append("&").append("channelserialid=").append(StringUtils.getStringFromEmpty(reverseInfo.getChannelserialid()));
//				sb.append("&").append("sendserialid=").append(StringUtils.getStringFromEmpty(reverseInfo.getSendserialid()));
//				sb.append("&").append("ordersource=").append(reverseInfo.getOrdersource());
//				sb.append("&").append("ispid=").append(reverseInfo.getIspid());
//				sb.append("&").append("channelid=").append(StringUtils.getStringFromEmpty(reverseInfo.getChannelid()));
//				sb.append("&").append("accnum=").append(reverseInfo.getMobilenum());
//				sb.append("&").append("amount=").append(StringUtils.getStringFromEmpty(reverseInfo.getQdamount()));
//				sb.append("&").append("provinceid=").append(StringUtils.getStringFromEmpty(reverseInfo.getProvinceid()));
//				sb.append("&").append("citycode=").append(StringUtils.getStringFromEmpty(reverseInfo.getCitycode()));
//				sb.append("&").append("finishtime=").append(StringUtils.getStringFromEmpty(reverseInfo.getFinishtime()));
//				reverseInfo.setSearchmsg(sb.toString());
				
				
				String req = "comm=8016&version=1.0&agentid=DE_WEISD&ordersource=1&czserialid=" + czserialid + "&hforderid=" + orderid + "&hfserialid=" + hfserialid + "&accnum=" + mobilenum + "&amount=" + chargeamount
						+ "&channelid=7334" + "&ispid=" + ispid + "&servicetype=0&provinceid=" + provinceid + "&citycode=010" + "&cardnum=" + cardnum + "&cardpwd=" + cardpwd;
			
//				String mobilenum = "15842396666";// 电话号码江苏
//				String provinceid = "8";
//				String citycode = "0413";
//				String req = "comm=8001&version=1.0&agentid=DE_WEISD&ordersource=1&" + "hforderid=" + orderid + "&hfserialid=" + hfserialid + "&accnum=" + mobilenum + "&amount=" + chargeamount
//						+ "&channelid=7334" + "&ispid=" + ispid + "&servicetype=0&provinceid=" + provinceid + "&citycode=" + citycode + "&cardnum=" + cardnum + "&cardpwd=" + cardpwd;

				cf.awaitUninterruptibly();
				IoSession session = cf.getSession();

				String messageDigest = HttpSignServiceV2.creatHttpParamLineString(req.trim(), "&", "sign", "123456");
				session.write(messageDigest);
				// session.write(messageDigest+"2");//错误加密
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
}

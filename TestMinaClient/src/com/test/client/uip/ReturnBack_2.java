package com.test.client.uip;

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
import com.test.client.MinaDBCoreGWHandler8001;

/**
 * 直接发送渠道socket
 * 
 * @author Administrator
 * 
 */
public class ReturnBack_2 {

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
			connector.setHandler(new MinaDBCoreGWHandler8001());
			connector.setConnectTimeoutMillis(15000);

			String ip = "172.25.61.79";
			int port = 9601;
//			String ip = "172.25.61.151";
			
			ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));

			for (int i = 0; i < 1; i++) {
				String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
				//02041234567891234567  SUPP80080012    10   172.25.58.74                                body={"Order":[{"treadsno":"kzcz360939960","liushuino":"1000360942375","channelsno":"5742442","resultno":"0000","errormsg":"江苏短信猫返回查询成功","status":"01","finishamount":"10000","dealtime":"20120703105124"}]}
				
				String req = "02551234567891234567  SUPP80010011    10   172.25.58.74                                body={\"order\":[{\"channelsno\":\"0000000005743847\",\"dealtime\":\"20120723134821\",\"errormsg\":\"0000000005743847      287455299620120723134821-江苏短信猫返回充值受理成功\",\"liushuino\":\"1000360942345\",\"resultno\":\"22000002\",\"returnResult\":\"10000000\",\"treadsno\":\"kzcz360939960\"}]}";



				cf.awaitUninterruptibly();
				IoSession session = cf.getSession();
				
//				String messageDigest = HttpSignServiceV2.creatHttpParamLineString(req.trim(), "&", "sign", "123456");
				String messageDigest = req;
				session.write(messageDigest);
//				session.write(messageDigest+"2");//错误加密
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

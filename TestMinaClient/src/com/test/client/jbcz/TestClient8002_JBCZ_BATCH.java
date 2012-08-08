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
public class TestClient8002_JBCZ_BATCH {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int k = 10; k < 14; k++) {
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

			for (int i = 0; i < 500; i++) {
				String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
				int c = (int) (Math.random() * 100 + 10);
				
				int end = 0;
				if (c > 50) {
					end = 1;
				}				
				
				
				String hf = "THF20120731152132010";
				String accnum = "18651770155";
				String req = "comm=8002&version=1.0&hfserialid=" + hf + "&channelserialid=&sendserialid=-1&ispid=0&accnum=" + accnum + "&channelid=7021&trantype=01";

				cf.awaitUninterruptibly();
				IoSession session = cf.getSession();

				String messageDigest = HttpSignServiceV2.creatHttpParamLineString(req.trim(), "&", "sign", "123456");
				session.write(messageDigest);
				// session.write(messageDigest+"2");//错误加密
				try {
					Thread.sleep(50);
//					Thread.sleep(1000);
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

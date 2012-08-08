package com.test.client.ka.qd;

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
public class TestClient8001_Web_2_qd_DX2_Balance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (int k = 0; k < 1; k++) {
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
//			String ip = "172.25.61.151";
			ConnectFuture cf = connector.connect(new InetSocketAddress(ip, 9992));

			for (int i = 0; i < 1; i++) {
				String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
				int c = (int) (Math.random() * 100 + 10);
				if (c > 99) {
					c = c - 10;
				}
				String req = "comm=8012&version=1.0";

				cf.awaitUninterruptibly();
				IoSession session = cf.getSession();
				
				String messageDigest = HttpSignServiceV2.creatHttpParamLineString(req.trim(), "&", "verifystring", "123456");
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

package com.test.client.jbcz;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
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
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient8002_JBCZ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File destDir = new File("D:\\http\\");
		File[] a = destDir.listFiles();

		for (int i = 0; i < a.length; i++) {
			File f = a[i];
			String filename = f.getName();
			if (filename.endsWith(".jpg") || filename.endsWith(".html")) {
				f.delete();
			}
		}

		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaHandler8001_JBCZ());
		connector.setConnectTimeoutMillis(15000);
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.79", 9990));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		String order = "JB011110250931268401";
		String hf = "THF20120807110540010";
		String accnum = "18651770155";
		String req = "comm=8002&version=1.0&hfserialid=" + hf + "&channelserialid=&sendserialid=-1&ispid=0&accnum=" + accnum + "&channelid=7021&trantype=01";
		
		String messageDigest = HttpSignServiceV2.creatHttpParamLineString(req.trim(), "&", "sign", "123456");
		session.write(messageDigest);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

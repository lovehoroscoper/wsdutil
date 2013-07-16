package com.test.mina.server.huaruida;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.test.mina.server.MinaServerHandler;

public class TestMinaServer {

	private static Logger logger = Logger.getLogger(MinaServerHandler.class);

	public static void main(String[] args) {
		TestMinaServer s = new TestMinaServer();
		s.startListener();
	}

	public boolean startListener() {
		 int port = 9999;
//		int port = 6003;
		boolean isSuc = false;
		try {
			SocketAcceptor acceptor = new NioSocketAcceptor();
			acceptor.setReuseAddress(true);
			acceptor.getSessionConfig().setReadBufferSize(1028 * 2);
			acceptor.getSessionConfig().setWriteTimeout(10);
			
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

			TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("UTF-8"));
			textLineCodecFactory.setDecoderMaxLineLength(4000);
			chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
			
			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
			acceptor.setHandler(new MinaServerHandlerHUA());
			acceptor.bind(new InetSocketAddress(port));
			
			
			String macStr = "82288E46A4C26AB64D4A9588";
			// 主密钥假设为：1234567890ABCDEF1234567890ABCDEF
			// byte[] ImacKey =
			// hexStringToByte("1234567890ABCDEF1234567890ABCDEF");
			String main_key = "1234567890ABCDEF";
			byte[] leftKey = DES_CBC.hexStringToByte(main_key);// "1234567890ABCDEF"
			byte[] rightKey = DES_CBC.hexStringToByte(main_key);// "1234567890ABCDEF"
			// MAC的加密密钥的密文为24位（其中 “MAC密钥”为前16位是密文，后8位是密文校验值；
			// 前16位解出明文后，对8个数值0做单倍长密钥算法，取结果的前8位与密文校验值比较应该是一致的）。
			// 收到的密钥信息报文体假设为：75DC386FC624184D6846EE8B（其中MAC密钥75DC386FC624184D，单倍长校验码：6846EE8B）
			String mac_16 = macStr.substring(0, 16);
			String mac_8 = macStr.substring(16, 24);
			byte[] MACMsg = DES_CBC.hexStringToByte(mac_16);// "75DC386FC624184D"
			byte[] MACCheck = DES_CBC.hexStringToByte(mac_8);// "6846EE8B"
			// 前16位解出明文后
			byte[] key = DES_CBC.decryptMode(leftKey, MACMsg);// 通过主密钥和MAC密钥进行3DES解密可以得到工作密钥
			key = DES_CBC.encryptMode(rightKey, key); // 通过主密钥和MAC密钥进行3DES解密可以得到工作密钥
			key = DES_CBC.decryptMode(leftKey, key); // 通过主密钥和MAC密钥进行3DES解密可以得到工作密钥
			
			DesCbcBiz.getInstance();
			DesCbcBiz.getInstance().setKey(key);

			logger.info("服务器启动" + port + ")");
		} catch (IOException e1) {
			isSuc = false;
			logger.error("启动()异常。", e1);
		}
		return isSuc;
	}

}

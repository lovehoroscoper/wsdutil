package com.test.nio.testserver1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

public class ProtocolImpl implements Protocol {

	private int buffSize;

	public ProtocolImpl(int buffSize) {
		this.buffSize = buffSize;
	}

	/**
	 * 接收一个socketChannel的处理
	 */
	public void handleAccept(SelectionKey skey) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel clientChannel = ((ServerSocketChannel)skey.channel()).accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(skey.selector(), SelectionKey.OP_READ,
				ByteBuffer.allocate(buffSize));
	}

	/**
	 * 向一个socketChannel写入
	 */
	public void handlWrite(SelectionKey skey ,ByteBuffer bf) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel clientChannel = (SocketChannel) skey.channel();
		clientChannel.write(bf);
		skey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
	}

	/**
	 * 接收一个socketChannel的处理
	 */
	public void handleRead(SelectionKey skey) throws IOException {
		// TODO Auto-generated method stub
		// 获得客户端通讯信道
		SocketChannel clientChannel = (SocketChannel) skey.channel();
		// 得到清空缓冲区
		ByteBuffer bf = (ByteBuffer) skey.attachment();
		bf.clear();
		long bytesRead = clientChannel.read(bf);
		if (bytesRead == -1) {
			clientChannel.close();
		} else {
			bf.flip();

			// 将字节转化为为UTF-16的字符串
			String receivedString = Charset.forName("UTF-16").newDecoder()
					.decode(bf).toString();

			// 控制台打印出来
			System.out.println("接收到来自"
					+ clientChannel.socket().getRemoteSocketAddress() + "的信息:"
					+ receivedString);
			skey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);	
		}

	}

}

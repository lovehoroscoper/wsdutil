//package com.test.nio.read;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.AsynchronousCloseException;
//import java.nio.channels.ClosedByInterruptException;
//import java.nio.channels.ClosedChannelException;
//import java.nio.channels.NotYetConnectedException;
//import java.util.Vector;
//
///**
// * @desc 描述：
// * 
// * @author weisd E-mail:xiyangdewuse@163.com
// * @version 创建时间：2012-9-13 下午2:33:44
// */
//public class Test {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * 接收内核 socket 缓冲区的数据到连接自建立的缓冲区(支持自动增长)
//	 * 
//	 * @param in
//	 *            实际接收到的数据包队列
//	 * @return PACKET_ERROR 表示对方关闭连接, PACKET_FULL 表示数据发送完毕, PACKET_LESS
//	 *         表示数据继续接收.
//	 * @see #parse(Vector)
//	 */
//	public Define.DataRecvStatus recv(Vector<byte[]> in) throws Exception {
//		Define.DataRecvStatus result = null;
//		result = Define.DataRecvStatus.PACKET_FULL;
//		int received = 0;
//		m_recvbuff.limit(this.m_recvcapacity);
//		m_onerecvbuff.clear();
//		// 尽量把内核接收缓冲区的数据全部读取出来
//		while (received < m_onerecvbuff.limit() && m_onerecvbuff.hasRemaining()) {
//			try {
//				int len = 0;
//				len = m_socketchannel.read(m_onerecvbuff);
//				// 通道 数据被读取完全,是否表示这个连接已经没有数据了
//				if (len < 0) {
//					// 一般是-1,可以认为连接被对方关闭
//					result = Define.DataRecvStatus.PACKET_ERROR;
//					break;
//				} else if (len == 0) {
//					// 如果每次都是 0,会是什么情况
//					result = Define.DataRecvStatus.PACKET_LESS;
//					break;
//				} else {
//					received += len;
//					result = Define.DataRecvStatus.PACKET_LESS;
//				}
//			} catch (NotYetConnectedException e) {
//				result = Define.DataRecvStatus.PACKET_ERROR;
//				logger.error("oops, got an exception: ", e);
//				break;
//			} catch (ClosedByInterruptException e) {
//				result = Define.DataRecvStatus.PACKET_ERROR;
//				logger.error("oops, got an exception: ", e);
//				break;
//			} catch (AsynchronousCloseException e) {
//				result = Define.DataRecvStatus.PACKET_ERROR;
//				logger.error("oops, got an exception: ", e);
//				break;
//			} catch (ClosedChannelException e) {
//				result = Define.DataRecvStatus.PACKET_ERROR;
//				logger.error("oops, got an exception: ", e);
//				break;
//			} catch (IOException e) {
//				result = Define.DataRecvStatus.PACKET_ERROR;
//				logger.error("oops, got an exception: ", e);
//				break;
//			}
//		}
//
//		m_onerecvbuff.flip();
//		if (m_onerecvbuff.limit() > m_recvbuff.remaining()) {
//			// 继续扩大接收缓冲区
//			m_recvbuff.flip();
//			byte[] bytes = new byte[m_recvbuff.limit()];
//			m_recvbuff.get(bytes, 0, m_recvbuff.limit());
//			m_recvcapacity *= 2;
//			m_recvbuff = ByteBuffer.allocate(m_recvcapacity);
//			m_recvbuff.put(bytes);
//		}
//
//		// 合并本次实际接收到的数据
//		m_recvbuff.put(m_onerecvbuff);
//
//		// 如果数据没有接收完整,对方关闭连接,最后一段不完整数据可能被丢弃
//		// 如果解析协议发生错误,等同于连接被关闭,不过是服务器主动关闭
//		if (result == Define.DataRecvStatus.PACKET_ERROR) {
//			if (received > 0) {
//				parse(in);
//			}
//		} else {
//			if (received > 0) {
//				result = parse(in);
//			}
//		}
//
//		return result;
//	}
//}

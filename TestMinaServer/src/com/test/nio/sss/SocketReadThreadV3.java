//package com.caoya.cz.thread;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//import java.util.Iterator;
//import java.util.Set;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.biz.SocketHelp;
//import com.caoya.cz.constant.MessageResponseDone;
//import com.hisunsray.commons.res.Config;
//import com.junbao.hf.utils.common.DateUtils;
//
//public class SocketReadThreadV3 extends Thread {
//
//	private static Logger logger = Logger.getLogger(SocketReadThreadV3.class);
//
//	// 读取流处理线程池维护线程的最少数量，哪怕是空闲的
//	private int readStreamThreadpoolsize;
//	// 读取流处理线程池维护线程所允许的空闲时间(秒)
//	private int readStreamThreadpoolKeepalivetime;
//	// 读取处理线程池维护线程的最大数量
//	private int readStreamThreadpoolMaximumpoolsize;
//	private static ThreadPoolExecutor threadPool;
//	private static int soCount;
//	private boolean isInterrupted = false;
//	private ByteBuffer buffer;
//
//	public SocketReadThreadV3() {
//		buffer = ByteBuffer.allocate(1024);
//		if (threadPool == null) {
//			readStreamThreadpoolsize = Integer.parseInt(Config.getProperty("READSTREAM_THREADPOOL_SIZE"));
//			readStreamThreadpoolKeepalivetime = Integer.parseInt(Config.getProperty("READSTREAM_THREADPOOL_KEEPALIVETIME"));
//			readStreamThreadpoolMaximumpoolsize = Integer.parseInt(Config.getProperty("READSTREAM_THREADPOOL_MAXIMUMPOOLSIZE"));
//			// 构造一个线程池
//			threadPool = new ThreadPoolExecutor(readStreamThreadpoolsize, readStreamThreadpoolMaximumpoolsize, readStreamThreadpoolKeepalivetime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(
//					readStreamThreadpoolMaximumpoolsize), new ThreadPoolExecutor.CallerRunsPolicy());
//			logger.info("*********构造读取buffer response流处理服务线程池|threadPoolsize=" + readStreamThreadpoolsize + "|Maximumpoolsize=" + readStreamThreadpoolMaximumpoolsize + "|Keepalivetime="
//					+ readStreamThreadpoolKeepalivetime + "|TimeUnit.SECONDS" + TimeUnit.SECONDS);
//		}
//	}
//
//	public void interrupt() {
//		isInterrupted = true;
//		threadPool.shutdownNow();
//		super.interrupt();
//	}
//
//	public void run() {
//		while (!isInterrupted) {
//			try {
//				// logger.info("经过：SocketHelp.getSelector():");
//				while (SocketHelp.getSelector().select(Integer.valueOf(Config.getProperty("SELECTOR_SELECT_TIMEOUT")) * 1000) > 0) {
//					Selector selector = SocketHelp.getSelector();
//					// logger.info("经过：while (!isInterrupted)");
//					try {
//						Set<SelectionKey> keys = selector.selectedKeys();
//						Iterator<SelectionKey> iter = keys.iterator();
//						while (iter.hasNext()) {
//							SelectionKey key = (SelectionKey) iter.next();
//							iter.remove();
//							if (key.isReadable()) {
//								// 将缓冲区清空以备下次读取
//								buffer.clear();
//								SocketChannel socketChannel = (SocketChannel) key.channel();
//								ByteArrayOutputStream bos = new ByteArrayOutputStream();
//								// 此循环从本事件的客户端对象读取服务器发送来的数据到缓冲区中
//								int byteRead = 0;
//								while ((byteRead = socketChannel.read(buffer)) > 0) {
//									buffer.flip();
//									// 如果获得数据长度比缓冲区大小小的话
//									if (byteRead < buffer.capacity()) {
//										// 建立一个临时byte数组,将齐长度设为获取的数据的长度
//										byte[] readByte = new byte[byteRead];
//										// 循环向此临时数组中添加数据
//										for (int i = 0; i < byteRead; i++) {
//											readByte[i] = buffer.get(i);
//										}
//										// 将此数据存入byte流中
//										bos.write(readByte);
//									} else {
//										// 否则就是获得数据长度等于缓冲区大小
//										// 将读取到的数据写入到byte流对象中
//										bos.write(buffer.array());
//									}
//									// 将缓冲区清空，以便进行下一次存储数据
//									buffer.clear();
//								}
//								buffer.flip();
//								threadPool.execute(new DetailReadStreamThreadV3(bos));
//								key.interestOps(SelectionKey.OP_READ);
//								if (byteRead > -1) {
//									soCount = 0;
//								} else {
//									String error_msg = "读取服务器信息返回[" + byteRead + "]关闭连接";
//									String time = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//									MessageResponseDone.createResultLoginMsg("", "0", "no", error_msg, time, "0", "");
//									sleep(5000);
//									if (soCount >= Integer.parseInt(Config.getProperty("READ_CLOSE_TIMEOUT")) * 1000) {
//										soCount = 0;
//										throw new RuntimeException(error_msg);
//									} else {
//										logger.error(error_msg);
//										soCount += 5000;
//									}
//								}
//							}
//						}
//					} catch (IOException e) {
//						logger.error("读取上游返回数据线程[IO]异常:" + e.getMessage());
//						try {
//							sleep(10000);
//							SocketHelp.connect();
//							LoginThread login = new LoginThread();
//							login.start();
//						} catch (Exception e2) {
//							String time = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//							MessageResponseDone.createResultLoginMsg("", "0", "no", "读取返回数据[IO]异常后登录失败", time, "0", "");
//							logger.error("重连[IO]异常:" + e2.getMessage());
//							Thread.sleep(5000);
//						}
//					} catch (Exception e) {
//						logger.error("读取数据线程[E]异常:", e);
//						try {
//							sleep(10000);
//							SocketHelp.connect();
//							LoginThread login = new LoginThread();
//							login.start();
//						} catch (Exception e2) {
//							logger.error("重连[E]异常:" + e2.getMessage());
//							String time = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//							MessageResponseDone.createResultLoginMsg("", "0", "no", "读取数据[E]异常后登录失败", time, "0", "");
//							Thread.sleep(5000);
//						}
//					}
//				}
//			} catch (Exception e) {
//				try {
//					logger.error("读取线程[外层]异常:", e);
//					sleep(5000);
//				} catch (Exception e1) {
//					logger.error("sleep error:" + e1.getMessage());
//				}
//			}
//		}
//	}
//
//}

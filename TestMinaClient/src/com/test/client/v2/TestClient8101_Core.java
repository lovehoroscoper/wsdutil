package com.test.client.v2;

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

import com.test.client.MinaDBCoreGWHandler8101;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient8101_Core {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory =  new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler8101());
		connector.setConnectTimeoutMillis(15000);

		//select '"'||t.hf_serialid||',"+' from od_fullnote t where t.hf_status = 2
		
	
			String ss =	
					"HF011112011436118288,"+
					"HF011112011436118289,"+
					"HF011112011436178300,"+
					"HF011112011436178301,"+
					"HF011112011436248312,"+
					"HF011112011436248313,"+
					"HF011112011436308315,"+
					"HF011112011436308316,"+
					"HF011112011436308317,"+
					"HF011112011436308318,"+
					"HF011112011436308319,"+
					"HF011112011436308320,"+
					"HF011112011436308321,"+
					"HF011112011436328322,"+
					"HF011112011436328323,"+
					"HF011112011436338324,"+
					"HF011112011436338325,"+
					"HF011112011436338326,"+
					"HF011112011436338327,"+
					"HF011112011436358328,"+
					"HF011112011436358329,"+
					"HF011112011436468351,"+
					"HF011112011436468352,"+
					"HF011112011436498357,"+
					"HF011112011436498358,"+
					"HF011112011436188302,"+
					"HF011112011436188303,"+
					"HF011112011436248314,"+
					"HF011112011436368330,"+
					"HF011112011436368331,"+
					"HF011112011436378335,"+
					"HF011112011436378336,"+
					"HF011112011436408339,"+
					"HF011112011436408340,"+
					"HF011112011436408341,"+
					"HF011112011436408342,"+
					"HF011112011436438347,"+
					"HF011112011436438348,"+
					"HF011112011436538365,"+
					"HF011112011436538366,"+
					"HF011112011436568371,"+
					"HF011112011436568372,"+
					"HF011112011436128290,"+
					"HF011112011436128291,"+
					"HF011112011436148292,"+
					"HF011112011436148293,"+
					"HF011112011436148294,"+
					"HF011112011436148295,"+
					"HF011112011436168296,"+
					"HF011112011436168297,"+
					"HF011112011436578373,"+
					"HF011112011436578374,"+
					"HF011112011436598377,"+
					"HF011112011436598378,"+
					"HF011112011436098282,"+
					"HF011112011436098283,"+
					"HF011112011436098284,"+
					"HF011112011436098285,"+
					"HF011112011436368332,"+
					"HF011112011436368333,"+
					"HF011112011436368334,"+
					"HF011112011436438345,"+
					"HF011112011436438346,"+
					"HF011112011436448349,"+
					"HF011112011436448350,"+
					"HF011112011436478353,"+
					"HF011112011436478354,"+
					"HF011112011436478355,"+
					"HF011112011436478356,"+
					"HF011112011436518361,"+
					"HF011112011436518362,"+
					"HF011112011437008380,"+
					"HF011112011436508359,"+
					"HF011112011436508360,"+
					"HF011112011436528363,"+
					"HF011112011436528364,"+
					"HF011112011436558367,"+
					"HF011112011436558368,"+
					"HF011112011436558369,"+
					"HF011112011436558370,"+
					"HF011112011436588375,"+
					"HF011112011436588376,"+
					"HF011112011436108286,"+
					"HF011112011436108287,"+
					"HF011112011436178298,"+
					"HF011112011436178299,"+
					"HF011112011436208304,"+
					"HF011112011436208305,"+
					"HF011112011436218306,"+
					"HF011112011436218307,"+
					"HF011112011436218308,"+
					"HF011112011436218309,"+
					"HF011112011436238310,"+
					"HF011112011436238311,"+
					"HF011112011436398337,"+
					"HF011112011436398338,"+
					"HF011112011436428343,"+
					"HF011112011436428344,"+
					"HF011112011436598379,"+
					"HF011112011437018381";
		
		String[] a = ss.split(",");
		int t = 1;
		for (int i = 0; i < a.length; i++) {
			ConnectFuture cf = null;
			String req_new = "";
			if(t == 1){
				
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String  dateTime= simpleDate.format(new Date());
				String hfserialid = a[i];
				String sendserialid = "s_" + dateTime;
				String channelserialid = "c_" + dateTime;
				String dealtime = dateTime;
				String dealamount = "2000";
				String resultno = "0000";
				String resultmsg = "充值成功";
				String othererrorcode = "0000";
				
				req_new = "comm=8101&version=1.0&hfserialid=" + hfserialid + 
						"&sendserialid=" + sendserialid + 
						"&channelserialid=" + channelserialid + 
						"&dealtime=" + dealtime + 
						"&dealamount=" + dealamount + 
						"&resultno=" + resultno + 
						"&resultmsg=" + resultmsg +
						"&errorcode=" + othererrorcode;
				
				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
				t = 0;
				
			}else{
				
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String  dateTime= simpleDate.format(new Date());

				String hfserialid = a[i];
				String sendserialid = "s_" + dateTime;
				String channelserialid = "c_" + dateTime;
				String dealtime = dateTime;
				String dealamount = "2000";
				String resultno = "1006";
				int c = (int) (Math.random() * 5000 + 1000);
				String resultmsg = "充值失败：" + c;
				String othererrorcode = "" + c;

				
				req_new = "comm=8101&version=1.0&hfserialid=" + hfserialid + 
						"&sendserialid=" + sendserialid + 
						"&channelserialid=" + channelserialid + 
						"&dealtime=" + dealtime + 
						"&dealamount=" + dealamount + 
						"&resultno=" + resultno + 
						"&resultmsg=" + resultmsg +
						"&errorcode=" + othererrorcode;
				
				cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
				t = 1;
			}
			cf.awaitUninterruptibly();
			IoSession session = cf.getSession();

			
			session.write(req_new);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

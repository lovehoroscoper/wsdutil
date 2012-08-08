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

import com.junbao.hf.utils.common.HttpSignServiceV2;
import com.junbao.hf.utils.common.KeyedDigestMD5;
import com.test.client.v2.handler.MinaDBCoreGWHandler8003;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient8003_CZ_MORE {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String old = 
				"JB011204171701541512,OD201204171701541552;"+
						"JB011204171701521511,OD201204171701511551;"+
						"JB011204171701501510,OD201204171701491550;"+
						"JB011204171701471509,OD201204171701471549;"+
						"JB011204171701451508,OD201204171701451548;"+
						"JB011204171701431507,OD201204171701421547;"+
						"JB011204171701391506,OD201204171701391546";
		
		String orderid = "";
		String hforderid = "";
		String mobilenum = "15201382330";
		
		String[] oneArr = old.split(";");
		
		for (int i = 0; i < oneArr.length; i++) {
			NioSocketConnector connector = new NioSocketConnector();
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
			TextLineCodecFactory textLineCodecFactory =  new TextLineCodecFactory(Charset.forName("GBK"));
			textLineCodecFactory.setDecoderMaxLineLength(4000);
			chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
			// 读写通道10秒内无操作进入空闲状态
			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
			connector.setHandler(new MinaDBCoreGWHandler8003());
			connector.setConnectTimeoutMillis(15000);

			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
			String  dateTime= simpleDate.format(new Date());
			
			String one_str = oneArr[i];
			String[] orderarr = one_str.split(",");
			
			String comm = "8003";
			String agentid = "DE201107261541540282";
			String ordersource = "1";
			
			String amount = "100";
			String saleserialid = "ECZ" + dateTime + i;
			
			hforderid = orderarr[0];
			orderid = orderarr[1];
		
			String req_new = "comm=" + comm +
					"&agentid=" + agentid + 
					"&ordersource=" + ordersource + 
					"&mobilenum=" + mobilenum + 
					"&amount=" + amount + 
					"&orderid=" + orderid + 
					"&hforderid=" + hforderid + 
					"&saleserialid=" + saleserialid;
			
			
			String req_new_bb = HttpSignServiceV2.creatHttpParamLineString(req_new, "&", "verifystring", "123456");
			
			
//			ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
//			ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.79", 9001));
			ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.151", 9001));
//			ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
			cf.awaitUninterruptibly();
			IoSession session = cf.getSession();
			session.write(req_new_bb);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("---------------------------------------");
		}
		

	}

}

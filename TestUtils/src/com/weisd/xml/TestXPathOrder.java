package com.weisd.xml;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-28 上午9:02:35
 */
public class TestXPathOrder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			//document = reader.read(new File("D:/junbao_newpro/hf-acquiringmq/WebContent/WEB-INF/orderftl/order8001.xml"));
			document = reader.read(new File("D:\\junbao_newpro\\hf-acquiringmq\\WebContent\\WEB-INF\\orderftl\\test\\odrderReq.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		List list = document.selectNodes("//xrpc2");
		
		Element version = (Element) document.selectSingleNode("//xrpc/xrpchead/version");
		System.out.println(version.getName());
		System.out.println(version.getTextTrim());
		
		
		Element rpcmode = (Element) document.selectSingleNode("//xrpc/public_req/rpcmode");
		System.out.println(rpcmode.getName());
		System.out.println(rpcmode.getTextTrim());
		Element memcache_key = (Element) document.selectSingleNode("//xrpc/public_req/memcache_key");
		System.out.println(memcache_key.getName());
		System.out.println();
		
		String key = memcache_key.getTextTrim();
		if(key == null){
			System.out.println("null");
		}
		if("".equals(key)){
			System.out.println(" ''");
		}
		
		
		Element transcode = (Element) document.selectSingleNode("//xrpc/transsvr_req/head/transcode");
		System.out.println(transcode.getName());
		System.out.println(transcode.getTextTrim());
		Element channel = (Element) document.selectSingleNode("//xrpc/transsvr_req/head/channel");
		System.out.println(channel.getName());
		System.out.println(channel.getTextTrim());
		Element chndate = (Element) document.selectSingleNode("//xrpc/transsvr_req/head/chndate");
		System.out.println(chndate.getName());
		System.out.println(chndate.getTextTrim());
		Element chntime = (Element) document.selectSingleNode("//xrpc/transsvr_req/head/chntime");
		System.out.println(chntime.getName());
		System.out.println(chntime.getTextTrim());
		
		
		Element maxrecords = (Element) document.selectSingleNode("//xrpc/transsvr_req/querymode/maxrecords");
		System.out.println(maxrecords.getName());
		System.out.println(maxrecords.getTextTrim());
		Element offset = (Element) document.selectSingleNode("//xrpc/transsvr_req/querymode/offset");
		System.out.println(offset.getName());
		System.out.println(offset.getTextTrim());
		Element countall = (Element) document.selectSingleNode("//xrpc/transsvr_req/querymode/countall");
		System.out.println(countall.getName());
		System.out.println(countall.getTextTrim());
		

		Element OID_WARE = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/OID_WARE");
		System.out.println(OID_WARE.getName());
		System.out.println(OID_WARE.getTextTrim());
		
		
		Element AMT_OCCUR = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/AMT_OCCUR");
		System.out.println(AMT_OCCUR.getName());
		System.out.println(AMT_OCCUR.getTextTrim());
		
		
		Element JNO_CLI = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/JNO_CLI");
		System.out.println(JNO_CLI.getName());
		System.out.println(JNO_CLI.getTextTrim());
		
		Element OID_REGUSER = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/OID_REGUSER");
		System.out.println(OID_REGUSER.getName());
		System.out.println(OID_REGUSER.getTextTrim());
		
		Element OID_TRADER = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/OID_TRADER");
		System.out.println(OID_TRADER.getName());
		System.out.println(OID_TRADER.getTextTrim());
		
		Element UID_CLI = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/UID_CLI");
		System.out.println(UID_CLI.getName());
		System.out.println(UID_CLI.getTextTrim());
		
		Element COUNT_WARE = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/COUNT_WARE");
		System.out.println(COUNT_WARE.getName());
		System.out.println(COUNT_WARE.getTextTrim());
		
		Element PROD_ONLINEID = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/PROD_ONLINEID");
		System.out.println(PROD_ONLINEID.getName());
		System.out.println(PROD_ONLINEID.getTextTrim());
		
		Element PROD_PAYAMOUNT = (Element) document.selectSingleNode("//xrpc/transsvr_req/body/PROD_PAYAMOUNT");
		System.out.println(PROD_PAYAMOUNT.getName());
		System.out.println(PROD_PAYAMOUNT.getTextTrim());

		

		
//		Element root = (Element) document.selectSingleNode("//root");


//		if (null != root) {
//			// 方法1
//			// 遍历book结点的所有孩子节点（即title....），并进行处理
//			for (Iterator iterInner = root.elementIterator(); iterInner.hasNext();) {
//				Element elementInner = (Element) iterInner.next();
//				String name = elementInner.getName();// 节点的名称，如title
//				String text = elementInner.getText();// 节点的内容，如title标签里的内容
//				System.out.println("name:" + name + ",text:" + text);
//			}
//		}
//		System.out.println(root);
	}

}

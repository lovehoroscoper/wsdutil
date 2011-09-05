package com.weisd.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-2 上午10:50:08
 */
public class TestDom4j {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SAXReader reader = new SAXReader();

		Document document = null;

		try {

			document = reader.read(new File("D:/junbao_newpro/hf-acquiringmq/WebContent/WEB-INF/orderftl/order8001.xml"));

		} catch (DocumentException e) {

			e.printStackTrace();

		}

		Element root = (Element) document.selectSingleNode("recharge//order");
		if (null != root) {
			// 方法1
			// 遍历book结点的所有孩子节点（即title....），并进行处理
			for (Iterator iterInner = root.elementIterator(); iterInner.hasNext();) {
				Element elementInner = (Element) iterInner.next();
				String name = elementInner.getName();// 节点的名称，如title
				String text = elementInner.getText();// 节点的内容，如title标签里的内容

				System.out.println("name:" + name + ",text:" + text);
			}

			// List list = root.elements();
			// for (int i = 0; i < list.size(); i++) {
			// Element book = (Element) list.get(i);
			// String name = book.getName();// Dom4j
			// String text = book.getTextTrim();// Lucene
			// // Studing
			//
			// System.out.println("name:" + name + ",text:" + text);
			// }
		}

		// root.g

		System.out.println(root);

	}

	/**
	 * 解包返回报文为map型
	 * 
	 * @param scoketStr
	 * @param nodePath
	 * @param paramMap
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, String> getMapResult(String scoketStr, String nodePath, Map<String, String> paramMap) throws DocumentException {
		if (null == paramMap) {
			paramMap = new HashMap<String, String>();
		}
		Node node = null;
		Document document = DocumentHelper.parseText(scoketStr);
		List<Object> list = document.selectNodes(nodePath);
		for (int i = 0; i < list.size(); i++) {
			node = (Node) list.get(i);
			paramMap.put(node.getName(), node.getText());
		}
		return paramMap;
	}
}

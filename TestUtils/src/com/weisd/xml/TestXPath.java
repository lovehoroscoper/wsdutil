package com.weisd.xml;

import java.io.File;
import java.util.Iterator;

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
public class TestXPath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			//document = reader.read(new File("D:/junbao_newpro/hf-acquiringmq/WebContent/WEB-INF/orderftl/order8001.xml"));
			document = reader.read(new File("D:\\junbao_newpro\\TestUtils\\src\\com\\weisd\\xml\\test.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Element root = (Element) document.selectSingleNode("//allproducts/product/prodId");
		System.out.println(root.getName());
		System.out.println(root.getTextTrim());
		
		
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
		System.out.println(root);
	}

}

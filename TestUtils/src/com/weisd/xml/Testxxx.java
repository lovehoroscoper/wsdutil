package com.weisd.xml;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-10-20 下午3:21:11
 */
public class Testxxx {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		XmlParserOrder test = new XmlParserOrder();
		// String path = "C:/a.xml";
		String path = "D:\\junbao_newpro\\TestUtils\\src\\com\\weisd\\xml\\order.xml";
		// 读取XML文件
		SAXReader reader = new SAXReader();

		Document doc = null;
		try {
			doc = reader.read(path);
			getConnectTopupResponseFromDocument(doc);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	private static Document xmlToDocument(String xmlString) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	private static String getConnectTopupResponseFromDocument(Document document) {
		// 解析Document
		String str = "";
		try {
			Element fill = document.getRootElement();
			List items = fill.elements();
			for (int i = 0; i < items.size(); i++) {
				Element o = (Element) items.get(i);
				for (Iterator t = o.elements().iterator(); t.hasNext();) {
					Element product = (Element) t.next();
					String value = product.getText();
					String name = product.getName();
					System.out.println("=node name:=====" + name + "====node value:==" + value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 递归遍历方法
	 * 
	 * @param element
	 */
	public Map getElementList(Element element, Map map) {
		List elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			Iterator it = element.attributeIterator();
			map.put(element.getName(), "");
			int i = 0;
			String name = "";
			String value = "";
			while (it.hasNext()) {
				Attribute attribute = (Attribute) it.next();
				if (i == 0) {
					name = attribute.getValue();
				} else {
					value = attribute.getValue();
				}
				i++;
			}
			map.put(name, value);
		} else {
			// 有子元素
			int i = 0;
			String name = "";
			String value = "";
			Iterator it = element.attributeIterator();
			while (it.hasNext()) {
				Attribute attribute = (Attribute) it.next();
				map.put(attribute.getName(), attribute.getValue());
			}
			map.put(element.getName(), "");
			for (Iterator it2 = elements.iterator(); it2.hasNext();) {
				Element elem = (Element) it2.next();
				// 递归遍历
				getElementList(elem, map);
			}
		}
		return map;
	}

}

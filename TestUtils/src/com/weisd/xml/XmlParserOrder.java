package com.weisd.xml;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;

/**
 * Java递归遍历XML所有元素
 * 
 * @author Administrator
 * @version [版本号, Apr 13, 2010]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class XmlParserOrder {
	// private static Map xmlmap = new HashMap();
	// 存储xml元素信息的容器

	public static void main(String args[]) throws DocumentException {
		XmlParserOrder test = new XmlParserOrder();
		// String path = "C:/a.xml";
		String path = "D:\\junbao_newpro\\TestUtils\\src\\com\\weisd\\xml\\order.xml";
		// 读取XML文件
		SAXReader reader = new SAXReader();

		Document doc = reader.read(path);
		// 获取XML根元素
		Element root = (Element) doc.selectSingleNode("//fill//items");
		Map xmlMap = new HashMap();
		test.getElementList(root, xmlMap);
	}

	/**
	 * 获取节点所有属性值 <功能详细描述>
	 * 
	 * @param element
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getNoteAttribute(Element element, Map xmlMap) {
		DefaultAttribute e = null;
		String key = "";
		String value = "";
		List list = element.attributes();
		for (int i = 0; i < list.size(); i++) {
			
			e = (DefaultAttribute) list.get(i);
			if("name".equals(e.getName())){
				key = e.getText();
			}else if("value".equals(e.getName())){
				value = e.getText();
			}
		}
		System.out.println(key + ":" + value);
		xmlMap.put(key, value);
		return "";
	}

	/**
	 * 递归遍历方法 <功能详细描述>
	 * 
	 * @param element
	 * @see [类、类#方法、类#成员]
	 */
	public void getElementList(Element element, Map xmlMap) {
		List elements = element.elements();
		// 没有子元素
		if (elements.isEmpty()) {
			getNoteAttribute(element, xmlMap);
		} else {
			// 有子元素
			Iterator it = elements.iterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				// 递归遍历
				getElementList(elem, xmlMap);
			}
		}
	}

	public String getListString(List elemList) {
		StringBuffer sb = new StringBuffer();
		// for (Iterator it = elemList.iterator(); it.hasNext();) {
		// Leaf2 leaf = (Leaf2) it.next();
		// sb.append("xpath: " +
		// leaf.getXpath()).append(", value: ").append(leaf.getValue());
		// if (!"".equals(leaf.getXattribute())) {
		// sb.append(", Attribute: ").append(leaf.getXattribute());
		// }
		// sb.append("\n");
		// }
		return sb.toString();
	}
}

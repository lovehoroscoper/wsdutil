package com.weisd.xml;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class DOM4JTest {

    /**
    * DOM4J读写XML示例
    * 
    * @param args
    * @throws DocumentException
    * @throws IOException
    * @author 孙宇
    */
    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        Document doc = null;// 声明文档对象

        try {
//        	doc = reader.read(new File("c:/student.xml"));// 读取XML文档
            doc = reader.read(new File("D:/junbao_newpro/hf-acquiringmq/WebContent/WEB-INF/orderftl/odrderReq.xml"));// 读取XML文档
            //String path = "D:/junbao_newpro/hf-acquiringmq/WebContent/WEB-INF/orderftl/odrderReq.xml";
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        String XPath = "//*";
        List nodes = doc.selectNodes(XPath);
        for (Object obj : nodes) {
            Node node = (Node) obj;
            String nodeName = node.getName();
            System.out.println("节点名称：" + nodeName);
            String nodeText = node.getText();
            if (nodeText.trim().equalsIgnoreCase("") == false) {
                System.out.println("节点值：" + nodeText);
            }
            Element element = (Element) node;
            for (int i = 0; i < element.attributeCount(); i++) {
                String attributeName = element.attribute(i).getName();
                String attributeText = element.attribute(i).getText();
                System.out.println("属性：【" + attributeName + "=" + attributeText
                        + "】");
            }
            System.out.println("");
        }

        System.out.println("----------------------------");
        System.out.println("完成");
    }

}


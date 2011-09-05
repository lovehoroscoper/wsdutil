package com.weisd.fmt.ftl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @desc 测试 freemarker 类
 * 
 * @author weisd
 * @version create date ：Dec 28, 2010 6:29:37 PM
 * 
 */
public class TestFreemarker{
	private static Configuration cfg = new Configuration();
	static {
		/**
		 * cfg.setTemplateLoader(new
		 * ClassTemplateLoader(TestFreem1.class.getClassLoader
		 * ().getClass(),"temp")); cfg.setTemplateLoader(new
		 * ClassTemplateLoader(TestFreem1.class,"../../../temp"));
		 * cfg.setTemplateLoader(new
		 * ClassTemplateLoader(Thread.currentThread().getContextClassLoader
		 * ().getClass(),"/temp")); cfg.setTemplateLoader(new
		 * WebappTemplateLoader(applicationContext.get));
		 * cfg.setServletContextForTemplateLoading(new ServletContext(),"");
		 * cfg.setTemplateLoader(new
		 * ClassTemplateLoader(TestFreem1.class,"../../../temp"));
		 * 
		 * 
		 */
		String dir = "D:\\weiwork\\ContractSystem\\src\\templates";
		try {
			cfg.setDirectoryForTemplateLoading(new File(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		//cfg.setEncoding(new Locale("cn"), "UTF-8");// 这个有问题
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test2();
	}

	public static void test2() {
		try {
			// 定义模板
			// Template template = cfg.getTemplate("testTable.ftl");
			// Template template = cfg.getTemplate("testTableDIV.ftl");
//			Template template = cfg.getTemplate("valueOutByHtml.ftl");
			 Template template = cfg.getTemplate("valueOutByDiv.ftl");
			// 定义数据
			Map root = new HashMap();
			List list = new ArrayList();
			for (int i = 1; i < 11; i++) {
				TestMapVo vo = new TestMapVo();
				vo.setCn1("cn_" + i + "1");
				vo.setCn2("cn_" + i + "2");
				vo.setCn3("cn_" + i + "3");
				vo.setCn4("cn_" + i + "4");
				vo.setCn5("cn_" + i + "5");
				vo.setCn6("cn_" + i + "6");
				list.add(vo);
			}
			String[] titles = null;
			String[] fieldNames = null;
			titles = new String[] { "中文1", "中文2", "中文3", "中文4", "中文5", "中文6" };
			fieldNames = new String[] { "cn1", "cn2", "cn3", "cn4", "cn5", "cn6" };
			root.put("titles", titles);// 表头字段 中文
			root.put("fieldNames", fieldNames);// 表头字段对应的英文
			root.put("list", list);// 数据信息
			// 定义输出到哪里
			Writer out = new FileWriter("D:\\project_log\\freemarker\\testTable.html");
			// 解释模板，并输出
			template.process(root, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// String aa = TestFreemarker.class.getResource("/").getPath();
		// System.out.println(aa);
	}
}

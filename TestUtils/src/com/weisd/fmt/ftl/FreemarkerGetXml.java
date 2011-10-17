package com.weisd.fmt.ftl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.junbao.hf.utils.common.DateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-1 下午6:09:42
 * @version v1.0
 */
public class FreemarkerGetXml {

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
//		String dir = "D:\\weiwork\\ContractSystem\\src\\templates";
//		String dir = "E:\\gaeProject\\TestUtils\\src\\com\\weisd\\mq\\ftl";
		String dir = "D:\\junbao_newpro\\TestUtils\\src\\com\\weisd\\mq\\ftl";
		try {
			cfg.setDirectoryForTemplateLoading(new File(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		// cfg.setEncoding(new Locale("cn"), "UTF-8");// 这个有问题
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String ss = sendXML();
	}
	
	public static String search(int i) {
		try {
			Template template = cfg.getTemplate("order8002.ftl");
			// 定义数据
			Map<String,String> paramMap = new HashMap<String,String>();
			
			//String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap);// 加入map到模板中
			
			
			
			
			
			String comm = "8002";
			String version = "1.0";
			String onlineid = "106998";
			String agentid = "weisd_mq";
			String ordersource = "1";
			String orderid = "";
			String mobilenum = "";
			String chargeamount = "";
			String payamount = "";
			String ordertime = "";
			String mark = "";
			
			
			
			
			//ordertime = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
			//mobilenum = "1520136" + "0" + i;
			
			orderid = "MQ20110902161205104";
			
			paramMap.put("comm", comm);
			paramMap.put("version", version);
			paramMap.put("onlineid", onlineid);
			paramMap.put("agentid", agentid);
			paramMap.put("ordersource", ordersource);
			paramMap.put("orderid", orderid);
			paramMap.put("mobilenum", mobilenum);
			paramMap.put("chargeamount", chargeamount);
			paramMap.put("payamount", payamount);
			paramMap.put("ordertime", ordertime);
			paramMap.put("mark", mark + i);
			
			
			StringWriter out = new StringWriter();
			template.process(paramMap, out);
			
			return out.toString();
			
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 下单
	 * @param i
	 * @return
	 */
	public static String sendXML(int i) {
		try {
			Template template = cfg.getTemplate("order8001_2.ftl");
			// 定义数据
			Map<String,String> paramMap = new HashMap<String,String>();
			
			//String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap);// 加入map到模板中
			
			
			
			
			
			String comm = "8001";
			String version = "1.0";
			
			String agentid = "weisd_mq";
			agentid = "V0000999";
			String ordersource = "2";
			String orderid = "8001";
			String mobilenum = "8001";
			String chargeamount = "30.00";
			
			String onlineid = "106998";
			String payamount = "30.00";
			String ordertime = "";
			String mark = "weisd测试";
			
			
			
			ordertime = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
//			chargeamount = "30.00";
//			onlineid = "107620";
//			payamount = "39";
//			mobilenum = "1520138" + "0" + i;
			
//			chargeamount = "50.00";
//			onlineid = "107621";
//			payamount = "49.50";
//			mobilenum = "1520138" + "0" + i;
			
//			chargeamount = "30";
//			onlineid = "107624";
////			onlineid = "";
//			payamount = "30.30";
////			mobilenum = "1340212" + "0" + i;
//			mobilenum = "1340212" + i;
//			
			chargeamount = "30";
			onlineid = "107620";
//			onlineid = "";
			payamount = "29.70";
//			mobilenum = "1340212" + "0" + i;
			mobilenum = "1520138" + i;
			
			
//			chargeamount = "20";
//			onlineid = "107619";
////			onlineid = "";
//			payamount = "19.8";
////			mobilenum = "1340212" + "0" + i;
//			mobilenum = "1520138" + i;
			
//			chargeamount = "30.00";//联通
//			onlineid = "107643";
//			payamount = "30.30";
//			mobilenum = "1551008" + "0" + i;
			
			
			
//			chargeamount = "30.00";
//			payamount = "30.00";
//			mobilenum = "1583960" + "0" + i;//河南 移动
//			chargeamount = "20.00";
//			payamount = "20.00";
//			mobilenum = "1551004" + "0" + i;//联通
			
			orderid = "MQ" + ordertime+i;
			
			paramMap.put("comm", comm);
			paramMap.put("version", version);
			paramMap.put("onlineid", onlineid);
			paramMap.put("agentid", agentid);
			paramMap.put("ordersource", ordersource);
			paramMap.put("orderid", orderid);
			paramMap.put("mobilenum", mobilenum);
			paramMap.put("chargeamount", chargeamount);
			paramMap.put("payamount", payamount);
			paramMap.put("ordertime", ordertime);
			paramMap.put("mark", mark + i);
			
			
			StringWriter out = new StringWriter();
			template.process(paramMap, out);
			
			return out.toString();
			
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String sendXML() {
		try {
			Template template = cfg.getTemplate("order8001.ftl");
			// 定义数据
			Map<String,String> paramMap = new HashMap<String,String>();
		
			//String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap);// 加入map到模板中
			
			
			
			
			
			String comm = "8001";
			String version = "1.0";
			String onlineid = "106900";
			String agentid = "weisd_mq";
			String ordersource = "1";
			String orderid = "8001";
			String mobilenum = "8001";
			String chargeamount = "10.00";
			String payamount = "10.0";
			String ordertime = "";
			String mark = "weisd测试";
			
			
			
			
			ordertime = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
			String num = DateUtils.getFormatDate(new Date(), "mmss");
			mobilenum = "1520138" + num;
			
			orderid = "MQ" + ordertime;
			
			paramMap.put("comm", comm);
			paramMap.put("version", version);
			paramMap.put("onlineid", onlineid);
			paramMap.put("agentid", agentid);
			paramMap.put("ordersource", ordersource);
			paramMap.put("orderid", orderid);
			paramMap.put("mobilenum", mobilenum);
			paramMap.put("chargeamount", chargeamount);
			paramMap.put("payamount", payamount);
			paramMap.put("ordertime", ordertime);
			paramMap.put("mark", mark + num);
			

			StringWriter out = new StringWriter();
			template.process(paramMap, out);
			
			return out.toString();
			
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}

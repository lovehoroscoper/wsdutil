package test;

import org.apache.log4j.Logger;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-5 下午4:50:50
 */
public class TestTime {

	private static Logger logger = Logger.getLogger(TestTime.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long start = System.currentTimeMillis();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		long s = (end - start)/1000;
		if(s > 2){
			logger.error("------------------匹配URL时间超过限制");
		}

	}

}

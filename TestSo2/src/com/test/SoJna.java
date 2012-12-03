package com.test;

import org.apache.log4j.Logger;

import com.test.face.TestSoInit;

public class SoJna {
	private static Logger logger = Logger.getLogger(SoJna.class);

	public void initial() {
		logger.info("进入-----------initial------");
		try {

			int res = TestSoInit.instance.tsttsInit("/mydata/logs/");

			logger.info("-------------res---------[" + res + "]");

		} catch (Exception e) {
			logger.error("TestSoInit.instance---------", e);
		}
		logger.info("完成设置--------System.setProperty----");

		logger.info("结束-----------initial------");
	}

}

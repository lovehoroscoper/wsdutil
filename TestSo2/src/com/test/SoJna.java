package com.test;

import org.apache.log4j.Logger;

import com.sun.jna.ptr.IntByReference;
import com.test.face.TestSoInit;

public class SoJna {
	private static Logger logger = Logger.getLogger(SoJna.class);

	public void initial() {
		logger.info("进入-----------initial------");
		try {

			Object res_tsttsInit = TestSoInit.instance.tsttsInit("/mydata/app/yuyin/work/");

			logger.info("-------------res_tsttsInit------[" + res_tsttsInit + "]");

			IntByReference ir = new IntByReference();
			Object res_tsttsNewSession = TestSoInit.instance.tsttsNewSession(ir);
			logger.info("-------------ir_tsttsNewSession------[" + ir.getValue() + "]");
			logger.info("-------------res_tsttsNewSession------[" + res_tsttsNewSession + "]");

		} catch (Exception e) {
			logger.error("TestSoInit.instance----Exception-----", e);
		}
		logger.info("完成设置--------System.setProperty----");

		logger.info("结束-----------initial------");
	}

}

package com.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sun.jna.ptr.IntByReference;
import com.test.face.TestSoInit;

public class SoJna {
	private static Logger logger = Logger.getLogger(SoJna.class);

	public void initial(String text) {
		logger.info("进入-----------initial------");
		try {

			Object res_tsttsInit = TestSoInit.instance.tsttsInit("/mydata/app/yuyin/work/");

			logger.info("-------------res_tsttsInit------[" + res_tsttsInit + "]");

			IntByReference ir = new IntByReference();
			Object res_tsttsNewSession = TestSoInit.instance.tsttsNewSession(ir);
			int pSessionId = ir.getValue();
			logger.info("-------------ir_tsttsNewSession------[" + pSessionId + "]");
			logger.info("-------------res_tsttsNewSession------[" + res_tsttsNewSession + "]");

			// snprintf(buf, sizeof(buf), "%f", 0.21);
			String strName = "Speed";
			String pBuf = "0.21";
			int res_tsttsParamSet = TestSoInit.instance.tsttsParamSet(pSessionId, strName, pBuf);
			logger.info("-------------res_tsttsParamSet------[" + res_tsttsParamSet + "]");

			// buf[0] = '\0';
			String buf = "\0" + pBuf;
			TestSoInit.instance.tsttsParamGet(pSessionId, "Speed", buf, buf.length());
			
			logger.info("-------------end ---tsttsParamGet---[tsttsParamGet]");

			String strFile = "WEISD_" + getFormatDate(new Date(), "yyyyMMddHHmmss") + ".wav";
			String pTextBuf = text;
			int res_tsttsTextToAudioFile = TestSoInit.instance.tsttsTextToAudioFile(pSessionId, strFile, pTextBuf, pTextBuf.length());
			logger.info("-------------res_tsttsTextToAudioFile------[" + res_tsttsTextToAudioFile + "]");

			int res_tsttsDelSession = TestSoInit.instance.tsttsDelSession(pSessionId);
			logger.info("-------------res_tsttsDelSession------[" + res_tsttsDelSession + "]");

			int res_tsttsUninit = TestSoInit.instance.tsttsUninit();
			logger.info("-------------res_tsttsUninit------[" + res_tsttsUninit + "]");

		} catch (Exception e) {
			logger.error("TestSoInit.instance----Exception-----", e);
		}
		logger.info("完成设置--------System.setProperty----");

		logger.info("结束-----------initial------");
	}

	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
}

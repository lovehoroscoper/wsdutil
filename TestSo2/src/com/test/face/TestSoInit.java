package com.test.face;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-3 下午6:46:51
 */
public interface TestSoInit extends Library {

	TestSoInit instance = (TestSoInit) Native.loadLibrary("tstts", TestSoInit.class);

	int tsttsInit(String path);

}

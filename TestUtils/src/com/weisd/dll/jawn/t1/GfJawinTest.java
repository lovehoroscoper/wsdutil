package com.weisd.dll.jawn.t1;

/*
 * Created on Dec 22, 2005
 *
 */
import org.jawin.FuncPtr;

import org.jawin.ReturnFlags;

/**
 * @author gf mail to gf@163.com
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GfJawinTest {

	public static void main(String[] args) {

		try {

			FuncPtr msgBox = new FuncPtr("USER32.DLL", "MessageBoxW");

			msgBox.invoke_I(0, "Hello From a DLL", "From Jawin", 0, ReturnFlags.CHECK_NONE);
			

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}

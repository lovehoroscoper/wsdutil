package com.weisd.dll.jawn.t1;

import org.jawin.COMException;
import org.jawin.FuncPtr;
import org.jawin.ReturnFlags;
import org.jawin.io.LittleEndianOutputStream;
import org.jawin.io.NakedByteStream;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-19 下午5:23:45
 */
public class Test1 {

//	public static void main(String[] args) throws Exception {
//		String filePath_library = "E:\\code_sdk\\C++\\wylt.cds";
//		FuncPtr msgBox = null;
//		try {
//			msgBox = new FuncPtr("AntiVC.dll", "LoadCdsFromFile"); // 常量
//			
////			n.setParameter(j++, Type.LONG, "0");
////			n.setParameter(j++, Type.STRING, "" + filePath_img);
////			n.setParameter(j++, Type.PSTRUCT, "" + aa);
////			
//			msgBox.invoke_I(0, "Hello From a DLL", "From Jawin", 0, ReturnFlags.);
//		} catch (COMException e) {
//			// handle exception
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (msgBox != null) {
//				try {
//					msgBox.close();
//				} catch (COMException e) {
//					// handle fatal exception
//					e.printStackTrace();
//					throw e;
//				}
//			}
//		}
//	}
	public static void main(String[] args) throws Exception {
		FuncPtr msgBox = null;
		try {
		  msgBox = new FuncPtr("AntiVC.dll", "LoadCdsFromFile");

		  // create a NakedByteStream for the serialization of Java variables
		  NakedByteStream nbs = new NakedByteStream();

		  // wrap it in a LittleEndianOutputStream
		  LittleEndianOutputStream leos = new LittleEndianOutputStream(nbs);

		  // and then write the Java arguments
		  
////		n.setParameter(j++, Type.LONG, "0");
////		n.setParameter(j++, Type.STRING, "" + filePath_img);
////		n.setParameter(j++, Type.PSTRUCT, "" + aa);
		  String aa = "";
		  String filePath_library = "E:\\code_sdk\\C++\\wylt.cds";
		  leos.writeStringUnicode("0");
		  leos.writeStringUnicode(filePath_library);
		  leos.writeStringUnicode(aa);
		  
//		  leos.writeInt(0);
//		  leos.writeStringUnicode("Generic Hello From a DLL");
//		  leos.writeStringUnicode("From Jawin");
//		  leos.writeInt(0);

		  // call the generic invoke, with the NakedByteStream
		  // and parameters describing how to deserialize the
		  // NakedByteStream byte-array on the native side
//		  byte[] b = msgBox.in.invoke("IGGI:I:", 16, nbs, null, ReturnFlags.CHECK_FALSE);
		  
//		  System.out.println(new String(b));
		  
		} catch (COMException e) {
			e.printStackTrace();
		  // handle exception
		} finally {
		  if (msgBox != null) {
		    try {
		      msgBox.close();
		    } catch (COMException e) {
		      // handle fatal exception
		    }
		  }
		}
	}
}

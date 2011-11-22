package com.weisd.dll.test1;

import java.io.IOException;

import com.jinvoke.JInvoke;
import com.jinvoke.NativeImport;

public class FindFirstFile {

	@NativeImport(library = "AntiVC")
	public static native long LoadCdsFromFile(String lpFileName);

	// @NativeImport(library = "AntiVC")
	// public static native boolean GetVcodeFromFile(long index, String img,
	// StringBuffer sb);

	public static void main(String[] args) throws Exception {
		JInvoke.initialize();
		String filePath_library = "E:\\wylt.cds";
		String filePath_img = "E:\\temp.jpg";
		try {
			RecurseDirectory(filePath_library, filePath_img);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static long RecurseDirectory(String filePath_library, String filePath_img) throws IllegalArgumentException, IOException, IllegalAccessException {

		long l = LoadCdsFromFile(filePath_library);
		// if (1 == l) {
		// StringBuffer sb = new StringBuffer();
		// Long index = new Long("1");
		// boolean b = GetVcodeFromFile(1, filePath_img, sb);
		//
		// System.out.println("");
		// }

		return 1;
	}

}

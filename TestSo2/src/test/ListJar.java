package test;

import java.io.File;


public class ListJar {

	private static StringBuffer sb = new StringBuffer("CLASSPATH=.");
	//private static String p = ":/app/jbcz/lib/";
	private static String p = ":/app/testso/lib/";

	public static void main(String[] args) {

		File file = new File("D:\\junbao_newpro\\TestSo\\src\\lib");
		ListJar.listFile(file);

		System.out.println(sb.toString());
	}

	public static void listFile(File f) {
		if (f.isDirectory()) {
			// System.out.println("1---directory:" + f.getPath() + "--parent:" +
			// f.getParent());
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				listFile(t[i]);
			}
		} else {
			// System.out.println("2---fileName:" + f.getAbsolutePath() +
			// "--parent:" + f.getParent());
			// System.out.println("2---fileName:" + f.getAbsolutePath());
			// System.out.println("2---fileName:" + f.getName());
			String filename = f.getName();

			if (filename.endsWith(".jar")) {
				sb.append(p).append(filename);
			}
		}
	}
}
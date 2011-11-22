package com.weisd.dll.jinvoke;
import com.jinvoke.JInvoke;
import com.jinvoke.NativeImport;

public class HelloWindows2 {
	
	@NativeImport(library="AntiVC",function="LoadCdsFromFile")
	public static native Object LoadCdsFromFile(String path);
	
	public static void main(String[] args){
		JInvoke.initialize();
		String filePath = "E:\\code_sdk\\C++\\wylt.cds";
		Object l = LoadCdsFromFile(filePath);
		
		System.out.println(l);
	}	
}
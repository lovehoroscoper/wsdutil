package com.weisd.dll.jinvoke;
import com.jinvoke.JInvoke;
import com.jinvoke.NativeImport;

public class HelloWindows {
	
	@NativeImport(library="User32",function="MessageBox")
	public static native int showMessage(int hwnd,String text,String caption,int type);
	
	public static void main(String[] args){
		JInvoke.initialize();
		showMessage(0,"Hello welcome","hello-world",0);
	}	
}
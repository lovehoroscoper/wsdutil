package com.weisd.dll.jawn2;

//创建word

import org.jawin.DispatchPtr;

import org.jawin.win32.Ole32;

public class CreateWord {

	public static void main(String[] args) {

		try {

			Ole32.CoInitialize();// 初始化

			DispatchPtr app = new DispatchPtr("Word.Application");// 创建word对象

			app.put("Visible", true); // 使word可见

			DispatchPtr docs = (DispatchPtr) app.get("Documents"); // 获得document对象集合

			DispatchPtr doc = (DispatchPtr) docs.invoke("Add"); // 新增一个文档

			app.invoke("Activate"); // 激活当前文档

			DispatchPtr objTextFont = (DispatchPtr) ((DispatchPtr) doc.get("Content")).get("Font");

			// 取得Font对象

			objTextFont.put("Name", "黑体");

			// 设置字体

			objTextFont.put("Size", "48");

			// 设置字号

			DispatchPtr docSelection = (DispatchPtr) app.get("Selection");

			// 取得Selection对象

			docSelection.invoke("TypeText", "Jawwintesttext!\nJawin测试文本。");

			// 使用TypeText方法添加文本

			doc.invoke("SaveAs", "d:\\jawintest.doc");

			// 保存文档(保存在C盘根目录下)

			doc.invoke("Close");

			// 关闭当前文档，去掉前面的注释符并重新编译后可生效

			app.invoke("Quit");

			// 退出Word，去掉前面的注释符并重新编译后可生效

			Ole32.CoUninitialize(); // 释放对象

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}

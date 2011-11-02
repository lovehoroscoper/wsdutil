package com.weisd.img;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * * @author 董林 Email:happyman_dong@sina.com 版权所有 盗版必究 * @since 2009-8-28 * @version
 * 1.0
 */

public class AutoImage { // 数字字符比特表

	private final String[] NUMERIC = { "011110100001100001101101101101101101101101100001100001011110",// 0
			"001000111000001000001000001000001000001000001000001000111110",// 1
			"011110100001100001000001000010000100001000010001100001111111",// 2
			"011110100001100001000010001100000010000001100001100001011110",// 3
			"000100000100001100010100100100100100111111000100000100001111",// 4
			"111111100000100000101110110001000001000001100001100001011110",// 5
			"001110010001100000100000101111110001100001100001100001011110",// 6
			"111111100010100010000100000100001000001000001000001000001100",// 7
			"011110100001100001100001011110010010100001100001100001011110",// 8
			"011100100010100001100001110011011101000001000001100010011100" };// 9
	// 字框高
	private int intCharHeight = 10;
	// 字框横向间隙
	private int intCharSpaceH = 4;
	// 字框纵向间隙
	private int intCharSpaceY = 1;
	// 字框宽
	private int intCharWidth = 6;
	private int IntImgHeight;
	private BufferedImage img;
	private int intBgColor;
	private int intCharColor;
	private int intImgWith;
	private int intMaxX;
	private int intMaxY;
	private int intMinX;
	private int intMinY;
	// 座标原点
	private Point pOrigin;
	private String strNum;

	/** */
	/** * Constructs * * * @param img * * @throws IOException */
	public AutoImage(BufferedImage img) throws IOException {
		this.img = img;
		init();
	}

	/** */
	/** * 构造函数 * * @param file * 本地文件 * @throws IOException */

	public AutoImage(File file) throws IOException {
		img = ImageIO.read(file);
		init();
	}

	/** */
	/** * 构造函数 * * @param url * 远程文件 * @throws IOException */
	public AutoImage(URL url) throws IOException {
		img = ImageIO.read(url);
		init();
	}

	/** */
	/** * 类初始工作 */
	private void init() {
		// 得到图象的长度和宽度
		intImgWith = img.getWidth();
		IntImgHeight = img.getHeight();
		// 得到图象的背景颜色
		intBgColor = img.getRGB(7, 4);
		// System.out.println(intBgColor);
		// 初始化图象原点座标
		pOrigin = new Point(2, 0);
	}

	/** */
	/** * Method description * */
	private void getBaseInfo() {
		System.out.println(intBgColor + "|" + intCharColor);
		System.out.println(intMinX + "|" + intMinY + "|" + intMaxX + "|" + intMaxY);
	}

	/** */
	/** * 得到字符的左上右下点座标 * * @param intNo * int 第n个字符 * @return int[] */
	private Point[] getCharRange(int intNo) {
		// 左上右下点座标
		Point pTopLeft = new Point(0, 0);
		Point pBottomRight = new Point(0, 0);
		// 左上点
		pTopLeft.x = pOrigin.x + intCharWidth * (intNo - 1) + intCharSpaceH * (intNo - 1);
		pTopLeft.y = pOrigin.y;
		// 右下点
		pBottomRight.x = pOrigin.x + intCharWidth * intNo + intCharSpaceH * (intNo - 1);
		pBottomRight.y = pOrigin.y + intCharHeight - 1;
		return new Point[] { pTopLeft, pBottomRight };
	}

	/** */
	/**
	 * * 与背景颜色比较返回相应的字符 * * @param x * int 横座标 * @param y * int 纵座标 * @return
	 * char 返回字符
	 */
	private char getBit(int x, int y) {
		int intCurtColor;
		intCurtColor = img.getRGB(x, y);
		return (intCurtColor == intBgColor) ? '0' : '1';
	}

	/** */
	/** * 得到第n个字符对应的字符串 * * @param intNo * int 第n个字符 * @return String 代表字符位的串 */
	private String getCharString(int intNo) { // 本字符的左上右下点座标
		Point[] p = getCharRange(intNo);
		Point pTopLeft = p[0];
		Point pBottomRight = p[1]; // 换算边界值
		int intX1, intY1, intX2, intY2;
		intX1 = pTopLeft.x;
		intY1 = pTopLeft.y;
		intX2 = pBottomRight.x;
		intY2 = pBottomRight.y; // 在边界内循环取象素
		int i, j;
		String strChar = "";
		for (i = intY1; i <= intY2; i++) {
			for (j = intX1; j < intX2; j++) {
				strChar = strChar + getBit(j, i);
			}
		}
		return strChar;
	}

	/** */
	/** * 得到第n个字符对应数值 * * @param intNo * int 第n个字符 * @return int 对应数值 */
	public int getNum(int intNo) { // 取得位字符串
		String strChar = getCharString(intNo);
		char[] sc = strChar.toCharArray(); // 在数字中循环比较
		int maxflag = 0;
		int intNum = 0;
		for (int i = 0; i <= 9; i++) {
			char[] tmpchar = NUMERIC[i].toCharArray();
			int flag = 0;
			for (int k = 0; k < tmpchar.length; k++) {
				if (tmpchar[k] == sc[k])
					flag++;
			}
			if (flag > maxflag) {
				maxflag = flag;
				intNum = i;
			}
		}
		return intNum;
	}

	public String getValidatecode(int length) {
		String strNum = "";
		for (int i = 1; i <= length; i++) {
			synchronized (this) {
				strNum += String.valueOf(getNum(i));
			}
		}

		return strNum;
	}
}

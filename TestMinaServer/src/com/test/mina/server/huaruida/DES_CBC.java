package com.test.mina.server.huaruida;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DES_CBC {

	private static final String Algorithm = "DES/ECB/NoPadding";// 定义 加密算法,可用
																// DES,DESede,Blowfish

	public static void main(String[] args) {
		// 主密钥假设为：1234567890ABCDEF1234567890ABCDEF
//		byte[] ImacKey = hexStringToByte("1234567890ABCDEF1234567890ABCDEF");
		byte[] leftKey = hexStringToByte("1234567890ABCDEF");
		byte[] rightKey = hexStringToByte("1234567890ABCDEF");
		
		//MAC的加密密钥的密文为24位（其中 “MAC密钥”为前16位是密文，后8位是密文校验值；
		//前16位解出明文后，对8个数值0做单倍长密钥算法，取结果的前8位与密文校验值比较应该是一致的）。
		
		// 收到的密钥信息报文体假设为：75DC386FC624184D6846EE8B（其中MAC密钥75DC386FC624184D，单倍长校验码：6846EE8B）
		byte[] MACMsg = hexStringToByte("75DC386FC624184D");
		
		byte[] MACCheck = hexStringToByte("6846EE8B");
		
		//前16位解出明文后
		byte[] key = decryptMode(leftKey, MACMsg);// 通过主密钥和MAC密钥进行3DES解密可以得到工作密钥
		
		
		
		key = encryptMode(rightKey, key); // 通过主密钥和MAC密钥进行3DES解密可以得到工作密钥
		key = decryptMode(leftKey, key); // 通过主密钥和MAC密钥进行3DES解密可以得到工作密钥
		System.out.println("3DES解密后，工作密钥为" + toHex(key));// 经过3DES解密之后可以获得工作密钥A878F1F435BBEB10
		
		
		byte[] Check = OneZero(key);// 取前半段与单倍长校验码比较
		boolean isRight = IsCheck(Check, MACCheck);
		System.out.println(isRight);// 校验该密钥是否正确，正确则返回true
		
		
		
		
		// 签到获得密钥成功之后，就可以使用该工作密钥来发送相关业务报文。注意如果链路断开重连一定要重新签到获取密钥。

		// -------------------------以下是报文MAC校验码计算方法
		// 假设参与mac运算的报文校验的相应信息截取下来组成报文207661100000001480766610019960101201104261021198975DC386FC624184D54AFC1F0
//		String block = "207661100000001480766610019960101201104261021198975DC386FC624184D54AFC1F0";
		String block = "207661100000001480766610019960101201104261021198975DC386FC624184D54AFC1F0";
		byte[] mac = getMAC(key, block.getBytes());// 传入运算报文和工作密钥，调用getMAC方法可以获得mac校验码
		System.out.println("计算获得的MAC为：" + toHex(mac));
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * DES解密方法
	 * 
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, "DES");
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * DES加密方法
	 * 
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, "DES");
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
			// 加密成功后返回
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
		// 失败返回null
	}

	/**
	 * 把字节数组按十六进制输出
	 */
	public static final String toHex(byte b[]) {
		char buf[] = new char[b.length * 2];
		int j;
		for (int i = j = 0; i < b.length; i++) {
			int k = b[i];
			buf[j++] = hex[k >>> 4 & 0xf];
			buf[j++] = hex[k & 0xf];
		}

		return new String(buf);
	}

	private static final char hex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 进行单倍长密钥算法
	 */
	public static byte[] OneZero(byte[] des) {
		byte[] pink = hexStringToByte("0000000000000000");
		byte[] Cvalue = encryptMode(des, pink);
		System.out.println("单倍长密钥运算后获得的值为：" + toHex(Cvalue));
		byte[] ch = new byte[pink.length / 2];
		for (int i = 0; i < ch.length; i++) {
			ch[i] = Cvalue[i];
		}
		return ch;
	}

	public static boolean IsCheck(byte[] value, byte[] checkvalue) {
		boolean he = true;
		for (int i = 0; i < value.length; i++) {
			if (value[i] != checkvalue[i]) {
				he = false;
			}
		}
		return he;
	}

	public static byte[] getMAC(byte[] workKey, byte[] block) {
		/**
		 * DES CBC运算参考代码
		 */
		byte[] xor = hexStringToByte("0000000000000000");
		// DESvalue 为DES加密后得到的数据
		byte[] DESvalue = hexStringToByte("0000000000000000");
		for (int i = 0; i < block.length;) {
			// 每次取64bit的数据进行异或
			for (int j = 0; j < xor.length; i++, j++) {
				// 最后一组如果不足64bit补0x00
				if (i >= block.length) {
					xor[j] = (byte) (0x00 ^ xor[j]);
				} else {
					xor[j] = (byte) (block[i] ^ xor[j]);
				}
			}
			// 异或得到内容进行DES加密
			DESvalue = encryptMode(workKey, xor);
			xor = DESvalue;
		}
		byte[] temp = new byte[DESvalue.length / 2];
		for (int k = 0; k < DESvalue.length / 2; k++) {
			temp[k] = DESvalue[k];
		}
		return temp;
	}
}
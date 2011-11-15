//package com.weisd.spring.utils;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.Security;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESKeySpec;
//import javax.crypto.spec.IvParameterSpec;
//
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
//
///**
// * 
// * @author Administrator
// *
// * v1 有个问题：在加密后返回的字符串后 补充一位  依旧成功
// *
// */
//public class SpringPropertiesDbDesUtil2 {
//
//	/**
//	 * 加密函数
//	 * 
//	 * @param data
//	 *            加密数据
//	 * @param key
//	 *            密钥
//	 * @return 返回加密后的数据
//	 */
//	public static byte[] CBCEncrypt(byte[] data, byte[] key, byte[] iv) {
//		try {
//	        Security.addProvider(new com.sun.crypto.provider.SunJCE());
//	        Key key = getKey(strKey.getBytes());
//	        encryptCipher = Cipher.getInstance("DES");
//	        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
//			
//			
//			// 从原始密钥数据创建DESKeySpec对象
//			DESKeySpec dks = new DESKeySpec(key);
//
//			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
//			// 一个SecretKey对象
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			SecretKey secretKey = keyFactory.generateSecret(dks);
//
//			// Cipher对象实际完成加密操作
//			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//			// 若采用NoPadding模式，data长度必须是8的倍数
//			// Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
//
//			// 用密匙初始化Cipher对象
//			IvParameterSpec param = new IvParameterSpec(iv);
//			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);
//
//			// 执行加密操作
//			byte encryptedData[] = cipher.doFinal(data);
//
//			return encryptedData;
//		} catch (Exception e) {
//			System.err.println("DES算法，加密数据出错!");
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//	
////	/**
////	 * 加密函数
////	 * 
////	 * @param data
////	 *            加密数据
////	 * @param key
////	 *            密钥
////	 * @return 返回加密后的数据
////	 */
////	public static byte[] CBCEncrypt(byte[] data, byte[] key, byte[] iv) {
////
////		try {
////			// 从原始密钥数据创建DESKeySpec对象
////			DESKeySpec dks = new DESKeySpec(key);
////
////			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
////			// 一个SecretKey对象
////			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
////			SecretKey secretKey = keyFactory.generateSecret(dks);
////
////			// Cipher对象实际完成加密操作
////			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
////			// 若采用NoPadding模式，data长度必须是8的倍数
////			// Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
////
////			// 用密匙初始化Cipher对象
////			IvParameterSpec param = new IvParameterSpec(iv);
////			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);
////
////			// 执行加密操作
////			byte encryptedData[] = cipher.doFinal(data);
////
////			return encryptedData;
////		} catch (Exception e) {
////			System.err.println("DES算法，加密数据出错!");
////			e.printStackTrace();
////		}
////
////		return null;
////	}
//
//	/**
//	 * 解密函数
//	 * 
//	 * @param data
//	 *            解密数据
//	 * @param key
//	 *            密钥
//	 * @return 返回解密后的数据
//	 */
//	public static byte[] CBCDecrypt(byte[] data, byte[] key, byte[] iv) {
//		try {
//			// 从原始密匙数据创建一个DESKeySpec对象
//			DESKeySpec dks = new DESKeySpec(key);
//
//			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
//			// 一个SecretKey对象
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			SecretKey secretKey = keyFactory.generateSecret(dks);
//
//			// using DES in CBC mode
//			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//			// 若采用NoPadding模式，data长度必须是8的倍数
//			// Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
//
//			// 用密匙初始化Cipher对象
//			IvParameterSpec param = new IvParameterSpec(iv);
//			cipher.init(Cipher.DECRYPT_MODE, secretKey, param);
//
//			// 正式执行解密操作
//			byte decryptedData[] = cipher.doFinal(data);
//
//			return decryptedData;
//		} catch (Exception e) {
//			System.err.println("DES算法，解密出错。");
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	/**
//	 * 
//	 * @param infoStr
//	 *            需要加密的内容
//	 * @param keyStr
//	 *            密钥
//	 * @return 加密后返回的信息
//	 * @throws UnsupportedEncodingException
//	 */
//	public static String getEncodeByInfo(String infoStr, String keyStr) throws UnsupportedEncodingException {
//		byte[] iv = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
//		byte[] bodyData = CBCEncrypt(infoStr.getBytes("UTF-8"), keyStr.getBytes("UTF-8"), iv);
//		BASE64Encoder base64en = new BASE64Encoder();
//		return base64en.encode(bodyData);
//	}
//
//	/**
//	 * 
//	 * 解密
//	 * 
//	 * @param infoStr
//	 *            加密过的信息
//	 * @param keyStr
//	 *            密钥
//	 * @return 返回的原文
//	 * @throws IOException
//	 */
//	public static String getDecodeByEncode(String encodeStr, String keyStr) throws IOException {
//		byte[] iv = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
//		BASE64Decoder base64De = new BASE64Decoder();
//		byte[] byteMi = base64De.decodeBuffer(encodeStr);
//		byte[] old_infoByte = CBCDecrypt(byteMi, keyStr.getBytes("UTF-8"), iv);
//		return new String(old_infoByte, "UTF-8");
//	}
//
//	public static void main(String[] args) throws IOException {
//		String infoStr = "root";
//		String keyStr = "key11223342232";
//		System.out.println("解密前：" + infoStr);
//		String encodeStr = getEncodeByInfo(infoStr, keyStr);
//		System.out.println("加密串：" + encodeStr);
//		String old_str = getDecodeByEncode(encodeStr, keyStr);
//		System.out.println("解密后：" + old_str);
//
//	}
//}

package com.weisd.esales;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * copy by weisd from 文档
 * 
 * @author
 * 
 * 
 * 
 */
public class KeyedDigestMD5 {
	public static byte[] getKeyedDigest(byte[] buffer, byte[] key) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(buffer);
			return md5.digest(key);
		} catch (NoSuchAlgorithmException e) {
			
		}
		return null;
	}

	public static String getKeyedDigest(String strSrc, String key) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes("UTF8"));
			String result = "";
			byte[] temp;
			temp = md5.digest(key.getBytes("UTF8"));
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String m1;
		String m2;
		//d15fe9e2a0cf4fdf8b715dfc6b8d15e2
		String s = "hf1000";//s=hf1000
		String verifystring1 = "agentid=10&source=esales&mobilenum=15024467050&merchantKey=d8b881abe90e4527aedef2540dafd5bfad7e489bcdb14d409dae17fb581d0a55f3229fec0f894b048db7516422328118267a1fd51b9c4bbc8bda5a152a5ad391";
		String verifystring2 = "agentid=10&source=esales&mobilenum=15024467050&merchantKey=d8b881abe90e4527aedef2540dafd5bfad7e489bcdb14d409dae17fb581d0a55f3229fec0f894b048db7516422328118267a1fd51b9c4bbc8bda5a152a5ad391&verifystring=d15fe9e2a0cf4fdf8b715dfc6b8d15e2";
		m1 = KeyedDigestMD5.getKeyedDigest("verifystring1", "");
		m2 = KeyedDigestMD5.getKeyedDigest("verifystring2", "");
		System.out.println("m1:" + m1);
		System.out.println("m2:" + m2);
		
	}

}

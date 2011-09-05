//package com.weisd.ssl;
//
//import java.io.FileInputStream;
//import java.security.KeyStore;
//
//import org.apache.commons.lang.StringUtils;
//
///**
// * @desc 描述：
// *
// * @author weisd E-mail:weisd@junbao.net
// * @version 创建时间：2011-8-23 下午1:03:40
// */
//public class Test2 {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			//从密钥库中读取CA证书
//			String storepass = "123456";//前面设置的密码
//			FileInputStream in = new FileInputStream("D:\\JDK\\jdk1.6.0_10\\weisdkeystore\\chinajavaworld.keystore");
//			KeyStore ks = KeyStore.getInstance("JKS");
//			ks.load(in, storepass.toCharArray());
//			//获取证书
//			java.security.cert.Certificate c1 = ks.getCertificate("chinajavaworld");
//			//BASE64编码
//			System.out.println(c1.getEncoded());//将chinajavaworld.cer内容改为这里输出的内容
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static String encodeBase64(byte data[])   
//	   {   
//	       boolean lineSep = false;   
//	       int sLen = data == null ? 0 : data.length;   
//	       if(sLen == 0)   
//	           return new String("");   
//	       int eLen = (sLen / 3) * 3;   
//	       int cCnt = (sLen - 1) / 3 + 1 << 2;   
//	       int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0);   
//	        char dArr[] = new char[dLen];   
//	        int s = 0;   
//	        int d = 0;   
//	        int cc = 0;   
//	        do  
//	        {   
//	            if(s >= eLen)   
//	                break;   
//	            int i = (data[s++] & 0xff) << 16 | (data[s++] & 0xff) << 8 | data[s++] & 0xff;   
//	            dArr[d++] = CA[i >>> 18 & 0x3f];   
//	            dArr[d++] = CA[i >>> 12 & 0x3f];   
//	            dArr[d++] = CA[i >>> 6 & 0x3f];   
//	            dArr[d++] = CA[i & 0x3f];   
//	            if(lineSep && ++cc == 19 && d < dLen - 2)   
//	            {   
//	                dArr[d++] = '\r';   
//	                dArr[d++] = '\n';   
//	                cc = 0;   
//	            }   
//	        } while(true);   
//	        int left = sLen - eLen;   
//	        if(left > 0)   
//	        {   
//	            int i = (data[eLen] & 0xff) << 10 | (left != 2 ? 0 : (data[sLen - 1] & 0xff) << 2);   
//	            dArr[dLen - 4] = CA[i >> 12];   
//	            dArr[dLen - 3] = CA[i >>> 6 & 0x3f];   
//	            dArr[dLen - 2] = left != 2 ? '=' : CA[i & 0x3f];   
//	            dArr[dLen - 1] = '=';   
//	        }   
//	        return new String(dArr);   
//	    }   
//	       
//	       
//	    public static final String encodeHex(byte bytes[])   
//	    {   
//	        StringBuffer buf = new StringBuffer(bytes.length * 2);   
//	        for(int i = 0; i < bytes.length; i++)   
//	        {   
//	            if((bytes[i] & 0xff) < 16)   
//	                buf.append("0");   
//	            buf.append(Long.toString(bytes[i] & 0xff, 16));   
//	        }   
//	    
//	        return buf.toString();   
//	    }   
//	       
//	       
//	    public static final byte[] decodeHex(String hex)   
//	    {   
//	        char chars[] = hex.toCharArray();   
//	        byte bytes[] = new byte[chars.length / 2];   
//	        int byteCount = 0;   
//	        for(int i = 0; i < chars.length; i += 2)   
//	        {   
//	            int newByte = 0;   
//	            newByte |= hexCharToByte(chars[i]);   
//	            newByte <<= 4;   
//	            newByte |= hexCharToByte(chars[i + 1]);   
//	            bytes[byteCount] = (byte)newByte;   
//	            byteCount++;   
//	        }   
//	    
//	        return bytes;   
//	    }  
//
//}

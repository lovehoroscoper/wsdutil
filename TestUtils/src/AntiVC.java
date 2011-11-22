

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xvolks.jnative.exceptions.NativeException;

public class AntiVC {

	private static final Log log = LogFactory.getLog(AntiVC.class);
	

	/**
	 * 调用VC动态链接库进行解密
	 */ 
	public void init() {
		log.debug("初始化,HashCode为:" + this.hashCode());	
		try {
			System.load("AntiVC.dll");
//			System.load("AntiVC");
			log.info("成功加载加解密动态链接库!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 声明的原生函数Decrypt，用于解密
	 * @param miwen
	 * @param key
	 * @return
	 */
	public native long LoadCdsFromBuffer(String FilePath);

	/**
	 * 声明的原生函数Encrypt，用于加密
	 * 
	 * @param mingwen
	 * @param key
	 * @return
	 */
//	public native String Encrypt(String mingwen, String key);
	
	public String getEncryptPassword(String pass){
//		return this.Encrypt(pass,key);
		return null;
	}

	public static void main(String[] args) throws NativeException, IllegalAccessException {
		String filePath_library = "E:\\wylt.cds";
		String filePath_img = "E:\\temp.jpg";
		AntiVC j = new AntiVC();
		j.init();
		long l = j.LoadCdsFromBuffer(filePath_library);
		
		log.info("成功加载加解密动态链接库!");
	}
}
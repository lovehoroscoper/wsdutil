package test;

import com.godtips.common.UtilString;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-1 下午2:46:22
 */
public class TestBoolean {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		boolean b1 = true;
//		boolean b2 = false;
//		
//		if( !b1 && !b2){
//			System.out.println("true");
//		}else{
//			
//			System.out.println("fase");
//		}
//		
//		System.out.println(!b1 && !b2);
//		System.out.println(!(b1 && !b2));
		
		String code = "1";
		String typedId = "";
		
		if (!UtilString.isEmptyOrNullByTrim(code) && !UtilString.isEmptyOrNullByTrim(typedId)) {
			System.out.println("true");
		}else{
			System.out.println("fase");
		}
	}

}

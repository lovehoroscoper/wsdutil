package test;
import java.sql.Types;

import net.jforum.entities.Banner;
/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-14 上午12:01:55
 * @version v1.0
 */
public class TestNull {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Banner b = new Banner();
		boolean bb = b.isActive();
		System.out.println(bb);
		System.out.println(Types.BOOLEAN);
		
		

	}

}

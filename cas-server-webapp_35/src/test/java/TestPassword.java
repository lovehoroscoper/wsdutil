import org.springframework.security.crypto.password.StandardPasswordEncoder;


/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-21 下午1:58:20
 */
public class TestPassword {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StandardPasswordEncoder t = new StandardPasswordEncoder("weisd");
		String pass = "123456";
		System.out.println(t.encode(pass));
		System.out.println(t.encode(pass));
		System.out.println(t.encode(pass));
//		CentralAuthenticationServiceImpl
//		String encodedPassword = "b476e9a815a2846ea5ccf0f22ea2635b1dd602f5f6de8983dfeb6a94f3bc7cff53e912b669efe4a11";
//		System.out.println(t.matches(pass, encodedPassword));

	}
//	AutowiringSchedulerFactoryBean

}

import java.net.URLDecoder;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-2-20 上午10:07:50
 */
public class Test34 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(
				URLDecoder.decode("https://github.com/login/oauth/authorize?client_id=bb7977b9e7f892c115c3&redirect_uri=https%3A%2F%2Flocalhost%3A8443%2Flogin%3Foauth_provider%3DGitHubProvider&scope=user")
				);

	}

}

import org.codehaus.jackson.JsonNode;
import org.scribe.up.profile.JsonHelper;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-21 下午7:35:28
 */
public class TestJson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String body = "access_token=FE04222222222CCE2&expires_in=7776000";
		String body = "callback( {\"client_id\":\"YOUR_APPID\",\"openid\":\"YOUR_OPENID\"} );";
		if (body.indexOf("callback") > -1) {
			int lpos = body.indexOf("(");
			int rpos = body.lastIndexOf(")");
			body = body.substring(lpos + 1, rpos);
		}
//		JsonNode json = JsonHelper.getFirstNode(body);
		JsonNode json = JsonHelper.getFirstNode(body);
		
		System.out.println();
	}

}

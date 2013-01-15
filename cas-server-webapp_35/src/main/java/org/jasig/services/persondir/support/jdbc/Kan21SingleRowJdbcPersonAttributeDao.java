package org.jasig.services.persondir.support.jdbc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.gonetbar.ssa.cas.exception.LoginTypeException;
import org.gonetbar.ssa.constant.UserLoginType;
import org.gonetbar.ssa.entity.ThirdProvider;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
import org.gonetbar.ssa.util.CheckUserLoginType;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.NamedPersonImpl;
import org.springframework.dao.support.DataAccessUtils;

import com.godtips.common.UtilString;

public class Kan21SingleRowJdbcPersonAttributeDao extends SingleRowJdbcPersonAttributeDao {

	public Kan21SingleRowJdbcPersonAttributeDao(DataSource ds, String sql) {
		super(ds, sql);
	}

	@Override
	public IPersonAttributes getPerson(String p_uid) {
		Validate.notNull(p_uid, "uid may not be null.");
		// weisd 第三方登录很可能是其他的ID
		String loginType = CheckUserLoginType.getLoginTypeByUid(p_uid);
		String username = "";
		if (UserLoginType.LOGIN_TYPE_LOCAL.equals(loginType)) {
			// 是本地用户
			username = p_uid;
		} else if (UserLoginType.LOGIN_TYPE_OAUTH.equals(loginType)) {
			// 是第三方用户
			String profileType = CheckUserLoginType.getProviderTypeByUid(p_uid);
			ThirdProvider provider = ssaUserService.findProviderIdByType(profileType);
			if (null != provider && !UtilString.isEmptyOrNullByTrim(profileType) && !UtilString.isEmptyOrNullByTrim(provider.getProviderId())) {
				UserProviderInfoVo thirdUser = ssaUserService.findUserByProviderId(provider.getProviderId(), CheckUserLoginType.getThirdUserIdTypeByUid(p_uid));
				if (null != thirdUser) {
					username = thirdUser.getUsername();
				}
			}
		}
		if (!UserLoginType.LOGIN_TYPE_LOCAL.equals(CheckUserLoginType.getLoginTypeByUid(username))) {
			throw new LoginTypeException("非法或无效的登录UID");
		}

		// Generate the seed map for the uid
		final Map<String, List<Object>> seed = this.toSeedMap(username);

		// Run the query using the seed
		final Set<IPersonAttributes> people = this.getPeopleWithMultivaluedAttributes(seed);

		// Ensure a single result is returned
		IPersonAttributes person = (IPersonAttributes) DataAccessUtils.singleResult(people);
		if (person == null) {
			return null;
		}

		// Force set the name of the returned IPersonAttributes if it isn't
		// provided in the return object
		if (person.getName() == null) {
			//person = new NamedPersonImpl(p_uid, person.getAttributes());
			//weisd 
			person = new NamedPersonImpl(username, person.getAttributes());
		}
		return person;

	}

	private SsaUserService ssaUserService;

	public final void setSsaUserService(SsaUserService ssaUserService) {
		this.ssaUserService = ssaUserService;
	}

}
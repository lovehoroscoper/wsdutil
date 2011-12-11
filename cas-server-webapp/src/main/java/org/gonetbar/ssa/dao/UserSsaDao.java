package org.gonetbar.ssa.dao;

import java.util.List;

import org.gonetbar.ssa.entity.UserInfoVo;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:35:37
 * @version v1.0
 */
public interface UserSsaDao {

	public List<UserDetails> loadUsersByUsername(String username);

	public UserInfoVo findUserByVo(UserInfoVo findVo);

}

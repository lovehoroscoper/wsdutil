package com.jeecms.core.manager.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.BadCredentialsException;
import com.jeecms.common.security.UsernameNotFoundException;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.core.dao.UnifiedUserDao;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.UnifiedUserMng;

@Service
@Transactional
public class UnifiedUserMngImpl implements UnifiedUserMng {
	public UnifiedUser passwordForgotten(Integer userId, EmailSender email,
			MessageTemplate tpl) {
		UnifiedUser user = findById(userId);
		String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
		user.setResetKey(uuid);
		String resetPwd = RandomStringUtils.randomNumeric(10);
		user.setResetPwd(resetPwd);
		senderEmail(user.getId(), user.getUsername(), user.getEmail(), user
				.getResetKey(), user.getResetPwd(), email, tpl);
		return user;
	}

	private void senderEmail(final Integer uid, final String username,
			final String to, final String resetKey, final String resetPwd,
			final EmailSender email, final MessageTemplate tpl) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(email.getHost());
		sender.setUsername(email.getUsername());
		sender.setPassword(email.getPassword());
		sender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper msg = new MimeMessageHelper(mimeMessage,
						false, email.getEncoding());
				msg.setSubject(tpl.getSubject());
				msg.setTo(to);
				msg.setFrom(email.getUsername(), email.getPersonal());
				String text = tpl.getText();
				text = StringUtils.replace(text, "${uid}", String.valueOf(uid));
				text = StringUtils.replace(text, "${username}", username);
				text = StringUtils.replace(text, "${resetKey}", resetKey);
				text = StringUtils.replace(text, "${resetPwd}", resetPwd);
				msg.setText(text);
			}
		});
	}

	public UnifiedUser resetPassword(Integer userId) {
		UnifiedUser user = findById(userId);
		user.setPassword(pwdEncoder.encodePassword(user.getResetPwd()));
		user.setResetKey(null);
		user.setResetPwd(null);
		return user;
	}

	public UnifiedUser login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException {
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("username not found: "
					+ username);
		}
		if (!pwdEncoder.isPasswordValid(user.getPassword(), password)) {
			throw new BadCredentialsException("password invalid");
		}
		updateLoginInfo(user.getId(), ip);
		return user;
	}

	public void updateLoginInfo(Integer userId, String ip) {
		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = findById(userId);

		user.setLoginCount(user.getLoginCount() + 1);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
	}

	public boolean usernameExist(String username) {
		return getByUsername(username) != null;
	}

	public boolean emailExist(String email) {
		return dao.countByEmail(email) > 0;
	}

	public UnifiedUser getByUsername(String username) {
		return dao.getByUsername(username);
	}

	public List<UnifiedUser> getByEmail(String email) {
		return dao.getByEmail(email);
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public UnifiedUser findById(Integer id) {
		UnifiedUser entity = dao.findById(id);
		return entity;
	}

	public UnifiedUser save(String username, String email, String password,
			String ip) {
		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = new UnifiedUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(pwdEncoder.encodePassword(password));
		user.setRegisterIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
		user.setLoginCount(0);
		dao.save(user);
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(Integer, String, String)
	 */
	public UnifiedUser update(Integer id, String password, String email) {
		UnifiedUser user = findById(id);
		if (!StringUtils.isBlank(email)) {
			user.setEmail(email);
		} else {
			user.setEmail(null);
		}
		if (!StringUtils.isBlank(password)) {
			user.setPassword(pwdEncoder.encodePassword(password));
		}
		return user;
	}

	public boolean isPasswordValid(Integer id, String password) {
		UnifiedUser user = findById(id);
		return pwdEncoder.isPasswordValid(user.getPassword(), password);
	}

	public UnifiedUser deleteById(Integer id) {
		UnifiedUser bean = dao.deleteById(id);
		return bean;
	}

	public UnifiedUser[] deleteByIds(Integer[] ids) {
		UnifiedUser[] beans = new UnifiedUser[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private PwdEncoder pwdEncoder;
	private UnifiedUserDao dao;

	@Autowired
	public void setPwdEncoder(PwdEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}

	@Autowired
	public void setDao(UnifiedUserDao dao) {
		this.dao = dao;
	}
}
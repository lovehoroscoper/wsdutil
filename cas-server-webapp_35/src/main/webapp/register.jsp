<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test</title>
<link rel="stylesheet" id="wp-admin-css" href="static/css/register.css" type="text/css" media="all">
<script type="text/javascript" src="static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	jQuery(function($) {
		jQuery("#rememberme").attr("checked", true);
		function tipWord(jQueryRule, defaultWord, preColor, afterColor) {
			jQuery(jQueryRule).attr("flag", 1);
			jQuery(jQueryRule).css("color", preColor);
			jQuery(jQueryRule).val(defaultWord);
			jQuery(jQueryRule).focus(
					function() {
						if (jQuery(this).attr("flag") == null
								|| jQuery(this).attr("flag") == 1) {
							jQuery(this).attr("flag", 2);
							jQuery(this).css("color", afterColor);
							jQuery(this).val("");
						}
					});
			jQuery(jQueryRule).blur(
					function() {
						if (jQuery(this).val() == ""
								&& jQuery(this).attr("flag") == 2) {
							jQuery(this).attr("flag", 1);
							jQuery(this).css("color", preColor);
							jQuery(this).val(defaultWord);
						}
					});
		};
		tipWord("#registerform #user_email", "请输入你的有效邮箱,开通后不能修改", "#AAA", "#222");
		jQuery("#registerform #user_email").attr("placeholder", "请输入你的有效邮箱");
		tipWord("#registerform #user_pwd1", "123456", "#AAA", "#222");
		tipWord("#registerform #user_pwd2", "123456", "#AAA", "#222");
	});
</script>
</head>
<body class="login">
	<div id="login">
		<h1>
			<a href="#" title="简单分享新鲜笑料">蛋花儿</a>
		</h1>
		<p class="message register">在这个站点注册</p>

		<form name="registerform" id="registerform" action="#" method="post">
			<p>
				<label for="user_email">电子邮件<br> <input placeholder="请输入你的有效邮箱" style="color: rgb(170, 170, 170);" flag="1" name="user_email"
					id="user_email" class="input" value="请输入你的有效邮箱" size="25" tabindex="20" type="email"></label>
			</p>
			<p>
				<label>密码(至少6位)<br> <input style="color: rgb(170, 170, 170);" flag="1" id="user_pwd1" class="input" tabindex="21" size="25" value=""
					name="user_pass" type="password">
				</label>
			</p>
			<p>
				<label>重复密码<br> <input style="color: rgb(170, 170, 170);" flag="1" id="user_pwd2" class="input" tabindex="22" size="25" value=""
					name="user_pass2" type="password">
				</label>
			</p>
			<p id="reg_passmail"></p>
			<br class="clear"> <input name="redirect_to" value="" type="hidden">
			<p class="submit">
				<input name="wp-submit" id="wp-submit" class="button-primary" value="注册" tabindex="100" type="submit">
			</p>
		</form>
		<p id="nav">
			<a href="#">登录</a> | <a href="#" title="找回密码">忘记密码？</a>
		</p>
		<p id="backtoblog">
			<a href="#" title="不知道自己在哪？">← 回到 蛋花儿</a>
		</p>
	</div>
	<div class="clear"></div>
</body>
</html>
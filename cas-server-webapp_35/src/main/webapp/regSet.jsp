<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>注册花瓣帐号</title>
<meta name="keywords" content="数据">
<meta name="description" content="晒晒自己想要的东西">
<link rel="stylesheet" href="css/reg_set.css">
</head>
<body class="">

	<!-- page -->
	<div id="page">

		<a id="login_return"
			href="http://openapi.qzone.qq.com/oauth/show?which=Login&amp;client_id=100240394&amp;response_type=code&amp;redirect_uri=http%3A%2F%2Fhuaban.com%2Foauth%2Fcallback%2F&amp;scope=get_user_info%2Cadd_topic%2Cget_info%2Cget_fanslist%2Cget_idolist%2Cadd_idol%2Ccheck_page_fans">
			返回 
		</a>
		<div id="signup">
			<a href="http://huaban.com/"><h1 id="login_logo"></h1></a>
			<div class="login_bar"></div>
			<h3>欢迎你！缘来等你，接下来只要简单设置以下信息就可以了</h3>
			<div class="avatar">
				<%--
				<img id="current_avatar" src="images/regset/default_buddy_icon.jpg">
				 --%>
				<img id="current_avatar" src="images/regset/default_buddy_icon_2.jpg">
			</div>
			
			<div class="signup_form">
				<form action="/signup/" method="post" class="Form FancyForm AuthForm">
					<ul>
						<li><div class="input">
								<input id="id_username" name="username" value="缘来等你" type="text"><label style="display: none;">昵称</label><span class="fff"></span>
								<div id="username_msgr" class="msgr left-arrow">
									<span class="txt"></span><span class="arrow">◣</span><span class="arrow-mask"></span>
								</div>
							</div></li>
						<li><div class="input">
								<input id="id_email" name="email" type="text"><label>登陆邮箱地址</label><span class="fff"></span>
								<div id="email_msgr" class="msgr left-arrow">
									<span class="txt"></span><span class="arrow">◣</span><span class="arrow-mask"></span>
								</div>
							</div></li>
						<li><div class="input">
								<input id="id_password" name="password" type="password"><label>密码</label><span class="fff"></span>
								<div id="password_msgr" class="msgr left-arrow">
									<span class="txt"></span><span class="arrow">◣</span><span class="arrow-mask"></span>
								</div>
							</div></li>
					</ul>
					<div class="non_inputs">
						<a id="submit" href="#" onclick="return false;" class="btn btn18 rbtn"><strong> 注册</strong><span></span></a>
					</div>
				</form>
			</div>
			
		</div>
	</div>
	<!-- // page -->

	<div class="clear"></div>
	<div id="page_overlay" style="display: none;" class="overlay"></div>
	<a id="elevator" href="#" onclick="return false;" class="Indicator off btn wbtn"><strong> 回到顶部</strong><span></span></a>

</body>
</html>
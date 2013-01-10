<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>登录系统</title>
<meta name="keywords" content="">
<meta name="description" content="">
<link rel="stylesheet" href="<c:url value="/css/login.css"/>">
</head>
<body>
	<!-- page -->
	<div id="page">
		<a id="login_return" href=""> 返回 </a>
		<div id="signup">
			<a href="#"><h1 id="login_logo"></h1></a>
			<div class="login_bar"></div>
			<h3>欢迎您！</h3>
			<div class="signup_form" style="padding-left: 145px;">
				<table cellspacing="8" cellpadding="4" id="tb_third">
					<tbody>
						<tr>
							<td id="td_third">
								<a href="oauth20/github/initlogin.do"><img src="<c:url value="/images/oauth/github/third_github.png"/>"></a> 
								<a href="oauth20/weibo/initlogin.do"><img src="<c:url value="/images/oauth/weibo/third_weibo.png"/>"></a> 
								<a href="oauth20/qq/initlogin.do"><img src="<c:url value="/images/oauth/qq/third_qq.png"/>"></a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- // page -->
	<div class="clear"></div>

</body>
</html>
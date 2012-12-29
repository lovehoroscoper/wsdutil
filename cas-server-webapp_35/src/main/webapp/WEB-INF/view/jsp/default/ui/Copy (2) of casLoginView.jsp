<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
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
		<a id="login_return" href="">
			返回 
		</a>
		<div id="signup">
			<a href="#"><h1 id="login_logo"></h1></a>
			<div class="login_bar"></div>
			<h3>欢迎您！</h3>
			<div class="signup_form" style="padding-left:180px;">
				<form:form method="post" id="fm1" cssClass="Form FancyForm AuthForm" commandName="${commandName}" htmlEscape="true">
					<input type="hidden" name="lt" value="${loginTicket}" />
					<input type="hidden" name="execution" value="${flowExecutionKey}" />
					<input type="hidden" name="_eventId" value="submit" />
					<ul>
						<li>
							<div class="input">
								<form:input id="username" accesskey="${userNameAccessKey}" path="username" htmlEscape="true" />
								<label>登陆邮箱地址</label>
								<span class="fff"></span>
								<div id="email_msgr" class="msgr left-arrow">
									<span class="txt"></span>
									<span class="arrow">◣</span>
									<span class="arrow-mask"></span>
								</div>
							</div>
						</li>
						<li>
							<div class="input">
								<form:password id="password" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true"/>
								<label>密码</label>
								<span class="fff"></span>
								<div id="password_msgr" class="msgr left-arrow">
									<span class="txt"></span>
									<span class="arrow">◣</span>
									<span class="arrow-mask"></span>
								</div>
							</div>
						</li>
					</ul>
					<div class="non_inputs">
						<a id="submitBtn" href="javascript:void(0);" onclick="return false;" class="btn btn18 rbtn">
							<strong> 登录</strong>
							<span></span>
						</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a id="resetBtn" href="javascript:void(0);" class="btn btn18 wbtn" >
							<strong> 重置</strong>
							<span></span>
						</a>
					</div>
				 </form:form>
			</div>
		</div>
	</div>
	

<a href="${WeiboProviderUrl}">Authenticate with Weibo</a><br /> 
<br />
<a href="${GitHubProviderUrl}">Authenticate with GitHub</a><br />
<br />

	
	<!-- // page -->
	<div class="clear"></div>
	<script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/js/login.js" />"></script>
</body>
</html>
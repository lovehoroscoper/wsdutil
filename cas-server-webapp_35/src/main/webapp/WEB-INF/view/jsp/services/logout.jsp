<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>注销认证中心·······看看而已</title>
	<link href="<c:url value="/css/logout.css"/>" rel="stylesheet" type="text/css" />
</head>
<body>
	<header class="header">
		<div class="links">
			<a href="<c:url value="/"/>">首页</a>
			<a href="http://help.163.com/special/007525G0/163mail_index.html?b65abh1" target="_blank">帮助</a>
		</div>
	</header>

	<section class="info">
		<div id="msg" class="success">
			<h2>注销成功</h2>
			<p>您已成功退出认证中心</p>
		</div>
		<a class="relogin" href="<c:url value="/login"/>" id="oRelogin">重新登录</a>
	</section>

	<section style="background-image: url(&quot;<c:url value="/images/logout/bg_spring.png"/>&quot;);" class="sectionShow" id="themeSpring">
		<div style="position: relative" class="pic">
			<img alt="@@" src="<c:url value="/images/logout/spring.jpg"/>">
		</div>
	</section>

	<footer class="footer">
		<div class="wraper">
			<%--
				<a href="#" target="_blank">关于</a>
				<a href="#" target="_blank">官方博客</a>
				<a href="#" target="_blank">客户服务</a>
				<a href="#" target="_blank">隐私政策</a>| 
			--%>
			版权所有 &copy; 2013-2013
		</div>
	</footer>
</body>
</html>
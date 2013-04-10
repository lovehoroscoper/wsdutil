<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.jasig.cas.web.support.WebUtils"%>
<%@ page import="org.springframework.security.web.WebAttributes"%>
<!DOCTYPE html>
<html>
<head>
	<title>您的访问被拒绝</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta http-equiv="Content-Style-Type" content="text/css">
	<link href="<c:url value="/css/authfail.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="overlay" id="overlay"> 
		<div id="result" class="wrap">
			<div id="ajaxWrap">
				<div id="wrapper">
					<div id="bg-top"></div>
					<div id="contentWrap">
						<div id="content">
							
							<!-- BEGIN LEFT COLUMN -->
							<div id="leftColumn">
								
								<%--
								<!-- YOUR LOGO GOES HERE -->
								<a href="index.html">
									<img id="logo" src="images/logo.png" alt="Logo Sample" width="222" height="100"/>
								</a>
								 --%>
								<div class="errors" id="msg">
									<h2 style="font-size: 22px;color: #BB0000;">您所做&nbsp;≠&nbsp;我所想</h2>
								</div>
								
								<!-- BEGIN MENU -->
								<div id="nav">
									<!-- 
									<span>menu</span>
									 -->
									<ul>
										<li class="home"><a href="<c:url value="/"/>">Home</a></li>
										<li class="about"><a href="javascript:void(0);" title="暂时无法跳转,请您见谅!!!"><s>About Us</s></a></li>
										<li class="contact"><a href="javascript:void(0);" title="暂时无法跳转,请您见谅!!!"><s>Contact Us</s></a></li>
									</ul>
								</div><!-- end div #nav -->
			
								<div class="" id="errorcode">
									<!-- ERROR CODE HERE -->
									<h1>NO<span>You can't</span></h1>
								</div>

							</div><!-- end div #leftColumn -->
							<!-- END LEFT COLUMN -->
							
							<!-- BEGIN RIGHT COLUMN -->
							<div id="rightColumn">
								<h3>访问被拒绝!!!</h3>
								<h3>Access Denied!!!</h3>
								<br>
								<%
								       // Look for details of authorization failure in well-known request attributes.
								       final String[] keys = new String[] {WebUtils.CAS_ACCESS_DENIED_REASON, WebAttributes.AUTHENTICATION_EXCEPTION};
								       Object detail = null;
								       for (String key : keys) {
								           detail = request.getAttribute(key);
								           if (detail == null) {
								               detail = request.getSession().getAttribute(key);
								               request.getSession().removeAttribute(key);
								           }
								           if (detail != null) {
								               break;
								           }
								       }
								       if (detail instanceof String) {
								           request.setAttribute("messageKey", detail);
								       } else if (detail instanceof Exception) {
								           final Exception cause = (Exception) detail;
								           //final String message = String.format("%s::%s", cause.getClass().getSimpleName(), cause.getMessage());
								           final String message = String.format("<%s>", cause.getMessage());
								           request.setAttribute("message", message);
								       }
								%>
								<div class="errors" id="errormsg">
									<c:choose>
									    <c:when test="${not empty messageKey}">
									        <p><spring:message code="${messageKey}" /></p>
									    </c:when>
									    <c:when test="${not empty message}">
									        <p><c:out value="${message}" escapeXml="true" /></p>
									    </c:when>
									</c:choose>
								</div>

								<%--
								<p>务必直接放弃,找谁都没用.</p>
								 --%>
								<br>
								<h4 class="regular"><strong><s>其实. 我们建议...</s></strong></h4>
								<ol>
									<li><span><s>质问美国总统奥巴马,让其务必马上立刻解决.</s></span></li>
									<li><span><s>到户外运动运动.</s></span></li>
									<li><span><s>拔掉网线,关闭电脑.</s></span></li>
									<li><span><s>以上方法还是不行,您只能...</s></span></li>
								</ol>
								<%-- 
									<!-- BEGIN SEARCH FORM - EDIT YOUR DOMAIN BELOW -->
									<form method="get" action="http://www.google.com/search">
										<div>
											<input type="text" name="search" maxlength="255" value="search...">
											<!--[if IE 6]><input type="submit" value="Go"><![endif]-->
											<!--[if !IE 6]><!--><input type="submit" value=""><!--<![endif]-->
											<input type="hidden" name="sitesearch" value="YOUR-DOMAIN.com">
										</div>
									</form>
									<!-- END SEARCH FORM -->
								 --%>
								<a id="close" href="<c:url value="/"/>">返回·主页</a>
			
								
							</div><!-- end div #rightColumn -->
							<!-- END RIGHT COLUMN -->
							
							<div class="clear"></div>
						</div><!-- end div #content -->
					</div><!-- end div #contentWrap -->
					<div id="bg-bottom"></div>
				</div><!-- end div #wrapper -->
			</div><!-- end div #ajaxWrap -->
		</div>
	</div>
</body>
</html>
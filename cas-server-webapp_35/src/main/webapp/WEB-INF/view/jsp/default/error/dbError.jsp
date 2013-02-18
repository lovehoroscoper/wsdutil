<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <title>Project Fedena - Could not find page</title>
	<style type="text/css">
		body { 
			background: #fff url(<c:url value="/images/error/bg.jpg"/>) repeat;
		}
		div.dialog {
			background: transparent url(<c:url value="/images/error/page-not-found.png"/>) no-repeat;
			height: 306px;
			margin: 50px auto;
			width: 485px;
		}
	</style>
</head>
<body>
  <!-- This file lives in public/404.html -->
  <div class="dialog">
    <!--h1>The page you were looking for doesn't exist.</h1>
    <p>You may have mistyped the address or the page may have moved.</p-->
  </div>
</body>
</html>
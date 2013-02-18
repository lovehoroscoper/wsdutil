<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>Could not find page -- error</title>
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
	<div class="dialog">
	</div>
</body>
</html>
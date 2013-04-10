<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>404 - 找不到的界面</title>
		<link rel="stylesheet" href="<c:url value="/css/error/404/201302V1/stylesheet.css"/>">
		<link rel="stylesheet" href="<c:url value="/css/error/404/201302V1/blue.css"/>">
		<%--
		<!-- Import google jquery -->
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script type="text/javascript">
		google.load("jquery", "1.3.1");
		google.setOnLoadCallback(function() {
			//---------------- Show and hide Colorchanger ----------------//
			$("#colors").hide();
			// Show colors when #showChanger clicked
			$("a#showChanger").click(function() {
				$("#colors").slideToggle("slow");
			});
		});
		</script> 
		 --%>
		 <style type="text/css">
			#text p {
			    color: #666666;
			}
			#text a {
			    color: #2AA4CE;
			}
			.unavailable{
				color: #ACACAC;
			}
		 </style>
	</head>
<body>
	<%--
	<!-- Colorchanger div -->
	<div id="colorchanger">
		<div id="colors">
			<!-- Blue -->
			<a class="colorbox colorblue" href="indexca00.html?theme=blue" title="Blue Theme"></a>
			
			<!-- Grey -->
			<a class="colorbox colorgrey" href="index0a59.html?theme=grey" title="Grey Theme"></a>
			
			<!-- Red -->
			<a class="colorbox colorred" href="index0e99.html?theme=red" title="Red Theme"></a>
			
			<!-- Brown -->
			<a class="colorbox colorbrown" href="index8e01.html?theme=brown" title="Brown Theme"></a>
			
			<!-- Green -->
			<a class="colorbox colorgreen" href="indexaf91.html?theme=green" title="Green Theme"></a>
		</div>
		<a href="#" id="showChanger"><img src="<c:url value="/images/error/404/201302V1/"/>colortab.png" alt="Change Theme" /></a>
	</div>
	<!-- End colorchanger div -->
	 --%>

	<!-- Warp around everything -->
	<div id="warp">
		
		<!-- Header top -->
		<div id="header_top"></div>
		<!-- End header top -->
		
		<!-- Header -->
		<div id="header">
			<h2>Sorry,这...这...这.就是一个无效的界面!!!</h2>
			<h5>您肯定不是第一个,也决不能可能是最后一个.</h5>
		</div>
		<!-- End Header -->
		
		<!-- The content div -->
		<div id="content">
		
			<!-- text -->
			<div id="text">
				<!-- The info text -->
				<p><b>Sorry, 找不到了,说得再多也没用.</b></p>
                <br />
				<h3 class="unavailable"><s>其实. 我们建议...</s></h3>
				<!-- End info text -->
				
				<!-- Page links -->
				<ul>
					<li><a href="<c:url value="/"/>">&raquo; 返回·首页</a></li>
					<li class="unavailable">&raquo; <s>About</s></li>
					<li class="unavailable">&raquo; <s>Contact</s></li>
				</ul>
				<!-- End page links -->	
			</div>
			<!-- End info text -->
		
			<!-- Book icon -->
			<img id="book" src="<c:url value="/images/error/404/201302V1/"/>/img-01.png" alt="Book iCon" />
			<!-- End Book icon -->
			
			<div style="clear:both;"></div>
		</div>
		<!-- End Content -->
		
		<!-- Footer -->
		<div id="footer">
			<%-- 
			<!-- Twitter -->
			<img src="<c:url value="/images/error/404/201302V1/"/>twitter.png" alt="twitter" id="twitter" />
			
			<!-- 
			<p id="twitter_text">
			<a href="#">@annanta</a> 
			 -->
			
			<!-- Get tweet -->
			<!-- 
			Under Construction Template <a href="http://ow.ly/16GJZa">http://ow.ly/16GJZa</a>			
			</p>
			 -->
			<!-- End get tweet -->
	    	<!-- End Twitter -->
			
			<!-- Search Form -->
			<form action="#" method="post">
			<p id="searchform">
				<input type="submit" name="submit" id="submit" value="HOME" />
			</p>
			</form>
			<!-- End Search form -->
			 --%>
			<div style="clear:both;"></div>
		</div>
		<!-- End Footer -->
		
		<!-- Footer bottom -->
		<div id="footer_bottom"></div>
		<!-- End Footer bottom -->
	
		<div style="clear:both;"></div>

	</div>
	<!-- End Warp around everything -->
	
</body>
</html>
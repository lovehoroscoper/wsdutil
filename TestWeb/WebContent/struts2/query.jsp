<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>栏目列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

</head>
<body>
<%  
          request.setAttribute("req", "request scope");  
           request.getSession().setAttribute("sess", "session scope");  
           request.getSession().getServletContext().setAttribute("app",                 "aplication scope");  
        %> 

	<br />
		<hr>
	el表达式：<br>
	testAttr:${testAttr}<br>
	requestSet:${requestSet}<br>
	requestMap:${requestMap}<br>
	requestMap.req_:${requestMap.req_}<br>
	attrMap:${attrMap}<br>
	attrMap.attr:${attrMap.attr}<br>
	<hr>
	struts2 ongl表达式：<br>
	<s:form action="/test/upLoad_test.do" method="post"
		enctype="multipart/form-data">
		<s:hidden name="t101" value="testAttr"></s:hidden><!-- no -->
		<s:hidden name="t102" value="requestSet"></s:hidden><!-- no -->
		<s:hidden name="t103" value="requestMap"></s:hidden><!-- no -->
		<s:hidden name="t104" value="requestMap.req_"></s:hidden><!-- no -->
		<s:hidden name="t105" value="attrMap"></s:hidden><!-- no -->
		<s:hidden name="t106" value="attrMap.attr"></s:hidden><!-- no -->
		
		<s:hidden name="t201" value="%{testAttr}"></s:hidden><!-- ok -->
		<s:hidden name="t202" value="%{requestSet}"></s:hidden><!-- no -->
		<s:hidden name="t203" value="%{requestMap}"></s:hidden><!-- no -->
		<s:hidden name="t204" value="%{requestMap.req_}"></s:hidden><!-- no -->
		<s:hidden name="t205" value="%{attrMap}"></s:hidden><!-- ok -->
		<s:hidden name="t206" value="%{attrMap.attr}"></s:hidden><!-- ok -->
		
		<s:hidden name="t301" value="%{request.testAttr}"></s:hidden><!-- no -->
		<s:hidden name="t302" value="%{request.requestSet}"></s:hidden><!-- no -->
		<s:hidden name="t303" value="%{request.requestMap}"></s:hidden><!-- no -->
		<s:hidden name="t304" value="%{request.requestMap.req_}"></s:hidden><!-- no -->
		<s:hidden name="t305" value="%{request.attrMap}"></s:hidden><!-- no -->
		<s:hidden name="t306" value="%{request.attrMap.attr}"></s:hidden><!-- no -->
		
		<s:hidden name="t401" value="#testAttr"></s:hidden><!-- no -->
		<s:hidden name="t402" value="#requestSet"></s:hidden><!-- no -->
		<s:hidden name="t403" value="#requestMap"></s:hidden><!-- no -->
		<s:hidden name="t404" value="#requestMap.req_"></s:hidden><!-- no -->
		<s:hidden name="t405" value="#attrMap"></s:hidden><!-- no -->
		<s:hidden name="t406" value="#attrMap.attr"></s:hidden><!-- no -->
		
		
		<s:hidden name="t401" value="#request.testAttr"></s:hidden><!-- no -->
		<s:hidden name="t402" value="#request.requestSet"></s:hidden><!-- no -->
		<s:hidden name="t403" value="#request.requestMap"></s:hidden><!-- no -->
		<s:hidden name="t404" value="#request.requestMap.req_"></s:hidden><!-- no -->
		<s:hidden name="t405" value="#request.attrMap"></s:hidden><!-- no -->
		<s:hidden name="t406" value="#request.attrMap.attr"></s:hidden><!-- no -->
		
		<!-- context -->
		<s:hidden name="t501" value="contextSet"></s:hidden><!-- no -->
		<s:hidden name="t502" value="contextMap"></s:hidden><!-- no -->
		
		<s:hidden name="t601" value="%{contextSet}"></s:hidden><!-- ok -->
		<s:hidden name="t602" value="%{contextMap}"></s:hidden><!-- ok -->
		<s:hidden name="t603" value="%{contextMap.cont}"></s:hidden><!-- no -->
		<s:hidden name="t604" value="%{contextMap[cont]}"></s:hidden><!-- no -->
		
		<s:hidden name="t701" value="#contextSet"></s:hidden><!-- no -->
		<s:hidden name="t702" value="#contextMap"></s:hidden><!-- no -->
		<s:hidden name="t703" value="#contextMap[cont]"></s:hidden><!-- no -->
		<s:hidden name="t704" value="#contextMap.cont"></s:hidden><!-- no -->
		
		
		<s:hidden name="t801" value="objVVV"></s:hidden><!-- no -->
		<s:hidden name="t802" value="%{objVVV}"></s:hidden><!-- ok -->
		<s:hidden name="t803" value="%{objVVV.name}"></s:hidden><!-- no -->
		
		<s:hidden name="t901" value="objVVV333"></s:hidden><!-- no -->
		<s:hidden name="t902" value="%{objVVV333}"></s:hidden><!-- no -->
		<s:hidden name="t903" value="%{objVVV333.name}"></s:hidden><!-- no -->
		
		<s:hidden name="t1001" value="objVVV333"></s:hidden><!-- no -->
		<s:hidden name="t1002" value="%{request.objVVV333}"></s:hidden><!-- no -->
		<s:hidden name="t1003" value="%{request.objVVV333.name}"></s:hidden><!-- no -->
		
		<s:hidden name="t1101" value="#objVVV333"></s:hidden><!-- no -->
		<s:hidden name="t1102" value="#objVVV333.name"></s:hidden><!-- no -->
		
		<s:hidden name="t1201" value="#request.objVVV333"></s:hidden><!-- no -->
		<s:hidden name="t1202" value="#request.objVVV333.name"></s:hidden><!-- no -->
		
		<s:hidden name="contextMap.cont" ></s:hidden><!-- no -->
		<s:hidden name="contextMap.cont" ></s:hidden><!-- no -->
		
		<%--
		<s:hidden name="t1301" value="${requestScope.requestSet}"></s:hidden>
		 --%>
		<s:hidden name="t1401" value="%{#requestScope.requestSet}"></s:hidden><!-- no -->
		<s:hidden name="t1402" value="%{#request.requestSet}"></s:hidden><!-- ok -->
		<s:hidden name="t1402" value="%{request.requestSet}"></s:hidden><!-- no -->
		
		<s:hidden name="t1501" value="%{#request.objVVV333.name}"></s:hidden><!--ok -->
		
		<s:hidden name="t1601" value="%{#contextMap[cont]}"></s:hidden><!-- no -->
		<s:hidden name="t1602" value="%{#contextMap.cont}"></s:hidden><!-- ok -->
		
		<s:hidden name="t1701" value="%{#objVVV[name]}"></s:hidden><!-- no -->
		<s:hidden name="t1702" value="%{#objVVV.name}"></s:hidden><!-- ok -->
		<s:hidden name="t1703" value="%{#request.objVVV.name}"></s:hidden><!-- ok -->
		
	</s:form>
			
		#testAttr:<s:property value="#testAttr"/><br><!-- no -->
		#requestSet:<s:property value="#requestSet"/><br><!-- no -->
		#request.testAttr:<s:property value="#request.testAttr"/><br><!-- ok -->
		#request.requestSet:<s:property value="#request.requestSet"/><br><br><!-- ok -->
		#request.requestMap:<s:property value="#request.requestMap"/><br><br><!-- ok -->
		#request.requestMap.req_:<s:property value="#request.requestMap.req_"/><br><br><!-- ok -->
		#request.attrMap:<s:property value="#request.attrMap"/><br><br><!-- ok -->
		#request.attrMap.attr:<s:property value="#request.attrMap.attr"/><br><br><!-- ok -->
		#contextSet:<s:property value="#contextSet"/><br><!-- ok -->
		#contextMap:<s:property value="#contextMap"/><br><br><!-- ok -->
		#request.contextSet:<s:property value="#request.contextSet"/><br><!-- ok -->
		#request.contextMap:<s:property value="#request.contextMap"/><br><br><!-- ok -->
		#request.contextMap.cont:<s:property value="#request.contextMap.cont"/><br><br><!-- ok -->
		#objVVV:<s:property value="#objVVV"/><br><br><!-- ok -->
		#objVVV.name:<s:property value="#objVVV.name"/><br><br><!-- ok -->
		#objVVV333:<s:property value="#objVVV333"/><br><br><!-- no -->
		#objVVV333.name:<s:property value="#objVVV333.name"/><br><br><!-- no -->
		#request.objVVV333:<s:property value="#request.objVVV333"/><br><br><!-- ok -->
		#request.objVVV333.name:<s:property value="#request.objVVV333.name"/><br><br><!-- ok -->
	
	<hr>
	
	<s:form action="/test/upLoad_test.do" method="post"
		enctype="multipart/form-data">
		<s:hidden name="testId" value="testAttr"></s:hidden>
		<s:hidden name="testId2" value="%{testAttr}"></s:hidden>
		<s:hidden name="testId3" value="#testMap.testAttr2"></s:hidden>
		<s:hidden name="testId4" value="#request.testMap.testAttr2"></s:hidden>
		<s:hidden name="testId5" value="%{testMap.testAttr2}"></s:hidden>
		<s:hidden name="testId6" value="%{request.testMap.testAttr2}"></s:hidden>
		<s:hidden name="testId7" value="%{requestScope.testMap.testAttr2}"></s:hidden>
		
		<s:hidden name="attr_1" value="attrMap"></s:hidden>
		<s:hidden name="attr_2" value="%{attrMap}"></s:hidden>
		<s:hidden name="attr_3" value="%{attrMap.attr}"></s:hidden>
		<s:hidden name="attr_4" value="#attrMap.attr"></s:hidden>
		
		
		<s:hidden name="req_1" value="#req"></s:hidden><br>
		<s:hidden name="req_2" value="#request.req"></s:hidden><br>
		<s:hidden name="req_3" value="%{req}"></s:hidden><br>
		
		#request.req:<s:property value="#request.req"/> <br>
		#testMap.testAttr2:<s:property value="#testMap.testAttr2"/> <br>
		#request.testMap.testAttr2:<s:property value="#request.testMap.testAttr2"/> <br>
		
		
		<s:file name="upload" label="输入要上传的文件名" />
	</s:form>
	有-request:<s:property value="#request.testMap.testAttr2"/><br>
	没有-request:<s:property value="#testMap.testAttr2"/><br>
	<br>
	el<br>
	${testMap}
	${testMap.testAttr2}
	
	
	

</body>
</html>
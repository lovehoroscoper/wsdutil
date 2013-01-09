<%
/*
final String queryString = request.getQueryString();
//final String url = request.getContextPath() + "/showplat/index.html" + (queryString != null ? "?" + queryString : "");
final String url = request.getContextPath() + "/showplat/index.html";
response.sendRedirect(response.encodeURL(url));
*/
%>

<jsp:directive.include file="includes/top.jsp" />

<a href="/cas/services/manage.html">
                        	kankan21.com
                        </a>

<jsp:directive.include file="includes/bottom.jsp" />

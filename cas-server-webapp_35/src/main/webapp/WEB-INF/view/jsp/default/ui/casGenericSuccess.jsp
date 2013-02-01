<%
final String queryString = request.getQueryString();
//final String url = request.getContextPath() + "/user/index.do";
final String url = request.getContextPath() + "/showplat/index.htm";
response.sendRedirect(response.encodeURL(url));
%>

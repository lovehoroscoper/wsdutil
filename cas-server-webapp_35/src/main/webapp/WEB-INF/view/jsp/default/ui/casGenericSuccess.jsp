<%
final String queryString = request.getQueryString();
final String url = request.getContextPath() + "/showplat/index.html" + (queryString != null ? "?" + queryString : "");
response.sendRedirect(response.encodeURL(url));
%>


<%
final String queryString = request.getQueryString();
//final String url = request.getContextPath() + "/showplat/index.html" + (queryString != null ? "?" + queryString : "");
final String url = request.getContextPath() + "/showplat/index.html";
response.sendRedirect(response.encodeURL(url));
%>


<%@ page import="entity.Book" %><%--
  Created by IntelliJ IDEA.
  User: 邦杠
  Date: 2018/8/15
  Time: 22:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Book book=(Book)  request.getSession().getAttribute("book");
%>
<h1><%=book.getName()%></h1>
<ul>
    <li><%=book.getPrice()%></li>
    <li><%=book.getAuthor()%></li>
</ul>
<input type="button" onclick="javascript:history.go(-1)" value="返回">
</body>
</html>

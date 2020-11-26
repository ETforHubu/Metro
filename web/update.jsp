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
<table>
    <tr>
        <td>编号:</td>
        <td><input type="text" id="id" disabled ="disabled " value="<%=book.getId()%>"></td>
    </tr>
    <tr>
        <td>书名:</td>
        <td><input type="text" id="name" value="<%=book.getName()%>"></td>
    </tr>
    <tr>
        <td>作者:</td>
        <td><input type="text" id="author" value="<%=book.getAuthor()%>"></td>
    </tr>
    <tr>
        <td>价钱:</td>
        <td><input type="text" id="price" value="<%=book.getPrice()%>"></td>
    </tr>
    <tr>
        <td ><input type="button" onclick="javascript:history.go(-1)" value="返回"></td>
        <td ><input type="button" onclick="go();" value="修改"></td>
    </tr>
</table>
<script>
    function go() {
        var ajax=new XMLHttpRequest();
        var id=document.getElementById("id").value;
        var name=document.getElementById("name").value;
        var author=document.getElementById("author").value;
        var price=document.getElementById("price").value;
        ajax.open("post","/BookUpdate",true);
        ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        ajax.send("id="+id+"&name="+name+"&author="+author+"&price="+price)
        ajax.onreadystatechange=function () {
            if(ajax.readyState==4&&ajax.status==200){
                alert(ajax.responseText);
                if(ajax.responseText=="修改成功"){
                    window.location.href="index.jsp";
                }

            }
        }
    }
</script>
</body>
</html>

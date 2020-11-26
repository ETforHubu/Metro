<%@ page import="dao.BookDAO" %>
<%@ page import="entity.Book" %><%--
  Created by IntelliJ IDEA.
  User: 邦杠
  Date: 2018/8/15
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>$Title$</title>
</head>
<style>
  html,body{width:100%;height: 100%}
  table{width: 1150px;height:500px;margin: auto}
  table,td,th{border: 1px solid #e6e6e6;border-collapse:collapse; }
  body{-moz-background-size:100% 100%;
    background-size:100% 100%;
    background-image:url("link.jpg");
    background-repeat: no-repeat}
  * { margin: 0; padding: 0; }
  table { border-collapse: collapse; text-align: center;  }
  /*关键设置 tbody出现滚动条*/
  table tbody {
    display: block;
    height: 500px;
    overflow-y: scroll;overflow-x:hidden;
  }
  table thead,  tbody tr { display: table;width: 100%; table-layout: fixed;  }
  table thead th {  height: 40px  }
  table tbody td {height: 30px }
  td input{margin-left: 5px;width: 40px}
</style>
</head>

<body>
<marquee><h1 style="color:white;">本页面用原生ajax进行展示</h1></marquee>
<table>
  <tr>
    <td>编号:</td>
    <td><input type="text" id="id" disabled ="disabled " value=""></td>
  </tr>
  <tr>
    <td>书名:</td>
    <td><input type="text" id="name" value=""></td>
  </tr>
  <tr>
    <td>作者:</td>
    <td><input type="text" id="author" value=""></td>
  </tr>
  <tr>
    <td>价钱:</td>
    <td><input type="text" id="price" value=""></td>
  </tr>
  <tr>
    <td ><input type="button" onclick="javascript:history.go(-1)" value="返回"></td>
  </tr>
</table>
</body>
<script>
    window.onload=function () {
        //alert(getQueryVariable("id"));
        var bookid=getQueryVariable("id");
        getDetail(bookid);
    }

    function getQueryVariable(variable)
    {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
        }
        return(false);
    }

    function getDetail(id){
        alert(id);
        //创建对象
        var ajax=new XMLHttpRequest();
        //http请求
        ajax.open("get","BookSelectOne.do?id="+id);
        //发送请求 (get为null post为参数)
        ajax.send(null);
        ajax.onreadystatechange=function () {
            if (ajax.readyState == 4 && ajax.status == 200) {
                var data = JSON.parse(ajax.responseText);
                alert(JSON.stringify(data));
                var id=document.getElementById("id");
                var name=document.getElementById("name");
                var author=document.getElementById("author");
                var price=document.getElementById("price");
                id.value = data.data[0].id;
                name.value = data.data[0].name;
                author.value = data.data[0].author;
                price.value = data.data[0].price;
            }
        }
    }

</script>
</html>


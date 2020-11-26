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
<table width="80%" border="1">
  <thead>
  <tr>
    <th style="width:230px">编号</th>
    <th style="width:231px">书名</th>
    <th style="width:231px">作者</th>
    <th style="width:231px">价格</th>
    <th style="width:249px">操作</th>
  </tr>
  <tr>
    <td colspan="5"><input onclick="insert()" type="button"value="添加新数据" style="width:100%;height:40px;margin: 0px" ></td>

  </tr>
  </thead>
  <tbody id="tbody">

  </tbody>
</table>
</body>
<script>
    window.onload=function () {
        select();
    }
    function insert() {
        window.location.href=("insert.jsp");
    }
    function update(id) {
        window.location.href=("BookUpdate?id="+id);
    }
    function dele(id) {
        //alert(id.toString());
        if(confirm("是否真的删除?")){
            var ajax=new XMLHttpRequest();
            ajax.open("get","BookDelete?id="+id);
            ajax.send(null);
            ajax.onreadystatechange=function () {
                console.log(ajax.readyState);
                console.log(ajax.status);
                if(ajax.readyState == 4 && ajax.status == 200){
                    if(ajax.responseText){
                        alert("删除成功");
                        select();
                    }
                }
            }
        }
    }
    function sele(id){
        //window.location.href=("BookUpdate?sele=sele&id="+id);

        window.location.href=("detail.jsp?id="+id);

    }
    function select() {
        //创建对象
        var ajax=new XMLHttpRequest();
        //http请求
        ajax.open("get","BookSelect.do")
        //发送请求 (get为null post为参数)
        ajax.send(null);
        //设置状态
        ajax.onreadystatechange=function () {
            if(ajax.readyState == 4 && ajax.status == 200){
                var data=JSON.parse(ajax.responseText);
                var tbody=document.getElementById("tbody");
                tbody.innerHTML="";
                console.log(data.data.length);
                for(var i=0;i<data.data.length;i++){
                    var tr=document.createElement("tr");
                    var tdid=document.createElement("td");
                    var tdname=document.createElement("td");
                    var tdauthor=document.createElement("td");
                    var tdprice=document.createElement("td");
                    var tdcc=document.createElement("td");
                    //创建按钮
                    var inputse=document.createElement("input");
                    inputse.value="详细";
                    inputse.setAttribute("onclick", "sele("+data.data[i].id+");");
                    var inputde=document.createElement("input");
                    inputde.value="删除";
                    inputde.setAttribute("onclick", "dele("+data.data[i].id+");");
                    var inputup=document.createElement("input");
                    inputup.value="修改";
                    inputup.setAttribute("onclick", "update("+data.data[i].id+");");
                    inputup.type=inputde.type=inputse.type="button";
                    //添加进入
                    var thid=document.createTextNode(data.data[i].id);
                    var thname=document.createTextNode(data.data[i].name);
                    var thauthor=document.createTextNode(data.data[i].author);
                    var thprice=document.createTextNode(data.data[i].price);
                    var thcc1=document.createTextNode(inputse);
                    var thcc2=document.createTextNode(inputde);
                    var thcc3=document.createTextNode( inputup);
                    tdcc.appendChild(inputse);
                    tdcc.appendChild(inputde);
                    tdcc.appendChild(inputup);

                    tdid.appendChild(thid);
                    tdname.appendChild(thname);
                    tdauthor.appendChild(thauthor);
                    tdprice.appendChild(thprice);

                    tr.appendChild(tdid);
                    tr.appendChild(tdname);
                    tr.appendChild(tdauthor);
                    tr.appendChild(tdprice);
                    tr.append(tdcc);
                    tbody.append(tr);
                }
            }
        }
    }

</script>
</html>


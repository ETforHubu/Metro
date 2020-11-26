<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/11/7
  Time: 8:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="js/themes/default/style.min.css" />
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <script src="js/jquery-3.5.1.js"></script>
    <script src="js/jstree.min.js"></script>
    <script src="js/Bootstrap v3.3.7.js"></script>
    <script type="text/javascript" charset="utf-8">
        $(function(){

        });

        function importExcel(){
            var xfile = document.getElementById("filePath");
            if(xfile.value==""){
                alert("您未选择任何文件！")
                return;
            }

            tempPath = "e:/导入数据1.xlsx"
//            $.ajax({
//                url : "UploadServlet",
//                type : "POST",
//                data: { "url": xfile },
//                success : function(data) {
//                   // var dataArray = eval('(' + data + ')');
//
//                },
//                error : function(e){
//                    alert("fail");
//                }
//            });

            //tempPath = xfile.value;
//            $.ajax({
//                url : "ReadExcel",
//                type : "POST",
//                data: { "url": tempPath },
//                success : function(data) {
//                   // var dataArray = eval('(' + data + ')');
//
//                },
//                error : function(e){
//                    alert("fail");
//                }
//            });
        };

        var getPath = function(obj){
            obj.select();
            var path=document.selection.createRange().text;
            obj.removeAttribute("src");
            obj.style.filter=
                "alpha(opacity=0);progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+path+"', sizingMethod='scale');";
            return path;
        }
    </script>
</head>
<body>
    <div id="container">
        <nav class="navbar navbar-default"  style="margin-bottom: 0px;" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">城市轨道交通工程造价管理系统</a>
                </div>
                <div>
                    <ul class="nav navbar-nav">
                        <li class="dropdown" >
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                数据查询
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="main.jsp">数据查询</a></li>
                                <li><a href="#">工程概况查询</a></li>
                                <li><a href="#">设备查询</a></li>
                                <li><a href="#">信息价查询</a></li>
                                <li><a href="#">概算查询</a></li>
                            </ul>
                        </li>
                        <li><a href="#">投资测算</a></li>
                        <li class="dropdown active" >
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                数据维护
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="batchUpdate.jsp">批量更新</a></li>
                                <li><a href="#">单条数据维护</a></li>
                                <li><a href="importExcel.jsp">数据导入</a></li>
                            </ul>
                        </li>
                        <li><a href="echarts.jsp">数据对比</a></li>
                        <li><a href="#">人员管理</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div id="header" style="background-color:#FFA500;">
            <h1 style="margin-bottom:0;">导入表格</h1></div>

        <form method="post" action="/TomcatTest/UploadServlet" enctype="multipart/form-data">
            选择一个文件:
            <input type="file" name="uploadFile" />
            <br/><br/>
            <input type="submit" value="导入" />
        </form>
        <%--<div class="ChooseImport">--%>
            <%--<input id="filePath" type="file" placeholder="请选需要导入的文件"/>--%>
            <%--<button onclick="importExcel()">提交</button>--%>
        <%--</div>--%>
    </div>
</body>

</html>

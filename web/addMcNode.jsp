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
    <title>新增子节点</title>
    <link rel="stylesheet" href="js/themes/default/style.min.css" />
    <script src="js/jquery-3.5.1.js"></script>
    <script src="js/jstree.min.js"></script>
    <script type="text/javascript">
        $(function(){

        });

        function addNewChild(){

        }

        function update(){

        }

    </script>
    <style>
    </style>
</head>
<body>
<div id="container">

    <div id="header" style="background-color:#FFA500;">
        <h1 style="margin-bottom:0;">请完整填写完整</h1></div>

        <p>父节点的id: <input type="text" id="nodeID" value=""></p>
        <table cellpadding="0" cellspacing="1" border="0" width="95%" class="formtable">
            <tr>
                <th style="width: 120px;">名称：</th>
                <td>
                    <input type="text" id="Title1"  class="mytext"   maxlength="100" style="width: 70%" /></td>
            </tr>
            <tr>
                <th style="width:80px;">数量：</th>
                <td><input type="text" id="Quantity"  class="mytext"  style="width:160px;" /></td>
            </tr>
            <tr>
                <th style="width:80px;">单位：</th>
                <td><input type="text" id="Unit"  class="mytext"  style="width:70%" /></td>
            </tr>
            <tr>
                <th class="auto-style1">建筑工程费：</th>
                <td class="auto-style2"><input type="text" id="ConstructionCost"  class="mytext"   style="width:160px;" /></td>
            </tr>
            <tr>
                <th class="auto-style1">安装工程费：</th>
                <td class="auto-style2"><input type="text" id="InstallCost"  class="mytext"   style="width:160px;" /></td>
            </tr>
            <tr>
                <th class="auto-style1">设备购置费：</th>
                <td class="auto-style2"><input type="text" id="DeviceCost"  class="mytext"   style="width:160px;" /></td>
            </tr>
            <tr>
                <th class="auto-style1">工程建设其他费用：</th>
                <td class="auto-style2"><input type="text" id="OtherCost"  class="mytext"   style="width:160px;" /></td>
            </tr>
            <tr>
                <th class="auto-style1">指标：</th>
                <td class="auto-style2"><input type="text"  id="Quota"  class="mytext"   style="width:160px;" /></td>
            </tr>
            <tr>
                <th class="auto-style1">编号：</th>
                <td class="auto-style2"><input type="text" id="SNumber"  class="mytext"    style="width:160px;" /></td>
            </tr>

            <tr style="display:none">
                <th style="width: 120px;">系统路径：</th>
                <td>
                    <input type="text" id="Code"  class="mytext"  validate="canempty,ajax" maxlength="100" style="width: 70%" /></td>
            </tr>
            <tr style="display:none">
                <th style="width: 120px;">当前目录：</th>
                <td>
                    <input type="text" id="Values"  class="mytext"  maxlength="100" style="width: 70%" value=""/></td>
            </tr>
            <tr style="display:none">
                <th style="width: 120px;">完整路径：</th>
                <td>
                    <textarea id="Note"  class="mytext"  style="width: 90%; height: 50px;"></textarea></td>
            </tr>
            <tr style="display:none">
                <th style="width: 120px;">备注：</th>
                <td>
                    <textarea id="Other" name="Other" class="mytext"  style="width: 90%; height: 150px;"></textarea></td>
            </tr>
        </table>
        <div style="display: flex;justify-content: center;margin-top:20px;" >
            <button style="margin-right:20px" onclick="addNewChild()">保存</button>
            <button style="margin-right:20px" onclick="update()">关闭</button>
        </div>
    </div>

</div>
</body>
</html>

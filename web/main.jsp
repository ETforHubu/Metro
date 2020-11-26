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
    <script src="js/Bootstrap v3.3.7.js"></script>
    <script src="js/jstree.min.js"></script>
    <script type="text/javascript">
        $(function(){
            showMenu();
        });
        function showMenu() {
            var myModalLabel = document.getElementById("myModalLabel");
            myModalLabel.innerHTML="增加子节点（未选中父节点）"
            $('#jstree_div')
                .on("changed.jstree", function (e, data) {
                    if(data.selected.length) {
                        $("#nodeID").val(data.instance.get_node(data.selected[0]).id);
                        getDetailInfo(data.instance.get_node(data.selected[0]).id);
                    }
                })
                .jstree({
                    'core' : {
                        'check_callback': true,
                        'data' : function (node, callback) {
                            $.ajax({
                                url : "menuOne",
                                type : "POST",
                                success : function(data) {
                                    var dataArray = eval('(' + data + ')');
                                    callback.call(this, dataArray);
                                },
                                error : function(e){
                                    alert("fail");
                                }
                            });
                        }
                    }
                });
        }

        function getDetailInfo(id){
            $.ajax({
                url : "selectById?id=" + id,
                type : "POST",
                success : function(data) {
                    var dataArray = eval('(' + data + ')');
                    var M_title = document.getElementById("Title1");
                    var M_Quantity = document.getElementById("Quantity");
                    var M_Unit = document.getElementById("Unit");
                    var M_ConstructionCost = document.getElementById("ConstructionCost");
                    var M_InstallCost = document.getElementById("InstallCost");
                    var M_DeviceCost = document.getElementById("DeviceCost");
                    var M_OtherCost = document.getElementById("OtherCost");
                    var M_Quota = document.getElementById("Quota");
                    var M_parentID = document.getElementById("parentID");
                    var M_SNumber = document.getElementById("SNumber");
                    var M_TotalCost= document.getElementById("TotalCost");

                    var myModalLabel = document.getElementById("myModalLabel");
                    var childSNumber = document.getElementById("childSNumber");

                    if(dataArray[0].title =="null"){
                        M_title.value = "" ;
                        myModalLabel.innerHTML = "未命名";
                    }else{
                        M_title.value = dataArray[0].title;
                        myModalLabel.innerHTML = "新增子节点（"+ dataArray[0].title +")";
                    }
                    if(dataArray[0].sNumber =="null"){
                        M_SNumber.value = "" ;
                        childSNumber.value = "";
                    }else{
                        M_SNumber.value = dataArray[0].sNumber;
                        childSNumber.value = dataArray[0].sNumber + ".";
                    }
                    if(dataArray[0].unit =="null"){
                        M_Unit.value = "" ;
                    }else{
                        M_Unit.value = dataArray[0].unit;
                    }
                    M_Quantity.value = dataArray[0].quantity;
                    M_ConstructionCost.value = dataArray[0].constructionCost;
                    M_InstallCost.value = dataArray[0].installCost;
                    M_DeviceCost.value = dataArray[0].deviceCost;
                    M_OtherCost.value = dataArray[0].otherCost;
                    var total = parseFloat(dataArray[0].constructionCost) + parseFloat(dataArray[0].installCost) + parseFloat(dataArray[0].deviceCost) + parseFloat(dataArray[0].otherCost)
                    M_parentID.value = dataArray[0].parentId;
                    M_TotalCost.value = total.toString();
                    var tquota = (total/parseFloat(dataArray[0].quantity)).toString()
                    M_Quota.value = tquota.substring(0,tquota.indexOf(".")+3) ;
                },
                error : function(e){
                    alert("fail");
                }
            });
        }

        function addNewChild(){
            var nodeId = document.getElementById("nodeID").value;
            if(nodeId == ""){
                alert("您未选中任何节点！");
                return;
            }
            var childSNumber = document.getElementById("childSNumber").value;
            if(childSNumber == ""){
                alert("节点编号不能为空！");
                return;
            }
            var childTitle = document.getElementById("childTitle").value;
            if(childTitle == ""){
                alert("节点名称不能为空！");
                return;
            }
            var childQuantity = document.getElementById("childQuantity").value;
            var childUnit = document.getElementById("childUnit").value;
            var childConstructionCost = document.getElementById("childConstructionCost").value;
            var childInstallCost = document.getElementById("childInstallCost").value;
            var childDeviceCost = document.getElementById("childDeviceCost").value;
            var childOtherCost = document.getElementById("childOtherCost").value;
            $.ajax({
                url : "addNode",
                type : "POST",
                data: {
                    "parentId": nodeId,
                    "sNumber": childSNumber,
                    "quantity": childQuantity,
                    "title": childTitle,
                    "unit": childUnit,
                    "constructionCost": childConstructionCost,
                    "installCost": childInstallCost,
                    "deviceCost": childDeviceCost,
                    "otherCost": childOtherCost,
                },
                success : function(data) {
                    console.log(data);
                    $('#myModal').modal('hide');
                    alert("新建成功！");''
                    document.getElementById("nodeID").value ="";
                    document.getElementById("childSNumber").value="";
                    document.getElementById("childTitle").value="";
                    document.getElementById("childQuantity").value="";
                    document.getElementById("childUnit").value="";
                    document.getElementById("childConstructionCost").value="";
                    document.getElementById("childInstallCost").value="";
                    document.getElementById("childDeviceCost").value ="";
                    document.getElementById("childOtherCost").value = "";
                    location.reload();
                },
                error : function(e){
                    alert("fail");
                }
            });
        }

        function update(){
            var nodeId = document.getElementById("nodeID").value;
            var M_title = document.getElementById("Title1");
            var M_Quantity = document.getElementById("Quantity");
            var M_Unit = document.getElementById("Unit");
            var M_ConstructionCost = document.getElementById("ConstructionCost");
            var M_InstallCost = document.getElementById("InstallCost");
            var M_DeviceCost = document.getElementById("DeviceCost");
            var M_OtherCost = document.getElementById("OtherCost");
            var M_Quota = document.getElementById("Quota");
            var M_SNumber = document.getElementById("SNumber");
            var M_parentID = document.getElementById("parentID");

            if(nodeId == ""){
                alert("您未选中任何节点！");
                return;
            }
            if(M_SNumber.value == ""){
                alert("节点编号不能为空！");
                return;
            }
            if(M_title.value == ""){
                alert("节点名称不能为空！");
                return;
            }
            $.ajax({
                url : "updateNode",
                type : "POST",
                data: {
                    "id": nodeId,
                    "parentId": M_parentID.value,
                    "sNumber": M_SNumber.value,
                    "quantity": M_Quantity.value,
                    "title": M_title.value,
                    "unit": M_Unit.value,
                    "constructionCost": M_ConstructionCost.value,
                    "installCost": M_InstallCost.value,
                    "deviceCost": M_DeviceCost.value,
                    "otherCost": M_OtherCost.value,
                },
                success : function(data) {
                    alert("更新节点成功！")
                    location.reload();
                },
                error : function(e){
                    alert("fail");
                }
            });
        }

        function del(){
            var nodeId = document.getElementById("nodeID").value;
            var M_parentID = document.getElementById("parentID");
            if(nodeId == ""){
                alert("您未选中任何节点！");
                return;
            }
            $.ajax({
                url : "deleteNode",
                type : "POST",
                data: {
                    "id": nodeId,
                    "parentId": M_parentID.value,
                },
                success : function(data) {
                    alert("删除节点成功！")
                        location.reload();
                },
                error : function(e){
                    alert("fail");
                }
            });
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
                    <li class="dropdown active" >
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
                        <li class="dropdown" >
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

    <div id="menu" style="background-color:#FFD700;height:90%;width:20%;float:left;overflow-y: auto">
        <p><div id="jstree_div"></div></p>
    </div>

    <div id="content" style="background-color:#EEEEEE;height:90%;width:80%;float:left;display: flex;align-items: center;flex-direction: column;">
        <p>NodeID: <input type="text" id="nodeID" value=""></p>
        <div style="padding: 20px 20px 10px; height:80%;width:50%">
            <form class="bs-example bs-example-form" role="form">
                <div class="input-group" style="display: none;">
                    <span class="input-group-addon">父节点id</span>
                    <input type="text" id="parentID" class="form-control" placeholder="请输入名称">
                </div>
                <div class="input-group">
                    <span class="input-group-addon">名称</span>
                    <input type="text" id="Title1" class="form-control" placeholder="请输入名称">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">数量</span>
                    <input type="text" id="Quantity" class="form-control" placeholder="请输入数量">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">单位</span>
                    <input type="text" id="Unit"  class="form-control" placeholder="请输入单位">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">建筑工程费</span>
                    <input type="text" id="ConstructionCost" class="form-control" placeholder="请输入建筑工程费">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">安装工程费</span>
                    <input type="text" id="InstallCost" class="form-control" placeholder="请输入安装工程费">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">设备购置费</span>
                    <input type="text" id="DeviceCost" class="form-control" placeholder="请输入设备购置费">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">工程建设其他费用</span>
                    <input type="text"  id="OtherCost" class="form-control" placeholder="请输入工程建设其他费用">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">指标</span>
                    <input type="text" id="Quota" class="form-control" placeholder="请输入指标">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">总价</span>
                    <input type="text" id="TotalCost" class="form-control" placeholder="请输入总价">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon">编号</span>
                    <input type="text" id="SNumber" class="form-control" placeholder="请输入编号">
                </div>
            </form>
        </div>
        <table cellpadding="0" cellspacing="1" border="0" width="95%" class="formtable">
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
            <button class="btn btn-primary btn-sm" style="margin-right:20px;" data-toggle="modal" data-target="#myModal">新增子节点</button>
            <button class="btn btn-primary btn-sm"  style="margin-right:20px;" onclick="update()">更新</button>
            <button class="btn btn-primary btn-sm"  onclick="del()">删除</button>
        </div>

        <%--警告--%>

        <div id="myAlert" hidden="true" class="alert alert-success">
            <a href="#" class="close" data-dismiss="alert">&times;</a>
            <strong>成功！</strong>子节点添加成功。
        </div>
    </div>


    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        新增子节点
                    </h4>
                </div>
                <div class="modal-body">
                    <div style="padding: 60px 60px 10px;">
                        <form class="bs-example bs-example-form" role="form">
                            <div class="input-group">
                                <span class="input-group-addon">编号</span>
                                <input type="text" id="childSNumber" class="form-control" placeholder="请输入编号">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">名称</span>
                                <input type="text" id="childTitle" class="form-control" placeholder="请输入名称">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">数量</span>
                                <input type="text" id="childQuantity" class="form-control" placeholder="请输入数量">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">单位</span>
                                <input type="text" id="childUnit" class="form-control" placeholder="请输入单位">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">建筑工程费</span>
                                <input type="text" id="childConstructionCost" class="form-control" placeholder="请输入建筑工程费">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">安装工程费</span>
                                <input type="text" id="childInstallCost" class="form-control" placeholder="请输入安装工程费">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">设备购置费</span>
                                <input type="text" id="childDeviceCost" class="form-control" placeholder="请输入设备购置费">
                            </div>
                            <br>
                            <div class="input-group">
                                <span class="input-group-addon">工程建设其他费用</span>
                                <input type="text" id="childOtherCost" class="form-control" placeholder="请输入工程建设其他费用">
                            </div>
                            <br>
                            <div class="input-group" style="display: none;">
                                <span class="input-group-addon">指标</span>
                                <input type="text" id="childQuota" class="form-control" placeholder="请输入指标">
                            </div>
                        </form>
                    </div>​
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" onclick="addNewChild()">
                        提交更改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <%--警告--%>
    <div id="myAlert1"  style="display: none" class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        <strong>警告！</strong>您未选中任何节点。
    </div>
</div>
</body>
</html>

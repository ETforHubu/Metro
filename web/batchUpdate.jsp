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
    <title>批量更新</title>
    <link rel="stylesheet" href="js/themes/default/style.min.css" />
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/sweetalert.css" />
    <script src="js/jquery-3.5.1.js"></script>
    <script src="js/Bootstrap v3.3.7.js"></script>
    <script src="js/sweetalert.js"></script>
    <script src="js/jstree.min.js"></script>
    <script type="text/javascript">
        //点击时获取的全部数据
        var dataArray  = new Array();
        //树形结构的最大层级数
        var MaxTreeLength = 0 ;
        //树形结构的最小层级数
        var MinTreeLength = 1000;

        $(function(){
            showMenu();
        });
        function showMenu() {
            $('#jstree_div')
                .on("changed.jstree", function (e, data) {
                    if(data.selected.length) {
                        $("#nodeID").val(data.instance.get_node(data.selected[0]).id);
                        getTable(data.instance.get_node(data.selected[0]).id);
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
                                    var dataArray1 = eval('(' + data + ')');
                                    callback.call(this, dataArray1);
                                },
                                error : function(e){
                                    alert("fail");
                                }
                            });
                        }
                    }
                });
        }
        //点击按钮将其与其子节点全部呈现出来
        function getTable(id){
            var tbody1 = document.getElementById("tbody1");
            tbody1.innerHTML="";
            $.ajax({
                url : "getTable",
                type : "POST",
                data:{
                    "id": id,
                },
                success : function(data) {
                    dataArray = eval('(' + data + ')');
                    console.log(dataArray);
                    //初始化
                    MinTreeLength = 1000;
                    MaxTreeLength = 0;
                    for(var i=0;i<dataArray.length;i++){
                        //找出数据的层级结构
                        var thisLevel = getStrCount(dataArray[i].sNumber,".");
                        if(thisLevel>MaxTreeLength){
                            MaxTreeLength = thisLevel;
                        }
                        if(thisLevel<MinTreeLength){
                            MinTreeLength = thisLevel;
                        }
                        dataArray[i].level = thisLevel.toString();
                        //找到表身位置
                        var tdclass = "";
                        if((i +1)%2 !=0){
                            tdclass = "inputClass";
                        }else{
                            tdclass = "inputClass1";
                        }
                        var tbody1 = document.getElementById("tbody1");
                        var tr = document.createElement("tr");
                        tbody1.appendChild(tr);

                        //编号
                        var td = document.createElement("td");
                        tr.appendChild(td);
                        var input = document.createElement("input");
                        input.setAttribute('class', tdclass);
                        input.setAttribute('disabled', "true");
                        input.value = dataArray[i].sNumber;
                        td.appendChild(input);

                        //工程及费用名称
                        var td1 = document.createElement("td");
                        tr.appendChild(td1);
                        var input1 = document.createElement("input");
                        input1.setAttribute('class', tdclass);
                        input1.setAttribute('oninput', "changeTitle(this,"+ i+")");
                        input1.value = dataArray[i].title;
                        td1.appendChild(input1);

                        //单位
                        var td2 = document.createElement("td");
                        tr.appendChild(td2);
                        var input2 = document.createElement("input");
                        input2.setAttribute('class', tdclass);
                        input2.setAttribute('oninput', "changeUnit(this,"+ i +")");
                        input2.value = dataArray[i].unit;
                        td2.appendChild(input2);

                        //建筑工程费
                        var td3 = document.createElement("td");
                        tr.appendChild(td3);
                        var input3 = document.createElement("input");
                        input3.setAttribute('class', tdclass);
                        input3.setAttribute('oninput', "changeConstructionCost(this,"+ i +")");
                        input3.value = dataArray[i].constructionCost;
                        td3.appendChild(input3);

                        //安装工程费
                        var td4 = document.createElement("td");
                        tr.appendChild(td4);
                        var input4 = document.createElement("input");
                        input4.setAttribute('class', tdclass);
                        input4.setAttribute('oninput', "changeInstallCost(this,"+ i +")");
                        input4.value = dataArray[i].installCost;
                        td4.appendChild(input4);

                        //设备购置费
                        var td5 = document.createElement("td");
                        tr.appendChild(td5);
                        var input5 = document.createElement("input");
                        input5.setAttribute('class',tdclass);
                        input5.setAttribute('oninput', "changeDeviceCost(this,"+ i +")");
                        input5.value = dataArray[i].deviceCost;
                        td5.appendChild(input5);

                        //工程建设其他费用
                        var td6 = document.createElement("td");
                        tr.appendChild(td6);
                        var input6 = document.createElement("input");
                        input6.setAttribute('class', tdclass);
                        input6.setAttribute('oninput', "changeOtherCost(this," + i +")");
                        input6.value = dataArray[i].otherCost;
                        td6.appendChild(input6);

                        var totalCost = parseFloat(dataArray[i].constructionCost)+parseFloat(dataArray[i].installCost)+parseFloat(dataArray[i].deviceCost)+parseFloat(dataArray[i].otherCost);
                        //合价
                        var td7 = document.createElement("td");
                        tr.appendChild(td7);
                        var input7 = document.createElement("input");
                        input7.setAttribute('disabled', "true");
                        input7.setAttribute('class', tdclass);
                        input7.value = totalCost;
                        td7.appendChild(input7);

                        //数量
                        var td8 = document.createElement("td");
                        tr.appendChild(td8);
                        var input8 = document.createElement("input");
                        input8.setAttribute('class', tdclass);
                        input8.setAttribute('oninput', "changeQuantity(this,"+ i +")");
                        input8.value = dataArray[i].quantity;
                        td8.appendChild(input8);

                        //指标
                        var td9 = document.createElement("td");
                        tr.appendChild(td9);
                        var input9 = document.createElement("input");
                        input9.setAttribute('class',tdclass);
                        input9.setAttribute('disabled', "true");
                        input9.value = (totalCost/parseFloat(dataArray[i].quantity)).toFixed(2);
                        td9.appendChild(input9);
                    }
                },
                error : function(e){
                    alert("fail");
                }
            });
        }
        //批量更新
        function update(){
//            $('#myModal').modal('show');//弹出模态框
                $.ajax({
                    url : "batchUpdate",
                    type : "POST",
                    data: {
                        "MaxTreeLength": MaxTreeLength,
                        "MinTreeLength":MinTreeLength,
                        "data":JSON.stringify(dataArray),
                    },
                    success : function(data) {
                        console.log(total,data);
                        alert("更新成功");
                        location.reload();
                    },
                    error : function(e){
                        alert("fail");
                    }
                });
        }

        //获取字符串中某字符的数量
        function getStrCount(str, char){
            var pos;
            var arr = [];
            pos = str.indexOf(char);
            while (pos > -1) {
                arr.push(pos);
                pos = str.indexOf(char, pos + 1);
            }
            return arr.length;

        }

        //改写sNumber
        function changesNumber(obj,index){
            console.log(dataArray)
        }
        //改写title
        function changeTitle(obj,index){
            dataArray[index].title = obj.value;
        }
        //改写unit
        function changeUnit(obj,index){
            dataArray[index].unit = obj.value;
        }
        //改写ConstructionCost
        function changeConstructionCost(obj,index){
            dataArray[index].constructionCost = obj.value;
        }
        //改写installCost
        function changeInstallCost(obj,index){
            dataArray[index].installCost = obj.value;
        }
        //改写deviceCost
        function changeDeviceCost(obj,index){
            dataArray[index].deviceCost = obj.value;
        }
        //改写otherCost
        function changeOtherCost(obj,index){
            dataArray[index].otherCost = obj.value;
        }
        //改写quantity
        function changeQuantity(obj,index){
            dataArray[index].quantity = obj.value;
        }

        function del(){
            console.log(MaxTreeLength,MinTreeLength);
            swal({
                title: "操作提示",      //弹出框的title
                text: "确定删除吗？",   //弹出框里面的提示文本
                type: "warning",        //弹出框类型
                showCancelButton: true, //是否显示取消按钮
                confirmButtonColor: "#DD6B55",//确定按钮颜色
                cancelButtonText: "取消",//取消按钮文本
                confirmButtonText: "是的，确定删除！",//确定按钮上面的文档
                closeOnConfirm: true
            }, function () {
                $.ajax({
                    type: "post",
                    url: "/Home/Delete",
                    data: { "": JSON.stringify(arrselections) },
                    success: function (data, status) {
                        if (status == "success") {
                            toastr.success('提交数据成功');
                            $("#tb_departments").bootstrapTable('refresh');
                        }
                    },
                    error: function () {
                        toastr.error('Error');
                    },
                    complete: function () {

                    }

                });
            });
        }

    </script>
    <style>
         .text-center{
             text-align: center;
         }
        .inputClass{
            width: 100%;
            background-color: #f9f9f9;
            border: none;
        }

         .inputClass1{
             width: 100%;
             background-color: #eee;
             border: none;
         }
    </style>
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

    <div id="menu" style="background-color:#FFD700;height:90%;width:20%;float:left;overflow-y: auto">
        <p><div id="jstree_div"></div></p>
    </div>

    <div id="content" style="background-color:#EEEEEE;height:90%;width:80%;float:left;display: flex;align-items: center;flex-direction: column;overflow-y: auto">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th class="text-center">编号</th>
                    <th class="text-center">工程及费用名称</th>
                    <th class="text-center">单位</th>
                    <th class="text-center">建筑工程费（万元）</th>
                    <th class="text-center">安装工程费（万元）</th>
                    <th class="text-center">设备购置费（万元）</th>
                    <th class="text-center">工程建设其他费用（万元）</th>
                    <th class="text-center">合价（万元）</th>
                    <th class="text-center">数量</th>
                    <th class="text-center">指标（万元）</th>
                </tr>
            </thead>
            <tbody id="tbody1">
            </tbody>
        </table>
        <div style="display: flex;justify-content: center;margin-top:20px;" >
            <button class="btn btn-primary btn-sm" style="margin-right:20px;" >新增子节点</button>
            <button class="btn btn-primary btn-sm"  style="margin-right:20px;" onclick="update()">更新</button>
            <button class="btn btn-primary btn-sm"  onclick="del()">删除</button>
        </div>

        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="myModalLabel">
                            正在进行批量更新
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div class="progress">
                            <div class="progress-bar progress-bar-success" id="progress" role="progressbar"
                                 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
                                 style="width: 0%;">
                                <span class="sr-only">90% 完成（成功）</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

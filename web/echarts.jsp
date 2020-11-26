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
  <title>echarts</title>
  <link rel="stylesheet" href="js/themes/default/style.min.css" />
  <link rel="stylesheet" href="css/bootstrap.min.css" />
  <script src="js/echarts.min.js"></script>
  <script src="js/jquery-3.5.1.js"></script>
  <script src="js/Bootstrap v3.3.7.js"></script>
</head>

</head>
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
          <li class="dropdown" >
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              数据维护
              <b class="caret"></b>
            </a>
            <ul class="dropdown-menu">
              <li><a href="batchUpdate.jsp">批量更新</a></li>
              <li><a href="">单条数据维护</a></li>
              <li><a href="importExcel.jsp">数据导入</a></li>
            </ul>
          </li>
          <li class="active"><a href="echarts.jsp">数据对比</a></li>
          <li><a href="#">人员管理</a></li>
        </ul>
      </div>
    </div>
  </nav>
  <div style="display: flex;align-content: center;">
    <div id="main" style="width: 600px;height:400px;"></div>
    <div>
      <div class="btn-group" style="margin-left: 20px;">
        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">选择需要查看的线路
          <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu" id="dropDownMenu">
        </ul>
      </div>
      <div id="main2" style="width: 600px;height:400px;"></div>
    </div>
  </div>
<body>

</body>
<script>
    window.onload=function () {
        getLine1Charts();
        changePie("48d1acf4ad99407aa04bfad6c0ba3256");
    }
    var lineInfo=new Array()
    var myChart1 = echarts.init(document.getElementById('main2'));

    function getLine1Charts(){
        var myChart = echarts.init(document.getElementById('main'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: '不同线路数据对比'
            },
            tooltip: {},
            legend: {
                data:['指标','工程费用占比','二类费占比']
            },
            xAxis: {
            },
            yAxis: {},
            series: []
        };
        //获取数据库数据
        $.ajax({
            url : "EchartsData",
            type : "POST",
            data: {
                "type": "line1",
            },
            success : function(data) {
                var dataArray = eval('(' + data + ')');
                option.xAxis = dataArray[0].xAxis;
                option.series = dataArray[0].series;
                getMorelineInfo(dataArray[0].lineInfo);
                console.log(dataArray);
                myChart.setOption(option);
            },
            error : function(e){
                alert("fail");
            }
        });
    }
    function getMorelineInfo(lineInfo) {
        var dropDownMenu = document.getElementById("dropDownMenu");
        for(var i= 0;i<lineInfo.length;i++){
            var li = document.createElement("li");
            li.innerHTML =lineInfo[i].title;
            console.log(lineInfo[i].id);
            li.setAttribute('onclick', "changePie('"+ lineInfo[i].id+"')");
            dropDownMenu.appendChild(li);
        }
    }

    function changePie(id){
        // 指定图表的配置项和数据
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            title: {
                text: ''
            },
            series : [
                {
                    name: '成本',
                    type: 'pie',
                    radius: '55%',
                    data:[]

                }
            ]
        };

        //获取数据库数据
        $.ajax({
            url : "EchartsData",
            type : "POST",
            data: {
                "type": "pie",
                "id": id,
            },
            success : function(data) {
                var dataArray = eval('(' + data + ')');
                console.log(dataArray);
                option.series[0].data = dataArray[0].data;
                option.title.text = dataArray[0].title;
                myChart1.setOption(option);
            },
            error : function(e){
                alert("fail");
            }
        });
    }

</script>
</html>


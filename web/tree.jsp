<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/11/6
  Time: 9:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JsTree--ajax</title>
    <link rel="stylesheet" href="js/themes/default/style.min.css" />
    <script src="js/jquery-3.5.1.js"></script>
    <script src="js/jstree.min.js"></script>
    <script type="text/javascript">
        $(function(){
            showMenu();
        });
        function showMenu() {
            $('#jstree_div')
                .on("changed.jstree", function (e, data) {
                    if(data.selected.length) {
                        alert('The selected node is: ' + data.instance.get_node(data.selected[0]).id);
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
    </script>
</head>
<body>
<div id="jstree_div"></div>




</body>
</html>

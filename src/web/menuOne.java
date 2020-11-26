package web;

import dao.mc_nodeDAO;
import entity.mc_node;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet( "/menuOne")
public class menuOne extends HttpServlet {
    mc_nodeDAO mc_nodeDAO1 = new mc_nodeDAO();
    List<mc_node> listMc_node=new ArrayList<>();
    String data ="";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码
        request.setCharacterEncoding("utf-8");
        // 设置响应编码
        response.setCharacterEncoding("utf-8");
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        // 获取请求参数
        // 响应内容传递数据给Ajax的回调函数data
        //String data="[{ 'id' : 'ajson1', 'parent' : '#', 'text' : 'Simple root node' },{ 'id' : 'ajson2', 'parent' : '#', 'text' : 'Root node 2' },{ 'id' : 'ajson3', 'parent' : 'ajson2', 'text' : 'Child 1' },{ 'id' : 'ajson4', 'parent' : 'ajson2', 'text' : 'Child 2' }]";
        data ="[{ 'id' : '1', 'parent' : '#', 'text' : '深圳轨道交通' },";
        getAllChild("1");
        data = data.substring(0,data.length()-1) + "]";
        response.getWriter().write(data);
    }

    protected void getAllChild(String b){
        List<mc_node> listTemp=new ArrayList<>();
        listTemp = mc_nodeDAO1.selectChild(b);
        if(listTemp.size() !=0){
            for(int i =0;i<listTemp.size();i++) {
                mc_node s = (mc_node)listTemp.get(i);
                 data += "{'id':'"+ s.getId()+"','parent':'"+ s.getParentId()+"','text':'"+s.getTitle() +"'},";
                 List<mc_node> listTemp1=new ArrayList<>();
                 listTemp1 = mc_nodeDAO1.selectChild(s.getId());
                 if(listTemp1.size() !=0){
                     getAllChild(s.getId());
                 }
            }
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }
}

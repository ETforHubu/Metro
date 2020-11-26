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

@WebServlet( "/selectById")
public class selectById extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码
        request.setCharacterEncoding("utf-8");
        // 设置响应编码
        response.setCharacterEncoding("utf-8");
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        // 获取请求参数
        // 响应内容传递数据给Ajax的回调函数data
        mc_nodeDAO mc_nodeDAO1 = new mc_nodeDAO();
        mc_node Mc_node1=new mc_node();
        Mc_node1 = mc_nodeDAO1.selectById( request.getParameter("id"));
        String data ="";
        data ="[{'id':'"+ Mc_node1.getId()+"','parentId':'"+ Mc_node1.getParentId()+"','title':'"+Mc_node1.getTitle()
                +"','code':'"+Mc_node1.getCode()+"','value':'"+Mc_node1.getValue()+"','note':'"+Mc_node1.getNote()
                +"','other':'"+Mc_node1.getOther()+"','sort':'"+Mc_node1.getSort()+"','quantity':'"+Mc_node1.getQuantity()
                +"','unit':'"+Mc_node1.getUnit()+"','constructionCost':'"+Mc_node1.getConstructionCost()+"','installCost':'"+Mc_node1.getInstallCost()
                +"','otherCost':'"+Mc_node1.getOtherCost()+"','quota':'"+Mc_node1.getQuota()+"','sNumber':'"+Mc_node1.getsNumber()
                +"','deviceCost':'"+Mc_node1.getDeviceCost()
                +"'}]";
        response.getWriter().write(data);
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }
}

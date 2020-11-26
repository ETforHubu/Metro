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

@WebServlet( "/deleteNode")
public class deleteNode extends HttpServlet {

    mc_nodeDAO mc_nodeDAO1 = new mc_nodeDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码
        request.setCharacterEncoding("utf-8");
        // 设置响应编码
        response.setCharacterEncoding("utf-8");
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        // 获取请求参数
        // 响应内容传递数据给Ajax的回调函数data
        mc_node Mc_node1=new mc_node();
        String data ="删除成功";
        String id = request.getParameter("id");
        deldeAllChild(id);
        updateParentNode(request.getParameter("parentId"));
        //mc_nodeDAO1.delete(id);
        response.getWriter().write(data);
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }
    //查询并删除所有子节点
    protected void deldeAllChild (String id){
        List<mc_node> listTemp=new ArrayList<>();
        listTemp = mc_nodeDAO1.selectChild(id);
        if(listTemp.size() !=0){
            for(int i =0;i<listTemp.size();i++) {
                mc_node s = (mc_node)listTemp.get(i);
                List<mc_node> listTemp1=new ArrayList<>();
                listTemp1 = mc_nodeDAO1.selectChild(s.getId());
                mc_nodeDAO1.delete(s.getId());
                if(listTemp1.size() !=0){
                    deldeAllChild(s.getId());
                }
            }
        }
        mc_nodeDAO1.delete(id);
    }

    //父节点的数据做出相应的更新
    protected void updateParentNode(String id){
        mc_node Mc_node2=new mc_node();
        Mc_node2 = mc_nodeDAO1.selectById(id);
        String tempParentId = Mc_node2.getParentId();
        if(tempParentId.equals("-1")){

        }
        else{
            double totalConstructionCost =0;
            double totalInstallCost =0;
            double totalDeviceCost =0;
            double totalOtherCost =0;
            List<mc_node> listTemp1 = new ArrayList<>();
            listTemp1 = mc_nodeDAO1.selectChild(Mc_node2.getId());
            for(int i =0;i<listTemp1.size();i++) {
                mc_node s = (mc_node)listTemp1.get(i);
                totalConstructionCost = totalConstructionCost + s.getConstructionCost();
                totalInstallCost = totalInstallCost + s.getInstallCost();
                totalDeviceCost = totalDeviceCost + s.getDeviceCost();
                totalOtherCost = totalOtherCost + s.getOtherCost();
            }
            Mc_node2.setConstructionCost(totalConstructionCost);
            Mc_node2.setInstallCost(totalInstallCost);
            Mc_node2.setDeviceCost(totalDeviceCost);
            Mc_node2.setOtherCost(totalOtherCost);
            mc_nodeDAO1.updateParent(Mc_node2);
            updateParentNode(Mc_node2.getParentId());
        }
    }

}

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
import java.util.UUID;

@WebServlet( "/addNode")
public class addNode extends HttpServlet {

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
        String data ="";
        data = getUuid();
        Mc_node1.setId(data);
        Mc_node1.setsNumber(request.getParameter("sNumber"));
        Mc_node1.setParentId(request.getParameter("parentId"));
        Mc_node1.setTitle(request.getParameter("title"));
        if(request.getParameter("quantity")!=""){
            Mc_node1.setQuantity(Double.parseDouble(request.getParameter("quantity")));
        }
        if(request.getParameter("constructionCost")!=""){
            Mc_node1.setConstructionCost(Double.parseDouble(request.getParameter("constructionCost")));
        }
        if(request.getParameter("installCost")!=""){
            Mc_node1.setInstallCost(Double.parseDouble(request.getParameter("installCost")));
        }
        if(request.getParameter("deviceCost")!=""){
            Mc_node1.setDeviceCost(Double.parseDouble(request.getParameter("deviceCost")));
        }
        if(request.getParameter("otherCost")!=""){
            Mc_node1.setOtherCost(Double.parseDouble(request.getParameter("otherCost")));
        }
        if(request.getParameter("unit")!=""){
            Mc_node1.setUnit(request.getParameter("unit"));
        }
        mc_nodeDAO1.add(Mc_node1);
        updateParentNode(request.getParameter("parentId"));
        response.getWriter().write(data);
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }

    //获取唯一id
    private String getUuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
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

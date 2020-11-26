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
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

@WebServlet( "/batchUpdate")
public class batchUpdate extends HttpServlet {

    mc_nodeDAO mc_nodeDAO1 = new mc_nodeDAO();
    List<mc_node> listMc_node=new ArrayList<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码
        request.setCharacterEncoding("utf-8");
        // 设置响应编码
        response.setCharacterEncoding("utf-8");
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        // 获取请求参数
        // 响应内容传递数据给Ajax的回调函数data
        int max = Integer.parseInt(request.getParameter("MaxTreeLength"));
        int min = Integer.parseInt(request.getParameter("MinTreeLength"));
        String data =request.getParameter("data");
        //获取json
        parseJSONWithJSONObject(response,data,max,min);

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }


    private void parseJSONWithJSONObject( HttpServletResponse response,String JsonData,int max,int min) {
        //建立临时层级相关数据存储变量
        double[] tempConstructionCost = new double[max+2];
        double[] tempInstallCost = new double[max+2];
        double[] tempDeviceCost = new double[max+2];
        double[] tempOtherCost = new double[max+2];
        int previousLevel = -1;
        try {
            JSONArray jsonArray = new JSONArray(JsonData);
            for (int i = jsonArray.length()-1; i>=0; i--) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int level = Integer.parseInt(jsonObject.getString("level"));//标记当前层级
                String id = jsonObject.getString("id");
                String parentId = jsonObject.getString("parentId");
                String title = jsonObject.getString("title");
                String unit = jsonObject.getString("unit");
                String sNumber = jsonObject.getString("sNumber");
                double quantity = Double.parseDouble(jsonObject.getString("quantity"));
                double constructionCost = Double.parseDouble(jsonObject.getString("constructionCost"));
                double installCost = Double.parseDouble(jsonObject.getString("installCost"));
                double deviceCost = Double.parseDouble(jsonObject.getString("deviceCost"));
                double otherCost = Double.parseDouble(jsonObject.getString("otherCost"));


                int currentLevel = max - level;
                //判断节点位置
                if(currentLevel==0 || previousLevel== -1 || level + 1 != previousLevel){
                    tempConstructionCost[currentLevel] = constructionCost;
                    tempInstallCost[currentLevel] = installCost;
                    tempDeviceCost[currentLevel] = deviceCost;
                    tempOtherCost[currentLevel] = otherCost;
                }

                //存储当前节点值于父节点临时变量
                tempConstructionCost[currentLevel +1] +=  tempConstructionCost[currentLevel];
                tempInstallCost[currentLevel +1] += tempInstallCost[currentLevel];
                tempDeviceCost[currentLevel +1] += tempDeviceCost[currentLevel];
                tempOtherCost[currentLevel +1] += tempOtherCost[currentLevel];

                //存入上个节点
                previousLevel = level;

                double tempConstructionCost1 = tempConstructionCost[currentLevel];
                double tempInstallCost1=tempInstallCost[currentLevel];
                double tempDeviceCost1 = tempDeviceCost[currentLevel];
                double tempOtherCost1 = tempOtherCost[currentLevel];

                mc_node Mc_node1=new mc_node();
                Mc_node1.setId(id.trim());
                Mc_node1.setTitle(title);
                Mc_node1.setUnit(unit);
                Mc_node1.setsNumber(sNumber);
                Mc_node1.setQuantity(quantity);
                Mc_node1.setConstructionCost(tempConstructionCost1);
                Mc_node1.setInstallCost(tempInstallCost1);
                Mc_node1.setDeviceCost(tempDeviceCost1);
                Mc_node1.setOtherCost(tempOtherCost1);

                //更新节点
                mc_nodeDAO1.update(Mc_node1);
                int Now_index = jsonArray.length()-i;
                response.getWriter().write(String.valueOf(Now_index));
                //清空当前节点临时存储变量数据
                tempConstructionCost[currentLevel] = 0;
                tempInstallCost[currentLevel] = 0;
                tempDeviceCost[currentLevel] = 0;
                tempOtherCost[currentLevel] = 0;

                //如果有父节点未更新则对其父节点进行更新
                if(level==min){
                    updateParentNode(id.trim());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
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

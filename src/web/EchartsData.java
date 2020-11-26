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

@WebServlet( "/EchartsData")
public class EchartsData extends HttpServlet {
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
        String data ="";
        String ChatrsType = request.getParameter("type");
        if(ChatrsType.equals("line1")){
            data = getLline1Data();
        }
        if(ChatrsType.equals("pie")){
            data = getLpieData(request.getParameter("id"));
        }
        response.getWriter().write(data);
    }

    protected String getLline1Data(){
        String data ="[{";
        String xAxis ="xAxis:{data:[";
        String series ="{name:'指标',type:'line',data:[";
        String series1 ="{name:'工程费用占比',type:'line',data:[";
        String series2 ="{name:'二类费占比',type:'line',data:[";
        String lineInfo ="lineInfo:[";
        List<mc_node> listTemp=new ArrayList<>();
        listTemp = mc_nodeDAO1.selectChild("1");
        if(listTemp.size() !=0){
            for(int i =0;i<listTemp.size();i++) {
                mc_node s = (mc_node)listTemp.get(i);
                xAxis += "'"+ s.getTitle()+"',";
                double total = s.getConstructionCost()+s.getInstallCost()+s.getDeviceCost()+s.getOtherCost();
                double total3 = s.getConstructionCost()+s.getInstallCost()+s.getDeviceCost();
                double quota = total/s.getQuantity();
                double quota1 = total3/total;
                double quota2 = s.getOtherCost()/total;
                series += String.format("%.2f",quota)+ ",";
                series1 += String.format("%.2f",quota1)+ ",";
                series2 += String.format("%.2f",quota2)+ ",";
                lineInfo += "{id:'"+ s.getId() +"',title:'" + s.getTitle()+"'},";
            }
        }
        xAxis = xAxis.substring(0,xAxis.length()-1) + "]},";
        series = series.substring(0,series.length()-1) + "]},";
        series1 = series1.substring(0,series1.length()-1) + "]},";
        series2 = series2.substring(0,series2.length()-1) + "]}";
        lineInfo = lineInfo.substring(0,lineInfo.length()-1) + "]";
        data = data + xAxis + lineInfo + ",series:["+ series + series1 + series2 +"]}]";
        return  data;
    }

    protected String getLpieData(String id){
        String data ="[{data:[";
        mc_node tempMc_node = new mc_node();
        tempMc_node = mc_nodeDAO1.selectById(id);
        data +="{value:"+tempMc_node.getConstructionCost()+", name:'建筑工程费(万元)'},"+ "{value:"+tempMc_node.getInstallCost()+", name:'安装工程费(万元)'},"+
                "{value:"+tempMc_node.getDeviceCost()+", name:'设备购置费(万元)'},"+ "{value:"+tempMc_node.getOtherCost()+", name:'工程建设其他费用(万元)'}"+
        "],title:'"+ tempMc_node.getTitle()+"'}]";
        return  data;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }
}

package web;

import dao.mc_nodeDAO;
import entity.mc_node;
import poi.PoiExcel;
import test.TestRead;
import test.TestSrv;
import test.TestVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet( "/ReadExcel")
public class ReadExcel extends HttpServlet {

    mc_nodeDAO mc_nodeDAO1 = new mc_nodeDAO();
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
        TestSrv tSrv = new TestSrv();
        List<TestVo> testList = new ArrayList<TestVo>();
        String filePath = request.getParameter("url");
        try {
            testList =tSrv.readFromExcel(filePath);
            //判断导入数据的最大层级数
            int maxLevel = 0;
            for(int i =0;i<testList.size();i++){
                TestVo s = (TestVo)testList.get(i);
                int pointNum = countString(s.getsNumber(),".");
                if(pointNum > maxLevel){
                    maxLevel = pointNum;
                }
            }
            //新建数组存储父ID
            String[] parentIdList = new String[maxLevel+2];
            parentIdList[0] = "1";
            //将数据导入到数据库中
            for(int i =0;i<testList.size();i++){
                TestVo s = (TestVo)testList.get(i);
                int pointNum = countString(s.getsNumber(),".");
                //获取表内值得新对象
                mc_node temp_mc_node = new mc_node();
                String TempId = getUuid();//获取唯一的自动生成的id
                temp_mc_node.setId(TempId);
                parentIdList[pointNum+1] = TempId;//将自身的id放入，所有所属该类下级节点的变量中
                temp_mc_node.setParentId(parentIdList[pointNum]);
                temp_mc_node.setConstructionCost(s.getConstructionCost());
                temp_mc_node.setDeviceCost(s.getDeviceCost());
                temp_mc_node.setInstallCost(s.getInstallCost());
                temp_mc_node.setOtherCost(s.getOtherCost());
                temp_mc_node.setsNumber(s.getsNumber());
                temp_mc_node.setTitle(s.getTitle());
                temp_mc_node.setUnit(s.getUnit());
                temp_mc_node.setQuantity(s.getQuantity());
                mc_nodeDAO1.add(temp_mc_node);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.getWriter().write(data);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }

    //判断某个字符的个数
    public int countString(String str,String s) {
        int count = 0,len = str.length();
        while(str.indexOf(s) != -1) {
            str = str.substring(str.indexOf(s) + 1,str.length());
            count++;
        }
        return count;
    }

    //获取唯一id
    private String getUuid(){
       UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}

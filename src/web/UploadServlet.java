package web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.mc_nodeDAO;
import entity.mc_node;
import poi.PoiExcel;
import test.TestRead;
import test.TestSrv;
import test.TestVo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




/**
 * Servlet implementation class UploadServlet
 */

// 如果不配置 web.xml ，可以使用下面的代码
// @WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
    mc_nodeDAO mc_nodeDAO1 = new mc_nodeDAO();
    String data ="";

    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    /**
     * 上传数据及保存文件
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIRECTORY;


        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String filePath ="";

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        request.setAttribute("message",
                                "文件上传成功!");
                    }
                }
            }

            request.setCharacterEncoding("utf-8");
            // 设置响应编码
            response.setCharacterEncoding("utf-8");
            // 设置响应内容类型
            response.setContentType("text/html;charset=utf-8");
            // 获取请求参数
            // 响应内容传递数据给Ajax的回调函数data
            TestSrv tSrv = new TestSrv();
            List<TestVo> testList = new ArrayList<TestVo>();
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

        } catch (Exception ex) {
            request.setAttribute("message",
                    "错误信息: " + ex.getMessage());
        }
        deleteFile(filePath);
//         跳转到 message.jsp
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 调用dopost方法、达到代码复用
        this.doPost( request,  response);
    }

    //删除文件
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import poi.PoiExcel;


public class TestSrv {
	
	private static Log log = LogFactory.getLog("service.log");
	
	//导出表
	public void exportCsglDm(String outFilePath,List<TestVo> testList){
		TestExport export = new TestExport(null,outFilePath,"/poi-config.xml","测试模版.xlsx");
		
		//初始化文件
		try {
			export.init();
		} catch (IOException e1) {
			log.error("初始化输出文件失败",e1);
			return;
		}
		
		//初始化写入表信息
		if(!export.initSheet(0)){
			log.error("初始化表信息失败");
			return;
		}
		
		//写入表头
		export.writeHeader();
		
		for(int i=0;i<testList.size();i++){
			TestVo test = testList.get(i);
			export.writeRowData(i+1, test,false);
		}
		
		//写入输出流
		export.writeToOutputStream();
		
		export.closeOutputStream();
	}
	
	//从excel中读取数据并保存到list中
	public List<TestVo> readFromExcel(String path) throws Exception{
		List<TestVo> testList = new ArrayList<TestVo>();
		
		TestRead tRead = new TestRead();
		tRead.setCheckHeader(true); 	//需要效验表头是否一致
		tRead.setTestList(testList);	//将list传进去用于保存值
		
		//路径，行处理类，模式，分隔符，模版名称
		PoiExcel pe = new PoiExcel(path,tRead,PoiExcel.USERMODEL,"|||","测试模版.xlsx");
		pe.read();
		
		return testList;
	}
	
	public static void main(String[] args) throws Exception{
		//先读再写
		TestSrv tSrv = new TestSrv();
		tSrv.exportCsglDm("e:/测试导出.xlsx", tSrv.readFromExcel("e:/测试导入.xlsx"));
	}
	
}

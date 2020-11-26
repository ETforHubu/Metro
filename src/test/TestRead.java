package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import poi.IRowReader;
import poi.MyCell;
import poi.MySheet;
import poi.PoiExcel;
import util.ComUtil;

/**
 * 
 * 读完excel一行数据后的处理操作类
 * 可在此处进行一行数据的导入数据库等操作，本演示是将数据保存在了list中
 *
 */
public class TestRead implements IRowReader{

	private static Log log = LogFactory.getLog("service.log");
	private boolean isCheckHeader = false;	//是否进行表头效验，默认关闭
	private boolean isForcedInterrupt = false;	//强制中断读取指令，默认不中断
	private List<TestVo> testList = new ArrayList<TestVo>();
	
	private Map<String,String> session = new HashMap<String, String>();	//导入结果信息存放session
	//原始值均为false
	public TestRead(){
		isCheckHeader = true;
		isForcedInterrupt = false;
	}
	
	@Override
	public void processLine(int sheetIndex, String sheetname, int rowIndex,
			String row, MySheet currSheet,int[] TitleSequence) {
		// TODO Auto-generated method stub
		if(ComUtil.isNull(row) || "null".equalsIgnoreCase(row)){
			log.debug("获取第" + (rowIndex+1) + "行内容为空，不进行该行数据处理");
			return;
		}
		
		if(currSheet == null){
			log.debug("表"+ sheetname + "配置文件获取失败，不进行该行数据导入处理");
			return;
		}

		
		String[] cellData = row.split(ComUtil.converSpecialCharater(PoiExcel.EXCEL_LINE_DELIMITER));
		TestVo test = new TestVo();
		//判断第一次获取的表头字符串信息
			for(int i = 0;i<cellData.length;i++){
				if(TitleSequence[i]==0){
					test.setsNumber(cellData[i]);
				}
				if(TitleSequence[i]==1){
					test.setTitle(cellData[i]);
				}
				if(TitleSequence[i]==2){
					test.setUnit(cellData[i]);
				}
				if(TitleSequence[i]==3){
					if(cellData[i] ==""){
						test.setConstructionCost(0);
					}else{
						test.setConstructionCost(Double.valueOf(cellData[i]));
					}
				}
				if(TitleSequence[i]==4){
					if(cellData[i].length() ==0){
						test.setInstallCost(0);
					}else{
						test.setInstallCost(Double.valueOf(cellData[i]));
					}
				}
				if(TitleSequence[i]==5){
					if(cellData[i].length() ==0){
						test.setDeviceCost(0);
					}else{
						test.setDeviceCost(Double.valueOf(cellData[i]));
					}
				}
				if(TitleSequence[i]==6){
					if(cellData[i].length() ==0){
						test.setOtherCost(0);
					}else{
						test.setOtherCost(Double.valueOf(cellData[i]));
					}
				}
				if(TitleSequence[i]==8){
					if(cellData[i].length() ==0){
						test.setQuantity(0);
					}else{
						test.setQuantity(Double.valueOf(cellData[i]));
					}
				}
			}
//			test.setTitle(cellData[0]);
//			test.setUnit(cellData[1]);
//		    test.setsNumber(cellData[2]);
//			test.setConstructionCost(Double.valueOf(cellData[3]));
//			test.setInstallCost(Double.valueOf(cellData[4]));
//			test.setDeviceCost(Double.valueOf(cellData[5]));
//			test.setOtherCost(Double.valueOf(cellData[6]));
//			test.setQuantity(Double.valueOf(cellData[7]));
//			test.setQuota(Double.valueOf(cellData[8]));
//		Map<String,MyCell> cell = currSheet.getCell();
//		if(isCheckHeader && rowIndex == currSheet.getSkipRow()){//以配置文件中默认跳过行为准
//			if(cell.size() > cellData.length){
//				log.info("表头校验不一致，终止读取该表！");
//				isForcedInterrupt = true;
//				return;
//			}else{
//				for(int i = 0; i < cellData.length; i++){
//					if(!cell.containsKey(String.valueOf(i))){
//						//跳过该单元格
//						continue;
//					}
//					if(!cell.get(String.valueOf(i)).getHeaderName().equals(cellData[i])){
//						log.info("表头校验不一致，终止读取该表！");
//						isForcedInterrupt = true;
//						return;
//					}
//				}
//				log.debug("表头校验成功");
//				isForcedInterrupt = false;
//				return;
//			}
//		}
		
//		TestVo test = new TestVo();
		
		//反射机制获取值
//		for(int i = 0; i < cellData.length; i++){
//			if(cell == null || !cell.containsKey(String.valueOf(i))){
//				continue;
//			}
//			String column = cell.get(String.valueOf(i)).getCellName();
//			if(ComUtil.isNull(column)){
//				//无对应映射关系则跳过
//				continue;
//			}
//			String field = column.substring(0, 1).toUpperCase() + column.substring(1);
//			try {
//				Method m = test.getClass().getMethod("set" + field, String.class);
//				m.invoke(test, cellData[i]);
//			} catch (NoSuchMethodException | SecurityException e) {
//				continue;
//			}catch (IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException e) {
//				continue;
//			}
//		}
		
		//数据效验
		if(!checkData(test)){
			log.info("第" + (rowIndex+1) + "行数据效验失败！" );
			return;
		}
		
		//将值保存进list
		testList.add(test);
	}

	@Override
	public boolean isForcedInterrupt() {
		// TODO Auto-generated method stub
		return isForcedInterrupt;
	}

	public boolean checkData(TestVo test){
		boolean rs = true;
		
		return rs;
	}
	
	public boolean isCheckHeader() {
		return isCheckHeader;
	}

	public void setCheckHeader(boolean isCheckHeader) {
		this.isCheckHeader = isCheckHeader;
	}

	public Map<String, String> getSession() {
		return session;
	}

	public void setSession(Map<String, String> session) {
		this.session = session;
	}

	public void setForcedInterrupt(boolean isForcedInterrupt) {
		this.isForcedInterrupt = isForcedInterrupt;
	}
	
	public List<TestVo> getTestList() {
		return testList;
	}

	public void setTestList(List<TestVo> testList) {
		this.testList = testList;
	}
}

package poi;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import util.ComUtil;

/**
 * 自定义配置文件解析器
 * @author snow
 * @version 1.0
 */
public class PoiConfigParse {

	private static Log log = LogFactory.getLog("service.log");
	private Document document;
	private String cfgPath;
	
	public PoiConfigParse(){
		this.cfgPath = "/poi-config.xml";
	}
	
	public PoiConfigParse(String cfgPath){
		if(!ComUtil.isNull(cfgPath)){
			this.cfgPath = cfgPath;
		}else{
			this.cfgPath = "/poi-config.xml";
		}
	}
	
	//根据文件名获取对应文件中的excel配置解析模版
	//返回该文件下表的所有信息
	@SuppressWarnings("unchecked")
	public Map<String,MySheet> getConfigByName(String fileName){
		Map<String,MySheet> sheetMap = new HashMap<String,MySheet>();
		
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		try {
			InputStream is = this.getClass().getResourceAsStream(cfgPath);
			document = saxReader.read(is);
			is.close();
		} catch (DocumentException e) {
			log.error("加载xml配置文件" + cfgPath + "失败：",e);
			return null;
		} catch (IOException e) {
			log.error("打开xml配置文件" + cfgPath + "失败：",e);
			return null;
		}
		
		//开始解析
		Element root = document.getRootElement();
		//获取文件列表
		List<Element> fileList = root.elements("file");
		for(Element file:fileList){
			if(file.attributeValue("name").equals(fileName)){
				//获取所需文件的表列表
				List<Element> sheetList = file.elements("sheet");
				for(Element sheet:sheetList){
					MySheet sheetInfo  = new MySheet();
					List<Element> cellList = sheet.elements("cell");
					//获取cell模版
					Map<String,MyCell> cellMap = new HashMap<String,MyCell>();
					for(Element cell:cellList){
						Element column = cell.element("column");
						MyCell cellInfo = new MyCell();
						cellInfo.setHeaderName(cell.attributeValue("name"));
						cellInfo.setCellName(column.attributeValue("name"));
						try{
							cellInfo.setCol(Integer.parseInt(cell.attributeValue("colnum")));
						}catch(Exception e){
							cellInfo.setCol(-1);
						}
						cellInfo.setColumnType(column.attributeValue("datatype"));
						
						//加入cellMap中 键值为colnum
						cellMap.put(cell.attributeValue("colnum"), cellInfo);	
					}
					sheetInfo.setSheetName(sheet.attributeValue("name"));
					try{
						sheetInfo.setSheetIndex(Integer.parseInt(sheet.attributeValue("index")));
					}catch(Exception e){
						sheetInfo.setSheetIndex(0);
					}
					try{
						sheetInfo.setSkipCol(Integer.parseInt(sheet.attributeValue("skipcol")));
					}catch(Exception e){
						sheetInfo.setSkipCol(0);
					}
					try{
						sheetInfo.setSkipRow(Integer.parseInt(sheet.attributeValue("skiprow")));
					}catch(Exception e){
						sheetInfo.setSkipRow(1);
					}
					sheetInfo.setIsUsed(sheet.attributeValue("isused"));
					sheetInfo.setTableName(sheet.attributeValue("table"));
					sheetInfo.setCell(new HashMap<String,MyCell>(cellMap));
					//加入sheetMap中 键值为sheetIndex
					sheetMap.put(sheet.attributeValue("index"), sheetInfo);
				}
			}
		}
		return sheetMap;
	}
	
	public static void main(String[] args) {
		
	}
}

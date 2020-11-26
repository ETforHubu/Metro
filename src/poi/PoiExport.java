package poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import util.ComUtil;

/**
 * poi处理excle工具类
 * 通用写入excle工具
 * 两种写入方式  03使用HSSF用户模式写入  07使用的sxssf的写入技术  能够支持大数据量写入
 * 
 * @version poi_3.10.1
 */
public class PoiExport {

private static Log log = LogFactory.getLog("service.log");
	
	private Map<String,MySheet> sheetMap;	//读取文件所有表映射配置信息
	protected MySheet currSheet;		//当前表配置信息
	private String configMapFileName = null;	//配置文件名称
	
	private String outputFile; //输出的路径加文件名
	private String outputFileName; //输出的文件名
	private String outputFileType; //输出的文件类型（03或07）
	
	protected Map<String, CellStyle> styles;	//单元格格式模版
	protected Map<String, MyCell> dataCell;	//数据单元格模版
	
	private Workbook  workbook;
	private Sheet sheet;
	private int currrow = 0;	//当前行
	private int startrow = 0;	//起始行
	private int startcol = 0;	//起始列
	private OutputStream outs;
	private String cfgPath;	//配置映射文件路径
	
	public PoiExport(String outputFile){
		this.outputFile = outputFile;
	}
	
	public PoiExport(String outputFile,String configMapFileName){
		this.outputFile = outputFile;
		this.configMapFileName = configMapFileName;
	}
	
	public PoiExport(OutputStream outs,String outputFile,String configMapFileName){
		this.outs = outs;
		this.outputFile = outputFile;
		this.configMapFileName = configMapFileName;
	}
	
	public PoiExport(OutputStream outs,String outputFile,String cfgPath,String configMapFileName){
		this.outs = outs;
		this.outputFile = outputFile;
		this.configMapFileName = configMapFileName;
		this.cfgPath = cfgPath;
	}
	
	
	//初始化输出文件信息
	public void init() throws IOException{
		//判断参数是否为空或者没有意义
		if(outputFile == null || outputFile.trim().equals("")){
			throw new IOException("未指定需输出的文件路径！");
		}
		
		if(outs == null){
			//创建输出流
			outs = new FileOutputStream(outputFile);
		}

		this.outputFileName = outputFile.substring(outputFile.lastIndexOf("/")+1);
		this.outputFileType = outputFile.substring(outputFile.lastIndexOf(".")+1);
		

		//根据文件类型创建工作簿
		if(this.outputFileType.equalsIgnoreCase("xls")){
			workbook = new HSSFWorkbook();
		}else if(this.outputFileType.equalsIgnoreCase("xlsx")){
			workbook = new SXSSFWorkbook();
		}
		
		//加载参数映射配置文件
		PoiConfigParse pcp = new PoiConfigParse(cfgPath);
		if(configMapFileName != null){
			sheetMap = pcp.getConfigByName(this.configMapFileName);
		}else{
			sheetMap = pcp.getConfigByName(outputFileName);
		}
		
		if(sheetMap == null){
			throw new IOException("未找到指定的配置文件！");
		}
	}
	
	//初始化表信息
	public boolean initSheet(int sheetIndex){
		
		//加载指定表的配置信息
		if(!loadSheetConfig(sheetIndex,null)){
			log.error("表配置模版信息加载失败");
			return false;
		}
		
		sheet = workbook.createSheet(currSheet.getSheetName());
		dataCell = currSheet.getCell();
		
		//加载单元格格式
		styles = createStyles(workbook);
		//加载数据区格式
		loadTemplate(sheetIndex);
		
		//单元格默认设置
		this.setDefultCellStyle();
		
		//设置起始行列
		this.startrow = this.currSheet.getSkipRow();
		this.startcol = this.currSheet.getSkipCol();
		this.currrow = this.startrow;
		
		return true;
	}
	
	//设置标题
	public void writeTitle(String title){
		Row titleRow = sheet.createRow(currrow++);
		titleRow.setHeightInPoints(30);
		Cell titleCell = titleRow.createCell(startcol);
		
		titleCell.setCellType(Cell.CELL_TYPE_STRING);
		titleCell.setCellValue(title);
        titleCell.setCellStyle(styles.get("titleStyle"));
        sheet.addMergedRegion(new CellRangeAddress(currrow-1, currrow-1, startcol, dataCell.size()-1));
	}
	
	//设置表头
	public void writeHeader(){
  		Row headerRow = sheet.createRow(currrow++);
  		for(int i = 0; i < this.dataCell.size(); i++){
			Cell headercell = headerRow.createCell(startcol + i);
			headercell.setCellType(Cell.CELL_TYPE_STRING);
			headercell.setCellValue(this.dataCell.get(String.valueOf(i)).getHeaderName());
			headercell.setCellStyle(styles.get("headerStyle"));
		}
	}
	
	//输出一行数据
	public void writeRowData(int rowIndex,Object obj,boolean hasRowIndex){
		Row dataRow = sheet.createRow(currrow++);
		for(int i = 0; i < this.dataCell.size(); i++){
			Cell cell = dataRow.createCell(startcol + i);
			
			//第一列为序号
			if(i == 0 && hasRowIndex){
				cell.setCellValue(String.valueOf(rowIndex));
				cell.setCellStyle(dataCell.get(String.valueOf(i)).getCellStyle());
				continue;
			}
			
			String cellvalue = String.valueOf(this.getData(dataCell.get(String.valueOf(i)).getCellName(), obj));
			if(Cell.CELL_TYPE_NUMERIC == dataCell.get(String.valueOf(i)).getCellType()){
				cell.setCellValue(ComUtil.strToFloat(cellvalue));
			}else{
				cell.setCellValue(cellvalue);
			}
			cell.setCellType(dataCell.get(String.valueOf(i)).getCellType());
			cell.setCellStyle(dataCell.get(String.valueOf(i)).getCellStyle());
		}
	}
	
	//反射机制根据数据模版匹配对应属性的值
	public Object getData(String column,Object obj){
		Object value = "";
		if(ComUtil.isNull(column)){
			//对应映射列名为空时返回空
			return value;
		}
		
		String field = column.substring(0, 1).toUpperCase() + column.substring(1);
		try {
			Method m = obj.getClass().getMethod("get" + field);
			value = m.invoke(obj);
		} catch (NoSuchMethodException | SecurityException e) {
			value = "";
		}catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			value = "";
		}
		return value;
	}
	
	//单元格默认设置
	public void setDefultCellStyle(){
		for(int i = 0; i <dataCell.size(); i++){
			sheet.setColumnWidth(i,dataCell.get(String.valueOf(i)).getCellWidth());
			sheet.setDefaultColumnStyle(i, dataCell.get(String.valueOf(i)).getCellStyle());
		}
	}
	
	//加载映射配置模版
	public boolean loadSheetConfig(int sheetIndex,String sheetName){
		boolean rs = false;

		if(sheetMap.containsKey(String.valueOf(sheetIndex))){
			currSheet = sheetMap.get(String.valueOf(sheetIndex));
			if(currSheet == null){
				log.error("未获取到第" + sheetIndex + "张表<" + sheetName + ">配置，跳过\n");
				return false;
			}
			if("0".equals(currSheet.getIsUsed())){
				log.error("第" + sheetIndex + "张表<" + sheetName + ">设置为不处理，跳过\n");
				return false;
			}
			
			rs = true;
		}else{
			log.error("未获取到第" + sheetIndex + "张表<" + sheetName + ">配置，跳过\n");
			return false;
		}
		return rs;	
	}
	
	public void writeToOutputStream(){
		//输出到输出流
		try {
			workbook.write(outs);
			log.info("写入完成！");
		} catch (IOException e) {
			log.error("写入失败：",e);
			//break;
		}
	}
	
	public void closeOutputStream(){
		try {
			outs.flush();
			outs.close();
		} catch (IOException e) {
			log.error("关闭输出流失败：",e);
		}
	}
	
	public Map<String, CellStyle> createStyles(Workbook wb){
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		CellStyle style;
		DataFormat format= wb.createDataFormat();
		
		Font titleFont = wb.createFont();
		titleFont.setFontName("宋体");
		titleFont.setFontHeightInPoints((short)9);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(titleFont);
		styles.put("titleStyle", style);
		
		Font headerFont = wb.createFont();
		headerFont.setFontName("宋体");
		headerFont.setFontHeightInPoints((short)9);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(headerFont);
		styles.put("headerStyle", style);
		
		//数据区格式
		Font dataFont = wb.createFont();
		dataFont.setFontName("宋体");
		dataFont.setFontHeightInPoints((short)9);
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setFont(dataFont);
		styles.put("dataStyle", style);
		
		//数据区格式1
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setFont(dataFont);
		styles.put("dataStyle1", style);
		
		//数据区格式2
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setFont(dataFont);
		styles.put("dataStyle2", style);
		
		//数据区格式3
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(dataFont);
		styles.put("dataStyle3", style);
		
		//数据区格式4
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(dataFont);
		styles.put("dataStyle4", style);
		
		//数据区格式5
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(dataFont);
		style.setDataFormat(format.getFormat("#0"));
		styles.put("dataStyle5", style);
		
		//数据区格式6
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(dataFont);
		style.setDataFormat(format.getFormat("#0.00"));
		styles.put("dataStyle6", style);
		
		return styles;
	}
	
	//加载数据单元格格式模版
	public void loadTemplate(int sheetIndex){
		//使用默认单元格格式、数据类型字符串、宽度256*10、高度13
		//若需修改请重写该方法
		for(int i = 0; i <dataCell.size(); i++){
			dataCell.get(String.valueOf(i)).setMyCell(styles.get("dataStyle"),Cell.CELL_TYPE_STRING,256*10,13);
		}
	};

	
}

package poi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import util.ComUtil;

/**
 * poi处理excel工具类   
 * 支持2种解析模式进行读取03或07格式的excel
 * 用户模式  USERMODEL  03和07都适用 请注意勿进行大文件读取 很容易内存不足
 * 事件驱动模式  EVENTMODEL  暂只支持07的   解析速度比用户模式快 且支持大数据量的读取
 * 
 * 需重写processLine方法
 * @author snow
 * @version 20150303_v1.0.1
 * @version poi_3.10.1
 * 
 */
public class PoiExcel extends DefaultHandler{

	private static Log log = LogFactory.getLog("service.log");
	public static final int USERMODEL = 0;
	public static final int EVENTMODEL = 1;
	public static final int SXSSF = 2;

	private int model = 0;
	
	private String inputFile; //输入的路径加文件名
	private String inputFileName; //输入的文件名
	private String inputFileType; //输入的文件类型（03或07）
	private String configMapFileName = null;	//配置文件名称
	
	private InputStream is = null;
	private HSSFWorkbook workbook = null;		//xls工作簿对象
	private XSSFWorkbook xssworkbook = null;	//xlsx工作簿对象
	
	private int currSheetIndex = 0;	//当前表序列号
	private String currSheetName;		//当前表名
	private int numOfSheets;			//文件包含表的数量
	
	private Map<String,MySheet> sheetMap = new HashMap<String,MySheet>();	//读取文件所有表映射配置信息
	private MySheet currSheet = new MySheet();		//当前表配置信息
	
	private IRowReader rowReader;	//行处理类接口
	
	private String cfgPath;	//配置映射文件路径
	
	public static String EXCEL_LINE_DELIMITER = ",";	//自定义每行单元格之间的分割符
	
	public PoiExcel(String inputfile,IRowReader rowReader){
		//默认使用usermodel
		this.model = 0;
		this.inputFile = inputfile;
		this.rowReader = rowReader;
	}
	
	public PoiExcel(String inputfile, IRowReader rowReader,int model){
		this.model = model;
		this.inputFile = inputfile;
		this.rowReader = rowReader;
	}
	
	public PoiExcel(String inputfile, IRowReader rowReader,int model,String EXCEL_LINE_DELIMITER){
		this.model = model;
		this.inputFile = inputfile;
		this.rowReader = rowReader;
		PoiExcel.EXCEL_LINE_DELIMITER = EXCEL_LINE_DELIMITER;
	}
	
	public PoiExcel(String inputfile, IRowReader rowReader,int model,String EXCEL_LINE_DELIMITER,String configMapFileName){
		this.model = model;
		this.inputFile = inputfile;
		this.rowReader = rowReader;
		PoiExcel.EXCEL_LINE_DELIMITER = EXCEL_LINE_DELIMITER;
		this.configMapFileName = configMapFileName;
	}
	
	public PoiExcel(String inputfile, IRowReader rowReader,int model,String EXCEL_LINE_DELIMITER,String cfgPath,String configMapFileName){
		this.model = model;
		this.inputFile = inputfile;
		this.rowReader = rowReader;
		PoiExcel.EXCEL_LINE_DELIMITER = EXCEL_LINE_DELIMITER;
		this.cfgPath = cfgPath;
		this.configMapFileName = configMapFileName;
	}
	
	public void read() throws Exception{
		//判断参数是否为空或者没有意义
		if(inputFile == null || inputFile.trim().equals("")){
			throw new IOException("未指定需打开的文件路径！");
		}
		this.inputFileName = inputFile.substring(inputFile.lastIndexOf("/")+1);
		this.inputFileType = inputFile.substring(inputFile.lastIndexOf(".")+1);
		log.info("准备处理的文件路径为：" + inputFile);
		log.info("准备处理的文件名为：" + inputFileName);
		log.info("准备处理的文件类型为：" + inputFileType);
		
		//加载参数映射配置文件
		PoiConfigParse pcp = new PoiConfigParse(cfgPath);
		if(configMapFileName != null){
			sheetMap = pcp.getConfigByName(this.configMapFileName);
		}else{
			sheetMap = pcp.getConfigByName(inputFileName);
		}
		
		//根据文件类型选择
		if(this.inputFileType.equalsIgnoreCase("xls")){
			if(this.model == USERMODEL){
				is = new FileInputStream(inputFile);
				workbook = new HSSFWorkbook(is);
				numOfSheets = workbook.getNumberOfSheets();
				currSheetIndex = 0;
				is.close();
				this.readXsl();
			}else if(this.model == EVENTMODEL){
				//暂未开发,后续完善
				log.info("当前暂不支持03的事件驱动模式");
			}
		}else if(this.inputFileType.equalsIgnoreCase("xlsx")){
			if(this.model == USERMODEL){
				is = new FileInputStream(inputFile);
				xssworkbook = new XSSFWorkbook(is);
				numOfSheets = xssworkbook.getNumberOfSheets();
				currSheetIndex = 0;
				is.close();
				this.readXlsx();
			}else if(this.model == EVENTMODEL){
				processAllSheets(inputFile);
			}
		}else{
			throw new Exception("不支持的文件类型");
		}
	}
	
	//-------------------2003处理------------------------
	
	//读取2003文件中的数据
	private void readXsl(){
		int dwk = 0; //循环了五十次都为空，那么考虑跳出
		
		while(currSheetIndex<numOfSheets){
			HSSFSheet sheet =workbook.getSheetAt(currSheetIndex);
			
			currSheetName = sheet.getSheetName();
			log.info("开始处理第" + (currSheetIndex+1) + "张表<" + currSheetName + ">");
		
			//检测是否存在配置文件
			if(!loadSheetConfig(currSheetIndex, currSheetName)){
				currSheetIndex++;
				continue;
			}
			
			//判断当前行是否到当前sheet的结尾。
			int num = sheet.getLastRowNum()+1;//获取表中实际有的行数
			int count = num/50;
			int ys = num%50;
			if(ys != 0){
				count++;
			}
			
			int begin = -50; //开始循环位置
			int end = 0;//中止循环为止

			//获取表头的数据并进行判断
			String tempTitle = getLineXls(sheet,0);
			String[] tempTitleData = tempTitle.split(ComUtil.converSpecialCharater(PoiExcel.EXCEL_LINE_DELIMITER));
			int[] tempTitleSequence = new int[tempTitleData.length];
			for(int b =0;b<tempTitleData.length;b++){
				tempTitleSequence[b] = getSequence(tempTitleData[b]);
			}

			for(int i = 0; i < count; i++){
				if(i==0){
					begin += this.currSheet.getSkipRow();//默认每个excel表单的第一行不读信息。从第二行开始。
				}
				begin += 50;
				end = begin + 50;
				if(end > num){
					end = num;
				}
				
				dwk = 0;
				for(int row = begin; row < end; row++){
					String temp = null;
					try{
						log.info("开始读取第" + (row+1) + "行！");
						temp = getLineXls(sheet,row);
						log.info("第" + (row+1) + "行内容：" + temp);
					}catch(Exception e){
						//捕获异常
						log.error("读取第" + (row+1) + "行出错:",e);
						log.error("------------------");
						//继续读取下行
						continue;
					}
					
					if(temp.equalsIgnoreCase("null")){
						dwk++;
						continue;
					}else{
						//获得数据
						try{
							log.info("正在解析第" + (row+1) + "行" + temp);
							rowReader.processLine(currSheetIndex,currSheetName,row,temp,this.currSheet,tempTitleSequence);
							log.info("------------------");
							if(rowReader.isForcedInterrupt()){
								log.info("解析到强制中断指令，停止读取文件！");
								return;
							}
						}catch(Exception e){
							//捕获异常
							log.error("解析第" + (row+1) + "行(" + temp + ")出错:",e);
							log.error("------------------");
							//继续读取下行
							continue;
						}
					}
				}
				if(dwk==50){
					//出现50行空行时直接跳出循环结束读取操作
					log.info("连续50行空行，判定该表数据为空，结束本表操作");
					break;
				}
			}
			log.info("表<" + currSheetName + ">处理完成！");
			currSheetIndex++;
		}
	}
	
	/**
	 * 读取excel2003一行数据
	 * @param sheet 表
	 * @param row	行号
	 * @return		以3竖线分隔的处理过的行数据   例：xx|||xx|||xx|||xx
	 */
	private String getLineXls(HSSFSheet sheet,int row){
		HSSFRow hrow = sheet.getRow(row);
		StringBuffer sb=new StringBuffer();	
		String temp = null;
		int nullCount = 0;
		if(hrow == null){
			log.info(" 第" + (row+1) + "行获取失败内容为null");
			return "null";
		}
		//int num = hrow.getPhysicalNumberOfCells();//使用这个方法会导致有空列时不能正确获取列数
		int num = hrow.getLastCellNum();
		for(int i = 0; i < num; i++){
			if(hrow.getCell(i) != null){
				switch(hrow.getCell(i).getCellType()){
					case 0:	//数字类型
						if(HSSFDateUtil.isCellDateFormatted(hrow.getCell(i))){
							Date d = hrow.getCell(i).getDateCellValue();  
						    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						    temp = formater.format(d);
						}else{
							DecimalFormat df = new DecimalFormat("#.##");  
							temp = df.format(hrow.getCell(i).getNumericCellValue());
						}	
						break;
					case 1:	//字符串类型
						temp = hrow.getCell(i).getStringCellValue();
						break;
					case 2: //公式型
						if(HSSFDateUtil.isCellDateFormatted(hrow.getCell(i))){
							Date d = hrow.getCell(i).getDateCellValue();  
						    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						    temp = formater.format(d);
						}else{
							DecimalFormat df = new DecimalFormat("#.##");  
							temp = df.format(hrow.getCell(i).getNumericCellValue());
						}
						break;
					case 3: //空值
						temp = "";
						nullCount++;
						break;
					case 4: //布尔型
						//暂不处理
						break;
					case 5: //错误
						//暂不处理
						break;	
					default:
		          		//do nothing
		          		break;  
				}
			}else{
				temp = "";
				nullCount++;
			}
			
			if(i == num-1){
				sb.append(temp);
			}else{
				sb.append(temp).append(EXCEL_LINE_DELIMITER);
			}
		}
    
		//如果所有单元格都为空值，跳过该行
		if(nullCount == num){
			return "null";
		}
   	    return sb.toString();	   
	}
	
	//-------------------2007处理------------------------
	
	//读取excel2007
	private void readXlsx() throws SQLException{
		int dwk = 0; //循环了五十次都为空，那么考虑跳出
		
		while(currSheetIndex<numOfSheets){
			XSSFSheet sheet = xssworkbook.getSheetAt(currSheetIndex);
			
			currSheetName = sheet.getSheetName();
			log.info("开始处理第" + (currSheetIndex+1) + "张表<" + currSheetName + ">");
			
			//检测是否存在配置文件
			if(!loadSheetConfig(currSheetIndex, currSheetName)){
				currSheetIndex++;
				continue;
			}
			
			//判断当前行是否到当前sheet的结尾。
			int num = sheet.getLastRowNum()+1;//获取表中实际有的行数
			int count = num/50;
			int ys = num%50;
			if(ys != 0){
				count++;
			}
			
			int begin = -50; //开始循环位置
			int end = 0;;//中止循环为止

			//获取表头的数据并进行判断
			String tempTitle = getLineXlsx(sheet,0);
			String[] tempTitleData = tempTitle.split(ComUtil.converSpecialCharater(PoiExcel.EXCEL_LINE_DELIMITER));
			int[] tempTitleSequence = new int[tempTitleData.length];
			for(int b =0;b<tempTitleData.length;b++){
				tempTitleSequence[b] = getSequence(tempTitleData[b]);
			}
			
			for(int i = 0; i < count; i++){
				if(i==0){
					begin += this.currSheet.getSkipRow();//默认每个excel表单的第一行不读信息。从第二行开始。
				}
				begin += 50;
				end = begin + 50;
				if(end > num){
					end = num;
				}
				
				dwk = 0;
				for(int row = begin; row < end; row++){
					String temp = null;
					try{
						log.info("开始读取第" + (row+1) + "行！");
						temp = getLineXlsx(sheet,row);
						log.info("第" + (row+1) + "行内容：" + temp);
					}catch(Exception e){
						//捕获异常
						log.error("读取第" + (row+1) + "行出错:",e);
						log.error("------------------");
						//继续读取下行
						continue;
					}
					if(temp.equalsIgnoreCase("null")){
						dwk++;
						continue;
					}else{
						//获得数据
						try{
							log.info("正在解析第" + (row+1) + "行：" + temp);
							rowReader.processLine(currSheetIndex,currSheetName,row,temp,this.currSheet,tempTitleSequence);
							log.info("------------------");
							if(rowReader.isForcedInterrupt()){
								log.info("解析到强制中断指令，停止读取文件！");
								return;
							}
						}catch(Exception e){
							//捕获异常
							log.error("解析第" + (row+1) + "行(" + temp + ")出错:",e);
							log.error("------------------");
							//继续读取下行
							continue;
						}
					}
				}
				if(dwk==50){
					//出现50行空行时直接跳出循环结束读取操作
					log.info("连续50行空行，判定该表数据为空，结束本表操作");
					break;
				}	
			}
			log.info("表<" + currSheetName + ">处理完成！");
			currSheetIndex++;
		}
	}

	private int getSequence( String name ){
		switch(name){
			case "编号" :
				//语句
				return 0;
			case "工程及费用名称" :
				return 1;
			case "单位" :
				return 2;
			case "建筑工程费" :
			case "建筑工程费（万元）" :
				return 3;
			case "安装工程费" :
			case "安装工程费（万元）" :
				return 4;
			case "设备购置费" :
			case "设备购置费（万元）" :
				return 5;
			case "工程建设其他费用" :
			case "工程建设其他费用（万元）" :
				return 6;
			case "合价" :
			case "合价（万元）" :
				return 7;
			case "数量" :
				return 8;
			case "指标" :
			case "指标（万元）" :
				return 9;
			default : //可选
				return -1;
		}

	}
	
	/**
	 * 读取excel2007一行数据
	 * @param sheet	表
	 * @param row	行号
	 * @return		以3竖线分隔的处理过的行数据   例：xx|||xx|||xx|||xx
	 */
	private String getLineXlsx(XSSFSheet sheet,int row){
		XSSFRow xssrow = sheet.getRow(row);
		StringBuffer sb=new StringBuffer();	
		String temp = null;
		int nullCount = 0;
		if(xssrow == null){
			log.info(" 第" + (row+1) + "行获取失败内容为null");
			return "null";
		}
		//int num = xssrow.getPhysicalNumberOfCells();//使用这个方法会导致有空列时不能正确获取列数
		int num = xssrow.getLastCellNum();
		for(int i = 0; i < num; i++){
			if(xssrow.getCell(i) != null){
				switch(xssrow.getCell(i).getCellType()){
					case 0:	//数字类型
						if(HSSFDateUtil.isCellDateFormatted(xssrow.getCell(i))){
							Date d = xssrow.getCell(i).getDateCellValue();  
						    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						    temp = formater.format(d);
						}else{
							DecimalFormat df = new DecimalFormat("#.##");  
							temp = df.format(xssrow.getCell(i).getNumericCellValue());
						}
						break;
					case 1:	//字符串类型
						temp = xssrow.getCell(i).getStringCellValue();
						break;
					case 2: //公式型
						if(HSSFDateUtil.isCellDateFormatted(xssrow.getCell(i))){
							Date d = xssrow.getCell(i).getDateCellValue();  
						    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						    temp = formater.format(d);
						}else{
							DecimalFormat df = new DecimalFormat("#.##");  
							temp = df.format(xssrow.getCell(i).getNumericCellValue());
						}
						break;	
					case 3: //空值
						temp = "";
						nullCount++;
						break;
					case 4: //布尔型
						//暂不处理
						break;
					case 5: //错误
						//暂不处理
						break;	
					default:
		          		//do nothing
		          		break;  
				}
			}else{
				temp = "";
				nullCount++;
			}
			
			if(i == num-1){
				sb.append(temp);
			}else{
				sb.append(temp).append(EXCEL_LINE_DELIMITER);
			}
		}
		
		//如果所有单元格都为空值，跳过该行
		if(nullCount == num){
			return "null";
		}
   	    return sb.toString();	   
	}
	
	//-------------------事件驱动模式------------------------
	 
	private enum XSSFDataType {  
		BOOL,
        ERROR,
        FORMULA,
        INLINESTR,
        SSTINDEX,
        NUMBER,   
    } 
	
	private StylesTable stylesTable;	//表格式表
	private SharedStringsTable sst;		//共享字符串表
	private int rowIndex; 			//当前行
	private int colIndex;			//当前列
	//private List<Object> row;			//行集合
	private Map<Integer,Object> row;    //行集合
	private boolean nextIsString;		//字符串标识
	private XSSFDataType nextDataType;	//数据类型
	private StringBuilder value = new StringBuilder();		//数据缓冲区
	private boolean isTElement;
	private int rowCellCount;
	private short formatIndex;  
    private String formatString;  
    private final DataFormatter formatter = new DataFormatter();
    
	private Map<Integer, String> strMap;
	
	//读取一个表(未测试 暂勿使用)
	public void processOneSheet(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader( pkg );
		this.sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);
		this.stylesTable = r.getStylesTable();
		// rId2 found by processing the Workbook
		// Seems to either be rId# or rSheet#
		InputStream sheet2 = r.getSheet("rId1");
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
	}

	//读取所有表
	public void processAllSheets(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader( pkg );
		this.sst = r.getSharedStringsTable();
		
		XMLReader parser = fetchSheetParser(sst);
		this.stylesTable = r.getStylesTable();
		SheetIterator  sheets = (SheetIterator)r.getSheetsData();
		this.currSheetIndex = 0;
		while(sheets.hasNext()) {
			log.info("Processing new sheet:");
			InputStream sheet = sheets.next();
			
			this.currSheetName = sheets.getSheetName();
			log.info("开始处理第" + (currSheetIndex+1) + "张表<" + currSheetName + ">");
			//检测是否存在配置文件
			if(!loadSheetConfig(currSheetIndex, currSheetName)){
				currSheetIndex++;
				sheet.close();
				continue;
			}
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			log.info("第" + (currSheetIndex+1) + "张表<" + currSheetName + ">处理完成\n");
			currSheetIndex++;
		}
	}
	
	//加载监听器
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser =
			XMLReaderFactory.createXMLReader(
					"org.apache.xerces.parsers.SAXParser"
			);
		
		//ContentHandler handler = new SheetHandler(sst,rowReader,this.sheetMap);
		parser.setContentHandler(this);
		return parser;
	}
	
	
	//处理文档解析开始事件
	public void startDocument() throws SAXException{
		
	}
	
	//处理文档解析结束事件
	public void endDocument() throws SAXException{
		
	}
	
	//处理元素开始事件
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		
		log.debug("当前localName:" + localName);
		log.debug("当前name:" + name);
	
		for(int i = 0; i < attributes.getLength(); i++){
			log.debug("当前attributes-localName:" + attributes.getLocalName(i));
			log.debug("当前attributes-values:" + attributes.getValue(i));
			
		}
		
		if("v".equals(name) || "inlineStr".equals(name)){
			nextIsString = true;
			value.setLength(0);;
		}else if("c".equals(name)){
			//c => cell
            String r = attributes.getValue("r");  
            int firstDigit = -1;  
            for (int c = 0; c < r.length(); ++c) {  
                if (Character.isDigit(r.charAt(c))) {  
                    firstDigit = c;  
                    break;  
                }  
            }
            //当前列索引
            this.colIndex = nameToColumn(r.substring(0, firstDigit)); 
           
			//获取单元格数据类型
			this.setDataType(attributes);
			
		}else if("row".equals(name)){
			//开始行处理  
            row = new HashMap<Integer,Object>();
            String r = attributes.getValue("r");
            if(r != null && !"".equals(r))
            	this.rowIndex = Integer.parseInt(r);
            
            String spans = attributes.getValue("spans");
            spans = spans.substring(spans.indexOf(":")+1);
            try{
            	this.rowCellCount = Integer.parseInt(spans);
            }catch(Exception e){
            	this.rowCellCount = -1;
            }   
		}else if ("sheetData".equals(name)) {  
			//表开始初始化参数
			initSheet();
		}
		
	}
	
	//处理元素结束事件
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		
		if("v".equals(name)){
			//单元格内容处理完成，将值加入list
			row.put(this.colIndex,this.getDataValue(value,this.nextDataType));
			nextIsString = false; 
		}else if("c".equals(name)){
			log.debug("第" + (rowIndex+1) + "行第" + colIndex + "列获取完毕值为：" + this.getDataValue(value,this.nextDataType));
			log.debug("-------------------------");
		}else if("row".equals(name)){
			//当前行尾
			//执行行自定义处理方法
//			mapRow(this.currSheetIndex, rowIndex, row, rowCellCount);
			row = null;
			rowCellCount = -1;
		}else if ("sheetData".equals(name)) {  
			//当前表尾
			rowIndex=0;
		}
		
	}
	
	//处理元素的字符内容
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(nextIsString)
			value.append(ch, start, length);
	}
	
	//参数初始化
	public void initSheet(){
		this.rowIndex = -1;
		this.colIndex = -1;
		this.nextIsString = false;
		this.nextDataType = XSSFDataType.NUMBER;
		this.value = new StringBuilder();
		
		this.formatIndex = -1;
		this.formatString = null;
	}
	
	
	//清楚缓冲数据
	public void clearSheet(){
		this.rowIndex = 0;
		this.colIndex = 0;
	}
	
	//处理数据类型
	public void setDataType(Attributes attributes){
		this.nextDataType = XSSFDataType.NUMBER;  
        this.formatIndex = -1;  
        this.formatString = null;  
        String cellType = attributes.getValue("t");  
        String cellStyleStr = attributes.getValue("s");
        
        if ("b".equals(cellType))
			nextDataType = XSSFDataType.BOOL;
		else if ("e".equals(cellType))
			nextDataType = XSSFDataType.ERROR;
		else if ("inlineStr".equals(cellType))
			nextDataType = XSSFDataType.INLINESTR;
		else if ("s".equals(cellType))
			nextDataType = XSSFDataType.SSTINDEX;
		else if ("str".equals(cellType))
			nextDataType = XSSFDataType.FORMULA;
		else if (cellStyleStr != null) {
			// It's a number, but almost certainly one
			//  with a special style or format 
			int styleIndex = Integer.parseInt(cellStyleStr);
			XSSFCellStyle style = this.stylesTable.getStyleAt(styleIndex);
			this.formatIndex = style.getDataFormat();
			this.formatString = style.getDataFormatString();
			if (this.formatString == null)
				this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
		}
	} 
	
	//对解析出来的数据进行类型处理
	public String getDataValue(StringBuilder value,XSSFDataType nextDataType){
		String thisStr = null;
		switch (nextDataType){
	        case BOOL:
	        	char first = value.charAt(0);
	            thisStr = first == '0' ? "FALSE" : "TRUE";
	            break;
	        case ERROR:
	            thisStr = value.toString().trim();
	            break;
	        case FORMULA:
	            thisStr = value.toString().trim();
	            break;
	        case INLINESTR:
	        	XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
	        	thisStr = rtsi.toString();
	        	rtsi = null;
	        	break;
	        case SSTINDEX:
	        	String sstIndex = value.toString();
	        	try{
	        		int idx = Integer.parseInt(sstIndex);
	        		XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
	        		thisStr = rtss.toString();
	        		rtss = null;
	        	}catch(Exception ex){
	        		log.error("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
	        	}
	        	break;
	        case NUMBER:
	        	 String n = value.toString();  
	             if (this.formatString != null)  
	                 thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);  
	             else 
	                 thisStr = n;  
	             break;
	        default:
	            thisStr = "";
	            break;
	    }
		return thisStr;
	}
	
	//从列名转换为列索引
	private int nameToColumn(String name) {
        int column = -1;
        for (int i = 0; i < name.length(); ++i) {
            int c = name.charAt(i);
            column = (column + 1) * 26 + c - 'A';
        }
        return column;
    }
	
	
//	//自定义接口解析行数据
//	public void mapRow(int sheetIndex, int rowIndex, Map<Integer,Object> row, int rowCellCount){
//		StringBuffer sb = new StringBuffer();
//		int count = row.size();
//		if(rowCellCount > count)
//			count = rowCellCount;
//
//		for(int i=0; i< count;i++){
//			if(row.containsKey(i)){
//				sb.append(row.get(i));
//			}
//			if(i < count - 1)
//				sb.append(EXCEL_LINE_DELIMITER);
//		}
//		log.info("第 "+ (sheetIndex+1) + "表的第" + (rowIndex+1) + "行的内容为：" + sb.toString());
//		rowReader.processLine(sheetIndex,this.currSheetName,rowIndex, sb.toString(),this.currSheet);
//	}
	
	//表配置模版加载
	public boolean loadSheetConfig(int sheetIndex,String sheetName){
		boolean rs = false;
		if(this.sheetMap.containsKey(String.valueOf(sheetIndex))){
			this.currSheet = sheetMap.get(String.valueOf(sheetIndex));
			if(currSheet == null){
				log.error("未获取到第" + (sheetIndex+1) + "张表<" + sheetName + ">配置，跳过\n");
				return false;
			}
			if("0".equals(currSheet.getIsUsed())){
				log.error("第" + (sheetIndex+1) + "张表<" + sheetName + ">设置为不处理，跳过\n");
				return false;
			}
/*		
			if(sheetName.equals(currSheet.getSheetName())){
				rs = true;
			}else{
				log.error("第" + (sheetIndex+1) + "张表<" + sheetName + ">表名与配置名称<" + currSheet.getSheetName() + ">不一致，跳过\n");
				rs = false;
			}
*/			
			rs = true;
		}else{
			log.error("未获取到第" + (sheetIndex+1) + "张表<" + sheetName + ">配置，跳过\n");
			return false;
		}
		return rs;	
	}
	
	/**
	 * 获取指定的表的最大行数
	 * 仅支持usermodel 故文件太大请勿使用该函数
	 * @param sheetIndex
	 * @return
	 */
	public int getSheetRowCount(int sheetIndex){
		if(this.inputFileType.equalsIgnoreCase("xls")){
			return workbook.getSheetAt(sheetIndex).getLastRowNum();
		}else if(this.inputFileType.equalsIgnoreCase("xlsx")){
			return xssworkbook.getSheetAt(sheetIndex).getLastRowNum();
		}else{
			return -1;
		}
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}

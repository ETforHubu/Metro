package poi;

import org.apache.poi.ss.usermodel.CellStyle;

public class MyCell {

	private String cellName;		//列名(对应数据库列名)
	private CellStyle cellStyle;	//单元格格式
	private Integer cellType;		//单元格数据类型
	private Integer cellWidth;		//单元格宽度
	private Integer cellHight;		//单元格高度
	private Object cellData;		//单元格数据
	private Integer row;			//所属行号
	private Integer col;			//所属列号
	private String headerName;		//列名(对应excel中的表头列名)
	private String columnType;		//数据库数据类型
	
	public MyCell(){
	}
	
	//加载时需添加的元素
	public MyCell(int col,String headerName,String cellName,String columnType){
		this.col = col;
		this.headerName = headerName;
		this.cellName = cellName;
		this.columnType = columnType;
	}
	
	//写入时 设置单元格所需元素
	public MyCell(String cellName,CellStyle cellStyle,int cellType,int cellWidth,int cellHight){
		this.cellName = cellName;
		this.cellStyle = cellStyle;
		this.cellType = cellType;
		this.cellWidth = cellWidth;
		this.cellHight = cellHight;
	}
			
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public CellStyle getCellStyle() {
		return cellStyle;
	}
	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}
	public int getCellType() {
		return cellType;
	}
	public void setCellType(Integer cellType) {
		this.cellType = cellType;
	}
	public int getCellWidth() {
		return cellWidth;
	}
	public void setCellWidth(Integer cellWidth) {
		this.cellWidth = cellWidth;
	}
	public int getCellHight() {
		return cellHight;
	}
	public void setCellHight(Integer cellHight) {
		this.cellHight = cellHight;
	}
	public Object getCellData() {
		return cellData;
	}
	public void setCellData(Object cellData) {
		this.cellData = cellData;
	}
	public int getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	public void setMyCell(CellStyle cellStyle,int cellType,int cellWidth,int cellHight){
		this.cellStyle = cellStyle;
		this.cellType = cellType;
		this.cellWidth = cellWidth;
		this.cellHight = cellHight;
	}
}

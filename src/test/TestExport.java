package test;

import java.io.OutputStream;

import poi.PoiExport;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 
 * 导出eccel类  继承自PoiExport 重构了单元格格式模版
 *
 */
public class TestExport extends PoiExport {

	public TestExport(OutputStream outs,String outputFile,String cfgPath,String configMapFileName){
		super(outs, outputFile, cfgPath, configMapFileName);
	}
	
	//若有必要可重写父类单格式格式设置函数
	
	//重写父类加载数据单元格格式模版
	@Override
	public void loadTemplate(int sheetIndex) {
		//加载默认的设置
		for(int i = 0; i <dataCell.size(); i++){
			dataCell.get(String.valueOf(i)).setMyCell(styles.get("dataStyle"),Cell.CELL_TYPE_STRING,256*10,13);
		}
		//需特殊设置的列
		dataCell.get("1").setMyCell(styles.get("dataStyle"),Cell.CELL_TYPE_STRING,256*16,13);
	}
}

package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/*
 * 通用工具类
 * 
 * 修改记录：
 * date					author				cause
 * 2015-03-30			snow				删除日期操作相关函数，日期操作已重构成新的DateUtil工具类
 * 
 */
public class ComUtil {

	/**
	 * 检测对象是否存在或为空
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj){
		if(obj == null || "".equals(obj))
			return true;
		else
			return false;
	}
	
	/**
	 * 从数组中获取key值的value值
	 * @param strArr
	 * @param id
	 * @return
	 */
	public static String getStringByKeyFromArray(String[][] strArr,String id){
		if(isNull(strArr) || isNull(id)){
			return "";
		}
		String value = "";
		for(int i = 0; i < strArr.length; i++){
			if(id.equals(strArr[i][0])){
				value = strArr[i][1];
				break;
			}
		}
		return value;
	}
	
	/**
	 * 左填充字符串 
	 * @param str
	 * @param pad_length
	 * @param pad_str
	 * @return
	 */
    public static String lpad(String str, int pad_length, char pad_str){
    	while(str.length() < pad_length){
    		str = pad_str + str;
    	}
    	return str;
    }
    
    /**
     * 12位分格式转为元
     * @param fenStr
     * @return
     */
/*    public static String fenToYuan(String fenStr){
    	String yuanStr = "";
    	if(fenStr == null || "".equals(fenStr)){
    		return "";
    	}
    	double fen = 0;
    	double yuan = 0;
    	fen = ComUtil.objToDouble(fenStr);
    	yuan = fen/100;
    	yuanStr = flamtPrice(yuan);
    	return yuanStr;
    }
 */   
    /**
     * 元转12位分格式
     * @param yuanStr
     * @return
     */
    public static String yuanToFen(String yuanStr){
    	String fenStr = "";
    	if(yuanStr == null || "".equals(yuanStr)){
    		return "";
    	}
    	int fen = 0;
    	float yuan = 0;
    	yuan = ComUtil.strToFloat(yuanStr);
    	fen = (int)(yuan * 100);
    	if(fen <0){
    		fenStr = String.valueOf(fen).substring(1);
    		fenStr = "-" + lpad(fenStr,11,'0');
    	}else{
    		fenStr = String.valueOf(fen);
    		fenStr = lpad(fenStr,12,'0');
    	}
    	return fenStr;
    }

    public static String flamtPrice(double price){
    	DecimalFormat decimalFormat=new DecimalFormat("#0.00");
    	String p=decimalFormat.format(price);
    	return p;
    }
    
    //数字字符串转整型
    public static int strToInt(String str){
    	int dec = 0;
    	try{
    		dec = Integer.parseInt(str);
    	}catch(Exception e){
    		dec = 0;
    	}
    	return dec;
    }
    
    //数字字符串转浮点型
    public static float strToFloat(String str){
    	float dec = 0;
    	try{
    		dec = Float.parseFloat(str);
    	}catch(Exception e){
    		dec = 0;
    	}
    	return dec;
    }
    
    /**
     * 解析连续数字 并返回以半角逗号分隔的数字
     * @param src
     * @return
     * modify by snow at 2015-2-3 for change int to long
     */
    public static String parseConsecutiveNum(String src){
    	if(src == null || "".equals(src))
    		return "";
    	
    	String[] srcList = src.split(",");
    	String dec = "";
    	for(int i = 0; i < srcList.length; i++){
    		String decTemp = srcList[i];
    		if(decTemp.indexOf('-') != -1){
    			String tempList[] = decTemp.split("-");
    			System.out.println(tempList[0]);
    			long min = Long.parseLong(tempList[0]);
    			long max = Long.parseLong(tempList[1]);
    			if(min >= max){
    				return "";
    			}
    			for(int j = 0; j <= (max-min); j++){
    				dec += String.valueOf(min+j) + ",";
    			}
    		}else{
    			dec += srcList[i] + ",";
    		}
    	}
    	return dec.substring(0, dec.lastIndexOf(","));
    }
    
    /**
     * 银行卡号加密
     * 规则：保留 前6后4，其余*化
     * @param srcCardNo
     * @return
     */
    public static String cardNoEncrypt(String srcCardNo){
    	String decCardNo="";
    	if(isNull(srcCardNo)){
    		return "";
    	}
    	decCardNo = srcCardNo.substring(0, 6);
    	decCardNo += srcCardNo.substring(6, srcCardNo.length()-4).replaceAll("\\d", "*");
    	decCardNo += srcCardNo.substring(srcCardNo.length()-4);
    	return decCardNo;
    }
    
    /**
	 * 分隔符特殊字符转换函数
	 * 支持  |
	 * @param spc
	 * @return
	 */
	public static String converSpecialCharater(String spc){
		if(ComUtil.isNull(spc)){
			return "";
		}
		String rs = "";
		if(spc.indexOf("|") != -1){
			rs = spc.replaceAll("\\|", "\\\\|");
		}
		return rs;
	}
    
	/**
	 * 其他对象转str  
	 * @param obj
	 * @return 为空返回空字符
	 */
	public static String objToStr(Object obj){
		try{
			if (obj == null){
				return "";
			}else
				return String.valueOf(obj);
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * 其他对象转int  
	 * @param obj
	 * @return 转换失败返回0
	 */
	public static int objToInt(Object obj){
		if (obj == null){
			return 0;
		}else{
			try{
				return Integer.parseInt(String.valueOf(obj));
			}catch(Exception e){
				return 0;
			}
		}
	}
	
	/**
	 * 其他对象转double 
	 * @param obj
	 * @return 转换失败返回0
	 */
	public static double objToDouble(Object obj){
		if (obj == null){
			return 0;
		}else{
			try{
				return Double.parseDouble(String.valueOf(obj));
			}catch(Exception e){
				return 0;
			}
		}
	}
	
	/**
	 * 将分为单位的转换为元 （除100）
	 * @param amount
	 * @return
	 */
	public static String changeF2Y(String amount){    
		String CURRENCY_FEN_REGEX  = "\\-?[0-9]+";
        if(!amount.matches(CURRENCY_FEN_REGEX)) {    
            return "";    
        } 
        String yuan  = "";
        DecimalFormat decimalFormat=new DecimalFormat("#0.00");
        yuan = decimalFormat.format(BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)));
        return yuan;    
    }
	
    public static void main(String[] args) {  
    	
    }  
}

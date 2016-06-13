/*
 * Created on 2008-02-01
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 * 
 * Copyright ? 2008 Hangzhou ewall Co. Ltd.
 * All right reserved
 *
 * @author kidd     
 * Created on 2008-02-01
 */
package gxlu.ietools.property.util;

import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wang yeliang
 * 
 *         This class is for most of the format tasks used in the web tier
 *         programming.
 * 
 */

public class FormatUtil {

	/**
	 * 英文编码
	 */
	public final static String ENG_CODE_PAGE = "ISO8859_1";
	/**
	 * 中文编码
	 */
	public final static String CN_CODE_PAGE = "GBK";

	private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final String defaultSplit = "--";

	/**
	 * 类型转换BYTE
	 * 
	 * @param object
	 * @return
	 */
	public static Byte getByteType(Object object) {
		Byte bytes = null;
		if (object != null) {
			if (object.getClass().getSimpleName().equals("Integer")) {
				bytes = ((Integer) object).byteValue();
			} else {
				bytes = (Byte) object;
			}
		}
		return bytes;
	}

	/**
	 * 对象转型为String
	 * 
	 * @param object
	 * @return
	 */
	public static String getStringType(Object object) {
		String string = "";
		if (object != null) {
			if (object.getClass().getSimpleName().equals("Long")) {
				string = ((Long) object).toString();
			} else if (object.getClass().getSimpleName().equals("Integer")) {
				string = ((Integer) object).toString();
			} else if (object.getClass().getSimpleName().equals("Date")) {
				string = CalendarUtil.date2FormattedString(((Date) object),
						"yyyy-MM-dd");
			} else if (object.getClass().getSimpleName().equals("Double")) {
				string = ((Double) object).toString();
			} else if (object.getClass().getSimpleName().equals("Float")) {
				string = ((Float) object).toString();
			} else if (object.getClass().getSimpleName().equals("Byte")) {
				string = ((Byte) object).toString();
			} else if (object.getClass().getSimpleName().equals("BigDecimal")) {
				string = ((BigDecimal) object).toString();
			} else {
				string = (String) object;
			}
		}
		return string;
	}

	/**
	 * 对象转型为String，并将默认值进行空转换
	 * 
	 * @param object
	 * @return
	 */
	public static String getStringTypeSetDefault(Object object) {
		String string = "";
		if (object != null) {
			if (object.getClass().getSimpleName().equals("Long")) {
				string=((Long)object).toString();
				if(Long.parseLong(string)<=0){
					string = "";
				}
			} else if (object.getClass().getSimpleName().equals("Integer")) {
				string=((Integer)object).toString();
				if(Integer.parseInt(string.toString())<=0){
					string = "";
				}
			} else if (object.getClass().getSimpleName().equals("Date")) {
				string = CalendarUtil.date2FormattedString(((Date) object),
						"yyyy-MM-dd");
			} else if (object.getClass().getSimpleName().equals("Double")) {
				string=((Double)object).toString();
				if(Double.parseDouble(string)<=0){
					string = "";
				}
			} else if (object.getClass().getSimpleName().equals("Float")) {
				string=((Float)object).toString();
				if(Float.parseFloat(string)<=0){
					string = "";
				}
			} else if (object.getClass().getSimpleName().equals("Byte")) {
				string = ((Byte) object).toString();
				if (Byte.parseByte(string)<=0) {
					string = "";
				}
			} else if (object.getClass().getSimpleName().equals("BigDecimal")) {
				string=((BigDecimal) object).toString();
				if(new BigDecimal(string).compareTo(BigDecimal.valueOf(0))<=0){
					string = "";
				}
			} else {
				string = (String) object;
			}
		}
		return string;
	}
	public static Object getStringTypeSetDefault(String type,Object object) {
		if(object!=null){
			if (type.equals("char")	|| type.equals("class java.lang.Character")){
				return object.toString();
			}else if (type.equals("long")|| type.equals("class java.lang.Long")){
				if(((Long)object)<=0){
					return "";
				}else{
					return (Long)object;
				}
			}		
			else if (type.equals("int")|| type.equals("class java.lang.Integer"))
			{
				if (((Integer)object)<=0){
					return "";
				}else{
					return (Integer)object;
				}
			}
			else if (type.equals("short")|| type.equals("class java.lang.Short")){
				if (((Short)object)<=0){
					return "";
				}else{
					return (Short)object;
				}
			}else if (type.equals("byte")|| type.equals("class java.lang.Byte")){
				if (((Byte)object)<=0){
					return "";
				}else{
					return (Byte)object;
				}
			}else if (type.equals("double")|| type.equals("class java.lang.Double"))
			{
				if (((Double)object)<=0){
					return "";
				}else{
					return (Double)object;
				}
			}
			else if (type.equals("float")|| type.equals("class java.lang.Float")){
				if (((Float)object)<=0){
					return "";
				}else{
					return (Float)object;
				}
			}else if (object.getClass().getSimpleName().equals("BigDecimal")) {
				String string=((BigDecimal) object).toString();
				if(new BigDecimal(string).compareTo(BigDecimal.valueOf(0))<=0){
					return "";
				}else{
					return string;
				}
			}else if (type.equals("class java.util.Date"))
			{
				java.util.Date current = new java.util.Date();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(object);
			}
			else{
				return object;
			}
		}else{
			return "";
		}
	}
	
	public static Object getTypeConvert(String type, Object value){
		try{
			if(type.equals("char")||type.equals("java.lang.Character")){
				if(value==null){
					value="";
				}
			}else if(type.equals("long")||type.equals("java.lang.Long")){
				if(value==null){
					value=-1;
				}
			}else if(type.equals("int")||type.equals("java.lang.Integer")){
				if(value==null){
					value=-1;
				}
			}else if(type.equals("short")||type.equals("java.lang.Short")){
				if(value==null){
					value=-1;
				}
			}else if(type.equals("byte")||type.equals("java.lang.Byte")){
				if(value==null){
					value=-1;
				}
			}else if(type.equals("double")||type.equals("java.lang.Double")){
				if(value==null){
					value=-1;
				}
			}else if(type.equals("float")||type.equals("java.lang.Float")){
				if(value==null){
					value=-1;
				}
			}else if ((type.equals("BigDecimal"))||(type.equals("java.math.BigDecimal"))) {
				if(value==null){
					value=-1;
				}
		    }else if(type.equals("java.util.Date")){
		    	if(value==null){
					value=null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 返回当前行数
	 * 
	 * @param objs
	 * @return
	 */
	public static int getRowsNumber(Object[][] objs) {
		int firstNumber = (Integer) objs[1][1];
		int lastNumber = (Integer) objs[1][2];
		int nowNumber = (Integer) objs[1][3];

		if (firstNumber == 0 && lastNumber == 0) {
			return nowNumber;
		} else {
			return firstNumber + nowNumber;
		}
	}

	/**
	 * 从属性集合中寻找与目标对象匹配的列头
	 * 
	 * @param propertyList
	 * @param targetObject
	 * @return
	 */
	public static String getTitle(List propertyList, String targetObject) {
		String title = "";
		for (int i = 0; i < propertyList.size(); i++) {
			Object object = propertyList.get(i);

			if (object instanceof PropertyValue) {
				if (((PropertyValue) object).getName().equalsIgnoreCase(
						targetObject)) {
					title = ((PropertyValue) object).getColumnTitle();
					break;
				}
			} else if (object instanceof PropertyObject) {
				if (((PropertyObject) object).getName().equalsIgnoreCase(
						targetObject)) {
					title = ((PropertyObject) object).getColumnTitle();
					break;
				}
			}
		}
		return title;
	}
}

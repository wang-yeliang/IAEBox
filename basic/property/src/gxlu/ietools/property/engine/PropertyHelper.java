package gxlu.ietools.property.engine;

import java.lang.reflect.Field;
import java.util.ArrayList;

import gxlu.ietools.property.exception.PropertyException;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;

public final class PropertyHelper {

	/**
	 * @param object
	 * @return
	 */
	public static boolean getCheckType(String object){
		if(object.equals("char")||object.equals("java.lang.Character")){
			return true;
		}else if(object.equals("long")||object.equals("java.lang.Long")){
			return true;
		}else if(object.equals("int")||object.equals("java.lang.Integer")){
			return true;
		}else if(object.equals("short")||object.equals("java.lang.Short")){
			return true;
		}else if(object.equals("byte")||object.equals("java.lang.Byte")){
			return true;
		}else if(object.equals("double")||object.equals("java.lang.Double")){
			return true;
		}else if(object.equals("float")||object.equals("java.lang.Float")){
			return true;
		}else if(object.equals("String")||object.equals("java.lang.String")){
			return true;
		}else if(object.equals("BigDecimal")||object.equals("java.math.BigDecimal")){
			return true;
		}else if(object.equals("class java.util.Date")){
			return true;
		}

		return false;
	}
	
	public static boolean getClassCheck(String object){
		if(object.indexOf("gxlu")>=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 从B类中获得Property对象
	 * @param classes
	 * @return
	 */
	public static Property getProperty(Class classes){
		Property property = new Property();
		property.setBclass(classes.getName());
		property.setByInsert("true");
		property.setByUpdate("true");
		property.setThdLineMax("0");
		property.setThdLineNum("0");
		return property;
	}
	
	/**
	 * 根据对象获得其所有属性，包括父类的
	 * @param classes B类
	 * @return
	 */
	public static PropertyValue[] getPropertyFields(Class classes){
		ArrayList arrayList = new ArrayList();
		
		Class superClass = classes.getSuperclass();
		
		if(!superClass.getName().equalsIgnoreCase("java.lang.Object")){
			Field[] methods = superClass.getDeclaredFields();
			for (int j = 0; j < methods.length; j++) {
				Field m = methods[j];
				if(PropertyHelper.getCheckType(m.getType().getName())){
					PropertyValue propertyValue = new PropertyValue();
					propertyValue.setName(m.getName());
					propertyValue.setColumnTitle("");
					propertyValue.setDatadict(false);
					propertyValue.setDatadictClass("");
					propertyValue.setDatadictAttr("");
					propertyValue.setIsQuery("none");
					arrayList.add(propertyValue);
				}
			}
		}
		
		Field[] methods = classes.getDeclaredFields();
		for (int j = 0; j < methods.length; j++) {
			Field m = methods[j];
			if(PropertyHelper.getCheckType(m.getType().getName())){
				PropertyValue propertyValue = new PropertyValue();
				propertyValue.setName(m.getName());
				propertyValue.setColumnTitle("");
				propertyValue.setDatadict(false);
				propertyValue.setDatadictClass("");
				propertyValue.setDatadictAttr("");
				propertyValue.setIsQuery("none");
				arrayList.add(propertyValue);
			}
		}

		return (PropertyValue[])arrayList.toArray(new PropertyValue[0]);
	}
	
	/**
	 * 根据对象获得其所有属性，包括父类的
	 * @param classes B类
	 * @return
	 */
	public static PropertyObject[] getPropertyObjectFields(Class classes){
		ArrayList arrayList = new ArrayList();
		
		Class superClass = classes.getSuperclass();
		
		if(!superClass.getName().equalsIgnoreCase("java.lang.Object")){
			Field[] methods = superClass.getDeclaredFields();
			for (int j = 0; j < methods.length; j++) {
				Field m = methods[j];
				if(PropertyHelper.getClassCheck(m.getType().getName())){
					PropertyObject propertyObject = new PropertyObject();
					propertyObject.setName(m.getName());
					propertyObject.setColumnTitle("");
					propertyObject.setJoinColumn(PropertyHelper.getJoinColumn(classes, m.getName()));
					propertyObject.setClassName(m.getType().getName());
					propertyObject.setClassColumn("");
					propertyObject.setIsQuery("none");
					propertyObject.setQueryClass("gxlu.netmaster.client.mobilenetwork.equip.querydlg.DlgQueryMNNE");
					arrayList.add(propertyObject);
				}
			}
		}
		
		Field[] methods = classes.getDeclaredFields();
		for (int j = 0; j < methods.length; j++) {
			Field m = methods[j];
			if(PropertyHelper.getClassCheck(m.getType().getName())){
				PropertyObject propertyObject = new PropertyObject();
				propertyObject.setName(m.getName());
				propertyObject.setColumnTitle("");
				propertyObject.setJoinColumn(PropertyHelper.getJoinColumn(classes, m.getName()));
				propertyObject.setClassName(m.getType().getName());
				propertyObject.setClassColumn("");
				propertyObject.setIsQuery("none");
				propertyObject.setQueryClass("gxlu.netmaster.client.mobilenetwork.equip.querydlg.DlgQueryMNNE");
				arrayList.add(propertyObject);
			}
		}

		return (PropertyObject[])arrayList.toArray(new PropertyObject[0]);
	}
	
	/**
	 * @param classes
	 * @param joinColumn
	 * @return
	 * @throws PropertyException
	 */
	public static String getJoinColumn(Class classes,String joinColumn){
		String joinColumnRt = "";

		joinColumn = joinColumn + "id";
		Class superClass = classes.getSuperclass();
		if(!superClass.getName().equalsIgnoreCase("java.lang.Object")){
			Field[] field = superClass.getDeclaredFields();
			for (int j = 0; j < field.length; j++) {
				Field m = field[j];
				if(m.getName().equalsIgnoreCase(joinColumn)){
					joinColumnRt = m.getName();
					break;
				}
			}
		}
			
		if("".equals(joinColumnRt)){
			Field[] field = classes.getDeclaredFields();
			for (int j = 0; j < field.length; j++) {
				Field m = field[j];
				if (joinColumn.equalsIgnoreCase(m.getName())) {
					joinColumnRt = m.getName();
					break;
				}
			}
		}

		return joinColumnRt;
	}
}

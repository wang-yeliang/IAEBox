/**************************************************************************
 * $$RCSfile: ReflectHelper.java,v $$  $$Revision: 1.13 $$  $$Date: 2010/06/13 02:03:31 $$
 *
 * $$Log: ReflectHelper.java,v $
 * $Revision 1.13  2010/06/13 02:03:31  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.12  2010/06/13 01:20:33  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.11  2010/06/04 07:43:32  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.10  2010/05/26 09:27:32  zhangj
 * $增加特殊引用属性的处理
 * $
 * $Revision 1.9  2010/05/11 02:03:52  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.8  2010/05/07 12:53:06  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.7  2010/05/06 07:10:37  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:03  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.util;

import gis.client.main.GisDictionary;
import gis.common.dataobject.YObjectInterface;
import gxlu.afx.system.common.FieldDictionary;
import gxlu.afx.system.common.SysDictionaryFactory;
import gxlu.afx.system.common.constant.CommonSysConst;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.query.client.BQueryClient;
import gxlu.afx.system.query.common.QueryExprBuilder;
import gxlu.ietools.basic.collection.util.DynamicObject;
import gxlu.ietools.client.query.gisquery.GisQueryClient;
import gxlu.ietools.property.exception.PropertyException;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.propertys.PropertyAccessorFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 
 * @author kidd
 *
 */
public class ReflectHelper {
	
	/**
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class classForName(String name) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader!=null) {
				return contextClassLoader.loadClass(name);
			} 
			else {
				return Class.forName(name);
			}
		}
		catch (Exception e) {
			return Class.forName(name);
		}
	}
	
    public static String getUpperCase(String args) {
    	String value  = args.substring(0,1).toUpperCase()+args.substring(1);
        return value;
    }
	public static String getDClassName(String className){
		return className.substring(className.lastIndexOf(".")+2);
	}
	public static String getJoinColumn(String name){
		return name.substring(name.indexOf("[")+1, name.indexOf("]"));
	}
	/**
	 * 利用反射机制赋值
	 * 		数据->模板->B类
	 * @param obj
	 * @param pname
	 * @param pvalue
	 * @return
	 */
	public static Object setValue(Object obj, String pname, Object pvalue) {
		Class clz = obj.getClass();
		String methodName = "set" + pname.substring(0, 1).toUpperCase() + pname.substring(1);
		Method method = null;
		Method[] methods = clz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
				method = m;
				break;
			}
		}
		
		Object result = null;
		try {
			if (method != null) {
				result = method.invoke(obj, pvalue);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

 public static Object setValuebysetMethod(Object obj, String methodName, Object pvalue)
  {
    Class clz = obj.getClass();
    Method method = null;
    Method[] methods = clz.getMethods();
    for (int i = 0; i < methods.length; ++i) {
      Method m = methods[i];
      if ((m.getName().equalsIgnoreCase(methodName)) && (m.getParameterTypes().length == 1)) {
        method = m;
        break;
      }
    }

    Object result = null;
    try {
      if (method != null){
    	  result = method.invoke(obj, new Object[] { pvalue });
      }
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return result;
  }
	/**
	 * 利用反射给OtoO对象赋值
	 * @param obj
	 * @param propertyObjs
	 * @return
	 */
	public static void setObjectToObject(Object obj,PropertyObject propertyObjs, Object pvalue) throws PropertyException{
		Class clz = obj.getClass();
		String methodName="";
		if(propertyObjs.getName().indexOf("[")>=0){
			methodName = "set" + getUpperCase(propertyObjs.getJoinColumn());
		}else{
			methodName = "set" + getUpperCase(propertyObjs.getName());
		}
		Method method = null;
		
		while(true){
			Class classe = clz;
				
			Method[] methods = classe.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method m = methods[i];
				if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
					method = m;
					break;
				}
			}
				
			if(method==null){
				String superClassName = classe.getSuperclass().getName();
				
				if("java.lang.Object".equals(superClassName)){
					break;
				}else{
					clz = classe.getSuperclass();
				}
			}else{
				break;
			}
		}
		
		try{
			if (method != null) {
				method.invoke(obj, pvalue);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new PropertyException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	/**
	 * 利用反射获取class
	 * @param obj
	 * @param propertyObjs
	 * @return
	 */
	public static Object[] getObjectToObject(PropertyObject propertyObjs, Object[][] object) throws PropertyException{
		Object[] objs = new Object[2];
		try{
			Class className = Class.forName(propertyObjs.getClassName());

			String values = (String)object[0][Integer.parseInt(propertyObjs.getColumnSeq())-1];
			
			if(values!=null&&!values.equals("")){
				Vector resultObj = 
					BQueryClient.getQueryResults(
						new QueryExprBuilder().get(propertyObjs.getClassColumn()).equal(values),
						className,
					null);
				
				if (resultObj != null) {
					for (int i = 0; i < resultObj.size(); i++) {
						if(propertyObjs.getName().indexOf("[")>=0){
							objs[0] = invokeMethod(resultObj.elementAt(i),"get"+getUpperCase(getJoinColumn(propertyObjs.getName())),null,null);
						}else{
							objs[0] = resultObj.elementAt(i);
						}
					}
					objs[1] = new Boolean(true);
				}else{
					Object[] errorObj = new Object[2];
					errorObj[0] = Integer.valueOf(FormatUtil.getRowsNumber(object));
					errorObj[1] = SystemErrorNames.ObjectDataError + ": "+propertyObjs.getColumnTitle()+"列对象值无法在数据库中找到对应的数据";
					PropertyAccessorFactory.addTypeConvertError(errorObj);
					objs = null;
				}
			}else{
				objs[0] = null;
				objs[1] = new Boolean(false);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return objs;
	}
	
	/**
	 * 通过ID查找对象B类对相应列进行替换
	 * @param obj
	 * @param property
	 * @param id
	 * @return
	 * @throws PropertyException
	 */
	public static Object[] getEvaluateValue(Object obj, PropertyValue propertyValue, Object[][] object) throws PropertyException{
		Class clz = obj.getClass();
		Object[] objs = new Object[2];
		
		String columnValue = FormatUtil.getStringType(object[0][Integer.parseInt(propertyValue.getColumnSeq())-1]);
		
		try{
			if(columnValue==null||columnValue.equals("")||columnValue.equals("-1")){
				objs[1] = new Boolean(false);
			}else{
				Vector resultObj=null;
				if(obj instanceof BObjectInterface){
					resultObj = 
						BQueryClient.getQueryResults(
							new QueryExprBuilder().get("id").equal(columnValue),
							clz,
						null);
				}else if(obj instanceof YObjectInterface){
					resultObj = GisQueryClient.exeGisQuery(null,null);
				}
				if (resultObj != null) {
					for (int i = 0; i < resultObj.size(); i++) {
						objs[0] = resultObj.elementAt(i);
					}
					objs[1] = new Boolean(false);
				}else{
					Object[] errorObj = new Object[2];
					errorObj[0] = Integer.valueOf(FormatUtil.getRowsNumber(object));
					errorObj[1] = SystemErrorNames.SearchDataError + ": 对应ID在数据库中无法找到对应的数据";
					PropertyAccessorFactory.addTypeConvertError(errorObj);
					objs[1] = new Boolean(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new PropertyException(e.getClass().getName() + ":" + e.getMessage());
		}

		return objs;
	}
	
	/**
	 * 利用反射机制赋值
	 * 		B类->模板->动态数据
	 * @param obj 返回对象实体
	 * @param property 模板信息
	 * @param bobject B类集合
	 * @return
	 */
	public static Object setDynamicValue(Object obj, Object property, Object pvalue) {
		Class clz = obj.getClass();
		PropertyValue propertyValue = (PropertyValue)property;
		
		String methodName = "setF" + propertyValue.getColumnSeq();
		Method method = null;
		Method[] methods = clz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
				method = m;
				break;
			}
		}

		try {
			if (method != null) {
				method.invoke(obj, FormatUtil.getStringTypeSetDefault(pvalue));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 通过查询获得引用对象
	 * @param propertyObjs
	 * @param value
	 * @return
	 */
	public static Object getJoinObjbyQuery(PropertyObject propertyObjs,String value){
		Vector resultObj=null;
		try {
			resultObj = BQueryClient.getQueryResults(new QueryExprBuilder().get(getJoinColumn(propertyObjs.getName())).equal(value),classForName(propertyObjs.getClassName()),null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(resultObj!=null&&resultObj.size()>0){
			return resultObj.get(0);
		}
		return null;
	}
	/**
	 * B类型对象转换输出显示字段
	 * @param obj
	 * @param property
	 * @param bobject
	 */
	public static void setDynamicObjectToObject(Object obj, Object property, Object pvalue) {
		Class clz = obj.getClass();
		PropertyObject propertyObjs = (PropertyObject)property;
		
		String methodName = "setF" + propertyObjs.getColumnSeq();
		Method method = null;
		Method[] methods = clz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
				method = m;
				break;
			}
		}

		Object pvalues = null;
		try {
			if(pvalue!=null&&propertyObjs.getName().indexOf("[")>=0){
				pvalue=getJoinObjbyQuery(propertyObjs,pvalue.toString());
			}
			if(pvalue!=null){
				Class classes = pvalue.getClass();

				while(true){
					Class classe = classes;
					
					Method[] methodes = classe.getDeclaredMethods();
					
					for (Method methodcs : methodes) {
						if(methodcs.getName().equalsIgnoreCase("get" + propertyObjs.getClassColumn().substring(0, 1).toUpperCase() + propertyObjs.getClassColumn().substring(1))){
							pvalues = methodcs.invoke(pvalue);
							break;
						}
					}
					
					if(pvalues==null){
						String superClassName = classe.getSuperclass().getName();
						
						if("java.lang.Object".equals(superClassName)){
							break;
						}else{
							classes = classe.getSuperclass();
						}
					}else{
						break;
					}
				}
			}

			if (method != null&&pvalue!=null) {
				method.invoke(obj, FormatUtil.getStringTypeSetDefault(pvalues));
			}else{
				method.invoke(obj, "");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
//	public static void setDynamicObjectToObject(Object obj, Object property, Object pvalue) {
//		Class clz = obj.getClass();
//		PropertyObject propertyObjs = (PropertyObject)property;
//		
//		String methodName = "setF" + propertyObjs.getColumnSeq();
//		Method method = null;
//		Method[] methods = clz.getDeclaredMethods();
//		for (int i = 0; i < methods.length; i++) {
//			Method m = methods[i];
//			if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
//				method = m;
//				break;
//			}
//		}
//
//		Object pvalues = null;
//		try {
//			if(pvalue!=null){
//				Class classes = pvalue.getClass();
//
//				while(true){
//					Class classe = classes;
//					
//					Method[] methodes = classe.getDeclaredMethods();
//					
//					for (Method methodcs : methodes) {
//						if(methodcs.getName().equalsIgnoreCase("get" + propertyObjs.getClassColumn().substring(0, 1).toUpperCase() + propertyObjs.getClassColumn().substring(1))){
//							pvalues = methodcs.invoke(pvalue);
//							break;
//						}
//					}
//					
//					if(pvalues==null){
//						String superClassName = classe.getSuperclass().getName();
//						
//						if("java.lang.Object".equals(superClassName)){
//							break;
//						}else{
//							classes = classe.getSuperclass();
//						}
//					}else{
//						break;
//					}
//				}
//			}
//
//			if (method != null&&pvalue!=null) {
//				method.invoke(obj, FormatUtil.getStringTypeSetDefault(pvalues));
//			}else{
//				method.invoke(obj, "");
//			}
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * 字典值转换
	 * 
	 * @param obj
	 * @param property
	 * @param bobject
	 */
	public static Object[] setDictionary(Object obj, Object property, Object[][] object) throws PropertyException{
		Class clz = obj.getClass();
		PropertyValue propertyValue = (PropertyValue)property;
		
		Object[] objects = new Object[2];
		objects[1] = new Boolean(true);

		int[][] dictionaryValue = new int[2][6];
		int j = 0;
		try{

			Integer columnSeq = Integer.parseInt(propertyValue.getColumnSeq())-1;
			
			String classId = propertyValue.getDatadictClass().toUpperCase();
			String attributeId = propertyValue.getDatadictAttr().toUpperCase();
			String valueCn = (String)object[0][columnSeq];
			
			if(valueCn!=null&&!valueCn.equals("")){
				dictionaryValue[0][j++] = SysDictionaryFactory.getSysDictionaryValue(classId, attributeId, valueCn);
				dictionaryValue[0][j++] = SysDictionaryFactory.getSysDictionaryValue(classId, attributeId, valueCn+" ");
				dictionaryValue[0][j++] = FieldDictionary.getFieldValue(classId, attributeId, valueCn);
				dictionaryValue[0][j++] = FieldDictionary.getFieldValue(classId, attributeId, valueCn+" ");
				dictionaryValue[0][j++] = GisDictionary.getValue(classId, attributeId, valueCn);
				dictionaryValue[0][j++] = GisDictionary.getValue(classId, attributeId, valueCn+" ");
				
				objects=setDictionaryProcedure(objects, object, dictionaryValue, j,propertyValue.getColumnTitle());
			}else{
				objects[1] = new Boolean(false);
			}
		} catch (Exception e) {
			objects=setDictionaryProcedure(objects, object, dictionaryValue, j,propertyValue.getColumnTitle());
		}
		return objects;
	}
	
	/**
	 * 字典赋值过程
	 * @param objects
	 * @param object
	 * @param dictionaryValue
	 * @param j
	 */
	public static Object[] setDictionaryProcedure(Object[] objects,Object[][] object,int[][] dictionaryValue,int j,String title){
		int check = 0;
		int valueSeq = 0;
		if(j<6){
			valueSeq = j-1;
		}else{
			valueSeq = dictionaryValue[0].length;
		}
		boolean flag=true;
		for(int i=0;i<valueSeq;i++){
			if(dictionaryValue[0][i]!=CommonSysConst.NULL_BYTE_FIELD&&dictionaryValue[0][i]!=-1){
				objects[0] = new Integer(dictionaryValue[0][i]);
				flag=false;
				break;
			}
		}
		
		if(flag){
			Object[] errorObj = new Object[2];
			objects = null;
			errorObj[0] = Integer.valueOf(FormatUtil.getRowsNumber(object));
			errorObj[1] = SystemErrorNames.DictionaryDataError + ":"+title+" 对应字典值无法在字典库中找到";
			PropertyAccessorFactory.addTypeConvertError(errorObj);
		}
		return objects;
	}
//	public static void setDictionaryProcedure(Object[] objects,Object[][] object,int[][] dictionaryValue,int j){
//		int check = 0;
//		int valueSeq = 0;
//		if(j<6){
//			valueSeq = j-1;
//		}else{
//			valueSeq = dictionaryValue[0].length;
//		}
//		for(int i=0;i<valueSeq;i++){
//			if(dictionaryValue[0][i]==CommonSysConst.NULL_BYTE_FIELD){
//				check++;
//			}else{
//				dictionaryValue[1][i] = 1;
//			}
//		}
//		
//		if (check<=6) {
//			for(int i=0;i<valueSeq;i++){
//				if(dictionaryValue[1][i]==1){
//					objects[0] = new Integer(dictionaryValue[0][i]);
//					break;
//				}
//			}
//		}else{
//			Object[] errorObj = new Object[2];
//			errorObj[0] = Integer.valueOf(FormatUtil.getRowsNumber(object));
//			errorObj[1] = SystemErrorNames.DictionaryDataError + ": 对应字典值无法在字典库中找到";
//			PropertyAccessorFactory.addTypeConvertError(errorObj);
//			objects = null;
//		}
//	}
	
	/**
	 * 字典值反向转换
	 * 
	 * @param obj
	 * @param property
	 * @param bobject
	 */
	public static String getDictionary(Object obj, Object property, Object[][] object) throws PropertyException{
		Class clz = obj.getClass();
		PropertyValue propertyValue = (PropertyValue)property;
		
		String dictionaryData = null;
		try{
			Integer columnSeq = Integer.parseInt(propertyValue.getColumnSeq())-1;
			
			String classId = propertyValue.getDatadictClass().toUpperCase();
			String attributeId = propertyValue.getDatadictAttr().toUpperCase();
			Byte valueCn = FormatUtil.getByteType(object[0][columnSeq]);

			String[][] dictionaryValue = new String[2][3];
			dictionaryValue[0][0] = SysDictionaryFactory.getSysDictionaryValueCN(classId, attributeId, valueCn);
			dictionaryValue[0][1] = FieldDictionary.getFieldDesc(classId, attributeId, valueCn);
			for(int i=0;i<dictionaryValue[0].length;i++){
				if(dictionaryValue[0][i]!=null&&!dictionaryValue[0][i].toString().equals("")){
					dictionaryData = dictionaryValue[0][i];
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new PropertyException(e.getClass().getName() + ":" + e.getMessage());
		}
		return dictionaryData;
	}
	
	/**
	 * 返回类型转换所需的3个参数
	 * @param dynamicObject
	 * @param property
	 * @param theClass
	 * @return 
	 * 		object[0]* -- 动态类对应值
	 * 		object[1]* -- 对象类型
	 * 		object[2]* -- 标题
	 */
	public static Object getReflectData(Object dynamicObject,Object property,Class theClass) throws PropertyException{
		Object objects = null;
		String columnSeq = null;
		if(property instanceof PropertyValue){
			columnSeq = ((PropertyValue)property).getColumnSeq();
		}else if(property instanceof PropertyObject){
			columnSeq = ((PropertyObject)property).getColumnSeq();
		}

		try{
			Class dynamicObjectClass = dynamicObject.getClass();
			Method method = dynamicObjectClass.getMethod("getF"+columnSeq);
			objects = method.invoke(dynamicObject);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objects;
	}
	
	/**
	 * 返回类型转换所需的3个参数
	 * @param bussinessObject
	 * @param property
	 * @param theClass
	 * @return
	 * @throws PropertyException
	 */
	public static Object getBReflectData(Object bussinessObject,Object property,Class theClass) throws PropertyException{
		Object objects = null;
		String columnName = null;
		if(property instanceof PropertyValue){
			columnName = ((PropertyValue)property).getName().substring(0,1).toUpperCase()+((PropertyValue)property).getName().substring(1);
		}else if(property instanceof PropertyObject){
			if(((PropertyObject)property).getName().indexOf("[")>=0){
				columnName = getUpperCase( ((PropertyObject)property).getJoinColumn()); 
			}else{
				columnName = getUpperCase(((PropertyObject)property).getName());
			}
		}

		columnName = ("get"+columnName);
		
		try{
			Class dynamicObjectClass = bussinessObject.getClass(); 
			
			Method method = null;
			Method[] methods = dynamicObjectClass.getMethods();
			for (int j = 0; j < methods.length; j++) {
				Method m = methods[j];
				if (columnName.equalsIgnoreCase(m.getName())) {
					method = m;
					break;
				}
			}
			
			if(method!=null){
				method = dynamicObjectClass.getMethod(method.getName());
				objects = method.invoke(bussinessObject);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objects;
	}
	
	/**
	 * 
	 * @param objects
	 * @param property
	 * @param theClass
	 * @return
	 * @throws PropertyException
	 */
	public static String[] getReflectAccordData(Object[][] objects,Object property,Class theClass) throws PropertyException{
		String[] object = new String[3];
		PropertyValue propertyValue = (PropertyValue)property;
		
		String objectValue = null;
		String fieldValue = null;
		try{
	        objectValue = FormatUtil.getStringType(objects[0][Integer.parseInt(propertyValue.getColumnSeq())-1]);

			while(true){
				Class classe = theClass;
					
				Field[] currentFields = classe.getDeclaredFields();
				for(int i=0;i<currentFields.length;i++){
					if(currentFields[i].getName().equalsIgnoreCase(propertyValue.getName())){
						 fieldValue = currentFields[i].getType().toString();
						 break;
					}
				}
					
				if(fieldValue==null){
					String superClassName = classe.getSuperclass().getName();
					
					if("java.lang.Object".equals(superClassName)){
						break;
					}else{
						theClass = classe.getSuperclass();
					}
				}else{
					break;
				}
			}
		} finally{
			object[0] = objectValue;
			object[1] = fieldValue;
			object[2] = propertyValue.getColumnTitle();
		}
		return object;
	}
	

	/**
	 * 替换数据库原始数据中对应的模板列值
	 * @param objects
	 * @param property
	 * @param obj
	 * @throws PropertyException
	 */
	public static void setUpdateEvaluate(Object[][] objects,Object property,Object obj) throws PropertyException{
		String columnName = "";
		Object columnValue = null;
		String name=null;
		if(property instanceof PropertyValue){
			columnName = ((PropertyValue)property).getName();
			columnValue = objects[0][Integer.parseInt(((PropertyValue)property).getColumnSeq())-1];
		}else if(property instanceof PropertyObject){
			name=((PropertyObject)property).getName();
			columnName=((PropertyObject)property).getName();
			if(columnName.indexOf("[")>=0){
				columnName = ((PropertyObject)property).getJoinColumn();
			}
			columnValue = objects[0][Integer.parseInt(((PropertyObject)property).getColumnSeq())-1];
		}

		try{
			Class classes = obj.getClass();
			while(true){
				Class clz = classes;

				String methodName = "set" + columnName;
				Method method = null;
				Method[] methods = clz.getDeclaredMethods();
				for (int j = 0; j < methods.length; j++) {
					Method m = methods[j];
					if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
						method = m;
						if(name!=null&&name.indexOf("[")>=0){
							method.invoke(obj,FormatUtil.getTypeConvert(method.getGenericParameterTypes()[0].toString(),columnValue));
						}else{
							method.invoke(obj, columnValue);
						}
						break;
					}
				}
					
				if(method==null){
					String superClassName = clz.getSuperclass().getName();
					
					if("java.lang.Object".equals(superClassName)){
						break;
					}else{
						classes = clz.getSuperclass();
					}
				}else{
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 友好类型翻译
	 * @param classType
	 * @return
	 */
	public static String getJavaDocConvert(String classType){
		String convertMsg = null;
		if(classType.toString().equals("char")||classType.toString().equals("class java.lang.Character")){
			convertMsg = "字符类型";
		}else if(classType.toString().equals("long")||classType.toString().equals("class java.lang.Long")){
			convertMsg = "长数字型";
		}else if(classType.toString().equals("int")||classType.toString().equals("class java.lang.Integer")){
			convertMsg = "短数字型";
		}else if(classType.toString().equals("short")||classType.toString().equals("class java.lang.Short")){
			convertMsg = "小范围数字型";
		}else if(classType.toString().equals("byte")||classType.toString().equals("class java.lang.Byte")){
			convertMsg = "二进制型";
		}else if(classType.toString().equals("double")||classType.toString().equals("class java.lang.Double")){
			convertMsg = "短精度小数点型";
		}else if(classType.toString().equals("float")||classType.toString().equals("class java.lang.Float")){
			convertMsg = "长精度小数点型";
		}else if(classType.toString().equals("BigDecimal")||classType.toString().equals("class java.math.BigDecimal")){
			convertMsg = "长精度小数点型";
		}else if(classType.toString().equals("class java.util.Date")){
			convertMsg = "时间类型";
		}
		return convertMsg;
	}
	
	/**
	 * 获取Excel表头信息
	 * @param propertValueList
	 * @return
	 */
	public static void setDynamicTitle(Object obj,Object objects){
		try{
			Class clz = obj.getClass();

			String[] conValue = new String[2];
			if(objects instanceof PropertyValue){
				conValue[0] = ((PropertyValue)objects).getColumnSeq();
				conValue[1] = ((PropertyValue)objects).getColumnTitle();
			}else if(objects instanceof PropertyObject){
				conValue[0] = ((PropertyObject)objects).getColumnSeq();
				conValue[1] = ((PropertyObject)objects).getColumnTitle();
			}
				
			String methodName = "setF" + conValue[0];
			Method method = null;
			Method[] methods = clz.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method m = methods[j];
				if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
					method = m;
					break;
				}
			}
			
			if(method!=null){
				method.invoke(obj, conValue[1]);
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Excel表头信息
	 * @param obj
	 * @param objects
	 * @param columnSeq
	 */
	public static void setDynamicTitle(Object obj, Object objects, int columnSeq){
		try{
			Class clz = obj.getClass();

			String[] conValue = new String[2];
			conValue[0] = String.valueOf(columnSeq+1);
			if(objects instanceof PropertyValue){
				conValue[1] = ((PropertyValue)objects).getColumnTitle();
			}else if(objects instanceof PropertyObject){
				conValue[1] = ((PropertyObject)objects).getColumnTitle();
			}
				
			String methodName = "setF" + conValue[0];
			Method method = null;
			Method[] methods = clz.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method m = methods[j];
				if (m.getName().equalsIgnoreCase(methodName) && (m.getParameterTypes().length == 1)) {
					method = m;
					break;
				}
			}
			
			if(method!=null){
				method.invoke(obj, conValue[1]);
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过表头与TITLE的匹配，进行ColumnSeq赋值
	 * @param propertyList
	 * @param objectList
	 * @param workFlag
	 * @return
	 * 			-- Object[]
	 *				- object[2]
	 * 				 + Boolean* true:正确 false:错误
	 * 		 		 + String* 错误原因
	 * @throws PropertyException
	 */
	public static Object[] setColumnSeq(List propertyList,List objectList,int workFlag) throws PropertyException{
		Object[] objectArray = new Object[2];
		objectArray[0] = new Boolean(true);
		try{
			for(int i=0;i<propertyList.size();i++){
				Object object = propertyList.get(i);
				
				boolean checkTitle = false;
				if(workFlag==1){
					DynamicObject dynamicObject = (DynamicObject)objectList.get(0);
					
					Class classes = dynamicObject.getClass();
					Method[] methodes = classes.getDeclaredMethods();
					
					for (int k=0;k<methodes.length;k++) {
						String name = methodes[k].getName();
						if(name.indexOf("getF")>-1){
							if(object instanceof PropertyValue){
								String dynamicString = FormatUtil.getStringType(methodes[k].invoke(dynamicObject));
								if(dynamicString.equalsIgnoreCase(((PropertyValue)object).getColumnTitle())){
									((PropertyValue)object).setColumnSeq(String.valueOf(i+1));
									propertyList.set(i, ((PropertyValue)object));
									checkTitle = true;
									break;
								}
							}else if(object instanceof PropertyObject){
								String dynamicString = FormatUtil.getStringType(methodes[k].invoke(dynamicObject));
								if(dynamicString.equalsIgnoreCase(((PropertyObject)object).getColumnTitle())){
									((PropertyObject)object).setColumnSeq(String.valueOf(i+1));
									propertyList.set(i, ((PropertyObject)object));
									checkTitle = true;
									break;
								}
							}
						}
					}
				}else if(workFlag==2){
					if(object instanceof PropertyValue){
						((PropertyValue)object).setColumnSeq(String.valueOf(i+1));
						propertyList.set(i, ((PropertyValue)object));
					}else if(object instanceof PropertyObject){
						((PropertyObject)object).setColumnSeq(String.valueOf(i+1));
						propertyList.set(i, ((PropertyObject)object));
					}
					checkTitle = true;
				}
				
				if(checkTitle==false){
					objectArray[0] = new Boolean(false);
					objectArray[1] = "EXCEL标题头与模板不匹配，请确认后重新导入";
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objectArray;
	}

	/**
	 * 通过类名和参数新建实例
	 * @param className 类名
	 * @param argTypes  参数类型数组(可为空)
	 * @param args      参数数组（为空为无参）
	 * @return
	 * @throws Exception
	 */
	public static Object newInstance(String className,Class[] argTypes,Object[] args) {
		try {
			Class newoneClass = Class.forName(className);
			argTypes=setParameters(argTypes,args);
			Constructor cons = newoneClass.getConstructor(argTypes);
			return cons.newInstance(args);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object newInstanceByName(String className) {
		try {
			Class newoneClass = Class.forName(className);
			return newoneClass.newInstance();
		} catch (ClassNotFoundException e) {
		} catch (SecurityException e) {
		}  catch (IllegalArgumentException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} 
		return null;
	}
	/**
	 * 执行某个类的静态方法
	 * @param className  类名
	 * @param methodName 方法名
	 * @param argTypes   参数类型数组(可为空)
	 * @param args       参数数组（为空为无参）
	 * @return
	 */
	public static Object invokeStaticMethod(String className,String methodName,Class[] argTypes,Object[] args){
		try {
			Class ownerClass = Class.forName(className);
			argTypes=setParameters(argTypes,args);
//			Method method = ownerClass.getMethod(methodName, argTypes);
			
			
			Method[] methods = ownerClass.getMethods();
			Method method = null;
		      for (int i = 0; i < methods.length; ++i) {
		        Method m = methods[i];
		        if (m.getName().equalsIgnoreCase(methodName)) {
		          Class[] parameterTypes = m.getParameterTypes();
		          if ((parameterTypes == args) || ((parameterTypes.length == 0) && (args == null))) {
		            method = m;
		            break;
		          }
		          if (parameterTypes.length == argTypes.length) {
		            boolean flag = true;
		            for (int j = 0; j < parameterTypes.length; ++j){
		              if (parameterTypes[j] != argTypes[j]){
		                flag = false;
		                method=null;
		                break;
		              }
		            }
		            if (flag) {
		              method = m;
		              break;
		            }
		          }
		        }
		      }

	      if (method != null)
	    	  return method.invoke(null, args);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 执行对象的方法
	 * @param obj　　　　　对象
	 * @param methodName 方法名
	 * @param argTypes   参数类型数组(可为空)
	 * @param args       参数数组（为空为无参）
	 * @return
	 */
	public static Object invokeMethod(Object obj,String methodName,Class[] argTypes,Object[] args){
		try {
			Class ownerClass = Class.forName(obj.getClass().getName());
			argTypes = setParameters(argTypes, args);
      Method[] methods = ownerClass.getMethods();
      Method method = null;
      for (int i = 0; i < methods.length; ++i) {
        Method m = methods[i];
        if (m.getName().equalsIgnoreCase(methodName)) {
          Class[] parameterTypes = m.getParameterTypes();
          if ((parameterTypes == args) || ((parameterTypes.length == 0) && (args == null))) {
            method = m;
            break;
          }
          if (parameterTypes.length == argTypes.length) {
            boolean flag = true;
            for (int j = 0; j < parameterTypes.length; ++j)
              if (parameterTypes[j] != argTypes[j])
                flag = false;


            if (flag) {
              method = m;
              break;
            }
          }
        }
      }

      if (method == null)
    	  return null;
      return method.invoke(obj, args);
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
		return null;
	}

 public static Object invokeMethodbyName(Object obj, String methodName, Class[] argTypes, Object[] args)
  {
    Class ownerClass;
    try
    {
      ownerClass = Class.forName(obj.getClass().getName());
      argTypes = setParameters(argTypes, args);
//      Method method = ownerClass.getMethod(methodName, argTypes);
      Method[] methods = ownerClass.getMethods();
		Method method = null;
	      for (int i = 0; i < methods.length; ++i) {
	        Method m = methods[i];
	        if (m.getName().equalsIgnoreCase(methodName)) {
	          Class[] parameterTypes = m.getParameterTypes();
	          if ((parameterTypes == args) || ((parameterTypes.length == 0) && (args == null))) {
	            method = m;
	            break;
	          }
	          if (parameterTypes.length == argTypes.length) {
	            boolean flag = true;
	            for (int j = 0; j < parameterTypes.length; ++j){
	              if (parameterTypes[j] != argTypes[j]){
	                flag = false;
	                method=null;
	                break;
	              }
	            }
	            if (flag) {
	              method = m;
	              break;
	            }
	          }
	        }
	      }

    if (method != null)
      return method.invoke(obj, args);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
	/**
	 * 如果没有给参数类型数组，则根据参数来获得类型
	 * @param argTypes  参数类型数组
	 * @param args      参数数组
	 */
	private static Class[] setParameters(Class[] argTypes,Object[] args){
		if((argTypes==null||argTypes.length<=0)&&null!=args&&args.length>0){
			argTypes = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				argTypes[i] = args[i].getClass();
			}
		}
		return argTypes;
	}
	
	/**
	 * 根据类的路径获得其所有属性，包括父类的
	 * @param bClassPath B类路径
	 * @return
	 */
	public static Field[] getAllFields(String bClassPath){
		Class bClass;
		Field fields[]=null;
		Field currentFields[]=null;
		Object obj=null;
		List list=new ArrayList();
		try {
			do{
				bClass = Class.forName(bClassPath);
				currentFields=bClass.getDeclaredFields();
				bClassPath=bClass.getSuperclass().getName();
				for(int i=0;i<currentFields.length;i++){
					if(!currentFields[i].getType().getName().equals("java.util.Vector")){
						list.add(currentFields[i]);
					}
				}
				if(bClassPath.equals("java.lang.Object")){
					break;
				}
			}while(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		fields=new Field[list.size()];
		for(int i=0;i<list.size();i++){
			fields[i]=(Field)list.get(i);
		}
		return fields;
	}
	
	/**
	 * 根据字段名获得字段
	 * @param bClassPath
	 * @param field
	 * @return
	 */
	public static Field getFieldbyName(String bClassPath,String fieldName){
		Class bClass=null;
		Field field=null;
		do{
			try {
				bClass = Class.forName(bClassPath);
				field = bClass.getDeclaredField(fieldName);
				if(field!=null){
					break;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				bClassPath=bClass.getSuperclass().getName();
				if(bClassPath.equals("java.lang.Object")){
					break;
				}
			}
		}while(true);
		return field;
	}
	
	/**
	 * 判断对象元素类和父类是否与class_column相符
	 * @param propertyList* -- 属性集合
	 * @return
	 * 		true:存在 false:不存在
	 *
	 * 判断对象元素类和父类是否与class_column相符
	 * @param propertyList* -- 属性集合
	 * @return
	 * 			-- Object[]
	 *				- object[2]
	 * 				 + Boolean* true:正确 false:错误
	 * 		 		 + String* 错误原因
	 * @throws PropertyException
	 */
	public static Object[] getClassColumnCheck(List propertyList) throws PropertyException{
		Object[] objectArray = new Object[2];
		objectArray[0] = new Boolean(true);
		try{
			for(int i=0;i<propertyList.size();i++){
				boolean nosErrorChk = false;
				if(propertyList.get(i) instanceof PropertyObject){
					PropertyObject propertyObjs = (PropertyObject)propertyList.get(i);
					Class classes = Class.forName(propertyObjs.getClassName());
					
					while(true){
						Class classe = classes;
							
						Method[] methode = classe.getDeclaredMethods();
						for (Method methodes : methode) {
							if(methodes.getName().equalsIgnoreCase("get" + propertyObjs.getClassColumn().substring(0, 1).toUpperCase() + propertyObjs.getClassColumn().substring(1))){
								nosErrorChk = true;
								break;
							}
						}
							
						if(nosErrorChk==false){
							String superClassName = classe.getSuperclass().getName();
							
							if("java.lang.Object".equals(superClassName)){
								break;
							}else{
								classes = classe.getSuperclass();
							}
						}else{
							break;
						}
					}
					
					if(nosErrorChk==false){
						objectArray[0] = new Boolean(false);
						objectArray[1] = "无法在模板对象"+propertyObjs.getClassName()+"元素中找到"+propertyObjs.getClassColumn()+"属性";
						break;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return objectArray;
	}
	public static boolean isObjectProperty(String type){
		if(
		type.toString().equals("java.lang.String")||
		   type.equals("char")||type.equals("java.lang.Character")||
		   type.equals("long")||type.equals("java.lang.Long")||
		   type.equals("int")||type.equals("java.lang.Integer")||
		   type.equals("short")||type.equals("java.lang.Short")||
		   type.equals("byte")||type.equals("java.lang.Byte")||
		   type.equals("double")||type.equals("java.lang.Double")||
		   type.equals("float")||type.equals("java.lang.Float")||		   
		   type.equals("boolean")||type.equals("java.lang.Boolean")||
		   type.equals("java.util.Date")){
			return false;
		}
		return true;
	}
	
	public static void main(String args[]){
//		System.out.println(getFieldbyName("gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BAcdcDistributionScreen","host").getType().getName());
	}
}

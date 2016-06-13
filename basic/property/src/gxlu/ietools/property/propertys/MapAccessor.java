/**************************************************************************
 * $$RCSfile: MapAccessor.java,v $$  $$Revision: 1.9 $$  $$Date: 2010/05/26 09:27:32 $$
 *
 * $$Log: MapAccessor.java,v $
 * $Revision 1.9  2010/05/26 09:27:32  zhangj
 * $增加特殊引用属性的处理
 * $
 * $Revision 1.8  2010/05/14 01:08:14  zhangj
 * $20100514更新
 * $
 * $Revision 1.7  2010/05/07 12:53:06  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:08  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.propertys;

import gxlu.ietools.basic.collection.util.DynamicObject;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.property.exception.PropertyException;
import gxlu.ietools.property.exception.PropertyNotFoundException;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.util.FormatUtil;
import gxlu.ietools.property.util.ReflectHelper;
import gxlu.ietools.property.util.SystemErrorNames;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author kidd
 */
public class MapAccessor implements PropertyAccessor {
	public Getter getGetter(Class theClass) throws PropertyNotFoundException {
		return new MapGetter(theClass);
	}

	public Setter getSetter(Class theClass) throws PropertyNotFoundException {
		return new MapSetter(theClass);
	}
	
	public static final class MapSetter implements Setter {
		private Class theClass;
		MapSetter(Class theClass) {
			this.theClass = theClass;
		}

		public Object[][] initPropertyArray(List params,ResultController resultCol) throws PropertyException{
			Object[][] objs = new Object[2][params.size()];
			DynamicObject dynamicObject = resultCol.getDynamicObject();
			for(int i=0;i<params.size();i++){
				Object properts = params.get(i);
				objs[0][i] = ReflectHelper.getReflectData(dynamicObject, properts, theClass);
			}
			
			int[] rowsCount = resultCol.getRowsCount();
			for(int j=0;j<rowsCount.length-1;j++){
				objs[1][j] = rowsCount[j];
			}
			objs[1][3] = resultCol.getNumRowsCount();
			
			return objs;
		}
		
		public void setAccordDataValue(List params,Object[][] objs) throws PropertyException{
			if(objs[0]!=null){
				int rowsNumber = FormatUtil.getRowsNumber(objs);
				for(int i=0;i<params.size();i++){
					Object objects = params.get(i);
					if(objects instanceof PropertyValue){
						PropertyValue propertyValue = (PropertyValue)objects;
						String[] object = ReflectHelper.getReflectAccordData(objs, propertyValue, theClass);
						
						Object[] obj = getTypeConvert(object,rowsNumber);
						
						if(obj==null){
							objs[0] = null;
							break;
						}else{
							objs[0][i] = obj[0];
//							if(((Boolean)obj[1]).booleanValue()){
//								objs[0][i] = obj[0];
//							}else{
//								objs[0][i] = null;
//							}
						}
					}
				}
			}
		}
		
		public void setObjectToObjectValue(List params,Object[][] object) throws PropertyException{
			if(object[0]!=null){
				Object[] obj = null;
				for(int i=0;i<params.size();i++){
					Object objects = params.get(i);
					if(objects instanceof PropertyObject){
						PropertyObject propertyObject = (PropertyObject)objects;
						obj = ReflectHelper.getObjectToObject(propertyObject, object);

						if(obj==null){
							object[0] = null;
							break;
						}else{
							if(((Boolean)obj[1]).booleanValue()){
								object[0][i] = obj[0];
							}else{
								object[0][i]=null;
							}
						}
					}
				}
			}
		}

		public void set(List params, Object[][] value, List dataList) throws PropertyException {
			if(value[0]!=null){
				try {
					Object obj = theClass.newInstance();
					
					for(int i=0;i<params.size();i++){
						Object objects = params.get(i);
						if(objects instanceof PropertyValue){
							PropertyValue propertyValue = (PropertyValue)params.get(i);
							Object paramValue= value[0][Integer.parseInt(propertyValue.getColumnSeq())-1];
							ReflectHelper.setValue(obj, propertyValue.getName(), paramValue);
						}else if(objects instanceof PropertyObject){
							PropertyObject propertyObject = (PropertyObject)params.get(i);
							Object paramValue= value[0][Integer.parseInt(propertyObject.getColumnSeq())-1];
							if(paramValue!=null&&!paramValue.equals("")){
								ReflectHelper.setObjectToObject(obj, propertyObject, paramValue);
							}
						}
					}

					Object[] objs = new Object[2];
					objs[0] = obj;
					objs[1] = Integer.valueOf(FormatUtil.getRowsNumber(value));
					dataList.add(objs);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public Object[] getTypeConvert(String[] object, int rowNumber){
			Object[] value = new Object[2];
			try{
				if(object[1].equals("char")||object[1].equals("class java.lang.Character")){
					if(object[0].length()>1){
						return null;
					}else{
						value[0] = object[0].charAt(0);
						value[1] = new Boolean(true);
					}
				}else if(object[1].equals("long")||object[1].equals("class java.lang.Long")){
					if(object[0]==null||object[0].equals("")){
						value[0] =Long.parseLong("-1");
						value[1] = new Boolean(false);
					}else{
						value[0] = Long.parseLong(object[0]);
						value[1] = new Boolean(true);
					}
				}else if(object[1].equals("int")||object[1].equals("class java.lang.Integer")){
					if(object[0]==null||object[0].equals("")){
						value[0] =Integer.parseInt("-1");
						value[1] = new Boolean(false);
					}else{
						value[0] = Integer.parseInt(object[0]);
						value[1] = new Boolean(true);
					}
				}else if(object[1].equals("short")||object[1].equals("class java.lang.Short")){
					if(object[0]==null||object[0].equals("")){
						value[0] =Short.parseShort("-1");
						value[1] = new Boolean(false);
					}else{
						value[0] = Short.parseShort(object[0]);
						value[1] = new Boolean(true);
					}
				}else if(object[1].equals("byte")||object[1].equals("class java.lang.Byte")){
					if(object[0]==null||object[0].equals("")){
						value[0] =Byte.parseByte("-1");
						value[1] = new Boolean(false);
					}else{
						value[0] = Byte.parseByte(object[0]);
						value[1] = new Boolean(true);
					}
				}else if(object[1].equals("double")||object[1].equals("class java.lang.Double")){
					if(object[0]==null||object[0].equals("")){
						value[0] =Double.parseDouble("-1");
						value[1] = new Boolean(false);
					}else{
						value[0] = Double.parseDouble(object[0]);
						value[1] = new Boolean(true);
					}
				}else if(object[1].equals("float")||object[1].equals("class java.lang.Float")){
					if(object[0]==null||object[0].equals("")){
						value[0] =Float.parseFloat("-1");
						value[1] = new Boolean(false);
					}else{
						value[0] = Float.parseFloat(object[0]);
						value[1] = new Boolean(true);
					}
				}else if ((object[1].equals("BigDecimal"))||(object[1].equals("class java.math.BigDecimal"))) {
			          if ((object[0] == null) || (object[0].equals(""))) {
			        	  value[0] =BigDecimal.valueOf(-1);
			              value[1] = new Boolean(false);
			          }else{
				          value[0] =new BigDecimal(object[0].toString());
				          value[1] = new Boolean(true);
			          }
			    }else if(object[1].equals("class java.util.Date")){
					if(object[0]==null||object[0].equals("")){
						value[0]=null;
						value[1] = new Boolean(false);
					}else{
			            SimpleDateFormat dateStr = new SimpleDateFormat("yyyy-MM-dd");
						value[0] = dateStr.parse(object[0]);
						value[1] = new Boolean(true);
					}
				}else{
					value[0] = object[0];
					value[1] = new Boolean(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Object[] errorObj = new Object[2];
				errorObj[0] = new Integer(rowNumber);
				errorObj[1] = SystemErrorNames.TypeConvertError + ": " + object[2] + "的数据类型应该为" + ReflectHelper.getJavaDocConvert(object[1]);
				PropertyAccessorFactory.addTypeConvertError(errorObj);
				value = null;
			}
			return value;
		}

		public void setDictionaryValue(List params, Object[][] objs) throws PropertyException {
			if(objs[0]!=null){
				// TODO Auto-generated method stub
				try {
					Object obj = theClass.newInstance();
	
					for(int i=0;i<params.size();i++){
						Object objects = params.get(i);
						if(objects instanceof PropertyValue){
							PropertyValue propertyValue = (PropertyValue)params.get(i);
							if(propertyValue.isDatadict()){
								Object[] dictionaryValue = ReflectHelper.setDictionary(obj, propertyValue, objs);

								if(dictionaryValue==null){
									objs[0] = null;
									break;
								}else{
									if(((Boolean)dictionaryValue[1]).booleanValue()){
										objs[0][i] = dictionaryValue[0];
									}
								}
							}
						}
					}
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void setUpdateEvaluate(List params, Object[][] object, List dataList) throws PropertyException {
			if(object[0]!=null){
				try {
					Object obj = theClass.newInstance();
	
					Object[] objs = null;
					for(int i=0;i<params.size();i++){
						Object objects = params.get(i);
						if(objects instanceof PropertyValue){
							PropertyValue propertyValue = (PropertyValue)params.get(i);
							if(propertyValue.getName().equalsIgnoreCase("id")){
								objs = ReflectHelper.getEvaluateValue(obj, propertyValue, object);
								break;
							}
						}
					}

					if(((Boolean)objs[1]).booleanValue()){
						object[0] = null;
					}else{
						//如果找到相关数据
						if(objs[0]!=null){
							for(int i=0;i<params.size();i++){
								Object objects = params.get(i);
								ReflectHelper.setUpdateEvaluate(object, objects, objs[0]);
							}
							
							Object[] objes = new Object[2];
							objes[0] = objs[0];
							objes[1] = Integer.valueOf(FormatUtil.getRowsNumber(object));
							dataList.add(objes);
							object[0] = null;
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final class MapGetter implements Getter {

		private Class theClass;

		MapGetter(Class theClass) {
			this.theClass = theClass;
		}

		public Object[][] initPropertyArray(List params,Object bobject) throws PropertyException{
			Object[][] objs = new Object[2][params.size()];

			for(int i=0;i<params.size();i++){
				Object properts = params.get(i);
				objs[0][i] = ReflectHelper.getBReflectData(bobject, properts, theClass);
			}
			
			return objs;
		}
		
		public void set(List params,Object[][] objs, List dataList) throws PropertyException{
			try {
				Object obj = theClass.newInstance();
				
				for(int i=0;i<params.size();i++){
					Object objects = params.get(i);
					if(objects instanceof PropertyValue){
						PropertyValue propertyValue = (PropertyValue)objects;
						Object paramValue= objs[0][Integer.parseInt(propertyValue.getColumnSeq())-1];
						ReflectHelper.setDynamicValue(obj, propertyValue, paramValue);
					}else if(objects instanceof PropertyObject){
						PropertyObject propertyObject = (PropertyObject)objects;
						Object paramValue= objs[0][Integer.parseInt(propertyObject.getColumnSeq())-1];
						ReflectHelper.setDynamicObjectToObject(obj, propertyObject, paramValue);
					}
				}

				dataList.add(obj);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void setTitle(List params,List dataList) throws PropertyException {
			try {
				Object obj = theClass.newInstance();
					
				for(int i=0;i<params.size();i++){
					Object objects = params.get(i);
					ReflectHelper.setDynamicTitle(obj, objects);
				}

				dataList.add(obj);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Object getTitle(List params) throws PropertyException {
			Object obj = null;
			try {
				obj = theClass.newInstance();
				for(int i=0;i<params.size();i++){
					Object objects = params.get(i);
					ReflectHelper.setDynamicTitle(obj, objects, i);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}

		public void setDictionaryValue(List params, Object[][] objs) throws PropertyException {
			// TODO Auto-generated method stub
			try {
				Object obj = theClass.newInstance();

				for(int i=0;i<params.size();i++){
					Object objects = params.get(i);
					if(objects instanceof PropertyValue){
						PropertyValue propertyValue = (PropertyValue)params.get(i);
						if(propertyValue.isDatadict()){
							String dictionaryValue = ReflectHelper.getDictionary(obj, propertyValue, objs);
							objs[0][i] = dictionaryValue;
						}
					}
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

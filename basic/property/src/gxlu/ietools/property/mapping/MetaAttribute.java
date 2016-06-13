/**************************************************************************
 * $$RCSfile: MetaAttribute.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:05 $$
 *
 * $$Log: MetaAttribute.java,v $
 * $Revision 1.6  2010/04/20 02:08:05  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.mapping;

import gxlu.ietools.basic.collection.util.DynamicObject;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.basic.system.util.VariableNames;
import gxlu.ietools.property.exception.PropertyException;
import gxlu.ietools.property.propertys.Getter;
import gxlu.ietools.property.propertys.PropertyAccessor;
import gxlu.ietools.property.propertys.PropertyAccessorFactory;
import gxlu.ietools.property.propertys.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A meta attribute is a named value or values.
 * @author kidd
 */
public class MetaAttribute extends MetaAttributable {

	protected Logger logger = Logger.getLogger(MetaAttribute.class);

	private Setter setter;
	private Getter getter;

	public MetaAttribute(Class theClass) {
		try{
			PropertyAccessor propertyAccessor = PropertyAccessorFactory.getDynamicMapPropertyAccessor();
			setter = propertyAccessor.getSetter(theClass);
			getter = propertyAccessor.getGetter(theClass);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		}
	}

	public MetaAttribute() {
		
	}
	
	public Setter getSetter() {
		return setter;
	}

	public Getter getGetter() {
		return getter;
	}


	public List getExportAttributes(Map metas) throws PropertyException{
		List bussinessObj = (List)metas.get(VariableNames.BUSSINESS_OBJECT);
		List propertyList = (List)metas.get(VariableNames.PROPERTY_VALUE);
		List dataList = (List)metas.get(VariableNames.DATA_VALUE);
		int[] rowsCount = (int[])metas.get(VariableNames.ROWS_COUNT);

		Iterator it = bussinessObj.iterator();
		while(it.hasNext()){
			Object bobj = (Object)it.next();
			
			Object[][] value = getter.initPropertyArray(propertyList, bobj);
				
			getter.setDictionaryValue(propertyList, value);
				
			getter.set(propertyList, value, dataList);
		}

		return dataList;
	}

	public List getImportAttribute(Map metas) throws PropertyException{
		List dynamicObj = (List)metas.get(VariableNames.DYNAMIC_OBJECT);
		List propertyList = (List)metas.get(VariableNames.PROPERTY_VALUE);
		List dataList = (List)metas.get(VariableNames.DATA_VALUE);
		int[] rowsCount = (int[])metas.get(VariableNames.ROWS_COUNT);

		int numRowsCount = 0;

		Iterator it = dynamicObj.iterator();
		while(it.hasNext()){
			DynamicObject dobj = (DynamicObject)it.next();

			ResultController rst = new ResultController();
			rst.setDynamicObject(dobj);
			rst.setRowsCount(rowsCount);
			rst.setNumRowsCount(numRowsCount++);
			
			Object[][] value = setter.initPropertyArray(propertyList,rst);
			
			setter.setDictionaryValue(propertyList,value);
			
			setter.setAccordDataValue(propertyList,value);
				
			setter.setObjectToObjectValue(propertyList, value);
				
			setter.setUpdateEvaluate(propertyList, value, dataList);
			
			setter.set(propertyList, value, dataList);
		}
		
		return dataList;
	}


	public DynamicObject getTitleAttribute(Map metas) throws PropertyException {
		List propertyList = (List)metas.get(VariableNames.PROPERTY_VALUE);
		
		DynamicObject dynamicObject = (DynamicObject)getter.getTitle(propertyList);
		return dynamicObject;
	}

}

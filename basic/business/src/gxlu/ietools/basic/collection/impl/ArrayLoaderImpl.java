package gxlu.ietools.basic.collection.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gxlu.ietools.basic.collection.ArrayLoader;
import gxlu.ietools.basic.collection.util.DynamicObject;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.basic.elements.Scraper;
import gxlu.ietools.basic.elements.variables.Variable;
import gxlu.ietools.basic.elements.variables.XmlNode;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.util.VariableNames;
import gxlu.ietools.property.exception.PropertyException;
import gxlu.ietools.property.mapping.MetaAttributable;
import gxlu.ietools.property.mapping.MetaAttribute;
import gxlu.ietools.property.util.ReflectHelper;

public class ArrayLoaderImpl extends ArrayLoader{

	public ResultController getBObjectListAction(Object bobject,List objectList,List propertyList,String bobjectInfo,int[] rowsCount,int workFlag) {
		ResultController resultController = new ResultController();
		
		try {
			if(workFlag==1){
				Map metas = new HashMap();
				metas.put(VariableNames.DYNAMIC_OBJECT, objectList);
				metas.put(VariableNames.PROPERTY_VALUE, propertyList);
				metas.put(VariableNames.ROWS_COUNT, rowsCount);
				metas.put(VariableNames.DATA_VALUE, new ArrayList());
				MetaAttributable metaAttr = new MetaAttribute((Class)bobject);
				List valueList = metaAttr.getImportAttribute(metas);
				
				Scraper scraper = new Scraper();
				scraper.setBobjectList(valueList);
				scraper.setXmlNode(XmlNode.getInstance(bobjectInfo));
				scraper.setMetas(metas);
				Variable variable = scraper.execute();
				ContainerFactory.addObjectData(variable.getObjectList());
			}else if(workFlag==2){
				Map metas = new HashMap();
				metas.put(VariableNames.BUSSINESS_OBJECT, objectList);
				metas.put(VariableNames.PROPERTY_VALUE, propertyList);
				metas.put(VariableNames.ROWS_COUNT, rowsCount);
				metas.put(VariableNames.DATA_VALUE, new ArrayList());
				MetaAttributable metaAttr = new MetaAttribute(DynamicObject.class);
				List valueList = metaAttr.getExportAttributes(metas);
				ContainerFactory.addObjectData(valueList);
			}
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		}
		return resultController;
	}

	public ResultController getTitleListAction(Object bobject) {
		ResultController resultController = new ResultController();
		try {
			Map metas = new HashMap();
			metas.put(VariableNames.PROPERTY_VALUE, propertyList);
			MetaAttributable metaAttr = new MetaAttribute(DynamicObject.class);
			DynamicObject dynamicObject = metaAttr.getTitleAttribute(metas);
			
			resultController.setObjectValueCount(propertyList.size());
			resultController.setDynamicObject(dynamicObject);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		}
		return resultController;
	}

	@Override
	protected Object[] getChkError(List propertyList, List objectList, int workFlag) {
		Object[] chkError = new Object[2];
		chkError[0] = new Boolean(true);
		try {
			chkError = ReflectHelper.setColumnSeq(propertyList, objectList, workFlag);
			//判断字段序列验证是否存在问题
			if(((Boolean)chkError[0]).booleanValue()==false){
				return chkError;
			}
			
			//判断对象元素类和父类是否与class_column相符
			chkError = ReflectHelper.getClassColumnCheck(propertyList);
			if(((Boolean)chkError[0]).booleanValue()==false){
				return chkError;
			}
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		}
		return chkError;
	}


}

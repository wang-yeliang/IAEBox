
package gxlu.ietools.basic.collection.util;

import gxlu.ietools.property.mapping.Property;

import java.io.*;
import java.util.*;

/**
 * Title:<p>
 * Description:<p>
 * Copyright:Copyright (c) 2005<p>
 * Company: <p>
 * @author kidd
 * @version 1.0
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ResultController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 对象值总数
	 */
	private int objectValueCount = 0;

	/**
	 * 对象集合
	 */
	private Collection result = null;
	
	private Object[] typeConvertError;
	
	private Property property;
	
	/**
	 * 操作结果信息
	 * 	-- Object[]
	 *		- object[2]
	 * 		 + Boolean* true:正确 false:错误
	 * 		 + String* 错误原因
	 */
	private Object[] operationMessage;
	
	private int[] rowsCount;
	
	private int numRowsCount;
	/**
	 * 动态对象
	 */
	private DynamicObject dynamicObject = null;
  private Map propertyMap = null;
  private Map objectListMap = null;
	
	public DynamicObject getDynamicObject() {
		return dynamicObject;
	}

	public void setDynamicObject(DynamicObject dynamicObject) {
		this.dynamicObject = dynamicObject;
	}

  public Map getPropertyMap()
  {
    return this.propertyMap;
  }

  public void setPropertyMap(Map propertyMap) {
    this.propertyMap = propertyMap;
  }

  public Map getObjectListMap()
  {
    return this.objectListMap;
  }

  public void setObjectListMap(Map objectListMap) {
    this.objectListMap = objectListMap;
  }

	public ResultController() {
		result = new ArrayList();
		dynamicObject = new DynamicObject();
	}
	
	public ResultController(Collection list) {
		this.result = list;
	}
	
	public void closeController(){
		this.result = null;
		this.dynamicObject = null;
	}

	/**
	 * @return
	 */
	public Collection getResult() {
		return result;
	}

	/**
	 * @param collection
	 */
	public void setResult(Collection collection) {
		result = collection;
	}

	public int getObjectValueCount() {
		return objectValueCount;
	}

	public void setObjectValueCount(int objectValueCount) {
		this.objectValueCount = objectValueCount;
	}

	public Object[] getTypeConvertError() {
		return typeConvertError;
	}

	public void setTypeConvertError(Object[] typeConvertError) {
		this.typeConvertError = typeConvertError;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public int[] getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int[] rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getNumRowsCount() {
		return numRowsCount;
	}

	public void setNumRowsCount(int numRowsCount) {
		this.numRowsCount = numRowsCount;
	}

	public Object[] getOperationMessage() {
		return operationMessage;
	}

	public void setOperationMessage(Object[] operationMessage) {
		this.operationMessage = operationMessage;
	}

}
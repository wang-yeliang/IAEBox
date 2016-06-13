/**************************************************************************
 * $$RCSfile: Setter.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:08 $$
 *
 * $$Log: Setter.java,v $
 * $Revision 1.6  2010/04/20 02:08:08  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.propertys;

import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.property.exception.PropertyException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Sets values to a particular property.
 * 
 * @author kidd
 */
public interface Setter extends Serializable {

	/**
	 * 初始化属性赋值
	 * @param params
	 * @return
	 * @throws PropertyException
	 */
	public Object[][] initPropertyArray(List params,ResultController resultCol) throws PropertyException;
	
	/**
	 * 利用反射机制判断数据类型
	 * 
	 * @param params 模板配置字段信息
	 * 		param0 --Object*-- Property/Object-to-Object元素值
	 * 
	 * @param resultCol
	 * 		dynamicObject -- Excel对象数据
	 * @return
	 * @throws PropertyException
	 */
	public void setAccordDataValue(List params,Object[][] objs) throws PropertyException;
	
	/**
	 * 对字典类元素进行导入转换
	 * @param params
	 * @param objs
	 * @throws PropertyException
	 */
	public void setDictionaryValue(List params,Object[][] objs) throws PropertyException;
	
	/**
	 * B类型对象赋值及过滤返回为空对象
	 * @param params
	 * @param object
	 * @throws PropertyException
	 */
	public void setObjectToObjectValue(List params,Object[][] object) throws PropertyException;
	
	/**
	 * 导入修改项赋值
	 * @param params
	 * @param object
	 * @throws PropertyException
	 */
	public void setUpdateEvaluate(List params,Object[][] object, List dataList) throws PropertyException;
	
	/**
	 * 使用反射机制对应B类并赋值
	 * @param params 模板配置字段信息	  
	 *	 param0 --Object*-- Property/Object-to-Object元素值
	 *
	 * @param value 对象数组
	 * @throws PropertyException
	 */
	public void set(List params, Object[][] value, List dataList) throws PropertyException;
	
	/**
	 * 类型转换验证
	 * @param object 从Excel中读取的值
	 * 		-- Object[]
	 *			- object[2]
	 * 				+ boolean* true:正确 false:错误
	 * 		 		+ 
	 * @param params 模板配置字段信息
	 * 		param0 --String*-- 从Excel中读取的值
	 * 		param1 --String* -- 对象类型
	 * 		param2 --String*-- Title
	 * 
	 * @param rowNumber Excel行数
	 * @return
	 * @throws PropertyException
	 */
	public Object[] getTypeConvert(String[] object,int rowNumber);

}

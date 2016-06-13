/**************************************************************************
 * $$RCSfile: Getter.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:08 $$
 *
 * $$Log: Getter.java,v $
 * $Revision 1.6  2010/04/20 02:08:08  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.propertys;

import gxlu.ietools.property.exception.PropertyException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Gets values of a particular property
 *
 * @author kidd
 */
public interface Getter extends Serializable {

	/**
	 * 初始化属性赋值
	 * @param params
	 * @return
	 * @throws PropertyException
	 */
	public Object[][] initPropertyArray(List params,Object bobject) throws PropertyException;
	
	/**
	 * 使用反射机制对应动态类并赋值
	 * @param params 模板配置字段信息
	 *	 param0 --Property*-- Property/Object-to-Object元素值
	 *
	 * @param bobject B类对象
	 * @throws PropertyException
	 */
	public void set(List params,Object[][] objs,List dataList) throws PropertyException;
	
	/**
	 * 对字典类元素进行导出转换
	 * @param params
	 * @param objs
	 * @throws PropertyException
	 */
	public void setDictionaryValue(List params,Object[][] objs) throws PropertyException;

	/**
	 * 使用反射机制获得表头对象并存入容器
	 * @param params --Property*-- Property/Object-to-Object元素值
	 * @param dataList 
	 * @param rowNumber
	 * @throws PropertyException
	 */
	public void setTitle(List params, List dataList) throws PropertyException;
	
	/**
	 * 使用反射机制返回表头对象
	 * @param params --Property*-- Property/Object-to-Object元素值
	 * @return
	 * @throws PropertyException
	 */
	public Object getTitle(List params) throws PropertyException;

}

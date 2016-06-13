package gxlu.ietools.client.query;

import gxlu.ietools.basic.elements.variables.XmlNode;
import gxlu.ietools.property.mapping.Property;

import java.util.HashMap;
import java.util.List;

public interface QueryConditionParseIFC
{

	/**
	 * 组装属性对象
	 * 
	 * @return 封装成Property后的B类集合
	 */
	public HashMap readDomParseList();

	/**
	 * 读取模板文件信息至属性对象
	 * 
	 * @param params --应用参数
	 * 
	 *            param0 --String*-- 模板文件路径
	 * 
	 * @return 属性对象
	 * 
	 * @throws Exception
	 */
	public abstract Object readDomParse(String file);

	/**
	 * 
	 * @param params param0--String B类路径
	 * @return
	 */
	public List getAllPropertyList();

	/**
	 * 把所有支持导入导出的B类封装成Property
	 * 
	 * @param params param0--String B类路径
	 * @return 封装成Property后的B类集合
	 */
	public abstract Object getAllProperty(List params);

	/**
	 * 对读取的属性进行排序
	 * 
	 * @param 封装后的XML对象
	 */
	public void propertyOrderbyCol(Property property);

	/**
	 * 改写模板属性信息
	 * 
	 * @param params --应用参数
	 * 
	 *            param0 --String*-- 模板文件路径
	 * 
	 * @param object --属性对象信息
	 * 
	 * @return rst0 --Boolean*-- true=正确 false=错误 rst1 --String*-- 错误原因
	 */
	public abstract List writeDomParse(List params, Object object);

	/**
	 * 返回模板文件路径，如gxlu/ietools/property/template/wlansystem.xml
	 * 
	 * @return String*-- 路径
	 */
	public abstract List getTemplateFileList();

	/**
	 * 读取模板约束信息
	 * 
	 * @param params --应用参数
	 * 
	 *            param0 --String*-- 模板文件路径
	 * 
	 * @return
	 */
	public abstract XmlNode[] parseElements(String inFile);
}

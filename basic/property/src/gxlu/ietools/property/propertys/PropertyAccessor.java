/**************************************************************************
 * $$RCSfile: PropertyAccessor.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:08 $$
 *
 * $$Log: PropertyAccessor.java,v $
 * $Revision 1.6  2010/04/20 02:08:08  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.propertys;

import gxlu.ietools.property.exception.PropertyNotFoundException;

/**
 * Abstracts the notion of a "property". Defines a strategy for accessing the
 * value of an attribute.
 * @author kidd
 */
public interface PropertyAccessor {

	/**
	 * 导出依赖
	 * @param theClass B类
	 * @param propertyName 模板文件名称
	 * @return
	 * @throws PropertyNotFoundException
	 */
	public Getter getGetter(Class theClass) throws PropertyNotFoundException;

	/**
	 * 导入依赖
	 * @param theClass B类对象
	 * @return
	 * @throws PropertyNotFoundException
	 */
	public Setter getSetter(Class theClass) throws PropertyNotFoundException;
}

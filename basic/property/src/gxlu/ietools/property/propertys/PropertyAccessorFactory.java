/**************************************************************************
 * $$RCSfile: PropertyAccessorFactory.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:08 $$
 *
 * $$Log: PropertyAccessorFactory.java,v $
 * $Revision 1.6  2010/04/20 02:08:08  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.propertys;

import gxlu.ietools.basic.system.container.Container;
import gxlu.ietools.basic.system.container.ContainerException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.property.exception.PropertyException;

/**
 * A factory for get/set PropertyAccessor instances.
 *
 * @author kidd
 */
public final class PropertyAccessorFactory {

	private static final PropertyAccessor MAP_ACCESSOR = new MapAccessor();
	
	private static final Container container = ContainerFactory.getContainer();

	public static PropertyAccessor getDynamicMapPropertyAccessor() throws PropertyException {
		return MAP_ACCESSOR;
	}
	
	/**
	 * 添加转换错误数组到容器
	 * @param obj
	 */
	public static void addTypeConvertError(Object[] obj) {
		container.addTypeConvertError(obj);
	}
	
	/**
	 * 从容器中获取转换错误容器内容
	 * @return
	 */
	public static Object[] getTypeConvertError() {
		Object[] obj = null;
		try {
			Context context = ExectionUtil.getContext();
			obj = (Object[])context.lookupTypeConvertError();
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 删除转换错误容器内容
	 */
	public static void removeTypeConvertError(){
		try {
			Context context = ExectionUtil.getContext();
			context.removeTypeConvertError();
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将标签值数据放入容器
	 * @param beanInfo
	 */
	public static void addTagData(Object bobject,String objectInfo) {
		container.addTagData(bobject,objectInfo);
	}
	
	/**
	 * 从容器中获取标签值
	 * @return
	 */
	public static Object getTagData(String objectInfo) {
		Object objects = null;
		try {
			Context context = ExectionUtil.getContext();
			objects = context.lookupTagInfo(objectInfo);
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objects;
	}
	
	/**
	 * 删除容器中标签值
	 */
	public static void removeTagData(String objectInfo){
		try {
			Context context = ExectionUtil.getContext();
			context.removeTagInfo(objectInfo);
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

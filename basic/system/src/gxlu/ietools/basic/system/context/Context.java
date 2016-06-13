/**************************************************************************
 * $$RCSfile: Context.java,v $$  $$Revision: 1.7 $$  $$Date: 2010/05/06 07:10:44 $$
 *
 * $$Log: Context.java,v $
 * $Revision 1.7  2010/05/06 07:10:44  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import gxlu.ietools.basic.system.container.ContainerException;

/**
 * @author kidd
 * @version 1.0
 */
public interface Context {

	/**
	 * 
	 * @param name
	 * @return
	 * @throws ContainerException
	 */
	public SessionBean lookupSessionBean(String name) throws ContainerException;

	/**
	 * 加载元素过滤机制涉及类
	 * 
	 * @param name
	 * @return
	 * @throws ContainerException
	 */
	public Object lookupElementDef(String name) throws ContainerException;

	/**
	 * 使用模板路径返回B类
	 * 
	 * @param objectInfo
	 * @return
	 * @throws ContainerException
	 */
	public Class lookupBObject(String objectInfo) throws ContainerException;

	/**
	 * 使用B类返回模板路径
	 * 
	 * @param objectClass
	 * @return
	 * @throws ContainerException
	 */
	public String lookupBObjectInfo(Object objectClass)
			throws ContainerException;

	/**
	 * 输出数据
	 * 
	 * @param name
	 * @return
	 * @throws ContainerException
	 */
	public Object lookupTagInfo(String name) throws ContainerException;

	/**
	 * 删除指定标签值
	 * 
	 * @param name
	 * @throws ContainerException
	 */
	public void removeTagInfo(String name) throws ContainerException;

	/**
	 * 获取容器中的错误原因
	 * 
	 * @return objects[] - object[2] + Integer* 行数 + String* 错误原因
	 * @throws ContainerException
	 */
	public Object[] lookupTypeConvertError() throws ContainerException;

	/**
	 * 清空容器中的错误原因
	 * 
	 * @throws ContainerException
	 */
	public void removeTypeConvertError() throws ContainerException;

	/**
	 * @throws ContainerException
	 */
	public LinkedList lookupObjectList() throws ContainerException;

	/**
	 * @throws ContainerException
	 */
	public void removeObjectList() throws ContainerException;

	public abstract Map lookupAllProperty() throws ContainerException;

	public abstract void removeAllProperty() throws ContainerException;

	public abstract Map lookupObjectListByKey(Object paramObject) throws ContainerException;

	public abstract Map lookupObjectListMap() throws ContainerException;

	public abstract void removeObjectListByObjkey(Object paramObject) throws ContainerException;

	public abstract void removeAllObjectListMap() throws ContainerException;
}
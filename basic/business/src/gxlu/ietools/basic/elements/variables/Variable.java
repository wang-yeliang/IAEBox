/**************************************************************************
 * $$RCSfile: Variable.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:06 $$
 *
 * $$Log: Variable.java,v $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.variables;

import java.util.*;

/**
 * Variables Interface.
 * @author kidd
 */
public abstract class Variable {

	/**
	 * 反射方法返回对象
	 * @return
	 */
	abstract public Object getWrappedObject();
    
    /**
     * 放入返回集合
     * @param object
     */
    abstract public void setObjectList(Object object);

    /**
     * 输出集合对象赋值
     * @param object
     */
    abstract public void setWrappedObject(Object object);
    
    /**
     * 返回获取集合
     * @return
     */
    abstract public List getObjectList();
    
    /**
     * 确认集合中符合删除条件的对象
     * @param value
     * @return
     */
    abstract public boolean getCheckVariable(Object value);

    /**
     * Safely converts this variable to array of objects.
     * @return array of objects
     */
    public List toArray() {
        Object wrappedObject = getWrappedObject();
        if (wrappedObject == null) {
        	return new ArrayList();
        }else{
        	return (List)wrappedObject;
        }
        
    }

    /** xmlNode数组个数 **/
	public int xmlNodeLen = 0;

    public int getXmlNodeLen() {
		return xmlNodeLen;
	}

	public void setXmlNodeLen(int xmlNodeLen) {
		this.xmlNodeLen = xmlNodeLen;
	}
}
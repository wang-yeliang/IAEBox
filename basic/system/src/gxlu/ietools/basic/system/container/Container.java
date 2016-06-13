/**************************************************************************
 * $$RCSfile: Container.java,v $$  $$Revision: 1.7 $$  $$Date: 2010/05/06 07:10:44 $$
 *
 * $$Log: Container.java,v $
 * $Revision 1.7  2010/05/06 07:10:44  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.container;

import java.util.List;

import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.property.mapping.Property;

/** 
 *
 * @author Kidd
 * @version 1.0
 */
public interface Container {

    /**
     * 容器控制器
     * @return
     */
    public abstract Context newContext();
    
    /**
     * 加载会话处理类
     * @param beanName
     * @param beanInfo
     */
    public abstract void addSessionBean(String beanName, String beanInfo);
    
    /**
     * 加载元素过滤机制涉及类
     * @param beanName
     * @param beanInfo
     */
    public abstract void addElementDef(String beanName, String beanInfo);
    
    /**
     * 加载B类至容器
     * @param objectInfo
     * @param bobject
     */
    public abstract void addBobject(String objectInfo, Object bobject);
    
    /**
     * 加载模板路径至容器
     * @param bobject
     * @param objectInfo
     */
    public abstract void addBobjectInfo(Object bobject,String objectInfo);
    
    /**
     * 加载标签值
     * @param object
     */
    public abstract void addTagData(Object bobject,String objectInfo);

    /**
     * 加载类型错误
     * @param object
     */
    public abstract void addTypeConvertError(Object[] object);
    
    /**
     * 加载输出数据
     * @param object
     */
    public abstract void addObjectData(List objectList);

public abstract void addProperty(Property paramProperty);

  public abstract void addobjectListMap(Object paramObject1, String paramString1, String paramString2, Object paramObject2);

}

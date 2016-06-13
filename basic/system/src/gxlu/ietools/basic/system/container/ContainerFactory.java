/**************************************************************************
 * $$RCSfile: ContainerFactory.java,v $$  $$Revision: 1.7 $$  $$Date: 2010/05/06 07:10:44 $$
 *
 * $$Log: ContainerFactory.java,v $
 * $Revision 1.7  2010/05/06 07:10:44  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:05  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.container;

import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ArrayUtil;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.property.mapping.Property;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author  Kidd
 */
public class ContainerFactory {
    
    private static Container container = null;
    
    public synchronized static Container getContainer() {
        if(container == null) {
        	container = (Container) ContainerImpl.getInstance();
        }
        return container;
    }
    
    public static void setContainer(Container con) {
        container = con;
    }
    
	/**
	 * 
	 * @param objectList
	 */
	public static void addObjectData(List objectList) {
		container.addObjectData(objectList);
	}
	
	/**
	 * 
	 * @return
	 */
	public static LinkedList getObjectList() {
		LinkedList objectList = null;
		try {
			Context context = ExectionUtil.getContext();
			objectList = ArrayUtil.getStringType((LinkedList)context.lookupObjectList());
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objectList;
	}

  public static void addProperty(Property property) {
    container.addProperty(property); 
  }

  public static Map getAllProperty() {
    Map map = null;
    try {
      Context context = ExectionUtil.getContext();
      map = context.lookupAllProperty();
    } catch (ContainerException e) {
      e.printStackTrace();
    }
    return map;
  }

  public static void addobjectListMap(Object objKey, String sequence, String object_case, Object objValue) {
    container.addobjectListMap(objKey, sequence, object_case, objValue); 
  }

  public static Map getObjectListByKey(Object objKey) {
    Map bObjectMap = null;
    try {
      Context context = ExectionUtil.getContext();
      bObjectMap = context.lookupObjectListByKey(objKey);
    } catch (ContainerException e) {
      e.printStackTrace();
    }
    return bObjectMap; }

  public static Map getObjectListMap() {
    Map bObjectMap = null;
    try {
      Context context = ExectionUtil.getContext();
      bObjectMap = context.lookupObjectListMap();
    } catch (ContainerException e) {
      e.printStackTrace();
    }
    return bObjectMap; }

  public static void removeObjectListMap(Object objKey) {
    Context context;
    try { context = ExectionUtil.getContext();
      context.removeObjectListByObjkey(objKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

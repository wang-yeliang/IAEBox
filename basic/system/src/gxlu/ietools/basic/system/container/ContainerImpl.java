/**************************************************************************
 * $$RCSfile: ContainerImpl.java,v $$  $$Revision: 1.7 $$  $$Date: 2010/05/06 07:10:44 $$
 *
 * $$Log: ContainerImpl.java,v $
 * $Revision 1.7  2010/05/06 07:10:44  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:05  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.container;

import gxlu.ietools.basic.system.context.ContextImpl;
import gxlu.ietools.basic.system.context.SessionBean;
import gxlu.ietools.basic.system.context.SessionBeanImpl;
import gxlu.ietools.property.mapping.Property;

import java.lang.reflect.*;
import java.util.*;

import org.apache.log4j.*;



/**
 *
 * @author kidd
 *
 * @version 1.0
 *
 */
public class ContainerImpl implements Container{
	
	private static ContainerImpl container = null;

	private Hashtable sessionBeanTable = new Hashtable();
	
	private Hashtable elementDefTable = new Hashtable();
	
	private Hashtable bobjectTable = new Hashtable();
	
	private Hashtable bobjectInfoTable = new Hashtable();
	
	private Hashtable tagInfoTable = new Hashtable();
	
	private LinkedList objectList = new LinkedList();
	
	private ArrayList typeConvertErrorTable = new ArrayList();
private Map propertyMap = new HashMap();
  private Map objectListMap = new HashMap();
	protected Logger logger = Logger.getLogger(ContainerImpl.class);



	ContainerImpl() {
		//        this(DEFAULTDB_NAME);
		container = this;
		ContainerFactory.setContainer(this);
	}
    


	public static ContainerImpl getInstance() {
		if (container == null)
			return new ContainerImpl();
		else
			return container;
	}

	/* (non-Javadoc)
	 * @see com.taxation.common.container.Container#addSessionBean(java.lang.String, java.lang.String)
	 */
	public void addSessionBean(String beanName, String beanInfo) {
		sessionBeanTable.put(beanName, beanInfo);
		logger.info("SessionBean added:" + beanName);
	}

	public void addElementDef(String beanName, String beanInfo) {
		elementDefTable.put(beanName, beanInfo);
		logger.info("ElementDef added:" + beanName);
	}
	
	/* (non-Javadoc)
	 * @see gxlu.ietools.basic.system.container.Container#addBobject(java.lang.String, java.lang.Object)
	 */
	public void addBobject(String objectInfo, Object bobject) {
		bobjectTable.put(objectInfo, bobject);
		logger.info("BObject added:" + objectInfo);
	}
	
	public void addBobjectInfo(Object bobject,String objectInfo) {
		bobjectInfoTable.put(bobject,objectInfo);
		logger.info("BObjectInfo added:" + objectInfo);
	}

	public void addTypeConvertError(Object[] object){
		typeConvertErrorTable.add(object);
	}
	
	public void addTagData(Object bobject,String objectInfo) {
		tagInfoTable.put(bobject,objectInfo);
		logger.info("TagInfo added:" + objectInfo);
	}
	
	/**
	 * Method lookupSessionBean.
	 * @param sessionName
	 * @return SessionBean
	 * @throws ContainerException
	 */
	public SessionBean lookupSessionBean(String beanName) throws ContainerException{
		String beanValue = (String)sessionBeanTable.get(beanName);
		try {
			Class c = Class.forName(beanValue);
			Constructor constructor = c.getConstructor(null);
			SessionBeanImpl sessionBean = (SessionBeanImpl)constructor.newInstance((Object[])null);
			logger.debug("Session Bean created:" + beanName);
			return sessionBean;
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	public Object lookupElementDef(String name) throws ContainerException{
		String beanValue = (String)elementDefTable.get(name);
		if(beanValue==null){
			return null;
		}else{
			try {
				Class c = Class.forName(beanValue);
				Constructor constructor = c.getConstructor(null);
				Object baseInterface = constructor.newInstance((Object[])null);
				logger.debug("BaseInterface created:" + name);
				return baseInterface;
			} catch (Exception e) {
				throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
			}
		}

	}
	
	public Class lookupBObject(String objectInfo) throws ContainerException{
		Class c = (Class)bobjectTable.get(objectInfo);
		try {
			return c;
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public String lookupBObjectInfo(Object objectClass) throws ContainerException{
		String c = (String)bobjectInfoTable.get(objectClass);
		try {
			return c;
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public Object lookupTagInfo(String name) throws ContainerException{
		try {
			return tagInfoTable.get(name);
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void removeTagInfo(String name) throws ContainerException{
		try {
			bobjectInfoTable.remove(name);
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	public Object[] lookupTypeConvertError() throws ContainerException{
		try {
			return (Object[]) typeConvertErrorTable.toArray(new Object[0]);
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void removeTypeConvertError() throws ContainerException{
		try {
			typeConvertErrorTable = new ArrayList();
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public gxlu.ietools.basic.system.context.Context newContext() {
		ContextImpl context = new ContextImpl();
		context.setContainer(this);
		logger.debug("create new context");
		return context;
	}

    public void addObjectData(List objectList){
    	this.objectList.addAll(objectList);
    }
    
	public LinkedList lookupObjectList() throws ContainerException{
		try {
			return objectList;
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void removeObjectList() throws ContainerException{
		try {
			objectList = new LinkedList();
		} catch (Exception e) {
			throw new ContainerException(e.getClass().getName() + ":" + e.getMessage());
		}
	}

  public void addProperty(Property property) {
    if (this.propertyMap.get(property.getBclass()) == null)
      this.propertyMap.put(property.getBclass(), property);
  }

  public Map lookupAllProperty() throws gxlu.ietools.basic.system.container.ContainerException {
    try {
      return this.propertyMap;
    } catch (Exception e) {
      throw new gxlu.ietools.basic.system.container.ContainerException(e.getClass().getName() + ":" + e.getMessage()); }
  }

  public void removeAllProperty() throws gxlu.ietools.basic.system.container.ContainerException {
    try {
      this.propertyMap = new HashMap();
    } catch (Exception e) {
      throw new gxlu.ietools.basic.system.container.ContainerException(e.getClass().getName() + ":" + e.getMessage());
    }
  }

  public void addobjectListMap(Object objKey, String sequence, String object_Case, Object objValue) {
    if (this.objectListMap.get(objKey) == null)
      this.objectListMap.put(objKey, new HashMap());

    if ((sequence.equals("FRONT")) || (sequence.equals("BEHIND")) || (sequence.equals("SELF"))) {
      if (((Map)this.objectListMap.get(objKey)).get(sequence) == null)
        ((Map)this.objectListMap.get(objKey)).put(sequence, new HashMap());

      if ((object_Case.equals("INSERT")) || (object_Case.equals("UPDATE")) || (object_Case.equals("CONVERTOR")) || (object_Case.equals("OLDMETHOD"))) {
        if (((Map)((Map)this.objectListMap.get(objKey)).get(sequence)).get(object_Case) == null)
          ((Map)((Map)this.objectListMap.get(objKey)).get(sequence)).put(object_Case, new ArrayList());

        ((List)((Map)((Map)this.objectListMap.get(objKey)).get(sequence)).get(object_Case)).add(objValue); }
    }
  }

  public Map lookupObjectListByKey(Object objKey) throws gxlu.ietools.basic.system.container.ContainerException {
    try {
      return ((this.objectListMap.get(objKey) != null) ? (Map)this.objectListMap.get(objKey) : null);
    } catch (Exception e) {
      throw new gxlu.ietools.basic.system.container.ContainerException(e.getClass().getName() + ":" + e.getMessage()); }
  }

  public Map lookupObjectListMap() throws gxlu.ietools.basic.system.container.ContainerException {
    try {
      return this.objectListMap;
    } catch (Exception e) {
      throw new gxlu.ietools.basic.system.container.ContainerException(e.getClass().getName() + ":" + e.getMessage()); }
  }

  public void removeObjectListByObjkey(Object objKey) throws gxlu.ietools.basic.system.container.ContainerException {
    try {
      if (this.objectListMap.get(objKey) == null) return;
      this.objectListMap.remove(objKey);
    }
    catch (Exception e) {
      throw new gxlu.ietools.basic.system.container.ContainerException(e.getClass().getName() + ":" + e.getMessage()); }
  }

  public void removeAllObjectListMap() throws gxlu.ietools.basic.system.container.ContainerException {
    try {
      this.objectListMap = new HashMap();
    } catch (Exception e) {
      throw new gxlu.ietools.basic.system.container.ContainerException(e.getClass().getName() + ":" + e.getMessage());
    }
  }
}

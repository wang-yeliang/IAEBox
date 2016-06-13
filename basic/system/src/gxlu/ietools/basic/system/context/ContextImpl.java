/**************************************************************************
 * $$RCSfile: ContextImpl.java,v $$  $$Revision: 1.7 $$  $$Date: 2010/05/06 07:10:44 $$
 *
 * $$Log: ContextImpl.java,v $
 * $Revision 1.7  2010/05/06 07:10:44  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.context;

import gxlu.ietools.basic.system.container.Container;
import gxlu.ietools.basic.system.container.ContainerException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.container.ContainerImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.*;


/** context implementation of the db container
 *
 * @author kidd
 * @version 1.0
 */
public class ContextImpl implements Context {
    protected Logger logger = Logger.getLogger(ContextImpl.class);

    private Container container = null;

    /**
	 * @return Container
	 */
	private Container getContainer(){
    	return ContainerFactory.getContainer();
    }

    public SessionBean lookupSessionBean(String name) throws ContainerException {
        SessionBeanImpl sessionBean = (SessionBeanImpl)((ContainerImpl)getContainer()).lookupSessionBean(name);
		sessionBean.setContext(this);
        logger.debug("Session Bean created from context:" + name);
        return sessionBean;
    }
    
    public Object lookupElementDef(String name) throws ContainerException {
    	Object baseInterface = (Object)((ContainerImpl)getContainer()).lookupElementDef(name);
        return baseInterface;
    }
    
    public Class lookupBObject(String objectInfo) throws ContainerException {
    	Class bobject = (Class)((ContainerImpl)getContainer()).lookupBObject(objectInfo);
        logger.debug("Object created from context:" + bobject);
        return bobject;
    }
    
    public String lookupBObjectInfo(Object object) throws ContainerException {
    	String bobjectInfo = (String)((ContainerImpl)getContainer()).lookupBObjectInfo(object);
        logger.debug("ObjectInfo created from context:" + bobjectInfo);
        return bobjectInfo;
    }
    
    public Object lookupTagInfo(String name) throws ContainerException {
    	Object tagInfo = ((ContainerImpl)getContainer()).lookupTagInfo(name);
        logger.debug("TagInfo created from context:" + tagInfo);
        return tagInfo;
    }
    
	public void removeTagInfo(String name) throws ContainerException {
		// TODO Auto-generated method stub
		((ContainerImpl)getContainer()).removeTagInfo(name);
	}

    /** set container of this context
     * @param container container
     */
    public void setContainer(Container container) {
        this.container = (ContainerImpl) container;        
    }

	public Object[] lookupTypeConvertError() throws ContainerException {
		// TODO Auto-generated method stub
		Object[] objectList = (Object[])((ContainerImpl)getContainer()).lookupTypeConvertError();
        return objectList;
	}

	public void removeTypeConvertError() throws ContainerException {
		// TODO Auto-generated method stub
		((ContainerImpl)getContainer()).removeTypeConvertError();
	}

	public LinkedList lookupObjectList() throws ContainerException {
		// TODO Auto-generated method stub
		LinkedList objectList = (LinkedList)((ContainerImpl)getContainer()).lookupObjectList();
        return objectList;
	}

	public void removeObjectList() throws ContainerException {
		// TODO Auto-generated method stub
		((ContainerImpl)getContainer()).removeObjectList();
	}

  public Map lookupAllProperty() throws ContainerException {
    return ((ContainerImpl)getContainer()).lookupAllProperty(); }

  public void removeAllProperty() throws ContainerException {
    ((ContainerImpl)getContainer()).removeAllProperty();
  }

  public Map lookupObjectListByKey(Object objKey) throws ContainerException
  {
    return ((ContainerImpl)getContainer()).lookupObjectListByKey(objKey); }

  public Map lookupObjectListMap() throws ContainerException {
    return ((ContainerImpl)getContainer()).lookupObjectListMap(); }

  public void removeObjectListByObjkey(Object objKey) throws ContainerException {
    ((ContainerImpl)getContainer()).removeObjectListByObjkey(objKey); }

  public void removeAllObjectListMap() throws ContainerException {
    ((ContainerImpl)getContainer()).removeAllObjectListMap();
  }
}
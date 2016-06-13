/**************************************************************************
 * $$RCSfile: BaseElementDef.java,v $$  $$Revision: 1.8 $$  $$Date: 2010/05/14 09:38:13 $$
 *
 * $$Log: BaseElementDef.java,v $
 * $Revision 1.8  2010/05/14 09:38:13  zhangj
 * $20100514更新
 * $
 * $Revision 1.7  2010/05/06 07:10:28  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:02  wudawei
 * $20100420
 * $$
 **************************************************************************/

package gxlu.ietools.basic.elements.definition;

import gxlu.afx.system.common.CommonContext;
import gxlu.afx.system.common.LocalServiceFactory;
import gxlu.afx.system.common.ServiceContainer;
import gxlu.afx.system.common.SysDictionaryFactory;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.query.client.BQueryClient;
import gxlu.afx.system.query.common.QueryExpr;
import gxlu.afx.system.query.common.QueryExprBuilder;
import gxlu.afx.system.security.common.ActiveUser;
import gxlu.afx.system.security.interfaces.SecurityIFC;
import gxlu.ietools.basic.elements.variables.XmlNode;
import gxlu.ietools.basic.exception.ElementsException;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.basic.system.util.PropertyConstvalue;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.util.ReflectHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Elements Interface.
 * @author kidd
 */
public abstract class BaseElementDef{

	private String name;

	private String targetObject;
	
	private String targetMethod;
	
	private String targetReturn;
	private static SecurityIFC securityIFC = null;
    public String getName() {
		return name;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public String getTargetReturn() {
		return targetReturn;
	}

	protected BaseElementDef() {
    }


	public void setName(String name) {
		this.name = name;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public void setTargetReturn(String targetReturn) {
		this.targetReturn = targetReturn;
	}
	
    /**
     * 创建元素结构
     * @param node
     * @return
     * @throws ElementsException
     */
    public static BaseElementDef createElementDefinition(XmlNode node)  throws ElementsException{
    	String elementClass = node.getName();
		try {
			Class c = null;
			
			Context context = ExectionUtil.getContext();
            Object object = context.lookupElementDef(elementClass);
            if(object==null){
            	c = Class.forName(elementClass);
            }else{
            	c = object.getClass();
            }
			
			Constructor constructor = c.getConstructor(null);
			BaseElementDef elementDef = (BaseElementDef)constructor.newInstance((Object[])null);
			elementDef.setName(node.getName());
			elementDef.setTargetObject(node.getTargetObject());
			elementDef.setTargetMethod(node.getTargetMethod());
			elementDef.setTargetReturn(node.getTargetReturn());
			return elementDef;
		} catch (Exception e) {
			throw new ElementsException(e.getClass().getName() + ":" + e.getMessage());
		}
    }

    /**
     * 反射静态类获取目标属性值
     * @param obj B类
     * @param targetObject 目标属性名称
     * @return
     * @throws ElementsException
     */
    public Object getFieldValue(Object obj,String targetObject)  throws ElementsException{
    	try{
            Class cla = obj.getClass();
            Method[] ma = cla.getMethods();//.getDeclaredMethods();
            Method method = null;
            String methodName = null;
            Object returnValue = null;
            for(int i=0;i<ma.length;i++){
                method=ma[i];
                methodName=method.getName();
                if(methodName.equalsIgnoreCase("get"+ReflectHelper.getUpperCase(targetObject))){
                    returnValue = method.invoke(obj, null);
                    break; 
                }
            }
            return returnValue;
    	} catch(Exception e){
    		throw new ElementsException(e.getClass().getName() + ":" + e.getMessage());
    	}
    }
    
    public Object setFieldValue(Object obj,String targetObject,Object value)  throws ElementsException{
    	try{
            Class cla = obj.getClass();
            Method[] ma = cla.getMethods();//.getDeclaredMethods();
            Method method = null;
            String methodName = null;
            Object returnValue = null;
            for(int i=0;i<ma.length;i++){
                method=ma[i];
                methodName=method.getName();
                if(methodName.equalsIgnoreCase("set"+ReflectHelper.getUpperCase(targetObject))){
                    returnValue = method.invoke(obj, new Object[]{value});
                    break; 
                }
            }
            return returnValue;
    	} catch(Exception e){
    		throw new ElementsException(e.getClass().getName() + ":" + e.getMessage());
    	}
    }
    public void addPropertyValue(Property property,String pvName){
    	if(property.getPropertyValue()==null){
    		property.setPropertyValue(new ArrayList());
    	}
    	PropertyValue propertyValue=new  PropertyValue();
		propertyValue.setName(pvName);
		property.getPropertyValue().add(propertyValue);
    }
    public void addPropertyObj(Property property,String poName,String joinColumn,String className){
    	if(property.getPropertyObject()==null){
    		property.setPropertyObject(new ArrayList());
    	}
    	PropertyObject propertyObject=new PropertyObject();
    	propertyObject.setName(poName);
    	propertyObject.setJoinColumn(joinColumn);
    	propertyObject.setClassName(className);
    	property.getPropertyObject().add(propertyObject);
    }
    public Object getBObject(BObjectInterface obj, String colName, String value, String assemble) {
	    Vector resultObj = getResults(obj, colName, value, assemble);
	    if ((resultObj != null) && (resultObj.size() > 0))
	      return resultObj.get(0);
	    return null;
    }

  public Object getBObject(Class bClass, String colName, String value, String assemble) {
	    Vector resultObj = getResults(bClass, colName, value, assemble);
	    if ((resultObj != null) && (resultObj.size() > 0))
	      return resultObj.get(0);
	    return null; 
  }
  public Object getResult(Class bClass, String colName, String value, String assemble) {
    return BQueryClient.getQueryResult(new QueryExprBuilder().get(colName).equal(value), bClass, assemble); 
  }
  public Vector getResults(Class bClass, String colName, String value, String assemble) {
    return BQueryClient.getQueryResults(new QueryExprBuilder().get(colName).equal(value), bClass, assemble); 
  }

  public Vector getResults(BObjectInterface obj, String colName, String value, String assemble) {
    return BQueryClient.getQueryResults(new QueryExprBuilder().get(colName).equal(value), obj.getClass(), assemble);
  }
  public Vector getResultByQueryExpr(BObjectInterface obj, QueryExpr expr, String assemble) {
    return BQueryClient.getQueryResults(expr, obj.getClass(), assemble); }

  public Vector getResultByQueryExpr(Class bClass, QueryExpr expr, String assemble) {
    return BQueryClient.getQueryResults(expr, bClass, assemble);
  }

  public static Object getLocalIFC(String localServiceName) {
    LocalServiceFactory localServiceFactory = CommonContext.getLocalServiceFactory();

    if (localServiceFactory == null)
    {
      System.err.println("EquipUtil::getLocalIFC() error: local service factory is null!");
      return null;
    }

    return localServiceFactory.getLocalService(localServiceName);
  }

  public ActiveUser getActiveUser(CommonContext context)
  {
    if (securityIFC == null)
      securityIFC = (SecurityIFC)ServiceContainer.getAnyOneService("system.security");

    if (securityIFC != null)
    {
      long securityKey = context.getSessionKey() >> 16 << 16;
      try
      {
        return securityIFC.getUserInfo(securityKey);
      }
      catch (RemoteException e)
      {
        System.err.println(e.getMessage());
        return null;
      }
    }

    return null;
  }
  public static final String NEW = SysDictionaryFactory.getSysDictionaryValueCN("DNSUBNEAUDITS","OPERATIONTYPE",PropertyConstvalue.DNSUBNEAUDITS_OPERATIONTYPE_NEW);
  public static final String MODIFY = SysDictionaryFactory.getSysDictionaryValueCN("DNSUBNEAUDITS","OPERATIONTYPE",PropertyConstvalue.DNSUBNEAUDITS_OPERATIONTYPE_MODIFY);
  public static final String DELETE = SysDictionaryFactory.getSysDictionaryValueCN("DNSUBNEAUDITS","OPERATIONTYPE",PropertyConstvalue.DNSUBNEAUDITS_OPERATIONTYPE_DELETE);
  public String getOperationType(byte operType)
  {
      switch(operType)
      {
          case PropertyConstvalue.PERSIST_TYPE_ADD:
              return NEW;
          case PropertyConstvalue.PERSIST_TYPE_UPDATE:
              return MODIFY;
          case PropertyConstvalue.PERSIST_TYPE_DELETE:
              return DELETE;
      }
      return "未知";
  }
}
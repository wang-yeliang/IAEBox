package gxlu.ietools.api.client;

import gxlu.afx.system.common.CommonClientEnvironment;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.ietools.api.service.IEToolsIFC;
import gxlu.ietools.basic.system.util.ServiceName;
import gxlu.ietools.property.mapping.Property;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

public class BIEToolsClient {
	 private long sessionKey;
	 public BIEToolsClient()
    {
        sessionKey = CommonClientEnvironment.getSecurityKey();
    }
	/**
	 * 导入新增方法
	 * @param sessionKey
	 * @param bObj
	 * @param className
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public Vector import_create(BObjectInterface bObj,Property property) throws SDHException{
		 try {
			return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).import_create(sessionKey, bObj,property);
		} catch (RemoteException e) {
            SDHException se = new SDHException();
            //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
			throw se;
			//e.printStackTrace();
		}
	}
	public Vector import_create(BObjectInterface bObj) throws SDHException{
		 try {
			return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).import_create(sessionKey, bObj);
		} catch (RemoteException e) {
           SDHException se = new SDHException();
           //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
			throw se;
			//e.printStackTrace();
		}
	}

  public Vector import_create(BObjectInterface bObj, Map objMap, Map propertyMap) throws SDHException {
		 try {
			return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).import_create(this.sessionKey, bObj, objMap, propertyMap);
		} catch (RemoteException e) {
           SDHException se = new SDHException();
           //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
			throw se;
			//e.printStackTrace();
		}
	}
	public Vector addObject(BObjectInterface bObj) throws SDHException{
		 try {
			return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).addObject(sessionKey, bObj);
		} catch (RemoteException e) {
           SDHException se = new SDHException();
           //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
			throw se;
			//e.printStackTrace();
		}
	}
	/**
	 * 导入更新方法
	 * @param sessionKey
	 * @param bObj
	 * @param className
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public Vector import_update(BObjectInterface bObj,Property property) throws SDHException{
		 try {
				return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).import_update(sessionKey,bObj,property);
			} catch (RemoteException e) {
	            SDHException se = new SDHException();
	            //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
				throw se;
				//e.printStackTrace();
			}
	}
	public Vector import_update(BObjectInterface bObj) throws SDHException{
		 try {
				return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).import_update(sessionKey,bObj);
			} catch (RemoteException e) {
	            SDHException se = new SDHException();
	            //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
				throw se;
				//e.printStackTrace();
			}
	}

  public Vector import_update(BObjectInterface bObj, Map objMap, Map propertyMap) throws SDHException {
		 try {
				return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).import_update(this.sessionKey, bObj, objMap, propertyMap);
			} catch (RemoteException e) {
	            SDHException se = new SDHException();
	            //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
				throw se;
				//e.printStackTrace();
			}
	}
	public Vector updateObject(BObjectInterface bObj) throws SDHException{
		 try {
				return ((IEToolsIFC)CommonClientEnvironment.getServiceIFC(ServiceName.IETOOLS_SERVICE_JNDI)).updateObject(sessionKey,bObj);
			} catch (RemoteException e) {
	            SDHException se = new SDHException();
	            //se.addException( ExceptionCode.COMMON_EXCEPTION_TYPE,CommonExcCode.COMMON_EXCEPTION_REMOTEEXCEPTION );
				throw se;
				//e.printStackTrace();
			}
	}
}

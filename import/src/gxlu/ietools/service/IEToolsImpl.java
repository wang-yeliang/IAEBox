package gxlu.ietools.service;

import gxlu.afx.system.common.CommonContext;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.common.service.DefaultServiceImpl;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.afx.system.event.common.EventManager;
import gxlu.ietools.api.service.IEToolsIFC;
import gxlu.ietools.basic.system.util.ServiceName;
import gxlu.ietools.property.mapping.Property;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

public class IEToolsImpl extends DefaultServiceImpl implements IEToolsIFC {

	public IEToolsImpl() throws RemoteException {
		super();
	}

	private CommonContext constructContext(long _sessionKey) {
		CommonContext mContext = new CommonContext();
		mContext.setSessionKey(_sessionKey);
		mContext.setRuntimeLog(logger);
		mContext.setEventManager(new EventManager(eventIFC));
		return mContext;
	}

	private CommonContext constructContextForOperation(long sessionKey) {
		CommonContext mContext = new CommonContext();
		mContext.setSessionKey(sessionKey);
		mContext.setEventManager(new EventManager(eventIFC));
		mContext.setRuntimeLog(logger);
		return mContext;
	}
	/**
	 * 导入新增方法
	 * @param sessionKey
	 * @param bObj
	 * @param className
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public Vector import_create(long sessionKey, BObjectInterface bObj,Property property) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.import_create(bObj,property);
			context.end();			
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
	public Vector import_create(long sessionKey, BObjectInterface bObj,Map objMap, Map propertyMap) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.import_create(bObj, objMap, propertyMap);
			context.end();			
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
	public Vector import_create(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.import_create(bObj);
			context.end();			
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
	public Vector addObject(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.addObject(bObj);
			context.end();			
			return context.getBObjects();
		} finally {
			context.release();
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
	public Vector import_update(long sessionKey, BObjectInterface bObj,Property property) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.import_update(bObj,property);
			context.end();
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
	public Vector import_update(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.import_update(bObj);
			context.end();
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
	public Vector import_update(long sessionKey, BObjectInterface bObj,Map objMap,Map propertyMap) throws RemoteException, SDHException {
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.import_update(bObj,objMap, propertyMap);
			context.end();
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
	/**
	 * 修改相应的B类
	 */
	public Vector updateObject(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException{
		CommonContext context = this.constructContextForOperation(sessionKey);
		context.setRuntimeLog(logger);
		BIEToolsServer server = new BIEToolsServer();
		try {
			server.setCommonContext(context);
			server.updateObject(bObj);
			context.end();
			return context.getBObjects();
		} finally {
			context.release();
		}
	}
    public String getJndiNamePrefix() {
        return ServiceName.IETOOLS_SERVICE_JNDI;
    }
    
}

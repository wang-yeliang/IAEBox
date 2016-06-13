package gxlu.ietools.api.service;

import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.ietools.property.mapping.Property;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

public interface IEToolsIFC extends Remote{
	/**
	 * 导入新增方法
	 * @param sessionKey
	 * @param bObj
	 * @param className
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public Vector import_create(long sessionKey, BObjectInterface bObj,Property property) throws RemoteException, SDHException;
	
	public Vector import_create(long sessionKey, BObjectInterface bObj,Map paramMap1, Map paramMap2) throws RemoteException, SDHException;
	public Vector import_create(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException;
	public Vector addObject(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException;
	/**
	 * 导入更新方法
	 * @param sessionKey
	 * @param bObj
	 * @param className
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public Vector import_update(long sessionKey, BObjectInterface bObj,Property property) throws RemoteException, SDHException;
	public Vector import_update(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException;
	public Vector import_update(long sessionKey, BObjectInterface bObj,Map paramMap1, Map paramMap2) throws RemoteException, SDHException;
	/**
	 * 修改
	 * @param sessionKey
	 * @param bObj
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public Vector updateObject(long sessionKey, BObjectInterface bObj) throws RemoteException, SDHException;
}

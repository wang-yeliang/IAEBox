package gxlu.ietools.api.client;

import gis.client.main.ClientEnvironment;
import gis.client.mapui.HandleEvent;
import gis.common.dataobject.YObjectInterface;
import gis.common.exception.GisException;
import gxlu.afx.system.common.sysexception.SDHException;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class YIEToolsClient {
	/**
	 * 导入新增方法
	 * @param sessionKey
	 * @param bObj
	 * @param className
	 * @throws RemoteException
	 * @throws SDHException
	 */
	public YObjectInterface import_create(YObjectInterface yObject) throws SDHException{
		YObjectInterface objectInterface=null;
		try {
			objectInterface = ClientEnvironment.getIEToolsGisIFC().import_create(ClientEnvironment.getUserKey(), yObject);
			HandleEvent.createObject(objectInterface);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (GisException e) {
			e.printStackTrace();
		}
		return objectInterface;
	}
}

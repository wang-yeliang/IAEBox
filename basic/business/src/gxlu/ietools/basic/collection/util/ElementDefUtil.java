package gxlu.ietools.basic.collection.util;

import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.afx.system.query.client.BQueryClient;

import java.util.Hashtable;
import java.util.Vector;

public class ElementDefUtil {
	
	/**
	 * 热点管理生成序号
	 * @return
	 */
	public static int dn_BWlanHotspot_getHotspotSerialNo(){
		Vector result = new Vector();
		int oldSerialNo = 0;
		String sql = " SELECT MAX(W.HOTSPOTSERIALNO) MAXNO FROM WLANHOTSPOT W";
		try {
			result = BQueryClient.queryValueBySQL(sql);
		} catch (SDHException e) {
			e.printStackTrace();
		}
		if ((result != null) && (result.size() > 0)) {
			Hashtable ht = (Hashtable) result.elementAt(0);
			if (ht.get("MAXNO") != null)
				oldSerialNo = Integer.parseInt(ht.get("MAXNO").toString());
		}
		return oldSerialNo+1;
	}
	
	public static int dn_BWlanHotspot_getHotspotSerialNo(String hotspotIDStr){
		Vector result = new Vector();
		int oldSerialNo = 0;
		String sql = " SELECT MAX(W.HOTSPOTSERIALNO) MAXNO FROM WLANHOTSPOT W where W.HotspotID like "+"'"+hotspotIDStr+"%' ";
		try {
			result = BQueryClient.queryValueBySQL(sql);
		} catch (SDHException e) {
			e.printStackTrace();
		}
		if ((result != null) && (result.size() > 0)) {
			Hashtable ht = (Hashtable) result.elementAt(0);
			if (ht.get("MAXNO") != null)
				oldSerialNo = Integer.parseInt(ht.get("MAXNO").toString());
		}
		return oldSerialNo+1;
	}
}

/**********************************************************************
 *$RCSfile: XmlCfgMgtMenuBroker.java,v $  $Revision: 1.3 $  $Date: 2010/04/20 02:08:06 $
 *********************************************************************/ 
package gxlu.ietools.client.template;

import gxlu.afx.system.clientmain.MenuBrokerAdapter;
import gxlu.afx.system.sysdata.client.UISysDictionaryMgtTree;

import gxlu.ossc.datanetwork.system.security.BasicPermissionList;
import gxlu.ossc.datanetwork.common.DefaultInfo;
/**
 * <li>Title: XmlCfgMgtMenuBroker.java</li>
 * <li>Description: ¼ò½é</li>
 * <li>Project: ResouceWorks</li>
 * <li>Copyright: Copyright (c) 2010</li>
 * @Company: GXLU. All Rights Reserved.
 * @author zhangwei Of VASG.Dept
 * @version 1.0
 */
public class XmlCfgMgtMenuBroker extends MenuBrokerAdapter
{
	public void doAction()
    {
		UIXmlCfgMgtTree panel = new UIXmlCfgMgtTree();
        panel.showWindow();
    }

    public boolean checkPermission(String permissionName)
    {
        boolean bResult = ac.checkPermission(BasicPermissionList.SysDictionary_Add, null)||
                          ac.checkPermission(BasicPermissionList.SysDictionary_Delete, null)||
                          ac.checkPermission(BasicPermissionList.SysDictionary_Property, null)||
                          ac.checkPermission(BasicPermissionList.SysDictionary_Update, null);

        return true;
    }
}

/**********************************************************************
 *$RCSfile: XmlCfgMgtMenuBroker.java,v $  $Revision: 1.3 $  $Date: 2010/04/20 02:08:06 $
 *
 *$Log: XmlCfgMgtMenuBroker.java,v $
 *Revision 1.3  2010/04/20 02:08:06  wudawei
 *20100420
 *
 *Revision 1.1  2010/01/19 01:16:46  zhangw
 **** empty log message ***
 *
 *
 *********************************************************************/

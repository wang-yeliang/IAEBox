package gxlu.ietools.client.ie;

import gxlu.afx.system.clientmain.MenuBrokerAdapter;
import gxlu.ossc.datanetwork.system.security.BasicPermissionList;

public class IEMenuBroker extends MenuBrokerAdapter {
	
	public void doAction()
    {
		IeToolsUI panel = new IeToolsUI();
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

//    public void doAction() {
//        JGxluChildWindow.setDesktop(sdhFrame.getDesktopPane());
//
//        String title = "µ¼³ö²âÊÔ";
//        JGxluChildWindow cw = JGxluChildWindow.getOriginalChildWindowByFunction(title);
//        if(cw == null || cw.isClone()) {
//            CommonClientEnvironment.getTimeLog().beginTiming("mainmenu/service/dnporttype_mgmt");
//            SDHCursorControl.openWaitCursor();
//            Example.executeExport();
//            SDHCursorControl.closeWaitCursor();
//            CommonClientEnvironment.getTimeLog().endTiming("mainmenu/service/dnporttype_mgmt");
//        } else {
//            JGxluChildWindow.setActiveChildWindow(title);
//        }
//    }
}

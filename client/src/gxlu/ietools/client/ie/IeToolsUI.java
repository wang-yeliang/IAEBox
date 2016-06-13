/**********************************************************************
 *$RCSfile: IeToolsUI.java,v $ $Revision: 1.5 $ $Date: 2010/06/13 01:53:39 $
 *********************************************************************/
package gxlu.ietools.client.ie;

import gis.client.main.ClientEnvironment;
import gis.common.dataobject.YObjectInterface;
import gis.server.main.ServerEnvironment;
import gxlu.afx.publics.swingx.thread.TimeConsumeJob;
import gxlu.afx.publics.swingx.window.JGxluChildPanel;
import gxlu.afx.publics.swingx.window.JGxluChildWindow;
import gxlu.afx.publics.swingx.window.JGxluMessageBox;
import gxlu.afx.publics.swingx.window.JGxluSimpleDialog;
import gxlu.afx.publics.swingx.window.JGxluWaitDialog;
import gxlu.afx.publics.swingx.window.JGxluWindow;
import gxlu.afx.publics.swingx.window.JGxluWindowInterface;
import gxlu.afx.publics.util.Images;
import gxlu.afx.publics.util.TextLibrary;
import gxlu.afx.system.clientmain.TnmsClient;
import gxlu.afx.system.common.CommonClientEnvironment;
import gxlu.afx.system.common.SDHCursorControl;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.afx.system.query.client.UIBaseQueryDialog;
import gxlu.ietools.api.client.BIEToolsClient;
import gxlu.ietools.api.client.YIEToolsClient;
import gxlu.ietools.basic.collection.ArrayLoader;
import gxlu.ietools.basic.collection.util.ImportUtil;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.basic.system.container.ContainerBootStrap;
import gxlu.ietools.basic.system.container.ContainerException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.container.ContainerImpl;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ClassNoteNames;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.client.query.DlgQueryCommon;
import gxlu.ietools.client.query.gisquery.GisDlgQueryCommon;
import gxlu.ietools.client.query.gisquery.UIGisBaseQueryDialog;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.xml.DomHelper;
import gxlu.ietools.property.xml.DomParse;
import gxlu.ietools.property.xml.DomTemplateParse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * <li>Title: IeToolsUI.java</li> <li>Description: 简介</li> <li>Project: ResouceWorks</li>
 * <li>Copyright: Copyright (c) 2010</li>
 * 
 * @Company: GXLU. All Rights Reserved.
 * @author zhangwei Of VAS2.Dept
 * @version 1.0
 */
public class IeToolsUI extends JGxluChildPanel implements JGxluWindowInterface, ActionListener
{
	JPanel buttonPanel = new JPanel();

	JPanel mainPanel = new JPanel();

	JPanel SelectPanel = new JPanel();

	JPanel treePanel = new JPanel();

	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("导入导出对象列表");

	protected JTree tree = new JTree(root);

	JButton btnExport = new JButton();

	JButton btnImport = new JButton();

	/**
	 * 构造函数
	 * 
	 * @param appName
	 */
	public IeToolsUI()
	{
		try
		{
			uiInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void uiInit()
	{
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		mainPanel.add(treePanel, BorderLayout.CENTER);

		// buttonPanel
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.add(btnExport, new GridBagConstraints(0, 0, 1, 10, 0.25, 0.25, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonPanel.add(btnImport, new GridBagConstraints(1, 0, 1, 10, 0.25, 0.25, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// treePanel
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setPreferredSize(new Dimension(600, 400));
		treePanel.setLayout(new BorderLayout());
		treePanel.add(treeView, BorderLayout.CENTER);
		treePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "导入导出目标选择",
			TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 13)), BorderFactory.createEmptyBorder(5, 0, 5, 0)));

		btnExport.setPreferredSize(new Dimension(18, 18));
		btnExport.setText("导出查询");
		btnImport.setPreferredSize(new Dimension(18, 18));
		btnImport.setText("导入");
		btnExport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onExport();
			}
		});
		btnImport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onImport();
			}
		});
		loadNodes(root);
		expandAll(tree, true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * 获取所有的node数据并add进treeXml
	 * 
	 * @param node
	 */
	private void loadNodes(DefaultMutableTreeNode node)
	{
		List domList = new ArrayList();
		DomParse domTemplateParse = new DomTemplateParse();
		DomHelper domhelper = new DomHelper(domTemplateParse);
		domList = domhelper.readDomParseAllList();
		if (domList.size() == 0)
			return;
		DefaultMutableTreeNode nodeModule = null;

		for (int i = 0; i < domList.size(); i++)
		{
			Property proModule = (Property) (((List) domList.get(i)).get(1));
			PropertyValue pvSub = null;
			PropertyObject poSub = null;
			nodeModule = new DefaultMutableTreeNode(proModule);
			node.add(nodeModule);
		}
	}

	public void expandAll(JTree tree, boolean expand)
	{
		if (tree == null)
		{
			return;
		}
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), expand);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0)
		{
			for (Enumeration e = node.children(); e.hasMoreElements();)
			{
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}
		if (expand)
		{
			tree.expandPath(parent);
		}
		else
		{
			if (!node.isRoot())
			{
				tree.collapsePath(parent);
			}
		}
	}

	public static TnmsClient sdhFrame = new TnmsClient();

	private DefaultMutableTreeNode node = null;

	public void onImport()
	{
		node = null;
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null || node.getParent() == null)
		{
			JGxluMessageBox.show(this, "请先选择导入目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		new JGxluWaitDialog(sdhFrame, TextLibrary.getText("caption_WaitDialog"),
				TextLibrary.getText("正在导入设备信息，请稍候..."), new TimeConsumeJob()
				{
					public Object doInConstruct()
					{
						try
						{
							SDHCursorControl.openWaitCursor();
							CommonClientEnvironment.getTimeLog().beginTiming("toolmenu/Import");
							ContainerBootStrap bootStrap = new ContainerBootStrap();
							bootStrap.startup();
							String strBclass = ((Property) node.getUserObject()).getBclass();
							if(strBclass.charAt(strBclass.lastIndexOf(".")+1)!='Y'){
								strBclass = strBclass.substring(strBclass.lastIndexOf(".") + 2, strBclass.length());
							}else{
								strBclass = strBclass.substring(strBclass.lastIndexOf(".") + 1, strBclass.length());
							}							
							DomParse domTemplateParse = new DomTemplateParse();
							List templateList = domTemplateParse.getTemplateFileList();
							if (templateList.size() < 0)
							{
								SDHCursorControl.closeWaitCursor();
								CommonClientEnvironment.getTimeLog().endTiming("toolmenu/Import");
								return null;
							}

							List bObjectList = ImportUtil.importExcel();
							if (bObjectList == null)
							{
								SDHCursorControl.closeWaitCursor();
								CommonClientEnvironment.getTimeLog().endTiming("toolmenu/Import");
								return null;
							}
							String path = templateList.get(0).toString();
							String templatePath = path.substring(0, path.lastIndexOf("/") + 1) + strBclass + ".xml";
							Class BClass = (Class) ((ContainerImpl) ContainerFactory.getContainer())
									.lookupBObject(templatePath);
							Context context = ExectionUtil.getContext();
							ArrayLoader arrayLoader = (ArrayLoader) context
									.lookupSessionBean(ClassNoteNames.ARRAYLOADER_BEAN);
							ResultController resultController = arrayLoader.getBObjectList(BClass, bObjectList, 1);

							Object[] operationMessage = resultController.getOperationMessage();
							if (((Boolean) operationMessage[0]).booleanValue() == false)
							{
								JGxluMessageBox.show(CommonClientEnvironment.getMainFrame(),
									((String) operationMessage[1]), "错误", JOptionPane.ERROR_MESSAGE);
							}
							BIEToolsClient bIEToolsClient = new BIEToolsClient();
							YIEToolsClient yIEToolsClient=new YIEToolsClient();
							Collection cols = resultController.getResult();
							Iterator it = cols.iterator();
							int count = 0;
							while (it.hasNext())
							{
								Object obj=it.next();
								if(obj instanceof BObjectInterface){
									BObjectInterface bObj = (BObjectInterface)obj;
									Vector result = null;
									Map objMap = (resultController.getObjectListMap().get(bObj) != null) ? (Map)resultController.getObjectListMap().get(bObj) : null;
									if (bObj.getId() > 0)
									{
										result = bIEToolsClient.import_update(bObj,objMap, resultController.getPropertyMap());
									}
									else
									{
										result = bIEToolsClient.import_create(bObj,objMap, resultController.getPropertyMap());
									}
									if (result != null && result.size() > 0)
									{
										count++;
									}
								}else if(obj instanceof YObjectInterface){
									YObjectInterface yObject = (YObjectInterface)obj;
									YObjectInterface object=yIEToolsClient.import_create(yObject);
									if(object!=null){
										count++;
									}
								}
							}
							// 写入导入出错信息
							ImportUtil.writeImportResult(count, resultController.getTypeConvertError());
							CommonClientEnvironment.getTimeLog().endTiming("toolmenu/Import");

						}
						catch (ContainerException e)
						{
							e.printStackTrace();
						}
						catch (SDHException e)
						{
							e.printStackTrace();
						}
						return null;
					}

					public void doInFinished()
					{
						return;
					}
				});
	}

	// public void onImport()
	// {
	// try
	// {
	// DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	// tree.getLastSelectedPathComponent();
	// if (node == null)
	// {
	// JGxluMessageBox.show(this, "请先选择导入目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
	// return;
	// }
	// ContainerBootStrap bootStrap = new ContainerBootStrap();
	// bootStrap.startup();
	//
	// String templateSelected = node.toString();
	// DomParse domTemplateParse = new DomTemplateParse();
	// List templateList = domTemplateParse.getTemplateFileList();
	// if (templateList.size() < 0)
	// return;
	// List bObjectList = ImportUtil.importExcel();
	// if (bObjectList == null)
	// {
	// return;
	// }
	// String path = templateList.get(0).toString();
	// String templatePath = path.substring(0, path.lastIndexOf("/") + 1) +
	// templateSelected + ".xml";
	// Class BClass = (Class) ((ContainerImpl)
	// ContainerFactory.getContainer()).lookupBObject(templatePath);
	// Context context = ExectionUtil.getContext();
	// ArrayLoader arrayLoader = (ArrayLoader)
	// context.lookupSessionBean(ClassNoteNames.ARRAYLOADER_BEAN);
	// ResultController resultController = arrayLoader.getBObjectList(BClass, bObjectList,
	// 1);
	//
	// Object[] operationMessage = resultController.getOperationMessage();
	// if (((Boolean) operationMessage[0]).booleanValue() == false)
	// {
	// JGxluMessageBox.show(CommonClientEnvironment.getMainFrame(), ((String)
	// operationMessage[1]), "错误",
	// JOptionPane.ERROR_MESSAGE);
	// }
	//
	// BIEToolsClient b = new BIEToolsClient();
	// Collection cols = resultController.getResult();
	// Iterator it = cols.iterator();
	// int count=0;
	// while (it.hasNext())
	// {
	// BObjectInterface bObj = (BObjectInterface) it.next();
	// boolean flag=false;
	// if (bObj.getId() > 0)
	// {
	// flag=b.import_update(bObj, resultController.getProperty());
	// }
	// else
	// {
	// flag=b.import_create(bObj, resultController.getProperty());
	// }
	// if(flag){
	// count++;
	// }
	// }
	// // 写入导入出错信息
	// ImportUtil.writeImportResult(count,resultController.getTypeConvertError());
	// }
	// catch (ContainerException e)
	// {
	// e.printStackTrace();
	// }
	// catch (SDHException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public void onExport()
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null || node.getParent() == null)
		{
			JGxluMessageBox.show(this, "请先选择导出目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		String strBclass = ((Property) node.getUserObject()).getBclass();
		if(strBclass.charAt(strBclass.lastIndexOf(".")+1)!='Y'){
			strBclass = strBclass.substring(strBclass.lastIndexOf(".") + 2, strBclass.length());
		}else{
			strBclass = strBclass.substring(strBclass.lastIndexOf(".") + 1, strBclass.length());
		}
		DomParse domTemplateParse = new DomTemplateParse();
		List templateList = domTemplateParse.getTemplateFileList();
		if (templateList.size() < 0)
			return;
		String path = templateList.get(0).toString();
		String templatePath = path.substring(0, path.lastIndexOf("/") + 1) + strBclass + ".xml";
		
		UIBaseQueryDialog panel = null;
		try {
			if(strBclass.charAt(0)=='Y'){
				ClientEnvironment.isSelectDomain_IETools();
				if(ServerEnvironment.getDomain()!=null&&!ServerEnvironment.getDomain().equals("")){
					panel = new GisDlgQueryCommon(CommonClientEnvironment.getMainFrame(),templatePath, node.toString());
				}
			}else{
				panel = new DlgQueryCommon(CommonClientEnvironment.getMainFrame(), templatePath, node.toString());
			}
			if(panel!=null){
				panel.show();
			}
		} catch (ContainerException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean perform(int nResourceID)
	{
		switch (nResourceID) {
		case JGxluSimpleDialog.SB_EXIT:
			return true;
		case JGxluSimpleDialog.SB_CANCEL:
			return true;
		}
		return true;
	}

	/**
	 * 显示窗口
	 */
	public void showWindow()
	{
		String functionName = "IeToolsUI";

		JGxluChildWindow cw = JGxluChildWindow.getOriginalChildWindowByFunction(functionName);

		if (cw == null)
		{
			JGxluWindow win = new JGxluWindow(this, this, JGxluWindow.SB_EXIT);
			String caption = "导入导出";
			cw = JGxluChildWindow.createChildWindow(caption, win, functionName, new Dimension(780, 620));

			cw.setFrameIcon(Images.getImage("images/user/user.gif"));

			// 设置帮助
			// JButton btnHelp = win.getButtonInstance(JGxluWindow.SB_HELP);
			// Tools.setHelp(this, btnHelp, this.helpId);
		}
		else
		{
			JGxluChildWindow.setActiveChildWindow(functionName);
		}

		cw.show();
	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}

}

/**********************************************************************
 *$RCSfile: IeToolsUI.java,v $ $Revision: 1.5 $ $Date: 2010/06/13 01:53:39 $
 * 
 *$Log: IeToolsUI.java,v $
 *Revision 1.5  2010/06/13 01:53:39  zhangj
 *扩展管线导入导出
 *
 *Revision 1.4  2010/05/06 07:09:48  zhangj
 **** empty log message ***
 *
 *Revision 1.3  2010/04/20 02:08:08  wudawei
 *20100420
 *
 * 
 *********************************************************************/

package gxlu.ietools.client.query;

import gxlu.afx.publics.swingx.table.JGxluTableModel;
import gxlu.afx.publics.swingx.window.JGxluMessageBox;
import gxlu.afx.system.common.CommonClientEnvironment;
import gxlu.afx.system.common.FieldDictionary;
import gxlu.afx.system.common.SDHCursorControl;
import gxlu.afx.system.common.SysDictionaryFactory;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.afx.system.query.client.BQueryClient;
import gxlu.afx.system.query.client.ChildrenProvider;
import gxlu.afx.system.query.client.DialogShowConstants;
import gxlu.afx.system.query.client.OperatorMethodInterface;
import gxlu.afx.system.query.client.QueryControl;
import gxlu.afx.system.query.client.QueryControlFactory;
import gxlu.afx.system.query.client.QueryMethodInterface;
import gxlu.afx.system.query.client.QueryResult;
import gxlu.afx.system.query.client.UIBaseQueryDialog;
import gxlu.afx.system.query.client.UniQueryChildInterface;
import gxlu.afx.system.query.common.QueryExpr;
import gxlu.afx.system.query.common.QueryExprBuilder;
import gxlu.afx.system.query.common.QueryParam;
import gxlu.ietools.basic.collection.ArrayLoader;
import gxlu.ietools.basic.collection.util.ExportUtil;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.basic.system.container.ContainerBootStrap;
import gxlu.ietools.basic.system.container.ContainerException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.container.ContainerImpl;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ClassNoteNames;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.property.util.FormatUtil;
import gxlu.ietools.property.util.ReflectHelper;
import gxlu.netmaster.client.mobilenetwork.system.help.HelpConstFactory;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPopupMenu;

/**
 * <li>Title: DlgQueryMNNE.java</li> <li>Description: 简介</li> <li>Project: mn_main</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * 
 * @Company: GXLU. All Rights Reserved.
 * @author wudawei Of VAS2.Dept
 * @version 1.0
 */
public class DlgQueryCommon extends UIBaseQueryDialog implements ChildrenProvider, QueryMethodInterface,
		OperatorMethodInterface
{

	private HashMap hmQueryItemSet;

	private String strTemplateName;

	private String strTemplatePath;

	private String title;

	private HashMap hmChildItemSet;

	private Class BClass;

	private final static byte OPR_EXPORTPART = 7;

	private final static byte OPR_EXPORTALL = 8;

	private QueryParam qParam;

	public DlgQueryCommon(Frame owner, String path, String title) throws ContainerException, ClassNotFoundException
	{
		ContainerBootStrap bootStrap = new ContainerBootStrap();
		bootStrap.startup();
		this.strTemplatePath = path;
		this.strTemplateName = path.substring(path.lastIndexOf("/") + 1, path.length());
		this.title = title;
		BClass = (Class) ((ContainerImpl) ContainerFactory.getContainer()).lookupBObject(strTemplatePath);
		QueryConditionParseIFC queryTemplateParse = new QueryConditionParseImpl();
		this.hmQueryItemSet = queryTemplateParse.readDomParseList();
		aceInit(owner, DialogShowConstants.OPR_SELECT, false, false, null, null);
	}

	protected String getAssembleString()
	{
		String assembleString = BClass.getSimpleName().substring(1);
		QueryItems queryItems = (QueryItems) hmQueryItemSet.get(strTemplateName);
		HashMap hmQueryItems = queryItems.getQueryItemSet();
		Iterator iter = hmQueryItems.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			QueryItem queryItem = (QueryItem) val;
			if (queryItem.getOtype().equalsIgnoreCase("search") == false)
			{
				if (key.toString().endsWith("_PO"))
				{
					//如果name和jncolumn一样则不能当对象处理
					if (queryItem.getName() != null&&queryItem.getName().indexOf("[")<0)
						assembleString = assembleString + "[" + queryItem.getName().substring(0, 1).toUpperCase()
							+ queryItem.getName().substring(1) + "]";
				}
			}
		}
		if (assembleString.contains("[") == false)
			assembleString = null;
		return assembleString;
	}

	protected String getDialogTitle(byte type)
	{
		return this.title;
	}

	protected Class getQueryClass()
	{
		return BClass;
	}

	@Override
	protected void initParameters()
	{
		int lineFlag = 1, childFlag = 0;
		this.setHelp(HelpConstFactory.COMMON_QUERY);
		QueryControl c = null;
		QueryItems queryItems = (QueryItems) hmQueryItemSet.get(strTemplateName);
		HashMap hmQueryItems = queryItems.getQueryItemSet();
		Iterator iter = hmQueryItems.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			QueryItem queryItem = (QueryItem) val;
			if (queryItem.getOtype().equalsIgnoreCase("none") == false)
			{
				if (queryItem.getType().equalsIgnoreCase("TextField") == true)
				{
					c = QueryControlFactory.createStringQueryControl(queryItem.getTitle(), new String[] { queryItem
							.getName() });
					// c.setIDString("ID_" + queryItem.getName());
				}
				else if (queryItem.getType().equalsIgnoreCase("DataDict") == true)
				{
					Vector vecDict = SysDictionaryFactory.getSysDictionaryValueCNVector(queryItem.getDclass(),
						queryItem.getDattr());
					if (vecDict.size() > 0)
					{
						c = QueryControlFactory.createSysDictionaryQueryControl(queryItem.getTitle(),
							new String[] { queryItem.getName() }, queryItem.getDclass(), queryItem.getDattr());
						// c.setIDString("ID_" + queryItem.getName());
					}
					else
					{
						vecDict = FieldDictionary.getFieldDescVector(queryItem.getDclass(), queryItem.getDattr());
						if (vecDict.size() > 0)
						{
							c = QueryControlFactory.createListQueryControl(queryItem.getTitle(),
								new String[] { queryItem.getName() }, queryItem.getDclass(), queryItem.getDattr(),
								UIBaseQueryDialog.MULTI_SELECT);
							// c.setIDString("ID_" + queryItem.getName());
						}
						else
						{
							c = QueryControlFactory.createSysDictionaryQueryControl(queryItem.getTitle(),
								new String[] { queryItem.getName() }, queryItem.getDclass(), queryItem.getDattr());
							// c.setIDString("ID_" + queryItem.getName());
						}
					}
				}
				else if (queryItem.getType().equalsIgnoreCase("ParentQuery") == true)
				{
					childFlag = childFlag + 1;
					byte childID = (byte) (childFlag - 1);
					c = QueryControlFactory.createParentQueryControl(queryItem.getTitle(), new String[] { queryItem
							.getJncolumn() }, this, childID);
					// c.setIDString("ID_" + queryItem.getName());
					hmChildItemSet = (hmChildItemSet == null ? new HashMap() : hmChildItemSet);
					hmChildItemSet.put(new Byte(childID), queryItem);
				}
				c.setIDString("ID_" + queryItem.getName());

				if (lineFlag == 4)
				{
					this.addToNextLine(c);
					lineFlag = 1;
				}
				else
				{
					this.addToThisLine(c);
					lineFlag = lineFlag + 1;
				}
			}
		}
		this.setOperatorDisplayType(BOTH_POPUPMENU_AND_BUTTONS);
		initButton();
	}

	/**
	 * 设置该界面显示的大小
	 */
	public Dimension getPreferredTableSize()
	{
		return new Dimension(800, 250);
	}

	public Vector getTableHeader()
	{
		Vector vHeader = new Vector();
		QueryItems queryItems = (QueryItems) hmQueryItemSet.get(strTemplateName);
		HashMap hmQueryItems = queryItems.getQueryItemSet();
		Iterator iter = hmQueryItems.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			QueryItem queryItem = (QueryItem) val;
			if (queryItem.getOtype().equalsIgnoreCase("search") == false)
			{
				if (queryItem.getType().equalsIgnoreCase("Datadict") == false)
				{
					vHeader.addElement(queryItem.getTitle());
				}
				else
				{
					vHeader.addElement(queryItem.getTitle());
				}
			}
		}

		return vHeader;
	}

	// ------------------------------------------------------------------------------
	public int[] getHeaderDataType()
	{
		Method[] methods = BClass.getMethods();
		QueryItems queryItems = (QueryItems) hmQueryItemSet.get(strTemplateName);
		HashMap hmQueryItems = queryItems.getQueryItemSet();
		Iterator iter = hmQueryItems.entrySet().iterator();
		int[] fieldType = new int[hmQueryItems.size()];
		int j = 0;
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			QueryItem queryItem = (QueryItem) val;
			if (queryItem.getOtype().equalsIgnoreCase("search") == false)
			{
				String methodName;
				if (queryItem.getType().equalsIgnoreCase("ParentQuery"))
					methodName = "get" + queryItem.getJncolumn().substring(0, 1).toUpperCase()
						+ queryItem.getJncolumn().substring(1);
				else
					methodName = "get" + queryItem.getName().substring(0, 1).toUpperCase()
						+ queryItem.getName().substring(1);
				for (int i = 0; i < methods.length; i++)
				{
					Method m = methods[i];
					if (m.getName().equalsIgnoreCase(methodName))
					{
						try
						{
							if (m != null)
							{
								String strFieldType = m.getReturnType().toString();
								if (strFieldType.equals("char") || strFieldType.equals("class java.lang.Character"))
									fieldType[j] = JGxluTableModel.nString;
								else if (strFieldType.equals("long") || strFieldType.equals("class java.lang.Long"))
									fieldType[j] = JGxluTableModel.nLong;
								else if (strFieldType.equals("int") || strFieldType.equals("class java.lang.Integer"))
									fieldType[j] = JGxluTableModel.nInteger;
								else if (strFieldType.equals("short") || strFieldType.equals("class java.lang.Short"))
									fieldType[j] = JGxluTableModel.nInteger;
								else if (strFieldType.equals("byte") || strFieldType.equals("class java.lang.Byte"))
									fieldType[j] = JGxluTableModel.nBoolean;
								else if (strFieldType.equals("double") || strFieldType.equals("class java.lang.Double"))
									fieldType[j] = JGxluTableModel.nDouble;
								else if (strFieldType.equals("float") || strFieldType.equals("class java.lang.Float"))
									fieldType[j] = JGxluTableModel.nFloat;
								else if (strFieldType.equals("class java.util.Date"))
									fieldType[j] = JGxluTableModel.nDate;
								else
									fieldType[j] = JGxluTableModel.nString;
							}
						}
						catch (IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						break;
					}
				}
				j = j + 1;
			}
		}
		return fieldType;
	}

	@SuppressWarnings("unchecked")
	public Vector getTableRow(BObjectInterface object)
	{
		Vector v = new Vector();
		QueryItems queryItems = (QueryItems) hmQueryItemSet.get(strTemplateName);
		HashMap hmQueryItems = queryItems.getQueryItemSet();
		Iterator iter = hmQueryItems.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			QueryItem queryItem = (QueryItem) val;
			if (queryItem.getOtype().equalsIgnoreCase("search") == false)
			{
				String methodName="";
				if(queryItem.getName().indexOf("[")>=0){
					methodName = "get" + ReflectHelper.getUpperCase(queryItem.getJncolumn());
				}else{
					methodName = "get" + ReflectHelper.getUpperCase(queryItem.getName());
				}
				Method[] methods = BClass.getMethods();
				for (int i = 0; i < methods.length; i++)
				{
					Method m = methods[i];
					if (m.getName().equalsIgnoreCase(methodName))
					{
						try
						{
							if (m != null)
							{
								if (queryItem.getType().equalsIgnoreCase("DataDict"))
								{
									Vector vecDict = SysDictionaryFactory.getSysDictionaryValueCNVector(queryItem
											.getDclass(), queryItem.getDattr());
									if (vecDict.size() > 0)
									{
										try
										{
											v.addElement(SysDictionaryFactory.getSysDictionaryValueCN(queryItem
													.getDclass(), queryItem.getDattr(), Byte.parseByte(m.invoke(object)
													.toString())));
										}
										catch (NumberFormatException e)
										{
											v.addElement("");
										}
									}
									else
									{
										vecDict = FieldDictionary.getFieldDescVector(queryItem.getDclass(), queryItem
												.getDattr());
										if (vecDict.size() > 0)
										{
											try
											{
												Object object1 = m.invoke(object);
												v.addElement(FieldDictionary.getFieldDesc(queryItem.getDclass(),
													queryItem.getDattr(), Byte.parseByte(object1.toString())));
											}
											catch (NumberFormatException e)
											{
												v.addElement("");
											}
										}
										else
										{
											try
											{
												v.addElement(SysDictionaryFactory.getSysDictionaryValueCN(queryItem
														.getDclass(), queryItem.getDattr(), Byte.parseByte(m.invoke(
													object).toString())));
											}
											catch (NumberFormatException e)
											{
												v.addElement("");
											}
										}
									}
									break;
								}
								else if (queryItem.getType().equalsIgnoreCase("TextField"))
								{
									try
									{
										String strFieldType = m.getReturnType().toString();
//										if (strFieldType.equals("char")
//											|| strFieldType.equals("class java.lang.Character"))
//											v.addElement(m.invoke(object));
//										else if (strFieldType.equals("long")|| strFieldType.equals("class java.lang.Long")){
//											if()
//											v.addElement(m.invoke(object));
//										}
//											
//										else if (strFieldType.equals("int")|| strFieldType.equals("class java.lang.Integer"))
//										{
//											if (m.invoke(object).toString().equalsIgnoreCase("-2147483648"))
//												v.addElement("");
//											else
//												v.addElement(m.invoke(object));
//										}
//										else if (strFieldType.equals("short")
//											|| strFieldType.equals("class java.lang.Short"))
//											v.addElement(m.invoke(object));
//										else if (strFieldType.equals("byte")
//											|| strFieldType.equals("class java.lang.Byte"))
//											v.addElement(m.invoke(object));
//										else if (strFieldType.equals("double")
//											|| strFieldType.equals("class java.lang.Double"))
//										{
//											if (m.invoke(object).toString().equalsIgnoreCase("-1.0"))
//												v.addElement("");
//											else
//												v.addElement(m.invoke(object));
//										}
//										else if (strFieldType.equals("float")
//											|| strFieldType.equals("class java.lang.Float"))
//											v.addElement(m.invoke(object));
//										else if (strFieldType.equals("class java.util.Date"))
//										{
//											java.util.Date current = new java.util.Date();
//											java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
//													"yyyy-MM-dd");
//											if (m.invoke(object) != null)
//												v.addElement(sdf.format(m.invoke(object)));
//											else
//												v.addElement("");
//										}
//										else
//											v.addElement(m.invoke(object));
										v.addElement(FormatUtil.getStringTypeSetDefault(strFieldType,m.invoke(object)));
									}
									catch (NumberFormatException e)
									{
										v.addElement("");
									}
									break;
								}
								else if (queryItem.getType().equalsIgnoreCase("ParentQuery"))
								{
									BObjectInterface bObject=null;
									if(queryItem.getName()!=null&&queryItem.getName().indexOf("[")>=0){
////										String joinColumn=ReflectHelper.getJoinColumn(queryItem.getName());
										Vector resultObj=null;
										try {
											Object value =  m.invoke(object);
											if(value!=null){
												resultObj = BQueryClient.getQueryResults(
														new QueryExprBuilder().get(ReflectHelper.getJoinColumn(queryItem.getName())).equal(value.toString()),
														ReflectHelper.classForName(queryItem.getBObject()),null);
											}
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
										if(resultObj!=null&&resultObj.size()>0){
											bObject=(BObjectInterface)resultObj.get(0);
										}else{
											bObject=null;
										}
									}else{
										bObject = (BObjectInterface) m.invoke(object);
									}
									if (bObject == null)
									{
										v.addElement(null);
										break;
									}
									Method[] pMethods = bObject.getClass().getMethods();
									String pMethodName = "get" + queryItem.getBObcolumn().substring(0, 1).toUpperCase()
										+ queryItem.getBObcolumn().substring(1);
									for (int j = 0; j < pMethods.length; j++)
									{
										Method pm = pMethods[j];
										if (pm.getName().equalsIgnoreCase(pMethodName))
										{
											if (pm != null)
											{
												try
												{
													v.addElement(pm.invoke(bObject));
												}
												catch (NumberFormatException e)
												{
													v.addElement("");
												}
												break;
											}
											else
												v.addElement("");
										}
									}
								}
							}
							else
								v.addElement("");
						}
						catch (IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						catch (InvocationTargetException e)
						{
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}
		return v;
	}

	public Vector execQuery(QueryParam param) throws SDHException
	{
		Vector vResult = null;// new
		QueryExpr[] expParts = { param.getExpression(), null, null };
		expParts[1] = new QueryExprBuilder().get("id").notNull();
		QueryExpr exprFinal = null;
		for (int i = 0; i < expParts.length; i++)
		{
			if (exprFinal == null)
				exprFinal = expParts[i];
			else
				exprFinal = exprFinal.and(expParts[i]);
		}

		param.setExpression(exprFinal);
		param.setBatchReadingAllowed(false);
		if (param.getIsPaged())
			vResult = (Vector) BQueryClient.queryPagedObject(param);
		else
			vResult = (Vector) BQueryClient.queryObject(param);
		qParam = (qParam == null ? new QueryParam() : qParam);
		qParam = param;
		return vResult;
	}

	/**
	 * (ChildProvider)
	 */
	public UniQueryChildInterface getQueryChild(byte childID)
	{
		Byte b = new Byte(childID);

		if (hmChildItemSet != null && hmChildItemSet.get(b) != null)
		{
			QueryItem queryItem = (QueryItem) hmChildItemSet.get(b);
			String strQueryClassName = queryItem.getQclass();
			if (strQueryClassName == null)
			{
				JGxluMessageBox.show(this, "请先配置查询类路径！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return null;
			}
			if (strQueryClassName.startsWith("gxlu.") == false)
			{
				JGxluMessageBox.show(this, "请正确配置查询类路径！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return null;
			}
			Class cls = null;
			Object obj = null;
			UIBaseQueryDialog dlg = null;
			try
			{
				cls = Class.forName(strQueryClassName);
				obj = cls.newInstance();
			}
			catch (Exception e)
			{
				JGxluMessageBox.show(this, "请正确配置查询类路径！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				e.printStackTrace();
				return null;
			}

			if (obj instanceof UIBaseQueryDialog)
			{
				dlg = (UIBaseQueryDialog) obj;
				// Gets the current dialog or frame
				Component parent = this.getOwner();
				if (parent instanceof Dialog)
				{
					dlg.aceInit((Dialog) parent, dlg.SELECT_CANCEL_HELP, true, true);
				}
				else if (parent instanceof Frame)
				{
					dlg.aceInit((Frame) parent, dlg.SELECT_CANCEL_HELP, true, true);
				}
				else
				{
					dlg.aceInit((Dialog) null, dlg.SELECT_CANCEL_HELP, true, true);
				}
				return (UniQueryChildInterface) dlg;
			}
			else
				return null;
		}
		else
			return null;
	}

	protected void initButton()
	{
		JPopupMenu popup = new JPopupMenu();
		popup.add(createOperatorItem(popup, "导出选择项", true, "导出选择项", true, ALL, OPR_EXPORTPART));
		popup.add(createOperatorItem(popup, "导出所有项", true, "导出所有项", true, ALL, OPR_EXPORTALL));
	}

	public void perform(byte commandID, QueryResult result)
	{
		switch (commandID) {
		case OPR_EXPORTPART:// 导出选择的站点信息
			SDHCursorControl.openWaitCursor();
			CommonClientEnvironment.getTimeLog().beginTiming("toolmenu/Import");
			// 导出处理
			Vector vec = result.getSelectedItems();
			onExport(vec);
			SDHCursorControl.closeWaitCursor();
			CommonClientEnvironment.getTimeLog().endTiming("toolmenu/Import");
			break;
		case OPR_EXPORTALL:// 导出全部站点信息
			SDHCursorControl.openWaitCursor();
			CommonClientEnvironment.getTimeLog().beginTiming("toolmenu/Import");
			// 导出处理
			Vector vecAll = null;
			try
			{
				if (qParam != null && result.getItems() != null)
					vecAll = (Vector) BQueryClient.queryObject(qParam);
			}
			catch (SDHException e)
			{
				// TODOe.printStackTrace();
			}
			onExport(vecAll);
			SDHCursorControl.closeWaitCursor();
			CommonClientEnvironment.getTimeLog().endTiming("toolmenu/Import");
			break;
		}
	}

	public void onExport(Vector vec)
	{
		if (vec == null)
		{
			JGxluMessageBox.show(this, "查询数据为空！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		List list = new ArrayList();
		for (int i = 0; i < vec.size(); i++)
		{
			list.add(vec.get(i));
		}
		ContainerBootStrap bootStrap = new ContainerBootStrap();
		bootStrap.startup();

		try
		{
			Context context = ExectionUtil.getContext();
			ArrayLoader arrayLoader = (ArrayLoader) context.lookupSessionBean(ClassNoteNames.ARRAYLOADER_BEAN);
			ResultController resultController = arrayLoader.getBObjectList(BClass, list, 2);
			ExportUtil.export(resultController);
		}
		catch (ContainerException e)
		{
			e.printStackTrace();
		}

	}
}

/**********************************************************************
 *$RCSfile: UIXmlCfgMgtTree.java,v $ $Revision: 1.3 $ $Date: 2010/04/20 02:08:06 $
 *********************************************************************/
package gxlu.ietools.client.template;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.tree.*;

import gxlu.afx.publics.swingx.text.JGxluStringTextField;
import gxlu.afx.publics.swingx.window.*;
import gxlu.afx.publics.util.Images;

import gxlu.afx.system.common.*;
import gxlu.ietools.basic.collection.ArrayLoader;
import gxlu.ietools.basic.collection.util.ExportUtil;
import gxlu.ietools.basic.collection.util.ImportUtil;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.basic.system.container.ContainerBootStrap;
import gxlu.ietools.basic.system.container.ContainerException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.container.ContainerImpl;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ClassNoteNames;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.xml.DomHelper;
import gxlu.ietools.property.xml.DomParse;
import gxlu.ietools.property.xml.DomTemplateParse;

/**
 * <li>Title: test.java</li> <li>Description: 简介</li> <li>Project: ResouceWorks</li> <li>
 * Copyright: Copyright (c) 2010</li>
 * 
 * @Company: GXLU. All Rights Reserved.
 * @author zhangwei Of VASG.Dept
 * @version 1.0
 */

public class UIXmlCfgMgtTree extends JGxluChildPanel implements JGxluWindowInterface, JGxluSimpleDialogInterface,
		TreeExpansionListener, MouseListener, ActionListener
{

	// GUI definition
	private DefaultMutableTreeNode rootAll = new DefaultMutableTreeNode("目标对象");

	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("目标对象");

	// B类树，显示所有B类及其列字段
	protected JTree treeAll = new JTree(rootAll);

	// XML树，显示选中B类及其字段
	protected JTree treeXml = new JTree(root);

	// 两棵树和btPanel的panel：treePanelAll+btPanel+treePanel
	JPanel navigationPanel = new JPanel();

	// 左边大panel：buttonPanel+dataviewPanel
	JPanel leftPanel = new JPanel();

	// treeAll的panel
	JPanel treePanelAll = new JPanel();

	// 选择字段工具的panel：btAdd+btDel
	JPanel btPanel = new JPanel();

	// treeXml的panel
	JPanel treePanel = new JPanel();

	// 操作按钮的panel，位于leftPanel的上部
	JPanel buttonPanel = new JPanel();

	// 节点属性的panel：topPanel+midPanel+bottomPanel
	JPanel dataviewPanel = new JPanel();

	// property类的节点属性
	JPanel topPanel = new JPanel();

	// propertyValue类的节点属性
	JPanel midPanel = new JPanel();

	// propertyObject类的节点属性
	JPanel bottomPanel = new JPanel();

	// 操作按钮
	JButton btAdd = new JButton();

	JButton btDel = new JButton();

	JButton btSave = new JButton();

	JButton btCancle = new JButton();

	JButton btOutputXml = new JButton();

	JButton btOutputXls = new JButton();

	// Property类的属性字段
	JLabel lbBclass = new JLabel();

	JLabel lbCname = new JLabel();

	JLabel lbIsInsert = new JLabel();

	JLabel lbIsUpdate = new JLabel();

	JLabel lbLineNum = new JLabel();

	JLabel lbLineMax = new JLabel();

	JGxluStringTextField tfBclass = new JGxluStringTextField();

	JGxluStringTextField tfCname = new JGxluStringTextField();

	JGxluStringTextField tfIsInsert = new JGxluStringTextField();

	JGxluStringTextField tfIsUpdate = new JGxluStringTextField();

	JGxluStringTextField tfLineNum = new JGxluStringTextField();

	JGxluStringTextField tfLineMax = new JGxluStringTextField();

	// propertyValue属性字段
	JLabel lbName = new JLabel();

	JLabel lbColumnseq = new JLabel();

	JLabel lbColumnTitle = new JLabel();

	JLabel lbIsDatadict = new JLabel();

	JLabel lbDatadictClass = new JLabel();

	JLabel lbDatadictAttr = new JLabel();

	JLabel lbIsQuery = new JLabel();

	// JLabel lbDataDict = new JLabel();

	JGxluStringTextField tfName = new JGxluStringTextField();

	JGxluStringTextField tfColumnseq = new JGxluStringTextField();

	JGxluStringTextField tfColumnTitle = new JGxluStringTextField();

	JGxluComboBox cbIsDatadict = new JGxluComboBox();

	JGxluStringTextField tfDatadictClass = new JGxluStringTextField();

	JGxluStringTextField tfDatadictAttr = new JGxluStringTextField();

	JGxluComboBox cbIsQuery = new JGxluComboBox();

	// PropertyObject属性字段
	JLabel lbO2OName = new JLabel();

	JLabel lbO2OSeq = new JLabel();

	JLabel lbO2OTitle = new JLabel();

	JLabel lbO2OJoinCol = new JLabel();

	JLabel lbO2OClass = new JLabel();

	JLabel lbO2OClassCol = new JLabel();

	JLabel lbO2OIsQuery = new JLabel();

	JLabel lbO2OQueryClass = new JLabel();

	JGxluStringTextField tfO2OName = new JGxluStringTextField();

	JGxluStringTextField tfO2OSeq = new JGxluStringTextField();

	JGxluStringTextField tfO2OTitle = new JGxluStringTextField();

	JGxluStringTextField tfO2OJoinCol = new JGxluStringTextField();

	JGxluStringTextField tfO2OClass = new JGxluStringTextField();

	JGxluStringTextField tfO2OClassCol = new JGxluStringTextField();

	JGxluComboBox cbO2OIsQuery = new JGxluComboBox();

	JGxluStringTextField tfO2OQueryClass = new JGxluStringTextField();

	PropertyValue tempPropertyValue;

	PropertyObject tempPropertyObject;

	DefaultMutableTreeNode tempNode;

	Hashtable htModule = new Hashtable();

	public List domList = new ArrayList();

	public List BclassList = new ArrayList();

	/**
	 * 构造函数
	 * 
	 * @param appName
	 */
	public UIXmlCfgMgtTree()
	{
		try
		{
			DomParse domTemplateParse = new DomTemplateParse();
			DomHelper domhelper = new DomHelper(domTemplateParse);
			domList = domhelper.readDomParseAllList();
			BclassList = domhelper.getAllPropertyAllList();
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		navigationPanel.setLayout(new BorderLayout());
		leftPanel.setLayout(new BorderLayout());

		treePanelAll.setLayout(new BorderLayout());
		btPanel.setLayout(new GridBagLayout());
		treePanel.setLayout(new BorderLayout());

		buttonPanel.setLayout(new GridBagLayout());
		dataviewPanel.setLayout(new GridBagLayout());
		topPanel.setLayout(new GridBagLayout());
		midPanel.setLayout(new GridBagLayout());
		bottomPanel.setLayout(new GridBagLayout());
		btPanel.setLayout(new GridBagLayout());

		// Renderer
		treeXml.setCellRenderer(new SysDataTreeCellRenderer());

		this.add(navigationPanel, BorderLayout.WEST);
		this.add(leftPanel, BorderLayout.CENTER);

		// navigationPanel
		navigationPanel.add(treePanelAll, BorderLayout.WEST);
		navigationPanel.add(btPanel, BorderLayout.CENTER);
		navigationPanel.add(treePanel, BorderLayout.EAST);

		// leftPanel
		leftPanel.add(buttonPanel, BorderLayout.NORTH);
		leftPanel.add(dataviewPanel, BorderLayout.CENTER);

		// treePanelAll
		JScrollPane treeViewAll = new JScrollPane(treeAll);
		treeViewAll.setPreferredSize(new Dimension(220, 324));
		treePanelAll.add(treeViewAll, BorderLayout.CENTER);
		treePanelAll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "待选字段列表",
			TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 13)), BorderFactory.createEmptyBorder(5, 0, 5, 0)));

		btPanel.add(btAdd, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(55, 0, 5, 0), 0, 0));
		btPanel.add(btDel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(55, 0, 5, 0), 0, 0));

		// treePanel
		JScrollPane treeView = new JScrollPane(treeXml);
		treeView.setPreferredSize(new Dimension(220, 324));
		treePanel.add(treeView, BorderLayout.CENTER);
		treePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "已选字段列表",
			TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 13)), BorderFactory.createEmptyBorder(5, 0, 5, 0)));

		// buttonPanel
		buttonPanel.add(btSave, new GridBagConstraints(0, 0, 1, 1, 0.25, 0.25, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonPanel.add(btCancle, new GridBagConstraints(1, 0, 1, 1, 0.25, 0.25, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonPanel.add(btOutputXml, new GridBagConstraints(2, 0, 1, 1, 0.25, 0.25, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		buttonPanel.add(btOutputXls, new GridBagConstraints(3, 0, 1, 1, 0.25, 0.25, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// dataviewPanel
		dataviewPanel.add(topPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 1, 1));
		dataviewPanel.add(midPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));
		dataviewPanel.add(bottomPanel, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));

		// topPanel
		topPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "B类",
			TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 13)), BorderFactory.createEmptyBorder(5, 0, 5, 0)));
		midPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "基本属性",
			TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 13)), BorderFactory.createEmptyBorder(5, 0, 5, 0)));
		bottomPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "扩展属性",
			TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 13)), BorderFactory.createEmptyBorder(5, 0, 5, 0)));

		btSave.setPreferredSize(new Dimension(18, 18));
		btSave.setText("保存");

		btCancle.setPreferredSize(new Dimension(18, 18));
		btCancle.setText("取消");

		btOutputXml.setPreferredSize(new Dimension(18, 18));
		btOutputXml.setText("生成配置模版");

		btOutputXls.setPreferredSize(new Dimension(18, 18));
		btOutputXls.setText("生成EXCEL模版");

		// btAdd.setText(">>");
		btAdd.setPreferredSize(new Dimension(24, 18));
		btAdd.setText("》");
		// btAdd.setPressedIcon(IeIconFactory.forwardIcon);
		// btDel.setText("<<");
		btDel.setPreferredSize(new Dimension(24, 18));
		// btDel.setPressedIcon(IeIconFactory.backIcon);
		btDel.setText("《");

		topPanel.add(lbBclass, new GridBagConstraints(0, 0, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(tfBclass, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(lbCname, new GridBagConstraints(2, 0, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(tfCname, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(lbIsInsert, new GridBagConstraints(0, 1, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(tfIsInsert, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(lbIsUpdate, new GridBagConstraints(2, 1, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(tfIsUpdate, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(lbLineNum, new GridBagConstraints(0, 2, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(tfLineNum, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(lbLineMax, new GridBagConstraints(2, 2, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		topPanel.add(tfLineMax, new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		lbBclass.setHorizontalAlignment(SwingConstants.RIGHT);
		lbBclass.setText("B类名称:");
		lbCname.setHorizontalAlignment(SwingConstants.RIGHT);
		lbCname.setText("显示名称:");
		lbIsInsert.setHorizontalAlignment(SwingConstants.RIGHT);
		lbIsInsert.setText("是否插入:");
		lbIsUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
		lbIsUpdate.setText("是否更新:");
		lbLineNum.setHorizontalAlignment(SwingConstants.RIGHT);
		lbLineNum.setText("线程阀值:");
		lbLineMax.setHorizontalAlignment(SwingConstants.RIGHT);
		lbLineMax.setText("最大行值:");

		midPanel.add(lbName, new GridBagConstraints(0, 0, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(tfName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(lbColumnTitle, new GridBagConstraints(2, 0, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(tfColumnTitle, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(lbIsDatadict, new GridBagConstraints(0, 1, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(cbIsDatadict, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(lbDatadictClass, new GridBagConstraints(2, 1, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(tfDatadictClass, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(lbDatadictAttr, new GridBagConstraints(0, 2, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(tfDatadictAttr, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(lbIsQuery, new GridBagConstraints(2, 2, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		midPanel.add(cbIsQuery, new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		lbName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbName.setText("字段名称:");
		lbColumnseq.setHorizontalAlignment(SwingConstants.RIGHT);
		lbColumnseq.setText("字段序号:");
		lbColumnTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lbColumnTitle.setText("显示名称:");
		lbIsDatadict.setHorizontalAlignment(SwingConstants.RIGHT);
		lbIsDatadict.setText("是否字典:");
		lbDatadictClass.setHorizontalAlignment(SwingConstants.RIGHT);
		lbDatadictClass.setText("字典类名:");
		lbDatadictAttr.setHorizontalAlignment(SwingConstants.RIGHT);
		lbDatadictAttr.setText("字典属性:");
		lbIsQuery.setHorizontalAlignment(SwingConstants.RIGHT);
		lbIsQuery.setText("是否查询:");
		lbIsQuery.setHorizontalAlignment(SwingConstants.RIGHT);

		cbIsDatadict.addItem("false");
		cbIsDatadict.addItem("true");

		cbIsQuery.addItem("all");
		cbIsQuery.addItem("search");
		cbIsQuery.addItem("none");

		cbO2OIsQuery.addItem("all");
		cbO2OIsQuery.addItem("search");
		cbO2OIsQuery.addItem("none");

		bottomPanel.add(lbO2OName, new GridBagConstraints(0, 0, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(tfO2OName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(lbO2OTitle, new GridBagConstraints(2, 0, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(tfO2OTitle, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(lbO2OJoinCol, new GridBagConstraints(0, 1, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(tfO2OJoinCol, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(lbO2OClass, new GridBagConstraints(2, 1, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(tfO2OClass, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(lbO2OClassCol, new GridBagConstraints(0, 2, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(tfO2OClassCol, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(lbO2OIsQuery, new GridBagConstraints(2, 2, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(cbO2OIsQuery, new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(lbO2OQueryClass, new GridBagConstraints(0, 3, 1, 1, 0.2, 1.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		bottomPanel.add(tfO2OQueryClass, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		lbO2OName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OName.setText("字段名称:");
		lbO2OSeq.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OSeq.setText("字段序号:");
		lbO2OTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OTitle.setText("显示名称:");
		lbO2OJoinCol.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OJoinCol.setText(" 连接列名:");
		lbO2OClass.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OClass.setText("对象B类:");
		lbO2OClassCol.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OClassCol.setText("B类字段:");
		lbO2OIsQuery.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OIsQuery.setText("是否查询:");
		lbO2OQueryClass.setHorizontalAlignment(SwingConstants.RIGHT);
		lbO2OQueryClass.setText("查询类:");

		tfBclass.setEditable(false);
		tfCname.setEditable(false);
		tfIsInsert.setEditable(false);
		tfIsUpdate.setEditable(false);
		tfLineNum.setEditable(false);
		tfLineMax.setEditable(false);
		tfName.setEditable(false);
		tfO2OName.setEditable(false);

		loadXmlNodes(root);
		loadAllNodes(rootAll);

		treeXml.addMouseListener(this);
		treeXml.addTreeExpansionListener(this);

		btAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onAddMenu();
			}
		});
		btDel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onDelMenu();
			}
		});
		btSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onSave();
			}
		});
		btCancle.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onCancle();
			}
		});
		btOutputXml.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onOutputXml();
			}
		});
		btOutputXls.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onOutputXls();
			}
		});
		cbIsDatadict.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onIsDataDict();
			}
		});
		cbO2OIsQuery.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onIsQuery();
			}
		});
	}

	// 树节点的click事件
	public void mouseClicked(MouseEvent e)
	{
		if (1 == e.getClickCount() && e.getModifiers() == MouseEvent.BUTTON1_MASK)
			showCategoryInfo();
	}

	/**
	 * 获取所有的node数据并add进treeXml
	 * 
	 * @param node
	 */
	private void loadXmlNodes(DefaultMutableTreeNode node)
	{
		if (domList.size() == 0)
			return;
		DefaultMutableTreeNode nodeModule = null;
		DefaultMutableTreeNode nodeSubPv = null;
		DefaultMutableTreeNode nodeSubPo = null;

		for (int i = 0; i < domList.size(); i++)
		{
			Property proModule = (Property) (((List) domList.get(i)).get(1));
			PropertyValue pvSub = null;
			PropertyObject poSub = null;
			nodeModule = new DefaultMutableTreeNode(proModule);
			node.add(nodeModule);
			for (int j = 0; j < proModule.getPropertyValue().size(); j++)
			{
				pvSub = ((PropertyValue) proModule.getPropertyValue().get(j));
				nodeSubPv = new DefaultMutableTreeNode(pvSub);
				nodeModule.add(nodeSubPv);
			}
			for (int k = 0; k < proModule.getPropertyObject().size(); k++)
			{
				poSub = ((PropertyObject) proModule.getPropertyObject().get(k));
				nodeSubPo = new DefaultMutableTreeNode(poSub);
				nodeModule.add(nodeSubPo);
			}
		}
	}

	/**
	 * 获取所有的node数据并add进treeAll
	 * 
	 * @param node
	 */
	private void loadAllNodes(DefaultMutableTreeNode node)
	{
		if (BclassList.size() == 0)
			return;
		DefaultMutableTreeNode nodeModule = null;
		DefaultMutableTreeNode nodeSubPv = null;
		DefaultMutableTreeNode nodeSubPo = null;

		for (int i = 0; i < BclassList.size(); i++)
		{
			Property proModule = (Property) (BclassList.get(i));
			PropertyValue pvSub = null;
			PropertyObject poSub = null;
			nodeModule = new DefaultMutableTreeNode(proModule);
			node.add(nodeModule);
			for (int j = 0; j < proModule.getPropertyValue().size(); j++)
			{
				pvSub = ((PropertyValue) proModule.getPropertyValue().get(j));
				nodeSubPv = new DefaultMutableTreeNode(pvSub);
				nodeModule.add(nodeSubPv);
			}
			for (int k = 0; k < proModule.getPropertyObject().size(); k++)
			{
				poSub = ((PropertyObject) proModule.getPropertyObject().get(k));
				nodeSubPo = new DefaultMutableTreeNode(poSub);
				nodeModule.add(nodeSubPo);
			}
		}
	}

	/**
	 * 在dataviewPanel显示xml树的节点属性
	 * 
	 * @param needCheck
	 * 
	 */
	private void showCategoryInfo()
	{

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeXml.getLastSelectedPathComponent();
		tempNode = node;
		if (node == null)
			return;
		DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode) node.getParent();
		Object nodeInfo = node.getUserObject();
		if (nodeParent == null)
			return;
		Object nodeParentInfo = nodeParent.getUserObject();

		if (node.isLeaf())
		{
			Property proNodeInfo = (Property) nodeParentInfo;
			showCategoryInfo(proNodeInfo);
			if (nodeInfo instanceof PropertyValue)
			{
				PropertyValue pvNodeInfo = (PropertyValue) nodeInfo;
				tempPropertyValue = pvNodeInfo;
				showCategoryInfo(pvNodeInfo);
			}
			else if (nodeInfo instanceof PropertyObject)
			{
				PropertyObject poNodeInfo = (PropertyObject) nodeInfo;
				tempPropertyObject = poNodeInfo;
				showCategoryInfo(poNodeInfo);
			}
		}
	}

	/**
	 * 更新Bclass相关的文本框中的值
	 * 
	 * @param category
	 */
	private void showCategoryInfo(Property pro)
	{
		tfBclass.setText(pro.getBclass().trim());
		tfCname.setText(pro.getCname().trim());
		tfIsInsert.setText(pro.getByInsert().trim());
		tfIsUpdate.setText(pro.getByUpdate().trim());
		tfLineNum.setText(pro.getThdLineNum().trim());
		tfLineMax.setText(pro.getThdLineMax().trim());
	}

	/**
	 * 更新PV相关的文本框中的值
	 * 
	 * @param category
	 */
	private void showCategoryInfo(PropertyValue pv)
	{
		resetPVTextField();
		resetPOTextField();
		if (pv.getName() == null)
			tfName.setText("");
		else
			tfName.setText(pv.getName().trim());
		if (pv.getColumnTitle() == null)
			tfColumnTitle.setText("");
		else
			tfColumnTitle.setText(pv.getColumnTitle().trim());
		if (pv.getDatadictClass() == null)
			tfDatadictClass.setText("");
		else
			tfDatadictClass.setText(pv.getDatadictClass().trim());
		if (pv.getDatadictAttr() == null)
			tfDatadictAttr.setText("");
		else
			tfDatadictAttr.setText(pv.getDatadictAttr().trim());
		cbIsDatadict.setSelectedItem(String.valueOf(pv.isDatadict()));
		cbIsQuery.setSelectedItem(String.valueOf(pv.getIsQuery()));
	}

	/**
	 * 更新PO相关的文本框中的值
	 * 
	 * @param category
	 */
	private void showCategoryInfo(PropertyObject po)
	{

		resetPVTextField();
		resetPOTextField();
		if (po.getName() == null)
			tfO2OName.setText("");
		else
			tfO2OName.setText(po.getName().trim());
		if (po.getColumnTitle() == null)
			tfO2OTitle.setText("");
		else
			tfO2OTitle.setText(po.getColumnTitle().trim());
		if (po.getJoinColumn() == null)
			tfO2OJoinCol.setText("");
		else
			tfO2OJoinCol.setText(po.getJoinColumn().trim());
		if (po.getClassName() == null)
			tfO2OClass.setText("");
		else
			tfO2OClass.setText(po.getClassName().trim());
		if (po.getClassColumn() == null)
			tfO2OClassCol.setText("");
		else
			tfO2OClassCol.setText(po.getClassColumn().trim());
		if (po.getQueryClass() == null)
			tfO2OQueryClass.setText("");
		else
			tfO2OQueryClass.setText(po.getQueryClass().trim());
		cbO2OIsQuery.setSelectedItem(String.valueOf(po.getIsQuery()));
	}

	/**
	 * 将PV相关的文本框中的值设空
	 */
	private void resetPVTextField()
	{
		tfName.setText("");
		tfColumnseq.setText("");
		tfColumnTitle.setText("");
		tfDatadictClass.setText("");
		tfDatadictAttr.setText("");
	}

	/**
	 * 将PO相关的文本框中的值设空
	 */
	private void resetPOTextField()
	{
		tfO2OName.setText("");
		tfO2OSeq.setText("");
		tfO2OTitle.setText("");
		tfO2OJoinCol.setText("");
		tfO2OClass.setText("");
		tfO2OClassCol.setText("");
		tfO2OQueryClass.setText("");
	}

	/**
	 * 对树枝进行展开和回缩时的初始化和验证
	 */
	private void treeOperationInit()
	{
		// resetTextField();
		// haveSaved = true;
	}

	/**
	 * 响应应用按钮触发的事件
	 * 
	 * @return
	 */
	private void save()
	{
		int question = JGxluMessageBox.show(CommonClientEnvironment.getMainFrame(), "您确定采用如列表所示的数据字典的显示顺序吗？", "确认",
			JGxluMessageBox.QUESTION_MESSAGE, JGxluMessageBox.YES_NO_OPTION);
		if (question != JGxluMessageBox.YES_OPTION)
		{
			return;
		}
		else
		{

		}
		// return saveDictionary();
	}

	public boolean perform(int nResourceID)
	{
		switch (nResourceID) {
		case JGxluSimpleDialog.SB_APPLY:
			save();
			return true;
			// case JGxluSimpleDialog.SB_HELP:
			// return false;
		case JGxluSimpleDialog.SB_EXIT:
			return true;
		case JGxluSimpleDialog.SB_CANCEL:
			return true;
		}
		return true;
	}

	// 事件
	public void treeExpanded(TreeExpansionEvent event)
	{
		treeOperationInit();
	}

	public void treeCollapsed(TreeExpansionEvent event)
	{
		treeOperationInit();
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void actionPerformed(ActionEvent e)
	{

	}

	public void initDialog()
	{
		// TODO Auto-generated method stub

	}

	public void showSimpleDialog()
	{
		JGxluSimpleDialog simpledialog = new JGxluSimpleDialog(CommonClientEnvironment.getMainFrame(), this, this,
				JGxluSimpleDialog.SB_APPLY | JGxluSimpleDialog.SB_EXIT, // Modify-Key:
				"导出字段配置");
		simpledialog.centerDialog();
		simpledialog.setVisible(true);
	}

	/**
	 * 显示窗口
	 */
	public void showWindow()
	{
		String functionName = "UIXmlCfgMgtTree";

		JGxluChildWindow cw = JGxluChildWindow.getOriginalChildWindowByFunction(functionName);

		if (cw == null)
		{
			JGxluWindow win = new JGxluWindow(this, this, JGxluWindow.SB_EXIT);
			String caption = "模版配置管理";
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

	/**
	 * 根据左侧树增加右侧树节点
	 */
	public void onAddMenu()
	{
		TreePath[] pathSelects = treeAll.getSelectionPaths();
		if (pathSelects == null)
		{
			return;
		}
		for (int k = 0; k < pathSelects.length; k++)
		{
			// 增加选择的路径
			Object[] nodes = pathSelects[k].getPath();
			for (int i = 1; i < nodes.length; i++)
			{
				addNode((DefaultMutableTreeNode) nodes[i - 1], (DefaultMutableTreeNode) nodes[i], false);
			}
			// 如果路径的左后一个节点还有儿子，将儿子全部加入
			DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) pathSelects[k].getLastPathComponent();
			if (lastNode.getChildCount() != 0)
			{
				for (int i = 0; i < lastNode.getChildCount(); i++)
				{
					addNode(lastNode, (DefaultMutableTreeNode) lastNode.getChildAt(i), true);
				}
			}
		}
		treeXml.updateUI();
	}

	/**
	 * 在父亲节点下增加节点，注意：入参都为左面树上的节点
	 * 
	 * @param _parentNode DefaultMutableTreeNode
	 * @param _node DefaultMutableTreeNode
	 * @param isAddChild boolean
	 */
	private void addNode(DefaultMutableTreeNode _parentNode, DefaultMutableTreeNode _node, boolean isAddChild)
	{
		if (searchNodeInUserTree(_node) != null)
		{
			return;
		}
		else
		{
			DefaultMutableTreeNode parentNode = searchNodeInUserTree(_parentNode);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(_node.getUserObject());
			parentNode.add(node);
			if (isAddChild)
			{
				if (_node.getChildCount() != 0)
				{
					for (int i = 0; i < _node.getChildCount(); i++)
					{
						addNode(_node, (DefaultMutableTreeNode) _node.getChildAt(i), true);
					}
				}
			}
		}
	}

	/**
	 * 在"已选字段"树上删除节点
	 */
	public void onDelMenu()
	{
		TreePath[] pathSelects = treeXml.getSelectionPaths();
		if (pathSelects == null)
		{
			return;
		}
		for (int k = 0; k < pathSelects.length; k++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) pathSelects[k].getLastPathComponent();
			if (node.getParent() != null)
			{
				removeNode((DefaultMutableTreeNode) node.getParent(), node);
			}
		}
		// 注释掉删除节点展开该树的功能
		// expandAll(treeXml, true);
		treeXml.updateUI();
	}

	/**
	 * 删除节点，如果没有兄弟，说明目录已空，递归删除父亲
	 * 
	 * @param parent DefaultMutableTreeNode
	 * @param node DefaultMutableTreeNode
	 */
	private void removeNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode node)
	{
		parent.remove(node);
		if (parent.getChildCount() == 0 && parent.getParent() != null)
		{
			removeNode((DefaultMutableTreeNode) parent.getParent(), parent);
		}
	}

	/**
	 * 在用户权限树上寻找节点
	 * 
	 * @param node DefaultMutableTreeNode
	 * @return DefaultMutableTreeNode
	 */
	private DefaultMutableTreeNode searchNodeInUserTree(DefaultMutableTreeNode node)
	{
		// 若是根节点
		if (node.getUserObject() instanceof String && node.toString().equals("导出字段"))
		{
			return (DefaultMutableTreeNode) treeXml.getModel().getRoot();
		}
		// 若非根节点，则从与根节点开始递归比较
		return searchNodeInUserTree((DefaultMutableTreeNode) treeXml.getModel().getRoot(), node);
	}

	/**
	 * 在用户权限树上递归寻找节点
	 * 
	 * @param parentNode DefaultMutableTreeNode
	 * @param node DefaultMutableTreeNode
	 * @return DefaultMutableTreeNode
	 */
	private DefaultMutableTreeNode searchNodeInUserTree(DefaultMutableTreeNode currentNode, DefaultMutableTreeNode node)
	{
		if (currentNode.getChildCount() == 0)
		{
			return null;
		}

		// 子节点中找到，返回子节点
		for (int i = 0; i < currentNode.getChildCount(); i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentNode.getChildAt(i);
			// if ((child.toString()).equals(node.getUserObject().toString()))
			// return child;
			// Modified by ZhangWei 2010-03-29 for add new node for MR RSW10-772
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			if ((child.toString()).equals(node.getUserObject().toString())
				&& (currentNode.toString()).equals(parent.toString()))
				return child;
		}

		// 子节点中未找到，则递归，在子节点的子节点中递归寻找
		for (int i = 0; i < currentNode.getChildCount(); i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentNode.getChildAt(i);
			DefaultMutableTreeNode result = searchNodeInUserTree(child, node);
			if (result != null)
			{
				return result;
			}
		}
		return null;
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

	/**
	 * 保存节点编辑
	 */
	public void onSave()
	{
		TreePath[] pathSelects = treeXml.getSelectionPaths();
		if (pathSelects == null)
		{
			return;
		}
		if (pathSelects.length > 1)
		{
			JGxluMessageBox.show(this, "请确认只选择了一个节点！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		Object[] nodes = pathSelects[0].getPath();
		if (nodes.length < 3)
		{
			JGxluMessageBox.show(this, "请确认选择的是叶节点！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		DefaultMutableTreeNode BclassNode = (DefaultMutableTreeNode) nodes[nodes.length - 2];
		DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) nodes[nodes.length - 1];
		if (leaf.toString().equals(tfName.getText().trim()) == false
			&& leaf.toString().equals(tfO2OName.getText().trim()) == false)
		{
			JGxluMessageBox.show(this, "请确认左边选择的节点和右边的节点信息保持一致！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		Object leafInfo = leaf.getUserObject();
		if (leafInfo instanceof PropertyValue)
		{
			if (tfColumnTitle.getText() == null || tfColumnTitle.getText().equals(""))
			{
				JGxluMessageBox.show(this, "请必须填写“显示名称”！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return;
			}
			PropertyValue pv = (PropertyValue) leafInfo;
			pv.setName(tfName.getText());
			pv.setColumnTitle(tfColumnTitle.getText());
			pv.setDatadictClass(tfDatadictClass.getText());
			pv.setDatadictAttr(tfDatadictAttr.getText());
			pv.setDatadict(Boolean.valueOf(cbIsDatadict.getValueString()));
			pv.setIsQuery(cbIsQuery.getValueString());
		}
		else if (leafInfo instanceof PropertyObject)
		{
			if (tfO2OTitle.getText() == null || tfO2OTitle.getText().equals(""))
			{
				JGxluMessageBox.show(this, "请必须填写“显示名称”！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return;
			}
			PropertyObject po = (PropertyObject) leafInfo;
			po.setName(tfO2OName.getText());
			po.setColumnTitle(tfO2OTitle.getText());
			// po.setColumnSeq(tfO2OSeq.getText());
			po.setJoinColumn(tfO2OJoinCol.getText());
			po.setClassName(tfO2OClass.getText());
			po.setClassColumn(tfO2OClassCol.getText());
			po.setIsQuery(cbO2OIsQuery.getValueString());
			po.setQueryClass(tfO2OQueryClass.getValueString());
		}

		JGxluMessageBox.show(this, "节点属性修改成功！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
		treeXml.updateUI();
	}

	public void setSeqs(DefaultMutableTreeNode parent)
	{
		for (int i = 0; i < parent.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
			Object nodeInfo = node.getUserObject();
			if (nodeInfo instanceof PropertyValue)
			{
				PropertyValue pv = (PropertyValue) nodeInfo;
				pv.setColumnSeq(String.valueOf(i + 1));
			}
			else if (nodeInfo instanceof PropertyObject)
			{
				PropertyObject po = (PropertyObject) nodeInfo;
				po.setColumnSeq(String.valueOf(i + 1));
			}
		}
	}

	/**
	 * 取消节点编辑
	 */
	public void onCancle()
	{
		showCategoryInfo();
	}

	/**
	 * 导出模版配置文件
	 */
	public void onOutputXml()
	{
		DefaultMutableTreeNode BclassNode = (DefaultMutableTreeNode) treeXml.getLastSelectedPathComponent();
		if (BclassNode == null)
		{
			JGxluMessageBox.show(this, "请先选择目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode) BclassNode.getParent();
		if (nodeParent == null)
		{
			JGxluMessageBox.show(this, "请先选择目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
			return;
		}
		if (nodeParent.equals(root) == false)
		{
			DefaultMutableTreeNode nodeParentParent = (DefaultMutableTreeNode) nodeParent.getParent();
			if (nodeParentParent == null)
			{
				JGxluMessageBox.show(this, "请先选择目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return;
			}
			if (nodeParentParent.equals(root) == true)
				BclassNode = nodeParent;
			else
			{
				JGxluMessageBox.show(this, "请正确选择“导出字段”下面第一级子菜单！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return;
			}
		}

		Property p = null;
		PropertyValue pv = null;
		PropertyObject po = null;
		List pList = new ArrayList(), pvList = new ArrayList(), poList = new ArrayList();

		p = (Property) BclassNode.getUserObject();
		String strBclass = ((Property) BclassNode.getUserObject()).getBclass();
		strBclass = strBclass.substring(strBclass.lastIndexOf(".") + 2, strBclass.length());
		for (int j = 0; j < BclassNode.getChildCount(); j++)
		{
			DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) BclassNode.getChildAt(j);
			Object leafInfo = leaf.getUserObject();
			if (leafInfo instanceof PropertyValue)
			{
				pv = (PropertyValue) leafInfo;
				if (pv.getColumnTitle() == null || pv.getColumnTitle().equals(""))
				{
					JGxluMessageBox.show(this, "请必须填写【" + pv.getName() + "】显示名称！", "提示",
						JGxluMessageBox.INFORMATION_MESSAGE);
					return;
				}
				pvList.add(pv);
			}
			else if (leafInfo instanceof PropertyObject)
			{
				po = (PropertyObject) leafInfo;
				if (po.getColumnTitle() == null || po.getColumnTitle().equals(""))
				{
					JGxluMessageBox.show(this, "请必须填写【" + po.getName() + "】显示名称！", "提示",
						JGxluMessageBox.INFORMATION_MESSAGE);
					return;
				}
				poList.add(po);
			}
		}
		p.setPropertyValue(pvList);
		p.setPropertyObject(poList);
		List params = new ArrayList();
		String strPath = new String();
		ContainerBootStrap bootStrap = new ContainerBootStrap();
		bootStrap.startup();
		DomParse domTemplateParse = new DomTemplateParse();
		List templateList = domTemplateParse.getTemplateFileList();
		if (templateList.size() < 0)
			return;
		for (int i = 0; i < templateList.size(); i++)
		{
			String strTemplatePath = templateList.get(i).toString();
			String templateName = strTemplatePath.substring(strTemplatePath.lastIndexOf("/") + 1, strTemplatePath
					.length() - 4);
			if (templateName.equalsIgnoreCase(strBclass))
			{
				strPath = strTemplatePath;
				break;
			}
		}
		params.add(strPath);
		DomHelper domhelper = new DomHelper(domTemplateParse);
		List result = domhelper.updateDomParse(params, p);
		if ((Boolean) result.get(0) == false)
		{
			JGxluMessageBox.show(this, "模板文件生成失败，失败原因是" + result.get(0) + "！", "提示", JGxluMessageBox.ERROR_MESSAGE);
			return;
		}
		JGxluMessageBox.show(this, "模板配置文件生成成功！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
	}

	// /**
	// * 导出模版配置文件
	// */
	// public void onOutputXml()
	// {
	// Property p = null;
	// PropertyValue pv = null;
	// PropertyObject po = null;
	// List pList = new ArrayList();
	// ;
	// List pvList = null;
	// List poList = null;
	// DomParse domTemplateParse = new DomTemplateParse();
	// DomHelper domhelper = new DomHelper(domTemplateParse);
	// for (int i = 0; i < root.getChildCount(); i++)
	// {
	// p = new Property();
	// pvList = new ArrayList();
	// poList = new ArrayList();
	// DefaultMutableTreeNode BclassNode = (DefaultMutableTreeNode) root.getChildAt(i);
	// Object BclassInfo = BclassNode.getUserObject();
	// p = (Property) BclassInfo;
	// for (int j = 0; j < BclassNode.getChildCount(); j++)
	// {
	// DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) BclassNode.getChildAt(j);
	// Object leafInfo = leaf.getUserObject();
	// if (leafInfo instanceof PropertyValue)
	// {
	// pv = (PropertyValue) leafInfo;
	// pvList.add(pv);
	// }
	// else if (leafInfo instanceof PropertyObject)
	// {
	// po = (PropertyObject) leafInfo;
	// poList.add(po);
	// }
	// }
	// p.setPropertyValue(pvList);
	// p.setPropertyObject(poList);
	// List params = new ArrayList();
	// params.add(((List) domList.get(i)).get(0));
	// List result = domhelper.updateDomParse(params, p);
	// if ((Boolean) result.get(0) == false)
	// {
	// JGxluMessageBox.show(this, "模板文件生成失败，失败原因是" + result.get(0) + "！", "提示",
	// JGxluMessageBox.ERROR_MESSAGE);
	// return;
	// }
	// }
	// // domhelper.updateDomParse(params, object)
	//
	// JGxluMessageBox.show(this, "模板配置文件生成成功！", "提示",
	// JGxluMessageBox.INFORMATION_MESSAGE);
	// }

	/**
	 * 导出模版文件
	 */
	public void onOutputXls()
	{
		try
		{
			DefaultMutableTreeNode BclassNode = (DefaultMutableTreeNode) treeXml.getLastSelectedPathComponent();
			if (BclassNode == null)
			{
				JGxluMessageBox.show(this, "请先选择目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return;
			}
			DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode) BclassNode.getParent();
			if (nodeParent == null)
			{
				JGxluMessageBox.show(this, "请先选择目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
				return;
			}
			if (nodeParent.equals(root) == false)
			{
				DefaultMutableTreeNode nodeParentParent = (DefaultMutableTreeNode) nodeParent.getParent();
				if (nodeParentParent == null)
				{
					JGxluMessageBox.show(this, "请先选择目标！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
					return;
				}
				if (nodeParentParent.equals(root) == true)
					BclassNode = nodeParent;
				else
				{
					JGxluMessageBox.show(this, "请正确选择“导出字段”下面第一级子菜单！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
					return;
				}
			}
			String strPath = new String();
			ContainerBootStrap bootStrap = new ContainerBootStrap();
			bootStrap.startup();
			DomParse domTemplateParse = new DomTemplateParse();
			List templateList = domTemplateParse.getTemplateFileList();
			Property p = (Property) BclassNode.getUserObject();
			String strBclass = ((Property) BclassNode.getUserObject()).getBclass();
			strBclass = strBclass.substring(strBclass.lastIndexOf(".") + 2, strBclass.length());
			if (templateList.size() < 0)
				return;
			for (int i = 0; i < templateList.size(); i++)
			{
				String strTemplatePath = templateList.get(i).toString();
				String templateName = strTemplatePath.substring(strTemplatePath.lastIndexOf("/") + 1, strTemplatePath
						.length() - 4);
				if (templateName.equalsIgnoreCase(strBclass))
				{
					strPath = strTemplatePath;
					break;
				}
			}
			Class BClass = (Class) ((ContainerImpl) ContainerFactory.getContainer()).lookupBObject(strPath);
			Context context = ExectionUtil.getContext();
			ArrayLoader arrayLoader = (ArrayLoader) context.lookupSessionBean(ClassNoteNames.ARRAYLOADER_BEAN);
			ResultController resultController = arrayLoader.getTitleList(BClass);
			ExportUtil.printTemplet(resultController);

		}
		catch (ContainerException e)
		{
			e.printStackTrace();
		}
	}

	// /**
	// * 导出模版文件
	// */
	// public void onOutputXls()
	// {
	// try
	// {
	// ContainerBootStrap bootStrap = new ContainerBootStrap();
	// bootStrap.startup();
	// DomParse domTemplateParse = new DomTemplateParse();
	// List templateList = domTemplateParse.getTemplateFileList();
	// if (templateList.size() < 0)
	// return;
	// for (int i = 0; i < templateList.size(); i++)
	// {
	// String strTemplatePath = templateList.get(i).toString();
	// Class BClass = (Class) ((ContainerImpl)
	// ContainerFactory.getContainer()).lookupBObject(strTemplatePath);
	// Context context = ExectionUtil.getContext();
	// ArrayLoader arrayLoader = (ArrayLoader)
	// context.lookupSessionBean(ClassNoteNames.ARRAYLOADER_BEAN);
	// ResultController resultController = arrayLoader.getTitleList(BClass);
	// ExportUtil.printTemplet(resultController);
	// // if ((Boolean) result.get(0) == false)
	// // {
	// // JGxluMessageBox.show(this, "模板文件生成失败，失败原因是" + result.get(0) + "！");
	// // return;
	// // }
	// }
	//
	// }
	// catch (ContainerException e)
	// {
	// e.printStackTrace();
	// }
	// // domhelper.updateDomParse(params, object)
	//
	// JGxluMessageBox.show(this, "模板文件生成成功！", "提示", JGxluMessageBox.INFORMATION_MESSAGE);
	// }

	public void onIsDataDict()
	{
		if (cbIsDatadict.getSelectedItem().toString().equalsIgnoreCase("true"))
		{
			tfDatadictClass.setEditable(true);
			tfDatadictAttr.setEditable(true);
		}
		else
		{
			tfDatadictClass.setEditable(false);
			tfDatadictAttr.setEditable(false);
		}
	}

	public void onIsQuery()
	{
		if (cbO2OIsQuery.getSelectedItem().toString().equalsIgnoreCase("all"))
			tfO2OQueryClass.setEditable(true);
		else if (cbO2OIsQuery.getSelectedItem().toString().equalsIgnoreCase("search"))
			tfO2OQueryClass.setEditable(true);
		else if (cbO2OIsQuery.getSelectedItem().toString().equalsIgnoreCase("none"))
			tfO2OQueryClass.setEditable(false);
	}

//	public void popUpDataDict()
//	{
//		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeXml.getLastSelectedPathComponent();
//		if (node == null)
//			return;
//		Object nodeInfo = node.getUserObject();
//		String classID = "", attributeID = "";
//		if (node.isLeaf())
//		{
//			if (nodeInfo instanceof PropertyValue)
//			{
//				// PropertyValue pvNodeInfo = (PropertyValue) nodeInfo;
//				classID = ((PropertyValue) nodeInfo).getDatadictClass();
//				attributeID = ((PropertyValue) nodeInfo).getDatadictAttr();
//			}
//			else
//				return;
//		}
//		// String appModule = CommonClientEnvironment.getSubModule();
//		// IESysDictionaryMgt panel = new IESysDictionaryMgt(appModule);
//		// // panel.setDefaultSysDictionary(classID,attributeID);
//		// panel.showSimpleDialog();
//		String appModule = CommonClientEnvironment.getSubModule();
//		UISysDictionaryMgt panel = new UISysDictionaryMgt(appModule);
//		panel.setDefaultSysDictionary(classID, attributeID);
//		panel.showSimpleDialog();
//	}

	/**
	 * 树渲染器
	 */
	private class SysDataTreeCellRenderer extends DefaultTreeCellRenderer
	{
		// 节点颜色
		private Color strNodeColor = Color.black;

		private Color savedColor = Color.blue;

		private Color unsavedColor = Color.green;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

			if (node != null && node.isLeaf())
			{
				Object nodeInfo = node.getUserObject();
				if (nodeInfo instanceof PropertyValue)
				{
					PropertyValue pv = (PropertyValue) nodeInfo;
					if (pv.getColumnTitle() == null)
						setForeground(savedColor);
					else
						setForeground(unsavedColor);
				}
				else if (nodeInfo instanceof PropertyObject)
				{
					PropertyObject po = (PropertyObject) nodeInfo;
					if (po.getColumnTitle() == null)
						setForeground(savedColor);
					else
						setForeground(unsavedColor);
				}
			}
			return this;
		}
	}

	class myJGxluFilterTextDocument extends PlainDocument
	{
		private boolean haveDot = false;

		private int length = 0;

		private double max = Double.MIN_VALUE, min = Double.MAX_VALUE;

		private boolean isLimit = false;

		/**
		 * 构造一个缺省的JGxluFilterTextDocument。
		 * 
		 * @since 0.1
		 */
		public myJGxluFilterTextDocument()
		{

		}

		/**
		 * 构造一个限制数字大小的JGxluFilterTextDocument。 只有在max不小于min的情况下max和min的设置才生效。
		 * 
		 * @param max 最大值
		 * @param min 最小值
		 * @since 0.3
		 */
		public myJGxluFilterTextDocument(double max, double min)
		{
			if (max >= min)
			{
				this.max = max;
				this.min = min;
			}
			this.isLimit = true;
		}

		/**
		 * 构造一个限制数字大小的NumberOnlyDocument。 只有在max不小于min的情况下max和min的设置才生效。
		 * 
		 * @param max 最大值
		 * @param min 最小值
		 * @param isLimit 是否限制数字大小
		 * @since 0.3
		 */
		public myJGxluFilterTextDocument(double max, double min, boolean isLimit)
		{
			if (max >= min)
			{
				this.max = max;
				this.min = min;
			}
			this.isLimit = isLimit;
		}

		/**
		 * 设置是否限制数字的大小。 如果参数是true时只有在原来的最大值大于最小值是这个设置才起效，否则此方法调用没有任何效果。
		 * 如果参数值是false则取消输入数字大小的限制
		 * 
		 * @param isLimit 是否限制输入数字的大小
		 * @since 0.3
		 */
		public void setLimit(boolean isLimit)
		{
			if (isLimit == true && max >= min)
			{
				this.isLimit = isLimit;
			}
			else if (isLimit == false)
			{
				this.isLimit = isLimit;
			}
		}

		/**
		 * 设置可以输入的值的范围。 如果max小于min则可以输入的最大值和最小值的限制无效，作为没有大小限制的情况处理。
		 * 
		 * @param max 可以输入的最大值
		 * @param min 可以输入的最小值
		 * @since 0.3
		 */
		public void setRange(double max, double min)
		{
			if (max >= min)
			{
				this.max = max;
				this.min = min;
				isLimit = true;
			}
		}

		/**
		 * 是否限制数字的大小。
		 * 
		 * @return 限制输入大小时返回true，否则返回false。
		 * @since 0.3
		 */
		public boolean isLimit()
		{
			return isLimit;
		}

		/**
		 * 返回限制的最大值。
		 * 
		 * @return 限制的最大值。如果是不限制则返回Double.MIN_VALUE。
		 * @since 0.3
		 */
		public double getLimitedMax()
		{
			if (!isLimit)
			{
				return Double.MIN_VALUE;
			}
			else
			{
				return max;
			}
		}

		/**
		 * 返回限制的最小值。
		 * 
		 * @return 限制的最小值。如果是不限制则返回Double.MAX_VALUE。
		 * @since 0.3
		 */
		public double getLimitedMin()
		{
			if (!isLimit)
			{
				return Double.MAX_VALUE;
			}
			else
			{
				return min;
			}
		}

		/**
		 * 插入某些内容到文档中。 只有当输入的是数字或者和数字相关的“.”、“-”等符号并且符合构成合法数字的规则时才被插入。 此方法是线程安全的。
		 * 
		 * @param offs 插入位置的偏移量
		 * @param str 插入内容
		 * @param a 属性集合
		 * @throws BadLocationException 给出的插入位置不是文档中的有效位置
		 * @since 0.1
		 */
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{

			if (str == null)
			{
				return;
			}
			char[] number = str.toUpperCase().toCharArray();
			for (int i = 0; i < number.length; i++)
			{
				if (offs == 0)
				{
					if (!((number[i] >= '0' && number[i] <= '9') || number[i] == '-' || (number[i] >= 'A' && number[i] <= 'F')))
					{
						if (offs == length - 1)
						{
							remove(offs + i, 1);
						}
						else
						{
							return;
						}

					}
					else
					{
						length++;
					}
				}
				else
				{
					if (!haveDot)
					{
						if (!(number[i] >= '0' && number[i] <= '9' || number[i] == '-' || (number[i] >= 'A' && number[i] <= 'F')))
						{
							if (offs == length - 1)
							{
								remove(offs + i, 1);
							}
							else
							{
								return;
							}
						}
						else
						{
							if (number[i] == '.')
							{
								haveDot = true;
							}
							length++;
						}
					}
					else
					{
						if (!(number[i] >= '0' && number[i] <= '9' || number[i] == '-' || (number[i] >= 'A' && number[i] <= 'F')))
						{
							if (offs == length - 1)
							{
								remove(offs + i, 1);
							}
							else
							{
								Toolkit.getDefaultToolkit().beep();
								return;
							}
						}
						else
						{
							length++;
						}
					}
				}
			}
			if (isLimit == true)
			{
				String text = getText(0, offs) + str + getText(offs, getLength());
				double result = Double.parseDouble(text);
				if (result > max || result < min)
				{
					return;
				}
			}
			super.insertString(offs, new String(number), a);
		}
	}
}

/**********************************************************************
 *$RCSfile: UIXmlCfgMgtTree.java,v $ $Revision: 1.3 $ $Date: 2010/04/20 02:08:06 $
 * 
 *$Log: UIXmlCfgMgtTree.java,v $
 *Revision 1.3  2010/04/20 02:08:06  wudawei
 *20100420
 * Revision 1.1 2010/01/19 01:16:46 zhangw empty log
 * message ***
 * 
 * 
 *********************************************************************/

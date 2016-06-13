/**************************************************************************
 * $$RCSfile: LevelsElementDef.java,v $$  $$Revision: 1.14 $$  $$Date: 2010/06/04 07:43:49 $$
 *
 * $$Log: LevelsElementDef.java,v $
 * $Revision 1.14  2010/06/04 07:43:49  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.13  2010/05/28 05:18:40  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.12  2010/05/26 09:28:43  zhangj
 * $AC,AP,WlanHotspot业务处理
 * $
 * $Revision 1.11  2010/05/20 05:06:24  zhangj
 * $增加WLAN热点,AC,AP的业务验证
 * $
 * $Revision 1.10  2010/05/18 06:18:42  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.9  2010/05/14 09:38:13  zhangj
 * $20100514更新
 * $
 * $Revision 1.8  2010/05/07 12:52:54  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.7  2010/05/06 07:10:28  zhangj
 * $*** empty log message ***
 * $
 * $Revision 1.6  2010/04/20 02:08:02  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.definition;

import gxlu.afx.system.common.CommonClientEnvironment;
import gxlu.afx.system.common.FieldDictionary;
import gxlu.afx.system.common.RateListFactory;
import gxlu.afx.system.common.SysDictionaryFactory;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.afx.system.query.client.BQueryClient;
import gxlu.afx.system.query.common.QueryExpr;
import gxlu.afx.system.query.common.QueryExprBuilder;
import gxlu.afx.system.ratelist.common.businessobject.BRateList;
import gxlu.afx.system.ratelist.service.dataobject.RateList;
import gxlu.afx.system.sysconfig.client.SysConfig;
import gxlu.afx.system.sysconfig.common.SysConfigConst;
import gxlu.afx.system.topo.common.businessobject.BMap;
import gxlu.afx.system.topo.common.businessobject.BMapNode;
import gxlu.afx.system.user.common.businessobject.BOperator;
import gxlu.ietools.basic.elements.definition.util.datanetwork.BDNPathOperation;
import gxlu.ietools.basic.exception.ElementsException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.util.PropertyConstvalue;
import gxlu.ietools.basic.system.util.VariableNames;
import gxlu.ietools.property.mapping.Property;
import gxlu.ossc.datanetwork.common.Context;
import gxlu.ossc.datanetwork.common.bizobject.BDNVertex;
import gxlu.ossc.datanetwork.common.bizobject.BMaintainer;
import gxlu.ossc.datanetwork.common.bizobject.logical.BATMCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BE1CTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BFRCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BUIFCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BUIFSubCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BWlanHotspot;
import gxlu.ossc.datanetwork.common.bizobject.physical.BATM;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDDN;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNDevice;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNDeviceAsset;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNDeviceAudit;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNDeviceType;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNPort;
import gxlu.ossc.datanetwork.common.bizobject.physical.BFR;
import gxlu.ossc.datanetwork.common.bizobject.physical.BShelf;
import gxlu.ossc.datanetwork.common.bizobject.provision.BADNPathTerminal;
import gxlu.ossc.datanetwork.common.bizobject.provision.BATMPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BCommonService;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDDNMultiPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDDNPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNBusinessPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNBusinessPathAudits;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNPathConnection;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNPathTerminal;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNRoute;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDistrict;
import gxlu.ossc.datanetwork.common.bizobject.provision.BFRPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BFRPortService;
import gxlu.ossc.datanetwork.common.bizobject.provision.BInformationPoint;
import gxlu.ossc.datanetwork.common.bizobject.provision.BPathConnection_Route;
import gxlu.ossc.datanetwork.common.bizobject.provision.BRelayPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BTwigDevice;
import gxlu.ossc.datanetwork.common.bizobject.provision.BUserPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BZDNPathTerminal;
import gxlu.ossc.datanetwork.common.convertor.BDConvertor;
import gxlu.ossc.datanetwork.common.dataobject.Maintainer;
import gxlu.ossc.datanetwork.common.dataobject.logical.ATMCTP;
import gxlu.ossc.datanetwork.common.dataobject.logical.E1CTP;
import gxlu.ossc.datanetwork.common.dataobject.logical.FRCTP;
import gxlu.ossc.datanetwork.common.dataobject.logical.UIFCTP;
import gxlu.ossc.datanetwork.common.dataobject.physical.Connector;
import gxlu.ossc.datanetwork.common.dataobject.physical.DNDevice;
import gxlu.ossc.datanetwork.common.dataobject.physical.DNPort;
import gxlu.ossc.datanetwork.common.dataobject.physical.DNSlot;
import gxlu.ossc.datanetwork.common.dataobject.physical.PortAssoc;
import gxlu.ossc.datanetwork.common.dataobject.physical.Shelf;
import gxlu.ossc.datanetwork.common.dataobject.provision.DNPath;
import gxlu.ossc.datanetwork.common.dataobject.provision.DNPathConnection;
import gxlu.ossc.datanetwork.common.dataobject.provision.DNPathTerminal;
import gxlu.ossc.datanetwork.common.dataobject.provision.PathConnection_Route;
import gxlu.ossc.datanetwork.provision.api.common.pathtextdesc.TextDescHashTable;
import gxlu.ossc.datanetwork.provision.components.service.ProvisionServerUtil;
import gxlu.ossc.datanetwork.provision.components.service.path.BDNPathServer;
import gxlu.ossc.datanetwork.provision.components.service.path.BPathConnectionServer;
import gxlu.ossc.datanetwork.provision.components.service.path.BPathServerUtil;
import gxlu.ossc.datanetwork.provision.components.service.path.BPathTextServer;
import gxlu.ossc.datanetwork.provision.components.service.path.BTerminalServer;
import gxlu.ossc.datanetwork.provision.components.service.path.domains.IDomainServer;
import gxlu.ossc.inventory.basic.common.bizobject.asset.BAssetCategory;
import gxlu.ossc.inventory.basic.common.bizobject.location.BHost;
import gxlu.ossc.inventory.basic.common.bizobject.location.BRegion;
import gxlu.ossc.inventory.basic.common.bizobject.location.BRoom;
import gxlu.ossc.inventory.basic.common.bizobject.location.BSite;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BACdistributionScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BAcCabinet;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BAcdcDistributionScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BAirconditionBox;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BBootBattery;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BCenterAirconditionerRefrigerator;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BCollectionStation;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BControlBoard;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BControllerEngine;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BCoolantPump;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BCoolingTower;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BDcCabinet;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BDcDistributionBox;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BDcDistributionScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BDryTransformer;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BFireequ;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BFreezePump;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BGeneratrixCopper;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BGuardModule;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighCoilinScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighCoiloutScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighFreqNE;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighGeneratrixScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighLiaisonScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighMeasureScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighPressureScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighRingScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighShieldScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighSwitchScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BHighVoltOthers;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BIndirectcur;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BIndoorEngine;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowCoilinScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowCompensationScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowDistributionBox;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowFreqNE;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowLiaisonScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowMeasureScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowOutputScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowUserScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BLowVoltOthers;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOilSwitchScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOilengineGenerator;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOilimmerTransformer;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOliEngineAccessScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOnetomanySinairconditioner;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOnetooneSinairconditioner;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOperateBatScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOperateControlScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOperatePowerScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BOutdoorEngine;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPdr;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPowerNE;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPowerNEAsset;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BRackPdr;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BSignalScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BStoragebat;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BSwitchPowerScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BTank;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BThunderDef;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BTransene;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BUPSCabinet;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BUPSHost;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BUpsDistributionScreen;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BUpsbat;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BWYQ;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BWindowAirconditioner;
import gxlu.ossc.inventory.basic.common.bizobject.project.BCustomer;
import gxlu.ossc.inventory.basic.common.bizobject.project.BProject;
import gxlu.ossc.inventory.basic.common.dataobject.Customer;
import gxlu.ossc.inventory.basic.common.dataobject.Project;
import gxlu.ossc.mobilenetwork.common.bizobject.logical.BMNTrunk;
import gxlu.ossc.mobilenetwork.common.bizobject.logical.BMNTrunkAudits;
import gxlu.ossc.mobilenetwork.common.bizobject.physical.BMNNE;
import gxlu.ossc.mobilenetwork.common.bizobject.physical.BMNPort;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import TOPLink.Public.Expressions.Expression;
import TOPLink.Public.Expressions.ExpressionBuilder;

/**
 * Definition of LevelsElementDef.
 * 
 * @author kidd
 */
public class LevelsElementDef extends BaseElementDef {

	public LevelsElementDef() {
	}

	/**
	 * 动力网类型及子类型验证
	 * 
	 * @param iParam
	 * @return
	 */
	public Object powerNEType(List iParam) {
		List blist = (List) iParam.get(0);

		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			Object obj = bObject[0];
			BPowerNE bPowerNE = (BPowerNE) bObject[0];
			// 高压配电设备
			if (obj instanceof BHighShieldScreen) { // 高压隔离屏
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:高压隔离柜（屏）";
				}
			} else if (obj instanceof BHighCoilinScreen) { // 高压进线柜
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:高压进线柜（屏）";
				}
			} else if (obj instanceof BHighMeasureScreen) { // 高压计量屏
															// HighMeasureScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:高压计量柜（屏）";
				}
			} else if (obj instanceof BHighCoiloutScreen) { // 高压出线柜（屏）HighCoiloutScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 4) {
					bObject[0] = "设备子类型应为:高压出线柜（屏）";
				}
			} else if (obj instanceof BHighLiaisonScreen) { // 高压联络柜
															// HighLiaisonScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 5) {
					bObject[0] = "设备子类型应为:高压联络柜（屏）";
				}
			} else if (obj instanceof BHighPressureScreen) { // 高压压变柜
																// HighPressureScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 6) {
					bObject[0] = "设备子类型应为:高压压变柜（屏）";
				}
			} else if (obj instanceof BHighGeneratrixScreen) { // 高压母线柜
																// HighGeneratrixScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 7) {
					bObject[0] = "设备子类型应为:高压母线柜（屏）";
				}
			} else if (obj instanceof BHighRingScreen) { // 高压环网柜 HighRingScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 8) {
					bObject[0] = "设备子类型应为:高压环网柜";
				}
			} else if (obj instanceof BHighSwitchScreen) { // 高压开关柜
															// HighSwitchScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 9) {
					bObject[0] = "设备子类型应为:高压开关柜";
				}
			} else if (obj instanceof BOperatePowerScreen) { // 操作电源屏OperatePowerScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 10) {
					bObject[0] = "设备子类型应为:操作电源屏";
				}
			} else if (obj instanceof BOperateControlScreen) { // 操作控制屏OperateControlScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 11) {
					bObject[0] = "设备子类型应为:操作控制屏";
				}
			} else if (obj instanceof BSignalScreen) { // 信号屏SignalScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 12) {
					bObject[0] = "设备子类型应为:信号屏";
				}
			} else if (obj instanceof BOperateBatScreen) { // 操作电池屏OperateBatScreen
				if (bPowerNE.getParentType() != 1) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 13) {
					bObject[0] = "设备子类型应为:操作电池屏";
				}
			} else if (obj instanceof BHighVoltOthers) { // 其它
				if (bPowerNE.getParentType() != 14) {
					bObject[0] = "设备种类应为:高压配电设备";
				} else if (bPowerNE.getPowerNeType() != 13) {
					bObject[0] = "设备子类型应为:其它";
				}
			}
			// 低压配电设备
			else if (obj instanceof BLowCoilinScreen) { // 低压进线屏 LowCoilinScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:低压进线屏";
				}
			} else if (obj instanceof BLowOutputScreen) { // 低压输出屏
															// LowOutputScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:低压输出屏";
				}
			} else if (obj instanceof BLowCompensationScreen) { // 低压补偿屏
																// LowCompensationScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:低压补偿屏";
				}
			} else if (obj instanceof BLowLiaisonScreen) { // 低压联络屏
															// LowLiaisonScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 4) {
					bObject[0] = "设备子类型应为:低压联络屏";
				}
			} else if (obj instanceof BLowUserScreen) { // 低压用户屏 LowUserScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 5) {
					bObject[0] = "设备子类型应为:低压用户屏";
				}
			} else if (obj instanceof BOilSwitchScreen) { // 油/市切换屏
															// OilSwitchScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 6) {
					bObject[0] = "设备子类型应为:油/市切换屏";
				}
			} else if (obj instanceof BLowDistributionBox) { // 低压配电箱
																// LowDistributionBox
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 7) {
					bObject[0] = "设备子类型应为:低压配电箱";
				}
			} else if (obj instanceof BOliEngineAccessScreen) { // 应急油机接入屏
																// OliEngineAccessScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 8) {
					bObject[0] = "设备子类型应为:应急油机接入屏（箱）";
				}
			} else if (obj instanceof BLowMeasureScreen) { // 低压计量柜LowMeasureScreen
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 9) {
					bObject[0] = "设备子类型应为:低压计量柜";
				}
			} else if (obj instanceof BLowVoltOthers) { // 其它LowVoltOthers
				if (bPowerNE.getParentType() != 2) {
					bObject[0] = "设备种类应为:低压配电设备";
				} else if (bPowerNE.getPowerNeType() != 10) {
					bObject[0] = "设备子类型应为:其它";
				}
			}
			// 油机发电机组
			else if (obj instanceof BOilengineGenerator) { // 油机发电机组
															// OilengineGenerator
				if (bPowerNE.getParentType() != 3) {
					bObject[0] = "设备种类应为:油机发电机组";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:油机发电机组";
				}
			} else if (obj instanceof BControlBoard) { // 外置油机控制屏 ControlBoard
				if (bPowerNE.getParentType() != 3) {
					bObject[0] = "设备种类应为:油机发电机组";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:外置油机控制屏";
				}
			} else if (obj instanceof BBootBattery) { // 启动电池充电机 BootBattery
				if (bPowerNE.getParentType() != 3) {
					bObject[0] = "设备种类应为:油机发电机组";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:启动电池充电机";
				}
			} else if (obj instanceof BTank) { // 储油罐 Tank
				if (bPowerNE.getParentType() != 3) {
					bObject[0] = "设备种类应为:油机发电机组";
				} else if (bPowerNE.getPowerNeType() != 4) {
					bObject[0] = "设备子类型应为:储油罐";
				}
			}
			// 直流配电设备
			else if (obj instanceof BDcDistributionScreen) { // 直流配电屏
																// DcDistributionScreen
				if (bPowerNE.getParentType() != 7) {
					bObject[0] = "设备种类应为:直流配电设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:直流配电屏";
				}
			} else if (obj instanceof BLowFreqNE) { // 相控电源LowFreq
				if (bPowerNE.getParentType() != 7) {
					bObject[0] = "设备种类应为:直流配电设备";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:相控电源";
				}
			} else if (obj instanceof BHighFreqNE) { // 高频开关电源HighFreq
				if (bPowerNE.getParentType() != 7) {
					bObject[0] = "设备种类应为:直流配电设备";
				} else if (bPowerNE.getPowerNeType() != 4) {
					bObject[0] = "设备子类型应为:高频开关电源";
				}
			} else if (obj instanceof BDcDistributionBox) { // DC/DC变换器
															// DCDistributionBox
				if (bPowerNE.getParentType() != 7) {
					bObject[0] = "设备种类应为:直流配电设备";
				} else if (bPowerNE.getPowerNeType() != 5) {
					bObject[0] = "设备子类型应为:DC/DC变换器";
				}
			}
			// 蓄电池
			else if (obj instanceof BStoragebat) { // 蓄电池 Storagebat
				if (bPowerNE.getParentType() != 10) {
					bObject[0] = "设备种类应为:蓄电池";
				}
			}
			// 交流不间断电源（UPS）设备
			else if (obj instanceof BUPSHost) { // UPS主机 UPSHost
				if (bPowerNE.getParentType() != 12) {
					bObject[0] = "设备种类应为:交流不间断电源（UPS）";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:UPS主机";
				}
			} else if (obj instanceof BUPSCabinet) { // UPS并机柜 UPSCabinet
				if (bPowerNE.getParentType() != 12) {
					bObject[0] = "设备种类应为:交流不间断电源（UPS）";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:UPS并机柜";
				}
			} else if (obj instanceof BUpsbat) { // UPS蓄电池 Upsbat
				if (bPowerNE.getParentType() != 12) {
					bObject[0] = "设备种类应为:交流不间断电源（UPS）";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:UPS蓄电池";
				}
			} else if (obj instanceof BUpsDistributionScreen) { // UPS输出屏UPSdistributionScreen
				if (bPowerNE.getParentType() != 12) {
					bObject[0] = "设备种类应为:交流不间断电源（UPS）";
				} else if (bPowerNE.getPowerNeType() != 4) {
					bObject[0] = "设备子类型应为:UPS输出屏";
				}
			} else if (obj instanceof BTransene) { // 逆变器 Transene
				if (bPowerNE.getParentType() != 12) {
					bObject[0] = "设备种类应为:交流不间断电源（UPS）";
				} else if (bPowerNE.getPowerNeType() != 6) {
					bObject[0] = "设备子类型应为:逆变器";
				}
			} else if (obj instanceof BIndirectcur) { // 其他交流供电设备IndirectCur
				if (bPowerNE.getParentType() != 12) {
					bObject[0] = "设备种类应为:交流不间断电源（UPS）";
				} else if (bPowerNE.getPowerNeType() != 7) {
					bObject[0] = "设备子类型应为:其他交流供电设备";
				}
			}
			// 防雷接地设备
			else if (obj instanceof BThunderDef) {
				if (bPowerNE.getParentType() != 17) {
					bObject[0] = "设备种类应为:防雷接地设备";
				}
			}
			// 变压器设备
			else if (obj instanceof BOilimmerTransformer) { // 油浸式变压器
															// OilimmerTransformer
				if (bPowerNE.getParentType() != 25) {
					bObject[0] = "设备种类应为:变压器设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:油浸变压器";
				}
			} else if (obj instanceof BDryTransformer) { // 干式变压器 DryTransformer
				if (bPowerNE.getParentType() != 25) {
					bObject[0] = "设备种类应为:变压器设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:干式变压器";
				}
			} else if (obj instanceof BWYQ) { // 交流稳压器 WYQ
				if (bPowerNE.getParentType() != 25) {
					bObject[0] = "设备种类应为:变压器设备";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:交流稳压器";
				}
			}
			// 交直流组合屏设备
			else if (obj instanceof BAcdcDistributionScreen) { // 交直流组合配电屏
																// AcdcDistributionScreen
				if (bPowerNE.getParentType() != 29) {
					bObject[0] = "设备种类应为:交直流组合屏";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:交直流组合配电屏";
				}
			} else if (obj instanceof BSwitchPowerScreen) { // 组合开关电源屏
															// SwitchPowerScreen
				if (bPowerNE.getParentType() != 29) {
					bObject[0] = "设备种类应为:交直流组合屏";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:组合开关电源屏";
				}
			}
			// 中央空调设备
			else if (obj instanceof BCenterAirconditionerRefrigerator) { // 中央空调制冷机组
																			// CenterAirconditionerRefrigerator
				if (bPowerNE.getParentType() != 14) {
					bObject[0] = "设备种类应为:中央空调设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:中央空调制冷机组";
				}
			} else if (obj instanceof BCoolingTower) { // 冷却塔 CoolingTower
				if (bPowerNE.getParentType() != 14) {
					bObject[0] = "设备种类应为:中央空调设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:冷却塔";
				}
			} else if (obj instanceof BCoolantPump) { // 冷却泵 CoolantPump
				if (bPowerNE.getParentType() != 14) {
					bObject[0] = "设备种类应为:中央空调设备";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:冷却泵";
				}
			} else if (obj instanceof BFreezePump) { // 冷冻泵 FreezePump
				if (bPowerNE.getParentType() != 14) {
					bObject[0] = "设备种类应为:中央空调设备";
				} else if (bPowerNE.getPowerNeType() != 4) {
					bObject[0] = "设备子类型应为:冷冻泵";
				}
			} else if (obj instanceof BAirconditionBox) { // 空调箱 AirconditionBox
				if (bPowerNE.getParentType() != 14) {
					bObject[0] = "设备种类应为:中央空调设备";
				} else if (bPowerNE.getPowerNeType() != 5) {
					bObject[0] = "设备子类型应为:空调箱";
				}
			}

			// 分体空调设备
			else if (obj instanceof BOnetooneSinairconditioner) { // 分体一对一空调
																	// OnetooneSinairconditioner
				if (bPowerNE.getParentType() != 16) {
					bObject[0] = "设备种类应为:分体空调设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:分体一对一空调";
				}
			} else if (obj instanceof BOnetomanySinairconditioner) { // 分体一对多空调
																		// OnetomanySinairconditioner
				if (bPowerNE.getParentType() != 16) {
					bObject[0] = "设备种类应为:分体空调设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:分体一对多空调";
				}
			} else if (obj instanceof BWindowAirconditioner) { // 窗式空调
																// WindowAirconditioner
				if (bPowerNE.getParentType() != 16) {
					bObject[0] = "设备种类应为:分体空调设备";
				} else if (bPowerNE.getPowerNeType() != 3) {
					bObject[0] = "设备子类型应为:窗式空调";
				}
			}

			// 专用空调设备
			else if (obj instanceof BIndoorEngine) { // 室内机 IndoorEngine
				if (bPowerNE.getParentType() != 15) {
					bObject[0] = "设备种类应为:专用空调设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:分体一对一空调";
				}
			} else if (obj instanceof BOutdoorEngine) { // 室外机 OutdoorEngine
				if (bPowerNE.getParentType() != 15) {
					bObject[0] = "设备种类应为:专用空调设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:分体一对多空调";
				}
			}

			// 动力监控设备
			else if (obj instanceof BCollectionStation) { // 采集器
															// CollectionStation
				if (bPowerNE.getParentType() != 18) {
					bObject[0] = "设备种类应为:动力监控设备";
				} else if (bPowerNE.getPowerNeType() != 2) {
					bObject[0] = "设备子类型应为:采集器";
				}
			} else if (obj instanceof BControllerEngine) { // 工控机
															// ControllerEngine
				if (bPowerNE.getParentType() != 18) {
					bObject[0] = "设备种类应为:动力监控设备";
				} else if (bPowerNE.getPowerNeType() != 1) {
					bObject[0] = "设备子类型应为:工控机";
				}
			}
			// 母线铜排
			else if (obj instanceof BGeneratrixCopper) { // 母线铜排GeneratrixCopper
				if (bPowerNE.getParentType() != 28) {
					bObject[0] = "设备种类应为:母线铜排";
				}
			}
			// 告警模块
			else if (obj instanceof BGuardModule) { // 告警模块GuardModule
				if (bPowerNE.getParentType() != 26) {
					bObject[0] = "设备种类应为:告警模块";
				}
			}
			// 机架电源柜
			else if (obj instanceof BRackPdr) { // 机架电源柜RackPdr
				if (bPowerNE.getParentType() != 20) {
					bObject[0] = "设备种类应为:机架电源柜";
				}
			}
			// 列头柜设备
			else if (obj instanceof BPdr) { // 列头柜设备Pdr
				if (obj instanceof BDcCabinet) { 
					if (bPowerNE.getParentType() != 19) {
						bObject[0] = "设备种类应为:直流配电设备";
					} else if (bPowerNE.getPowerNeType() != 1) {
						bObject[0] = "设备子类型应为:直流列柜";
					}
				}else if(obj instanceof BAcCabinet){
					if (bPowerNE.getParentType() != 19) {
						bObject[0] = "设备种类应为:列头柜设备";
					}else if (bPowerNE.getPowerNeType() != 2) {
						bObject[0] = "设备子类型应为:交流列柜";
					}
				}
			}
			else if (obj instanceof BACdistributionScreen) { // 交流配电柜（箱）ACdistributionScreen
				if (bPowerNE.getParentType() != 30) {
					bObject[0] = "设备种类应为:交流配电柜（箱）";
				}
			} 
			// 灭火器设备
			else if (obj instanceof BFireequ) { // 灭火器设备Fireequ
				if (bPowerNE.getParentType() != 21) {
					bObject[0] = "设备种类应为:灭火器设备";
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
	public Object powerNECheckByRoom(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			if (!(bObject[0] instanceof String)) {
				BPowerNE bObj = (BPowerNE) bObject[0];
				Object oldObj = null;
				if (bObj.getId() > 0) {
					oldObj = getBObject(bObj, "id", String
							.valueOf(bObj.getId()), "PowerNEView[Host]");
				} else {
					oldObj = bObj;
				}
				if (oldObj != null) {
					BPowerNE bPowerNE = (BPowerNE) oldObj;
					if (bPowerNE.getHost() instanceof BRoom) {
						BRoom room = (BRoom) bPowerNE.getHost();
						if (targetObject.equals("depth")) {
							if (bObj.getDepth() > room.getWidth()) {
								bObject[0] = "设备深度超过机房的宽度";
							}
						} else if (targetObject.equals("width")) {
							if (bObj.getWidth() > room.getLength()) {
								bObject[0] = "设备宽度超过机房的长度";
							}
						} else if (targetObject.equals("height")) {
							if (bObj.getHeight() > room.getHeight()) {
								bObject[0] = "设备高度超过机房的高度";
							}
						}
					}
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 * 动力设备固定资产
	 * @param iParam
	 * @return
	 */
	public Object powerNEAssetCheck(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();

		Property property = new Property();
		property.setBclass(BPowerNEAsset.class.getName());
		addPropertyValue(property, "costCenter");
		addPropertyValue(property, "status");
		addPropertyValue(property, "specFormula");
		addPropertyValue(property, "entityName");
		addPropertyValue(property, "site");
		addPropertyValue(property, "projectNo");
		addPropertyObj(property, "project", "projectId","gxlu.ossc.inventory.basic.common.bizobject.project.BProject");
		ContainerFactory.addProperty(property);
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			if (!(bObject[0] instanceof String)) {
				BPowerNE bPowerNE = (BPowerNE) bObject[0];
				BPowerNEAsset asset = null;
//				if (bPowerNE.getId() <= 0) {
//					asset = new BPowerNEAsset();
//					asset.setCostCenter(PropertyConstvalue.ASSRT_DEFAULT_COSTCENTER);
//					asset.setStatus(PropertyConstvalue.ASSRT_DEFAULT_STATUS);
//				} else {
//					BPowerNE oldBPowerNE = (BPowerNE) getBObject(bPowerNE,"id", String.valueOf(bPowerNE.getId()),"PowerNE[PowerNEAsset]");
//					asset = oldBPowerNE.getPowerNEAsset();
//				}
				if(bPowerNE.getId()>0){
					BPowerNE oldBPowerNE = (BPowerNE) getBObject(bPowerNE,"id", String.valueOf(bPowerNE.getId()),"PowerNE[PowerNEAsset]");
					asset = oldBPowerNE.getPowerNEAsset();
				}
				if(asset==null){
					asset = new BPowerNEAsset();
					asset.setCostCenter(PropertyConstvalue.ASSRT_DEFAULT_COSTCENTER);
					asset.setStatus(PropertyConstvalue.ASSRT_DEFAULT_STATUS);
				}

				String SpecFormula = FieldDictionary.getFieldDesc("PNPOWERNE","TYPE", (byte) bPowerNE.getParentType());
				asset.setSpecFormula(SpecFormula);
				asset.setEntityName(bPowerNE.getName());
				if (bPowerNE.getHost() != null)
					asset.setSite(bPowerNE.getHost().getName());

				if (asset.getId() >0)
					ContainerFactory.addobjectListMap(bPowerNE, VariableNames.OBJECT_FRONT,	VariableNames.OBJECT_UPDATE, asset);
				else
					ContainerFactory.addobjectListMap(bPowerNE, VariableNames.OBJECT_FRONT,VariableNames.OBJECT_INSERT, asset);

				Map map = new HashMap();
				map.put("setPowerNEAsset", asset);
				ContainerFactory.addobjectListMap(bObject[0], VariableNames.OBJECT_SELF,VariableNames.OBJECT_CONVERTOR, map);
				bPowerNE.setPowerNEAsset(asset);
			}
			dataList.add(bObject);
		}
		return dataList;
	}

	public Object dnNE_Relation(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();

		Property property = new Property();
		property.setBclass(BDNDeviceAsset.class.getName());
		addPropertyValue(property, "costCenter");
		addPropertyValue(property, "status");
		addPropertyValue(property, "site");
		addPropertyValue(property, "entityName");
		addPropertyValue(property, "assetCategoryNo");
		addPropertyValue(property, "assetName");		
		addPropertyObj(property,"assetCategory","assetCategoryId","gxlu.ossc.inventory.basic.common.bizobject.asset.BAssetCategory");
		ContainerFactory.addProperty(property);
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			if (!(bObject[0] instanceof String)) {
				BDNDevice bDNDevice = (BDNDevice) bObject[0];
				BDNDeviceAsset bDNDEviceAsset = null;
				
//				if (bDNDevice.getId() <= 0) {
//					bDNDEviceAsset = new BDNDeviceAsset();
//					bDNDEviceAsset.setCostCenter(PropertyConstvalue.ASSRT_DEFAULT_COSTCENTER);
//					bDNDEviceAsset.setStatus(PropertyConstvalue.ASSRT_DEFAULT_STATUS);
//				} else {
//					bDNDEviceAsset = (BDNDeviceAsset) getBObject(BDNDeviceAsset.class, "id", String.valueOf(bDNDevice.getDNDeviceAssetId()),null);
//				}
				
				if(bDNDevice.getId()>0){
					Object asset =  getBObject(BDNDeviceAsset.class, "id", String.valueOf(bDNDevice.getDNDeviceAssetId()),null);
					if(asset!=null){
						bDNDEviceAsset=(BDNDeviceAsset)asset;
					}
				}
				if (bDNDEviceAsset ==null) {
					bDNDEviceAsset = new BDNDeviceAsset();
					bDNDEviceAsset.setCostCenter(PropertyConstvalue.ASSRT_DEFAULT_COSTCENTER);
					bDNDEviceAsset.setStatus(PropertyConstvalue.ASSRT_DEFAULT_STATUS);
				} 
				
				if (bDNDevice.getHost() != null){
					bDNDEviceAsset.setSite(bDNDevice.getHost().getName());
					bDNDEviceAsset.setEntityName(bDNDevice.getHost().getName()+"/"+bDNDevice.getShortName());
				}
                else{
                	bDNDEviceAsset.setEntityName(bDNDevice.getShortName());
                }
				BAssetCategory ac = new BAssetCategory();
				QueryExpr exp = new QueryExprBuilder().get("id").equal(bDNDevice.getDNDeviceTypeId());
			    BDNDeviceType type = (BDNDeviceType)BQueryClient.getQueryResult(exp,BDNDeviceType.class,"NDNeviceType[AssetCategory]");
		        if(type != null && type.getAssetCategory() != null)
		          ac = type.getAssetCategory();
		        else
		          ac = getAssetCategoryByRule(bDNDevice);
		        if(ac != null){
		        	bDNDEviceAsset.setAssetCategory(ac);
		        	bDNDEviceAsset.setAssetCategoryNo(ac.getAssembleCode());
		        	bDNDEviceAsset.setAssetName(ac.getAssembleName());
		        }
				if (bDNDEviceAsset.getId() >0)
					ContainerFactory.addobjectListMap(bDNDevice, VariableNames.OBJECT_FRONT,VariableNames.OBJECT_UPDATE, bDNDEviceAsset);
				else
					ContainerFactory.addobjectListMap(bDNDevice, VariableNames.OBJECT_FRONT,VariableNames.OBJECT_INSERT, bDNDEviceAsset);

				bDNDevice.setDNDeviceAsset(bDNDEviceAsset);
				Map map = new HashMap();
				map.put("setDNDeviceAsset", bDNDEviceAsset);
				ContainerFactory.addobjectListMap(bObject[0], VariableNames.OBJECT_SELF,VariableNames.OBJECT_CONVERTOR, map);
				dn_createAudit(bDNDevice);
				if ((bDNDevice.getIsTemplate() == 0)&& (bDNDevice.getRoles() != null)&& (hasPE(bDNDevice.getRoles()))){
					if (bDNDevice.getId() > 0) {
						QueryExprBuilder builder = new QueryExprBuilder();
						QueryExpr expr = builder.get("entityId").equal(bDNDevice.getId());
						expr = expr.and(builder.get("entityType").equal(1));
						expr = expr.and(builder.get("mapId").equal(29));
						Vector vec = getResultByQueryExpr(new BMapNode(), expr,null);
						if ((vec != null) && (vec.size() > 0)){
							dn_updateMapNode(bDNDevice);
						}else{
							dn_createMapNode(bDNDevice);
						}
					} else {
						dn_createMapNode(bDNDevice);
					}
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	 public BAssetCategory getAssetCategoryByRule(BDNDevice _cm){
		 String str = SysConfig.GetOptionItemValue(SysConfigConst.SYSCONFIG_CAPITALASSERTS_INTOMARKET);
		 byte market=(byte)0;
         if(str != null && !str.equals("")){        	 
             market = Byte.valueOf(str).byteValue();
         }
	        if(_cm == null)
	            return null;
	        else{
	          if(_cm instanceof BATM)
	            return loadCategory("04","05",market);
	        else if(_cm instanceof BDDN)
	            return loadCategory("04","03",market);
	        else if(_cm instanceof BFR)
	            return loadCategory("04","04",market);
	          else
	            return loadCategory("04",market);
	        }
	    }
	 /**
	     * 根据类编号、项编号查找相应的项目录
	     * @param _classCode 类编号
	     * @param _itemCode 项编号
	     * @return
	     */
	    public  BAssetCategory loadCategory(String _classCode,String _itemCode,byte market){
	        QueryExprBuilder eb = new QueryExprBuilder();
	        QueryExpr exp = eb.get("parentAssetCategory").get("code").equal(_classCode)
	            .and(eb.get("code").equal(_itemCode))
	            .and(eb.get("level").equal(PropertyConstvalue.ASSETCATEGORY_CATEGORYLEVEL_ITEM))
	            .and(eb.get("comeIntoMarket").equal(market));
	        return (BAssetCategory)BQueryClient.getQueryResult(exp,        BAssetCategory.class,null);
	    }
	    /**
	      * 根据类编号查找相应的项目录
	      * @param _classCode 类编号
	      * @return
	      */
	     public  BAssetCategory loadCategory(String _classCode,byte market){
	         QueryExprBuilder eb = new QueryExprBuilder();
	         QueryExpr exp = eb.get("code").equal(_classCode)
	         .and(eb.get("level").equal(PropertyConstvalue.ASSETCATEGORY_CATEGORYLEVEL_CLASS))
	         .and(eb.get("comeIntoMarket").equal(market));
	         return (BAssetCategory)BQueryClient.getQueryResult(exp,
	             BAssetCategory.class,null);
	     }

	/**
	 * 数据网设备创建审计
	 * @param bDNDevice
	 */
	private void dn_createAudit(BDNDevice bDNDevice) {
		BDNDeviceAudit audit = new BDNDeviceAudit();
		Map map = new HashMap();
		Map vMap = new HashMap();
		Map objMap = new HashMap();
		objMap.put(bDNDevice, "getId");
		vMap.put("setEntityId", objMap);
		vMap.put("setDnDeviceName", bDNDevice.getShortName());
		vMap.put("setDnDeviceCode", bDNDevice.getShortCode());
		BHost bHost=bDNDevice.getHost();
		if(bHost==null&&bDNDevice.getHostId()>0){
			Object objHost=getBObject(BHost.class, "id", bDNDevice.getHostId()+"","Host[Region]");
			if(objHost!=null){
				bHost=(BHost)objHost;
			}
		}
		if (bHost instanceof BRoom) {
			vMap.put("setRoom", bHost.getName());
			String region="";
			if (bHost.getRegion() != null){
				region=bHost.getRegion().getNameCN();
			}else if(bHost.getRegion()==null&&bHost.getRegionId()>0){
				Object objRegion=getBObject(BRegion.class, "id", bHost.getRegionId()+"",null);
				if(objRegion!=null){
					region=((BRegion)objRegion).getNameCN();
				}
			}
			vMap.put("setRegion",region);
			String site="";
			if (((BRoom)bHost).getSite() != null){
				site=((BRoom)bHost).getSite().getName();
			}else if(((BRoom)bHost).getSite() == null&&((BRoom)bHost).getSiteId()>0){
				Object objSite=getBObject(BSite.class, "id", ((BRoom)bHost).getSiteId()+"",null);
				if(objSite!=null){
					site=((BSite)objSite).getNameCN();
				}
			}
			vMap.put("setSite", site);
		}
		vMap.put("setSerialNo", String.valueOf(bDNDevice.getSerialNo()));
		String dndeviceNameMonikerObj = getDndeviceNameMonikerObj(bDNDevice,"拼装名称-系统缺省");
		if ((dndeviceNameMonikerObj != null)&& (!(dndeviceNameMonikerObj.trim().equals(""))))
			vMap.put("setAfterModify", dndeviceNameMonikerObj);
		else
			vMap.put("setAfterModify", bDNDevice.toString());
		if (bDNDevice.getId() > 0){
			vMap.put("setOperationType",getOperationType(PropertyConstvalue.PERSIST_TYPE_UPDATE));
			Object objDNDevice=this.getBObject(bDNDevice, "id",bDNDevice.getId()+"",null);
			if(objDNDevice!=null){
				String beforeModifyString="";
				BDNDevice oldDNDevice=(BDNDevice)objDNDevice;
				 if(oldDNDevice.getNameMoniker() == null || oldDNDevice.getNameMoniker().equals(""))
			            beforeModifyString = oldDNDevice.toString();
			        else
			            beforeModifyString = oldDNDevice.getNameMoniker();
				vMap.put("setBeforModify", beforeModifyString);
			}
		}else{
			vMap.put("setOperationType", getOperationType(PropertyConstvalue.PERSIST_TYPE_ADD));
		}

		vMap.put("setIdInEMS", bDNDevice.getIdInEMS());
		BOperator bOperator = CommonClientEnvironment.getOperator();
		if (bOperator != null) {
			vMap.put("setOperator", bOperator.getName());
			vMap.put("setOrganizationName", getOrgNameOfOperator(bOperator,false));
		}
		vMap.put("setOperationTime", new Date());
		map.put(audit, vMap);
		ContainerFactory.addobjectListMap(bDNDevice, VariableNames.OBJECT_BEHIND, VariableNames.OBJECT_INSERT, map);
	}

	private String getOrgNameOfOperator(BOperator bOperator,boolean	flag) {
		if (bOperator == null)
			return "";
		ExpressionBuilder builder = new ExpressionBuilder();
		BOperator dOperator = (BOperator) getBObject(BOperator.class, "id",String.valueOf(bOperator.getId()), "Operator[Organization]");
		if ((dOperator == null) || (dOperator.getOrganization() == null))
			return "";
		if(flag){
			return dOperator.getOrganization().toString();
		}else{
			return dOperator.getOrganization().getName();
		}
	}

	private void dn_createMapNode(BDNDevice bDNDevice) {
		Map nodeMap = new HashMap();
		Map nodePMap = new HashMap();
		BMapNode mapnode = new BMapNode();
		nodePMap.put("setXPosition", Double.valueOf(100.0D));
		nodePMap.put("setYPosition", Double.valueOf(100.0D));
		nodePMap.put("setLabelPosition", Integer.valueOf(1));
		nodePMap.put("setMap", (BMap) getBObject(BMap.class, "id", String.valueOf(29L), null));
		nodePMap.put("setLabel", bDNDevice.getShortName());
		Map nodeVMap = new HashMap();
		nodeVMap.put(bDNDevice, "getId");
		nodePMap.put("setEntityId", nodeVMap);
		nodePMap.put("setEntityType", "PE");
		nodePMap.put("setType", "MAPNODETYPE-PE");
		BRegion region = null;
		ExpressionBuilder builder = new ExpressionBuilder();
		if (bDNDevice != null) {
			BHost host = bDNDevice.getHost();
			if (host != null)
				region = (BRegion) getBObject(BRegion.class, "id", String.valueOf(host.getRegionId()), null);
		}

		if (region != null)
			nodePMap.put("setRegionId", Long.valueOf(region.getId()));
		else
			nodePMap.put("setRegionId", Integer.valueOf(-1));

		nodePMap.put("setsubEntityType", Byte.parseByte("-1"));
		nodePMap.put("setTopoFilter", null);
		nodeMap.put("gxlu.afx.system.topo.service.dataobject.MapNode",nodePMap);

		List list = new ArrayList();
		list.add("gxlu.afx.system.topo.service.BTopoMapServerUtil");
		list.add("eventOperation");
		list.add(new Object[] { "gxlu.afx.system.common.CommonContext","java.lang.Object",int.class, byte.class });
		list.add(new Object[] { null, nodeMap, Integer.valueOf(1), Byte.parseByte("2") });
		ContainerFactory.addobjectListMap(bDNDevice, VariableNames.OBJECT_BEHIND, VariableNames.OBJECT_OLDMETHOD,list);
	}
	private void dn_updateMapNode(BDNDevice bDNDevice) {
		QueryExprBuilder builder1 = new QueryExprBuilder();
		Vector nodeList = getResultByQueryExpr(BMapNode.class, builder1.get("entityId").equal(bDNDevice.getId()).and(builder1.get("entityType").equal("PE")), null);
		for (int i = 0; i < nodeList.size(); ++i) {
			BMapNode mapNode = (BMapNode) nodeList.elementAt(i);
			mapNode.setLabel(bDNDevice.getShortName());

			Map nodeMap = new HashMap();
			Map nodePMap = new HashMap();
			nodePMap.put("setLabel", bDNDevice.getShortName());
			nodeMap.put(mapNode, nodePMap);
			List list = new ArrayList();
			list.add("gxlu.afx.system.topo.service.BTopoMapServerUtil");
			list.add("eventOperation");
			list.add(new Object[] { "gxlu.afx.system.common.CommonContext","java.lang.Object",int.class, byte.class });
			list.add(new Object[] { null, mapNode, Integer.valueOf(1), Byte.parseByte("2") });
			ContainerFactory.addobjectListMap(bDNDevice, VariableNames.OBJECT_BEHIND, VariableNames.OBJECT_OLDMETHOD,list);
		}
	}

	private boolean hasPE(String str) {
		if ((str == null) || (str.length() < 1))
			return false;
		char[] values = str.toCharArray();
		String dic = String.valueOf(1);
		for (int i = 0; i < values.length; ++i)
			if (String.valueOf(values[i]).equals(dic))
				return true;

		return false;
	}

	private String getDndeviceNameMonikerObj(BDNDevice bDNDevice,String appContext) {
		if (appContext != "拼装名称-系统缺省")
			return null;
		byte category = bDNDevice.getCategory();
		HashMap hm = new HashMap();
		hm.put(new Integer(5), "R");
		hm.put(new Integer(6), "S");
		hm.put(new Integer(7), "F");
		hm.put(new Integer(8), "H");
		hm.put(new Integer(11), "K");
		hm.put(new Integer(12), "Z");
		hm.put(new Integer(4), "L");
		hm.put(new Integer(3), "N");
		hm.put(new Integer(1), "A");
		hm.put(new Integer(2), "D");
		hm.put(new Integer(9), "G");
		hm.put(new Integer(10), "M");
		hm.put(new Integer(14), "M");
		hm.put(new Integer(13), "Q");
		hm.put(new Integer(15), "Q");

		String arg0 = null;
		String arg1 = null;
		String arg2 = null;
		try {
			arg0 = (String) hm.get(new Integer(category));
			arg1 = bDNDevice.getDNDeviceType().toString();
			arg2 = "" + bDNDevice.getSerialNo();
		} catch (Exception e) {
			return null;
		}

		if ((arg0 == null) || (arg1 == null))
			return null;

		if (("".equals(arg0)) || ("".equals(arg1)))
			return null;

		if ("".equals(arg2))
			arg2 = "null";

		String partition = "-";

		return arg0 + partition + arg1 + partition + arg2;
	}
	/**
	 * 如果是导入新增的话创建MapLine
	 * @param iParam
	 * @return
	 */
	public Object mn_CreateMapLine(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);

		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		int i = 0;
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			if (bObject[0] instanceof String) {
				dataList.add(bObject);
			} else {
				BMNTrunk bMNTrunk = (BMNTrunk) bObject[0];

				if ((bMNTrunk.getId() <= 0)&& (bMNTrunk.getZMNNe() != null)) {
					Map map = new HashMap();
					Map pMap = new HashMap();
					Map vMap = new HashMap();
					vMap.put(bMNTrunk, "getId");
					pMap.put("setEntityId", vMap);
					pMap.put("setType", "MAPLINETYPE-MNCONMMONLINE");
					pMap.put("setEntityType", "MNTrunk");
					pMap.put("setAEntityId", Long.valueOf(bMNTrunk.getAMNNeId()));
					pMap.put("setZEntityId", Long.valueOf(bMNTrunk.getZMNNeId()));
					pMap.put("setAEntityType", "MNNE");
					pMap.put("setZEntityType", "MNNE");
					map.put("gxlu.afx.system.topo.common.MapLineData", pMap);

					List list = new ArrayList();
					list.add(getLocalIFC("TopoMapLocalIFC"));
					list.add("createMapLine");
					list.add(new Object[] {"gxlu.afx.system.common.CommonContext","gxlu.afx.system.topo.common.MapLineData",String.class });
					list.add(new Object[] { 0, map, "MNNEMAPTRUNKLINESERVER" });
					ContainerFactory.addobjectListMap(bObject[0], "BEHIND","OLDMETHOD", list);
				}
				this.mn_createMNTrunkAudit(bMNTrunk);
				dataList.add(bObject);
			}
		}
		return dataList;
	}
	/**
	 * 中继电路审计
	 * @param bMNTrunk
	 */
	private void mn_createMNTrunkAudit(BMNTrunk bMNTrunk) {
		BMNTrunkAudits audits = new BMNTrunkAudits();
		Map map = new HashMap();
		Map vMap = new HashMap();
		Map objMap = new HashMap();
		objMap.put(bMNTrunk, "getId");
		vMap.put("setEntityId", objMap);
		String aMNNEName="";
		if(bMNTrunk.getAMNNe()!=null&&bMNTrunk.getAMNNe().getMonikerName()!=null){
			aMNNEName=bMNTrunk.getAMNNe().getMonikerName();
		}else if(bMNTrunk.getAMNNe()==null&&bMNTrunk.getAMNNeId()>0){
			Object objAMNNE=getBObject(BMNNE.class, "id", bMNTrunk.getAMNNeId()+"",null);
			if(objAMNNE!=null){
				aMNNEName=((BMNNE)objAMNNE).getMonikerName();
			}
		}
		vMap.put("setAMNNeName",aMNNEName!=null?aMNNEName:"");
		
		String aMNPortName="";
		if(bMNTrunk.getAMNPort()!=null&&bMNTrunk.getAMNPort().getMonikerName()!=null){
			aMNPortName=bMNTrunk.getAMNPort().getMonikerName();
		}else if(bMNTrunk.getAMNPort()==null&&bMNTrunk.getAMNPortId()>0){
			Object objAMNPort=getBObject(BMNPort.class, "id", bMNTrunk.getAMNPortId()+"",null);
			if(objAMNPort!=null){
				aMNPortName=((BMNPort)objAMNPort).getMonikerName();
			}
		}
		vMap.put("setAMNPortName", aMNPortName!=null?aMNPortName:"");
		
		String zMNNeName="";
		if(bMNTrunk.getZMNNe()!=null&&bMNTrunk.getZMNNe().getMonikerName()!=null){
			zMNNeName=bMNTrunk.getZMNNe().getMonikerName();
		}else if(bMNTrunk.getZMNNe()==null&&bMNTrunk.getZMNNeId()>0){
			Object objZMNME=getBObject(BMNNE.class, "id", bMNTrunk.getZMNNeId()+"",null);
			if(objZMNME!=null){
				zMNNeName=((BMNNE)objZMNME).getMonikerName();
			}
		}
		vMap.put("setZMNNeName", zMNNeName!=null?zMNNeName:"");
		
		String zMNPortName="";
		if(bMNTrunk.getZMNPort()!=null&&bMNTrunk.getZMNPort().getMonikerName()!=null){
			zMNPortName=bMNTrunk.getZMNPort().getMonikerName();
		}else if(bMNTrunk.getZMNPort()==null&&bMNTrunk.getZMNPortId()>0){
			Object objZMNPort=getBObject(BMNPort.class, "id", bMNTrunk.getZMNPortId()+"",null);
			if(objZMNPort!=null){
				zMNPortName=((BMNPort)objZMNPort).getMonikerName();
			}
		}
		vMap.put("setZMNPortName",zMNPortName!=null?zMNPortName:"");
		
		if (bMNTrunk.getId() > 0){
			vMap.put("setOperationType",PropertyConstvalue.MNGLOBAL_AUDITSOPERATIONTYPE_MODIFY);
			Object oldObj=this.getBObject(bMNTrunk, "id",bMNTrunk.getId()+"", "MNTrunk[AMNNe][ZMNNe][AMNPort][ZMNPort]");
			if(oldObj!=null){
				try {
					vMap.put("setBeforModify",getObjInfo((BMNTrunk)oldObj));
				} catch (SDHException e) {
					e.printStackTrace();
				}
			}
		}else{
			vMap.put("setOperationType", PropertyConstvalue.MNGLOBAL_AUDITSOPERATIONTYPE_NEW);
		}
		
		try {
			vMap.put("setAfterModify",getObjInfo(bMNTrunk));
		} catch (SDHException e) {
			e.printStackTrace();
		}
		BOperator bOperator = CommonClientEnvironment.getOperator();
		if(bOperator!=null){
			vMap.put("setIpAddress", bOperator.getIPAddress());
			vMap.put("setOrganization", getOrgNameOfOperator(bOperator,true));
			vMap.put("setOperator", bOperator.getName());
		}
		vMap.put("setOperationTime", new Date());
		map.put(audits, vMap);
		ContainerFactory.addobjectListMap(bMNTrunk, VariableNames.OBJECT_BEHIND, VariableNames.OBJECT_INSERT, map);
	}
	
	protected String getObjInfo(Object _bObj) throws SDHException
	{
		BMNTrunk bObj = (BMNTrunk)_bObj;
        StringBuffer strBuf = new StringBuffer();
        if(bObj==null)
        	return "";
        //中继电路名称、中继电路编码、中继类型、中继速率、开通时间、业务状态、承载路由、所属工程、本端设备、本端端口、对端设备、对端端口

        strBuf.append(bObj.getName());
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        strBuf.append(bObj.getNo());
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        strBuf.append(SysDictionaryFactory.getSysDictionaryValueCN("MNTRUNK","TYPE",(byte)bObj.getType()));
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        strBuf.append(SysDictionaryFactory.getSysDictionaryValueCN("MNTRUNK","RATE",(byte)bObj.getRate()));
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getFinishTimeLimit()!=null)
        	strBuf.append(bObj.getFinishTimeLimit().toLocaleString());
        else
        	strBuf.append("");
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        strBuf.append(SysDictionaryFactory.getSysDictionaryValueCN("MNGLOBAL","SERVICESTATUS",(byte)bObj.getServiceStatus()));
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getRouteNo()!=null)
        	strBuf.append(bObj.getRouteNo());
        else
        	strBuf.append("");
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getProject()!=null){
        	strBuf.append(bObj.getProject().getNo());
        }else if(bObj.getProject()==null&&bObj.getProjectId()>0){
        	Object objProject=this.getBObject(BProject.class, "id", bObj.getProjectId()+"", null);
        	strBuf.append(objProject==null?"":((BProject)objProject).getNo());
        }else{
        	strBuf.append("");
        }
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getAMNNe()!=null){
        	strBuf.append(bObj.getAMNNe().getMonikerName());
        }else if(bObj.getAMNNe()==null&&bObj.getAMNNeId()>0){
        	Object objAMNNe=this.getBObject(BMNNE.class, "id", bObj.getAMNNeId()+"", null);
        	strBuf.append(objAMNNe==null?"":((BMNNE)objAMNNe).getMonikerName());
        }else{
        	strBuf.append("");
        }
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getAMNPort()!=null){
        	strBuf.append(bObj.getAMNPort().getMonikerName());
        }else if(bObj.getAMNPort()==null&&bObj.getAMNPortId()>0){
        	Object objAMNPort=this.getBObject(BMNPort.class, "id", bObj.getAMNPortId()+"", null);
        	strBuf.append(objAMNPort==null?"":((BMNPort)objAMNPort).getMonikerName());
        }else{
        	strBuf.append("");
        }
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getZMNNe()!=null){
        	strBuf.append(bObj.getZMNNe().getMonikerName());
        }else if(bObj.getZMNNe()==null&&bObj.getZMNNeId()>0){
        	Object objZMNNe=this.getBObject(BMNNE.class, "id", bObj.getZMNNeId()+"", null);
        	strBuf.append(objZMNNe==null?"":((BMNNE)objZMNNe).getMonikerName());
        }else{
        	strBuf.append("");
        }
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);

        if(bObj.getZMNPort()!=null){
        	strBuf.append(bObj.getZMNPort().getMonikerName());
        }else if(bObj.getZMNPort()==null&&bObj.getZMNPortId()>0){
        	Object objZMNPort=this.getBObject(BMNPort.class, "id", bObj.getZMNPortId()+"", null);
        	strBuf.append(objZMNPort==null?"":((BMNPort)objZMNPort).getMonikerName());
        }else{
        	strBuf.append("");
        }
        strBuf.append(PropertyConstvalue.Audit_DELIMITER_COL);
        return strBuf.toString();
	}	
	
	/**
	 * 验证端口是是小区设备上的端口
	 * @param iParam
	 * @return
	 */
	public Object dn_BInformationPoint_DNPortCheck(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			Object obj = bObject[0];
			if(!(obj instanceof String)){
				BInformationPoint bInformationPoint = (BInformationPoint) bObject[0];
				// 获得目标属性值
				Object value = null;
				try {
					value = getFieldValue(bObject[0], targetObject);
				} catch (ElementsException e) {
					e.printStackTrace();
				}
				if(value!=null){
					BDNPort bDNPort=(BDNPort)value;
					//判断是否是小区内的端口
					Vector vec=null;
					Object objBDistrict=getResult(BDistrict.class, "id",""+bInformationPoint.getDistrictId(),"District[DNDevice]");
					if(objBDistrict!=null){
						BDistrict bDistrict=(BDistrict)objBDistrict;
						vec=bDistrict.getDNDevices();
					}
					if(vec==null||vec.size()<=0){
						bObject[0]="小区中不存在设备，所以不存在端口!";
					}else{
						boolean flag=true;
						for(int i=0;i<vec.size();i++){
							BDNDevice bDNDevice=(BDNDevice)vec.get(i);
							System.out.println(bDNDevice.getId());
							if(bDNPort.getDNDeviceId()==bDNDevice.getId()){
								flag=false;
								break;
							}
						}
						if(flag){
							bObject[0]="端口:"+bDNPort.getId()+"不属于小区设备上的端口!";
						}
					}
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	/**
	 * 验证位置类型和相应的
	 * @param iParam
	 * @return
	 */
	public Object dn_BInformationPoint_LocationTypeCheck(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			Object obj = bObject[0];
			Object value=null;
			try {
				value = getFieldValue(bObject[0], targetObject);
			} catch (ElementsException e) {
				e.printStackTrace();
			}
			if(value!=null){
				BInformationPoint bInformationPoint = (BInformationPoint) bObject[0];
				byte locationType=Byte.parseByte(value.toString());
				//验证依赖的不能为空
				switch(locationType){
					case 1:
						//验证房间
						if(bInformationPoint.getApartment()==null){
							bObject[0]="位置类型为房间时小区,大楼,楼层,房间都不能为空";
							locationType=-1;
						}
					case 2:
						//验证楼层
						if(bInformationPoint.getFloor()==null){
							bObject[0]="位置类型为房间时小区,大楼,楼层都不能为空";
							locationType=-1;
						}
					case 3:
						//验证大楼
						if(bInformationPoint.getBuilding()==null){
							bObject[0]="位置类型为房间时小区,大楼都不能为空";
							locationType=-1;
						}
					case 4:
						//验证小区
						if(bInformationPoint.getDistrict()==null){
							bObject[0]="位置类型为房间时小区不能为空";
							locationType=-1;
						}
						break;
				}
				//验证依赖关系
				switch(locationType){
					case 1:
						//验证房间
						if(bInformationPoint.getApartment().getFloorId()!=bInformationPoint.getFloorId()){
							bObject[0]="房间必须是\""+bInformationPoint.getFloor().getName()+"\"楼层内的房间!";
						}
					case 2:
						//验证楼层
						if(bInformationPoint.getFloor().getBuildingId()!=bInformationPoint.getBuildingId()){
							bObject[0]="楼层必须是\""+bInformationPoint.getBuilding().getName()+"\"大楼内的楼层!";
						}
					case 3:
						//验证大楼
						if(bInformationPoint.getBuilding().getDistrictId()!=bInformationPoint.getDistrictId()){
							bObject[0]="大楼必须是\""+bInformationPoint.getDistrict().getName()+"\"小区内的大楼!";
						}
						break;
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 * 数据网中 WLAN 热点验证省市区的关系
	 * @param iParam
	 * @return
	 */
	public Object regionCheckForRegionObj(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			Object pObj=null;
			Object cObj=null;
			Object dObj=null;
			try {
				pObj=getFieldValue(bObject[0], "province");
				cObj=getFieldValue(bObject[0], "city");
				dObj=getFieldValue(bObject[0], "district");
			} catch (ElementsException e) {
				e.printStackTrace();
			}
			//验证城市和省份的关系
			if(cObj!=null){
				if(((BRegion)cObj).getParentRegionId()>0&&pObj==null){
					bObject[0]="请输入城市所在的省份";
				}else{
					if(pObj!=null){
						if(((BRegion)cObj).getParentRegionId()!=((BRegion)pObj).getId()){
							//避免不同省份下城市同名
							Vector citys=this.getResults(BRegion.class,"nameCN", ((BRegion)cObj).getNameCN(), null);
							boolean flag=true;
							if(citys!=null){								
								for(int i=0;i<citys.size();i++){
									BRegion bRegion=(BRegion)citys.get(i);
									if(bRegion.getParentRegionId()==((BRegion)pObj).getId()){
										try {
											this.setFieldValue(bObject[0], "city",bRegion);
										} catch (ElementsException e) {
											e.printStackTrace();
										}
										flag=false;
										break;
									}
								}
							}
							if(flag){
								bObject[0]="城市:"+((BRegion)cObj).getNameCN()+"不属于"+((BRegion)pObj).getNameCN()+"省";
							}
						}
					}
				}
			}
			//验证区县和城市的关系
			if(dObj!=null){
				if(cObj==null){
					bObject[0]="请输入区县所在的城市";
				}else{
					if(((BRegion)dObj).getParentRegionId()!=((BRegion)cObj).getId()){
						//避免不同城市下区县同名
						Vector districts=this.getResults(BRegion.class,"nameCN", ((BRegion)dObj).getNameCN(), null);
						boolean flag=true;
						if(districts!=null){								
							for(int i=0;i<districts.size();i++){
								BRegion bRegion=(BRegion)districts.get(i);
								if(bRegion.getParentRegionId()==((BRegion)cObj).getId()){
									try {
										this.setFieldValue(bObject[0], "district",bRegion);
									} catch (ElementsException e) {
										e.printStackTrace();
									}
									flag=false;
									break;
								}
							}
						}
						if(flag){
							bObject[0]="区县:"+((BRegion)dObj).getNameCN()+"不属于"+((BRegion)cObj).getNameCN()+"市";
						}
					}
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 * 数据网中 AC，AP 验证省市区的关系
	 * @param iParam
	 * @return
	 */
	public Object regionRelateionCheckForName(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			Object pObj=null;
			Object cObj=null;
			Object dObj=null;
			try {
				pObj=getFieldValue(bObject[0], "province");
				cObj=getFieldValue(bObject[0], "city");
				dObj=getFieldValue(bObject[0], "district");
			} catch (ElementsException e) {
				e.printStackTrace();
			}
			
			Object pRegion=null;
			Object cRegion=null;
			Object dRegion=null;
			boolean flag=true;
			//验证是否存在
			if(pObj!=null&!pObj.toString().equals("")){
				pRegion=getBObject((BObjectInterface)bObject[0], "nameCN", pObj.toString(), null);
				if(dRegion==null){
					bObject[0]="不存在名称为:"+pObj.toString()+"的省";
					flag=false;
				}
			}
			if(cObj!=null&&!cObj.toString().equals("")){
				cRegion=getBObject((BObjectInterface)bObject[0], "nameCN", cObj.toString(), null);
				if(dRegion==null){
					bObject[0]="不存在名称为:"+cObj.toString()+"的城市";
					flag=false;
				}
			}
			if(dObj!=null&&!dObj.toString().equals("")){
				dRegion=getBObject((BObjectInterface)bObject[0], "nameCN", dObj.toString(), null);
				if(dRegion==null){
					bObject[0]="不存在名称为:"+dObj.toString()+"的区县";
					flag=false;
				}
			}
			if(flag){
				//验证城市和省份的关系
				if(cRegion!=null){
					if(((BRegion)cRegion).getParentRegionId()>0&&pRegion==null){
						bObject[0]="请输入城市所在的省份";
						flag=false;
					}else{
						if(pRegion!=null){
							if(((BRegion)cRegion).getParentRegionId()!=((BRegion)pRegion).getId()){
								flag=false;
								//避免不同省份下城市同名
								Vector citys=this.getResults(BRegion.class,"nameCN", ((BRegion)cRegion).getNameCN(), null);
								boolean flag1=true;
								if(citys!=null){								
									for(int i=0;i<citys.size();i++){
										BRegion bRegion=(BRegion)citys.get(i);
										if(bRegion.getParentRegionId()==((BRegion)pRegion).getId()){
											flag1=false;
											break;
										}
									}
								}
								if(flag1){
									bObject[0]="城市:"+((BRegion)cRegion).getNameCN()+"不属于"+((BRegion)pRegion).getNameCN()+"省";
								}
							}
						}
					}
				}
				if(flag){
					//验证区县和城市的关系
					if(dRegion!=null){
						if(cRegion==null){
							bObject[0]="请输入区县所在的城市";
						}else{
							if(((BRegion)dRegion).getParentRegionId()!=((BRegion)cRegion).getId()){
								//避免不同城市下区县同名
								Vector districts=this.getResults(BRegion.class,"nameCN", ((BRegion)dRegion).getNameCN(), null);
								boolean flag1=true;
								if(districts!=null){								
									for(int i=0;i<districts.size();i++){
										BRegion bRegion=(BRegion)districts.get(i);
										if(bRegion.getParentRegionId()==((BRegion)cRegion).getId()){
											flag1=false;
											break;
										}
									}
								}
								if(flag1){
									bObject[0]="区县:"+((BRegion)dRegion).getNameCN()+"不属于"+((BRegion)cRegion).getNameCN()+"市";
								}
							}
						}
					}
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 * 数据网中 WLAN热点 验证类型和小类的关系
	 * @param iParam
	 * @return
	 */
	public Object dn_BWlanHotspot_SubCategory(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		boolean flag=true;
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			BWlanHotspot bWlanHotspot=(BWlanHotspot)bObject[0];
			int[] a=null;
			//学校
			if(bWlanHotspot.getHotspotType()==5){
				a=new int[]{1,2,3,4,5,6,7};
			}else if(bWlanHotspot.getHotspotType()==6){
				a=new int[]{8,9,10,11,12};
			}else if(bWlanHotspot.getHotspotType()==13){
				a=new int[]{13,14,15,16,17,18};
			}
			if(a!=null){
				for(int i=0;i<a.length;i++){
					if(bWlanHotspot.getSubCategory()==a[i]){
						flag=false;
						break;
					}
				}
				//排除小类为空的情况
				if(flag&&bWlanHotspot.getSubCategory()>0){
					String subCategory = SysDictionaryFactory.getSysDictionaryValueCN("WLANHOTSPOT", "SUBCATEGORY", (byte)bWlanHotspot.getSubCategory());
					String hotspotType = SysDictionaryFactory.getSysDictionaryValueCN("WLANHOTSPOT", "HOTSPOTTYPE", (byte)bWlanHotspot.getHotspotType());
					bObject[0]="小类:"+subCategory+"不属于类型:"+hotspotType;
				}
			}else{
				bWlanHotspot.setSubCategory(-1);
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 *  数据网    EPON中继  新增和修改时要处理的关系
	 * @param iParam
	 * @return
	 */
	public Object dn_BRelayPath_Relation(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		boolean flag=true;
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			BRelayPath bRelayPath=(BRelayPath)bObject[0];
			if(bRelayPath.getId()<=0){
				BDNPathOperation.dn_BRelayPath_createDNPathConnection(bRelayPath);
				BDNPathOperation.dn_BRelayPath_SetValue(bRelayPath);
			}else{
				bRelayPath.setSequenceNo(bRelayPath.getId());
			}
			BDNPathOperation.dn_BRelayPath_createBDNBusinessPathAudits(bRelayPath);
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 *  数据网    EPON中继  新增和修改时要处理的关系
	 * @param iParam
	 * @return
	 */
	public Object dn_BRelayPath_IntegrityOfpath(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		boolean flag=true;
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			BRelayPath bRelayPath=(BRelayPath)bObject[0];
			if(bRelayPath.getId()<=0){
				BDNPathOperation.dn_BRelayPath_createDNPathConnection(bRelayPath);
			}
			BDNPathOperation.dn_BRelayPath_createBDNBusinessPathAudits(bRelayPath);
			dataList.add(bObject);
		}
		return dataList;
	}
	
	/**
	 *  数据网    EPON中继  新增和修改时要处理的关系
	 * @param iParam
	 * @return
	 */
	public Object dn_BRelayPath_backupStatus(List iParam) {
		List blist = (List) iParam.get(0);
		String targetObject = (String) iParam.get(1);
		String title = (String) iParam.get(2);
		List dataList = new ArrayList();
		Iterator it = blist.iterator();
		boolean flag=true;
		while (it.hasNext()) {
			Object[] bObject = (Object[]) it.next();
			BRelayPath bRelayPath=(BRelayPath)bObject[0];
			if(bRelayPath.getBackupStatus()==0){
				bRelayPath.setMainStandbyDNPath(null);
			}else if(bRelayPath.getBackupStatus()==1){
				Object obj=this.getBObject(bRelayPath, "id", bRelayPath.getId()+"", null);
				if(obj!=null){
					BRelayPath oldBRelayPath=(BRelayPath)obj;
					if(oldBRelayPath.getMainStandbyDNPath()!=null){
						bRelayPath.setMainStandbyDNPath(oldBRelayPath.getMainStandbyDNPath());
					}
//					bRelayPath.setStandbyDNPath(bRelayPath.getMainStandbyDNPath());
				}
			}
			dataList.add(bObject);
		}
		return dataList;
	}
	
}

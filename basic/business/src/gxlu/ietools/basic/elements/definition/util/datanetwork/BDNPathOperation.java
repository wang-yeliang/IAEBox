package gxlu.ietools.basic.elements.definition.util.datanetwork;

import gxlu.afx.system.common.CommonClientEnvironment;
import gxlu.afx.system.common.RateListFactory;
import gxlu.afx.system.common.SysDictionaryFactory;
import gxlu.afx.system.common.sysexception.SDHException;
import gxlu.afx.system.user.common.businessobject.BOperator;
import gxlu.ietools.basic.elements.definition.util.ElementDefHelper;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.util.PropertyConstvalue;
import gxlu.ietools.basic.system.util.VariableNames;
import gxlu.ossc.datanetwork.common.bizobject.BDNVertex;
import gxlu.ossc.datanetwork.common.bizobject.logical.BATMCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BE1CTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BFRCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BUIFCTP;
import gxlu.ossc.datanetwork.common.bizobject.logical.BUIFSubCTP;
import gxlu.ossc.datanetwork.common.bizobject.physical.BConnector;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNDevice;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNPort;
import gxlu.ossc.datanetwork.common.bizobject.physical.BDNSlot;
import gxlu.ossc.datanetwork.common.bizobject.physical.BShelf;
import gxlu.ossc.datanetwork.common.bizobject.physical.connection.BPortAssoc;
import gxlu.ossc.datanetwork.common.bizobject.provision.BADNPathTerminal;
import gxlu.ossc.datanetwork.common.bizobject.provision.BATMPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BCommonService;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDDNMultiPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDDNPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNBusinessPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNBusinessPathAudits;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNBusinessRoute;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNPathConnection;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNPathTerminal;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNRoute;
import gxlu.ossc.datanetwork.common.bizobject.provision.BDNRoute_CTP;
import gxlu.ossc.datanetwork.common.bizobject.provision.BFRPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BFRPortService;
import gxlu.ossc.datanetwork.common.bizobject.provision.BPathConnection_Route;
import gxlu.ossc.datanetwork.common.bizobject.provision.BRelayPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BUserPath;
import gxlu.ossc.datanetwork.common.bizobject.provision.BZDNPathTerminal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BDNPathOperation {
	
	public static String getPathSequenceNo(BDNPath _bPath)
    {
        if(_bPath.getSequenceNo()<0)
            return "";
        String seqNo=_bPath.getSequenceNo()+"";
        int seqLength=seqNo.length();
        for(int i=0;i<5-seqLength;i++)
        {
            seqNo="0"+seqNo;
        }
        return "TM"+seqNo;
    }
	/**
	 * 数据网    EPON中继   记录中继信息
	 * @param bRelayPath
	 */
	public static void dn_BRelayPath_createDNPathConnection(BRelayPath bRelayPath){
		BDNPathConnection pathConnection=new BDNPathConnection();
		Map map = new HashMap();
		Map vMap = new HashMap();
		vMap.put("setDNPath", bRelayPath);
		vMap.put("setLifeCycle",Byte.parseByte("1"));
		map.put(pathConnection, vMap);
		ContainerFactory.addobjectListMap(bRelayPath, VariableNames.OBJECT_BEHIND, VariableNames.OBJECT_INSERT, map);
	}
	 //资源准确性检查
	   public static void dn_BRelayPath_SetValue(BDNPath bPath)
	   {
		   Map map=new HashMap();
		   byte integrityOfpath=2;
		   int nCountEnd = 0;
	       if (bPath.getCurrentConnection() != null)
	       {
	           BDNPathConnection bDNPathConn = bPath.getCurrentConnection();
	           if( bDNPathConn.getADNPathTerminal() != null )
	               nCountEnd++;
	           if( bDNPathConn.getZDNPathTerminals() != null && bDNPathConn.getZDNPathTerminals().size() > 0 )
	               nCountEnd++;

	           if( nCountEnd == 0 )
	        	   integrityOfpath = PropertyConstvalue.DNPATH_INTEGRITYOFPATH_NEITHERINTEGRITY ;
	           else if( nCountEnd == 1 )
	        	   integrityOfpath = PropertyConstvalue.DNPATH_INTEGRITYOFPATH_SINGLEINTEGRITY ;
	           else
	        	   integrityOfpath = PropertyConstvalue.DNPATH_INTEGRITYOFPATH_BOTHINTEGRITY ;
	       }
	       map.put("setIntegrityOfpath", integrityOfpath);
	       
	       StringBuffer sb = new StringBuffer();
	       sb.append("电路名称 : " + bPath.getName() + "\n");
	       sb.append("业务编号 : "+ (bPath.getServiceCode() == null ? "" : bPath.getServiceCode())+ "\n");
	       map.put("setTextRoute", sb.toString());
	       map.put("setTextRoute2", "业务编号 : "+ (bPath.getServiceCode() == null ? "" : bPath.getServiceCode())+ "\n");
	       Map objMap = new HashMap();
	       objMap.put(bPath, "getId");
	       map.put("setSequenceNo", objMap);
	       
	       ContainerFactory.addobjectListMap(bPath, VariableNames.OBJECT_SELF, VariableNames.OBJECT_CONVERTOR, map);
	   }
	/**
	 *  数据网    EPON中继  创建审计
	 * @param bRelayPath
	 */
	public static void dn_BRelayPath_createBDNBusinessPathAudits(BDNPath bPath){
		BDNBusinessPathAudits audit=new BDNBusinessPathAudits();
		BDNPath oldPath=null;
		Map map = new HashMap();
		Map vMap = new HashMap();
		vMap.put("setAfterModifyPara", "");
		vMap.put("setAfterModifyRoute", "");
		vMap.put("setAttributeModify", "");//修改前属性
		vMap.put("setBeforeModifyPara", "");
		vMap.put("setBeforModifyRoute", "");
		vMap.put("setServiceAttributeAfterModify", "");// 修改后属性		
		vMap.put("setOperationTime", new Date());
		
		vMap.put("setOperationTime", new Date());
        BOperator bOperator = CommonClientEnvironment.getOperator();
		if (bOperator != null) {
			vMap.put("setOperator", bOperator.getName());
			vMap.put("setOrganizationName", ElementDefHelper.getOrgNameOfOperator(bOperator,false));
			vMap.put("setIpaddress", bOperator.getIPAddress());
			if (bPath.getId() > 0){
				vMap.put("setOperationType", ElementDefHelper.getOperationType(PropertyConstvalue.PERSIST_TYPE_UPDATE));
			}else{
				vMap.put("setOperationType", ElementDefHelper.getOperationType(PropertyConstvalue.PERSIST_TYPE_ADD));
			}
		}
		if(bPath.getId()>0){
			Object obj=ElementDefHelper.getResult(bPath.getClass(), "id",bPath.getId()+"",
					"DNPath[ATwigDevice][ZTwigDevice][Project][ACustomer][ZCustomer][Maintainer][Maintainers][AMaintainer][BMaintainer][DNPathConnection]"+
					"[MainStandbyDNPath][StandbyDNPath][ARegion][ZRegion][DNPathConnection[PathConnection_Route][ADNPathTerminal][ZDNPathTerminal]]");
//					"DNPath[ATwigDevice][ZTwigDevice][Project][ACustomer][ZCustomer][Rate][Maintainer][Maintainers][AMaintainer][BMaintainer][DNPathConnection]"+
//					"[MainStandbyDNPath][StandbyDNPath][ARegion][ZRegion][DNPathConnection[PathConnection_Route][ADNPathTerminal][ZDNPathTerminal]]");
			oldPath =obj!=null?(BDNPath)obj:null;
		}
        if (bPath.getId()>0&& bPath.getLifeStatus()!= PropertyConstvalue.DNCONNECTION_LIFESTATUS_WAIT_SWITCH
                    && bPath.getLifeStatus()!= PropertyConstvalue.DNCONNECTION_LIFESTATUS_WAIT_CHANGE) {
        	 //bPath是修改后得，dPath是修改前的。
        	 try {
				fetchBasicAuditInfo(bPath,vMap);
			} catch (SDHException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
        	 vMap.put("setAttributeModify",getAttributeModify(oldPath));
             vMap.put("setServiceAttributeAfterModify",getAttributeModify(bPath));//修改后属性
             vMap.put("setBeforeModifyPara",getModifyParam(oldPath));
             vMap.put("setAfterModifyPara",getModifyParam(bPath));
			try {
				oldPath = (BDNPath)assembleBDNPath(oldPath);
				vMap.put("setAfterModifyRoute",generatePathRouteDesc(bPath));
				vMap.put("setBeforModifyRoute",generatePathRouteDesc(oldPath));
			} catch (SDHException e) {
				e.printStackTrace();
			}
         }
         else if (bPath.getId()<=0) { //bPath是新建/新装的，dPath是新建/新装后的。
             try {
				fetchBasicAuditInfo(bPath, vMap);
			} catch (SDHException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
             vMap.put("setAfterModifyPara",getModifyParam(bPath));
             try {
				vMap.put("setAfterModifyRoute",generatePathRouteDesc(bPath));
				vMap.put("setServiceAttributeAfterModify",getAttributeModify(bPath));//新增后属性
			} catch (SDHException e) {
				e.printStackTrace();
			}
         }
        map.put(audit, vMap);
        ContainerFactory.addobjectListMap(bPath, VariableNames.OBJECT_BEHIND, VariableNames.OBJECT_INSERT, map);
	}
	// 组装完整的电路对象,对于调度中的dpath,同时组织完整的backupPath.
	private static BDNPath assembleBDNPath(BDNPath oldPath) throws SDHException {
		if (oldPath.getATwigDevice()==null || oldPath.getZTwigDevice()==null) {
			oldPath.setATwigDevice(null);
			oldPath.setZTwigDevice(null);
		}
		BDNPath backPath=oldPath.getBackupDNPath();
		if (backPath != null) {
			BDNPath bBackPath = assembleBDNPath(backPath);
			oldPath.setBackupDNPath(bBackPath);
			bBackPath.setMainDNPath(oldPath);
		}
//		copyRelations(oldPath);
//		Vector dConns = oldPath.getDNPathConnections();
//		if (dConns != null && dConns.size() > 0) {
//			for (int i = 0; i < dConns.size(); i++) {
//				DNPathConnection dConn = (DNPathConnection) dConns.elementAt(i);
//				BDNPathConnection bConn = assembleConnection(dConn);
//				bConn.setDNPath(oldPath);
//				oldPath.addDNPathConnection(bConn);
//			}
//		}
		return oldPath;
	}

//	private BDNPathConnection assembleConnection(BDNPathConnection bConn) {
//		if (bConn == null)
//			return null;
//		Vector dPCRs = bConn.getPathConnection_Routes();
//		if (dPCRs == null)
//			return null;
//
//		for (int i = 0; i < dPCRs.size(); i++) {
//			BPathConnection_Route dPCR = (BPathConnection_Route) dPCRs.elementAt(i);
//			BPathConnection_Route bPCR = loadPCR(dPCR);
//			bPCR.setDNPathConnection(_bConn);
//			_bConn.addPathConnection_Route(bPCR);
//		}
//
//		BTerminalServer termServer = new BTerminalServer(context);
//		DNPathTerminal aTerm = _dConn.getADNPathTerminal();
//		if (aTerm != null)
//			_bConn.setADNPathTerminal((BADNPathTerminal) termServer.load(aTerm.getId()));
//		Vector zTerms = _dConn.getZDNPathTerminals();
//		if (zTerms == null)
//			return;
//
//		for (int i = 0; i < zTerms.size(); i++) {
//			DNPathTerminal dTerm = (DNPathTerminal) zTerms.elementAt(i);
//			BDNPathTerminal term = termServer.load(dTerm.getId());
//			BZDNPathTerminal bTerm = (BZDNPathTerminal) term;
//			bTerm.setDNPathConnection(_bConn);
//			_bConn.addZDNPathTerminal(bTerm);
//		}
//		return bConn;
//	}

	/**
	 * 复制DNPath的关系到BDNPath中。
	 */
//	protected void copyRelations(BDNPath bDNPath)throws SDHException {
//		if (bDNPath.getMainStandbyDNPath() != null) {
//			BDNPath bMainStandbyPath = (BDNPath) BDConvertor.dtob(_source.getMainStandbyDNPath());
//			_target.setMainStandbyDNPath(bMainStandbyPath);
//			bMainStandbyPath.setStandbyDNPath(_target);
//		}
//		if (bDNPath.getStandbyDNPath() != null) {
//			BDNPath bStandbyPath = (BDNPath) BDConvertor.dtob(_source.getStandbyDNPath());
//			_target.setStandbyDNPath(bStandbyPath);
//			bStandbyPath.setMainStandbyDNPath(_target);
//		}
//	}

	// 获取电路当前状态的基本信息
	private static void fetchBasicAuditInfo(BDNPath bPath, Map vMap)
			throws SDHException, RuntimeException {

		if (bPath.getCustomerNo() != null)
			vMap.put("setCustomerNo", bPath.getCustomerNo());
		else if (bPath.getCustomer() != null)
			vMap.put("setCustomerNo", bPath.getCustomer().getNo());
		else
			vMap.put("setCustomerNo", "");

		vMap.put("setRate", RateListFactory.getRateListDisplayName(bPath.getRateId()));
		Map objMap1=new HashMap();
		objMap1.put(bPath, "getId");
		vMap.put("setEntityId", objMap1);
		vMap.put("setMaintainer", bPath.getMaintainer() == null ? "" : bPath.getMaintainer().getName());
		vMap.put("setName", bPath.getName());
		vMap.put("setNo", bPath.getNo());
		vMap.put("setSeqNo", bPath.getSeqNo());

		if (bPath instanceof BATMPath) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_ATM);
			vMap.put("setType", ((BATMPath) bPath).getType());
			vMap.put("setServiceCategory", ((BATMPath) bPath).getAtmServiceCategory());
			vMap.put("setServiceLevel", ((BDNBusinessPath) bPath).getServiceLevel());
		} else if (bPath instanceof BFRPath) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_FR);
			vMap.put("setType", ((BFRPath) bPath).getType());
			vMap.put("setServiceCategory", ((BFRPath) bPath).getFrServiceCategory());
			vMap.put("setServiceLevel", ((BDNBusinessPath) bPath).getServiceLevel());
		} else if (bPath instanceof BFRPortService) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_FRPORTSERVICE);
			vMap.put("setType", ((BFRPortService) bPath).getType());
			vMap.put("setServiceCategory", ((BFRPortService) bPath).getFrServiceCategory());
			vMap.put("setServiceLevel(", ((BDNBusinessPath) bPath).getServiceLevel());
		} else if (bPath instanceof BDDNPath) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_DDN);
			vMap.put("setType", ((BDDNPath) bPath).getType());
			vMap.put("setServiceLevel", ((BDNBusinessPath) bPath).getServiceLevel());
		} else if (bPath instanceof BCommonService) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_COMMONSERVICE);
			vMap.put("setType", ((BCommonService) bPath).getType());
			vMap.put("setServiceLevel", ((BDNBusinessPath) bPath).getServiceLevel());
		} else if (bPath instanceof BDDNMultiPath) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_DDNMULTIPATH);
			vMap.put("setType", ((BDDNMultiPath) bPath).getMuxType());
			// vMap.put("setServiceLevel( ( (BDDNMultiPath)
			// bPath)getServiceLevel());
		} else if (bPath instanceof BRelayPath) {
			if (((BRelayPath) bPath).getType() == PropertyConstvalue.RELAYPATH_TYPE_IP)
				vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_IPRELAYPATH);
			else if (((BRelayPath) bPath).getType() == PropertyConstvalue.RELAYPATH_TYPE_XDSL)
				vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_XDSLRELAYPATH);
			// else if (
			// ((BRelayPath) bPath).getType() ==
			// PropertyConstvalue.RELAYPATH_TYPE_USERCIRCUIT)
			// vMap.put("setEntityType(
			// PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_CIRCUIT);
			else
				vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_RELAYPATH);
			vMap.put("setType", ((BRelayPath) bPath).getType());
		} else if (bPath instanceof BUserPath) {
			vMap.put("setEntityType",PropertyConstvalue.DNBUSINESSPATHAUDITS_ENTITYTYPE_CIRCUIT);
		}
		// 电路维护等级
		vMap.put("setCircuitMaintainLevel", bPath.getCircuitMaintainLevel());

	}

	public static String generatePathRouteDesc(BDNPath path) throws SDHException {
		StringBuffer sbStrResult = new StringBuffer();
		Vector tdhts = generatePathDescTable(path, null);

		if (tdhts == null && tdhts.size() <= 0)
			return "";

		for (int i = 0; i < tdhts.size(); i++) {
			TextDescHashTable tdht = (TextDescHashTable) tdhts.get(i);
			for (int j = 0; j < tableHeaderBSPath.size(); j++) {
				String value = tdht	.getValue((String) (tableHeaderBSPath.get(j)));
				if (value == null)
					value = " ";
				sbStrResult.append(value);
				sbStrResult.append(PropertyConstvalue.DELIMITER_COL);
			}
			sbStrResult.append(PropertyConstvalue.DELIMITER_ROW);
		}
		return sbStrResult.toString();
	}

	// 生成BDNPathConnection的文本路由.(不生成connection A/Z端描述,因为A/Z端已经在route中存在)
	// 返回Vector中按顺序每一个对象是一个TextDescHashTable对象,代表一行数据.
	// 按_routes的顺序依次生成.
	public static Vector generatePathDescTable(BDNPath _path, List _formats)throws SDHException {
		Vector result = new Vector();
		if (_path == null)
			return result;
		BDNPathConnection conn = _path.getCurrentConnection();
		if (conn == null)
			return result;

		// 如果没有路由，也应该加入一条纪录，因为一条电路至少有一条文本描述纪录，否则UI上无法判断
		Vector bCRs = conn.getPathConnection_Routes();

		// 中继电路需要加上A端端口描述
		if (_path instanceof BRelayPath || _path instanceof BDDNMultiPath|| _path instanceof BFRPortService) {
			BDNPathTerminal aterm = conn.getADNPathTerminal();
			if (aterm != null) {
				Vector aterms = new Vector(1);
				aterms.add(aterm);
				result.addAll(getTermTableDesc(aterms));
			}
		}

		if (bCRs != null || bCRs.size() > 0) {
			result.addAll(genPcrsDescTable(bCRs, _formats));
		}

		// 中继电路需要加上Z端端口描述
		if (_path instanceof BRelayPath || _path instanceof BDDNMultiPath) {
			Vector zterm = conn.getZDNPathTerminals();
			if (zterm != null && zterm.size() > 0)
				result.addAll(getTermTableDesc(zterm));
		}

		return result;
	}

	public static Vector getTermTableDesc(Vector terms) throws SDHException {
		Vector result = new Vector();
		for (int i = 0; i < terms.size(); i++) {
			BDNPathTerminal term = (BDNPathTerminal) terms.get(i);
			TextDescHashTable termDesc = getTermTableDesc(term);
			if (termDesc != null) {
				result.add(termDesc);
			}
		}
		return result;
	}

	public static Vector genPcrsDescTable(Vector _pcrs, List _formats)	throws SDHException {
		Vector result = new Vector();
		BPathConnection_Route bPCR = null;
		Enumeration routeEnum = _pcrs.elements();
		while (routeEnum.hasMoreElements()) {
			bPCR = (BPathConnection_Route) routeEnum.nextElement();
			BDNRoute route = bPCR.getDNRoute();
			Vector routeRows = generateDNRouteTextDescHashtable(route,null);
			if (routeRows != null) {
				result.addAll(routeRows);
			}
		}
		return result;
	}
	 public static Vector generateDNRouteTextDescHashtable(BDNRoute dnRoute, List _format) throws SDHException {
	        if (dnRoute instanceof BDNBusinessRoute) {
	        	Vector tableVector = new Vector();
	        	//一个DNRoute_CTP就对应一个TextDescHashTable
	        	tableVector.addAll(getVecTDHashTable(((BDNBusinessRoute)dnRoute).getADNRoute_CTPs(), CTPTYPE_AEND));
	        	tableVector.addAll(getVecTDHashTable(((BDNBusinessRoute)dnRoute).getRelayRoute_CTPs(),CTPTYPE_NORMAL));
	        	tableVector.addAll(getVecTDHashTable(((BDNBusinessRoute)dnRoute).getZDNRoute_CTPs(),CTPTYPE_ZEND));
	        	return tableVector;
	        }
	        return new Vector();
	  }
	 
	  private static Vector getVecTDHashTable(Vector route_ctps, byte ctpType)throws SDHException {
			Vector rows = new Vector();
			if (route_ctps == null || route_ctps.size() <= 0)
				return rows;
			for (int i = 0; i < route_ctps.size(); i++) { //考虑有多个起始CTP存在，目前只有一个
			    BDNRoute_CTP bRC = (BDNRoute_CTP) route_ctps.elementAt(i);
			    BDNVertex vertex = bRC.getDNVertex();
			    //获取端点的描述:端口/插盘/机房/网元...
	
			    TextDescHashTable tdht =getVertexTableDesc(vertex);
	
			    if(ctpType==CTPTYPE_AEND)
				tdht.putValue(tdht.TEXTDESC_POSITION, tdht.TEXTDESC_POSITION_BEGIN);
			    else if(ctpType==CTPTYPE_NORMAL)
				tdht.putValue(tdht.TEXTDESC_POSITION, tdht.TEXTDESC_POSITION_RELAY);
			    if(ctpType==CTPTYPE_ZEND)
				tdht.putValue(tdht.TEXTDESC_POSITION, tdht.TEXTDESC_POSITION_END);
			    rows.addElement(tdht);
			}
			return rows;
	}
	private static TextDescHashTable getTermTableDesc(BDNPathTerminal term)throws SDHException {
		TextDescHashTable result = null;
		if (term == null)
			return result;

		if (term instanceof BADNPathTerminal && term.getDNVertex() != null) {
			result = getVertexTableDesc(term.getDNVertex());
			BDNPort dPort = getDNPort(term.getDNVertex());
			appendInOutConnectorsByPort(result, dPort);
			result.putValue(result.TEXTDESC_POSITION,result.TEXTDESC_POSITION_BEGIN);
		} else if (term instanceof BZDNPathTerminal	&& term.getDNVertex() != null) {
			result = getVertexTableDesc(term.getDNVertex());
			BDNPort dPort = getDNPort(term.getDNVertex());
			appendInOutConnectorsByPort(result, dPort);
			result.putValue(result.TEXTDESC_POSITION,result.TEXTDESC_POSITION_END);
		}
		return result;
	}

	public static TextDescHashTable getVertexTableDesc(BDNVertex vertex)throws SDHException {
		TextDescHashTable tdht = getDNPortTableDesc(getDNPort(vertex));
		if (vertex instanceof BATMCTP) {
			BATMCTP atmctp = (BATMCTP) vertex;
			tdht.putValue(tdht.TEXTDESC_POINTTYPE, tdht.TEXTDESC_POINTTYPE_ATM);
			tdht.putValue(tdht.TEXTDESC_VPI, String.valueOf(atmctp.getVpi()));
			tdht.putValue(tdht.TEXTDESC_VCI, String.valueOf(atmctp.getVci()));
		} else if (vertex instanceof BFRCTP) {
			BFRCTP frctp = (BFRCTP) vertex;
			tdht.putValue(tdht.TEXTDESC_POINTTYPE, tdht.TEXTDESC_POINTTYPE_FR);
			if (frctp.getE1CTP() != null)
				tdht.putValue(tdht.TEXTDESC_SLOTS, frctp.getE1CTP().getTslots());
			else if (frctp.getUIFCTP() != null) {
				String abPortStr = frctp.getUIFCTP().toStringCTPNumber();
				if (abPortStr != null && abPortStr.startsWith("/"))
					abPortStr = abPortStr.substring(1);
				tdht.putValue(tdht.TEXTDESC_SLOTS, abPortStr);
			}
			tdht.putValue(tdht.TEXTDESC_DLCI, String.valueOf(frctp.getDlci()));
			tdht.putValue(tdht.TEXTDESC_RATE, String.valueOf(frctp.getRate()));
		} else if (vertex instanceof BE1CTP) {
			BE1CTP e1ctp = (BE1CTP) vertex;
			tdht.putValue(tdht.TEXTDESC_POINTTYPE,
					tdht.TEXTDESC_POINTTYPE_DDNE1);
			tdht.putValue(tdht.TEXTDESC_SLOTS, e1ctp.getTslots());
		} else if (vertex instanceof BUIFCTP) {
			BUIFCTP uifctp = (BUIFCTP) vertex;
			tdht.putValue(tdht.TEXTDESC_POINTTYPE,
					tdht.TEXTDESC_POINTTYPE_DDNE1);
			tdht.putValue(tdht.TEXTDESC_SLOTS, uifctp.toStringCTPNumber());
		} else if (vertex instanceof BUIFSubCTP) {
			BUIFSubCTP uifctp = (BUIFSubCTP) vertex;
			if (uifctp.getE1CTP() != null) {
				tdht.putValue(tdht.TEXTDESC_POINTTYPE,
						tdht.TEXTDESC_POINTTYPE_DDNE1);
				tdht.putValue(tdht.TEXTDESC_SLOTS, uifctp.getE1CTP()
						.getTslots());
			} else if (uifctp.getUIFCTP() != null) {
				tdht.putValue(tdht.TEXTDESC_POINTTYPE,
						tdht.TEXTDESC_POINTTYPE_DDNE1);
				tdht.putValue(tdht.TEXTDESC_SLOTS, uifctp.getUIFCTP()
						.toStringCTPNumber());
			}
		}

		return tdht;
	}

	public static TextDescHashTable getDNPortTableDesc(BDNPort bPort)throws SDHException {
		TextDescHashTable unit = new TextDescHashTable();
		// DNPort dPort = BPathServerUtil.getDNPort(context, bPort);
		BDNPort bDNPort = getDNPort(bPort);
		if (bDNPort == null)
			return unit;
		unit.putValue(TextDescHashTable.TEXTDESC_PORTNAME, bDNPort.getNameMoniker());

		if (bDNPort.getDNPackage() != null && bDNPort.getDNPackage().getDNSlots() != null)// 板卡
		{
			Vector slots = bDNPort.getDNPackage().getDNSlots();
			StringBuffer slotsStr = new StringBuffer("");
			for (int i = 0; i < slots.size(); i++) {
				BDNSlot slot = (BDNSlot) slots.get(i);
				if (slot.getDNSlotType() != null&& slot.getDNSlotType().getDisplayLabel() != null) {
					if (i != 0)
						slotsStr.append("-");
					slotsStr.append(slot.getDNSlotType().getDisplayLabel());
				}
			}
			unit.putValue(TextDescHashTable.TEXTDESC_PACKAGE, slotsStr.toString());// 板卡
		} else
			unit.putValue(TextDescHashTable.TEXTDESC_PACKAGE, "");// 板卡

		if (bDNPort.getDNDevice() != null) {
			BDNDevice device = bDNPort.getDNDevice();
			// 设备
			unit.putValue(TextDescHashTable.TEXTDESC_DEVICE, device
					.getShortCode());

			if (device.getHost() != null)// 机房
				unit.putValue(TextDescHashTable.TEXTDESC_HOST, device.getHost()
						.getName());

			if (device.getDNNode() != null)// 节点
				unit.putValue(TextDescHashTable.TEXTDESC_NODE, device
						.getDNNode().getName());// 节点

			if (device.getShelf() != null) {
				BShelf dShelf = device.getShelf();
				// 子架
				if (dShelf.getLogicalShelfNo() != null)
					unit.putValue(TextDescHashTable.TEXTDESC_SHELF, dShelf
							.getLogicalShelfNo()); // 子架

				if (dShelf.getRack() != null
						&& dShelf.getRack().getName() != null)// 机柜
					unit.putValue(TextDescHashTable.TEXTDESC_RACK, dShelf
							.getRack().getName());// 机柜
			}
		}
		return unit;
	}

	public static BDNPort getDNPort(BDNVertex bVertex) {
		if (bVertex == null)
			return null;
		if (bVertex.getId() <= 0) {
			// 同生共灭的资源不能查Vertex本身，要直接查端口
			long portId = -1;
			if (bVertex instanceof BATMCTP) {
				portId = ((BATMCTP) bVertex).getDNPortId();
			} else if (bVertex instanceof BFRCTP) {
				if (((BFRCTP) bVertex).getDNPortId() >= 0)
					portId = ((BFRCTP) bVertex).getDNPortId();
				else if (((BFRCTP) bVertex).getE1CTP() != null)
					portId = ((BFRCTP) bVertex).getE1CTP().getDNPortId();
				else if (((BFRCTP) bVertex).getUIFCTP() != null)
					portId = ((BFRCTP) bVertex).getUIFCTP().getDNPortId();
			} else if (bVertex instanceof BE1CTP) {
				portId = ((BE1CTP) bVertex).getDNPortId();
			} else if (bVertex instanceof BUIFCTP) {
				portId = ((BUIFCTP) bVertex).getDNPortId();
			} else if (bVertex instanceof BDNPort) {
				portId = bVertex.getId();
			}
			if (portId >= 0)
				return (BDNPort) ElementDefHelper.getBObject(BDNPort.class, "id", portId+ "", null);
			else
				return null;
		}
		if (bVertex instanceof BATMCTP) {
			BATMCTP atmCTP = (BATMCTP) ElementDefHelper.getBObject(BATMCTP.class, "id",	bVertex.getId() + "", null);
			return atmCTP.getDNPort();
		} else if (bVertex instanceof BFRCTP) {
			BFRCTP frCTP = (BFRCTP) ElementDefHelper.getBObject(BFRCTP.class, "id", bVertex.getId()	+ "", null);
			if (frCTP.getDNPort() != null)
				return frCTP.getDNPort();
			else if (frCTP.getUIFCTP() != null)
				return frCTP.getUIFCTP().getDNPort();
			else if (frCTP.getE1CTP() != null)
				return frCTP.getE1CTP().getDNPort();
		} else if (bVertex instanceof BE1CTP) {
			BE1CTP e1CTP = (BE1CTP) ElementDefHelper.getBObject(BE1CTP.class, "id", bVertex.getId()	+ "", null);
			return e1CTP.getDNPort();
		} else if (bVertex instanceof BUIFCTP) {
			BUIFCTP uifCTP = (BUIFCTP) ElementDefHelper.getBObject(BUIFCTP.class, "id",	bVertex.getId() + "", null);
			return uifCTP.getDNPort();
		} else if (bVertex instanceof BDNPort) {
			BDNPort dnPort = (BDNPort) ElementDefHelper.getBObject(BDNPort.class, "id",	bVertex.getId() + "", null);
			return dnPort;
		}
		return null;
	}
	private static void appendInOutConnectorsByPort(TextDescHashTable ht, BDNPort port)
			throws SDHException {
		if (port == null)
			return;

		// Expression expression = new
		// ExpressionBuilder().get("aVertexId").equal(
		// port.getId());
		// Vector portAssocs = context.getSession().readAllObjects(
		// PortAssoc.class, expression);
		Vector portAssocs = ElementDefHelper.getResults(BPortAssoc.class, "aVertexId", port.getId() + "", null);
		if (portAssocs == null || portAssocs.isEmpty()) {
			return;
		}

		List connectors = new ArrayList();
		StringBuffer buff = new StringBuffer();
		int num = 0;
		for (int i = 0; i < portAssocs.size(); i++) {
			num++;
			BConnector connector = ((BPortAssoc)(portAssocs.get(i))).getZConnector();
			if (connector == null)
				continue;
			if (num == 1) {
				buff.append(connector.getMoniker());
			} else {
				buff.append("," + connector.getMoniker());
			}
		}
		ht.putValue(ht.TEXTDESC_INOUTCONNECTOR, buff.toString());
	}

	public static final Vector tableHeaderBSPath = new Vector();
	static {
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_POSITION);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_HOST);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_RACK);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_SHELF);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_DEVICE);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_PACKAGE);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_PORTNAME);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_SLOTS);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_DLCI);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_VPI);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_VCI);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_INNERCONNECTOR);
		tableHeaderBSPath.add(PropertyConstvalue.TEXTDESC_OUTERCONNECTOR);
	}

	private static String getAttributeModify(BDNPath bPath) {
		if (bPath == null)
			return "";
		StringBuffer attributeModify = new StringBuffer("电路名称:");
		attributeModify.append(bPath.getName() == null ? "" : bPath.getName());
		attributeModify.append("，电路编号：");
		attributeModify.append(bPath.getNo() == null ? "" : bPath.getNo());
		attributeModify.append("，类型：");

		byte type = -1;
		if (bPath instanceof BATMPath) {
			type = ((BATMPath) bPath).getType();
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("ATMPATH", "TYPE", type));

			attributeModify.append("，服务等级：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNBUSINESSPATH","ATMSERVICECATEGORY", ((BATMPath) bPath)
									.getAtmServiceCategory()));
			attributeModify.append("，级别：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNBUSINESSPATH", "SERVICELEVEL",((BATMPath) bPath).getServiceLevel()));

		} else if (bPath instanceof BFRPath) {
			type = ((BFRPath) bPath).getType();
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("FRPATH", "TYPE", type));

			attributeModify.append("，服务等级：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNBUSINESSPATH","FRSERVICECATEGORY", ((BFRPath) bPath)
									.getFrServiceCategory()));
			attributeModify.append("，级别：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNBUSINESSPATH", "SERVICELEVEL",((BFRPath) bPath).getServiceLevel()));

		} else if (bPath instanceof BDDNPath) {
			type = ((BDDNPath) bPath).getType();
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DDNPATH", "TYPE", type));
			attributeModify.append("，级别：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNBUSINESSPATH", "SERVICELEVEL",((BDDNPath) bPath).getServiceLevel()));
		} else if (bPath instanceof BDDNMultiPath) {
			type = ((BDDNMultiPath) bPath).getMuxType();
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DDNMULTIPATH", "MUXTYPE", type));
		} else if (bPath instanceof BRelayPath) {
			type = ((BRelayPath) bPath).getType();
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("RELAYPATH", "TYPE", type));
			attributeModify.append("，主备用：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("RELAYPATH", "BACKUPSTATUS",((BRelayPath) bPath).getBackupStatus()));
			attributeModify.append("，中继性质：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("RELAYPATH", "RELAYTYPE",((BRelayPath) bPath).getRelayType()));
		} else if (bPath instanceof BCommonService) {
			type = ((BCommonService) bPath).getType();
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("COMMONSERVICE", "TYPE", type));
			attributeModify.append("，级别：");
			attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNBUSINESSPATH", "SERVICELEVEL",((BCommonService) bPath).getServiceLevel()));
		}

		attributeModify.append("，带宽：");
		attributeModify.append(RateListFactory.getRateListDisplayName(bPath.getRateId()));

		attributeModify.append("，电路维护人员：");
		attributeModify.append(bPath.getMaintainer() == null ? "" : bPath.getMaintainer().getName());
		attributeModify.append("，客户编号：");
		String custNo = bPath.getCustomerNo();
		if (custNo == null) {
			if (bPath.getCustomer() != null)
				custNo = bPath.getCustomer().getNo();
		}
		attributeModify.append(custNo == null ? "" : custNo);

		attributeModify.append("，方向：");
		attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("DNPATH", "DIRECTION", bPath.getDirection()));
		attributeModify.append("，电路维护等级：");
		attributeModify.append(SysDictionaryFactory.getSysDictionaryValueCN("GLOBAL", "CIRCUITMAINTAINLEVEL", bPath
						.getCircuitMaintainLevel()));
		if (bPath instanceof BFRPortService) {
			attributeModify.append("，地址：");
			attributeModify.append(bPath.getAAddress() == null ? "" : bPath.getAAddress());
			attributeModify.append("，联系人：");
			attributeModify.append(bPath.getAContactor() == null ? "" : bPath.getAContactor());
			attributeModify.append("，联系人电话：");
			attributeModify.append(bPath.getAPhone() == null ? "" : bPath.getAPhone());
			attributeModify.append("，联系人传真：");
			attributeModify.append(bPath.getAFax() == null ? "" : bPath.getAFax());
			attributeModify.append("，安装地址：");
			attributeModify.append(bPath.getAMountAddress() == null ? "": bPath.getAMountAddress());
			// 投递地址 MAILINGADDRESS 邮编 POSTALCODE
			// attributeModify.append((BFRPortService)bPath.getm()== null ? ""
			// :bPath.getAMountAddress());
			// 客户名称 原有客户编号 是否注释以下两行
			// attributeModify.append("， 客户名称：");
			// attributeModify.append(bPath.getCustomer()== null ?
			// bPath.getCustomerNo():bPath.getCustomer().getName());
			// 客户经理
			attributeModify.append("， 客户经理：");
			attributeModify.append(bPath.getCustManager() == null ? "" : bPath.getCustManager());
			// 客户经理联系电话
			attributeModify.append("， 客户经理联系电话：");
			attributeModify.append(bPath.getCustManagerTel() == null ? "": bPath.getCustManagerTel());

		} else if (bPath instanceof BDDNMultiPath) {
			attributeModify.append("，本端安装地址：");
			attributeModify.append(bPath.getAMountAddress() == null ? "": bPath.getAMountAddress());
			attributeModify.append("，本端用户地址缩写：");
			attributeModify.append(bPath.getAAddress() == null ? "" : bPath.getAAddress());
			attributeModify.append("，本端联系人：");
			attributeModify.append(bPath.getAContactor() == null ? "" : bPath.getAContactor());
			attributeModify.append("，本端联系电话：");
			attributeModify.append(bPath.getAPhone() == null ? "" : bPath.getAPhone());

			attributeModify.append("，对端安装地址：");
			attributeModify.append(bPath.getZMountAddress() == null ? "": bPath.getZMountAddress());
			attributeModify.append("，对端用户地址缩写：");
			attributeModify.append(bPath.getZAddress() == null ? "" : bPath.getZAddress());
			attributeModify.append("，对端联系人：");
			attributeModify.append(bPath.getZContactor() == null ? "" : bPath.getZContactor());
			attributeModify.append("，对端联系电话：");
			attributeModify.append(bPath.getZPhone() == null ? "" : bPath.getZPhone());
		} else if (bPath instanceof BRelayPath) {

		} else {
			attributeModify.append("，A端名称：");
			attributeModify.append(bPath.getAEndName() == null ? "" : bPath.getAEndName());
			attributeModify.append("，A端地址：");
			attributeModify.append(bPath.getAAddress() == null ? "" : bPath.getAAddress());
			attributeModify.append("，A端联系人：");
			attributeModify.append(bPath.getAContactor() == null ? "" : bPath.getAContactor());
			attributeModify.append("，A端联系人电话：");
			attributeModify.append(bPath.getAPhone() == null ? "" : bPath.getAPhone());
			attributeModify.append("，A端联系人传真：");
			attributeModify.append(bPath.getAFax() == null ? "" : bPath.getAFax());
			attributeModify.append("，A端安装地址：");
			attributeModify.append(bPath.getAMountAddress() == null ? "": bPath.getAMountAddress());
			attributeModify.append("，Z端名称：");
			attributeModify.append(bPath.getZEndName() == null ? "" : bPath.getZEndName());
			attributeModify.append("，Z端地址：");
			attributeModify.append(bPath.getZAddress() == null ? "" : bPath.getZAddress());
			attributeModify.append("，Z端联系人：");
			attributeModify.append(bPath.getZContactor() == null ? "" : bPath.getZContactor());
			attributeModify.append("，Z端联系人电话：");
			attributeModify.append(bPath.getZPhone() == null ? "" : bPath.getZPhone());
			attributeModify.append("，Z端联系人传真：");
			attributeModify.append(bPath.getZFax() == null ? "" : bPath.getZFax());
			attributeModify.append("，Z端安装地址：");
			attributeModify.append(bPath.getZMountAddress() == null ? "": bPath.getZMountAddress());
			// 客户名称 原有客户编号 是否注释以下两行
			// attributeModify.append("， 客户名称：");
			// attributeModify.append(bPath.getCustomer()== null ?
			// bPath.getCustomerNo():bPath.getCustomer().getName());
			// 客户经理
			attributeModify.append("， 客户经理：");
			attributeModify.append(bPath.getCustManager() == null ? "" : bPath.getCustManager());
			// 客户经理联系电话
			attributeModify.append("， 客户经理联系电话：");
			attributeModify.append(bPath.getCustManagerTel() == null ? "": bPath.getCustManagerTel());

		}

		attributeModify.append("， 省局调度单号：");
		attributeModify.append(bPath.getPrivisionTicketNo() == null ? "": bPath.getPrivisionTicketNo());
		attributeModify.append("， 大客户系统流水号：");
		attributeModify.append(bPath.getLargeCustSysNo() == null ? "" : bPath.getLargeCustSysNo());
		attributeModify.append("， 长途对端端口：");
		attributeModify.append(bPath.getLongPort() == null ? "" : bPath.getLongPort());
		attributeModify.append("， 长途中继路由：");
		attributeModify.append(bPath.getLongTrunkRoute() == null ? "" : bPath.getLongTrunkRoute());
		attributeModify.append("， 网际长途出口：");
		attributeModify.append(bPath.getInternalLongOutPort() == null ? "": bPath.getInternalLongOutPort());
		attributeModify.append("， 长途电路编号：");
		attributeModify.append(bPath.getLongHaulCode() == null ? "" : bPath.getLongHaulCode());
		// end
		return attributeModify.toString();
	}

	private static String getModifyParam(BDNPath bPath) {
		StringBuffer modeifyParam = new StringBuffer();
		modeifyParam.append("准确性：");
		modeifyParam.append(SysDictionaryFactory.getSysDictionaryValueCN("DNPATH", "RESOURCESTATUS", bPath.getResourceStatus()));
		if (bPath instanceof BDDNPath)
			modeifyParam.append(getDDNModifyParam((BDDNPath) bPath));
		else if (bPath instanceof BFRPath)
			modeifyParam.append(getFRModifyParam((BFRPath) bPath));
		else if (bPath instanceof BATMPath)
			modeifyParam.append(getATMModifyParam((BATMPath) bPath));
		else if (bPath instanceof BCommonService)
			modeifyParam.append(getCommonServiceModifyParam((BCommonService) bPath));

		return modeifyParam.toString();
	}

	private static String getDDNModifyParam(BDDNPath ddnPath) {
		StringBuffer afterModiParam = new StringBuffer("A端专线编号:");
		afterModiParam.append(ddnPath.getSpecialCode() == null ? "" : ddnPath.getSpecialCode());
		afterModiParam.append("，Z端专线编号：");
		afterModiParam.append(ddnPath.getSpecLineNo2() == null ? "" : ddnPath.getSpecLineNo2());
		return afterModiParam.toString();
	}

	private static String getFRModifyParam(BFRPath frPath) {
		StringBuffer afterModiParam = new StringBuffer("Cir:");
		afterModiParam.append(frPath.getCir());
		afterModiParam.append("，Bc：");
		afterModiParam.append(frPath.getBc());
		afterModiParam.append("，Be：");
		afterModiParam.append(frPath.getBe());
		afterModiParam.append("，Eir：");
		afterModiParam.append(frPath.getEir());
		return afterModiParam.toString();
	}

	private static String getATMModifyParam(BATMPath atmPath) {
		StringBuffer afterModiParam = new StringBuffer("PCR:");
		afterModiParam.append(atmPath.getPcr());
		afterModiParam.append("，SCR：");
		afterModiParam.append(atmPath.getScr());
		afterModiParam.append("，MCR：");
		afterModiParam.append(atmPath.getMcr());
		afterModiParam.append("，CDVT：");
		afterModiParam.append(atmPath.getCdvt());
		return afterModiParam.toString();
	}

	private static String getCommonServiceModifyParam(BCommonService path) {
		StringBuffer afterModiParam = new StringBuffer("A端专线编号:");
		afterModiParam.append(path.getSpecialCode() == null ? "" : path.getSpecialCode());
		afterModiParam.append("，Z端专线编号：");
		afterModiParam.append(path.getSpecLineNo2() == null ? "" : path.getSpecLineNo2());
		return afterModiParam.toString();
	}
	
    private static final byte CTPTYPE_AEND = 0; //A端端口
    private static final byte CTPTYPE_ZEND = 1; //Z端端口
    private static final byte CTPTYPE_NORMAL = 2; //中间端口
}

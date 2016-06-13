package gxlu.ietools.client.query.gisquery;

import gis.client.extmodule.provision.util.OpticUtil;
import gis.client.main.ClientEnvironment;
import gis.client.main.GisDictionary;
import gis.client.mapui.JFormattedTextField;
import gis.common.dataobject.GisConst;
import gis.common.dataobject.YCDB;
import gis.common.dataobject.YCable;
import gis.common.dataobject.YCableSection;
import gis.common.dataobject.YCableStore;
import gis.common.dataobject.YCableTerm;
import gis.common.dataobject.YCqj;
import gis.common.dataobject.YDT;
import gis.common.dataobject.YDuctSeg;
import gis.common.dataobject.YECable;
import gis.common.dataobject.YECableAlarm;
import gis.common.dataobject.YECableStore;
import gis.common.dataobject.YECableTerm;
import gis.common.dataobject.YFXH;
import gis.common.dataobject.YJJX;
import gis.common.dataobject.YLogFiber;
import gis.common.dataobject.YMDF;
import gis.common.dataobject.YODF;
import gis.common.dataobject.YOFXX;
import gis.common.dataobject.YOJJX;
import gis.common.dataobject.YOT;
import gis.common.dataobject.YObjectInterface;
import gis.common.dataobject.YOptic;
import gis.common.dataobject.YOpticRoute;
import gis.common.dataobject.YPOS;
import gis.common.dataobject.YPole;
import gis.common.dataobject.YRoom;
import gis.common.dataobject.YSite;
import gis.common.dataobject.YSlingSeg;
import gis.common.dataobject.YWell;
import gis.common.util.TableNameViewUtil;
import gxlu.afx.system.query.common.QueryConst;
import gxlu.afx.system.query.common.QueryExpr;
import gxlu.afx.system.query.common.QueryExprItem;
import gxlu.afx.system.query.common.QueryParam;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import TOPLink.Public.Expressions.Expression;

public class GisQueryClient {
	
	public static Vector exeGisQuery(YObjectInterface yObject,QueryParam queryParam){
        if ( yObject == null )
            return null;

        String strSQL = "" ;
//        if ( m_leftButtomTabPane.getSelectedIndex () == 0 ) //普通条件
//            strSQL = m_codeAndNameQueryTable.getQueryString ( obj , siteId ,regionId ) ;
//        else
//            strSQL = m_allQueryConditionsTable.getQueryString ( obj , siteId ,regionId ) ;
//            System.out.println(strSQL);
//        if ( m_chkbxNoSiteCode.isVisible () && m_chkbxNoSiteCode.isSelected () )
//        {
//            if ( ! ( obj instanceof YSite || obj instanceof YCqj  ||
//                     obj instanceof YECableAlarm
//					 ||obj instanceof YMNBTS||obj instanceof YMNDistributionSystem ||obj instanceof YDNAP||obj instanceof YDNAC
//					 ||obj instanceof YDDF||obj instanceof YRack	//added by xuzhiqiang 20100604 for 增加虚拟连接设备
//					))//Updated by qlj,for MR:移动网GIS显示,2009.3.20
//            {//obj instanceof YMNAP Updated by wuzhanghui,for MR:数据网GIS显示,2009.12.03
//                String strTemp = "" ;
//                if ( strSQL.indexOf ( "WHERE" ) == -1 )
//                    strTemp = " WHERE " ;
//                else
//                    strTemp = " AND " ;
//                if ( obj instanceof YCableSection )
//                    strSQL = strSQL + strTemp + "GISCABLESECTION.CABLEID IN(SELECT GISCABLE.ID FROM GISCABLE WHERE GISCABLE.SITECODE IS NULL)" ;
//                else if ( obj instanceof YECableSection )
//                    strSQL = strSQL + strTemp + "GISECABLESECTION.ECABLEID IN(SELECT GISECABLE.ID FROM GISECABLE WHERE GISECABLE.SITECODE IS NULL)" ;
//                else
//                    strSQL = strSQL + strTemp + obj.getTableName () +
//                             ".SITECODE IS NULL" ;
//            }
//        }
        
        String sql="SELECT DISTINCT V_GISCABLETERM.* FROM V_GISCABLETERM WHERE V_GISCABLETERM.TERMTYPE=6";
        return construct(yObject,sql,1,10);
	}
	
//	public static String getQueryString(){
//		return "";
//	}
	 public static Vector construct(YObjectInterface m_queryObject,
			String m_strSQL, int pageNum, int queryPageSize) {
		Vector vecResult = null;
		try {
			vecResult = ClientEnvironment.getCommonQueryIFC().queryPaged(
					ClientEnvironment.getUserKey(), m_queryObject, m_strSQL,
					pageNum, queryPageSize);
			// 如果查询未果，重新查询一遍:MR:JLOOX-2342
			if (vecResult == null || vecResult.size() == 0) {
				vecResult = ClientEnvironment.getCommonQueryIFC().queryPaged(
						ClientEnvironment.getUserKey(), m_queryObject,
						m_strSQL, pageNum, queryPageSize);
			}

			if (m_queryObject instanceof YOptic) {
				for (int j = 1; j < vecResult.size(); j++) {
					YOptic optic = (YOptic) vecResult.get(j);
					if (optic.length == 0) {
						Vector unPreFreeOpticRoutes = OpticUtil
								.getUnPreFreeOpticRoutes(optic);
						float length = 0;
						for (int i = 0; i < unPreFreeOpticRoutes.size(); i++) {
							YOpticRoute route = (YOpticRoute) unPreFreeOpticRoutes
									.get(i);
							length += route.getLogFiber().gettestLength();
						}
						optic.length = length;
					}
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			vecResult = new Vector();
		}
		if (vecResult.size() > 0)
			vecResult.remove(0);
		return vecResult;
	}
//	 
//	 public String getQueryString(YObjectInterface object, long siteId, long regionId) {
//		String strQuery = "";
//		if (object == null)
//			return "";
//		String ownerTableName = object.getSelectTableName().toUpperCase();
//		ownerTableName = TableNameViewUtil.getTableName(ownerTableName);
//		if (rowCount <= 0)
//			return "";
//		if (object instanceof YOptic) {
//			return getOpticQueryString(siteId, regionId);
//		}
//		if (object instanceof YLogFiber) {
//			return getLogFiberQueryString(siteId, regionId);
//		}
//
//		Vector vecConditions = getQueryConditions();
//		if (ownerTableName.equals("GISECABLESENSOR")
//				|| ownerTableName.equals("GISECABLEVALVE")) {
//			String strSql1 = "";
//			String strSql2 = "";
//			String strSql3 = "";
//			String strSql4 = "";
//
//			if (vecConditions.size() > 0) //设定了查询条件
//			{
//				if (regionId == -1) //局站或者本地网下
//				{
//					strSql1 = getQueryStringByListAndHost(strSql1,
//							ownerTableName, vecConditions.elements(),
//							"GISROOM", 0, siteId, " AND ");
//					strSql2 = getQueryStringByListAndHost(strSql2,
//							ownerTableName, vecConditions.elements(),
//							"GISDUCTBRAKE", 100000000, siteId, " AND ");
//					strSql3 = getQueryStringByListAndHost(strSql3,
//							ownerTableName, vecConditions.elements(),
//							"GISWELL", 200000000, siteId, " AND ");
//					strSql4 = getQueryStringByListAndHost(strSql4,
//							ownerTableName, vecConditions.elements(),
//							"GISPOLE", 300000000, siteId, " AND ");
//
//					if (!strSql1.equals("")) {
//						if (siteId != -1)
//							strQuery = strSql1 + " UNION " + strSql2
//									+ " UNION " + strSql3 + " UNION " + strSql4;
//						else
//							strQuery = strSql1;
//					}
//
//				} else //子区域下
//				{
//					strSql1 = getQueryStringByListAndHostInRegion(strSql1,
//							ownerTableName, vecConditions.elements(),
//							"GISROOM", 0, regionId, " AND ");
//					strSql2 = getQueryStringByListAndHostInRegion(strSql2,
//							ownerTableName, vecConditions.elements(),
//							"GISDUCTBRAKE", 100000000, regionId, " AND ");
//					strSql3 = getQueryStringByListAndHostInRegion(strSql3,
//							ownerTableName, vecConditions.elements(),
//							"GISWELL", 200000000, regionId, " AND ");
//					strSql4 = getQueryStringByListAndHostInRegion(strSql4,
//							ownerTableName, vecConditions.elements(),
//							"GISPOLE", 300000000, regionId, " AND ");
//					strQuery = strSql1 + " UNION " + strSql2 + " UNION "
//							+ strSql3 + " UNION " + strSql4;
//
//				}
//			}
//			if (strQuery.equals("")) //未设定查询条件
//			{
//				if (m_object != null) {
//					strQuery = "SELECT DISTINCT " + ownerTableName + ".* FROM ";
//					strQuery = strQuery + ownerTableName;
//					if (siteId != -1) {
//						strSql1 = strQuery + " ,GISROOM WHERE "
//								+ ownerTableName + ".HOSTID = "
//								+ " GISROOM.ID AND GISROOM.SITEID = " + siteId;
//						strSql2 = strQuery
//								+ " ,GISDUCTBRAKE WHERE "
//								+ "(("
//								+ ownerTableName
//								+ ".HOSTID = "
//								+ " GISDUCTBRAKE.ID + 100000000 AND GISDUCTBRAKE.ID>0) OR ("
//								+ ownerTableName
//								+ ".HOSTID = "
//								+ " GISDUCTBRAKE.ID - 100000000 AND GISDUCTBRAKE.ID<0))"
//								+ " AND GISDUCTBRAKE.SITEID = " + siteId;
//						strSql3 = strQuery
//								+ " ,GISWELL WHERE "
//								+ "(("
//								+ ownerTableName
//								+ ".HOSTID = "
//								+ " GISWELL.ID + 200000000 AND GISWELL.ID>0) OR ("
//								+ ownerTableName + ".HOSTID = "
//								+ " GISWELL.ID - 200000000 AND GISWELL.ID<0))"
//								+ " AND GISWELL.SITEID =" + siteId;
//						strSql4 = strQuery
//								+ " ,GISPOLE WHERE "
//								+ "(("
//								+ ownerTableName
//								+ ".HOSTID = "
//								+ " GISPOLE.ID + 300000000 AND GISPOLE.ID>0) OR ("
//								+ ownerTableName + ".HOSTID = "
//								+ " GISPOLE.ID - 300000000 AND GISPOLE.ID<0))"
//								+ " AND GISPOLE.SITEID =" + siteId;
//						strQuery = strSql1 + " UNION " + strSql2 + " UNION "
//								+ strSql3 + " UNION " + strSql4;
//					} else {
//						if (regionId != -1) {
//							strSql1 = strQuery
//									+ " ,GISROOM ,GISSITE,GISREGION WHERE "
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISROOM.ID AND GISROOM.SITEID =GISSITE.ID AND GISSITE.REGIONID=GISREGION.ID AND GISREGION.ID in (select region_tmp.childid from region_tmp where region_tmp.id="
//									+ regionId + ")";
//							strSql2 = strQuery
//									+ " ,GISDUCTBRAKE,GISSITE,GISREGION WHERE "
//									+ "(("
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISDUCTBRAKE.ID + 100000000 AND GISDUCTBRAKE.ID>0) OR ("
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISDUCTBRAKE.ID - 100000000 AND GISDUCTBRAKE.ID<0))"
//									+ " AND GISDUCTBRAKE.SITEID = GISSITE.ID AND GISSITE.REGIONID=GISREGION.ID AND GISREGION.ID in (select region_tmp.childid from region_tmp where region_tmp.id="
//									+ regionId + ")";
//							strSql3 = strQuery
//									+ " ,GISWELL,GISSITE,GISREGION WHERE "
//									+ "(("
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISWELL.ID + 200000000 AND GISWELL.ID>0) OR ("
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISWELL.ID - 200000000 AND GISWELL.ID<0))"
//									+ " AND GISWELL.SITEID = GISSITE.ID AND GISSITE.REGIONID=GISREGION.ID AND GISREGION.ID in (select region_tmp.childid from region_tmp where region_tmp.id="
//									+ regionId + ")";
//							strSql4 = strQuery
//									+ " ,GISPOLE,GISSITE,GISREGION WHERE "
//									+ "(("
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISPOLE.ID + 300000000 AND GISPOLE.ID>0) OR ("
//									+ ownerTableName
//									+ ".HOSTID = "
//									+ " GISPOLE.ID - 300000000 AND GISPOLE.ID<0))"
//									+ " AND GISPOLE.SITEID = GISSITE.ID AND GISSITE.REGIONID=GISREGION.ID AND GISREGION.ID in (select region_tmp.childid from region_tmp where region_tmp.id="
//									+ regionId + ")";
//							strQuery = strSql1 + " UNION " + strSql2
//									+ " UNION " + strSql3 + " UNION " + strSql4;
//						}
//					}
//
//				}
//			}
//
//		} else {
//			if (vecConditions.size() > 0) {
//				Enumeration em = vecConditions.elements();
//				if (regionId == -1)
//					strQuery = getQueryStringByList(strQuery, ownerTableName,
//							em, siteId, " AND ");
//				else
//					//增加在用户选择区域的情况下的查询
//					strQuery = getQueryStringByListInRegion(strQuery,
//							ownerTableName, em, regionId, " AND ");
//
//			}
//
//			if (strQuery.equals("")) {
//				if (m_object != null) {
//					strQuery = "SELECT DISTINCT " + ownerTableName + ".* FROM ";
//					strQuery = strQuery + ownerTableName;
//					if (siteId != -1) {
//						if (ownerTableName.equals("GISCQJ")
//								|| ownerTableName.equals("GISECABLEALARM")
//								|| ownerTableName.equals("GISECABLESECTION")
//								|| ownerTableName.equals("GISCABLESECTION")) {
//							strQuery = strQuery
//									+ getExtTableName(ownerTableName)
//									+ " WHERE "
//									+ getExtTableCondition(ownerTableName,
//											siteId);
//						} else if (ownerTableName.equals("GISSITE")) {
//							strQuery = strQuery + " WHERE " + ownerTableName
//									+ ".ID = " + siteId;
//						} else {
//							strQuery = strQuery + " WHERE " + ownerTableName
//									+ ".SITEID =" + siteId;
//						}
//					} else {
//						if (regionId != -1) {
//							if (ownerTableName.equals("GISSITE")) {
//								strQuery = strQuery
//										+ " WHERE "
//										+ ownerTableName
//										+ ".REGIONID in (select region_tmp.childid from region_tmp where region_tmp.id="
//										+ regionId + ") AND GISSITE.ISGIS=1";
//							} else {
//								strQuery = strQuery
//										+ getExtTableNameInRegion(ownerTableName)
//										+ " WHERE "
//										+ getExtTableConditionInRegion(ownerTableName, regionId);
//							}
//						}
//
//					}
//				}
//			} else {
//				if (siteId != -1) {
//					if (ownerTableName.equals("GISSITE"))
//						strQuery = strQuery + " AND " + ownerTableName
//								+ ".ID =" + siteId;
//					else if (ownerTableName.equals("GISCQJ")
//							|| ownerTableName.equals("GISECABLEALARM")
//							|| ownerTableName.equals("GISECABLESECTION")
//							|| ownerTableName.equals("GISCABLESECTION")) {
//						; //CQJ/ALARM/光缆段和电缆段的“局站”条件已经加过了，就不加了。
//					} else
//						strQuery = strQuery + " AND " + ownerTableName
//								+ ".SITEID = " + siteId;
//				}
//			}
//		}
//		String connectString = "";
//		if (strQuery.indexOf("WHERE") == -1)
//			connectString = " WHERE ";
//		else
//			connectString = " AND ";
//		if (object instanceof YMDF)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.ECABLETERM_TYPE_MDF;
//		else if (object instanceof YJJX)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.ECABLETERM_TYPE_JJX;
//		else if (object instanceof YFXH)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.ECABLETERM_TYPE_FXH;
//		else if (object instanceof YDT)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.ECABLETERM_TYPE_DT;
//		else if (object instanceof YECableStore)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.ECABLETERM_TYPE_STORE;
//		else if (object instanceof YODF)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.CABLETERM_TYPE_ODF;
//		else if (object instanceof YOJJX)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.CABLETERM_TYPE_OJJX;
//		else if (object instanceof YPOS) { //added by nyx, 2009-07-15, 增加分光器
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.CABLETERM_TYPE_POS;
//		} else if (object instanceof YOFXX) {
//			YOFXX ofxx = (YOFXX) object;
//			if (ofxx.termType == GisConst.CABLETERM_TYPE_OFXX)
//				strQuery = strQuery + connectString + ownerTableName
//						+ ".TERMTYPE=" + GisConst.CABLETERM_TYPE_OFXX;
//			else if (ofxx.termType == GisConst.CABLETERM_TYPE_TERMBOX) //光终端盒
//				strQuery = strQuery + connectString + ownerTableName
//						+ ".TERMTYPE=" + GisConst.CABLETERM_TYPE_TERMBOX;
//		} else if (object instanceof YOT)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.CABLETERM_TYPE_OT;
//		else if (object instanceof YCableStore)
//			strQuery = strQuery + connectString + ownerTableName + ".TERMTYPE="
//					+ GisConst.CABLETERM_TYPE_STORE;
//
//		else if (object instanceof YPole) {
//			strQuery = strQuery + connectString + ownerTableName
//					+ ".ISTEMPLATE=" + GisConst.POLE_ISTEMPLATE_NO;
//			YPole pole = (YPole) object;
//			byte category = pole.getCategory();
//			strQuery = strQuery + " AND " + ownerTableName + ".CATEGORY="
//					+ category;
//		} else if (object instanceof YWell)
//			strQuery = strQuery + connectString + ownerTableName
//					+ ".ISTEMPLATE=" + GisConst.WELL_ISTEMPLATE_NO;
//		if (ClientEnvironment.getIsStandalone()) {
//			if (!strQuery.equals("")) {
//				if (strQuery.indexOf("WHERE") == -1)
//					strQuery += " WHERE " + ownerTableName + ".MODIFYSTATUS<>2";
//				else
//					strQuery += " AND " + ownerTableName + ".MODIFYSTATUS<>2";
//			}
//		}
//		return strQuery;
//	}
//	 private String getOpticQueryString(long siteId, long regionId) {
//		Vector vecConditions = getQueryConditions();
//		String strTableName = "VW_GISOPTIC";
//		if (siteId != -1)
//			strTableName = "VW_OPTIC_SITE";
//		else if (regionId != -1)
//			strTableName = "VW_OPTIC_REGION";
//		String strQuery = "SELECT * FROM " + strTableName;
//
//		for (int i = 0, size = vecConditions.size(); i < size; i++) {
//			String strCur = (String) vecConditions.get(i);
//			if (i == 0) {
//				strQuery = strQuery + " WHERE ";
//				if (strCur.indexOf("LOWER") != -1)
//					strQuery = strQuery + "LOWER(" + strTableName + "."
//							+ strCur + "";
//				else
//					strQuery = strQuery + strTableName + "." + strCur + " ";
//			} else {
//				if (strCur.indexOf("LOWER") != -1)
//					strQuery = strQuery + " AND" + " LOWER(" + strTableName
//							+ "." + strCur + "";
//				else
//					strQuery = strQuery + " AND" + " " + strTableName + "."
//							+ strCur + " ";
//			}
//		}
//
//		if (siteId != -1) {
//			if (strQuery.indexOf("WHERE") == -1)
//				strQuery = strQuery + " WHERE ";
//			else
//				strQuery = strQuery + " AND ";
//			//
//			strQuery = strQuery + "VW_OPTIC_SITE.SITEID=" + siteId;
//		} else if (regionId != -1) {
//			if (strQuery.indexOf("WHERE") == -1)
//				strQuery = strQuery + " WHERE ";
//			else
//				strQuery = strQuery + " AND ";
//			//
//			strQuery = strQuery
//					+ "VW_OPTIC_REGION.REGIONID in (select childid from region_tmp where id="
//					+ regionId + ")";
//		}
//
//		if (strQuery.indexOf("WHERE") == -1)
//			strQuery = strQuery + " WHERE ";
//		else
//			strQuery = strQuery + " AND ";
//		//
//		strQuery = strQuery + strTableName + ".DOMAIN_='"
//				+ ClientEnvironment.DOMAIN
//				+ "'";
//
//		return strQuery;
//	}
//	private String getLogFiberQueryString(long siteId, long regionId) {
//		Vector vecConditions = getQueryConditions();
//		String strTableName = "GISLOGFIBER";
//		if (siteId != -1)
//			strTableName = "VW_LOGFIBER_SITE";
//		else if (regionId != -1)
//			strTableName = "VW_LOGFIBER_REGION";
//		String strQuery = "SELECT * FROM " + strTableName;
//
//		for (int i = 0, size = vecConditions.size(); i < size; i++) {
//			String strCur = (String) vecConditions.get(i);
//			if (i == 0) {
//				strQuery = strQuery + " WHERE ";
//				if (strCur.indexOf("LOWER") != -1)
//					strQuery = strQuery + "LOWER(" + strTableName + "."
//							+ strCur + "";
//				else
//					strQuery = strQuery + strTableName + "." + strCur + " ";
//			} else {
//				if (strCur.indexOf("LOWER") != -1)
//					strQuery = strQuery + " AND" + " LOWER(" + strTableName
//							+ "." + strCur + "";
//				else
//					strQuery = strQuery + " AND" + " " + strTableName + "."
//							+ strCur + " ";
//			}
//		}
//
//		if (siteId != -1) {
//			if (strQuery.indexOf("WHERE") == -1)
//				strQuery = strQuery + " WHERE ";
//			else
//				strQuery = strQuery + " AND ";
//			//
//			strQuery = strQuery + "VW_LOGFIBER_SITE.SITEID=" + siteId;
//		} else if (regionId != -1) {
//			if (strQuery.indexOf("WHERE") == -1)
//				strQuery = strQuery + " WHERE ";
//			else
//				strQuery = strQuery + " AND ";
//			//
//			strQuery = strQuery
//					+ "VW_LOGFIBER_REGION.REGIONID in (select childid from region_tmp where id="
//					+ regionId + ")";
//		}
//
//		if (strQuery.indexOf("WHERE") == -1)
//			strQuery = strQuery + " WHERE ";
//		else
//			strQuery = strQuery + " AND ";
//		//
//		strQuery = strQuery + strTableName + ".DOMAIN_='"
//				+ ClientEnvironment.DOMAIN
//				+ "'";
//
//		return strQuery;
//
//	}
//	 private String getQueryStringByListAndHost(String strQuery,
//			String ownerTableName, Enumeration em, String extTableName,
//			long idBase, long siteId, String operation) {
//		while (em.hasMoreElements()) {
//			Object objCur = em.nextElement();
//			String strCur = (String) objCur;
//			if (objCur != null) {
//				if (strQuery.equals("")) //
//				{
//					strQuery = "SELECT " + ownerTableName + ".* FROM "
//							+ ownerTableName;
//					if (siteId != -1) // 需要把站点作为查询的参数
//					{
//						strQuery = strQuery + " , " + extTableName;
//					}
//					strQuery = strQuery + "  WHERE ";
//					if (siteId != -1) {
//						strQuery = strQuery + "((" + ownerTableName
//								+ ".HOSTID = " + extTableName + ".ID + "
//								+ idBase + " AND " + extTableName
//								+ ".ID>0) OR (" + ownerTableName + ".HOSTID = "
//								+ extTableName + ".ID - " + idBase + " AND "
//								+ extTableName + ".ID<0))" + " AND "
//								+ extTableName + ".SITEID = " + siteId
//								+ " AND ";
//					}
//					if (strCur.indexOf("LOWER") != -1)
//						strQuery = strQuery + "LOWER(" + ownerTableName + "."
//								+ strCur + "";
//					else
//						strQuery = strQuery + ownerTableName + "." + strCur
//								+ " ";
//				} else {
//					if (strCur.indexOf("LOWER") != -1)
//						strQuery = strQuery + operation + " LOWER("
//								+ ownerTableName + "." + strCur + "";
//					else
//						strQuery = strQuery + operation + " " + ownerTableName
//								+ "." + strCur + " ";
//				}
//			}
//		}
//		return strQuery;
//	}
//	 private String getQueryStringByList ( String strQuery ,
//             String ownerTableName ,
//             Enumeration em , long siteId ,
//             String operation )
//	 {
//		while (em.hasMoreElements()) {
//			Object objCur = em.nextElement();
//			String strCur = (String) objCur;
//			if (objCur != null) {
//				if (strQuery.equals("")) // while循环的第一次
//				{
//					strQuery = "SELECT " + ownerTableName + ".* FROM "
//							+ ownerTableName;
//					if (siteId != -1 && !ownerTableName.equals("GISSITE")) {
//						strQuery = strQuery + getExtTableName(ownerTableName);
//					}
//					strQuery = strQuery + "  WHERE ";
//					if (siteId != -1 && (!ownerTableName.equals("GISSITE"))) {
//						String s = getExtTableCondition(ownerTableName, siteId);
//						if (s.trim().length() != 0)
//							strQuery = strQuery + s + " AND ";
//					}
//					if (strCur.indexOf("LOWER") != -1)
//						strQuery = strQuery + "LOWER(" + ownerTableName + "."
//								+ strCur + "";
//					else
//						strQuery = strQuery + ownerTableName + "." + strCur
//								+ " ";
//				} else {
//					if (strCur.indexOf("LOWER") != -1)
//						strQuery = strQuery + operation + " LOWER("
//								+ ownerTableName + "." + strCur + "";
//					else
//						strQuery = strQuery + operation + " " + ownerTableName
//								+ "." + strCur + " ";
//				}
//			}
//		}
//		return strQuery;
//	}
//
//	private String getQueryStringByListInRegion(String strQuery,
//			String ownerTableName, Enumeration em, long regionId,
//			String operation) {
//		while (em.hasMoreElements()) {
//			Object objCur = em.nextElement();
//			String strCur = (String) objCur;
//			if (objCur != null) {
//				if (strQuery.equals("")) // while循环的第一次
//				{
//					// UPDATED BY LIMIN,20091016.
//					strQuery = "SELECT DISTINCT " + ownerTableName + ".* FROM "
//							+ ownerTableName;
//					strQuery = strQuery
//							+ getExtTableNameInRegion(ownerTableName);
//
//					strQuery = strQuery + " WHERE ";
//					String s = getExtTableConditionInRegion(ownerTableName,
//							regionId);
//
//					// ADDED BY LIMIN,20091016,START.
//					s = s + " AND GISSITE.ISGIS=1";
//					// ADDED BY LIMIN,20091016,END.
//					// updated by limin,20091016,end.
//					if (s.trim().length() != 0)
//						strQuery = strQuery + s + " AND ";
//
//					if (strCur.indexOf("LOWER") != -1)
//						strQuery = strQuery + "LOWER(" + ownerTableName + "."
//								+ strCur + "";
//					else
//						strQuery = strQuery + ownerTableName + "." + strCur
//								+ " ";
//				} else {
//					if (strCur.indexOf("LOWER") != -1)
//						strQuery = strQuery + operation + " LOWER("
//								+ ownerTableName + "." + strCur + "";
//					else
//						strQuery = strQuery + operation + " " + ownerTableName
//								+ "." + strCur + " ";
//				}
//			}
//		}
//		return strQuery;
//	}
//
//	private String getExtTableName(String ownerTableName) {
//		if (ownerTableName.equals("GISCQJ")
//				|| ownerTableName.equals("GISECABLEALARM")) {
//			return " ,GISDUCTBRAKE ";
//		} else if (ownerTableName.equals("GISECABLESECTION")) {
//			return " ,GISECABLE ";
//		} else if (ownerTableName.equals("GISCABLESECTION")) {
//			return " ,GISCABLE ";
//		}
//
//		return " ";
//	}
//	private String getQueryStringByListAndHostInRegion ( String strQuery ,
//            String ownerTableName , Enumeration em , String extTableName ,
//            long idBase ,
//            long regionId , String operation )
//    {
//        while ( em.hasMoreElements () )
//        {
//            Object objCur = em.nextElement () ;
//            String strCur = ( String ) objCur ;
//            if ( objCur != null )
//            {
//                if ( strQuery.equals ( "" ) ) //first time
//                {
//                    strQuery = "SELECT " + ownerTableName + ".* FROM " +
//                               ownerTableName ;
//                    if ( regionId != -1 ) //需要把站点作为查询的参数
//                    {
//                        strQuery = strQuery + " , " + extTableName +
//                                   ",GISSITE,GISREGION" ;
//                    }
//                    strQuery = strQuery + "  WHERE " ;
//                    if ( regionId != -1 )
//                    {
//						strQuery = strQuery + "((" + ownerTableName + ".HOSTID = "
//								   + extTableName + ".ID + " + idBase + " AND "
//								   + extTableName + ".ID>0) OR ("
//								   + ownerTableName + ".HOSTID = "
//								   + extTableName + ".ID - " + idBase + " AND "
//								   + extTableName + ".ID<0))"
//								   + " AND "
//                                   + extTableName +
//                                   ".SITEID = GISSITE.ID AND GISSITE.REGIONID=GISREGION.ID "
//                                   + "AND GISREGION.ID in (select region_tmp.childid from region_tmp where region_tmp.id=" + regionId +
//                                   ") AND " ;
//                    }
//                    if ( strCur.indexOf ( "LOWER" ) != -1 )
//                        strQuery = strQuery + "LOWER(" + ownerTableName + "." +
//                                   strCur + "" ;
//                    else
//                        strQuery = strQuery + ownerTableName + "." + strCur +
//                                   " " ;
//                }
//                else
//                {
//                    if ( strCur.indexOf ( "LOWER" ) != -1 )
//                        strQuery = strQuery + operation + " LOWER(" +
//                                   ownerTableName + "." + strCur + "" ;
//                    else
//                        strQuery = strQuery + operation + " " + ownerTableName +
//                                   "." + strCur + " " ;
//                }
//            }
//        }
//        return strQuery ;
//    }
//	 private String getExtTableCondition(String ownerTableName, long siteID) {
//		if (ownerTableName.equals("GISCQJ")
//				|| ownerTableName.equals("GISECABLEALARM")) {
//			return ownerTableName
//					+ ".DUCTBRAKEID = GISDUCTBRAKE.ID AND GISDUCTBRAKE.SITEID ="
//					+ siteID;
//		} else if (ownerTableName.equals("GISECABLESECTION")) {
//			return ownerTableName
//					+ ".ECABLEID=GISECABLE.ID AND GISECABLE.SITEID=" + siteID;
//		} else if (ownerTableName.equals("GISCABLESECTION")) {
//			return ownerTableName + ".CABLEID=GISCABLE.ID AND GISCABLE.SITEID="
//					+ siteID;
//		}
//		return " ";
//	}
//
//	private String getExtTableNameInRegion(String ownerTableName) {
//		if (ownerTableName.equals("GISCQJ")
//				|| ownerTableName.equals("GISECABLEALARM")) {
//			return ",GISDUCTBRAKE,GISSITE,GISREGION";
//		} else if (ownerTableName.equals("GISECABLESECTION")) {
//			return ",GISECABLE,GISSITE,GISREGION";
//		} else if (ownerTableName.equals("GISCABLESECTION")) {
//			return ",GISCABLE,GISSITE,GISREGION";
//		} else if (ownerTableName.equals("GISSITE")) {
//			return ",GISREGION";
//		}
//
//		return ",GISSITE,GISREGION";
//	}
//	private String getExtTableConditionInRegion(String ownerTableName,
//			long regionID) {
//		if (ownerTableName.equals("GISCQJ")
//				|| ownerTableName.equals("GISECABLEALARM")) {
//			return ownerTableName
//					+ ".DUCTBRAKEID = GISDUCTBRAKE.ID AND GISDUCTBRAKE.SITEID=GISSITE.ID AND "
//					+ "GISSITE.REGIONID in (select region_tmp.childid from region_tmp where region_tmp.id="
//					+ regionID + ")";
//		} else if (ownerTableName.equals("GISECABLESECTION")) {
//			return ownerTableName
//					+ ".ECABLEID=GISECABLE.ID AND GISECABLE.SITEID=GISSITE.ID AND "
//					+ "GISSITE.REGIONID in (select region_tmp.childid from region_tmp where region_tmp.id="
//					+ regionID + ")";
//		} else if (ownerTableName.equals("GISCABLESECTION")) {
//			return ownerTableName
//					+ ".CABLEID=GISCABLE.ID AND GISCABLE.SITEID=GISSITE.ID AND "
//					+ "GISSITE.REGIONID in (select region_tmp.childid from region_tmp where region_tmp.id="
//					+ regionID + ")";
//		} else if (ownerTableName.equals("GISSITE")) {
//			return ownerTableName
//					+ ".REGIONID in (select region_tmp.childid from region_tmp where region_tmp.id="
//					+ regionID + ")";
//		}
//
//		return ownerTableName
//				+ ".SITEID=GISSITE.ID AND "
//				+ "GISSITE.REGIONID in (select region_tmp.childid from region_tmp where region_tmp.id="
//				+ regionID + ")";
//	}
//	
//	private Vector getQueryConditions ()
//    {
//        Vector vecQueryConditions = new Vector () ;
//        String strQueryConditions = "" ;
//        String propertyName = null ;
//        JComboBox comboxOperator = null ;
//        for ( int i = 0 ; i < m_vecProperties.size () ; i++ )
//        {
//            //得到属性名称
//            propertyName = ( String ) m_vecProperties.elementAt ( i ) ;
//            //得到操作符的JComboBox
//            comboxOperator = ( JComboBox ) m_htblOperatorCombox.get (propertyName ) ;
//            Object compValue = m_htblValues.get ( propertyName ) ;
//            if ( comboxOperator == null || compValue == null )
//                continue ;
//            if ( comboxOperator.getSelectedIndex () == -1 )
//                continue ;
//
//            strQueryConditions = ( String ) m_htblProp.get ( propertyName )
//                                 + " " +
//                                 ( String ) m_htblOperators.get ( comboxOperator.
//                    getSelectedItem () ) + " " ;
//
//            if ( compValue instanceof JComboBox )
//            {
//                JComboBox combxValue = ( JComboBox ) compValue ;
//                if ( combxValue.getSelectedIndex () == -1 )
//                    continue ;
//
////              Added by nyx,for MR:NMBF50_GIS20080407-36 ,2009-07-08,Start
//                byte dictionaryStr = -1;
//                String strField = (String)m_htblProp.get(propertyName);
//                if ( m_object.getObjectType () == GisConst.OBJECTTYPE_ODF ){	//added by xuzhiqiang 20091017
//                	if("CONNECTORTYPE".equalsIgnoreCase(strField))		//适配类型 added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GisCableTerm", "ConnectorType", combxValue.getSelectedItem() ) ;
//					}
//					else if("RACKTYPE".equalsIgnoreCase(strField))		//架类型 added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GisCableTerm", "RACKTYPE", combxValue.getSelectedItem() ) ;
//					}
//					else if("SHELFPL".equalsIgnoreCase(strField))		//框的排列方式 added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GisCableTerm", "SHELFPL", combxValue.getSelectedItem() ) ;
//					}
//					else if("LONGORLOCAL".equalsIgnoreCase(strField))		//长途本地属性 added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GisCableTerm", "LONGORLOCAL", combxValue.getSelectedItem() ) ;
//					}
//					else if("SPECIALITY".equalsIgnoreCase(strField))		//所属专业类别 added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GisCableTerm", "ConnectorType", combxValue.getSelectedItem() ) ;
//					}
//					else if("INSERVICE".equalsIgnoreCase(strField))		//使用状态 added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GisCableTerm", "STRUCTSTATUS", combxValue.getSelectedItem() ) ;
//					}
//                }else if( m_object.getObjectType () == GisConst.OBJECTTYPE_SITE ){
//                	if (strField.equals ( "LongOrLocal" )){	//长途本地属性
//                		dictionaryStr = GisDictionary.getValue ( "GisSite" , "LONGORLOCAL",  combxValue.getSelectedItem() ) ;
//                	}
//                }
//                
//                if(m_object instanceof YSite			//如果是公用部分的属性
//                || m_object instanceof YWell
//                || m_object instanceof YPole
//                || m_object instanceof YCqj
//                || m_object instanceof YECableAlarm
//                || m_object instanceof YDuctSeg
//                || m_object instanceof YSlingSeg
//                || m_object instanceof YECableTerm
//                || m_object instanceof YECable
//                || m_object instanceof YCableTerm
//                || m_object instanceof YCable
//                || m_object instanceof YRoom
//                || m_object instanceof YLogFiber
//                || m_object instanceof YCableSection
//                || m_object instanceof YCDB
//                || m_object instanceof YOptic			//如果是光路
//                ){
//                	if("ASSETATTRIBUTION".equalsIgnoreCase(strField))
//					{
//                		dictionaryStr = GisDictionary.getValue ( "GLOBAL", "ASSETATTRIBUTION", combxValue.getSelectedItem() ) ;
//					}
//					else if("MAINTENDUTY".equalsIgnoreCase(strField))
//					{
//						dictionaryStr = GisDictionary.getValue ( "GLOBAL", "MAINTENDUTY", combxValue.getSelectedItem() ) ;
//					}
//					else if("USEWAY".equalsIgnoreCase(strField))
//					{
//						dictionaryStr = GisDictionary.getValue ( "GLOBAL", "USEWAY", combxValue.getSelectedItem() ) ;
//					}
//					else if("MAINTAINMETHOD".equalsIgnoreCase(strField)
//						  || "MAINTAINWAY".equalsIgnoreCase(strField))	//added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GLOBAL", "MAINTAINMETHOD", combxValue.getSelectedItem() ) ;
//					}
//					else if("PROPERTYTYPE".equalsIgnoreCase(strField)
//						  || "ASSETTYPE".equalsIgnoreCase(strField))	//added by xuzhiqiang 20091017
//					{
//						dictionaryStr = GisDictionary.getValue ( "GLOBAL", "PROPERTYTYPE", combxValue.getSelectedItem() ) ;
//					}
//					else if("isMain".equalsIgnoreCase(strField) 
//						  || "isEpon".equalsIgnoreCase(strField)
//						  || "ISCITY".equalsIgnoreCase(strField))//added by nyx, 2009-08-21, 增加是否农村局站条件
//					{
//						dictionaryStr = GisDictionary.getValue ( "GLOBAL", "BOOLEAN", combxValue.getSelectedItem() ) ;
//					}
//					else//只运算其中6个公用字段。私有字段依然是getTableName
//					{
//						dictionaryStr = GisDictionary.getValue ( m_object.getTableName (), strField, combxValue.getSelectedItem () ) ;
//					}
//                }else{
//                	dictionaryStr = GisDictionary.getValue ( m_object.getTableName (), strField, combxValue.getSelectedItem () ) ;
//                }
//
////              Added by nyx,for MR:NMBF50_GIS20080407-36 ,2009-07-08,End
//
//                strQueryConditions = strQueryConditions + dictionaryStr;
//            }
//            //此判断条件必须放在JTextField的前面,因为JFromattedTextField继承自JTextField
//            else if ( compValue instanceof gis.client.mapui.JFormattedTextField )
//            {
//                JFormattedTextField ftxtValue = ( JFormattedTextField )
//                                                compValue ;
//                if ( ftxtValue.getText ().trim ().equals ( "" ) )
//                    continue ;
//                Object object = m_htblAttribute.get ( propertyName ) ;
//                if ( object != null )
//                {
//                    if ( object instanceof Date )
//                    {
//                        String str = ftxtValue.getText () ;
//                        if ( ClientEnvironment.getDbType ().equalsIgnoreCase (
//                                GisConst.DB_ORACLE ) )
//                        {
//                            strQueryConditions = strQueryConditions +
//                                                 " TO_DATE('" + str +
//                                                 "','YYYYMMDD')" ;
//                        }
//                        else if ( ClientEnvironment.getDbType ().
//                                  equalsIgnoreCase ( GisConst.DB_SYBASE ) )
//                        {
//                            strQueryConditions = strQueryConditions +
//                                                 " convert('datetime','" + str +
//                                                 "')" ;
//                        }
//                    }
//                    else
//                    {
//                        strQueryConditions = strQueryConditions +
//                                             ftxtValue.getText () ;
//                    }
//                }
//            }
//            else if ( compValue instanceof JTextField )
//            {
//                JTextField txtValue = ( JTextField ) compValue ;
//                if ( txtValue.getText ().trim ().equals ( "" ) )
//                    continue ;
//                if ( comboxOperator.getSelectedItem ().equals ( "精确查找" ) )
//                {
//                    strQueryConditions = strQueryConditions + "'" +
//                                         ( String ) txtValue.getText () + "'" ;
//                }
//                else if ( comboxOperator.getSelectedItem ().equals ( "模糊查找" ) )
//                {
//                    strQueryConditions = strQueryConditions + "LOWER('" + "%" +
//                                         ( String ) txtValue.getText () + "%" +
//                                         "')" ;
//                }
//            }
//
//            else if ( compValue instanceof JCheckBox )
//            {
//                JCheckBox chbxValue = ( JCheckBox ) compValue ;
//                if ( !chbxValue.isSelected () )
//                    continue ;
//                strQueryConditions = strQueryConditions + "1" ;
//            }
//
//            vecQueryConditions.addElement ( strQueryConditions ) ;
//        }
//        return vecQueryConditions ;
//
//    }
	 
	 
//	 
//	 public Expression getExpression(QueryExpr expr) {
//		if (expr == null)
//			return null;
//
//		Vector exp = expr.getExpr();
//		if ((exp == null) || (exp.size() == 0))
//			return null;
//		Expression result = null;
//		if (exp.size() > 0) //the first item
//		{
//			switch (((QueryExprItem) exp.elementAt(0)).getOperator()) {
//			case QueryConst.OPERATOR_get:
//				result = expBuilder.get((String) ((QueryExprItem) exp.elementAt(0)).getOprand());
//				break;
//			case QueryConst.OPERATOR_anyOf:
//				result = expBuilder.anyOf((String) ((QueryExprItem) exp.elementAt(0)).getOprand());
//				break;
//			case QueryConst.OPERATOR_anyOfAllowingNone:
//				result = expBuilder
//						.anyOfAllowingNone((String) ((QueryExprItem) exp.elementAt(0)).getOprand());
//				break;
//			case QueryConst.OPERATOR_getAllowingNull:
//				result = expBuilder
//						.getAllowingNull((String) ((QueryExprItem) exp.elementAt(0)).getOprand());
//				break;
//			case QueryConst.OPERATOR_appendSQL:
//				result = expBuilder.appendSQL((String) ((QueryExprItem) exp.elementAt(0)).getOprand());
//				break;
//			case QueryConst.OPERATOR_twist:
//				result = expBuilder.twist(
//						getExpression((QueryExpr) ((QueryExprItem) exp.elementAt(0)).getOprand()),
//						getExpression((QueryExpr) ((QueryExprItem) exp.elementAt(1)).getOprand()));
//				break;
//			default:
//				return expError();
//			}
//		}
//		for (int i = 1; i < exp.size(); i++) {
//			QueryExprItem item = (QueryExprItem) exp.elementAt(i);
//			if (item.getOperator() == QueryConst.OPERATOR_twist && i == 1)
//				continue;
//			if (item.getOprand() instanceof QueryExpr) //the Oprand is a QueryExpr object
//				item.setOprand(getExpression((QueryExpr) item.getOprand())); //recurse to get Expression
//			if ((item.getOperator() == QueryConst.OPERATOR_between)
//					|| (item.getOperator() == QueryConst.OPERATOR_notBetween)) {
//				i++;
//				QueryExprItem item2 = (QueryExprItem) exp.elementAt(i);
//				result = getExpressionFromItem(result, item, item2);
//			} else
//				result = getExpressionFromItem(result, item);
//		}
//		return result;
//	}
//	  private Expression getExpressionFromItem(Expression r, QueryExprItem item1,
//			QueryExprItem item2) {
//		switch (item1.getOperator()) {
//		case QueryConst.OPERATOR_between:
//			return r.between(item1.getOprand(), item2.getOprand());
//		case QueryConst.OPERATOR_notBetween:
//			return r.notBetween(item1.getOprand(), item2.getOprand());
//		default:
//			return expError();
//		}
//	 }
//	 private Expression getExpressionFromItem(Expression r, QueryExprItem item) {
//		switch (item.getOperator()) {
//		case QueryConst.OPERATOR_get:
//			return r.get((String) item.getOprand());
//		case QueryConst.OPERATOR_anyOf:
//			return r.anyOf((String) item.getOprand());
//		case QueryConst.OPERATOR_anyOfAllowingNone:
//			return r.anyOfAllowingNone((String) item.getOprand());
//		case QueryConst.OPERATOR_appendSQL:
//			return r.appendSQL((String) item.getOprand());
//		case QueryConst.OPERATOR_toUppercase:
//			return r.toUpperCase();
//		case QueryConst.OPERATOR_toLowercase:
//			return r.toLowerCase();
//		case QueryConst.OPERATOR_and:
//			return r.and((Expression) item.getOprand());
//		case QueryConst.OPERATOR_or:
//			return r.or((Expression) item.getOprand());
//		case QueryConst.OPERATOR_equal:
//			return r.equal(item.getOprand());
//		case QueryConst.OPERATOR_notEqual:
//			return r.notEqual(item.getOprand());
//		case QueryConst.OPERATOR_greaterThan:
//			return r.greaterThan(item.getOprand());
//		case QueryConst.OPERATOR_greaterThanEqual:
//			return r.greaterThanEqual(item.getOprand());
//		case QueryConst.OPERATOR_lessThan:
//			return r.lessThan(item.getOprand());
//		case QueryConst.OPERATOR_lessThanEqual:
//			return r.lessThanEqual(item.getOprand());
//		case QueryConst.OPERATOR_in:
//			return (item.getOprand() instanceof Vector) ? r.in((Vector) item.getOprand()) : r.in((Expression) item.getOprand());
//		case QueryConst.OPERATOR_notIn:
//			return r.notIn((Vector) item.getOprand());
//		case QueryConst.OPERATOR_like:
//			return r.like((String) item.getOprand());
//		case QueryConst.OPERATOR_notLike:
//			return r.notLike((String) item.getOprand());
//		case QueryConst.OPERATOR_likeIgnoreCase:
//			return r.likeIgnoreCase((String) item.getOprand());
//		case QueryConst.OPERATOR_isNull:
//			return r.isNull();
//		case QueryConst.OPERATOR_notNull:
//			return r.notNull();
//		case QueryConst.OPERATOR_getAllowingNull:
//			return r.getAllowingNull((String) item.getOprand());
//		default:
//			return expError();
//		}
//	}
}


package gxlu.ietools.basic.system.util;

public class PropertyConstvalue
{
  public static final String AUDIT_OPERATIONTYPE_ADD = "NEW";
  public static final String AUDIT_OPERATIONTYPE_UPDATE = "MODIFY";
  public static final String AUDIT_OPERATIONTYPE_DELETE = "DELETE";
  public static final String AUDIT_OPERATIONTYPE_OTHER = "未知";
  //数据网中
  public final static byte PERSIST_TYPE_NOCHANGE = 1;
  public final static byte PERSIST_TYPE_ADD = 2;
  public final static byte PERSIST_TYPE_UPDATE = 3;
  public final static byte PERSIST_TYPE_DELETE = 4;
  
  //审记
  public static final byte DNSUBNEAUDITS_OPERATIONTYPE_NEW = 0; //新增
  public static final byte DNSUBNEAUDITS_OPERATIONTYPE_MODIFY = 1; //修改
  public static final byte DNSUBNEAUDITS_OPERATIONTYPE_DELETE = 2; //删除
  
  //移动网中审记
  public final static int MNGLOBAL_AUDITSOPERATIONTYPE_NEW = 1; //新建
  public final static int MNGLOBAL_AUDITSOPERATIONTYPE_MODIFY = 2; //修改
  public final static int MNGLOBAL_AUDITSOPERATIONTYPE_DELETE = 3; //删除
  
  public static final String ASSRT_DEFAULT_COSTCENTER = "生产用";
  public static final String ASSRT_DEFAULT_STATUS = "在用";
  public static final int GLOBAL_ISTEMPLATE_NOMODULE = 0;
  public static final int GLOBAL_ISTEMPLATE_MODULE = 1;
  public static final int GLOBAL_ISTEMPLATE_COMPNENT = 2;
  public static final byte DNDEVICE_ROLES_PE = 1;
  public static final byte DNDEVICE_ROLES_CE = 2;
  public static final byte DNDEVICE_ROLES_ATM = 3;
  public static final byte DNDEVICE_ROLES_DDN = 4;
  public static final byte DNDEVICE_ROLES_FR = 5;
  public static final byte DNDEVICE_ROLES_ROUTER = 6;
  public static final byte DNDEVICE_ROLES_TWOSWITCH = 7;
  public static final byte DNDEVICE_ROLES_THREESWITCH = 8;
  public static final byte DNDEVICE_ROLES_FOURSWITCH = 9;
  public static final byte DNDEVICE_ROLES_XPONOLT = 20;
  public static final byte DNDEVICE_ROLES_XPONONU = 21;
  public static final long NMTOPOMAP_MAP_TRANSPORTSYSTEMMAP_ID = 1L;
  public static final long NMTOPOMAP_MAP_TRANSPORTNETWORKMAP_ID = 2L;
  public static final long NMTOPOMAP_MAP_SWITCHINGNETWORKMAP_ID = 3L;
  public static final long NMTOPOMAP_MAP_ACCESSINGNETWORKMAP_ID = 4L;
  public static final long NMTOPOMAP_MAP_SYNCNETWORKMAP_ID = 5L;
  public static final long NMTOPOMAP_MAP_MW_ID = 7L;
  public static final long NMTOPOMAP_MAP_PAGING_ID = 8L;
  public static final long NMTOPOMAP_MAP_MOBILE_ID = 9L;
  public static final long NMTOPOMAP_MAP_LOCATION_ID = 10L;
  public static final long NMTOPOMAP_MAP_CABLE_ID = 11L;
  public static final long NMTOPOMAP_MAP_64KSLNETWORKMAP_ID = 12L;
  public static final long NMTOPOMAP_MAP_DATA_DDNFR_ID = 13L;
  public static final long NMTOPOMAP_MAP_DATA_ATM_ID = 14L;
  public static final long NMTOPOMAP_MAP_DATA_X25_ID = 15L;
  public static final long NMTOPOMAP_MAP_DATA_BAS_ID = 16L;
  public static final long NMTOPOMAP_MAP_DATA_IPHOTEL_ID = 17L;
  public static final long NMTOPOMAP_MAP_DATA_NAS_ID = 18L;
  public static final long NMTOPOMAP_MAP_DATA_IPLAN_ID = 19L;
  public static final long NMTOPOMAP_MAP_DATA_MEETINGTV_ID = 20L;
  public static final long NMTOPOMAP_MAP_DATA_DCN_ID = 21L;
  public static final long NGTOPOMAP_MAP_TRANSPORTNETWORKMAP_ID = 22L;
  public static final long NGTOPOMAP_MAP_CIRCUITMAP_ID = 23L;
  public static final long NGTOPOMAP_MAP_ROOMMAP_ID = 24L;
  public static final long NGTOPOMAP_MAP_EMSMAP_ID = 25L;
  public static final long NMTOPOMAP_MAP_DNDOMAIN_ID = 26L;
  public static final long NMTOPOMAP_MAP_NODE_ID = 27L;
  public static final long NMTOPOMAP_MAP_IP_ID = 28L;
  public static final long NMTOPOMAP_MAP_VPN_ID = 29L;
  public static final long NMTOPOMAP_MAP_XDSL_ID = 30L;
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_NE = "NE";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_TRANSPORTSYSTEM = "TSYSTEM";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_REGION = "REGION";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_SITE = "SITE";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_DATASET = "DATASET";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_DATANE = "DATANE";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_CABLETERM = "CABLETERM";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_SL64KSYSTEM = "SL64KSYSTEM";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_PAGINGCENTER = "PAGINGCENTER";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_PAGINGSTATION = "STATION";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_TOWER = "TOWER";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_REMOTEBTS = "REMOTEBTS";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_ROOM = "ROOM";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_CABLETERMHOST = "CABLETERM_HOST";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_DNDOMAIN = "DNDOMAIN";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_NODE = "NODE";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_PE = "PE";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_CE = "CE";
  public static final String NMTOPOMAP_MAPNODE_ENTITYTYPE_DSLNODE = "DSLNODE";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_DNDOMAIN = "MAPNODETYPE-DNDOMAIN-";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_NODE_ATM = "MAPNODETYPE-NODE-ATM";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_NODE_DDN = "MAPNODETYPE-NODE-DDN";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_NODE_FR = "MAPNODETYPE-NODE-FR";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_NODE_ATM_DDN_FR = "MAPNODETYPE-NODE-ATM_DDN_FR";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_NODE_IP = "MAPNODETYPE-NODE-IP";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_NODE_OTHER = "MAPNODETYPE-NODE-OTHER";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_IP_NODE = "MAPNODETYPE-IP-NODE";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_CE = "MAPNODETYPE-CE";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_PE = "MAPNODETYPE-PE";
  public static final String NMTOPOMAP_MAPNODE_TYPE_PREFIX_FOR_DSLNODE = "MAPNODETYPE-DSLNODE";
  
  
  public static String Audit_DELIMITER_COL = "\1";
  
  public static final byte ASSETCATEGORY_CATEGORYLEVEL_CLASS = 1; //类
  public static final byte ASSETCATEGORY_CATEGORYLEVEL_ITEM = 2; //项
  public static final byte ASSETCATEGORY_CATEGORYLEVEL_CATEGORY = 3; //目
  public static final byte ASSETCATEGORY_CATEGORYLEVEL_SECTION = 4; //节
  public static final byte ASSETCATEGORY_CATEGORYLEVEL_DOT = 5; //点
  
  public static final byte DNCONNECTION_LIFESTATUS_OPEN = 0; //开通
  public static final byte DNCONNECTION_LIFESTATUS_WAIT_OPEN = 1; //待开通
  public static final byte DNCONNECTION_LIFESTATUS_WAIT_DESTROY = 2; //待拆除
  public static final byte DNCONNECTION_LIFESTATUS_WAIT_SWITCH = 3; //待割接
  public static final byte DNCONNECTION_LIFESTATUS_WAIT_CHANGE = 4; //待改参
  public static final byte DNCONNECTION_LIFESTATUS_WAIT_STOP = 5; //待停机
  public static final byte DNCONNECTION_LIFESTATUS_WAIT_START = 6; //待复机
  public static final byte DNCONNECTION_LIFESTATUS_STOP = 7; //停机
  public static final byte DNCONNECTION_LIFESTATUS_OCCUPY_DESTROY = 8; //拆除预占
  public static final byte DNCONNECTION_LIFESTATUS_OCCUPY_SWITCH = 9; //移机预占
  public static final byte DNCONNECTION_LIFESTATUS_OCCUPY_CHANGE = 10; //改参预占
  public static final byte DNCONNECTION_LIFESTATUS_OCCUPY_STOP = 11; //停机预占
  public static final byte DNCONNECTION_LIFESTATUS_OCCUPY_START = 12; //复机预占
  public static final byte DNCONNECTION_LIFESTATUS_STOPWAIT_DESTROY = 13; //停机待拆除
//add by tailichun 20060322
  public static final byte DNCONNECTION_LIFESTATUS_OCCUPY_SERVICE = 14; //业务预占
  public static final byte DNCONNECTION_LIFESTATUS_DESTORY = 15; //已拆除
  
  
//路由文本描述的表格Title
  static public final String TEXTDESC_PATHNAME="电路名称";
  static public final String TEXTDESC_HOST="机房";
  static public final String TEXTDESC_DEVICE="设备";
  static public final String TEXTDESC_NODE="节点";
  static public final String TEXTDESC_RACK="机柜";
  static public final String TEXTDESC_SHELF="子架";
  static public final String TEXTDESC_SUBNE= "机框";
  static public final String TEXTDESC_PACKAGE="板卡";
  
  static public final String TEXTDESC_PORTNAME="端口";
  static public final String TEXTDESC_POSITION= "位置";
  static public final String TEXTDESC_SLOTS="时隙";
  static public final String TEXTDESC_DLCI="DLCI";
  static public final String TEXTDESC_RATE="速率";
  static public final String TEXTDESC_VPI="VPI";
  static public final String TEXTDESC_VCI="VCI";
  static public final String TEXTDESC_OUTERCONNECTOR= "外侧端子";
  static public final String TEXTDESC_INNERCONNECTOR="内侧端子";
  static public final String TEXTDESC_INOUTCONNECTOR="收发端子";
  
  
  public static String OPR_ADD = "新增";
  public static String OPR_UPDATE = "修改";
  public static String OPR_DELETE = "删除";
  public static String OPR_INSTALL = "安装";
  public static String OPR_DESTROY = "拆除";
  public static String OPR_SWITCH = "移机";
  public static String OPR_CHANGE = "改参";
  public static String OPR_HALT = "停机";
  public static String OPR_RESUME = "复机";
  public static String OPR_FINISH = "调度完工";
  public static String OPR_CANCEL = "撤销调度";
  public static String OPR_MODIFYDESIGN = "修改调度设计";

  public static String DELIMITER_COL = ",";
  public static String DELIMITER_ROW = "\n";
  
  
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_ATM = 1;
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_DDN = 2;
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_FR = 3;
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_RELAYPATH = 4; //基础网中继
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_IPRELAYPATH = 5; //IP网中继
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_XDSLRELAYPATH = 6; //xDSL中继
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_DDNMULTIPATH = 7; //DDN复用电路
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_WAN = 8; //城域网业务
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_VPN = 9; //VPN业务
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_LAN = 10; //LAN业务
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_CIRCUIT = 11; //用户电路

  //add by tailichun 20060713f
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_ATMPORT = 14; //ATMPORT端口电路
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_DDNPORT = 15; //DDNPORT端口电路
  
  public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_FRPORTSERVICE = 12; //FR端口电路
//add by chenlijuan20050608
    public final static byte DNBUSINESSPATHAUDITS_ENTITYTYPE_COMMONSERVICE = 13; //通用业务电路
    
    public static final byte RELAYPATH_TYPE_DDN = 0; //DDN
    public static final byte RELAYPATH_TYPE_FR = 1; //FR
    public static final byte RELAYPATH_TYPE_ATM = 2; //ATM
    public static final byte RELAYPATH_TYPE_IP = 3; //IP
    public static final byte RELAYPATH_TYPE_XDSL = 4; //xDSL
    public static final byte RELAYPATH_TYPE_USERCIRCUIT = 5; //用户电路
    public static final byte RELAYPATH_TYPE_INTERLINKAGE = 6; //互连
    public static final byte RELAYPATH_TYPE_OTHER = 99; //Other
    //add by chenlijuan 20050609 中继电路增加类型
    public static final byte RELAYPATH_TYPE_IDC = 7; //IDC
    public static final byte RELAYPATH_TYPE_DCN = 8; //DCN
    public static final byte RELAYPATH_TYPE_WLAN = 9; //WLAN
    public static final byte RELAYPATH_TYPE_EXTRASERVICE = 10; //增值服务
    public static final byte RELAYPATH_TYPE_EPON = 11; //EPON中继
    //add by wuzhanghui 20091123 WLAN中继电路增加类型
    public static final byte RELAYPATH_TYPE_WLANACESS = 12; //WLAN接入中继
    
    
    public final static byte DNPATH_INTEGRITYOFPATH_BOTHINTEGRITY = 0; //两端完整
    public final static byte DNPATH_INTEGRITYOFPATH_SINGLEINTEGRITY = 1; //单端完整
    public final static byte DNPATH_INTEGRITYOFPATH_NEITHERINTEGRITY = 2; //两端不完整
}
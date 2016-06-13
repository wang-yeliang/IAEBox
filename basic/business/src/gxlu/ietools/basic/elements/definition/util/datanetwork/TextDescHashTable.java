/***************************************************************************************
 * $RCSfile: TextDescHashTable.java,v $
 ***************************************************************************************/
package gxlu.ietools.basic.elements.definition.util.datanetwork;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *这个类主要是生成文本描述的时候用的，就不要Server端提供String返回的接口
 *将返回的hashtable转化成String是这个类的一个接口
 *只要Server端能过给出足够的信息，就可以在客户端转换，没有必要在Server端组装
 ***/
public class TextDescHashTable implements Serializable{
  public TextDescHashTable()
  {
    ht = new Hashtable();
    //电路名称将在客户端进行设置，这里缺省都为空
  }
  //Hashtable中不论Key还是Value只有String
  private Hashtable ht;

  public void putValue(String key,String value)
  {
    if(value==null|| key==null)return;
    ht.put(key,value);
  }
  public String getValue(String key)
  {//这里强制类型转换了
    return (String)ht.get(key);
  }
  public String toTextDescString()
  {
    if(ht==null)    return null;
    String textDesc = new String("");
    String type = (String)ht.get(TEXTDESC_POINTTYPE);
    String pos = (String)ht.get(TEXTDESC_POSITION);
    if(type.equals(TEXTDESC_POINTTYPE_ATM))
    {
      textDesc += "<"+ht.get(TEXTDESC_PORTNAME)+">";
      textDesc += "<"+ht.get(TEXTDESC_POSITION)+">";
//      textDesc += ht.get(TEXTDESC_STARTSLOT);
//      textDesc += ht.get(TEXTDESC_ENDSLOT);
//      textDesc += ht.get(TEXTDESC_DLCI);
      textDesc += "<"+ht.get(TEXTDESC_VPI)+"/";
      textDesc += ht.get(TEXTDESC_VCI)+">";
      if(!pos.equals(TEXTDESC_POSITION_RELAY))
      {//只要不是转接端口，就要打印出外侧端子和内侧端子
	textDesc += "<"+ht.get(TEXTDESC_INNERCONNECTOR)+"、";
	textDesc += ht.get(TEXTDESC_OUTERCONNECTOR)+">";
      }
    }else if(type.equals(TEXTDESC_POINTTYPE_FR))
    {
      textDesc += "<"+ht.get(TEXTDESC_PORTNAME)+">";
      textDesc += "<"+ht.get(TEXTDESC_POSITION)+">";
      textDesc += "<"+ht.get(TEXTDESC_SLOTS)+",";
      textDesc += ht.get(TEXTDESC_DLCI)+">";
//      textDesc += ht.get(TEXTDESC_VPI);
//      textDesc += ht.get(TEXTDESC_VCI);
      if(!pos.equals(TEXTDESC_POSITION_RELAY))
      {//只要不是转接端口，就要打印出外侧端子和内侧端子
	textDesc += "<"+ht.get(TEXTDESC_INNERCONNECTOR)+"、";
	textDesc += ht.get(TEXTDESC_OUTERCONNECTOR)+">";
      }
    }else if(type.equals(TEXTDESC_POINTTYPE_DDNE1))
    {
      textDesc += "<"+ht.get(TEXTDESC_PORTNAME)+">";
      textDesc += "<"+ht.get(TEXTDESC_POSITION)+">";
      textDesc += "<"+ht.get(TEXTDESC_SLOTS)+">";
//      textDesc += ht.get(TEXTDESC_DLCI);
//      textDesc += ht.get(TEXTDESC_VPI);
//      textDesc += ht.get(TEXTDESC_VCI);
      if(!pos.equals(TEXTDESC_POSITION_RELAY))
      {//只要不是转接端口，就要打印出外侧端子和内侧端子
	textDesc += "<"+ht.get(TEXTDESC_INNERCONNECTOR)+"、";
	textDesc += ht.get(TEXTDESC_OUTERCONNECTOR)+">";
      }
    }else if(type.equals(TEXTDESC_POINTTYPE_DDNUIF))
    {
      textDesc += "<"+ht.get(TEXTDESC_PORTNAME)+">";
      textDesc += "<"+ht.get(TEXTDESC_POSITION)+">";
//      textDesc += ht.get(TEXTDESC_STARTSLOT);
//      textDesc += ht.get(TEXTDESC_ENDSLOT);
//      textDesc += ht.get(TEXTDESC_DLCI);
//      textDesc += ht.get(TEXTDESC_VPI);
//      textDesc += ht.get(TEXTDESC_VCI);
      if(!pos.equals(TEXTDESC_POSITION_RELAY))
      {//只要不是转接端口，就要打印出外侧端子和内侧端子
	textDesc += "<"+ht.get(TEXTDESC_INNERCONNECTOR)+"、";
	textDesc += ht.get(TEXTDESC_OUTERCONNECTOR)+">";
      }
    }else if(type.equals(TEXTDESC_POINTTYPE_DDNPORT))
    {
      textDesc += "<"+ht.get(TEXTDESC_PORTNAME)+">";
      textDesc += "<"+ht.get(TEXTDESC_POSITION)+">";
//      textDesc += ht.get(TEXTDESC_STARTSLOT);
//      textDesc += ht.get(TEXTDESC_ENDSLOT);
//      textDesc += ht.get(TEXTDESC_DLCI);
//      textDesc += ht.get(TEXTDESC_VPI);
//      textDesc += ht.get(TEXTDESC_VCI);
      if(!pos.equals(TEXTDESC_POSITION_RELAY))
      {//只要不是转接端口，就要打印出外侧端子和内侧端子
	textDesc += "<"+ht.get(TEXTDESC_INNERCONNECTOR)+"、";
	textDesc += ht.get(TEXTDESC_OUTERCONNECTOR)+">";
      }
    }else{
      textDesc += ht.get(TEXTDESC_PORTNAME);
      textDesc += ht.get(TEXTDESC_POSITION);
      textDesc += ht.get(TEXTDESC_SLOTS);
      textDesc += ht.get(TEXTDESC_DLCI);
      textDesc += ht.get(TEXTDESC_RATE);
      textDesc += ht.get(TEXTDESC_VPI);
      textDesc += ht.get(TEXTDESC_VCI);
      textDesc += ht.get(TEXTDESC_INNERCONNECTOR);
      textDesc += ht.get(TEXTDESC_OUTERCONNECTOR);
    }
    textDesc += "\n";
    return textDesc;
  }
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
  //这个是自己加的扩展，根据点的类型进行字符串格式化
  //否则<>不知道应该在什么地方
  static public final String TEXTDESC_POSITION_BEGIN="本端";
  static public final String TEXTDESC_POSITION_RELAY="转接";
  static public final String TEXTDESC_POSITION_END="对端";

  static public final String TEXTDESC_POINTTYPE="点类型";
  static public final String TEXTDESC_POINTTYPE_ATM= "ATM";
  static public final String TEXTDESC_POINTTYPE_FR= "FR";
  static public final String TEXTDESC_POINTTYPE_DDNE1="DDNE1";
  static public final String TEXTDESC_POINTTYPE_DDNUIF= "DDNUIF";
  static public final String TEXTDESC_POINTTYPE_DDNPORT= "DDNPORT";
}

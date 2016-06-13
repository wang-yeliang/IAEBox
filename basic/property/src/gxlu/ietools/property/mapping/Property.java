//$Id: Property.java,v 1.6 2010/04/20 02:08:05 wudawei Exp $
package gxlu.ietools.property.mapping;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a property as part of an entity or a component.
 *
 * @author kidd
 */
public class Property implements Serializable {

	private String bclass;
	private String cname;
	private String byInsert;
	private String byUpdate;
	private String thdLineNum;
	private String thdLineMax;
	private List propertyValue;
	private List propertyObject;

	public String getBclass() {
		return bclass;
	}

	public List getPropertyObject() {
		return propertyObject;
	}

	public void setPropertyObject(List propertyObject) {
		this.propertyObject = propertyObject;
	}

	public List getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(List propertyValue) {
		this.propertyValue = propertyValue;
	}

	public void setBclass(String bclass) {
		this.bclass = bclass;
	}

	public Map getMetaAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMetaAttributes(Map metas) {
		// TODO Auto-generated method stub
		
	}

	public String getByInsert() {
		return byInsert;
	}

	public void setByInsert(String byInsert) {
		this.byInsert = byInsert;
	}

	public String getByUpdate() {
		return byUpdate;
	}

	public void setByUpdate(String byUpdate) {
		this.byUpdate = byUpdate;
	}

	public String getThdLineMax() {
		return thdLineMax;
	}

	public void setThdLineMax(String thdLineMax) {
		this.thdLineMax = thdLineMax;
	}

	public String getThdLineNum() {
		return thdLineNum;
	}

	public void setThdLineNum(String thdLineNum) {
		this.thdLineNum = thdLineNum;
	}

	public String toString() {
//        return bclass.substring(bclass.lastIndexOf(".")+1,bclass.length());
		return cname;
    }

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
}

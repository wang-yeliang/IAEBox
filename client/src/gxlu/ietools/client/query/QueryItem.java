//$Id: QueryItem.java,v 1.3 2010/04/20 02:08:03 wudawei Exp $
package gxlu.ietools.client.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a property as part of an entity or a component.
 *
 * @author kidd
 */
public class QueryItem implements Serializable {

	private String name;
	private String title;
	private String dclass;
	private String dattr;
	private String type;
	private String otype;
	private String jncolumn;
	private String bObject;
	private String bObcolumn;
	private String qclass;


	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDclass() {
		return dclass;
	}

	public String getDattr() {
		return dattr;
	}
	
	public String getType() {
		return type;
	}
	
	public String getOtype() {
		return otype;
	}
	
	public String getJncolumn() {
		return jncolumn;
	}
	
	public String getBObject() {
		return bObject;
	}
	
	public String getBObcolumn() {
		return bObcolumn;
	}
		
	public String getQclass() {
		return qclass;
	}
	


	public void setName(String name) {
		this.name = name;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDclass(String dclass) {
		this.dclass = dclass;
	}

	public void setDattr(String dattr) {
		this.dattr = dattr;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setOtype(String otype) {
		this.otype = otype;
	}
	
	public void setJncolumn(String jncolumn) {
		this.jncolumn = jncolumn;
	}
	
	public void setBObject(String bObject) {
		this.bObject = bObject;
	}
	
	public void setBObcolumn(String bObcolumn) {
		this.bObcolumn = bObcolumn;
	}
	
	public void setQclass(String qclass) {
		this.qclass = qclass;
	}
	
	public String toString() {
        return this.name;
    }
}
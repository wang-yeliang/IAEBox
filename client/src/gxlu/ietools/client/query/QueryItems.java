//$Id: QueryItems.java,v 1.3 2010/04/20 02:08:03 wudawei Exp $
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
public class QueryItems implements Serializable {

	private String TemplateName;
	private HashMap QueryItemSet;

	public String getTemplateName() {
		return TemplateName;
	}

	public HashMap getQueryItemSet() {
		return QueryItemSet;
	}

	public void setTemplateName(String TemplateName) {
		this.TemplateName = TemplateName;
	}
	
	public void setQueryItemSet(HashMap QueryItemSet) {
		this.QueryItemSet = QueryItemSet;
	}

	public String toString() {
        return TemplateName.substring(TemplateName.lastIndexOf("/")+1,TemplateName.length());
    }
}

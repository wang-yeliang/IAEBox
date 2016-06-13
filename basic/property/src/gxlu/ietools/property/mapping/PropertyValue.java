package gxlu.ietools.property.mapping;

import java.io.Serializable;

public class PropertyValue implements Serializable {
    private String name;
    private String columnSeq;
    private String columnTitle;
    private boolean isDatadict;
    private String datadictClass;
    private String datadictAttr;
    private String isQuery;
    
	public String getIsQuery() {
		return isQuery;
	}
	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((columnSeq == null) ? 0 : columnSeq.hashCode());
		result = PRIME * result + ((columnTitle == null) ? 0 : columnTitle.hashCode());
		result = PRIME * result + (isDatadict ? 1231 : 1237);
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((datadictClass == null) ? 0 : datadictClass.hashCode());
		result = PRIME * result + ((datadictAttr == null) ? 0 : datadictAttr.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PropertyValue other = (PropertyValue) obj;
		if (columnSeq == null) {
			if (other.columnSeq != null)
				return false;
		} else if (!columnSeq.equals(other.columnSeq))
			return false;
		if (columnTitle == null) {
			if (other.columnTitle != null)
				return false;
		} else if (!columnTitle.equals(other.columnTitle))
			return false;
		if (isDatadict != other.isDatadict)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (datadictClass == null) {
			if (other.datadictClass != null)
				return false;
		} else if (!datadictClass.equals(other.datadictClass))
			return false;
		
		if (datadictAttr == null) {
			if (other.datadictAttr != null)
				return false;
		} else if (!datadictAttr.equals(other.datadictAttr))
			return false;
		return true;
	}
	public String getColumnSeq() {
		return columnSeq;
	}
	public void setColumnSeq(String columnSeq) {
		this.columnSeq = columnSeq;
	}
	public String getColumnTitle() {
		return columnTitle;
	}
	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}
	public boolean isDatadict() {
		return isDatadict;
	}
	public void setDatadict(boolean isDatadict) {
		this.isDatadict = isDatadict;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
        return name;
    }
	public String getDatadictAttr() {
		return datadictAttr;
	}
	public void setDatadictAttr(String datadictAttr) {
		this.datadictAttr = datadictAttr;
	}
	public String getDatadictClass() {
		return datadictClass;
	}
	public void setDatadictClass(String datadictClass) {
		this.datadictClass = datadictClass;
	}

}

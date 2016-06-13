package gxlu.ietools.property.mapping;

import java.io.Serializable;

public class PropertyObject implements Serializable{
		private String name;
		private String columnSeq;
    	private String columnTitle;
        private String joinColumn;
        private String className;
        private String classColumn;
        private String isQuery;
        private String queryClass;
        
		public String getQueryClass() {
			return queryClass;
		}
		public void setQueryClass(String queryClass) {
			this.queryClass = queryClass;
		}
		public String getIsQuery() {
			return isQuery;
		}
		public void setIsQuery(String isQuery) {
			this.isQuery = isQuery;
		}

		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((classColumn == null) ? 0 : classColumn.hashCode());
			result = PRIME * result + ((className == null) ? 0 : className.hashCode());
			result = PRIME * result + ((columnSeq == null) ? 0 : columnSeq.hashCode());
			result = PRIME * result + ((columnTitle == null) ? 0 : columnTitle.hashCode());
			result = PRIME * result + ((joinColumn == null) ? 0 : joinColumn.hashCode());
			result = PRIME * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final PropertyObject other = (PropertyObject) obj;
			if (classColumn == null) {
				if (other.classColumn != null)
					return false;
			} else if (!classColumn.equals(other.classColumn))
				return false;
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
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
			if (joinColumn == null) {
				if (other.joinColumn != null)
					return false;
			} else if (!joinColumn.equals(other.joinColumn))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		public String getClassColumn() {
			return classColumn;
		}
		public void setClassColumn(String classColumn) {
			this.classColumn = classColumn;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
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
		public String getJoinColumn() {
			return joinColumn;
		}
		public void setJoinColumn(String joinColumn) {
			this.joinColumn = joinColumn;
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

}

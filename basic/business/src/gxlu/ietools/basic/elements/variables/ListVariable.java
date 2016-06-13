/**************************************************************************
 * $$RCSfile: ListVariable.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:06 $$
 *
 * $$Log: ListVariable.java,v $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.variables;

import gxlu.ietools.property.propertys.PropertyAccessorFactory;
import gxlu.ietools.property.util.FormatUtil;
import gxlu.ietools.property.util.ReflectHelper;
import gxlu.ietools.property.util.SystemErrorNames;

import java.util.ArrayList;
import java.util.List;

/**
 * List variable - String wrapper.
 * @author kidd
 */
public class ListVariable extends Variable {

    private Object object;
    
    private volatile ArrayList arrayList = new ArrayList();

	public Object getObject() {
		return object;
	}

    public ListVariable(){}
    
	public ListVariable(Object object) {
        this.object = object;
    }

	public void setWrappedObject(Object object) {
		// TODO Auto-generated method stub
		this.object = object;
	}
	
    public Object getWrappedObject() {
        return this.object;
    }

	public List getObjectList() {
		// TODO Auto-generated method stub
		return this.arrayList;
	}
    
	public void setObjectList(Object object) {
		// TODO Auto-generated method stub
		if(arrayList.size()>0){
			arrayList.clear();
		}
		arrayList = (ArrayList)object;
	}

	public boolean getCheckVariable(Object value){
		if(value instanceof String){
			return true;
		}
		return false;
	}

}
/**************************************************************************
 * $$RCSfile: Scraper.java,v $$  $$Revision: 1.7 $$  $$Date: 2010/05/14 01:10:27 $$
 *
 * $$Log: Scraper.java,v $
 * $Revision 1.7  2010/05/14 01:10:27  zhangj
 * $20100514更新
 * $
 * $Revision 1.6  2010/04/20 02:08:08  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements;

import org.apache.log4j.Logger;

import gxlu.ietools.basic.elements.definition.BaseElementDef;
import gxlu.ietools.basic.elements.processors.BaseProcessor;
import gxlu.ietools.basic.elements.processors.ProcessorResolver;
import gxlu.ietools.basic.elements.variables.ListVariable;
import gxlu.ietools.basic.elements.variables.Variable;
import gxlu.ietools.basic.elements.variables.XmlNode;
import gxlu.ietools.basic.exception.ElementsException;
import gxlu.ietools.property.propertys.PropertyAccessorFactory;
import gxlu.ietools.property.util.FormatUtil;
import gxlu.ietools.property.util.SystemErrorNames;

import java.util.*;

/**
 * Basic class.
 * @author kidd
 */
public class Scraper {

    private Logger logger = Logger.getLogger("" + System.currentTimeMillis());

    private volatile List bobjectList = new ArrayList();
    
    private XmlNode[] xmlNode;
    private Map metas;

	public Map getMetas() {
		return metas;
	}

	public void setXmlNode(XmlNode[] xmlNode) {
		this.xmlNode = xmlNode;
	}

	public void setBobjectList(List bobjectList) {
		this.bobjectList = bobjectList;
	}

	public void setMetas(Map metas) {
		this.metas = metas;
	}
	
	/**
     * execute of running processors
     * @param xmlNode
     * @return
     */
    public Variable execute() {
    	long startTime = System.currentTimeMillis();
    	
    	Variable variable = new ListVariable();
    	try{
    		if(xmlNode.length>0){
        		for(int i=0;i<xmlNode.length;i++){
        			XmlNode xmlNodes = xmlNode[i];
            		BaseElementDef elementDef = BaseElementDef.createElementDefinition(xmlNodes);
        	        BaseProcessor processor = ProcessorResolver.createProcessor(elementDef, this);

        	        Variable variables = null;
        	    	if (processor != null) {
        	    		variables = processor.run(this);
        	    	}
        	    	if(variables!=null){
            	    	variable.setObjectList(variables.getWrappedObject());
            	    	
            	    	execute(variable);
        	    	}

        		}
    		}else{
    			variable.setObjectList(getBobjectList());
    		}

    		variable.setXmlNodeLen(xmlNode.length);
		} catch (ElementsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Elements executed in " + (System.currentTimeMillis() - startTime) + "ms.");
		return variable;
    }
    
    /**
     * 过滤最终数据
     * @param ops
     */
    public void execute(Variable variable){
    	List valueList = variable.getObjectList();

    	for (int i = 0; i < valueList.size(); i++) {
    		Object[] value = (Object[])valueList.get(i);
    		if(variable.getCheckVariable(value[0])){
    			Object[] errorObj = new Object[2];
    			errorObj[0] = (Integer)value[1];
    			errorObj[1] = SystemErrorNames.ElementsDataError + ": "+(String)value[0];
    			PropertyAccessorFactory.addTypeConvertError(errorObj);
    			valueList.remove(i);
    			i--;
    		}else{
    	   		int count = 0;
    	   		for (int j = 0; j < valueList.size(); j++) {
    	    		if (value.equals(valueList.get(j))) {
    	    			count++;     
    	    		}
    	   		}
    	   		if (count<variable.getXmlNodeLen()) {
    	   			valueList.remove(i);
    	   			i--;
    	   		}
    	   		count=0;
    		}
    	}

    	variable.setWrappedObject(valueList);
    	setBobjectList(valueList);
    }

    public Logger getLogger() {
        return logger;
    }

	public List getBobjectList() {
		return bobjectList;
	}

}
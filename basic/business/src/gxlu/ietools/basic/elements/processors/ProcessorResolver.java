/**************************************************************************
 * $$RCSfile: ProcessorResolver.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: ProcessorResolver.java,v $
 * $Revision 1.6  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.processors;

import gxlu.ietools.basic.elements.Scraper;
import gxlu.ietools.basic.elements.definition.BaseElementDef;
import gxlu.ietools.basic.elements.definition.IteratorElementDef;
import gxlu.ietools.basic.elements.definition.LevelsElementDef;
import gxlu.ietools.basic.elements.definition.NullElementDef;
import gxlu.ietools.basic.elements.definition.UniqueElementDef;

/**
 * ProcessorResolver definition processor.
 * @author kidd
 */
public class ProcessorResolver {
	
    /**
     * 创建工作进程实例
     * @param elementDef
     * @param scraper
     * @return
     */
    public static BaseProcessor createProcessor(BaseElementDef elementDef, Scraper scraper) {
        if (elementDef instanceof NullElementDef) {
	    	return new NullProcessor( (NullElementDef)elementDef );
	    } else if (elementDef instanceof IteratorElementDef) {
	    	return new IteratorProcessor( (IteratorElementDef)elementDef );
	    } else if (elementDef instanceof LevelsElementDef) {
	    	return new LevelsProcessor( (LevelsElementDef)elementDef );
	    } else if (elementDef instanceof UniqueElementDef) {
	    	return new UniqueProcessor( (UniqueElementDef)elementDef );
	    }

        return null;
    }
    
}
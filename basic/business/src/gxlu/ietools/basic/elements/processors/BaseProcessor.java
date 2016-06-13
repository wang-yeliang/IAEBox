/**************************************************************************
 * $$RCSfile: BaseProcessor.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: BaseProcessor.java,v $
 * $Revision 1.6  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.processors;

import gxlu.ietools.basic.elements.Scraper;
import gxlu.ietools.basic.elements.definition.BaseElementDef;
import gxlu.ietools.basic.elements.variables.Variable;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ExectionUtil;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Base processor that contains common processor logic.
 * All other processors extend this class.
 * @author kidd
 */
abstract public class BaseProcessor{

    abstract public Variable execute(Scraper scraper);

    protected BaseElementDef elementDef;

    protected BaseProcessor() {
    }

    protected Context context = ExectionUtil.getContext();
    
    /**
     * Base constructor - assigns element definition to the processor.
     * @param elementDef
     */
    protected BaseProcessor(BaseElementDef elementDef) {
        this.elementDef = elementDef;
    }

    public Variable run(Scraper scraper) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
        synchronized(scraper) {
	           if ( scraper.getLogger().isInfoEnabled() ) {
	           	scraper.getLogger().info("Execution paused [" + dateFormatter.format(new Date()) + "].");
	           }
        }

        long startTime = System.currentTimeMillis();
        
        Variable result = execute(scraper);

        if ( scraper.getLogger().isInfoEnabled() ) {
            scraper.getLogger().info("BaseProcessor processor executed in " + startTime + "ms.");
        }

        return result;
    }

}
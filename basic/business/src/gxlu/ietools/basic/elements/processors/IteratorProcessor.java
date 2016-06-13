/**************************************************************************
 * $$RCSfile: IteratorProcessor.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: IteratorProcessor.java,v $
 * $Revision 1.6  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.processors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import gxlu.ietools.basic.elements.Scraper;
import gxlu.ietools.basic.elements.definition.IteratorElementDef;
import gxlu.ietools.basic.elements.variables.ListVariable;
import gxlu.ietools.basic.elements.variables.Variable;
import gxlu.ietools.basic.system.util.VariableNames;
import gxlu.ietools.property.util.FormatUtil;

/**
 * Function definition processor.
 * @author kidd
 */
public class IteratorProcessor extends BaseProcessor {

    private IteratorElementDef iteratorElementDef;

    public IteratorProcessor(IteratorElementDef iteratorElementDef) {
        super(iteratorElementDef);
        this.iteratorElementDef = iteratorElementDef;
    }

    public Variable execute(Scraper scraper) {
        try {
        	Class classes = null;
            Object object = context.lookupElementDef(iteratorElementDef.getName());
            if(object==null){
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                classes = classLoader.loadClass(iteratorElementDef.getName());
            }else{
            	classes = object.getClass();
            }
            
            List iParam = new ArrayList();
            iParam.add(0, scraper.getBobjectList());
            iParam.add(1, iteratorElementDef.getTargetObject());
            iParam.add(2, FormatUtil.getTitle((List)scraper.getMetas().get(VariableNames.PROPERTY_VALUE), iteratorElementDef.getTargetObject()));
            Method m = classes.getMethod(iteratorElementDef.getTargetMethod(), List.class);
            Object obj = m.invoke(classes.newInstance(), new Object[] { iParam });
            return new ListVariable(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public static void main(String[] args){
    	
    }
}
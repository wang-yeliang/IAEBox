/**************************************************************************
 * $$RCSfile: LevelsProcessor.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: LevelsProcessor.java,v $
 * $Revision 1.6  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.elements.processors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import gxlu.ietools.basic.elements.Scraper;
import gxlu.ietools.basic.elements.definition.LevelsElementDef;
import gxlu.ietools.basic.elements.variables.ListVariable;
import gxlu.ietools.basic.elements.variables.Variable;
import gxlu.ietools.basic.system.util.VariableNames;
import gxlu.ietools.property.util.FormatUtil;

/**
 * Function definition processor.
 * @author kidd
 */
public class LevelsProcessor extends BaseProcessor {

    private LevelsElementDef levelsElementDef;

    public LevelsProcessor(LevelsElementDef levelsElementDef) {
        super(levelsElementDef);
        this.levelsElementDef = levelsElementDef;
    }

    public Variable execute(Scraper scraper) {
        try {
        	Class classes = null;
            Object object = context.lookupElementDef(levelsElementDef.getName());
            if(object==null){
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                classes = classLoader.loadClass(levelsElementDef.getName());
            }else{
            	classes = object.getClass();
            }
            
            List iParam = new ArrayList();
            iParam.add(0, scraper.getBobjectList());
            iParam.add(1, levelsElementDef.getTargetObject());
            iParam.add(2, FormatUtil.getTitle((List)scraper.getMetas().get(VariableNames.PROPERTY_VALUE), levelsElementDef.getTargetObject()));
            Method m = classes.getMethod(levelsElementDef.getTargetMethod(), List.class);
            Object obj = m.invoke(classes.newInstance(), new Object[] { iParam });
            return new ListVariable(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

}
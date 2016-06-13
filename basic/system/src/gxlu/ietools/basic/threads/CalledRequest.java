/**************************************************************************
 * $$RCSfile: CalledRequest.java,v $$  $$Revision: 1.4 $$  $$Date: 2010/04/20 02:08:03 $$
 *
 * $$Log: CalledRequest.java,v $
 * $Revision 1.4  2010/04/20 02:08:03  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.threads;

import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.util.ExectionUtil;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

public class CalledRequest{
	
	protected Logger logger = Logger.getLogger(CalledRequest.class);
	
    private final Object bobject;
    private final List objectList;
    private final List propertyList;
    private final int workFlag;
    private final int[] rowCount;
    private final String classNote;
    private final String methodNote;
    private final String bobjectInfo;

    /**
     * 通过构造函数赋值
     * @param iParam
     */
    public CalledRequest(List iParam) {
    	this.bobject = iParam.get(0);
    	this.objectList = (List)iParam.get(1);
    	this.workFlag = ((Integer)iParam.get(2)).intValue();
        this.rowCount = (int[])iParam.get(3);
        this.classNote = (String)iParam.get(4);
        this.methodNote = (String)iParam.get(5);
        this.propertyList = (List)iParam.get(6);
        this.bobjectInfo = (String)iParam.get(7);
    }
    
    /**
     * 通过反射方式执行方法
     */
    public void execute(){
    	try{
	    	Context context = ExectionUtil.getContext();
	    	Object object = context.lookupSessionBean(classNote);
	    	Class classes = object.getClass();
	    	
	    	Method m2 = classes.getDeclaredMethod(methodNote, Object.class,List.class,List.class,String.class,int[].class,int.class);
	    	m2.invoke(object, bobject,objectList,propertyList,bobjectInfo,rowCount,workFlag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());
			logger.error(e.toString());
			e.printStackTrace();
		}
    }
}

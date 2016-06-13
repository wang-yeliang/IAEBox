/**************************************************************************
 * $$RCSfile: ExectionUtil.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:06 $$
 *
 * $$Log: ExectionUtil.java,v $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.util;

import gxlu.ietools.basic.system.container.Container;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.context.Context;

import org.apache.log4j.Logger;
/**
*
* @author kidd
*/
public class ExectionUtil {
	
	private static Container container = ContainerFactory.getContainer();
	
	private static Context context = container.newContext();
	
	protected Logger logger = Logger.getLogger(ExectionUtil.class);
	
	/**
	 * @return
	 */
	public static Context getContext() {
		return context;
	}

}

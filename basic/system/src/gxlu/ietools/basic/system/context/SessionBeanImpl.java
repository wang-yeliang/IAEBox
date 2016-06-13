/**************************************************************************
 * $$RCSfile: SessionBeanImpl.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:06 $$
 *
 * $$Log: SessionBeanImpl.java,v $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.context;

import org.apache.log4j.Logger;

/**
* @author  kidd
* @version
*/
public abstract class SessionBeanImpl implements SessionBean{

	protected Context context = null;
	
	protected Logger logger = Logger.getLogger(SessionBeanImpl.class);

	/**
	 * @return
	 */
	public Context getContext() {
		return new ContextImpl();
	}

	/**
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
	
}

/**************************************************************************
 * $$RCSfile: SystemException.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: SystemException.java,v $
 * $Revision 1.6  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.exception;

/**
*
* @author kidd
*/

public class SystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemException(Throwable root) {
		super(root);
	}

	public SystemException(String string, Throwable root) {
		super(string, root);
	}

	public SystemException(String s) {
		super(s);
	}
}







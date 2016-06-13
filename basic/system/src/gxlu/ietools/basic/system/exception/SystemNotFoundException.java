/**************************************************************************
 * $$RCSfile: SystemNotFoundException.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: SystemNotFoundException.java,v $
 * $Revision 1.6  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.exception;

/**
 *
 * @author kidd
 */
public class SystemNotFoundException extends ClassNotFoundException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemNotFoundException() {
		super();
	}

	public SystemNotFoundException(String string, Throwable root) {
		super(string, root);
	}

	public SystemNotFoundException(String s) {
		super(s);
	}

}







/**************************************************************************
 * $$RCSfile: ThreadException.java,v $$  $$Revision: 1.4 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: ThreadException.java,v $
 * $Revision 1.4  2010/04/20 02:08:07  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.exception;

/**
 * 
 * @author kidd
 *
 */

public class ThreadException extends BaseException implements java.io.Serializable{

	public ThreadException(String errorCode) {
		super(errorCode);
	}
	
	
	/**
	 * @see com.esolution.common.exception.BaseException#BaseException(String, String)
	 */
	public ThreadException(String errorCode, String errorMsg)  {
		super(errorCode, errorMsg);
	}
}







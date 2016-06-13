/**************************************************************************
 * $$RCSfile: PropertyException.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:03 $$
 *
 * $$Log: PropertyException.java,v $
 * $Revision 1.6  2010/04/20 02:08:03  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.property.exception;

/**
 * 
 * @author kidd
 *
 */

public class PropertyException extends BaseException implements java.io.Serializable{

	public PropertyException(String errorCode) {
		super(errorCode);
	}
	
	
	/**
	 * @see com.esolution.common.exception.BaseException#BaseException(String, String)
	 */
	public PropertyException(String errorCode, String errorMsg)  {
		super(errorCode, errorMsg);
	}
}







/**************************************************************************
 * $$RCSfile: BaseException.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:03 $$
 *
 * $$Log: BaseException.java,v $
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
public class BaseException extends Exception implements java.io.Serializable{

		private String errorCode;
	
	
		/**
		 * @see java.lang.Throwable#Throwable(String)
		 */
		public BaseException(String errorCode) {
			this.errorCode = errorCode;	
		}

		/**
		 * Method BaseException.
		 * @param errorCode
		 * @param errorMsg
		 */
		public BaseException(String errorCode, String errorMsg)  {
			super(errorMsg);
			this.errorCode = errorCode;
		}

		/**
		 * Method getErrorCode.
		 * @return String
		 */
		public String getErrorCode() {
			return errorCode;
		}

		/**
		 * Method setErrorCode.
		 * @param errorCode
		 */
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}


	}

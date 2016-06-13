/**************************************************************************
 * $$RCSfile: BaseException.java,v $$  $$Revision: 1.4 $$  $$Date: 2010/04/20 02:08:07 $$
 *
 * $$Log: BaseException.java,v $
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

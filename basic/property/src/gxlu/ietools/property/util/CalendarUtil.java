/*
 * Created on 2008-02-01
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 * 
 * Copyright ? 2008 Hangzhou ewall Co. Ltd.
 * All right reserved
 *
 * @author kidd     
 * Created on 2008-02-01
 */
package gxlu.ietools.property.util;

import java.text.SimpleDateFormat;


/**
 * Title:<p>
 * Description:<p>
 * Copyright:Copyright (c) 2005<p>
 * Company: <p>
 * @author kidd
 * @version 1.0
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CalendarUtil {
	
	/**
	 * To convert a date time object to a String according to the given date style
	 * @param dateTime the datetime object that is to be converted
	 * @param dateFormat the date format that the convert must conform to
	 * @return a formatted String object
	 * @throws java.sql.SQLException
	 */
	public static String date2FormattedString(
		java.util.Date dateTime,
		String dateFormat) {
		SimpleDateFormat customDateFormat = new SimpleDateFormat(dateFormat);
		return customDateFormat.format(dateTime);
	}
}
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
package gxlu.ietools.basic.system.util;

import gxlu.ietools.basic.collection.util.DynamicObject;

import java.util.*;

/**
 * @author wang yeliang
 *
 * 
 * 
 */

public class ArrayUtil {
	public static LinkedList getStringType(List objectList){
		LinkedList returnList = new LinkedList();
		
		for(int i=0;i<objectList.size();i++){
			Object object = objectList.get(i);
			if(object instanceof DynamicObject){
				returnList.add(object);
			}else if(object instanceof Object[]){
				Object[] dataValue = (Object[])objectList.get(i);
				returnList.add(i,dataValue[0]);
			}
		}
		
		return returnList;
	}
}

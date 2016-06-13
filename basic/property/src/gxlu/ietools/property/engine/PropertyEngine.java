/*
 * Created on 2004-9-10
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package gxlu.ietools.property.engine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPowerCable;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPowerInPort;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPowerOutPort;
import gxlu.ossc.inventory.basic.common.bizobject.powernetwork.BPowerPort;
import gxlu.ossc.inventory.common.bizobject.power.BPowerPortConnect;
import gxlu.ossc.mobilenetwork.common.bizobject.logical.BMNTrunk;

public class PropertyEngine {
	
	public static void main(String[] args) {
		readWrite(BPowerPortConnect.class, "c:\\PowerPortConnect.xml");
	}

	/**
	 * @param args
	 */
	public static StringBuffer getTemplateBuffer(Class classes) {
			Property property = PropertyHelper.getProperty(classes);
			PropertyValue[] fieldArray = PropertyHelper.getPropertyFields(classes);
			PropertyObject[] fieldObjectArray = PropertyHelper.getPropertyObjectFields(classes);

			StringBuffer buffer = new StringBuffer();
			buffer.append("<?xml version=\"1.0\" encoding=\"GBK\"?> \n");
			buffer.append("<gxlu-mapping> \n");
			buffer.append("	<bclass name=\""+property.getBclass()+"\" \n");
			buffer.append("		cname=\""+property.getCname()+"\" \n");
			buffer.append("		by_insert=\"" + property.getByInsert() + "\" \n");
			buffer.append("		by_update=\"" + property.getByUpdate() + "\" \n");
			buffer.append("		thd_line_num=\"" + property.getThdLineNum() + "\" \n");
			buffer.append("		thd_line_max=\"" + property.getThdLineMax() + "\" \n");
			buffer.append("	> \n");
			
			if(fieldArray.length>0) {
				for(int i=0;i<fieldArray.length;i++){
					PropertyValue propertyValue = (PropertyValue)fieldArray[i];
					buffer.append("	<property \n");
					buffer.append("		name=\"" + propertyValue.getName() + "\" \n");
					buffer.append("		column_title=\"" + propertyValue.getColumnTitle() + "\" \n");
					buffer.append("		datadict_class=\"" + propertyValue.getDatadictClass() + "\" \n");
					buffer.append("		datadict_attr=\"" + propertyValue.getDatadictAttr() + "\" \n");
					buffer.append("		isDatadict=\"" + propertyValue.isDatadict() + "\" \n");
					buffer.append("		isQuery=\"" + propertyValue.getIsQuery() + "\" \n");
					buffer.append("	/> \n");
				}
			}
			
			if(fieldObjectArray.length>0) {
				for(int i=0;i<fieldObjectArray.length;i++){
					PropertyObject propertyObject = (PropertyObject)fieldObjectArray[i];
					buffer.append("	<object-to-object \n");
					buffer.append("		name=\"" + propertyObject.getName() + "\" \n");
					buffer.append("		column_title=\"" + propertyObject.getColumnTitle() + "\" \n");
					buffer.append("		join-column=\"" + propertyObject.getJoinColumn() + "\" \n");
					buffer.append("		class=\"" + propertyObject.getClassName() + "\" \n");
					buffer.append("		class-column=\"" + propertyObject.getClassColumn() + "\" \n");
					buffer.append("		isQuery=\"" + propertyObject.getIsQuery() + "\" \n");
					buffer.append("		query_class=\"" + propertyObject.getQueryClass() + "\" \n");
					buffer.append("	/> \n");
				}
			}
			buffer.append("	</bclass> \n");
			buffer.append("</gxlu-mapping> \n");
			
			return buffer;
	}
	
	/**
	 * 
	 */
	public PropertyEngine() {		
		
	}

	public static void readWrite(Class classes, String paths) {
		try{
			File file = new File(paths);
			if(!file.exists()){
				file.createNewFile();
			}

			FileOutputStream  outfile = new FileOutputStream(file);
			BufferedOutputStream  bufferout = new BufferedOutputStream(outfile);

			StringBuffer buffer = PropertyEngine.getTemplateBuffer(classes);
			
			byte b[]=buffer.toString().getBytes();
			bufferout.write(b);
			bufferout.flush();
			bufferout.close();
			outfile.close();

		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

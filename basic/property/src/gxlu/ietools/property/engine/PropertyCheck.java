package gxlu.ietools.property.engine;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.util.ReflectHelper;
import gxlu.ietools.property.xml.DomHelper;
import gxlu.ietools.property.xml.DomTemplateParse;

public class PropertyCheck {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DomHelper domHelper = new DomHelper(new DomTemplateParse());
		List parseList = domHelper.readDomParseAllList();

		Iterator it = parseList.iterator();
		while(it.hasNext()){
			List proList = (List)it.next();
			Property property = (Property)proList.get(1);
			Field[] field = ReflectHelper.getAllFields(property.getBclass());
			
			System.out.println("[B类名称] -- "+property.getBclass());
			
			//查找在B类中无法找到的属性
			List propertyValueList = property.getPropertyValue();
			for(int j=0;j<propertyValueList.size();j++){
				PropertyValue propertyValue = (PropertyValue)propertyValueList.get(j);
				for(int i=0;i<field.length;i++){
					Field fields = field[i];
					if(fields.getName().equalsIgnoreCase(propertyValue.getName())){
						propertyValueList.remove(j);
						j--;
					}
				}
			}
			
			if(propertyValueList.size()>0){
				System.out.println("	无法找到的普通属性名称为：");
				for(int k=0;k<propertyValueList.size();k++){
					PropertyValue propertyValue = (PropertyValue)propertyValueList.get(k);
					System.out.println("		 -- "+propertyValue.getName());
				}
			}
			
			
			//查找在B类中无法找到的对象属性
			List propertyObjectList = property.getPropertyObject();
			for(int j=0;j<propertyObjectList.size();j++){
				PropertyObject propertyObject = (PropertyObject)propertyObjectList.get(j);
				for(int i=0;i<field.length;i++){
					Field fields = field[i];
					if(fields.getName().equalsIgnoreCase(propertyObject.getName())){
						propertyObjectList.remove(j);
						j--;
					}
				}
			}
			
			if(propertyObjectList.size()>0){
				System.out.println("	无法找到的对象属性名称为：");
				for(int k=0;k<propertyObjectList.size();k++){
					PropertyObject propertyObject = (PropertyObject)propertyObjectList.get(k);
					System.out.println("		 -- "+propertyObject.getName());
				}
			}
		}
	}

}

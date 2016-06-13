/**************************************************************************
 * $$RCSfile: ContainerBootStrap.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:05 $$
 *
 * $$Log: ContainerBootStrap.java,v $
 * $Revision 1.6  2010/04/20 02:08:05  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.container;

import java.util.ArrayList;
import java.util.List;

import gxlu.ietools.basic.system.util.ClassNoteNames;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.xml.DomParse;
import gxlu.ietools.property.xml.DomTemplateParse;

import org.apache.log4j.Logger;

/**
 *
 * @author  kidd
 * @version
 */
public class ContainerBootStrap {

	protected static Logger logger = Logger.getLogger(ContainerBootStrap.class);

	public void startup() {
		loadSessionBean();
		loadBobject();
		loadElementDef();
	}

	/**
	 * 初始化加载会话处理
	 */
	public void loadSessionBean(){
		System.out.println("[ContainerBootStrap]: start load Session Bean" );
		Container container = ContainerFactory.getContainer();
		container.addSessionBean(ClassNoteNames.ARRAYLOADER_BEAN,"gxlu.ietools.basic.collection.impl.ArrayLoaderImpl");
		System.out.println("[ContainerBootStrap]: end load Session Bean" );
	}
	
	public void loadElementDef(){
		System.out.println("[ContainerBootStrap]: start load Element Def" );
		Container container = ContainerFactory.getContainer();
		container.addElementDef(ClassNoteNames.NULLELEMENT_INTERFACE,"gxlu.ietools.basic.elements.definition.NullElementDef");
		container.addElementDef(ClassNoteNames.UNIQUEELEMENT_INTERFACE,"gxlu.ietools.basic.elements.definition.UniqueElementDef");
		container.addElementDef(ClassNoteNames.LEVELSELEMENT_INTERFACE,"gxlu.ietools.basic.elements.definition.LevelsElementDef");
		container.addElementDef(ClassNoteNames.ITERATORELEMENT_INTERFACE,"gxlu.ietools.basic.elements.definition.IteratorElementDef");
		System.out.println("[ContainerBootStrap]: end load Element Def" );
	}
	
	/**
	 * 初始化加载B类
	 */
	public void loadBobject(){
		System.out.println("[ContainerBootStrap]: start load BObject" );
		Container container = ContainerFactory.getContainer();
		domParseContainer(container);
		System.out.println("[ContainerBootStrap]: end load BObject" );
	}
	
	/**
	 * 初始化模板B类放入容器
	 * @param container
	 */
	private void domParseContainer(Container container){
		DomParse domParse = new DomTemplateParse();
		List templateFileList =  domParse.getTemplateFileList();
		try {
			if(templateFileList.size()>0){
				for(int i=0;i<templateFileList.size();i++){
					List params = new ArrayList();
					params.add(0,(String)templateFileList.get(i));
					Property property = (Property)domParse.readDomParse(params,2);
					container.addBobject((String)templateFileList.get(i),Class.forName(property.getBclass()));
					container.addBobjectInfo(Class.forName(property.getBclass()), (String)templateFileList.get(i));
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

}
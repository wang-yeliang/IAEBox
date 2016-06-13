package gxlu.ietools.property.xml;

import gxlu.afx.framework.util.DOMWriter;
import gxlu.ietools.basic.elements.variables.XmlNode;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.util.ReflectHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomTemplateParse implements DomParse{

    protected static Logger log = Logger.getLogger(DomTemplateParse.class);
	
	public Object readDomParse(List params,int workFlag) {
		String inFile = (String)params.get(0);
		
		Property property = new Property();
		List propertyValueList = new ArrayList();
		List propertyObjectList = new ArrayList();
		try {
			DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder=domfac.newDocumentBuilder();
			InputStream is=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				is=this.getClass().getClassLoader().getResourceAsStream(inFile);
			}else{
				is=new FileInputStream(new File(inFile));
			}
			Document doc=dombuilder.parse(is);
			Element root=doc.getDocumentElement();
			
			NodeList books = root.getChildNodes();
			if(books!=null){
				for(int i=0;i<books.getLength();i++){
					Node book=books.item(i);
					if(book.getNodeType()==Node.ELEMENT_NODE){
						
						if(book.getNodeName().equals("bclass")){
							property.setBclass(book.getAttributes().getNamedItem("name").getNodeValue());
							Node cnameNode = book.getAttributes().getNamedItem("cname");
							if(cnameNode!=null){
								property.setCname(cnameNode.getNodeValue());
							}
							property.setByInsert(book.getAttributes().getNamedItem("by_insert").getNodeValue());
							property.setByUpdate(book.getAttributes().getNamedItem("by_update").getNodeValue());
							property.setThdLineNum(book.getAttributes().getNamedItem("thd_line_num").getNodeValue());
							property.setThdLineMax(book.getAttributes().getNamedItem("thd_line_max").getNodeValue());
							
							for(Node node=book.getFirstChild();node!=null;node=node.getNextSibling()){
								if(node.getNodeType()==Node.ELEMENT_NODE){
									if(node.getNodeName().equals("property")){
											if(DomHelper.getQueryTagCheck(node, workFlag)){
												PropertyValue propertyValue = new PropertyValue();
												propertyValue.setName(node.getAttributes().getNamedItem("name").getNodeValue());
												propertyValue.setColumnTitle(node.getAttributes().getNamedItem("column_title").getNodeValue());
												propertyValue.setDatadict(Boolean.parseBoolean(node.getAttributes().getNamedItem("isDatadict").getNodeValue()));
												propertyValue.setIsQuery(node.getAttributes().getNamedItem("isQuery").getNodeValue());
												propertyValue.setDatadictClass(node.getAttributes().getNamedItem("datadict_class").getNodeValue());
												propertyValue.setDatadictAttr(node.getAttributes().getNamedItem("datadict_attr").getNodeValue());
												propertyValueList.add(propertyValue);
											}
									}

									if(node.getNodeName().equals("object-to-object")){
										if(DomHelper.getQueryTagCheck(node, workFlag)){
											PropertyObject propertyObject = new PropertyObject();
											propertyObject.setName(node.getAttributes().getNamedItem("name").getNodeValue());
											propertyObject.setColumnTitle(node.getAttributes().getNamedItem("column_title").getNodeValue());
											propertyObject.setClassName(node.getAttributes().getNamedItem("class").getNodeValue());
											propertyObject.setJoinColumn(node.getAttributes().getNamedItem("join-column").getNodeValue());
											propertyObject.setClassColumn(node.getAttributes().getNamedItem("class-column").getNodeValue());
											propertyObject.setIsQuery(node.getAttributes().getNamedItem("isQuery").getNodeValue());
											propertyObject.setQueryClass(node.getAttributes().getNamedItem("query_class").getNodeValue());
											propertyObjectList.add(propertyObject);
										}
									}
								}
							}
						}
						property.setPropertyValue(propertyValueList);
						property.setPropertyObject(propertyObjectList);
					}
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return property;
	}
	
	public Object getAllProperty(List params){
		String bClassPath = (String)params.get(0);
		String cname = (String)params.get(1);
		Class bClass;
		Property property=new Property();
		property.setBclass(bClassPath);
		property.setCname(cname);
		try {
			Field fields[]=ReflectHelper.getAllFields(bClassPath);
			PropertyValue propertyValue=null;
			PropertyObject propertyObject=null;
			List propertyValueList=new ArrayList();
			List propertyObjectList=new ArrayList();
			for(int i=0;i<fields.length;i++){
				Field field=fields[i];
				String type=field.getType().getName();
				if(!ReflectHelper.isObjectProperty(type)){
					propertyValue=new PropertyValue();
					propertyValue.setName(field.getName());
					propertyValueList.add(propertyValue);
				}else{					
					propertyObject=new PropertyObject();
					propertyObject.setName(field.getName());
					propertyObjectList.add(propertyObject);
				}
			}
			property.setPropertyValue(propertyValueList);
			property.setPropertyObject(propertyObjectList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return property;
	}
	
	public void propertyOrderbyCol(Property property){
		for(int i=0;i<property.getPropertyValue().size()-1;i++){
			PropertyValue propertyValuei=(PropertyValue)property.getPropertyValue().get(i); 
			for(int j=i+1;j<property.getPropertyValue().size();j++){
				PropertyValue propertyValuej=(PropertyValue)property.getPropertyValue().get(j); 
				if(Integer.parseInt(propertyValuei.getColumnSeq())>Integer.parseInt(propertyValuej.getColumnSeq())){
					PropertyValue newPropertyValue=propertyValuei;
					propertyValuei=propertyValuej;
					propertyValuej=newPropertyValue;					
				}
			}
		}
		for(int i=0;i<property.getPropertyObject().size()-1;i++){
			PropertyObject propertyObjecti=(PropertyObject)property.getPropertyObject().get(i); 
			for(int j=i+1;j<property.getPropertyObject().size();j++){
				PropertyObject propertyObjectj=(PropertyObject)property.getPropertyObject().get(j); 
				if(Integer.parseInt(propertyObjecti.getColumnSeq())>Integer.parseInt(propertyObjectj.getColumnSeq())){
					PropertyObject newPropertyObject=propertyObjecti;
					propertyObjecti=propertyObjectj;
					propertyObjectj=newPropertyObject;					
				}
			}
		}

	}
	
	public List writeDomParse(List params,Object object) {
		List resultList=new ArrayList();
		boolean result=true;
		StringBuffer resutlStr=new StringBuffer();
		String inFile = (String)params.get(0);
        try {
			DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder=domfac.newDocumentBuilder();
			InputStream is=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				is=this.getClass().getClassLoader().getResourceAsStream(inFile);
			}else{
				is=new FileInputStream(new File(inFile));
			}
			Document doc=dombuilder.parse(is);
			Element root=doc.getDocumentElement();
			NodeList books = root.getChildNodes();
			if(books!=null){
				for(int i=0;i<books.getLength();i++){
					Node book=books.item(i);
					if(book.getNodeType()==Node.ELEMENT_NODE){
						if(book.getNodeName().equals("bclass")){
							Node previousNode=book.getPreviousSibling();
							String str="";
							if(previousNode!=null&&previousNode.getNodeType()==Node.TEXT_NODE)
								str=previousNode.getNodeValue().substring(previousNode.getNodeValue().lastIndexOf("\n"))+"\t";
							else{
								str="\n\t\t";
							}
							NodeList childNodes=book.getChildNodes();
							//删除此节点下的所有子节点(清空所有属性)
							for(int j=childNodes.getLength()-1;j>=0;j--){
								book.removeChild(childNodes.item(j));
							}
							//重新向此xml中添加属性
							Property property=object instanceof Property?(Property)object:null;
							if(property!=null){
								//一般属性
								List propertyValueList=property.getPropertyValue();
								for(int j=0;j<propertyValueList.size();j++){
									Object o=propertyValueList.get(j);
									PropertyValue propertyValue=o!=null&&o instanceof PropertyValue?(PropertyValue)o:null;
									if(propertyValue!=null){
										Element childNode=doc.createElement("property");
										childNode.setAttribute("name", propertyValue.getName());
										childNode.setAttribute("column_title",propertyValue.getColumnTitle());
										childNode.setAttribute("isDatadict",String.valueOf(propertyValue.isDatadict()));
										childNode.setAttribute("datadict_attr",String.valueOf(propertyValue.getDatadictAttr()));
										childNode.setAttribute("datadict_class",String.valueOf(propertyValue.getDatadictClass()));
										childNode.setAttribute("isQuery",String.valueOf(propertyValue.getIsQuery()));
										book.appendChild(doc.createTextNode(str));
										book.appendChild(childNode);
									}					
								}
								//引用属性
								List propertyObjectList=property.getPropertyObject();
								for(int j=0;j<propertyObjectList.size();j++){
									Object o=propertyObjectList.get(j);
									PropertyObject propertyObject=o!=null&&o instanceof PropertyObject?(PropertyObject)o:null;
									if(propertyObject!=null){
										Element childNode=doc.createElement("object-to-object");
										childNode.setAttribute("name", propertyObject.getName());
										childNode.setAttribute("column_title",propertyObject.getColumnTitle());
										childNode.setAttribute("join-column",propertyObject.getJoinColumn());
										childNode.setAttribute("class",propertyObject.getClassName());
										childNode.setAttribute("class-column",propertyObject.getClassColumn());
										childNode.setAttribute("isQuery",propertyObject.getIsQuery());
										childNode.setAttribute("query_class",propertyObject.getQueryClass());
										book.appendChild(doc.createTextNode(str));
										book.appendChild(childNode);
									}
								}
								book.appendChild(doc.createTextNode(str.substring(0, str.lastIndexOf("\t"))));
							}
						}
					}
				}
			}
			OutputStream out=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				String filePath=this.getClass().getClassLoader().getResource(inFile).getPath();
				String jarUrl=filePath.substring(6,filePath.indexOf("/template")-1);
		        File  file  =  new  File(jarUrl);
		 	    JarFile jarFile = null;
		 	    Enumeration<JarEntry> enum1=null;
		 	    try {
		 			jarFile = new JarFile(file);
		 			enum1 = jarFile.entries();
		 		
	 			} catch (IOException e) {
	 	 			e.printStackTrace();
	 	 		}
	 			JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(file));
	 			while(enum1.hasMoreElements()){
	 				JarEntry jarEntry=enum1.nextElement();
	 				jarOutputStream.putNextEntry(jarEntry);
	 			}
	 			out=jarOutputStream;
			}else{
				out=new FileOutputStream(new File(inFile));
			}
            DOMWriter.print(doc, out, false);
			result=true;
		}catch (ParserConfigurationException e) {
			result=false;
			resutlStr.append("模板配置错误");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			result=false;
			resutlStr.append("找不到指定模板文件");
			e.printStackTrace();
		} catch (SAXException e) {
			result=false;
			resutlStr.append("封装SAX错误");
			e.printStackTrace();
		} catch (IOException e) {
			result=false;
			resutlStr.append("IO异常");
			e.printStackTrace();
		}
		resultList.add(true);
		resultList.add(resutlStr);
		return resultList;
	}
	
	public List getTemplateFileList() {
		List templateList=new ArrayList();
		ClassLoader cl = this.getClass().getClassLoader();
	    java.net.URL u= cl.getResource("template");
	    String url = null;
		try {
			url = new String(u.getPath().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    if(url.indexOf("jar")>0){
	    	String jarUrl=url.substring(6,u.getPath().indexOf("/template")-1);
	        File  file  =  new  File(jarUrl);
	 	    JarFile jarFile = null;
	 	    try {
	 			 jarFile = new JarFile(file);
	 			 Enumeration<JarEntry> enum1 = jarFile.entries();
	 			 while (enum1.hasMoreElements()) {
	                JarEntry obj = enum1.nextElement();
	                if(!obj.isDirectory()&&obj.getName().length()>9&&obj.getName().substring(0, 9).equals("template/"))
	 		       	{
	             	   templateList.add(obj.getName());
	             	   System.out.println(obj.getName());
	 		       	}
	 			 }
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		}
	    }else{
	        File  file  =  new  File(url);
	        String[] files = file.list();
	        if(files!=null&&files.length>0){
			    for(int i=0;i<files.length;i++){
			    	String patch = u.getPath()+"/"+files[i];
			    	templateList.add(patch);
			    }
			}
	    }
		return templateList;
    }
	
	public XmlNode[] parseElements(String inFile) {
        long startTime = System.currentTimeMillis();
        List xmlNodeList = new ArrayList();
        
        try {
			DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder=domfac.newDocumentBuilder();
			InputStream is=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				is=this.getClass().getClassLoader().getResourceAsStream(inFile);
			}else{
				is=new FileInputStream(new File(inFile));
			}
			Document doc=dombuilder.parse(is);
			Element root=doc.getDocumentElement();
			
			NodeList books = root.getChildNodes();
			if(books!=null){
				for(int i=0;i<books.getLength();i++){
					Node book=books.item(i);
					if(book.getNodeType()==Node.ELEMENT_NODE){
						if(book.getNodeName().equals("bean")){
							XmlNode xmlNode = new XmlNode();
							xmlNode.setName(book.getAttributes().getNamedItem("class").getNodeValue());
							
							for(Node node=book.getFirstChild();node!=null;node=node.getNextSibling()){
								if(node.getNodeType()==Node.ELEMENT_NODE){
									//targetObject
									if(node.getAttributes().getNamedItem("name").getNodeValue().equals("targetObject")){
										xmlNode.setTargetObject(node.getFirstChild().getNodeValue());
									}
									//targetMethod
									if(node.getAttributes().getNamedItem("name").getNodeValue().equals("targetMethod")){
										xmlNode.setTargetMethod(node.getFirstChild().getNodeValue());
									}
									//targetReturn
									if(node.getAttributes().getNamedItem("name").getNodeValue().equals("targetReturn")){
										xmlNode.setTargetReturn(node.getFirstChild().getNodeValue());
									}
								}
							}
							xmlNodeList.add(xmlNode);
						}
					}
				}
			}

            log.info("XML bean class in " + (System.currentTimeMillis() - startTime) + "ms.");
        } catch (Exception e) {
            try {
				throw new Exception(e.getMessage(), e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }

        return (XmlNode[]) xmlNodeList.toArray(new XmlNode[0]);
	}
	public static void main(String args[]){
		DomTemplateParse d=new DomTemplateParse();
		List params=new ArrayList();
		params.add("template/AC.xml");
//		d.writeDomParse(params, object);
		try {
			d.test("template/AC.xml");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	public void test(String inFile) throws ParserConfigurationException, IOException, SAXException{
		String filePath="D:/work/lib/teleworks_lib/client-lib/tw_conf.jar";
		DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder=domfac.newDocumentBuilder();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/AC.xml");
//		InputStream is=new FileInputStream(new File(inFile));
//		Document doc=dombuilder.parse(is);	
		
		
		OutputStream out=null;
//      DOMWriter.print(doc, new FileOutputStream(new File(inFile)),false);
//		String filePath=this.getClass().getClassLoader().getResource(inFile).getPath();
		if(filePath.indexOf("jar")>0){
//			String jarUrl=filePath.substring(6,filePath.indexOf("/template")-1)+inFile;
	        File  file  =  new  File("D:/work/lib/teleworks_lib/client-lib/tw_conf.jar");
	        FileOutputStream fileOutputStream= new FileOutputStream(file);
	 	    JarFile jarFile = null;
	 	    try {
	 			 jarFile = new JarFile(file);
	 			 Enumeration<JarEntry> enum1 = jarFile.entries();
	 			 JarEntry zipEntry=jarFile.getJarEntry(inFile);
			} catch (IOException e) {
	 			e.printStackTrace();
	 		}
            out = new JarOutputStream(fileOutputStream);
		}else{
			out=new FileOutputStream(new File(inFile));
		}
//		DOMWriter.print(doc, out, false);
	}
	
//	static Properties readJarProperty() throws IOException {
//		currentJarPath = URLDecoder.decode(com.taisys.ota.util.RunMain.class
//				.getProtectionDomain().getCodeSource().getLocation().getFile(),
//				"UTF-8"); // 获取当前Jar文件名，并对其解码，防止出现中文乱码
//		System.out.println("currentJarPath=" + currentJarPath);
//		JarFile currentJar = new JarFile(currentJarPath);
//
//		JarEntry dbEntry = currentJar
//				.getJarEntry("com/taisys/ota/util/jdbc.properties");
//		InputStream in = currentJar.getInputStream(dbEntry);
//		Properties p = new Properties();
//		p.load(in);
//
//		in.close();
//		return p;
//
//	}
//
//	static void updateJarProperty() throws IOException {
//		String currentJarPath = URLDecoder.decode(com.taisys.ota.util.RunMain.class
//				.getProtectionDomain().getCodeSource().getLocation().getFile(),
//				"UTF-8"); // 获取当前Jar文件名，并对其解码，防止出现中文乱码
//		String EntryName = "jdbc.properties";
//		currentPath = startServer();
//		byte[] data = null;
//		File properFile = new File(currentPath + "/jdbc.properties");
//		if (properFile.exists()) {
//			InputStream in = new FileInputStream(properFile);
//			data = getByte(in);
//		}
//		try {
//			JarFile jf = new JarFile(currentJarPath);
//			TreeMap<String, byte[]> tm = new TreeMap<String, byte[]>();
//			Enumeration es = jf.entries();
//			while (es.hasMoreElements()) {
//				JarEntry je = (JarEntry) es.nextElement();
//				byte[] b = getByte(jf.getInputStream(je));
//				tm.put(je.getName(), b);
//			}
//			JarOutputStream out = new JarOutputStream(new FileOutputStream(
//					currentJarPath));
//			Set set = tm.entrySet();
//			Iterator it = set.iterator();
//			boolean has = false;
//			while (it.hasNext()) {
//				Map.Entry me = (Map.Entry) it.next();
//				String name = (String) me.getKey();
//				JarEntry jeNew = new JarEntry(name);
//				// System.out.println("jeNew.getName()="+jeNew.getName());
//				out.putNextEntry(jeNew);
//				byte[] b;
//				if (name.equals(EntryName)) {
//					b = data;
//					has = true;
//				} else
//					b = (byte[]) me.getValue();
//				out.write(b, 0, b.length);
//			}
//			if (!has) {
//				JarEntry jeNew = new JarEntry(EntryName);
//				out.putNextEntry(jeNew);
//				out.write(data, 0, data.length);
//			}
//			out.flush();
//			out.finish();
//			out.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}

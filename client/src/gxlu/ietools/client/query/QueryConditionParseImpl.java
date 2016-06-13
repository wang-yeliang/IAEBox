package gxlu.ietools.client.query;

import gxlu.afx.framework.util.DOMWriter;
import gxlu.afx.system.common.xmlprocess.XMLFiles;
import gxlu.ietools.basic.elements.variables.XmlNode;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.mapping.PropertyObject;
import gxlu.ietools.property.mapping.PropertyValue;
import gxlu.ietools.property.xml.DomParse;
import gxlu.ietools.property.xml.DomTemplateParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class QueryConditionParseImpl implements QueryConditionParseIFC
{

	protected static Logger log = Logger.getLogger(QueryConditionParseImpl.class);

	public HashMap readDomParseList()
	{
		DomParse domTemplateParse = new DomTemplateParse();
		List params = domTemplateParse.getTemplateFileList();
		HashMap hmQueryItems = new HashMap();
		if (params.size() > 0)
		{
			for (int i = 0; i < params.size(); i++)
			{
				QueryItems queryItems = (QueryItems) this.readDomParse(params.get(i).toString());
				String TemplateName = queryItems.toString();
				hmQueryItems.put(TemplateName, queryItems);
			}
		}

		return hmQueryItems;
	}

	public Object readDomParse(String file)
	{
		String inFile = file;
		QueryItems queryItems = new QueryItems();
		HashMap hmQueryItems = new HashMap();

		try
		{
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			InputStream is=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				is=this.getClass().getClassLoader().getResourceAsStream(inFile);
			}else{
				is=new FileInputStream(new File(inFile));
			}
			Document doc = dombuilder.parse(is);
			Element root = doc.getDocumentElement();

			NodeList books = root.getChildNodes();
			if (books != null)
			{
				for (int i = 0; i < books.getLength(); i++)
				{
					Node book = books.item(i);
					if (book.getNodeType() == Node.ELEMENT_NODE)
					{
						if (book.getNodeName().equals("bclass"))
						{
							for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling())
							{
								if (node.getNodeType() == Node.ELEMENT_NODE)
								{
									if (node.getNodeName().equals("property"))
									{
										QueryItem queryItem = new QueryItem();
										String name = node.getAttributes().getNamedItem("name").getNodeValue();
										queryItem.setName(name);
										queryItem.setJncolumn(name);
										String title = node.getAttributes().getNamedItem("column_title").getNodeValue();
										queryItem.setTitle(title);
										String otype = node.getAttributes().getNamedItem("isQuery").getNodeValue();
										queryItem.setOtype(otype);
										String strType = node.getAttributes().getNamedItem("isDatadict").getNodeValue();
										if (strType.equalsIgnoreCase("true"))
										{
											queryItem.setType("DataDict");
											String dClass = node.getAttributes().getNamedItem("datadict_class")
													.getNodeValue();
											queryItem.setDclass(dClass);
											String dAttr = node.getAttributes().getNamedItem("datadict_attr")
													.getNodeValue();
											queryItem.setDattr(dAttr);
										}
										else if (strType.equalsIgnoreCase("false"))
											queryItem.setType("TextField");
										hmQueryItems.put(name + "_PV", queryItem);
									}

									else if (node.getNodeName().equals("object-to-object"))
									{
										QueryItem queryItem = new QueryItem();
										String name = node.getAttributes().getNamedItem("name").getNodeValue();
										queryItem.setName(name);
										// queryItem.setName(name + "Id");
										String title = node.getAttributes().getNamedItem("column_title").getNodeValue();
										queryItem.setTitle(title);
										String jncolumn = node.getAttributes().getNamedItem("join-column")
												.getNodeValue();
										queryItem.setJncolumn(jncolumn);
										String bObject = node.getAttributes().getNamedItem("class").getNodeValue();
										queryItem.setBObject(bObject);
										String bObcolumn = node.getAttributes().getNamedItem("class-column")
												.getNodeValue();
										queryItem.setBObcolumn(bObcolumn);
										String otype = node.getAttributes().getNamedItem("isQuery").getNodeValue();
										queryItem.setOtype(otype);
										String queryClass = node.getAttributes().getNamedItem("query_class")
												.getNodeValue();
										queryItem.setQclass(queryClass);
										queryItem.setType("ParentQuery");
										hmQueryItems.put(queryItem + "_PO".toString(), queryItem);
									}
								}
							}
							if (hmQueryItems != null)
							{
								queryItems.setTemplateName(inFile.substring(inFile.lastIndexOf("/") + 1, inFile
										.length()));
								queryItems.setQueryItemSet(hmQueryItems);
							}
						}
					}
				}
			}

		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return queryItems;
	}

	public List getAllPropertyList()
	{
		List params = this.getTemplateFileList();
		List bClassList = new ArrayList();
		List propertyList = null;
		for (int i = 0; i < params.size(); i++)
		{
			propertyList = new ArrayList();
			propertyList.add(params.get(i));
			Property property = (Property) this.readDomParse(params.get(i).toString());
			propertyList.clear();
			propertyList.add(property.getBclass());
			property = (Property) this.getAllProperty(propertyList);
			bClassList.add(property);
		}
		return bClassList;
	}

	public Object getAllProperty(List params)
	{
		String bClassPath = (String) params.get(0);
		Class bClass;
		Property property = new Property();
		property.setBclass(bClassPath);
		try
		{
			bClass = Class.forName(bClassPath);
			Field fields[] = bClass.getDeclaredFields();
			PropertyValue propertyValue = null;
			PropertyObject propertyObject = null;
			List propertyValueList = new ArrayList();
			List propertyObjectList = new ArrayList();
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				String type = field.getType().toString();
				if (type.toString().equals("class java.lang.String") || type.equals("char")
					|| type.equals("class java.lang.Character") || type.equals("long")
					|| type.equals("class java.lang.Long") || type.equals("int")
					|| type.equals("class java.lang.Integer") || type.equals("short")
					|| type.equals("class java.lang.Short") || type.equals("byte")
					|| type.equals("class java.lang.Byte") || type.equals("double")
					|| type.equals("class java.lang.Double") || type.equals("float")
					|| type.equals("class java.lang.Float") || type.equals("boolean")
					|| type.equals("class java.math.BigDecimal") || type.equals("BigDecimal")
					|| type.equals("class java.lang.Boolean") || type.equals("class java.util.Date"))
				{
					propertyValue = new PropertyValue();
					propertyValue.setName(field.getName());
					propertyValueList.add(propertyValue);
				}
				else
				{
					propertyObject = new PropertyObject();
					propertyObject.setName(field.getName());
					propertyObjectList.add(propertyObject);
				}
			}
			property.setPropertyValue(propertyValueList);
			property.setPropertyObject(propertyObjectList);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return property;
	}

	public void propertyOrderbyCol(Property property)
	{
		for (int i = 0; i < property.getPropertyValue().size() - 1; i++)
		{
			PropertyValue propertyValuei = (PropertyValue) property.getPropertyValue().get(i);
			for (int j = i + 1; j < property.getPropertyValue().size(); j++)
			{
				PropertyValue propertyValuej = (PropertyValue) property.getPropertyValue().get(j);
				if (Integer.parseInt(propertyValuei.getColumnSeq()) > Integer.parseInt(propertyValuej.getColumnSeq()))
				{
					PropertyValue newPropertyValue = propertyValuei;
					propertyValuei = propertyValuej;
					propertyValuej = newPropertyValue;
				}
			}
		}
		for (int i = 0; i < property.getPropertyObject().size() - 1; i++)
		{
			PropertyObject propertyObjecti = (PropertyObject) property.getPropertyObject().get(i);
			for (int j = i + 1; j < property.getPropertyObject().size(); j++)
			{
				PropertyObject propertyObjectj = (PropertyObject) property.getPropertyObject().get(j);
				if (Integer.parseInt(propertyObjecti.getColumnSeq()) > Integer.parseInt(propertyObjectj.getColumnSeq()))
				{
					PropertyObject newPropertyObject = propertyObjecti;
					propertyObjecti = propertyObjectj;
					propertyObjectj = newPropertyObject;
				}
			}
		}

	}

	public List writeDomParse(List params, Object object)
	{
		List resultList = new ArrayList();
		boolean result = true;
		StringBuffer resutlStr = new StringBuffer();
		String inFile = (String) params.get(0);
		try
		{
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			InputStream is=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				is=this.getClass().getClassLoader().getResourceAsStream(inFile);
			}else{
				is=new FileInputStream(new File(inFile));
			}		
			Document doc = dombuilder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList books = root.getChildNodes();
			if (books != null)
			{
				for (int i = 0; i < books.getLength(); i++)
				{
					Node book = books.item(i);
					if (book.getNodeType() == Node.ELEMENT_NODE)
					{
						if (book.getNodeName().equals("bclass"))
						{
							Node previousNode = book.getPreviousSibling();
							String str = "";
							if (previousNode != null && previousNode.getNodeType() == Node.TEXT_NODE)
								str = previousNode.getNodeValue().substring(
									previousNode.getNodeValue().lastIndexOf("\n"))
									+ "\t";
							else
							{
								str = "\n\t\t";
							}
							NodeList childNodes = book.getChildNodes();
							// 删除此节点下的所有子节点(清空所有属性)
							for (int j = childNodes.getLength() - 1; j >= 0; j--)
							{
								book.removeChild(childNodes.item(j));
							}
							// 重新向此xml中添加属性
							Property property = object instanceof Property ? (Property) object : null;
							if (property != null)
							{
								// 一般属性
								List propertyValueList = property.getPropertyValue();
								for (int j = 0; j < propertyValueList.size(); j++)
								{
									Object o = propertyValueList.get(j);
									PropertyValue propertyValue = o != null && o instanceof PropertyValue
											? (PropertyValue) o : null;
									if (propertyValue != null)
									{
										Element childNode = doc.createElement("property");
										childNode.setAttribute("name", propertyValue.getName());
										childNode.setAttribute("column_seq", propertyValue.getColumnSeq());
										childNode.setAttribute("column_title", propertyValue.getColumnTitle());
										childNode
												.setAttribute("isDatadict", String.valueOf(propertyValue.isDatadict()));
										book.appendChild(doc.createTextNode(str));
										book.appendChild(childNode);
									}
								}
								// 引用属性
								List propertyObjectList = property.getPropertyObject();
								for (int j = 0; j < propertyObjectList.size(); j++)
								{
									Object o = propertyObjectList.get(j);
									PropertyObject propertyObject = o != null && o instanceof PropertyObject
											? (PropertyObject) o : null;
									if (propertyObject != null)
									{
										Element childNode = doc.createElement("object-to-object");
										childNode.setAttribute("name", propertyObject.getName());
										childNode.setAttribute("column_seq", propertyObject.getColumnSeq());
										childNode.setAttribute("column_title", propertyObject.getColumnTitle());
										childNode.setAttribute("join-column", propertyObject.getJoinColumn());
										childNode.setAttribute("class", propertyObject.getClassName());
										childNode.setAttribute("class-column", propertyObject.getClassColumn());
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
			DOMWriter.print(doc, new FileOutputStream(new File(XMLFiles.getPath(inFile))), false);
			result = true;
		}
		catch (ParserConfigurationException e)
		{
			result = false;
			resutlStr.append("模板配置错误");
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			result = false;
			resutlStr.append("找不到指定模板文件");
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			result = false;
			resutlStr.append("封装SAX错误");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			result = false;
			resutlStr.append("IO异常");
			e.printStackTrace();
		}
		resultList.add(true);
		resultList.add(resutlStr);
		return resultList;
	}

	public List getTemplateFileList()
	{
		List templateList = new ArrayList();
		Class theClass = QueryConditionParseImpl.class;
		java.net.URL u = theClass.getResource("../template");
		File file = new File(u.getPath());
		String[] files = file.list();
		if (files != null && files.length > 0)
		{
			for (int i = 0; i < files.length; i++)
			{
				String patch = u.getPath() + "/" + files[i];
				int firstInt = patch.indexOf("gxlu");
				templateList.add(patch.substring(firstInt, patch.length()));
			}
		}
		return templateList;
	}

	public XmlNode[] parseElements(String inFile)
	{
		long startTime = System.currentTimeMillis();
		List xmlNodeList = new ArrayList();

		try
		{
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			InputStream is=null;
			if(this.getClass().getClassLoader().getResource(inFile)!=null&&this.getClass().getClassLoader().getResource(inFile).getPath().indexOf("jar")>0){
				is=this.getClass().getClassLoader().getResourceAsStream(inFile);
			}else{
				is=new FileInputStream(new File(inFile));
			}
			Document doc = dombuilder.parse(is);
			Element root = doc.getDocumentElement();

			NodeList books = root.getChildNodes();
			if (books != null)
			{
				for (int i = 0; i < books.getLength(); i++)
				{
					Node book = books.item(i);
					if (book.getNodeType() == Node.ELEMENT_NODE)
					{
						if (book.getNodeName().equals("bean"))
						{
							XmlNode xmlNode = new XmlNode();
							xmlNode.setName(book.getAttributes().getNamedItem("class").getNodeValue());

							for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling())
							{
								if (node.getNodeType() == Node.ELEMENT_NODE)
								{
									// targetObject
									if (node.getAttributes().getNamedItem("name").getNodeValue().equals("targetObject"))
									{
										xmlNode.setTargetObject(node.getFirstChild().getNodeValue());
									}
									// targetMethod
									if (node.getAttributes().getNamedItem("name").getNodeValue().equals("targetMethod"))
									{
										xmlNode.setTargetMethod(node.getFirstChild().getNodeValue());
									}
									// targetReturn
									if (node.getAttributes().getNamedItem("name").getNodeValue().equals("targetReturn"))
									{
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
		}
		catch (Exception e)
		{
			try
			{
				throw new Exception(e.getMessage(), e);
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return (XmlNode[]) xmlNodeList.toArray(new XmlNode[0]);
	}
}

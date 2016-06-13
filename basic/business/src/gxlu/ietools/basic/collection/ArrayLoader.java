package gxlu.ietools.basic.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import gxlu.ietools.basic.collection.util.DynamicObject;
import gxlu.ietools.basic.collection.util.ResultController;
import gxlu.ietools.basic.system.container.ContainerException;
import gxlu.ietools.basic.system.container.ContainerFactory;
import gxlu.ietools.basic.system.context.Context;
import gxlu.ietools.basic.system.context.SessionBeanImpl;
import gxlu.ietools.basic.system.util.ClassNoteNames;
import gxlu.ietools.basic.system.util.ExectionUtil;
import gxlu.ietools.basic.system.util.MethodNoteNames;
import gxlu.ietools.basic.threads.CalledThread;
import gxlu.ietools.basic.threads.TaskProcessors;
import gxlu.ietools.property.exception.PropertyException;
import gxlu.ietools.property.mapping.Property;
import gxlu.ietools.property.propertys.Getter;
import gxlu.ietools.property.propertys.PropertyAccessor;
import gxlu.ietools.property.propertys.PropertyAccessorFactory;
import gxlu.ietools.property.xml.DomHelper;
import gxlu.ietools.property.xml.DomTemplateParse;

public abstract class ArrayLoader extends SessionBeanImpl{

	protected List propertyList;
	protected Property property;
	protected String bobjectInfo;
	
	public void setBobjectInfo(String bobjectInfo) {
		this.bobjectInfo = bobjectInfo;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public void setPropertyList(List propertyList) {
		this.propertyList = propertyList;
	}

	/**
	 * @param bobject*-- B类对象 
	 * @param objectList*-- 数据对象集合
	 * @param propertyList*-- 模板元素集合
	 * @param rowsCount*-- 行数统计数组
	 * 			-- [0]* -- 总行数
	 *      	-- [1]* -- 开始行数
	 *      	-- [2]* -- 结束行数
	 *      	-- [3]* -- 当前行数
	 *      	-- [4]* -- 线程编号
	 * @param workFlag*-- 1-导入 2-导出
	 * @return
	 */
	protected abstract ResultController getBObjectListAction(Object bobject,List objectList,List propertyList,String bobjectInfo,int[] rowsCount,int workFlag);
	
	/**
	 * 验证项进行系列验证
	 * @param propertyList
	 * @param objectList
	 * @param workFlag
	 * @return
	 * @throws PropertyException
	 */
	protected abstract Object[] getChkError(List propertyList,List objectList,int workFlag);
	
	/**
	 * 方法描述:根据传入B类从容器中获得集合
	 * 
	 * @param Object*-- B类对象
	 * @param objectList*-- 对象集合
	 * @param workFlag*-- 1-导入 2-导出
	 *
	 * @return 对象集合
	 * 		result
	 * 			-- 如果 workFlag = 1，集合中为bobject
	 *      	-- 如果 workFlag = 2，集合中为DynamicObject
	 * 		objectValueCount* -- int
	 * 			-- 列数
	 * 		typeConvertError* -- 如果 workFlag = 1 为导入，将返回错误信息
	 * 			-- Object[]
	 *				- object[2]
	 * 				 + Integer* 行数
	 * 				 + String* 错误原因
	 * 
	 * 		property* -- 属性模板数据
	 * 
	 * 		operationMessage* -- 操作结果信息
	 * 			-- Object[]
	 *				- object[2]
	 * 				 + Boolean* true:正确 false:错误
	 * 		 		 + String* 错误原因
	 */
	public ResultController getBObjectList(Object bobject,List objectList,int workFlag){
		ResultController resultController = new ResultController();
		Context context = ExectionUtil.getContext();
		try {
			String bobjectInfo = (String)context.lookupBObjectInfo(bobject);
			setBobjectInfo(bobjectInfo);
			
			DomHelper domHelper = new DomHelper(new DomTemplateParse());
			List params = new ArrayList();
			params.add(0,bobjectInfo);
			setPropertyList(domHelper.getPropertyEngineList(params));
			setProperty(domHelper.getPropertyEngine(params));
			
			int thdLineMax = Integer.parseInt(property.getThdLineMax());
			int thdLineNum = Integer.parseInt(property.getThdLineNum());
			int objectNum = objectList.size();

			//在进入引擎前需要对验证项进行系列验证
			Object[] isCheckTitle = getChkError(propertyList,objectList,workFlag);
			
			if(((Boolean)isCheckTitle[0]).booleanValue()){
				//如果是导入，去除第一行的表头数据
				if(workFlag==1){
					objectList.remove(0);
					objectNum--;
				}
				
				context.removeObjectList();
        context.removeAllProperty();
        context.removeAllObjectListMap();
				if(workFlag==2){
					List valueList = new ArrayList();
					PropertyAccessor propertyAccessor = PropertyAccessorFactory.getDynamicMapPropertyAccessor();
					Getter getter = propertyAccessor.getGetter(DynamicObject.class);
					getter.setTitle(propertyList,valueList);
					ContainerFactory.addObjectData(valueList);
				}
				
				//判断是否启动多线程机制
				if(thdLineMax==0||objectNum<thdLineMax){
					int[] rowsCount = new int[4];
					rowsCount[0] = objectNum;//总数据
					rowsCount[1] = 0;
					rowsCount[2] = objectNum;
					resultController = getBObjectListAction(bobject,objectList,propertyList,bobjectInfo,rowsCount,workFlag);
				}else{
					if(objectNum>=thdLineMax){
						BigDecimal threadCount = new BigDecimal((float)objectNum/thdLineNum+1);
						
						TaskProcessors taskProcessors = new TaskProcessors(threadCount.intValue());
						taskProcessors.startWorkers();
						
						for(int i=0;i<threadCount.intValue();i++){
							int firstNum = i*thdLineNum;
							int lastNum = i*thdLineNum+thdLineNum;
							if(lastNum>objectNum){
								lastNum = objectNum;
							}
							int[] rowsCount = new int[4];
							rowsCount[0] = objectNum;
							rowsCount[1] = firstNum;
							rowsCount[2] = lastNum;
							
							List iParams = new ArrayList();
							iParams.add(0,bobject);
							iParams.add(1,objectList.subList(firstNum, lastNum));
							iParams.add(2,new Integer(workFlag));
							iParams.add(3,rowsCount);
							iParams.add(4,ClassNoteNames.ARRAYLOADER_BEAN);
							iParams.add(5,MethodNoteNames.BOBJECTLIST_ACTION);
							iParams.add(6,propertyList);
							iParams.add(7,bobjectInfo);
					        CalledThread alice = new CalledThread(iParams, taskProcessors);
					        alice.start();
						}
						
						listenThread(objectNum);
						
						taskProcessors.stopAllWorkers();
					}
				}
				ContainerFactory.addProperty(this.property);
        resultController.setPropertyMap(ContainerFactory.getAllProperty());
        resultController.setObjectListMap(ContainerFactory.getObjectListMap());
				resultController.setResult(ContainerFactory.getObjectList());
				resultController.setTypeConvertError(getTypeConvertError());
				resultController.setObjectValueCount(propertyList.size());
				resultController.setProperty(property);
			}
			resultController.setOperationMessage(isCheckTitle);

		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		}
		return resultController;
	}

	/**
	 * 根据传入的B类执行得到标题动态对象的过程
	 * @param bobject B类对象
	 * @return
	 */
	public abstract ResultController getTitleListAction(Object bobject);
	
	/**
	 * 方法描述:根据传入B类得到标题
	 * @param bobject B类对象
	 * @return 
	 */
	public ResultController getTitleList(Object bobject){
		try {
			String bobjectInfo = (String)context.lookupBObjectInfo(bobject);
			setBobjectInfo(bobjectInfo);
			
			DomHelper domHelper = new DomHelper(new DomTemplateParse());
			List params = new ArrayList();
			params.add(0,bobjectInfo);
			setPropertyList(domHelper.getPropertyEngineList(params));
			setProperty(domHelper.getPropertyEngine(params));
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());			
			logger.error(e.toString());
			logger.error(e.getMessage());
		}
		
		ResultController resultController = getTitleListAction(bobject);
		return resultController;
		
	}
	
	/**
	 * 监听线程是否已经完成工作
	 * @param objectNum
	 */
	protected void listenThread(int objectNum){
		while(true){
			LinkedList objectList = ContainerFactory.getObjectList();
			Object[] typeConErr = PropertyAccessorFactory.getTypeConvertError();
			//通过对象数量+错误对象数量=总对象数量
			if(objectList!=null&&typeConErr!=null){
				int objectSize = objectList.size() + typeConErr.length;
				if(objectSize>=objectNum){
					break;
				}
			}
		}
	}
	
	/**
	 * 类型转换错误信息
	 * 		-- Object[]
	 *			- object[2]
	 * 				+ Integer* 行数
	 * 				+ String* 错误原因
	 * @return
	 */
	private Object[] getTypeConvertError() {
		Object[] objects = null;
		try {
			objects = PropertyAccessorFactory.getTypeConvertError();
			PropertyAccessorFactory.removeTypeConvertError();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());
			logger.error(e.toString());
		}
		return objects;
	}
}

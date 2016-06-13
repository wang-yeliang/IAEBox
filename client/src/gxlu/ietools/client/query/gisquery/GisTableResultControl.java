package gxlu.ietools.client.query.gisquery;

import java.util.Vector;

import gis.common.dataobject.YObjectInterface;
import gxlu.afx.system.common.ObjectComparison;
import gxlu.afx.system.common.SwingCommon;
import gxlu.afx.system.query.client.ResultOperatorInterface;
import gxlu.afx.system.query.client.TableResultControl;
import gxlu.afx.system.query.client.TableValueInterface;
import gxlu.afx.system.query.common.QueryConst;

public class GisTableResultControl extends TableResultControl {
	
	public GisTableValueInterface tableValue = null;
	public GisTableResultControl(GisTableValueInterface tableValue,ResultOperatorInterface resultOperator) {
		super((TableValueInterface)tableValue,resultOperator);
		this.tableValue = tableValue;
//		resultOperator = resultOperator;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector getTableRowWithJudgementForPrint(YObjectInterface item)
	{
	  Vector vRet=getTableRowWithJudgement(item);
	  Vector visibleColNames = new Vector();
		//获得显示的列的列名
		int visibleNum=this.tableResult.getColumnCount();
		for (int i = 0; i < visibleNum; i++)
		{
		  visibleColNames.add(this.tableResult.getColumnName(i));
		}

		//重新构造要输出的数据
		Vector vTemp=new Vector();
		for(int i=0;i<visibleNum;i++)
		{
		  int index=originalColNames.indexOf(visibleColNames.get(i));
		  vTemp.add(vRet.get(index) ) ;
		}
		return vTemp;

	}
	/**
	 * Getst the table row vector with judgement
	 */
	private Vector getTableRowWithJudgement(YObjectInterface item){
		Vector vRet = null;
		if(bIsNewWay){
			vRet = getTableRowValues(item);
		} else{
			vRet = tableValue.getTableRow(item);
		}



		int lenColName = 0;

		if(vColName != null){
			lenColName = vColName.size();
		}

		int lenRet = 0;

		if(vRet != null){
			lenRet = vRet.size();
		}

		if(lenColName != lenRet && vRet != null){ //vRet may be null, this is valid
			System.out.println(
				"-------Length of row vector and column name doesn't match!");
		}

		return vRet;
	}
	
	public void addItem(YObjectInterface item){
		if(tableValue == null){
			return;
		}

		Vector vItem = getTableRowWithJudgement(item);

		if(vItem == null){
			return;
		}

		vItem.addElement(new Long(item.getId())); //Adds the Id column

		tablemodelResult.addRow(vItem);

		if(vResult == null){
			vResult = new Vector();
		}

		removeTableListSelectionListener();

		vResult.addElement(item);

		addTableListSelectionListener();

		iDisplayed += 1;
		iTotal += 1;
		iSelected = SwingCommon.getTableSelectedRowCount(tableResult);

		notifyRowCount();
	}

	/**
	 * [ResultInterface---->QueryResult]
	 */
	public void deleteItem(YObjectInterface item){
		int index = lookforIndexInTable(item);

		tablemodelResult.deleteRow(index);

		removeTableListSelectionListener();

		vResult.removeElement(item);

		addTableListSelectionListener();

		// add by brook
		iRemove += 1;

		iDisplayed -= 1;
		iTotal -= 1;
		iSelected = SwingCommon.getTableSelectedRowCount(tableResult);

		notifyRowCount();
	}

	/**
	 * [ResultInterface---->QueryResult]
	 */
	public void replaceItem(YObjectInterface oldItem,YObjectInterface newItem){
		if(tableValue == null){
			return;
		}

		Vector vItem = getTableRowWithJudgement(newItem);

		if(vItem == null){
			return;
		}

		vItem.addElement(new Long(newItem.getId())); //Adds the Id column

		int index = lookforIndexInTable(oldItem);

		tablemodelResult.updateRow(vItem,index);

		removeTableListSelectionListener();

		vResult.removeElement(oldItem);
		vResult.addElement(newItem);

		addTableListSelectionListener();

		iSelected = SwingCommon.getTableSelectedRowCount(tableResult);

		notifyRowCount();
	}
	
	public void setItems(Vector data,boolean clearOldData){
		if(tableValue == null){
			return;
		}
//Added by Harvey on 2003/1/27 for supporting 'clear history' (Begin)

		//setStatInfo() must have been called BEFORE this method executing

		//initializes for paging
		if(iOprIndicator == QueryConst.QUERY_DIRECTION_BACKWARD){
			clearOldData = true;
		}

		if(clearOldData){
			iDisplayedMinPageNoInCurrentQuery = iPageNo;
			iDisplayedMaxPageNoInCurrentQuery = iPageNo;
			iHistoryCount = 0;
		} else{
			if(iOprIndicator == QueryConst.QUERY_DIRECTION_START){
				iDisplayedMinPageNoInCurrentQuery = iPageNo; //iPageNo = 1 in fact
				iDisplayedMaxPageNoInCurrentQuery = iPageNo;
				iHistoryCount = tableResult.getRowCount();
			} else
			if(iOprIndicator == QueryConst.QUERY_DIRECTION_FORWARD){
				//iDisplayedMinPageNoInCurrentQuery keeps nochange
				iDisplayedMaxPageNoInCurrentQuery = iPageNo;
				//iHistoryCount keeps nochange
			}
		}

		if(clearOldData || iOprIndicator == QueryConst.QUERY_DIRECTION_START){
			iDisplayed = 0;
			iRepeatedCount = 0;
		} else{
			//keeps nochange
		}
		if(clearOldData){
			vResult = null;

			//Displays the data in the table
			Vector vAllData = null;
			Vector vOneItem = null;
			YObjectInterface item = null;

			if((data != null) && (data.size() != 0)){
				vAllData = new Vector();

				for(int i = 0; i < data.size(); i++){
					item = (YObjectInterface)data.elementAt(i);
					vOneItem = getTableRowWithJudgement(item);

					if(vOneItem != null){
						vOneItem.addElement(new Long(item.getId())); //Adds the Id column
						vAllData.addElement(vOneItem);
					}
				}
			}

			if((vAllData != null) && (vAllData.size() == 0)){
				vAllData = null;
			}

			removeTableListSelectionListener();

			tablemodelResult.replaceTableModel(vAllData);

			addTableListSelectionListener();

			vResult = data;
		}
		else{
			Vector vOneItem = null;
			YObjectInterface item = null;

			if((data != null) && (data.size() != 0)){
				removeTableListSelectionListener();

				for(int i = 0; i < data.size(); i++){
					item = (YObjectInterface)data.elementAt(i);
					if(GisObjectComparison.getIndexInVector(item,vResult) == -1){
						vOneItem = getTableRowWithJudgement(item);

						if(vOneItem != null){
							vOneItem.addElement(new Long(item.getId())); //Adds the Id column
							tablemodelResult.addRow(vOneItem);

							if(vResult == null){
								vResult = new Vector();
							}

							vResult.addElement(item);
						}
					}
					else{
						iRepeatedCount++;
					
				}

				addTableListSelectionListener();
			}
		}
		}
	}
	
	private Vector sortByWYSIWYG(Vector vOrigin){
		if(SwingCommon.getVectorLength(vOrigin) == 0
			|| tableResult.getRowCount() == 0){
			return null;
		}

		Vector vRet = new Vector(SwingCommon.getVectorLength(vOrigin));
		YObjectInterface boi = null;
		for(int i = 0; i < tableResult.getRowCount(); i++){
			boi = lookforObjectInVector(i);
			if(GisObjectComparison.getEqualObjectById(boi,vOrigin) != null){
				vRet.addElement(boi);
			}
		}
		return vRet;
	}
	
	public Vector getSelectedItems(){
		if(SwingCommon.getTableSelectedRowCount(tableResult) <= 0){
			return null;
		}

		if(vSelectedItems == null){
			vSelectedItems = new Vector();
		} else if(vSelectedItems.size() > 0){
			vSelectedItems.removeAllElements();
		}

		int[] iSel = tableResult.getSelectedRows();
		YObjectInterface item = null;

		for(int i = 0; i < iSel.length; i++){
			item = lookforObjectInVector(iSel[i]);
			vSelectedItems.addElement(item);
		}
		return sortByWYSIWYG(vSelectedItems);
	}
	
	public Vector getTableRowData(Vector vObjs){
		if(vObjs == null){
			return null;
		}

		Vector vRet = new Vector();
		Vector vOne = null;

	  if(!this.isPrinting)
		for(int i = 0; i < vObjs.size(); i++){
			vOne = getTableRowWithJudgement((YObjectInterface)vObjs.elementAt(i));
			vRet.addElement(vOne);
		}
	  else
		for(int i = 0; i < vObjs.size(); i++){
			vOne = getTableRowWithJudgementForPrint((YObjectInterface)vObjs.elementAt(i));
			vRet.addElement(vOne);
		}
	  this.isPrinting=false;

		return vRet;
	}
	
	private YObjectInterface lookforObjectInVector(int rowIndex){
		YObjectInterface item = null;
		int length = 0;

		if(vResult != null){
			length = vResult.size();
		}

		Vector v = tablemodelResult.getRowVector(rowIndex);
//	        Long l = (Long)v.elementAt(vColName.size());
		Long l = (Long)v.lastElement();//The last column, that is, the ID column

		for(int i = 0; i < length; i++){
			item = (YObjectInterface)vResult.elementAt(i);
			if(item.getId() == l.longValue()){
				return item;
			}
		}

		return null;
	}
	
	private int lookforIndexInTable(YObjectInterface item){
		Vector v = null;

		for(int i = 0; i < iDisplayed; i++){
			v = tablemodelResult.getRowVector(i);
			Long l = (Long)v.elementAt(vColName.size()); //The last column, that is, the ID column

			if(item.getId() == l.longValue()){
				return i;
			}
		}

		return -1;
	}
	
	private Vector getTableRowValues(YObjectInterface item){
		Vector v = null;
		String[] stra = tableValue.getSpecifiedColumns() != null ?
			tableValue.getSpecifiedColumns() :
			tableValue.getDisplayedColumns();

		int iSize = 0;
		if(stra != null){
			iSize = stra.length;
		}

		for(int i = 0; i < iSize; i++){
			if(v == null){
				v = new Vector();
			}
			v.addElement(tableValue.getDataOfTable(stra[i],item,
					GisTableValueInterface.TVI_GET_COLUMN_VALUE));
		}

		return v;
	}
}

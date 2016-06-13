package gxlu.ietools.basic.elements.definition.util;

import gxlu.afx.system.common.SysDictionaryFactory;
import gxlu.afx.system.common.interfaces.BObjectInterface;
import gxlu.afx.system.query.client.BQueryClient;
import gxlu.afx.system.query.common.QueryExpr;
import gxlu.afx.system.query.common.QueryExprBuilder;
import gxlu.afx.system.user.common.businessobject.BOperator;
import gxlu.ietools.basic.system.util.PropertyConstvalue;

import java.util.Vector;

import TOPLink.Public.Expressions.ExpressionBuilder;

public class ElementDefHelper {
	public static Object getBObject(BObjectInterface obj, String colName, String value, String assemble) {
		Vector resultObj = getResults(obj, colName, value, assemble);
		if ((resultObj != null) && (resultObj.size() > 0))
			return resultObj.get(0);
		return null;
	}

	public static Object getBObject(Class bClass, String colName, String value, String assemble) {
		Vector resultObj = getResults(bClass, colName, value, assemble);
		if ((resultObj != null) && (resultObj.size() > 0))
			return resultObj.get(0);
		return null;
	}

	public static Object getResult(Class bClass, String colName, String value,	String assemble) {
		return BQueryClient.getQueryResult(new QueryExprBuilder().get(colName).equal(value), bClass, assemble);
	}

	public static Vector getResults(Class bClass, String colName, String value, String assemble) {
		return BQueryClient.getQueryResults(new QueryExprBuilder().get(colName)
				.equal(value), bClass, assemble);
	}

	public static Vector getResults(BObjectInterface obj, String colName, String value, String assemble) {
		return BQueryClient.getQueryResults(new QueryExprBuilder().get(colName)
				.equal(value), obj.getClass(), assemble);
	}

	public static Vector getResultByQueryExpr(BObjectInterface obj, QueryExpr expr,String assemble) {
		return BQueryClient.getQueryResults(expr, obj.getClass(), assemble);
	}

	public static Vector getResultByQueryExpr(Class bClass, QueryExpr expr,String assemble) {
		return BQueryClient.getQueryResults(expr, bClass, assemble);
	}
	public static String getOrgNameOfOperator(BOperator bOperator,boolean	flag) {
		if (bOperator == null)
			return "";
		ExpressionBuilder builder = new ExpressionBuilder();
		BOperator dOperator = (BOperator) getBObject(BOperator.class, "id",String.valueOf(bOperator.getId()), "Operator[Organization]");
		if ((dOperator == null) || (dOperator.getOrganization() == null))
			return "";
		if(flag){
			return dOperator.getOrganization().toString();
		}else{
			return dOperator.getOrganization().getName();
		}
	}
	 public static final String NEW = SysDictionaryFactory.getSysDictionaryValueCN("DNSUBNEAUDITS","OPERATIONTYPE",PropertyConstvalue.DNSUBNEAUDITS_OPERATIONTYPE_NEW);
	  public static final String MODIFY = SysDictionaryFactory.getSysDictionaryValueCN("DNSUBNEAUDITS","OPERATIONTYPE",PropertyConstvalue.DNSUBNEAUDITS_OPERATIONTYPE_MODIFY);
	  public static final String DELETE = SysDictionaryFactory.getSysDictionaryValueCN("DNSUBNEAUDITS","OPERATIONTYPE",PropertyConstvalue.DNSUBNEAUDITS_OPERATIONTYPE_DELETE);
	  public static String getOperationType(byte operType)
	  {
	      switch(operType)
	      {
	          case PropertyConstvalue.PERSIST_TYPE_ADD:
	              return NEW;
	          case PropertyConstvalue.PERSIST_TYPE_UPDATE:
	              return MODIFY;
	          case PropertyConstvalue.PERSIST_TYPE_DELETE:
	              return DELETE;
	      }
	      return "δ֪";
	  }
}

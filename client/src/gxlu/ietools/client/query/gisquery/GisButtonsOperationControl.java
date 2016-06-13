package gxlu.ietools.client.query.gisquery;

import gis.common.dataobject.YObjectInterface;
import gxlu.afx.system.query.client.BQueryClient;
import gxlu.afx.system.query.client.ButtonsOperationControl;
import gxlu.afx.system.query.common.QueryExprBuilder;
import gxlu.afx.system.query.common.QueryParam;

import java.util.Vector;

public class GisButtonsOperationControl extends ButtonsOperationControl {
	
	 public GisButtonsOperationControl(UIGisBaseQueryDialog _dlg){
	        super(_dlg);
	    }
	 /**
     * Executes the whole querying operation
     */
    public Object execWholeQuery() throws Exception
    {
        Vector vResult = null;

        QueryParam param = new QueryParam();

        param.setExpression(conditionDisplayer.getExpression(new QueryExprBuilder()));
        param.setObjectClass(className);
        param.setAssembleString(strAssemble);
        param.setConstraintExpression(ceSecurity);
        param.setHiddenConstraint(expHidden);
        param.setIsVector(true);
        param.setIsPaged(false);//Query All result

        //Executes query operation
        //commented by JEFF, use the BQueryclient to get the whole results. While the queryMeoth can not
        if (queryMethod != null)
        {
//            //Using the customed query method
            vResult = queryMethod.execQuery(param);
        }
        else
        {
            //Using the default query method
            vResult = (Vector)BQueryClient.queryObject(param);
        }

        if ((vResult!= null) && (vResult.size()==1) && (vResult.elementAt(0).toString()=="null"))
        {
            vResult = null;
        }

        //filtering
        int iSize = (vResult == null) ? 0 : vResult.size();
        int i = 0;

        while (i < iSize)
        {
            if (! (vResult.elementAt(i) instanceof YObjectInterface))
            {
                vResult.removeElementAt(i);
                iSize --;
            }
            i ++;
        }

        vResult = resultDisplayer.getAttachedData(vResult);

        return vResult;
    }
}

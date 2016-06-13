package gxlu.ietools.service;

import gis.common.dataobject.YObjectInterface;
import gis.common.exception.GisException;
import gis.server.dbaccess.GisWrite;
import gis.server.implementation.Context;
import gxlu.ietools.property.util.ReflectHelper;

import java.sql.SQLException;
import java.util.Vector;

public class YIEToolsGisServer {
	private Context context;
	public YIEToolsGisServer(Context _context){
		context = _context;
	}
	public YObjectInterface import_create(YObjectInterface yObjectInterface) throws SQLException,GisException{
		Object name=ReflectHelper.invokeMethod(yObjectInterface, "getName", null, null);
		if(name==null||name.toString().equals("")){
			long id = GisWrite.createSequenceNumber (yObjectInterface.getTableName () ) ;
            yObjectInterface.setId ( id ) ;
            ReflectHelper.setValuebysetMethod(yObjectInterface, "setName", "cableterm_****" + id + "****");
		}
		Object code=ReflectHelper.invokeMethod(yObjectInterface, "getCode", null, null);
		if(code==null||code.toString().equals("")){
			Object uid=ReflectHelper.invokeMethod(yObjectInterface, "getUID", null, null);
			ReflectHelper.setValuebysetMethod(yObjectInterface, "setCode", uid);
		}
	        if ( !checkNoUnique ( yObjectInterface ) )
	        {
	            {
	                GisException ex = new GisException ( yObjectInterface ,GisException.EX_CREATE_FAIL ,"请检查名称和编码是否唯一！" ) ;
	                throw ex ;
	            }
	        }
	        YObjectInterface ret = ( YObjectInterface ) GisWrite.create ( context , yObjectInterface ) ;
//	        if ( ServerEnvironment.getIsStandalone () )
//	        {
//	            if ( ret.getObjectType () == GisConst.OBJECTTYPE_OT
//	                 || ret.getObjectType () == GisConst.OBJECTTYPE_CABLESTORE )
//	                updateRelatedHost ( ret ) ;
//	        }
	        return ret ;
	}
	 private boolean checkNoUnique ( YObjectInterface bObj )
    {
        boolean ret = false ;
        Vector attrs = new Vector ( 1 ) ;
        attrs.addElement ( "code" ) ;
        Vector vals = new Vector ( 1 ) ;
        Object code=ReflectHelper.invokeMethod(bObj, "getCode", null, null);
        vals.addElement ( code ) ;
        try
        {
            ret = GisWrite.checkStringUnique ( context , bObj , attrs , vals ) ;
        }
        catch ( SQLException e )
        {
            return false ;
        }
        return ret ;
    }
}

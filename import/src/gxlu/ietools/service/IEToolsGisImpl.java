package gxlu.ietools.service;

import gis.common.dataobject.GisConst;
import gis.common.dataobject.YObjectInterface;
import gis.common.event.GisEvent;
import gis.common.exception.GisException;
import gis.server.implementation.Context;
import gis.server.implementation.ObjectCache;
import gis.server.main.ServerEnvironment;
import gxlu.ietools.api.service.IEToolsGisIFC;
import gxlu.ietools.basic.system.util.ServiceName;

import java.sql.SQLException;
import java.util.Vector;

public class IEToolsGisImpl implements IEToolsGisIFC {

	public YObjectInterface import_create(long userKey ,YObjectInterface yObject)
			throws SQLException, GisException {
		Context context = Context.createContext ( userKey ) ;
		YIEToolsGisServer yIEToolsGisServer=new YIEToolsGisServer(context);
		YObjectInterface object = yIEToolsGisServer.import_create(yObject);
		context.end () ;
		processEvent(context);
		return object;
	}
	
	 private void processEvent ( Context context )
	    {
	        Vector events = context.getEvents () ;

	        //write cache
	        ObjectCache cache = ObjectCache.getInstance () ;
	        for ( int i = 0 ; i < events.size () ; i++ )
	        {
	            GisEvent event = ( GisEvent ) events.elementAt ( i ) ;
	            switch ( event.getType () )
	            {
	                case GisEvent.GISEVENT_CREATE:
	                {
	                    cache.putObject ( ( YObjectInterface ) event.getObject () ) ;
	                    break ;
	                }
	                case GisEvent.GISEVENT_UPDATE:
	                case GisEvent.GISEVENT_MOVE:
	                {
	                    cache.updateObject ( ( YObjectInterface ) event.getObject () ) ;
	                    break ;
	                }
	                case GisEvent.GISEVENT_DELETE:
	                {
	                    //add by wumin 2005-04-13 单机版
	                    if ( ServerEnvironment.getIsStandalone () )
	                    {
	                        // 删除实体时还要判断是否为新增实体，如果是新增的则直接删除
	                        YObjectInterface obj = (YObjectInterface) event.getObject();
	                        if ( obj.getModifyStatus () == GisConst.OBJECT_NEW )
	                            cache.removeObject ( obj ) ;
	                        else
	                            cache.updateObject ( obj ) ;
	                    }
	                    //end add by wumin
	                    else
	                    {
	                        cache.removeObject( (YObjectInterface) event.getObject());
	                    }
	                    break ;
	                }
	            }
	        }
	        events.clear () ;
	        events = null ;
	    }
	 
	public String getJndiNamePrefix() {
        return ServiceName.IETOOLS_GISSERVICE_JNDI+ ServerEnvironment.getDomain();
    }
}

package gxlu.ietools.api.service;

import gis.common.dataobject.YObjectInterface;
import gis.common.exception.GisException;

import java.sql.SQLException;

public interface IEToolsGisIFC {
	public YObjectInterface import_create(long userKey ,YObjectInterface yObject) throws SQLException,GisException;
}

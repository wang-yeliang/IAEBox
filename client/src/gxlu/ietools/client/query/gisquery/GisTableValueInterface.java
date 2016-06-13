//hfcao

/**************************************************************************
 *
 * $RCSfile: GisTableValueInterface.java,v $  $Revision: 1.1 $  $Date: 2010/06/13 01:54:07 $
 *
 * $Log: GisTableValueInterface.java,v $
 * Revision 1.1  2010/06/13 01:54:07  zhangj
 * 扩展管线导入导出查询
 *
 * Revision 1.1  2009/08/11 06:24:03  ximao
 * MR#:NMBF50-0000 teleworks10
 *
 * Revision 1.1.1.1  2002/03/26 03:58:27  hanweili
 * MR#:NMBF20-329.迁移统一版本交换网378数据网518代码
 *
 * Revision 1.3  2002/03/26 03:58:27  hfcao
 * improves the performance of 'print'
 *
 * Revision 1.2  2002/03/13 11:51:02  hfcao
 * remigrates from core1.1
 * adds the function to customize operations, check before querying, etc.
 *
 * Revision 1.3  2002/01/07 05:12:49  hfcao
 * improvements - customizing the query condition, query result
 * at run time
 *
 * Revision 1.2  2001/10/08 03:22:26  zma
 * Synchronize Core1.1 with Core1.0
 *
 * Revision 1.1  2001/08/27 06:42:56  zma
 * initialize for core
 *
 * Revision 1.1  2001/08/23 09:29:16  hfcao
 * Moves 'query' from netmaster to framework
 *
 * Revision 1.3  2001/04/17 09:39:48  wzwu
 * no message
 *
 * Revision 1.2  2001/04/12 06:07:50  zma
 * no message
 *
 * Revision 1.1.2.2  2000/11/17 07:35:29  hfcao
 * comments added
 *
 * Revision 1.1.2.1  2000/11/14 02:35:54  hfcao
 * no message
 *
 *
 *
 ***************************************************************************/

package gxlu.ietools.client.query.gisquery;

import gis.common.dataobject.YObjectInterface;

import java.util.Vector;
import java.awt.Dimension;



/**
 * A interface to provide neccessary parameters for the table's display
 */
public interface GisTableValueInterface
{
  //the following constants are used to be parameters of getDataOfTable() method

  //Indicates to request for the description of a specific table column,
  //which is packed in a TableDescriptor object
  public static final int TVI_GET_COLUMN_DESC           = 0;


  //Indicates to request for the value of a specific table column for a
  //BObject, which can be any Object
  public static final int TVI_GET_COLUMN_VALUE          = 1;


  //Retrieves whether the user of these classes are using the new way of getting
  //data of table. This is mainly for comliance with the previous version
  //Boolean.TRUE or Boolean.FALSE will be returned
  public static final int TVI_USE_NEW_WAY               = 2;


  //Decides whether to show the row of the specific BObject
  //Boolean.TRUE or Boolean.FALSE will be returned
  public static final int TVI_SHOW_THIS_ROW             = 3;


  //Just an indicator
  public static final Object NOT_PROCESSED_INDICATOR    = new Object();


  /**
   * Gets a string vector to reprensent the header of the table
   * *****************Deprecated
   */
  public Vector getTableHeader();


  /**
   * Gets the data type of each column, for sorting
   * *****************Deprecated
   */
  public int[] getHeaderDataType();


  /**
   * Gets the ratio of column width
   * *****************Deprecated
   */
  public int[] getTableColumnRatio();


  /**
   * Gets a vector from a BObjectInterface object for displaying in the table
   * *****************Deprecated
   */
  public Vector getTableRow(YObjectInterface object);


  /**
   * Gets the preferred size of the table
  */
  public Dimension getPreferredTableSize();


  /**
   * Gets the specified size of the table, which has a higher priority than
   * getPreferredTableSize()
  */
  public Dimension getSpecifiedTableSize();


  /**
   * Used to replace the above 4 deprecated methods
   */
  public Object getDataOfTable(String strColumnId, YObjectInterface objB, int iControlFlag);


  /**
   * Designed for the developer to customize the columns of the table
   */
  public String[] getDisplayedColumns();


  /**
   * Gets the specified size of the table, which has a higher priority than
   * getPreferredTableSize()
  */
  public String[] getSpecifiedColumns();


  /**
   * Sometimes, the values from querying are not enough for displaying.
   * For the consideration of performance, it's not a good idea to get
   * the additional values row by row. This method provides a way to read
   * additional values in a batch.
   */
  public Vector getAttachedData(Vector vOrigin) throws Exception;
}
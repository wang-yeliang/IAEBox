
package gxlu.ietools.basic.collection.util;

import gxlu.afx.publics.swingx.JGxluFileChooser;
import gxlu.afx.publics.swingx.JGxluFileFilter;
import gxlu.afx.publics.swingx.thread.TimeConsumeJob;
import gxlu.afx.publics.swingx.window.JGxluMessageBox;
import gxlu.afx.publics.swingx.window.JGxluWaitDialog;
import gxlu.afx.publics.util.TextLibrary;
import gxlu.afx.system.clientmain.TnmsClient;
import gxlu.afx.system.common.CommonClientEnvironment;
import gxlu.afx.system.common.JExceptionDialog;
import gxlu.afx.system.common.SDHCursorControl;
import gxlu.afx.system.common.SwingCommon;
import gxlu.afx.system.common.sysexception.SDHException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.ORANGE;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ImportUtil {
	static String fileName;
    private static HSSFWorkbook workbook;
    private final static int MAXROWNUM = 80000;
    public static int countOfRows = 0;
    public static TnmsClient sdhFrame = new TnmsClient();
    //用于输出带有颜色的模板
    private static short color = ORANGE.index;
    private static String outputFileName;
    private static FileOutputStream fos;
    public static HSSFWorkbook outputWorkbook;
    private static final String postfix = ".xls";
    private static int headRowNum = 1;
    private static List list=null;
    public ImportUtil()
    {
    }
    /**
     * 打开文件
     * @param filename 文件名称
     * @return
     */
    public static boolean openFile(String title)
    {
        JGxluFileChooser chooser = new JGxluFileChooser();
        JGxluFileFilter filter = new JGxluFileFilter( "xls", "Microsoft Excel file" );
        chooser.setFileFilter( filter );
        int return_val = chooser.showDialog( CommonClientEnvironment.getMainFrame(), title );
        if ( return_val == chooser.APPROVE_OPTION )
        {
            fileName = chooser.getSelectedFile().getAbsolutePath();
            File file = new File( fileName );
            if ( !file.exists() )
            {
                SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(), "没有找到文件 ： " + fileName );
                return false;
            }
            if ( !file.canRead() )
            {
                SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(),"不能读取文件 ： " + fileName + "请关闭其他打开该文件的应用程序重试。");
                return false;
            }
            if ( !file.canWrite() )
            {
                SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(), "不能写入文件 ： " + fileName + "请关闭其他打开该文件的应用程序重试。" );
                return false;
            }
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream( file );
                POIFSFileSystem ps = new POIFSFileSystem( fis );
                workbook = new HSSFWorkbook( ps );
                fis.close();
            }
            catch ( IOException ie )
            {
                if ( fis != null )
                {
                    try
                    {
                        fis.close();
                    }
                    catch ( IOException ex )
                    {
                    }
                }
                SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(), "导入文件错误：" + "请关闭其他打开该文件的应用程序重试。\n" + ie.getMessage() );
                return false;
            }            
            if ( !checkImportFile( workbook) )
            {
                return false;
            }
            //试着去写，看文件有没有打开
            if ( !writeError( fileName, workbook, 1, "", ( short ) (workbook.getSheetAt(0).getRow(0).getLastCellNum()-2) ) )
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        return true;
    }
    private static void showFileNotRightMessage()
    {
        SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(),"导入文件模板错误，请选择模板正确的导入文件！" );
    }
    //导出模板
    public static void outputTemplet(DynamicObject obj,int colCount){
        if ( openOutputFile() )
        {
            HSSFSheet sheet = outputWorkbook.createSheet();
            HSSFRow row = sheet.createRow( 0 );            
            String colName="";
            for ( int i = 0; i < colCount; i++ )
            {
            	Object result=invokeMethod(obj, "getF"+(i+1));
            	colName=result!=null?(String)result:"";
            	//设置背景颜色暂时没有实现
                fillACellWithColor( row,i,colName, false );
            }
            fillACellWithColor( row,colCount,"", false ); //导入出错原因前空一个
            fillACellWithColor( row,colCount+1,"导入出错原因", false ); //导入出错原因
            if ( endExcelFile() )
            {
                SwingCommon.showInfoMessage(CommonClientEnvironment.getMainFrame(),
                    "模板已导出。\n注意！如果某些属性需要填写特殊符号，如日期2005-01-01,信令点编码11-11-11,\n类似这些格式的数据请先把这些列的单元格格式设置成文本格式，然后再输入数据。" );
            }
        }
    }
    //通过反射获得相应的方法
    public static Object invokeMethod(Object obj,String methodName){
    	Method method;
		try {
			method = obj.getClass().getMethod(methodName);
        	return String.valueOf(method.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * 关闭输入的文件
     * @return boolean
     */
    public static boolean endExcelFile()
    {
        try
        {
            outputWorkbook.write( fos );
            fos.flush();
            fos.close();
        }
        catch ( IOException e )
        {
            return false;
        }
        return true;
    }
    /**
     * openOutputFile
     * 输出导入模板前打开文件
     * @return boolean
     */
    public static boolean openOutputFile()
    {
        JGxluFileChooser chooser = new JGxluFileChooser();
        JGxluFileFilter filter = new JGxluFileFilter( "xls", "Microsoft Excel file" );
        chooser.addChoosableFileFilter( filter );
        int return_val = chooser.showDialog( CommonClientEnvironment.getMainFrame(), "输出" );
        if ( return_val == chooser.APPROVE_OPTION )
        {
            //read the excel file and input the records into the database
            outputFileName = chooser.getSelectedFile().getAbsolutePath();
            //自动加上后缀.xls，自动生成excel文件
            if ( !outputFileName.endsWith( postfix ) )
            {
                outputFileName += postfix;
            }
            File file = new File( outputFileName );
            if ( file.exists() )
            {
                if ( JOptionPane.showConfirmDialog( CommonClientEnvironment.getMainFrame(),
                                                    "您选择的文件已经存在，是否覆盖？",
                                                    "提示",
                                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
                     ) != JOptionPane.YES_OPTION )
                {
                    return openOutputFile();
                }
            }
            try
            {
                fos = new FileOutputStream( outputFileName );
            }
            catch ( FileNotFoundException e )
            {
                JOptionPane.showMessageDialog( CommonClientEnvironment.getMainFrame(),
                                               "初始化文件错误，请确认是否有其他程序占用该文件。", "信息",
                                               JOptionPane.OK_OPTION );
                return false;
            }
            outputWorkbook = new HSSFWorkbook();
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * 检查打开的文件//read the specified sheet
     * @param workbook
     * @return
     */
    public static boolean checkImportFile(HSSFWorkbook workbook)
    {
        if ( workbook == null )
        {
            showFileNotRightMessage();
            return false;
        }
        HSSFSheet sheet = workbook.getSheetAt( 0 ); //现在只能按照模版导入，只能导入第一个sheet
        if ( sheet == null )
        {
            showFileNotRightMessage();
            return false;
        }
        HSSFRow row = sheet.getRow( headRowNum - 1 ); //如果多行表头，取最下边一行表头
        if ( row == null )
        {
            showFileNotRightMessage();
            return false;
        }
        //验证标题是否为空
        for ( short k = 0; k < (row.getLastCellNum()-2); k++ )
        {
            HSSFCell cell = row.getCell( k );
            String tmpStr = new String();
            if ( cell == null )
            { //该列空
                showFileNotRightMessage();
                return false;
            }
        }
        countOfRows = sheet.getPhysicalNumberOfRows();
        if ( countOfRows <= 1 )
        {
            SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(), "导入文件中没有数据，请先输入数据再导入！" );
            return false;
        }
        return true;
    }
    /**
     * 根据cell类型调用不同方法得到单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(HSSFCell cell)
    {
        switch ( cell.getCellType() )
        {
            case HSSFCell.CELL_TYPE_BLANK:
                return "";
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return String.valueOf( cell.getBooleanCellValue() );
            case HSSFCell.CELL_TYPE_ERROR:
                return String.valueOf( cell.getErrorCellValue() );
            case HSSFCell.CELL_TYPE_FORMULA:
                return String.valueOf( ( long ) cell.getNumericCellValue() );
            case HSSFCell.CELL_TYPE_NUMERIC:            	
                 String str=String.valueOf( ( double ) cell.getNumericCellValue() );
            	 String endfloatstr="";
            	 if(str.indexOf("-")>0) 
            	     endfloatstr=str.substring(str.indexOf("-")+1, str.length());
            	 return doubleFormat(( double ) cell.getNumericCellValue(),endfloatstr);
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }
    public static String doubleFormat(double a,String str){
    	if(str.equals("")){
    		String s=String.valueOf(a);
    		String floatStr="";
    		String endStr=s.substring(s.length()-1,s.length());
    		if(s.indexOf(".")>0){
    			floatStr=s.substring(s.indexOf(".")+1,s.length());
    			if(floatStr.length()>1)
    			{
    				if(endStr.equals("0")){
    				 s=s.substring(0,s.length()-1);
    			}
    			}else if(floatStr.length()==1){
    				if(endStr.equals("0"))
    					s=s.substring(0,s.indexOf("."));
    			}
    		}
    		
    		return s;
    	}
    	else{
    		//得到小数后面的0
    		int b=Integer.parseInt(str);
    		 StringBuffer stb = new StringBuffer();
    	      stb.append("0.");
    	      for (int i=0;i<b;i++) {
    	         stb.append("0");
    	      }
    	    DecimalFormat df = new DecimalFormat(stb.toString());
    	   return df.format(a);
    	}
		
    }
    
    public boolean writeError(HashMap result,ResultController resultController)
    {
        if ( result != null && !result.isEmpty() )
        {
            Iterator it = result.keySet().iterator();
            while ( it.hasNext() )
            {
                Long key = ( Long ) it.next();
                String msg = "";
                if ( result.get( key ) instanceof SDHException )
                {
                    msg = JExceptionDialog.getSimpleMessage( ( SDHException ) result.get( key ) );
                }
                else
                {
                    msg = result.get( key ).toString();
                }
                if ( !writeError( fileName, workbook, key.longValue(), msg,
                                  (short)resultController.getObjectValueCount()))
                {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 写入出错信息
     * @param position 位置行数
     * @param message 信息
     */
    public static boolean writeError(String fileName,HSSFWorkbook workbook,long position,String message,short errorPosition)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream( fileName );
            HSSFSheet sheet = workbook.getSheetAt( ( int ) ( position / MAXROWNUM ) );
            HSSFRow row = sheet.getRow( ( short ) ( position % MAXROWNUM ) );
            HSSFCell cell = row.createCell( errorPosition );
            cell.setEncoding( HSSFWorkbook.ENCODING_UTF_16 );
            cell.setCellValue( message );
            workbook.write( fileOut );
            fileOut.close();
        }
        catch ( IOException ex )
        {
            SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(), "导入文件错误：" + "请关闭其他打开该文件的应用程序重试。\n" + ex.getMessage() );
            return false;
        }
        return true;
    }
    /**
     * 读取表格信息
     * @param fileName
     * @return
     */
    private static List readSheet(Class objClass)
    {
        Object info = null;
        List table = new ArrayList();
        HSSFSheet sheet = workbook.getSheetAt( 0 );
//        countOfRows = sheet.getPhysicalNumberOfRows();
        Iterator rows = sheet.rowIterator();
        int currentRow=0;
        int endIndex=0;
        while ( rows.hasNext() )
        {
            try
            {
                info = objClass.newInstance();
            }
            catch ( Exception ex1 )
            {
            }
            HSSFRow row = ( HSSFRow ) rows.next();
            int i = row.getRowNum();
            if (i < headRowNum-1)
            {
                continue;
            }
            boolean savable = true;
            if(currentRow==0){
            	endIndex=row.getLastCellNum()-2;
            }
            for ( int j = 0; j <endIndex; j++ )
            {
                HSSFCell cell = row.getCell((short)j );
                String tmpStr = new String();
                if ( cell != null )
                {
                    tmpStr = getCellValue( cell );
                }
				try {
	                Class[] params = new Class[]{new String().getClass()};
					Method method = objClass.getMethod( "setF"+(j+1), params );
	                Object[] paramsObject = new Object[]{tmpStr.trim()};
	                method.invoke( info, paramsObject );
				}catch(Exception e) {
					e.printStackTrace();
				}
            }
            currentRow++;
            table.add(info);
        }
        return table;
    }
    
    public static void showImportResult(List list,String [][] errorData)
    {
        int updateNum = list.size()-1-errorData.length;
        int errorNum=errorData.length;
        if(errorNum>0)
            SwingCommon.showErrorMessage(CommonClientEnvironment.getMainFrame(), "成功导入 " + updateNum + " 个资源，" + "导入失败" + errorNum +
                              "个资源！\n请查看xls导入文件中“导入出错原因”列。" );
        else
            SwingCommon.showInfoMessage(CommonClientEnvironment.getMainFrame(), "成功导入 " + updateNum + " 个资源，" + "导入失败" + errorNum +
                              "个资源！" );
    }
    /**
     * 在单元格中设置背景色
     * @param row HSSFRow 行数
     * @param colNum int  列数
     * @param value Object 输入值
     * @param IsFillColor boolean 是否要填充背景色
     */
    public static void fillACellWithColor(HSSFRow row,int colNum,Object value,boolean IsFillColor)
    {
        HSSFCell cell = row.createCell( ( short ) colNum );
        //设置背景颜色
        HSSFCellStyle style = outputWorkbook.createCellStyle();
        if ( IsFillColor )
        {
            style.setFillForegroundColor( color );
            style.setFillPattern( HSSFCellStyle.SOLID_FOREGROUND );
            cell.setCellStyle( style );
        }
        cell.setEncoding( HSSFWorkbook.ENCODING_UTF_16 );
        cell.setCellValue( value == null ? "" : value.toString());
    }
    //设备excel单元格的背景色
    public static short getColor()
    {
        return color;
    }
    public static void setColor(short color)
    {
        color = color;
    }
    //设置表头行数
    public static void setHeadRowNum(int rowNum)
    {
        headRowNum = rowNum;
    }
    
    public static List importExcel()
    {
        if (openFile("导入设备信息" ) )
        {
            new JGxluWaitDialog( sdhFrame, TextLibrary.getText( "caption_WaitDialog" ), TextLibrary.getText( "正在导入设备信息，请稍候..." ), new TimeConsumeJob()
            {
                public Object doInConstruct()
                {
                    SDHCursorControl.openWaitCursor();
                    CommonClientEnvironment.getTimeLog().beginTiming( "toolmenu/Import" );
                    //读取的Excel信息都封装在此对象中
                    list= readSheet(DynamicObject.class);      
                    SDHCursorControl.closeWaitCursor();
                    CommonClientEnvironment.getTimeLog().endTiming( "toolmenu/Import" );
                    return list;
                }
                public void doInFinished()
                {
                    return;
                }
            } );
            return list;
        }
        return null;
    }
    public static boolean writeImportResult(int count,Object[] errorData){
    	//清空原来的错误信息
        HSSFSheet sheet = workbook.getSheetAt( 0 );
        Iterator rows = sheet.rowIterator();
        if(rows.hasNext())
        	rows.next();
        int k=1;
        while(rows.hasNext()){
        	rows.next();
        	if ( !writeError( fileName, workbook,k,"",( short )(workbook.getSheetAt(0).getRow(0).getLastCellNum()-1)) )
	        {
        		JGxluMessageBox.show( CommonClientEnvironment.getMainFrame(), "写入出错信息时出错", "确认", JGxluMessageBox.INFORMATION_MESSAGE );
	            return false;
	        }
        	k++;
        }
        //重新写入错误信息
    	for(int i=0;i<errorData.length;i++){
    		 if ( !writeError( fileName, workbook,Long.parseLong(((Object[])errorData[i])[0].toString())+1,((Object[])errorData[i])[1].toString(),( short )(workbook.getSheetAt(0).getRow(0).getLastCellNum()-1)) )
             {
    			 JGxluMessageBox.show( CommonClientEnvironment.getMainFrame(), "写入出错信息时出错", "确认", JGxluMessageBox.INFORMATION_MESSAGE );
                 return false;
             }
    	}
    	String showResult="";
    	if(errorData.length>0)
    		showResult="成功导入"+count+"条数据，导入失败"+(list.size()-count)+"条数据";
    	else
    		showResult="成功导入"+count+"条数据";
    	JGxluMessageBox.show( CommonClientEnvironment.getMainFrame(), showResult, "确认", JGxluMessageBox.INFORMATION_MESSAGE );
    	
    	return true;
    }
}

package gxlu.ietools.client.query.gisquery;

import gis.common.dataobject.YObjectInterface;
import gxlu.afx.system.common.SwingCommon;
import gxlu.afx.system.query.client.OperationInterface;
import gxlu.afx.system.query.client.ResultInterface;
import gxlu.afx.system.query.client.UIBaseQueryDialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JDialog;

public class UIGisBaseQueryDialog  extends UIBaseQueryDialog implements GisTableValueInterface{
	
	  public Vector getTableRow(YObjectInterface object)
	  {
	    return null;
	  }
	  public Object getDataOfTable(String strColumnId, YObjectInterface objB, int iControlFlag)
	  {
	        if (iControlFlag == GisTableValueInterface.TVI_USE_NEW_WAY)
	        {
	            //to keep compliant with the old way
	            return Boolean.FALSE;
	        }
	        else
	        {
	            //doesn't respond to other flags
	            return null;
	        }
	  }
	  
	  protected Object getDefaultDataOfTable(String strColumnId, YObjectInterface objB, int iControlFlag)
	  {
	        if (iControlFlag == GisTableValueInterface.TVI_USE_NEW_WAY)
	        {
	            return Boolean.TRUE;
	        }
	        else
	        if (iControlFlag == GisTableValueInterface.TVI_SHOW_THIS_ROW)
	        {
	            return Boolean.TRUE;
	        }
	        else
	        {
	            return GisTableValueInterface.NOT_PROCESSED_INDICATOR;
	        }
	  }
	  public void initAsDialog()
	  {
	    if (dlgOwner != null)
	    {
	      dlg = new JDialog(dlgOwner);
	    }
	    else if (frmOwner != null)
	    {
	      dlg = new JDialog(frmOwner);
	    }
	    else
	    {
	      dlg = new JDialog();
	    }

	    dlg.setTitle(strTitle);
	    dlg.setModal(bModal);

	    //Allows the panelMain to be resized with the dialog
	    dlg.getContentPane().addComponentListener
	    (
	      new ComponentAdapter()
	      {
	        public void componentResized(ComponentEvent e)
	        {
	          layeredPane.hideAllDetailQueryControl();

	          layeredPane.setSize(e.getComponent().getSize());
	          panelMain.setSize(e.getComponent().getSize());
	          panelMain.validate();
	        }
	      }
	    );


	    //alias
	    child = this;


	    //When the dialog is opened or closed, executes something
	    dlg.addWindowListener
	    (
	      new WindowAdapter()
	      {
	        public void windowOpened(WindowEvent e)
	        {
	          initDialog();
	        }

	        //As a child query dialog, notifys the parent when it is closed
	        public void windowClosed(WindowEvent e)
	        {
	          if (operationDisplayer != null)
	          {
	            operationDisplayer.closeDialog();
	          }

	          if (parent != null)
	          {
	            if (dialogShower.getOperationType() == OPR_SELECT)
	            {
	              if (resultDisplayer != null)
	              {
	                parent.childQuerySelected(child, resultDisplayer.getSelectedItems());
	              }
	              else
	              {
	                parent.childQueryCanceled(child);
	              }
	            }

	            else if (dialogShower.getOperationType() == OPR_CLOSE)
	            {
	              parent.childQueryCanceled(child);
	            }
	          }
	        }

	        public void windowClosing(WindowEvent e)
	        {
	          if(resultDisplayer != null)
	          {
	            GisTableResultControl tableResult = (GisTableResultControl) resultDisplayer.getResultControl();
	            if(tableResult != null && tableResult.getTable() != null)
	            {
	              tableResult.getTable().clearSelection();
	            }
	          }
	          onWindowClosing();
	        }
	      }
	    );

	    dlg.getContentPane().setLayout(new BorderLayout());
	    dlg.getContentPane().add(panelMain, BorderLayout.CENTER);

	    //Prepares to show this dialog
	    dlg.pack();
	    SwingCommon.centerDialog(dlg);
	  }
	  
	  protected OperationInterface createOperationDisplayer()
	  {
	         return new GisButtonsOperationControl(this);
	  }
	  protected ResultInterface createResultDisplayer()
	  {
	    return new GisTableResultControl(getGisTableValueInterface(), getResultOperator());
	  }
	  protected GisTableValueInterface getGisTableValueInterface()
	  {
	    return this;
	  }
	  public void showAsInternalFrame()
	  {
	    showAsInternalFrame(null);
	    if(this.getResultDisplayer() instanceof GisTableResultControl){
	        ((GisTableResultControl)this.getResultDisplayer()).getTableColumnShowControl().initColumnWidth();
	    }
	  }
	  public UIGisBaseQueryDialog aceInit(Frame owner, byte dialogControlType, boolean singleSelect, boolean modal, String xmlFileName, String uiName)
	  {
	    frmOwner = owner;
	    init(false, dialogControlType, singleSelect, modal, xmlFileName, uiName);
	    return this;
	  }
	@Override
	protected String getAssembleString() {
		return null;
	}

	
	protected String getDialogTitle(byte type) {
		return null;
	}

	protected Class getQueryClass() {
		return null;
	}

}

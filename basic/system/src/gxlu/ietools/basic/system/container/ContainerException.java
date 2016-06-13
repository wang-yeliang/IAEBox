/**************************************************************************
 * $$RCSfile: ContainerException.java,v $$  $$Revision: 1.6 $$  $$Date: 2010/04/20 02:08:06 $$
 *
 * $$Log: ContainerException.java,v $
 * $Revision 1.6  2010/04/20 02:08:06  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.system.container;

/** Exception when error happens to create entity
 * 
 * @author kidd
 */
public class ContainerException extends Exception

{



    /** entity home creation error
     * @param s reason
     */    
    public ContainerException(String s)

    {

        super(s);

    }
}

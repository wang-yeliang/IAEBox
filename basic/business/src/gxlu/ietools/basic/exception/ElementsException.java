// $Id: ElementsException.java,v 1.6 2010/04/20 02:08:02 wudawei Exp $
package gxlu.ietools.basic.exception;

/** Exception when error happens to create entity home
 */
public class ElementsException extends Exception

{



    /** entity home creation error
     * @param s reason
     */    
    public ElementsException(String s)

    {

        super(s);

    }
}

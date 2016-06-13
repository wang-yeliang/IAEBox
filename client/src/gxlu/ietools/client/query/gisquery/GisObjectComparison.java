package gxlu.ietools.client.query.gisquery;

import gis.common.dataobject.YObjectInterface;
import gxlu.afx.system.common.ObjectComparison;
import gxlu.afx.system.common.SwingCommon;

import java.util.List;
import java.util.Vector;

public class GisObjectComparison extends ObjectComparison {
	 public static YObjectInterface getEqualObjectById(YObjectInterface oneObject, List list)
	  {
	    if (oneObject==null) return null;

	    YObjectInterface temp;
	    int length = 0;

	    if (list!=null) length = list.size();

	    for (int i=0; i<length; i++)
	    {
	      temp = (YObjectInterface)list.get(i);

	      if (temp==null) continue;

	      if (temp.getId()==oneObject.getId())
	      {
	        return temp;
	      }
	    }

	    return null;
	  }
	 
	  public static int getIndexInVector(YObjectInterface oneObject, Vector v)
	  {
	    if ((oneObject==null) || (v==null))  return -1;

	    if (v.size()<=0)  return -1;

	    for (int i=0; i<v.size(); i++)
	    {
	      if (isEqualById(oneObject, (YObjectInterface)v.elementAt(i)))
	      {
	        return i;
	      }
	    }

	    return -1;
	  }
	  
	  /**
	   * Whether two objects are equal by ID
	   */
	  public static boolean isEqualById(YObjectInterface one, YObjectInterface two)
	  {
	    if ((one==null) && (two==null))
	    {
	      return true;
	    }
	    else if ((one==null) || (two==null))
	    {
	      return false;
	    }

	    if (one.getId()==two.getId())
	    {
	      return true;
	    }
	    else
	    {
	      return false;
	    }
	  }
	  /**
	   * Merges two Vector
	   */
	  public static Vector vectorOr(Vector v1, Vector v2)
	  {
	    if (v1 == null && v2 == null)
	    {
	        return null;
	    }

	    if (v1 == null)
	    {
	        return v2;
	    }

	    if (v2 == null)
	    {
	        return v1;
	    }

	    Vector v0 = new Vector();

	    for (int i = 0; i < v1.size(); i ++)
	    {
	        if (getEqualObjectById((YObjectInterface)v1.elementAt(i), v0) == null)
	        {
	            v0.addElement(v1.elementAt(i));
	        }
	    }

	    for (int i = 0; i < v2.size(); i ++)
	    {
	        if (getEqualObjectById((YObjectInterface)v2.elementAt(i), v0) == null)
	        {
	            v0.addElement(v2.elementAt(i));
	        }
	    }

	    return v0;
	  }


	  /**
	   * Ands two Vector
	   */
	  public static Vector vectorAnd(Vector v1, Vector v2)
	  {
	    if (v1 == null || v2 == null)
	    {
	        return null;
	    }

	    Vector v0 = new Vector();

	    for (int i = 0; i < v1.size(); i ++)
	    {
	        if (getEqualObjectById((YObjectInterface)v1.elementAt(i), v2) != null)
	        {
	            v0.addElement(v1.elementAt(i));
	        }
	    }

	    return v0;
	  }


	    /**
	     * judge whether two vector ar equal
	     */
	    public static boolean isVectorEqual(Vector v1, Vector v2)
	    {
	        if (SwingCommon.getVectorLength(v1) != SwingCommon.getVectorLength(v2))
	        {
	            return false;
	        }

	        if (SwingCommon.getVectorLength(v1) == 0 && SwingCommon.getVectorLength(v2) == 0)
	        {
	            return true;
	        }

	        for (int i = 0; i < SwingCommon.getVectorLength(v1); i ++)
	        {
	            if (getEqualObjectById((YObjectInterface)v1.elementAt(i), v2) == null)
	            {
	                return false;
	            }
	        }

	        for (int i = 0; i < SwingCommon.getVectorLength(v2); i ++)
	        {
	            if (getEqualObjectById((YObjectInterface)v2.elementAt(i), v1) == null)
	            {
	                return false;
	            }
	        }

	        return true;
	    }
}

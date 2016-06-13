/**************************************************************************
 * $$RCSfile: CalledThread.java,v $$  $$Revision: 1.3 $$  $$Date: 2010/04/20 02:08:03 $$
 *
 * $$Log: CalledThread.java,v $
 * $Revision 1.3  2010/04/20 02:08:03  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.threads;

import gxlu.ietools.basic.exception.ThreadException;

import java.util.List;

public class CalledThread extends Thread {
    private final TaskProcessors taskProcessors;
    private final List iParam;
    private volatile boolean terminated = false;
    
    public CalledThread(List iParam, TaskProcessors taskProcessors) {
        this.taskProcessors = taskProcessors;
        this.iParam = iParam;
    }
    
    /* 
     * 将请求放入请求队列
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        try {
        	try {
            	CalledRequest request = new CalledRequest(iParam);
            	taskProcessors.putRequest(request);
            } catch (ThreadException e) {
                terminated = true;
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + " is terminated.");
        }
    }
    
    public void stopThread() {
        terminated = true;
        interrupt();
    }
}

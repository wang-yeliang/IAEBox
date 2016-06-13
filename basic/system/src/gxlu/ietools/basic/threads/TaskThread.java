/**************************************************************************
 * $$RCSfile: TaskThread.java,v $$  $$Revision: 1.3 $$  $$Date: 2010/04/20 02:08:03 $$
 *
 * $$Log: TaskThread.java,v $
 * $Revision 1.3  2010/04/20 02:08:03  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.threads;

import gxlu.ietools.basic.exception.ThreadException;

public class TaskThread extends Thread {
    private final TaskProcessors channel;
    private volatile boolean terminated = false;
    
    public TaskThread(String name, TaskProcessors channel) {
        super(name);
        this.channel = channel;
    }
    
    /* 
     * 从请求队列中获取请求并执行
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        try {
            while (!terminated) {
                try {
                    CalledRequest request = channel.takeRequest();    
                    request.execute();
                } catch (ThreadException e) {
                    terminated = true;
                }                                               
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

/**************************************************************************
 * $$RCSfile: TaskProcessors.java,v $$  $$Revision: 1.3 $$  $$Date: 2010/04/20 02:08:03 $$
 *
 * $$Log: TaskProcessors.java,v $
 * $Revision 1.3  2010/04/20 02:08:03  wudawei
 * $20100420
 * $$
 **************************************************************************/
package gxlu.ietools.basic.threads;

import gxlu.ietools.basic.exception.ThreadException;
import gxlu.ietools.property.mapping.MetaAttribute;

import org.apache.log4j.Logger;

public final class TaskProcessors {
	protected Logger logger = Logger.getLogger(MetaAttribute.class);
	
    private static final int MAX_REQUEST = 100;
    private final CalledRequest[] requestQueue;
    private int tail;  // 下次要putRequest的位置
    private int head;  // 下次要takeRequest的位置
    private int count; // Request的次数

    private final TaskThread[] threadPool;

    /**
     * 初始化请求队列和线程池
     * @param threads
     */
    public TaskProcessors(int threads) {
        this.requestQueue = new CalledRequest[MAX_REQUEST];
        this.head = 0;
        this.tail = 0;
        this.count = 0;

        threadPool = new TaskThread[threads];
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i] = new TaskThread("Worker-" + i, this);
        }
    }
    
    /**
     * 启动工作线程
     */
    public void startWorkers() {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].start();
        }
    }

    /**
     * 结束工作线程
     */
    public void stopAllWorkers() {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].stopThread();
        }
    }
    
    /**
     * 将工作请求放入线程队列
     * @param request
     * @throws ThreadException
     */
    public synchronized void putRequest(CalledRequest request) throws ThreadException {
		try {
	        while (count >= requestQueue.length) {
	            wait();
	        }
	        requestQueue[tail] = request;
	        tail = (tail + 1) % requestQueue.length;
	        count = count + 1;
	        notifyAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());
			logger.error(e.toString());
			throw new ThreadException(e.getMessage(),e.toString());
		}
    }
    
    /**
     * 从线程队列中获取
     * @return
     * @throws ThreadException
     */
    public synchronized CalledRequest takeRequest() throws ThreadException {
		try {
	        while (count <= 0) {
	            wait();
	        }
	        CalledRequest request = requestQueue[head];
	        head = (head + 1) % requestQueue.length;
	        count = count - 1;
	        notifyAll();
	        return request;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());
			logger.error(e.toString());
			throw new ThreadException(e.getMessage(),e.toString());
		}
    }
    
}

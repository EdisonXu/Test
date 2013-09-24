package com.edi.test.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.edi.test.bean.MyEventRetryTO;


/**
 * A queue to store the event to be reported to NBI(BMC).
 */
public class NbiEventCache {

    /*
     * Health line 4000 comes from the statistic result of 600-concurrent eMBMS session WPST during half an hour.
     */
    public static final int HEALTH_LINE = 4000;
    
    /*
     * Add 1000 as a buffer from Health line.
     */
    public static final int MAX_SIZE = 5000;
    
    
    /*
     * Get status -> Peek one event is one transaction.
     * This lock ensure that only one thread can make this peek transaction at a time.
     * Time cost is small after confirm.
     */
    private static ReentrantLock internalTransLock = new ReentrantLock();
    
    private static Queue<MyEventRetryTO> queue = new ConcurrentLinkedQueue<>();
    
    /*
     * This cache stores the events with a same eMBMS session id.
     * 
     */
    private static FilteredEventCache filteredEventCache = new FilteredEventCache();
    
    private static AtomicInteger size = new AtomicInteger(0);
    
    private static ReentrantLock lockForClear = new ReentrantLock();
    
    public static enum STATUS
    {
        OK, OVERLOAD, FULL, ERROR;
    }
    
    public static boolean offer(MyEventRetryTO event){
        if(event==null) return false;
        /*
         * If the lockForClear is locked, means there's a clear operation is executing.
         * Do NOT remove .isLocked() check, because it's totally not necessary for each offer
         * thread to synchronized with each other depends on this lock.
         */
        if(lockForClear.isLocked() && !lockForClear.isHeldByCurrentThread())
            lockForClear.lock();
        size.incrementAndGet();
        return queue.offer(event);
    }
    
    /**
     * Retrieves, but does not remove, the head of this cache, or returns null if this cache is empty.
     * @return the head of this cache, or null if this cache is empty
     */
    public static MyEventRetryTO poll(){
        if(!internalTransLock.isHeldByCurrentThread())
        	internalTransLock.lock();
        MyEventRetryTO event = queue.poll();
        
        if(event==null){
            internalTransLock.unlock();
            return null;
        }
        
        if(event.getSessionId()!=null)
        {
            /*
             * If the eMBMS session id is not null, remove it from the 1st level queue 
             * and add it into the 2nd level cache filtered by eMBMS session id.
             */
        	/*
             * If already contained, means that there's already a thread sending a event with
             * a same eMBMS session id. Then add this one to the filtered cache and peek the next.
             */
            if(filteredEventCache.containSameSessionId(event.getSessionId()))
            {
            	if(size.get()>3500)
                	System.out.println("[" + Thread.currentThread().getName() + "] adds event Id=" + event.getId() + ", eMBMSSessionId=" +event.getSessionId() + " current size: "+ size.get());
                filteredEventCache.add(event);
                //internalTransLock.unlock();
                return null;
            }
            filteredEventCache.add(event);
        }
        size.decrementAndGet();
        if(size.get()>3500)
        {
        	//System.out.println(Thread.currentThread().getName() + " Reduce to" + size.get());
        	//debug
            if(event!=null)
            	System.out.println("[" + Thread.currentThread().getName() + "] pick event Id=" + event.getId() + ", eMBMSSessionId=" +event.getSessionId() + " current size: "+ size.get());
        }
            
        releaseLock();
        return event;
    }
    
    /**
     * Poll the next event in the 2nd level filtered cache.
     * @param old The last event that has been handled.
     * @return the next event in the 2nd level cache with a same eMBMS session id with the one input. 
     */
    public static MyEventRetryTO poll(MyEventRetryTO old)
    {
    	if(old==null)
    		return poll();
    	
    	/*
    	 * If the event don't have a eMBMS session id, it will not have a queue in the 
    	 * 2nd level filtered cache, so return directly.
    	 */
    	if(old.getSessionId() == null) return null;
        
        if(!internalTransLock.isHeldByCurrentThread())
            internalTransLock.lock();
        
        filteredEventCache.poll(old.getSessionId());
        MyEventRetryTO next = filteredEventCache.peek(old.getSessionId());
        if(next!=null)
        {
        	size.decrementAndGet();
        	if(size.get()>3500)
                System.out.println(Thread.currentThread().getName() + " reduces to " + size.get());
        }
        releaseLock();
        return next; 
    }
    
    public static void releaseLock()
    {
        while(internalTransLock.isHeldByCurrentThread())
        {
            internalTransLock.unlock();
        }
    }
    
    public static STATUS getStatus(){
        //int sizeValue = queue.size() + filteredEventCache.size();
        if(!internalTransLock.isHeldByCurrentThread())
            internalTransLock.lock();
        int sizeValue = size.get();
        if(sizeValue <=HEALTH_LINE){
            return STATUS.OK;
        }
        if(sizeValue > HEALTH_LINE && sizeValue < MAX_SIZE){
            return STATUS.OVERLOAD;
        }
        if(sizeValue >= MAX_SIZE){
            return STATUS.FULL;
        }
        return STATUS.ERROR;
    }
    
    /**
     * Clear the queue.
     *     
     * @return
     */
    public static List<MyEventRetryTO> clear()
    {
        List<MyEventRetryTO> list = new ArrayList<>();
        if(!internalTransLock.isHeldByCurrentThread())
        	internalTransLock.lock();    
        
        /*
         * Lock the cache for new offering
         */
        if(!lockForClear.isHeldByCurrentThread())
            lockForClear.lock();
        
        // double confirm the status here to avoid multiple threads are
        // executing the clear() method to make the new coming events after clear is removed.
        if(!getStatus().equals(STATUS.FULL))
        {
        	releaseLock();
        	return list;
        }
        
        if(queue.size()>0)
            list.addAll(queue);
        if(list.size()>0)
            queue.clear();
        
        list.addAll(filteredEventCache.clear());
        size.set(0);
        releaseLock();
        lockForClear.unlock();
        System.out.println("NbiEventCache is cleared with total events removed: " + list.size());
        return list;
    }
    
    public static int size()
    {
        return size.get();
    }
    
    /**
     * For test only. Do NOT use it in real implementation.
     */
    public static void reset ()
    {
        queue.clear();
        filteredEventCache.clear();
        size.set(0);
    }
}

/*
 * Filter the events with the eMBMS session id.
 * It's a map with eMBMS session id as key, a event queue as value.
 */
class FilteredEventCache
{
    private Map<Long, Queue<MyEventRetryTO>> filteredEventMap;
    private int filteredCacheSize;
    
    public FilteredEventCache()
    {
        filteredEventMap = new HashMap<Long, Queue<MyEventRetryTO>>();
        filteredCacheSize = 0;
    }
    
    public void add(MyEventRetryTO event)
    {
        Long id = event.getSessionId();
        if(id==null)
            return;
        Queue<MyEventRetryTO> q = filteredEventMap.get(id);
        
        if(q == null)
        {
            q = new LinkedBlockingQueue<MyEventRetryTO>();
        }
        q.offer(event);
        filteredCacheSize++;
        filteredEventMap.put(id, q);
    }
    
    public MyEventRetryTO poll(Long id)
    {
        if(id==null)
            return null;
        Queue<MyEventRetryTO> q = filteredEventMap.get(id);
        if(q==null)
            return null;
        MyEventRetryTO event = q.poll();
        if(event!=null)
            filteredCacheSize--;
        if(q.size()==0)
            filteredEventMap.remove(id);
        return event;
    }
    
    public MyEventRetryTO peek(Long id)
    {
        if(id==null)
            return null;
        Queue<MyEventRetryTO> q = filteredEventMap.get(id);
        if(q==null)
            return null;
        return q.peek();
    }
    
    public boolean containSameSessionId(Long id)
    {
        if(id==null)
            return false;
        Queue<MyEventRetryTO> q = filteredEventMap.get(id);
        if(q!=null && q.size()>0)
            return true;
        return false;
    }
    
    public int size()
    {
        return filteredCacheSize;
    }
    
    public List<MyEventRetryTO> clear()
    {
        ArrayList<MyEventRetryTO> list = new ArrayList<>();
        if(filteredEventMap.size()==0){
            filteredCacheSize = 0;
            return list;
        }
        for(Queue<MyEventRetryTO> queue : filteredEventMap.values())
        {
            list.addAll(queue);
        }
        
        this.filteredEventMap.clear();
        filteredCacheSize = 0;
        return list;
    }
}

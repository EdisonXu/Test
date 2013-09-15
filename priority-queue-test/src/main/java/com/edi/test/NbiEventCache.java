package com.edi.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


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
    
    private static Queue<BmscEventRetryTO> queue = new ConcurrentLinkedQueue<>();
    
    /*
     * This cache stores the events with a same eMBMS session id.
     * 
     */
    private static FilteredEventCache filteredEventCache = new FilteredEventCache();
    
    private static AtomicInteger size = new AtomicInteger(0);
    
    public static enum STATUS
    {
        OK, OVERLOAD, FULL, ERROR;
    }
    
    public static boolean offer(BmscEventRetryTO event){
        if(event==null) return false;
        size.incrementAndGet();
        return queue.offer(event);
    }
    
    /**
     * Retrieves, but does not remove, the head of this cache, or returns null if this cache is empty.
     * @return the head of this cache, or null if this cache is empty
     */
    public static BmscEventRetryTO poll(){
        if(!internalTransLock.isHeldByCurrentThread())
        	internalTransLock.lock();
        BmscEventRetryTO event = queue.poll();
        
        if(event==null){
            internalTransLock.unlock();
            return null;
        }
        
        if(event.getEMBMSSessionId()!=null)
        {
            /*
             * If the eMBMS session id is not null, remove it from the 1st level queue 
             * and add it into the 2nd level cache filtered by eMBMS session id.
             */
            synchronized (filteredEventCache) {
                /*
                 * If already contained, means that there's already a thread sending a event with
                 * a same eMBMS session id. Then add this one to the filtered cache and peek the next.
                 */
                if(filteredEventCache.containSameSessionId(event.getEMBMSSessionId()))
                {
                    filteredEventCache.add(event);
                    //internalTransLock.unlock();
                    return null;
                }
                filteredEventCache.add(event);
            }
        }
        size.decrementAndGet();
        if(size.get()>3500)
            System.out.println(Thread.currentThread().getName() + " Reduce to" + size.get());
        internalTransLock.unlock();
        return event;
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
    public static List<BmscEventRetryTO> clear()
    {
        List<BmscEventRetryTO> list = new ArrayList<>();
        if(!internalTransLock.isHeldByCurrentThread())
        	internalTransLock.lock();    
        
        // double confirm the status here to avoid multiple threads are
        // executing the clear() method to make the new coming events after clear is removed.
        if(!getStatus().equals(STATUS.FULL))
        	return list;
        
        if(queue.size()>0)
            list.addAll(queue);
        if(list.size()>0)
            queue.clear();
        
        synchronized (filteredEventCache) {
            list.addAll(filteredEventCache.clear());
        }
        size.set(0);
        if(internalTransLock.isHeldByCurrentThread())
            internalTransLock.unlock();
        System.out.println("NbiEventCache is cleared with total events removed: " + list.size());
        return list;
    }
    
    public static BmscEventRetryTO removeAndGetNext(BmscEventRetryTO event)
    {
        if(!internalTransLock.isHeldByCurrentThread())
            internalTransLock.lock();
        if(event==null || event.getEMBMSSessionId() == null) return null;
        
        synchronized (filteredEventCache) {
            filteredEventCache.poll(event.getEMBMSSessionId());
            event = filteredEventCache.peek(event.getEMBMSSessionId());
            if(event!=null)
                size.decrementAndGet();
            if(size.get()>3500)
                System.out.println(Thread.currentThread().getName() + " Reduce to " + size.get());
            internalTransLock.unlock();
            return event; 
        }
    }
    
    public static int size()
    {
        return size.get();
    }
    
}

/*
 * Filter the events with the eMBMS session id.
 * It's a map with eMBMS session id as key, a event queue as value.
 */
class FilteredEventCache
{
    private Map<Long, Queue<BmscEventRetryTO>> filteredEventMap;
    private int filteredCacheSize;
    
    public FilteredEventCache()
    {
        filteredEventMap = new HashMap<Long, Queue<BmscEventRetryTO>>();
        filteredCacheSize = 0;
    }
    
    public void add(BmscEventRetryTO event)
    {
        Long id = event.getEMBMSSessionId();
        if(id==null)
            return;
        Queue<BmscEventRetryTO> q = filteredEventMap.get(id);
        
        if(q == null)
        {
            q = new LinkedBlockingQueue<BmscEventRetryTO>();
        }
        q.offer(event);
        filteredCacheSize++;
        filteredEventMap.put(id, q);
    }
    
    public BmscEventRetryTO poll(Long id)
    {
        if(id==null)
            return null;
        Queue<BmscEventRetryTO> q = filteredEventMap.get(id);
        if(q==null)
            return null;
        BmscEventRetryTO event = q.poll();
        if(event!=null)
            filteredCacheSize--;
        if(q.size()==0)
            filteredEventMap.remove(id);
        return event;
    }
    
    public BmscEventRetryTO peek(Long id)
    {
        if(id==null)
            return null;
        Queue<BmscEventRetryTO> q = filteredEventMap.get(id);
        if(q==null)
            return null;
        return q.peek();
    }
    
    public boolean containSameSessionId(Long id)
    {
        if(id==null)
            return false;
        Queue<BmscEventRetryTO> q = filteredEventMap.get(id);
        if(q!=null && q.size()>0)
            return true;
        return false;
    }
    
    public int size()
    {
        return filteredCacheSize;
    }
    
    public List<BmscEventRetryTO> clear()
    {
        ArrayList<BmscEventRetryTO> list = new ArrayList<>();
        if(filteredEventMap.size()==0){
            filteredCacheSize = 0;
            return list;
        }
        for(Queue<BmscEventRetryTO> queue : filteredEventMap.values())
        {
            list.addAll(queue);
        }
        
        this.filteredEventMap.clear();
        filteredCacheSize = 0;
        return list;
    }
}

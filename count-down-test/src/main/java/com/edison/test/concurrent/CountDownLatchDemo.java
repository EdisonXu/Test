package com.edison.test.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class CountDownLatchDemo {
    
    private static final int PLAY_AMOUNT = 10;
    
    /**
     * @param args
     */
    public static void main(String[] args) {

        // once count down to 0, means match begin
        CountDownLatch begin = new CountDownLatch(1);
        
        // total players amount joining this match. Each count down means one play has arrive the end.
        CountDownLatch end = new CountDownLatch(PLAY_AMOUNT);

        // initialize players
        Player[] players = new Player[PLAY_AMOUNT];
        for(int i=0;i<PLAY_AMOUNT;i++)
        {
            players[i] = new Player(i+1, begin, end);
        }
        ExecutorService exe = Executors.newFixedThreadPool(PLAY_AMOUNT);
        // start each player, which means all players at the line
        for(Player p:players)
        {
            exe.execute(p);
        }
        System.out.println("Running match begin!");
        begin.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally
        {
            System.out.println("Running match finish!");
        }
        
        exe.shutdown();
    }

}

class Player implements Runnable
{
    private int id;
    
    private CountDownLatch begin;
    
    private CountDownLatch end;
    
    /**
     * @param id
     * @param begin
     * @param end
     */
    public Player(int id, CountDownLatch begin, CountDownLatch end) {
        super();
        this.id = id;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        try {
            begin.await(); // wait till begin counting down to 0
            Thread.sleep((long)(Math.random()*100));
            System.out.println("Player " + id + " has arrived");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally
        {
            end.countDown();
        }
    }
    
}
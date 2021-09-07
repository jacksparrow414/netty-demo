package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    private static final String THREAD_NUM="dhb-thread";
    public static void main(String[] args)  throws InterruptedException{
        Thread thread=new Thread(() -> {
            while (true) {
                System.out.println("I am a sub thread");
                try {
                    System.out.println(Thread.currentThread().isInterrupted());
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().isInterrupted());
                }
                System.out.println("--I am interrupted--");
            }
        },THREAD_NUM);
        thread.start();
        Thread.sleep(3000);
        System.out.println("--begin interrupt sub thread--");
        thread.interrupt();
        System.out.println("--end   interrupt sub thread--");
    }
}

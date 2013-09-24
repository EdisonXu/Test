package com.edi.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Unit test for simple App.
 */
public class TestAll 
{
    
    @Test
    public void test()
    {
        for(int i=0;i<100;i++)
        {
            JUnitCore junit = new JUnitCore();
            System.out.println((i+1) + " times run start");
            Result result = junit.run(NbiEventSendTaskTest.class);
            if(!result.wasSuccessful())
            {
                List list = result.getFailures();
                System.out.println(Arrays.toString(list.toArray()));
                Assert.assertTrue(false);
            }

        }

        Assert.assertTrue(true);
    }
}
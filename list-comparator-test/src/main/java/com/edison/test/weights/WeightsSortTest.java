package com.edison.test.weights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test Collection.sort(Collection<>) method usage with an Object implements Comparable.
 */
public class WeightsSortTest {

    public static void main(String[] args) {
        Weights w1 = new Weights(1.0D);
        Weights w2 = new Weights(5.0D);
        Weights w3 = new Weights(0.5D);
        Weights w4 = new Weights(10.0D);
        
        List<Weights> list = new ArrayList<Weights>();
        list.add(w1);
        list.add(w2);
        list.add(w3);
        list.add(w4);
        
        System.out.println("Weights order:");
        Collections.sort(list);
        for (Weights weights : list) {
            System.out.println(weights);
        }
        
    }
}

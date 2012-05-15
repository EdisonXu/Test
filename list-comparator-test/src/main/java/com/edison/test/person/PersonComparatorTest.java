package com.edison.test.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.edison.test.person.comparator.PersonComparatorFactory;
import com.edison.test.person.comparator.PersonComparatorFactory.ComparatorType;

/**
 * Test Collection.sort(Collection<>, Comparator<>) method usage with custom comparators.
 */
public class PersonComparatorTest {
    
    public static void printSortedList(ComparatorType type, List<Person> list)
    {
        if(list.isEmpty() || type == null)
        {
            System.out.println("Invalid parameters for sort.");
            return;
        }
        System.out.println("Sorted List in " + type.name() + " order:");
        Collections.sort(list, PersonComparatorFactory.getComparator(type));
        for(Person p:list)
        {
            System.out.println(p);
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        Person p1 = new Person(21, "Jason");
        Person p2 = new Person(19, "Anny");
        Person p3 = new Person(25, "Soraca");
        Person p4 = new Person(12, "Kenn");
        
        List<Person> list = new ArrayList<Person>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        
        PersonComparatorTest.printSortedList(ComparatorType.AGE_ASCENT, list);
        PersonComparatorTest.printSortedList(ComparatorType.AGE_DESCENT, list);
        PersonComparatorTest.printSortedList(ComparatorType.NAME_ASCENT, list);
        PersonComparatorTest.printSortedList(ComparatorType.NAME_DESCENT, list);
        
    }
}

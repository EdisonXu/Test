package com.edison.test.person.comparator;

import java.util.Comparator;

import com.edison.test.person.Person;


/**
 *
 */
public class PersonNameDescentComparator implements Comparator<Person> {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Person p1, Person p2) {
        String name1 = p1.getName();
        String name2 = p2.getName();
        if(name1.compareToIgnoreCase(name2)>0)
        {
            return -1;
        }else if(name1.compareToIgnoreCase(name2)<0)
        {
            return 1;
        }else
        {
            return 0;
        }
    }

}

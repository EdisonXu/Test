package com.edison.test.person.comparator;

import java.util.Comparator;

import com.edison.test.person.Person;


/**
 *
 */
public class PersonNameAscentComparator implements Comparator<Person> {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getName().compareToIgnoreCase(p2.getName());
    }

}

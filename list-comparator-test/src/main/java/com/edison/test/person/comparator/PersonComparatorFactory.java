package com.edison.test.person.comparator;

import com.edison.test.person.Person;


/**
 *
 */
public class PersonComparatorFactory {

    public static enum ComparatorType 
    {
        AGE_ASCENT,
        AGE_DESCENT,
        NAME_ASCENT,
        NAME_DESCENT;
    }
    
    public static java.util.Comparator<Person> getComparator(ComparatorType type)
    {
        switch(type)
        {
        case AGE_ASCENT:
            return new PersonAgeAscentComparator();
        case AGE_DESCENT:
            return new PersonAgeDescentComparator();
        case NAME_ASCENT:
            return new PersonNameAscentComparator();
        case NAME_DESCENT:
            return new PersonNameDescentComparator();
        default: 
            return null;
        }
        
    }
}

package com.edison.test.person;

/**
 * Person example with age/name.
 */
public class Person {

    private int age;
    private String name;
    
    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }
    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Person [age=" + age + ", name=" + name + "]";
    }
    /**
     * @param age
     * @param name
     */
    public Person(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }
    
}

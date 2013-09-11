package com.edi.test;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/users")
public class MyApplication extends Application {
    
    /*//private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();
    
    public MyApplication() 
    {
        //singletons.add(new UserHandler());
        classes.add(UserHandler.class);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }*/
    
}

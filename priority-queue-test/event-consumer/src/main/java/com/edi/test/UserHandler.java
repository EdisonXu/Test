package com.edi.test;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Stateless
@Path("/users")
public class UserHandler {

    
    /*@POST
    @Path("/create")
    @Consumes({ "application/xml" })
    public Response addUser(UserType user)
    {
        MyUser myUser = new MyUser();
        myUser.setId(user.getId());
        myUser.setName(user.getName());
        System.out.println(myUser + " is created.");
        map.put(myUser.getId(), myUser);
        return Response.created(URI.create("/users/" + myUser.getId())).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/xml")
    public UserType getUser(@PathParam("id") int id)
    {
        MyUser user = map.get(Integer.valueOf(id));
        if(user==null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        UserType u = new UserType();
        u.setId(user.getId());
        u.setName(user.getName());
        return u;
    }*/
    
}

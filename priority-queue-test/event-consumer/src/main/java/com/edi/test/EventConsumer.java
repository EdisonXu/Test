package com.edi.test;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.edi.test.restful.bean.MyEventType;


@Stateless
@Path("/")
public class EventConsumer {

    @POST
    @Path("services/sendEvent")
    @Consumes("application/xml")
    @Produces("application/xml")
    Response sendEvent(@Context javax.servlet.http.HttpServletRequest request,
                        @QueryParam("origin") String param,
                        MyEventType Event)
    {
        ResponseBuilder builder = Response.status(Response.Status.OK);
        return builder.build();
    }
}

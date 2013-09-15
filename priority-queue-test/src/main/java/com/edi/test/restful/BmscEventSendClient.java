package com.edi.test.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.client.ClientResponse;

public interface BmscEventSendClient {

	@POST
	@Path("services/sendEvent")
	@Consumes("application/xml")
	@Produces("application/xml")
	ClientResponse<StatusReportType> sendEvent(@QueryParam("origin") String param, String msgBody);
}

package hayden.ideasketch.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/hello-world")
public class HelloWorld {

	@GET
	@Path("/greetings")
	public Response sayHello() {
		String result = "<h1>RESTful Demo Application</h1>In real world application, a collection of users will be returned !!";
		return Response.status(Status.OK).type(MediaType.TEXT_HTML).entity(result).build();
	}

}

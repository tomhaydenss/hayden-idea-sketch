package hayden.ideasketch.rest.concurrency.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ServicoPessoaClient {

	@GET
	@Path("/servico/pessoas/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response pesquisarPessoa(@PathParam("id") Long id);

	@PUT
	@Path("/servico/pessoas/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response atualizarPessoa(@PathParam("id") Long id, @HeaderParam("If-Match") String eTag, @FormParam("idade") Integer idade);

}

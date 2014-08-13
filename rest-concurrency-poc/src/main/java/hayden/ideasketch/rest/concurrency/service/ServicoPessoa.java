package hayden.ideasketch.rest.concurrency.service;

import hayden.ideasketch.rest.concurrency.business.GerenciadorPessoa;
import hayden.ideasketch.rest.concurrency.model.Pessoa;
import hayden.ideasketch.rest.concurrency.util.ETagHelper;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/servico/pessoas")
@Consumes(MediaType.TEXT_PLAIN)
public class ServicoPessoa {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") Long id) {

		GerenciadorPessoa gerenciador = new GerenciadorPessoa();
		Pessoa pessoa = gerenciador.pesquisarPorId(id);

		if (pessoa != null) {
			ETagHelper eTagHelper = new ETagHelper();
			String eTag = eTagHelper.gerar(pessoa);
			return Response.ok(pessoa).tag(eTag).type(MediaType.APPLICATION_JSON).build();
		} else {
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Objeto nao encontrado.").build());
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response atualizarPessoa(@PathParam("id") Long id, @HeaderParam("If-Match") String eTag, @FormParam("idade") Integer idade) {

		GerenciadorPessoa gerenciador = new GerenciadorPessoa();
		Pessoa pessoa = gerenciador.pesquisarPorId(id);
		if (pessoa != null) {

			ETagHelper eTagHelper = new ETagHelper();
			if (!eTagHelper.validar(eTag, pessoa)) {
				throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity("Objeto nao modificado. Lock na aplicacao.").build());
			}

			boolean atualizado = gerenciador.atualizar(id, idade);
			if (atualizado) {
				return Response.status(Response.Status.ACCEPTED).entity("Objeto modificado.").build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).entity("Objeto nao modificado. Lock em Banco.").build();
			}

		} else {
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Objeto nao encontrado.").build());
		}

	}

}

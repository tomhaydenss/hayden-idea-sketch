package hayden.ideasketch.rest.function;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/validation")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class ServicoValidacaoCPF {

	@Path("/cpf/{valor}")
	@GET
	public String validarCPF(@PathParam("valor") String cpf) {
		boolean resultado = ValidadorCPF.validar(cpf);
		if (!resultado) {
			throw new WebApplicationException(Response
					.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
					.entity("CPF inválido").build());
		}
		return "CPF válido";
	}

}

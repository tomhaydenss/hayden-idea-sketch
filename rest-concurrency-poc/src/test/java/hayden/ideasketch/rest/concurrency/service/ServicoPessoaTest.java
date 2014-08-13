package hayden.ideasketch.rest.concurrency.service;

import java.util.Random;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class ServicoPessoaTest {

	private static final String URI_TARGET_BASE = "http://localhost:8080/rest-concurrency-poc";
	private static final Long PESSOA_JOAO_ID = 1L;
	private static final String PESSOA_JOAO_NOME = "Joao";
	
	private ServicoPessoaClient servicoPessoa;
	private Random randomGenerator;
	
	@Before
	public void inicializarRecursos() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(URI_TARGET_BASE);
		servicoPessoa = target.proxy(ServicoPessoaClient.class);
		randomGenerator = new Random();
	}

	@Test
	public void deveConsultarPessoa() {
		Response respostaPesquisarPessoa = servicoPessoa.pesquisarPessoa(PESSOA_JOAO_ID);
		
		Gson gson = new Gson();
		Pessoa p = gson.fromJson(respostaPesquisarPessoa.readEntity(String.class), Pessoa.class);
		
		Assert.assertEquals(Response.Status.OK, respostaPesquisarPessoa.getStatusInfo());
		Assert.assertEquals(PESSOA_JOAO_ID, p.getId());
		Assert.assertEquals(PESSOA_JOAO_NOME, p.getNome());
		
		respostaPesquisarPessoa.close();
	}
	
	@Test
	public void deveAtualizarIdadeDaPessoa() {
		Response respostaPesquisarPessoa = servicoPessoa.pesquisarPessoa(PESSOA_JOAO_ID);
		String eTag = respostaPesquisarPessoa.getEntityTag().getValue();
		int idade = gerarIdade();
		respostaPesquisarPessoa.close();
		
		Response respostaAtualizarPessoa = servicoPessoa.atualizarPessoa(PESSOA_JOAO_ID, eTag, idade);
		
		Assert.assertEquals(Response.Status.ACCEPTED, respostaAtualizarPessoa.getStatusInfo());
		respostaAtualizarPessoa.close();
	}

	private int gerarIdade() {
		return randomGenerator.nextInt(80);
	}

}

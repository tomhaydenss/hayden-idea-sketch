package hayden.ideasketch.rest.concurrency.service;

import java.util.Date;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class DedoNervoso implements Runnable {

	private static final Logger logger = Logger.getLogger(DedoNervoso.class);
	private static final String URI_TARGET_BASE = "http://localhost:8080/rest-concurrency-poc";
	private static final Long PESSOA_JOAO_ID = 1L;

	private ServicoPessoaClient servicoPessoa;

	private String nomeThread;
	private Integer idade;

	public DedoNervoso(String nomeThread, Integer idade) {
		super();
		this.nomeThread = nomeThread;
		this.idade = idade;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(URI_TARGET_BASE);
		servicoPessoa = target.proxy(ServicoPessoaClient.class);
	}

	public void run() {

		String eTag = pesquisarObjeto();
		dormir();
		atualizarObjeto(eTag);

	}

	private void atualizarObjeto(String eTag) {
		Response resposta = servicoPessoa.atualizarPessoa(PESSOA_JOAO_ID, eTag, idade);
		String info;
		switch (resposta.getStatus()) {
		case 202:
			info = "DedoNervoso[" + nomeThread + "]: Response " + resposta.getStatus() + " - Objeto Modificado.";
			break;
		case 304:
			info = "DedoNervoso[" + nomeThread + "]: Response " + resposta.getStatus() + " - Objeto Nao Modificado. Lock em nivel de Banco de Dados.";
			break;
		case 409:
			info = "DedoNervoso[" + nomeThread + "]: Response " + resposta.getStatus() + " - Objeto Nao Modificado. Lock em nivel de Aplicacao.";
			break;
		default:
			info = "DedoNervoso[" + nomeThread + "]: Response " + resposta.getStatus() + " - Nao Esperado.";
			break;
		}
		logger.info(info + " - " + System.currentTimeMillis());
	}

	private String pesquisarObjeto() {
		Response resposta = servicoPessoa.pesquisarPessoa(PESSOA_JOAO_ID);
		String eTag = resposta.getEntityTag().getValue();
		resposta.close();
		logger.info("DedoNervoso[" + nomeThread + "] pesquisou objeto com eTag " + eTag);
		return eTag;
	}

	private void dormir() {
		try {
			logger.info("DedoNervoso[" + nomeThread + "] dormindo 10s...");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			logger.error("Erro durante a soneca do DedoNervoso[" + this.nomeThread + "]", e);
		}

	}

}

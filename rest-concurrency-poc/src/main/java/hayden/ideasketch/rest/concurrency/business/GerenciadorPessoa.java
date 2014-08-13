package hayden.ideasketch.rest.concurrency.business;

import hayden.ideasketch.rest.concurrency.model.Pessoa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class GerenciadorPessoa {

	private static Logger logger = Logger.getLogger(GerenciadorPessoa.class);
	
	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("concurrencyPocPU");
	private EntityManager entityManager = factory.createEntityManager();

	public Pessoa pesquisarPorId(Long id) {
		Pessoa pessoa = entityManager.find(Pessoa.class, id);
		entityManager.detach(pessoa);
		return pessoa;
	}

	public boolean atualizar(Long id, Integer idade) {
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		try {
			Pessoa novo = entityManager.find(Pessoa.class, id);
			entityManager.lock(novo, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
			novo.setIdade(idade);
			tx.commit();
			return true;
		} catch (Exception e) {
			logger.error("Erro durante atualizacao de Pessoa.", e);
			return false;
		}
	}

}

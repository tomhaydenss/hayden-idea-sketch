package hayden.ideasketch.rest.concurrency.main;

import java.util.List;

import hayden.ideasketch.rest.concurrency.model.Pessoa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaTest {

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("concurrencyPocPU");
		EntityManager manager = factory.createEntityManager();
		JpaTest test = new JpaTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.criarPessoas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();

		test.listarPessoas();

		System.out.println(".. pronto");
	}

	private void criarPessoas() {
		int numOfEmployees = manager.createQuery("Select p From Pessoa p", Pessoa.class).getResultList().size();
		if (numOfEmployees == 0) {
			manager.persist(new Pessoa("Joao"));
			manager.persist(new Pessoa("Maria"));
		}
	}

	private void listarPessoas() {
		List<Pessoa> result = manager.createQuery("Select p From Pessoa p", Pessoa.class).getResultList();
		System.out.println("Total de Pessoas: " + result.size());
		for (Pessoa p : result) {
			System.out.println("Pessoa: " + p);
		}
	}
}

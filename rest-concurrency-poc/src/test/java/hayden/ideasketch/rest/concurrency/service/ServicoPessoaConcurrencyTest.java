package hayden.ideasketch.rest.concurrency.service;

public class ServicoPessoaConcurrencyTest {

	private static final int QTD_THREADS = 10;

	public static void main(String[] args) {
		for (int i = 0; i < QTD_THREADS; i++) {
			new Thread(new DedoNervoso("t" + i, i), "t" + i).start();
		}
	}

}

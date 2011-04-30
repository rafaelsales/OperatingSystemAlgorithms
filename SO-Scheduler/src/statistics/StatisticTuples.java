package statistics;

public class StatisticTuples {

	private String chave;
	private String valor;

	
	public StatisticTuples(String chave, String valor) {
		super();
		this.chave = chave;
		this.valor = valor;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(final String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(final String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return chave + ": " + valor;
	}
}

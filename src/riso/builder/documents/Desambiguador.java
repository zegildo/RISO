package riso.builder.documents;


public class Desambiguador {

	private String[] paragrafo;
	private String[] vetorTematico;
	private String conceito;
	private String sentido;
	private double valor;
	
	
	public Desambiguador(String[] paragrafo, String conceito, String[] vetorTematico, String sentido ){
		setParagrafo(paragrafo);
		setVetorTematico(vetorTematico);
		setConceito(conceito);
		setSentido(sentido);
		setValor(paragrafo,vetorTematico);
	}
	
	private double calculaVetorTermos(String[] paragrafo, String[] vetorTematico){
		
		double mach = 0;
		for (String palavra : paragrafo) {
				for (String termo : vetorTematico) {
					if(palavra.equals(termo)){
						mach++;
					}
				}
		}
		return mach;
	}


	public String[] getParagrafo() {
		return paragrafo;
	}


	public void setParagrafo(String[] paragrafo) {
		this.paragrafo = paragrafo;
	}


	public String[] getVetorTematico() {
		return vetorTematico;
	}


	public void setVetorTematico(String[] vetorTematico) {
		this.vetorTematico = vetorTematico;
	}


	public String getConceito() {
		return conceito;
	}


	public void setConceito(String conceito) {
		this.conceito = conceito;
	}


	public String getSentido() {
		return sentido;
	}


	public void setSentido(String sentido) {
		this.sentido = sentido;
	}


	public double getValor() {
		return valor;
	}


	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public void setValor(String[] paragrafo, String[] vetorTematico) {
		double valor = calculaVetorTermos(paragrafo, vetorTematico);
		setValor(valor);
	}
	
}

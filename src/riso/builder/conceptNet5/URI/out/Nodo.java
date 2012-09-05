package riso.builder.conceptNet5.URI.out;

import java.util.List;

public class Nodo {

	private List<Nodo> pai;
	private List<Nodo> filho;
	private List<String> sinonimos;
	private String frase;
	

	private String relacaoFilho;
	private String relacaoPai;
	private String conceito;
	
	
	public Nodo(String conceito){
		setConceito(conceito);
	}
	
	public String getFrase() {
		return frase;
	}

	public void setFrase(String frase) {
		this.frase = frase;
	}


	public List<String> getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(List<String> sinonimos) {
		this.sinonimos = sinonimos;
	}
	
	public List<Nodo> getPai() {
		return pai;
	}


	public void setPai(List<Nodo> pai) {
		this.pai = pai;
	}


	public List<Nodo> getFilho() {
		return filho;
	}


	public void setFilho(List<Nodo> filho) {
		this.filho = filho;
	}


	public String getRelacaoFilho() {
		return relacaoFilho;
	}


	public void setRelacaoFilho(String relacaoFilho) {
		this.relacaoFilho = relacaoFilho;
	}


	public String getRelacaoPai() {
		return relacaoPai;
	}


	public void setRelacaoPai(String relacaoPai) {
		this.relacaoPai = relacaoPai;
	}


	public String getConceito() {
		return conceito;
	}


	public void setConceito(String conceito) {
		this.conceito = conceito;
	}
	
	
}

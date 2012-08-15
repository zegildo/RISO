package riso.builder.conceptNet5.URI.in.actions.association;

import riso.builder.conceptNet5.URI.in.Constantes;

public class ComponenteAssociacao {

	private String termo;
	private int peso;

	public ComponenteAssociacao(String termo, int peso){
		setTermo(termo);
		setPeso(peso);
	}

	public ComponenteAssociacao(String termo){
		setTermo(termo);
	}

	public String getTermo() {
		return termo;
	}

	public void setTermo(String termo) {
		this.termo = termo;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	public String getPesoConf(){
		return Constantes.ARROBA+getPeso();
	}
	
	public String toString(){
		
		return getTermo()+getPesoConf();
	}
}

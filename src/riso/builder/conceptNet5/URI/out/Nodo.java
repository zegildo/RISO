package riso.builder.conceptNet5.URI.out;



public class Nodo {

	private Estrutura estrutura;
	private String conceito;

	
	public Nodo(String conceito){
		setConceito(conceito);
		setEstrutura(new Estrutura());
	}
	
	public Estrutura getEstrutura() {
		return estrutura;
	}

	public void setEstrutura(Estrutura estrutura) {
		this.estrutura = estrutura;
	}

	public String getConceito() {
		return conceito;
	}


	public void setConceito(String conceito) {
		this.conceito = conceito;
	}

//	public void imprimeGenealogia(Nodo no){
//
//		Nodo noDaVez = no;
//		String ESPACO ="";
//		final int ZERO = Constantes.ZERO;
//		while(noDaVez != null){
//			String impressao = ESPACO+noDaVez.getConceito()+" ";
//			String sinonimos = "{";
//			if(!noDaVez.getSinonimos().isEmpty()){
//
//				for (String str : noDaVez.getSinonimos()) {
//					sinonimos+=str+",";
//				}
//				sinonimos=sinonimos.substring(0, sinonimos.length()-1);
//
//			}
//			sinonimos+="}";
//			impressao+=sinonimos;
//			System.out.println(impressao);
//
//			ESPACO+=" ";
//			noDaVez = ((noDaVez.getPai() == null) || (noDaVez.getPai().size() == ZERO))?null:noDaVez.getPai().get(ZERO);
//		}
//	}


}

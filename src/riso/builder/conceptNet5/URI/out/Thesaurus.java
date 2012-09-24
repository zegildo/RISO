package riso.builder.conceptNet5.URI.out;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riso.builder.conceptNet5.URI.Constantes;

public class Thesaurus {

	private final int PAIS = 0;
	private final String INVERSA = "-1";
	private final String RELACAO = "/"+Constantes.RELACOES+"/";


	private Map<String, Aresta> controle = new HashMap<String, Aresta>();

	private Map<String, String> controleRelacoes = new HashMap<String, String>();

	private GerenciadorDeConsultasConceptnet5 gerenciador;

	public Thesaurus(){
		setGerenciador(new GerenciadorDeConsultasConceptnet5());
	}

	public GerenciadorDeConsultasConceptnet5 getGerenciador() {
		return gerenciador;
	}

	public void setGerenciador(GerenciadorDeConsultasConceptnet5 gerenciador) {
		this.gerenciador = gerenciador;
	}

	public Map<String, Aresta> getControle() {
		return controle;
	}

	public void setControle(Map<String, Aresta> controle) {
		this.controle = controle;
	}

	public Map<String, String> getControleRelacoes() {
		return controleRelacoes;
	}

	public void setControleRelacoes(Map<String, String> controleRelacoes) {
		this.controleRelacoes = controleRelacoes;
	}

	//OK
	private void montaAncestrais(String fonte, String caracter, String conceito){

		FiltroDeResultados filtro = new FiltroDeResultados();
		Aresta aresta = getGerenciador().gerenciaSolicitacoes(fonte, caracter, conceito);
		List<Set<ArestaConceptNet>> paisEFilhos = filtro.eliminarConceitosFracamenteRelacionados(aresta.getEdges(), conceito);
		Set<ArestaConceptNet> pais = paisEFilhos.get(PAIS);
		Map<String, Set<ArestaConceptNet>> agrupa = filtro.criaGrupoDePais(pais);
		parentescoPorFilho(agrupa);

	}

	private void parentescoPorFilho(Map<String, Set<ArestaConceptNet>> agrupa){

		Map<String, Nodo> controleElementos = new HashMap<String, Nodo>();

		for (String chave : agrupa.keySet()) {

			Object[] ancestrais = agrupa.get(chave).toArray();
			Nodo filho = controleElementos.get(chave);

			for (int i = 0; i < (ancestrais.length)-1; i++) {

				String atual = ((ArestaConceptNet)ancestrais[i]).getEnd();
				String relacaoAtual = ((ArestaConceptNet)ancestrais[i]).getRel();
				relacaoAtual = relacaoAtual.replaceAll(RELACAO, "");

				Nodo noAtual = controleElementos.get(atual);

				for (int j = i+1; j < ancestrais.length; j++) {

					String daVez = ((ArestaConceptNet)ancestrais[j]).getEnd();
					Nodo noDaVez = controleElementos.get(daVez);
					String relacaoDaVez = ((ArestaConceptNet)ancestrais[i]).getRel();
					relacaoDaVez = relacaoDaVez.replaceAll(RELACAO, "");


					if(filho == null){
						filho = new Nodo(chave);
						controleElementos.put(chave, filho);
					}

					if(noAtual == null){	
						noAtual = new Nodo(atual);
						controleElementos.put(atual, noAtual);
					}

					if((noDaVez == null)){
						noDaVez = new Nodo(daVez);
						controleElementos.put(daVez, noDaVez);
					}
					//TODO verificar estas condicioes.
					if(getControleRelacoes().get(chave+"|"+atual) == null){
						posicionaNoEstrutura(filho, relacaoAtual, noAtual);
						posicionaNoEstrutura(noAtual, relacaoAtual+INVERSA, filho);
						getControleRelacoes().put(chave+"|"+atual, relacaoAtual);
					}

					if(getControleRelacoes().get(chave+"|"+daVez) == null){
						posicionaNoEstrutura(filho, relacaoDaVez, noDaVez);
						posicionaNoEstrutura(noDaVez, relacaoDaVez+INVERSA, filho);
						getControleRelacoes().put(chave+"|"+daVez, relacaoDaVez);
					}

					if((getControleRelacoes().get(atual+"|"+daVez) == null) && (getControleRelacoes().get(daVez+"|"+atual) == null)){

						String relacao = constroiRelacoes(noAtual, noDaVez);
						getControleRelacoes().put(atual+"|"+daVez, relacao);

					}
				}
			}
		}
	}


	//	public void parentescoPorPadre(Map<String, Set<ArestaConceptNet>> agrupa){
	//
	//		Object[] genericos = agrupa.keySet().toArray();
	//
	//		for (int i = 0; i < (genericos.length)-1; i++) {
	//
	//			String generilazaoAtual = (String) genericos[i];
	//			Nodo noGenerAtual = new Nodo(generilazaoAtual);
	//			boolean naoSeRelacionou = true;
	//
	//			for (int j = i+1; j < genericos.length; j++) {
	//
	//				String generilazaoDaVez = (String) genericos[j];
	//				Nodo noGenerDaVez = new Nodo(generilazaoDaVez);
	//				Nodo no = constroiRelacoes(noGenerAtual, noGenerDaVez);
	//
	//				if(no != null){
	//
	//					if(no.equals(noGenerDaVez)){
	//
	//
	//					}else{
	//
	//
	//					}
	//				} 
	//
	//			}
	//			if(naoSeRelacionou){
	//
	//			}
	//		}
	//	}

	private String verificaExistenciaDeAlgumaRelacao(Nodo conceitoInicio, Nodo conceitoFim){

		String finall = "http://conceptnet5.media.mit.edu/data/5.1/search?start="+conceitoInicio.getConceito()+"/&end="+conceitoFim.getConceito()+"/&limit=1";
		Aresta aresta = getGerenciador().consultaConceptnet5(finall);

		if(aresta.getNumFound() > 0){

			return verificaConstrucaoDeRelacao(aresta, conceitoInicio, conceitoFim);
		}

		return null;
	}


	private String verificaExistenciaDeRelacoesFortes(Nodo conceitoInicio, Nodo conceitoFim){

		for (String relacao : Constantes.RELACOES_SEMANTICAS_FORTES) {

			String finall = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+conceitoInicio.getConceito()+"/,"+conceitoFim.getConceito()+"/]&limit=1";
			Aresta aresta = getGerenciador().consultaConceptnet5(finall);

			if(aresta.getNumFound() > 0){

				return construcoeRelacaoForte(aresta, conceitoInicio, conceitoFim);
			}
		}

		return null;
	}

	private String construcoeRelacaoForte(Aresta arestaDireta, Nodo conceitoInicio, Nodo conceitoFim){

		String relacao = ((ArestaConceptNet) arestaDireta.getEdges()
				.toArray()[0]).getRel();

		relacao = relacao.replaceAll(RELACAO, "");

		evitaDuplicata(conceitoInicio, relacao, conceitoFim);
		evitaDuplicata(conceitoFim, relacao+INVERSA, conceitoInicio);

		return relacao;

	}

	private String verificaConstrucaoDeRelacao(Aresta arestaDireta, Nodo conceitoInicio, Nodo conceitoFim){

		String conceitoIn = ((ArestaConceptNet) arestaDireta.getEdges()
				.toArray()[0]).getStart();
		String conceitoEnd = ((ArestaConceptNet) arestaDireta.getEdges()
				.toArray()[0]).getEnd();
		if((conceitoIn.equals(conceitoInicio.getConceito())) && 
				(conceitoEnd.equals(conceitoFim.getConceito()))){
			String relacao = ((ArestaConceptNet) arestaDireta.getEdges()
					.toArray()[0]).getRel();

			relacao = relacao.replaceAll(RELACAO, "");

			evitaDuplicata(conceitoInicio, relacao, conceitoFim);
			evitaDuplicata(conceitoFim, relacao+INVERSA, conceitoInicio);

			return relacao;
		}
		return "";

	}



	public String constroiRelacoes(Nodo conceitoInicio, Nodo conceitoFim){

		String relacao = getControleRelacoes().get(conceitoInicio.getConceito()+"|"+conceitoFim.getConceito());

		if(relacao == null){

			relacao = verificaExistenciaDeRelacoesFortes(conceitoInicio, conceitoFim);

			if(relacao == null){

				relacao = verificaExistenciaDeRelacoesFortes(conceitoFim, conceitoInicio);

				if(relacao == null){

					relacao = verificaExistenciaDeAlgumaRelacao(conceitoInicio, conceitoFim);

					if(relacao == null){

						relacao = verificaExistenciaDeAlgumaRelacao(conceitoFim, conceitoInicio);

						if(relacao == null){
							return "";
						}
					}
				}
			}
		}

		return relacao;
	}



	private void posicionaNoEstrutura(Nodo inicio, String relacao, Nodo fim){


		Set<Nodo> nodosInicio = recuperaNodosDeUmaRelacao(inicio, relacao);

		if(nodosInicio == null){
			nodosInicio = new HashSet<Nodo>();
			inicio.getEstrutura().getRelacoes().put(relacao,nodosInicio);
			nodosInicio.add(fim);	

		}else{

			Iterator<Nodo> nodosRelacao = nodosInicio.iterator();
			boolean naoAdicionou = true;

			while(nodosRelacao.hasNext()){

				Nodo nodoDaVez = nodosRelacao.next();

				String nodoDaVezRepresentacao = nodoDaVez.getConceito();
				String nodoFinal = fim.getConceito();

				String submissInicio = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+nodoDaVezRepresentacao+"/,"+nodoFinal+"/]&limit=0";
				String submissFim = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+nodoFinal+"/,"+nodoDaVezRepresentacao+"/]&limit=0";

				if(isInversa(relacao)){
					submissInicio = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+obtemInversa(relacao)+"/,"+nodoFinal+"/,"+nodoDaVezRepresentacao+"/]&limit=0";
					submissFim = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+obtemInversa(relacao)+"/,"+nodoDaVezRepresentacao+"/,"+nodoFinal+"/]&limit=0";
				}

				Aresta arestaAtual = getControle().get(submissInicio);	

				if(arestaAtual == null){
					arestaAtual = getGerenciador().consultaConceptnet5(submissInicio);		
					getControle().put(submissInicio, arestaAtual);
				}

				Aresta arestaDaVez = getControle().get(submissFim);

				if(arestaDaVez == null){
					arestaDaVez = getGerenciador().consultaConceptnet5(submissFim);	
					getControle().put(submissFim, arestaDaVez);
				}

				if(arestaAtual.getNumFound() > arestaDaVez.getNumFound()){

					evitaDuplicata(nodoDaVez, relacao, fim);
					evitaDuplicata(fim, obtemInversa(relacao), nodoDaVez);

					naoAdicionou = false;

				}else if(arestaDaVez.getNumFound() > arestaAtual.getNumFound()){

					Set<Nodo> nodosDaVez = recuperaNodosDeUmaRelacao(nodoDaVez, obtemInversa(relacao));

					nodosDaVez.remove(inicio);
					nodosDaVez.add(fim);
					getControleRelacoes().put(nodoDaVez.getConceito()+"|"+fim.getConceito(),obtemInversa(relacao));

					nodosInicio.remove(nodoDaVez);
					nodosInicio.add(fim);
					getControleRelacoes().put(inicio.getConceito()+"|"+fim.getConceito(),relacao);

					evitaDuplicata(fim, relacao, nodoDaVez);

					naoAdicionou = false;
				}		
			}
			if(naoAdicionou){
				nodosInicio.add(fim);	
			}
		}
	}

	private String obtemInversa(String relacao){
		if(isInversa(relacao)){
			return relacao.replaceAll(INVERSA, "");
		}else{
			return relacao+INVERSA;
		}
	}

	private boolean isInversa(String relacao){

		final int ZERO = 0;

		if(relacao.indexOf(INVERSA) > ZERO){
			return true;
		}
		return false;

	}

	private void evitaDuplicata(Nodo noInicial, String relacao, Nodo noFinal){

		if(!getControleRelacoes().containsKey(noInicial.getConceito()+"|"+noFinal.getConceito())){
			getControleRelacoes().put(noInicial.getConceito()+"|"+noFinal.getConceito(),relacao);
			posicionaNoEstrutura(noInicial, relacao, noFinal);
		}
	}

	private Set<Nodo> recuperaNodosDeUmaRelacao(Nodo no, String relacao){

		Estrutura estrutura = no.getEstrutura();
		Map<String, Set<Nodo>> relacoes = estrutura.getRelacoes();
		Set<Nodo> nodos = relacoes.get(relacao);

		return nodos;

	}


	public static void main(String args[]){

		//String searchText = "http://conceptnet5.media.mit.edu/data/5.1/search?text=";
		//String searchSurface = "http://conceptnet5.media.mit.edu/data/5.1/search?surfaceText=";

		//String lookUp = "http://conceptnet5.media.mit.edu/data/5.1/c/en/";

		//String conceito = "jaguar";
		Thesaurus t = new Thesaurus();


		Nodo jaguar = new Nodo("/c/en/jaguar");
		Nodo animal = new Nodo("/c/en/animal");
		Nodo mamifero = new Nodo("/c/en/mammal");

		Set<Nodo> nodosDiretos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoesDiretas = new HashMap<String, Set<Nodo>>();
		nodosDiretos.add(animal);
		relacoesDiretas.put("IsA", nodosDiretos);

		Estrutura estruDireta = new Estrutura();
		estruDireta.setRelacoes(relacoesDiretas);
		jaguar.setEstrutura(estruDireta);

		Set<Nodo> nodosInversos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoesInversas = new HashMap<String, Set<Nodo>>();
		nodosInversos.add(jaguar);
		relacoesInversas.put("IsA-1", nodosInversos);

		Estrutura estruInversa = new Estrutura();
		estruInversa.setRelacoes(relacoesInversas);
		animal.setEstrutura(estruInversa);


		t.constroiRelacoes(jaguar, mamifero);
		t.constroiRelacoes(mamifero,animal);
		t.constroiRelacoes(jaguar, mamifero);
		//t.montaAncestrais(searchText,"&",conceito);

		/*Aresta aresta = prepara(searchText+"jaguar&limit="+Constantes.ZERO);
		System.out.println("O que deve ser: "+aresta.getNumFound());
		Aresta arestaAux = null;


		long inicio = System.currentTimeMillis();
		if(aresta.getNumFound() > JANELA){
			int qtIt = aresta.getNumFound() / JANELA;
			int rest = aresta.getNumFound() % JANELA;
			int i = 1;
			aresta = prepara(searchText+"jaguar&limit="+JANELA);
			while(i < qtIt){
				arestaAux = prepara(searchText+"jaguar&limit="+JANELA+"&offset="+i*JANELA);
				aresta.addSetEdge(arestaAux.getEdges());
				i++;
			}
			arestaAux = prepara(searchText+"jaguar&limit="+rest+"&offset="+i*JANELA);
			aresta.addSetEdge(arestaAux.getEdges());
		}else{
			aresta = prepara(searchText+"jaguar&limit="+aresta.getNumFound());
		}
		System.out.println("Tempo gasto:"+ ((System.currentTimeMillis() - inicio) / 1000)+ "segundos");
		System.out.println("O que veio: "+aresta.getEdges().size());*/




		//
		//		Set<Nodo> nodos = new HashSet<Nodo>();
		//		Map<String, Set<Nodo>> relacoes = new HashMap<String, Set<Nodo>>();
		//		nodos.add(life);
		//		nodos.add(organism);
		//		relacoes.put("IsA", nodos);
		//
		//		Estrutura estru = new Estrutura();
		//		estru.setRelacoes(relacoes);
		//		plant.setEstrutura(estru);
		//		constroiRelacoes(plant, liveThing);


		//Caso 1 - Jaguar Animal
		//t.constroiRelacoes(animal, jaguar);
		//t.constroiRelacoes(jaguar, animal);



		//Caso 3 - OK -  troca de posicao entre jaguar-animal para jaguar-mamifero
		/*Set<Nodo> nodos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoes = new HashMap<String, Set<Nodo>>();
		nodos.add(mamifero);
		relacoes.put("IsA", nodos);

		Estrutura estru = new Estrutura();
		estru.setRelacoes(relacoes);
		jaguar.setEstrutura(estru);
		constroiRelacoes(jaguar, animal);*/

		//Caso 4 - OK -  adicionar elementos de mesma relação que não possuem 
		//relação hierarquica com os que já estao adicionados. 
		//Colocar nós semanticamente de mesmo nível
		/*Set<Nodo> nodos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoes = new HashMap<String, Set<Nodo>>();
		nodos.add(life);
		relacoes.put("IsA", nodos);

		Estrutura estru = new Estrutura();
		estru.setRelacoes(relacoes);
		plant.setEstrutura(estru);
		constroiRelacoes(plant, organism);*/


		/*Nodo organism = new Nodo("/c/en/organism/n/a_living_thing_that_has_the_ability_to_act_or_function_independently/");

		Nodo liveThing = new Nodo("/c/en/live_thing/n/a_living_entity/");

		Nodo life = new Nodo("/c/en/life/n/living_things_collectively/");
		Nodo plant = new Nodo("/c/en/plant/");

		Set<Nodo> nodos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoes = new HashMap<String, Set<Nodo>>();
		nodos.add(life);
		nodos.add(organism);
		relacoes.put("IsA", nodos);

		Estrutura estru = new Estrutura();
		estru.setRelacoes(relacoes);
		plant.setEstrutura(estru);
		constroiRelacoes(plant, liveThing);*/









		//		List<RelacoesValoradas> relacoes = relacoesHierarquicas("big");
		//
		//		for (RelacoesValoradas relacoesValoradas : relacoes) {
		//			System.out.println(relacoesValoradas.getRelacao());
		//		}
		//System.out.println("Antes ============== :"+listlookUp.size());

		//Set<ArestaConceptNet>  listSearchText = montaAncestrais(searchText,"&",conceito);

		//Set<ArestaConceptNet>  listSearchSurface = montaAncestrais(searchSurface,"&",conceito);

		//verificaElementosExclusivos(listlookUp,listSearchText);
		//System.out.println("Despues =============== :"+listlookUp.size());

		//verificaElementosExclusivos(listSearchText,listlookUp);

		//verificaElementosExclusivos(listlookUp,listSearchSurface);
		//verificaElementosExclusivos(listSearchSurface,listlookUp);

		//verificaElementosExclusivos(listSearchSurface,listSearchText);
		//verificaElementosExclusivos(listSearchText,listSearchSurface);

	}
}

package riso.builder.conceptNet5.URI.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riso.builder.conceptNet5.URI.Constantes;

import com.google.gson.Gson;

public class LerJson {

	public static final int PAIS = 0;
	public static final int JANELA = 5000;


	private static void parentescoPorFilho(Map<String, Set<ArestaConceptNet>> agrupa){

		Map<String, Nodo> controle = new HashMap<String, Nodo>();

		for (String chave : agrupa.keySet()) {

			Object[] ancestrais = agrupa.get(chave).toArray();

			for (int i = 0; i < (ancestrais.length)-1; i++) {

				String atual = ((ArestaConceptNet)ancestrais[i]).getEnd();
				Nodo noAtual = controle.get(atual);

				for (int j = i+1; j < ancestrais.length; j++) {

					String daVez = ((ArestaConceptNet)ancestrais[j]).getEnd();
					Nodo noDaVez = controle.get(daVez);

					if((noAtual == null) || (noDaVez == null)){

						if(noAtual == null){	
							noAtual = new Nodo(atual);
						}

						if((noDaVez == null)){
							noDaVez = new Nodo(daVez);
						}

						controle.put(atual, noAtual);
						controle.put(daVez, noDaVez);
						constroiRelacoes(noAtual,noDaVez);
					}
				}

			}
		}

	}

	public static void parentescoPorPadre(Map<String, Set<ArestaConceptNet>> agrupa){


	}

	private static boolean verificaExistenciaDeRelacao(Nodo conceitoInicio, Nodo conceitoFim){

		String finall = "http://conceptnet5.media.mit.edu/data/5.1/search?start="+conceitoInicio.getConceito()+"&end="+conceitoFim.getConceito()+"&limit=0";

		Aresta aresta = prepara(finall);

		if(aresta.getNumFound() > Constantes.ZERO){
			return true;
		}
		return false;
	}

	private static void constroiRelacoes(Nodo conceitoInicio, Nodo conceitoFim){

		boolean ordemDireta = verificaExistenciaDeRelacao(conceitoInicio, conceitoFim);
		boolean ordemIversa = verificaExistenciaDeRelacao(conceitoFim, conceitoInicio);
		if(ordemDireta || ordemIversa){
			
			Map<String, String> relacoesHierarquicas = Constantes.RELACOES_SEMANTICAS;

			for (String relacao : relacoesHierarquicas.keySet()) {

				constroiRelacao(conceitoInicio, relacao, conceitoFim);

			}
		}
		
	}

	/**
	 * Se consegue estabelecer a relacao entre dois conceitos
	 * retorna verdadeiro, caso contrario retorna false
	 * 
	 * @param conceitoInicio
	 * @param relacao
	 * @param conceitoFim
	 * @return
	 */
	private static void constroiRelacao(Nodo conceitoInicio, String relacao, Nodo conceitoFim){


		String textConceitoInicio = conceitoInicio.getConceito();
		String textConceitoFim = conceitoFim.getConceito();

		String submissInicio = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+textConceitoInicio+","+textConceitoFim+"]&limit=0";
		String submissFim = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+textConceitoFim+"/,"+textConceitoInicio+"]&limit=0";

		Aresta arestaAtual = prepara(submissInicio);	
		Aresta arestaDaVez = prepara(submissFim);			

		if(arestaAtual.getNumFound() > arestaDaVez.getNumFound()){
			posicionaNoEstrutura(conceitoInicio, relacao, conceitoFim);
		}else if(arestaDaVez.getNumFound() > arestaAtual.getNumFound()){
			posicionaNoEstrutura(conceitoFim, relacao, conceitoInicio);
		}		
	}

	private static void posicionaNoEstrutura(Nodo inicio, String relacao, Nodo fim){

		Estrutura estruturaInicio = inicio.getEstrutura();
		Map<String, Set<Nodo>> relacoesInicio = estruturaInicio.getRelacoes();
		Set<Nodo> nodosInicio = relacoesInicio.get(relacao);

		if(nodosInicio == null){
			nodosInicio = new HashSet<Nodo>();
			relacoesInicio.put(relacao,nodosInicio);
			nodosInicio.add(fim);	

		}else{

			Iterator<Nodo> nodosRelacao = nodosInicio.iterator();
			boolean naoAdicionou = true;

			while(nodosRelacao.hasNext()){

				Nodo nodoDaVez = nodosRelacao.next();

				String nodoDaVezRepresentacao = nodoDaVez.getConceito();
				String nodoFinal = fim.getConceito();

				String submissInicio = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+nodoDaVezRepresentacao+","+nodoFinal+"]&limit=0";
				String submissFim = "http://conceptnet5.media.mit.edu/data/5.1/search?uri=/a/[/r/"+relacao+"/,"+nodoFinal+","+nodoDaVezRepresentacao+"]&limit=0";

				Aresta arestaAtual = prepara(submissInicio);	
				Aresta arestaDaVez = prepara(submissFim);			

				if(arestaAtual.getNumFound() > arestaDaVez.getNumFound()){
					posicionaNoEstrutura(nodoDaVez, relacao, fim);
					posicionaNoEstrutura(fim, Constantes.RELACOES_SEMANTICAS.get(relacao), nodoDaVez);
					naoAdicionou = false;
				}else if(arestaDaVez.getNumFound() > arestaAtual.getNumFound()){
					nodosInicio.remove(nodoDaVez);
					nodosInicio.add(fim);
					posicionaNoEstrutura(fim, relacao, nodoDaVez);
					posicionaNoEstrutura(nodoDaVez, Constantes.RELACOES_SEMANTICAS.get(relacao), fim);
					naoAdicionou = false;
				}		
			}
			if(naoAdicionou){
				nodosInicio.add(fim);	
			}
		}
	}


	private static Aresta prepara(String request){

		Gson gson = new Gson();
		Aresta aresta = new Aresta();

		try {

			URL url = new URL(request);
			/*
			 * Como realizar a chamada a uma lista de associacao
			 * URL url = new URL("http://conceptnet5.media.mit.edu/data/5.1/assoc/list/en/boy,girl@-1?limit=5&rel=/r/PartOf/&offset=3&end=/c/en/car/");
			 * Associacao ass = gson.fromJson(br, Associacao.class);
			 * */
			BufferedReader br = new BufferedReader(
					new InputStreamReader(url.openStream()));
			aresta = gson.fromJson(br, Aresta.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return aresta;
	}

	//OK
	private static void montaAncestrais(String fonte, String caracter, String conceito){
		
		FiltroDeResultados filtro = new FiltroDeResultados();
		List<Set<ArestaConceptNet>> paisEFilhos = null;
		Aresta aresta = gerenciaSolicitacoes(fonte, caracter, conceito);
		paisEFilhos = filtro.eliminarConceitosFracamenteRelacionados(aresta.getEdges(), conceito);
		Set<ArestaConceptNet> pais = paisEFilhos.get(PAIS);
		Map<String, Set<ArestaConceptNet>> agrupa = filtro.agrupaTipos(pais);
		parentescoPorFilho(agrupa);

	}

	public static Aresta gerenciaSolicitacoes(String fonte, String caracter, String conceito){
	
		String uriFinal = fonte+conceito+caracter+Constantes.LIMIT;
		Aresta aresta = prepara(uriFinal+Constantes.ZERO);
		Aresta arestaAux = null;
		if(aresta.getNumFound() > JANELA){
			int qtIt = aresta.getNumFound() / JANELA;
			int rest = aresta.getNumFound() % JANELA;
			int i = 1;
			aresta = prepara(uriFinal+JANELA);
			while(i < qtIt){
				arestaAux = prepara(uriFinal+JANELA+Constantes.OFFSET+i*JANELA);
				aresta.addSetEdge(arestaAux.getEdges());
				i++;
			}
			arestaAux = prepara(uriFinal+rest+Constantes.OFFSET+i*JANELA);
			aresta.addSetEdge(arestaAux.getEdges());
		}else{
			aresta = prepara(uriFinal+aresta.getNumFound());
		}
		return aresta;
	}
	

	public static void main(String[] args) {

		String searchText = "http://conceptnet5.media.mit.edu/data/5.1/search?text=";
		//String searchSurface = "http://conceptnet5.media.mit.edu/data/5.1/search?surfaceText=";

		//String lookUp = "http://conceptnet5.media.mit.edu/data/5.1/c/en/";

		String conceito = "jaguar";

		montaAncestrais(searchText,"&",conceito);

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



		//Nodo jaguar = new Nodo("/c/en/jaguar/");
		//		Nodo animal = new Nodo("/c/en/animal/");
		//		Nodo mamifero = new Nodo("/c/en/mammal/");
		//		Nodo organism = new Nodo("/c/en/organism/n/a_living_thing_that_has_the_ability_to_act_or_function_independently/");
		//
		//		Nodo liveThing = new Nodo("/c/en/live_thing/n/a_living_entity/");
		//
		//		Nodo life = new Nodo("/c/en/life/n/living_things_collectively/");
		//		Nodo plant = new Nodo("/c/en/plant/");
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


		//Caso 2 - OK - troca de posicao entre jaguar-animal para jaguar-mamifero
		/*Set<Nodo> nodos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoes = new HashMap<String, Set<Nodo>>();
		nodos.add(animal);
		relacoes.put("IsA", nodos);

		Estrutura estru = new Estrutura();
		estru.setRelacoes(relacoes);
		jaguar.setEstrutura(estru);
		constroiRelacoes(jaguar, mamifero);*/


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

package riso.builder.conceptNet5.URI.out;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riso.builder.conceptNet5.URI.in.Constantes;
import riso.builder.wordnet.JWNL;
import riso.builder.wordnet.JWNLException;
import riso.builder.wordnet.data.IndexWord;
import riso.builder.wordnet.data.IndexWordSet;
import riso.builder.wordnet.data.POS;
import riso.builder.wordnet.data.Pointer;
import riso.builder.wordnet.data.PointerType;
import riso.builder.wordnet.data.Synset;
import riso.builder.wordnet.data.Word;
import riso.builder.wordnet.dictionary.DatabaseBackedDictionary;

import com.google.gson.Gson;

public class LerJson {

	public static final int ZERO = 0;
	public static final int PAIS = 0;
	public static final int JANELA = 5000;
	public static final String ESPACO = " ";
	public static final int POSICAO_GERAL = 1;


	private static Set<ArestaConceptNet> eliminarTranslations(Set<ArestaConceptNet> edges){
		Set<ArestaConceptNet> arestasSemTranslatOf = new HashSet<ArestaConceptNet>();

		for (ArestaConceptNet aresta : edges) {
			if(!aresta.getRel().equals("/r/TranslationOf")){
				arestasSemTranslatOf.add(aresta);
			}
		}
		//TODO imprime vetor final
		System.out.println("Apos retirar TranslationOf...");
		contaImprimeVetor(edges.size(), arestasSemTranslatOf.size());
		//
		return arestasSemTranslatOf;
	}

	
	private static void carregaInformacoesWordNet(){
		try {
			JWNL.initialize(new FileInputStream("src/config/wordnet/database_properties.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean verificaExistenciaMorfologica(POS pos, String termo){
		IndexWordSet indexWordSet = null;
		try {
			indexWordSet = DatabaseBackedDictionary.getInstance().lookupAllIndexWords(termo);
			IndexWord[] indWord = indexWordSet.getIndexWordArray();
			if(indWord.length == ZERO){
				return false;
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return indexWordSet.isValidPOS(pos);
	}
	
	private static void agregaSinonimos(Nodo no){
		String termo = no.getConceito();
		IndexWordSet indexWordSet = null;
		try {
			
			indexWordSet = DatabaseBackedDictionary.getInstance().lookupAllIndexWords(termo);
			IndexWord idWord = indexWordSet.getIndexWord(POS.NOUN);

			Synset synset = idWord.getSense(POSICAO_GERAL);
			for (Word word : synset.getWords()) {
				no.getSinonimos().add(word.getLemma());
			}
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	private static void criaHierarquia(Nodo no){
		
		IndexWordSet indexWordSet = null;
		
		try {
			indexWordSet = DatabaseBackedDictionary.getInstance().lookupAllIndexWords(no.getConceito());
			IndexWord idWord = indexWordSet.getIndexWord(POS.NOUN);

			if(idWord.getSenseCount() > ZERO){
				
				Synset syn = idWord.getSense(POSICAO_GERAL);
				Pointer[] pontos = syn.getPointers(PointerType.HYPERNYM);
				Nodo noDaVez = no;
				while(pontos.length > ZERO){
					Pointer ponto = pontos[ZERO];
					Word words[] = ponto.getTargetSynset().getWords();
					Nodo noPai = criaNo(words[ZERO].getLemma());
					for (int i = 1; i < words.length; i++) {
						noPai.getSinonimos().add(words[i].getLemma());
					}
					noPai.setFrase(ponto.getTargetSynset().getGloss());
					noDaVez.getPai().add(noPai);
					noPai.getFilho().add(noDaVez);
							
					pontos = ponto.getTargetSynset().getPointers(PointerType.HYPERNYM);
					noDaVez = noPai;
				}
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		imprimeGalho(no);
		
	}
	
	private static void imprimeGalho(Nodo no){
		
		Nodo noDaVez = no;
		String ESPACO ="";
		while(noDaVez != null){
			String impressao = ESPACO+noDaVez.getConceito()+" ";
			String sinonimos = "{";
			if(!noDaVez.getSinonimos().isEmpty()){
				
				for (String str : noDaVez.getSinonimos()) {
					sinonimos+=str+",";
				}
				sinonimos=sinonimos.substring(0, sinonimos.length()-1);
				
			}
			sinonimos+="}";
			impressao+=sinonimos;
			System.out.println(impressao);
			
			ESPACO+=LerJson.ESPACO;
			noDaVez = ((noDaVez.getPai() == null) || (noDaVez.getPai().size() == ZERO))?null:noDaVez.getPai().get(ZERO);
		}
	}
	
	private static void mesclaHierarquia(){
		
	}
	
	
	private static List<Set<ArestaConceptNet>> eliminarConceitosFracamenteRelacionados(Set<ArestaConceptNet> edges, String termo){

		Set<ArestaConceptNet> filhos = new HashSet<ArestaConceptNet>();
		Set<ArestaConceptNet> pais = new HashSet<ArestaConceptNet>();
		List<Set<ArestaConceptNet>>  paisEfilhos = new ArrayList<Set<ArestaConceptNet>>(); 
		
		for (ArestaConceptNet conceito : edges) {

			String conceitoStart = conceito.getStartLemmas().trim();
			String conceitoEnd = conceito.getEndLemmas().trim();
			
			if(verificaPaternidade(conceitoStart, termo)){
				pais.add(conceito);
			}else if(verificaPaternidade(conceitoEnd, termo)){
				filhos.add(conceito);
			}
		}

		paisEfilhos.add(pais);
		paisEfilhos.add(filhos);
		//TODO imprime vetor final
		contaImprimeVetor(edges.size(), pais.size() + filhos.size());
		//
		System.out.println("Pais e filhos...");
		System.out.println("Pais: "+pais.size());
		for (ArestaConceptNet aresta : pais) {
			System.out.println(aresta.getUri());
		}
		
		System.out.println("Filhos: "+filhos.size());
		for (ArestaConceptNet aresta : filhos) {
			System.out.println(aresta.getUri());
		}
		//
		return paisEfilhos;
	}


	private static boolean verificaPaternidade(String termoDaVez, String termo){
		
		if(termoDaVez.startsWith(termo+ESPACO)){
			return true;
		}else if(termoDaVez.endsWith(ESPACO+termo)){
			
			int ultimaOcorrenciaEspaco = termoDaVez.lastIndexOf(ESPACO);
			String termoComposto = termoDaVez.substring(0, ultimaOcorrenciaEspaco) ;
			boolean validade = verificaExistenciaMorfologica(POS.ADJECTIVE,termoComposto);
			return validade;
			
		}else if(termoDaVez.equals(termo)){
			return true;
		}
		
		return false;
	}

	public static void contaImprimeVetor(int entrada,int saida){

		System.out.println("Entrada: "+entrada);
		System.out.println("Saida: "+saida);
		System.out.println("Filtro: "+(entrada - saida));

	}

	private static Map<String, Set<ArestaConceptNet>> agrupaTipos(Set<ArestaConceptNet> arestas){
		Map<String, Set<ArestaConceptNet>> agrupar = new HashMap<String, Set<ArestaConceptNet>>();

		for (ArestaConceptNet aresta : arestas) {
			String conceito = aresta.getStart();
			Set<ArestaConceptNet> edgeList = agrupar.get(conceito);
			if(edgeList == null){
				edgeList = new HashSet<ArestaConceptNet>();
				agrupar.put(conceito,edgeList);
			}
			edgeList.add(aresta);
		}

		return agrupar;
	}


	private static void parentesco(Map<String, Set<ArestaConceptNet>> agrupa){


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
							noAtual = criaNo(atual);
						}

						if((noDaVez == null)){
							noDaVez = criaNo(daVez);
						}

						controle.put(atual, noAtual);
						controle.put(daVez, noDaVez);

						Aresta arestaAtual = verificaExistenciaDeRelacao(atual, daVez);
						Aresta arestaDaVez = verificaExistenciaDeRelacao(daVez, atual);

						if(arestaAtual.getNumFound() > arestaDaVez.getNumFound()){
							noAtual.getFilho().add(noDaVez);
							noDaVez.getPai().add(noAtual);

						}else if((arestaAtual.getNumFound() < arestaDaVez.getNumFound())){
							noDaVez.getFilho().add(noAtual);
							noAtual.getPai().add(noDaVez);
						}
					}

					/*
					 * Abordagem agressiva...
					 * 
					 * List<RelacoesValoradas> relacoesValoradas = relacoesHierarquicas(atual);

						for (RelacoesValoradas relacao : relacoesValoradas) {
							//TODO.. Ajustar aqui...
							Aresta aresta = lerJon(searchText+relacao.getRelacao(),"&",atual);
							for (ArestaConceptNet conceito : aresta.getEdges()) {

								String inicio = conceito.getStart();
								String fim = conceito.getEnd();


							}

						}*/
				}

			}
		}

	}


	private static Nodo criaNo(String conceito){
 
		Nodo no = new Nodo(conceito);
		no.setFilho(new ArrayList<Nodo>());
		no.setPai(new ArrayList<Nodo>());
		no.setSinonimos(new ArrayList<String>());
		return no;
	}

	private static Aresta verificaExistenciaDeRelacao(String conceitoInicio, String conceitoFim){

		String finall = "http://conceptnet5.media.mit.edu/data/5.1/search?start="+conceitoInicio+"&end="+conceitoFim+"&limit=0";

		Aresta aresta = prepara(finall,"");

		return aresta;
	}

	private static List<RelacoesValoradas> relacoesHierarquicas(String conceito){

		String padrao = "http://conceptnet5.media.mit.edu/data/5.1/search?text="+conceito+"&rel=/r/";
		List<RelacoesValoradas> relacoesValoradas = new ArrayList<RelacoesValoradas>();
		String relacoesHierarquicas[] = new String[]{
				Constantes.RELACAO_IS_A, 
				Constantes.RELACAO_PART_OF, 
				Constantes.RELACAO_DERIVEDED_FROM, 
				Constantes.RELACAO_INSTANCE_OF, 
				Constantes.RELACAO_MEMBER_OF};


		for (String relacao : relacoesHierarquicas) {
			String submiss = padrao+relacao;
			Aresta aresta = prepara(submiss,"&limit=0");			
			if(aresta.getNumFound() > ZERO){
				relacoesValoradas.add(new RelacoesValoradas(relacao, aresta.getNumFound()));
			}
		}

		Collections.sort(relacoesValoradas);

		return relacoesValoradas;
	}




	private static Aresta lerJon(String fonte, String caracter, String conceito){

		String finall = conceito+caracter+"limit=0";

		Aresta aresta = prepara(fonte, finall);

		if(aresta.getNumFound() > JANELA){
			aresta =  prepara(fonte, conceito+caracter+"limit="+JANELA);
			aresta.setPossuePoll(true);
			aresta.setPercorridos(JANELA);
		}
		aresta = prepara(fonte, conceito+caracter+"limit="+aresta.getNumFound());

		return aresta;
	}	


	private static Aresta prepara(String fonte, String termo){

		Gson gson = new Gson();
		Aresta aresta = new Aresta();

		try {

			URL url = new URL(fonte+termo);
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
		Aresta aresta = lerJon(fonte, caracter, conceito);

		Set<ArestaConceptNet> arestasSemTranslatOf = eliminarTranslations(aresta.getEdges());

		List<Set<ArestaConceptNet>> paisEFilhos = eliminarConceitosFracamenteRelacionados(arestasSemTranslatOf, conceito);

		Set<ArestaConceptNet> pais = paisEFilhos.get(PAIS);

		Map<String, Set<ArestaConceptNet>> agrupa = agrupaTipos(pais);
		imprimeGrupos(agrupa);
		//parentesco(agrupa);



	}

	//OK
	private static void imprimeGrupos(Map<String, Set<ArestaConceptNet>> agrupa){
		for (String str : agrupa.keySet()) {
			String st = str+"{";
			for (ArestaConceptNet ar : agrupa.get(str)) {
				st+=ar.getEndLemmas()+",";
			}
			st = st.substring(0, st.length()-1);
			st +="}";
			System.out.println(st);

		}
	}


	private static void verificaElementosExclusivos(Set<ArestaConceptNet> atual, Set<ArestaConceptNet> comparada){

		Set<ArestaConceptNet> elementosIguais = new HashSet<ArestaConceptNet>();

		Iterator<ArestaConceptNet> elemento = atual.iterator();

		while(elemento.hasNext()){
			ArestaConceptNet arestaAtual = elemento.next();
			Iterator<ArestaConceptNet> comparado = comparada.iterator();

			while(comparado.hasNext()){
				ArestaConceptNet arestaComparada = comparado.next();
				if(arestaAtual.equals(arestaComparada)){
					elementosIguais.add(arestaAtual);
					break;
				}
			}
		}
		atual.removeAll(elementosIguais);
		System.out.println("QtIguais:"+elementosIguais.size());
		System.out.println("QtExclusivos:"+atual.size());
		//		for (ArestaConceptNet aresta : atual) {
		//			System.out.println(aresta.getUri());
		//		}

	}



	public static void main(String[] args) {


		carregaInformacoesWordNet();
		
		
		Nodo no = criaNo("product");
		agregaSinonimos(no);
		
		System.out.println(no.getSinonimos());
		criaHierarquia(no);

		
		//String conceito = "jaguar";

		//String searchText = "http://conceptnet5.media.mit.edu/data/5.1/search?text=";
		//String searchSurface = "http://conceptnet5.media.mit.edu/data/5.1/search?surfaceText=";

		//String lookUp = "http://conceptnet5.media.mit.edu/data/5.1/c/en/";


		//montaAncestrais(searchText,"&",conceito);
		
		
		

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

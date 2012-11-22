package riso.builder.conceptNet5.URI.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jena.HierarchyBuilder;
import jena.VetorTematico;
import riso.db.RisoDAO;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ConceitoExpandido {

	private final int PAIS = 0;
	private final String BASE ="http://lsi.dsc.ufcg.edu.br/riso.owl#";

	private String conceito;
	private OntModel baseMinimal =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private OntModel baseVetorTematico =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private OntModel modeloMinimal =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private RisoDAO risoDAO = new RisoDAO();

	public RisoDAO getRisoDAO() {
		return risoDAO;
	}

	public void setRisoDAO(RisoDAO risoDAO) {
		this.risoDAO = risoDAO;
	}

	private List<VetorTematico> vetoresTematicos = new ArrayList<VetorTematico>();

	public List<VetorTematico> getVetoresTematicos() {
		return vetoresTematicos;
	}

	public void setVetoresTematicos(List<VetorTematico> vetoresTematicos) {
		this.vetoresTematicos = vetoresTematicos;
	}

	public OntModel getBaseMinimal() {
		return baseMinimal;
	}

	public void setBaseMinimal(OntModel baseMinimal) {
		this.baseMinimal = baseMinimal;
	}

	public OntModel getBaseVetorTematico() {
		return baseVetorTematico;
	}

	public void setBaseVetorTematico(OntModel baseVetorTematico) {
		this.baseVetorTematico = baseVetorTematico;
	}

	//OK
	public void enriqueceConceito(String fonte, String caracter, String conceito){

		setConceito(conceito);
		FiltroDeResultados filtro = new FiltroDeResultados();
		List<Set<ArestaConceptNet>> paisEFilhos = filtro.eliminarConceitosFracamenteRelacionados(fonte, caracter,conceito);
		Set<ArestaConceptNet> pais = paisEFilhos.get(PAIS);
		Set<ArestaConceptNet> afirmacoes = filtro.getAfirmacoes(pais);
		parentescoPorFilho(afirmacoes, conceito);

	}

	public String getConceito() {
		return conceito;
	}

	public void setConceito(String conceito) {
		this.conceito = conceito;
	}

	private void adicionaAfirmacao(ArestaConceptNet afirmacao){

		String s = BASE+afirmacao.getStart().trim();
		String p = afirmacao.getRel().trim();
		String o = BASE+afirmacao.getEnd().trim();

		OntClass suj = getBaseMinimal().createClass(s+" "+p);
		OntClass obj = getBaseMinimal().createClass(o+" "+p);
		suj.addSuperClass(obj);

		OntClass sujeito = getBaseVetorTematico().createClass(s);
		OntClass objeto = getBaseVetorTematico().createClass(o);
		sujeito.addSuperClass(objeto);
	}


	private void criaGrafoMinimal(OntModel modeloMinimal){
		modeloMinimal.setNsPrefix("base", BASE);
		try{
			FileOutputStream outInicio = new FileOutputStream(new File("ontoInicio.txt"));
			FileOutputStream outMinimal = new FileOutputStream(new File("ontoMinimal.txt"));

			modeloMinimal.write(outInicio);
			HierarchyBuilder hierarquia = new HierarchyBuilder();
			OntModel grafoMinimo = hierarquia.getMinimalGraph(modeloMinimal);
			setModeloMinimal(grafoMinimo);
			grafoMinimo.write(outMinimal);

			getRisoDAO().criaGrafoNomeado(grafoMinimo);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void criaVetorTematico(OntModel modeloVetorTematico, String conceito){
		HierarchyBuilder hierarquia = new HierarchyBuilder();
		List<List<String>> vetorTematico = hierarquia.getTematicVectores(getBaseVetorTematico());
		List<VetorTematico> vetoresTematicos = new ArrayList<VetorTematico>();
		for (List<String> list : vetorTematico) {
			vetoresTematicos.add(new VetorTematico(list,conceito));
		}
		setVetoresTematicos(vetoresTematicos);
		getRisoDAO().salvarVetores(vetoresTematicos);

		for (VetorTematico vetor: getVetoresTematicos()) {
			System.out.println(vetor.toString());
		}
	}

	private void parentescoPorFilho(Set<ArestaConceptNet> afirmacoes, String conceito){

		for (ArestaConceptNet afirmacao : afirmacoes) {

			adicionaAfirmacao(afirmacao);
		}

		criaGrafoMinimal(getBaseMinimal());
		criaVetorTematico(getBaseVetorTematico(), conceito);
	}

	public OntModel getModeloMinimal() {
		return modeloMinimal;
	}

	public void setModeloMinimal(OntModel modeloMinimal) {
		this.modeloMinimal = modeloMinimal;
	}

	public static void main(String args[]){



		String conceito = "jaguar";
		ConceitoExpandido t = new ConceitoExpandido();
		//
		//
		//		Nodo jaguar = new Nodo("/c/en/jaguar");
		//		Nodo animal = new Nodo("/c/en/animal");
		//		Nodo mamifero = new Nodo("/c/en/mammal");
		//
		//		Set<Nodo> nodosDiretos = new HashSet<Nodo>();
		//		Map<String, Set<Nodo>> relacoesDiretas = new HashMap<String, Set<Nodo>>();
		//		nodosDiretos.add(animal);
		//		relacoesDiretas.put("IsA", nodosDiretos);
		//
		//		Estrutura estruDireta = new Estrutura();
		//		estruDireta.setRelacoes(relacoesDiretas);
		//		jaguar.setEstrutura(estruDireta);
		//
		//		Set<Nodo> nodosInversos = new HashSet<Nodo>();
		//		Map<String, Set<Nodo>> relacoesInversas = new HashMap<String, Set<Nodo>>();
		//		nodosInversos.add(jaguar);
		//		relacoesInversas.put("IsA-1", nodosInversos);
		//
		//		Estrutura estruInversa = new Estrutura();
		//		estruInversa.setRelacoes(relacoesInversas);
		//		animal.setEstrutura(estruInversa);
		//
		//
		//		t.constroiRelacoes(jaguar, mamifero);
		//		t.constroiRelacoes(mamifero,animal);
		//		t.constroiRelacoes(jaguar, mamifero);
		t.enriqueceConceito(FiltroDeResultados.SEARCH_TEXT,"&",conceito);

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

		//Caso 4 - OK -  adicionar elementos de mesma rela��o que n�o possuem 
		//rela��o hierarquica com os que j� estao adicionados. 
		//Colocar n�s semanticamente de mesmo n�vel
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

package riso.builder.topicMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jena.HierarchyBuilder;
import jena.VetorTematico;
import riso.builder.conceptNet5.URI.out.ArestaConceptNet;
import riso.builder.documents.Documento;
import riso.db.RisoDAO;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class OntoMaker {

	private OntModel baseMinimal =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private OntModel baseVetorTematico =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private OntModel modeloMinimal =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private final String BASE ="http://lsi.dsc.ufcg.edu.br/riso.owl#";
	private RisoDAO risoDAO = new RisoDAO();
	private List<VetorTematico> vetoresTematicos = new ArrayList<VetorTematico>();

	public List<VetorTematico> getVetoresTematicos() {
		return vetoresTematicos;
	}

	public void setVetoresTematicos(List<VetorTematico> vetoresTematicos) {
		this.vetoresTematicos = vetoresTematicos;
	}

	public RisoDAO getRisoDAO() {
		return risoDAO;
	}

	public void setRisoDAO(RisoDAO risoDAO) {
		this.risoDAO = risoDAO;
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

	public OntModel getModeloMinimal() {
		return modeloMinimal;
	}

	public void setModeloMinimal(OntModel modeloMinimal) {
		this.modeloMinimal = modeloMinimal;
	}

	public void adicionaAfirmacao(ArestaConceptNet afirmacao){

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

	public void criaGrafoMinimal(String conceito){
		getBaseMinimal().setNsPrefix("base", BASE);
		try{
			FileOutputStream outInicio = new FileOutputStream(new File("reuters/results/"+conceito+"_ontoInicio.txt"));
			FileOutputStream outMinimal = new FileOutputStream(new File("reuters/results/"+conceito+"_ontoMinimal.txt"));

			getBaseMinimal().write(outInicio);
			HierarchyBuilder hierarquia = new HierarchyBuilder();
			OntModel grafoMinimo = hierarquia.getMinimalGraph(getBaseMinimal());
			setModeloMinimal(grafoMinimo);
			grafoMinimo.write(outMinimal);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void criaVetorTematico(String conceito){
		HierarchyBuilder hierarquia = new HierarchyBuilder();
		List<List<String>> vetorTematico = hierarquia.getTematicVectores(getBaseVetorTematico());
		List<VetorTematico> vetoresTematicos = new ArrayList<VetorTematico>();
		for (List<String> list : vetorTematico) {
			vetoresTematicos.add(new VetorTematico(list,conceito));
		}
		setVetoresTematicos(vetoresTematicos);
		getRisoDAO().salvarVetores(vetoresTematicos);
	}

	public void indexaDocumento(Documento doc, String texto, VetorTematico vetor){

		String conceito = null;
		final RDFNode rdf = null;
		final String HAS_TEXT = "hasText";
		final String HAS_CONTEXT = "hasContext";
		OntModel indexador =  ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

		String documento = BASE+doc.getNomeArquivo().trim();
		OntClass sujDocument = indexador.createClass(documento);
		OntProperty propHasText = indexador.createOntProperty(HAS_TEXT);
		OntProperty propHasContext = indexador.createOntProperty(HAS_CONTEXT);

		OntClass conceitoTexto = getModeloMinimal().getOntClass(BASE+"/c/en/"+texto);
		//TODO verificar se de fato o vetor tematico que mais se assemelha do texto possui
		//o conceito mais proximo do conceito citado
		StmtIterator it =  getModeloMinimal().listStatements(conceitoTexto, null, rdf);

		while(it.hasNext()){
			Statement afirm = it.next();
			for (String str : vetor.getVetor()) {
				if(str.equalsIgnoreCase(afirm.getObject().toString())){
					conceito = str;	
				}
			}
		}

		sujDocument.addProperty(propHasText, texto);
		sujDocument.addProperty(propHasContext, conceito);				

		getModeloMinimal().add(indexador);

		getRisoDAO().insereGrafoNomeado(getModeloMinimal());


	}



}


/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            27-Mar-2003
 * Filename           $RCSfile: ClassHierarchy.java.html,v $
 * Revision           $Revision: 1.4 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/17 10:44:18 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

// Package
///////////////
package jena;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import riso.builder.conceptNet5.URI.Constantes;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;


public class HierarchyBuilder {


	protected OntModel model;

	private OntModel getModel() {
		return model;
	}

	private void setModel(OntModel model) {
		this.model = model;
	}

	private final String conceitos = "http://lsi.dsc.ufcg.edu.br/riso.owl#/c/en/";
	private final String relacoes = "http://lsi.dsc.ufcg.edu.br/riso.owl#";


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<List<String>> getTematicVectores(OntModel m) {
		ExtendedIterator<OntClass> i = m.listHierarchyRootClasses()
				.filterDrop( new Filter() {
					public boolean accept( Object o ) {
						return ((Resource) o).isAnon();
					}} );
		List<List<String>> vetores = new ArrayList<List<String>>();
		while (i.hasNext()) {
			List<String> vetor = new ArrayList<String>();
			setVector(vetor,(OntClass) i.next(), new ArrayList<OntClass>());
			vetores.add(vetor);
		}

		return vetores;
	}


	private void setVector(List<String> vetor, OntClass cls, List<OntClass> occurs) {

		if (cls.canAs( OntClass.class )  &&  !occurs.contains( cls )) {
			String elemento = cls.getURI();
			int posicao = elemento.lastIndexOf("/");
			elemento = elemento.substring(posicao+1, elemento.length());
			vetor.add(elemento);
			for (@SuppressWarnings("rawtypes")
			Iterator i = cls.listSubClasses( true );  i.hasNext(); ) {
				OntClass sub = (OntClass) i.next();
				occurs.add( cls );
				setVector(vetor, sub, occurs);
				occurs.remove( cls );
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public OntModel getMinimalGraph(OntModel m) {
		ExtendedIterator<OntClass> i = m.listHierarchyRootClasses()
				.filterDrop( new Filter() {
					public boolean accept( Object o ) {
						return ((Resource) o).isAnon();
					}} );
		setModel(ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM));

		while (i.hasNext()) {
			buildMinimalGraph(getModel(),null,(OntClass) i.next(), new ArrayList<OntClass>(), 0 );
		}
		getModel().setNsPrefix("coneitos", conceitos);
		getModel().setNsPrefix("rel", "http://lsi.dsc.ufcg.edu.br/riso.owl#/r/");
		return getModel();
	}

	

	private void buildMinimalGraph(OntModel model, OntClass superior, OntClass cls, List<OntClass> occurs, int depth) {
		buildGraph(model,superior,cls);
		if (cls.canAs( OntClass.class )  &&  !occurs.contains( cls )) {
			for (@SuppressWarnings("rawtypes")
			Iterator i = cls.listSubClasses( true );  i.hasNext(); ) {
				OntClass sub = (OntClass) i.next();
				occurs.add( cls );
				buildMinimalGraph(model, cls, sub, occurs, depth + 1);
				occurs.remove( cls );
			}
		}
	}


	private void buildGraph(OntModel model, OntClass superior, OntClass obj) {

		if(superior != null){
			String sup = superior.getURI();
			sup = sup.substring(0, sup.indexOf(" "));
			OntClass objeto = model.createClass(sup.trim());
			String[] partes = obj.getURI().split(" ");
			OntClass sujeito = model.createClass(partes[0].trim());
			String relacao = partes[1].trim();
			OntProperty predicado = null;
			if(verificaRelacaoTransitiva(relacao)){
				predicado = model.createTransitiveProperty(relacoes+relacao);
			}else if(verificaRelacaoSimetrica(relacoes+relacao)){
				predicado = model.createSymmetricProperty(relacoes+relacao);
			}else{
				predicado = model.createOntProperty(relacoes+relacao);
			}
			sujeito.addProperty(predicado, objeto);
		}

	}

	private boolean verificaRelacaoSimetrica(String relacao){
		for (String rel : Constantes.RELACOES_SIMETRICAS) {
			if(relacao.contains(rel)){
				return true;
			}
		}
		return false;
	}
	
	private boolean verificaRelacaoTransitiva(String relacao){
		for (String rel : Constantes.RELACOES_TRANSITIVAS) {
			if(relacao.contains(rel)){
				return true;
			}
		}
		return false;
	}
}


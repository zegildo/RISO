package riso.builder.conceptNet5.URI.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riso.builder.conceptNet5.URI.Constantes;

public class FiltroDeResultados {



	public Map<String, Set<ArestaConceptNet>> agrupaTipos(Set<ArestaConceptNet> arestas){
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

	//OK
	public void imprimeGrupos(Map<String, Set<ArestaConceptNet>> agrupa){
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

	public void contaImprimeVetor(int entrada,int saida){

		System.out.println("Entrada: "+entrada);
		System.out.println("Saida: "+saida);
		System.out.println("Filtro: "+(entrada - saida));

	}

	public List<Set<ArestaConceptNet>> eliminarConceitosFracamenteRelacionados(Set<ArestaConceptNet> edges, String termo){

		Set<ArestaConceptNet> filhos = new HashSet<ArestaConceptNet>();
		Set<ArestaConceptNet> pais = new HashSet<ArestaConceptNet>();
		List<Set<ArestaConceptNet>>  paisEfilhos = new ArrayList<Set<ArestaConceptNet>>(); 
		UsuarioWordNet usuarioWordnet = UsuarioWordNet.getInstance();

		final String RELACAO_TRL_OF = "/"+Constantes.RELACOES+"/"+Constantes.RELACAO_TRANSLATION_OF;

		for (ArestaConceptNet conceito : edges) {
			
			if(!conceito.getRel().equals(RELACAO_TRL_OF)){
				
				String conceitoStart = conceito.getStartLemmas().trim();
				String conceitoEnd = conceito.getEndLemmas().trim();

				if(usuarioWordnet.verificaPaternidade(conceitoStart, termo)){
					pais.add(conceito);
				}else if(usuarioWordnet.verificaPaternidade(conceitoEnd, termo)){
					filhos.add(conceito);
				}
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



	public void verificaElementosExclusivos(Set<ArestaConceptNet> atual, Set<ArestaConceptNet> comparada){

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
}

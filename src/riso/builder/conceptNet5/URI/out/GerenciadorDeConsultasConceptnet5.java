package riso.builder.conceptNet5.URI.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import riso.builder.conceptNet5.URI.Constantes;

import com.google.gson.Gson;

public class GerenciadorDeConsultasConceptnet5 {

	public static final int JANELA = 5000;


	public Aresta consultaConceptnet5(String request){

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

	public Aresta gerenciaSolicitacoes(String fonte, String caracter, String conceito){

		String uriFinal = fonte+conceito+caracter+Constantes.LIMIT;
		Aresta aresta = consultaConceptnet5(uriFinal+Constantes.ZERO);
		Aresta arestaAux = null;
		if(aresta.getNumFound() > JANELA){
			int qtIt = aresta.getNumFound() / JANELA;
			int rest = aresta.getNumFound() % JANELA;
			int i = 1;
			aresta = consultaConceptnet5(uriFinal+JANELA);
			while(i < qtIt){
				arestaAux = consultaConceptnet5(uriFinal+JANELA+Constantes.OFFSET+i*JANELA);
				aresta.addSetEdge(arestaAux.getEdges());
				i++;
			}
			arestaAux = consultaConceptnet5(uriFinal+rest+Constantes.OFFSET+i*JANELA);
			aresta.addSetEdge(arestaAux.getEdges());
		}else{
			aresta = consultaConceptnet5(uriFinal+aresta.getNumFound());
		}
		return aresta;
	}
	
	public Aresta gerenciaSolicitacoesOnlyNumFound(String fonte, String caracter, String conceito){
		String uriFinal = fonte+conceito+caracter+Constantes.LIMIT;
		return consultaConceptnet5(uriFinal+Constantes.ZERO);
	}

}

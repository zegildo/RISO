package riso;

import java.util.Iterator;
import java.util.List;

import riso.builder.conceptNet5.URI.out.ConceitoExpandido;
import riso.builder.conceptNet5.URI.out.FiltroDeResultados;
import riso.builder.documents.Biblioteca;
import riso.builder.documents.Documento;
import riso.db.biblioteca.BibliotecaDAO;

public class RISO {
	
	public static void main(String args[]){
		
		ConceitoExpandido conceitoExp = new ConceitoExpandido();
		Biblioteca biblioteca = new Biblioteca();
		List<Documento> documentos = biblioteca.constroiDocumentos();
		
		BibliotecaDAO bibDAO = new BibliotecaDAO(documentos);
		bibDAO.salvarDocumentos();
		
		Iterator<Documento> it = documentos.iterator();
		
		while(it.hasNext()){
			
			Documento doc = it.next();
			String palavrasMarcadas = doc.getPalavrasMarcadas();
			String conceitos[] = palavrasMarcadas.split(",");
			
			for (String conceito : conceitos) {
				//verificar se a palavra ja foi enriquecida...
				//caso contrario...
				conceitoExp.enriqueceConceito(FiltroDeResultados.SEARCH_TEXT,"&",conceito);
				
				//comparar paragrafo do conceito com vetores tematicos do conceito
				//indexar documento com o conceito
			}	
			
		}
		
	}

}

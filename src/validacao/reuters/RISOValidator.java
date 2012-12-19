package validacao.reuters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import jena.VetorTematico;
import riso.builder.conceptNet5.URI.out.ConceitoExpandido;
import riso.builder.conceptNet5.URI.out.FiltroDeResultados;
import riso.db.vetoresTematicos.VetorTematicoDAO;

public class RISOValidator {


	public static void main(String args[]) throws IOException{

		LeitorArquivo leitor = new LeitorArquivo("reuters/all-people-strings.lc.txt");
		leitor.geraPalavrasArquivo();
		List<String> conceitos = leitor.getPalavras();
		VetorTematicoDAO vetorDAO = new VetorTematicoDAO();
		FileOutputStream file = new FileOutputStream(new File("reuters/Reuters.txt"));  
		int identificados = 0;
		int qtdConceitos = conceitos.size();
		for (int i = 0; i < qtdConceitos; i++) {
			String con = conceitos.get(i);
			System.out.print((i+1)+")"+con+": ");
			String conceito = con.replaceAll("-", " ");
			conceito = URLEncoder.encode(conceito,"UTF-8");
			ConceitoExpandido conceitoExp = new ConceitoExpandido(conceito);
			conceitoExp.enriqueceConceito(FiltroDeResultados.SEARCH_TEXT,"&");
			List<VetorTematico> vetores = vetorDAO.obtemVetoresTematicos(conceito);
			if(vetores.size()>0){
				identificados++;
			}
			String fin = con+":"+vetores.toString()+"\n";
	        file.write(fin.getBytes());
		}
		file.close();
		System.out.println("Qtde nomes lidos: "+qtdConceitos);
		System.out.println("Qtde nomes indentificados: "+identificados);
		System.out.println("Porcetagem: "+((identificados/qtdConceitos)*100)+"%");
	}
}

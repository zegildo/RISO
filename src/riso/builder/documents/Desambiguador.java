package riso.builder.documents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jena.VetorTematico;


public class Desambiguador {

	private Documento documento;
	private List<VetorTematico> vetorTematico = new ArrayList<VetorTematico>();


	public Desambiguador(Documento documento, List<VetorTematico> vetoresTematicos){
		setDocumento(documento);
		setVetorTematico(vetoresTematicos);
	}

	public void calculaVetorTermos(String conceitoDaVez){

		int mach = 0;

		String palavrasDocumento[] = getDocumento().getVetorParagrafo().split(",");

		for (VetorTematico vetor : getVetorTematico()) {

			List<String> vetorEnriquecido = vetor.getVetor();

			for (String elemento: vetorEnriquecido) {
				
				for (String palavra : palavrasDocumento) {
										
					if(!palavra.equalsIgnoreCase(conceitoDaVez) && (elemento.contains(palavra)||(palavra.contains(elemento)))){
						mach++;
					}
				}
			}
			vetor.setDesambiguador(mach);
			mach = 0;
		}
		Collections.sort(getVetorTematico());
		setVetorTematico(getVetorTematico());

	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public List<VetorTematico> getVetorTematico() {
		return vetorTematico;
	}

	public void setVetorTematico(List<VetorTematico> vetorTematico) {
		this.vetorTematico = vetorTematico;

		for (VetorTematico vt : vetorTematico) {

			System.out.println(vt.getDesambiguador()+":"+vt.getVetor());

		}
	}

}

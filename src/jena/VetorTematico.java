package jena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VetorTematico {

	private Set<String> vetor = new HashSet<String>();
	private String conceito;

	public VetorTematico(List<String> vetor, String conceito) {
		setVetor(vetor);
		setConceito(conceito);
	}

	public VetorTematico(String vetor, String conceito) {
		vetor = vetor.replace("[", "");
		vetor = vetor.replace("]", "");
		String strings[] = vetor.split(",");
		List<String> vector = new ArrayList<String>();

		for (String string : strings) {
			vector.add(string);
		}

		setVetor(vector);
	}

	public String getConceito() {
		return conceito;
	}

	public void setConceito(String conceito) {
		this.conceito = conceito;
	}

	public Set<String> getVetor() {
		return vetor;
	}

	public void setVetor(List<String> vetor) {
		this.vetor = new HashSet<String>(vetor);
	}

	public String toString(){

		String geral = "[";

		for (String str : getVetor()) {
			geral+=str+" ,";
		}
		geral = geral.substring(0, geral.length()-1)+"]";
		return geral;
	}

	public static void main(String args[]){

		String vetor = "[eastern_texas ,jaguar ,Marvel_Comics ,band ,Archie_Comics ,novel ,Insurgent_Comix ,a_large_spotted_feline_of_tropical_America_similar_to_the_leopard ,film ]";
		vetor = vetor.replace("[", "");
		vetor = vetor.replace("]", "");

		String strings[] = vetor.split(",");
	

		for (String string : strings) {
			System.out.println(string);		
		}


	}
}

package jena;

import java.util.ArrayList;
import java.util.List;


public class VetorTematico implements Comparable<VetorTematico> {

	private List<String> vetor;
	private String conceito;
	private double desambiguador;

	public VetorTematico(List<String> vetor, String conceito) {
		setVetor(vetor);
		setConceito(conceito);
	}

	public double getDesambiguador() {
		return desambiguador;
	}

	public void setDesambiguador(double desambiguador) {
		this.desambiguador = desambiguador;
	}

	public void setVetor(List<String> vetor) {
		this.vetor = vetor;
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
		setConceito(conceito);
	}

	public String getConceito() {
		return conceito;
	}

	public void setConceito(String conceito) {
		this.conceito = conceito;
	}

	public List<String> getVetor() {
		return vetor;
	}

	public String toString(){

		String geral = "[";

		for (String str : getVetor()) {
			geral+=str+" ,";
		}
		geral = geral.substring(0, geral.length()-1)+"]";
		return geral;
	}
	
	public int compareTo(VetorTematico o) {
		
		if(o.getDesambiguador() == getDesambiguador()){
			return 0;
		}else if(o.getDesambiguador() > getDesambiguador()){
			return 1;
		}
		return -1;
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

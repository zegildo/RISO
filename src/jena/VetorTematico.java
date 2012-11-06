package jena;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VetorTematico {

	private Set<String> vetor = new HashSet<String>();
	
	public VetorTematico(List<String> vetor) {
		setVetor(vetor);
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
}

package riso.builder.conceptNet5.URI.in.actions.association;

import java.util.List;

import riso.builder.conceptNet5.URI.in.Constantes;
import riso.builder.conceptNet5.URI.in.actions.ComplementoRestricoes;
import riso.builder.conceptNet5.URI.in.interfaces.Complementavel;

public class ComponenteAssociationList extends Association implements Complementavel {

	
	/**
	 * Lista de componentes cada componente pode ser um termo ou uma frase nominal
	 */
	private List<ComponenteAssociacao> componentes;
		
	public ComponenteAssociationList(List<ComponenteAssociacao> componentes, ComplementoRestricoes restricoes){
		setComponentes(componentes);
		super.setRestricoes(restricoes);
	}
	
	public ComponenteAssociationList(List<ComponenteAssociacao> componentes){
		setComponentes(componentes);
	}

	public List<ComponenteAssociacao> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<ComponenteAssociacao> componentes) {
		this.componentes = componentes;
	}


	public String getComplemento() {
		String list = Constantes.BARRA+Constantes.ASSOC_LIST+Constantes.BARRA+getIdioma().getString();
		for (ComponenteAssociacao componente : getComponentes()) {
			list+= componente.toString()+",";
		}
		list = list.substring(0, list.length()-1);
		
		if(super.getRestricoes()!= null){
			list+=super.getRestricoes().getComplemento();
		}
		return list;
	}
	
	
	
}

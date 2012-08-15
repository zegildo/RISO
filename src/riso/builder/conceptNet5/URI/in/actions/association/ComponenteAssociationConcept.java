package riso.builder.conceptNet5.URI.in.actions.association;

import riso.builder.conceptNet5.URI.in.URIGeral;
import riso.builder.conceptNet5.URI.in.actions.ComplementoRestricoes;
import riso.builder.conceptNet5.URI.in.interfaces.Complementavel;

public class ComponenteAssociationConcept extends Association implements Complementavel {

	
	private URIGeral uri;
	
	public ComponenteAssociationConcept(URIGeral uri, ComplementoRestricoes restricoes){
		setUri(uri);
		super.setRestricoes(restricoes);
	}
	public ComponenteAssociationConcept(URIGeral uri){
		setUri(uri);
	}
	
	public URIGeral getUri() {
		return uri;
	}

	public void setUri(URIGeral uri) {
		this.uri = uri;
	}

	public String getComplemento() {
		String compl = getUri().getTipoEComplemento()+getIdioma().getString();
		if(getRestricoes() == null){
			return compl;
		}
		return compl + getRestricoes().getComplemento();
	}

	
}

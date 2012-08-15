package riso.builder.conceptNet5.URI.in.source;

import riso.builder.conceptNet5.URI.in.URIGeral;
import riso.builder.conceptNet5.URI.in.types.ObjetosURIConceptNet;

public class URISource extends URIGeral{

	private ComplementoSourceConceptNet compl;
	
	public URISource(ComplementoSourceConceptNet complemento){
		super(ObjetosURIConceptNet.FONTE, complemento.getComplemento());
		
	}

	public ComplementoSourceConceptNet getCompl() {
		return compl;
	}

	public void setComplemento(ComplementoSourceConceptNet compl) {
		this.compl = compl;
	}	
	
}

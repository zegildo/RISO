package riso.builder.conceptNet5.URI.out;

import java.util.HashSet;
import java.util.Set;


public class Aresta {

	private int numFound;
	private Set<ArestaConceptNet> edges = new HashSet<ArestaConceptNet>();
	private double maxScore;
	private int percorridos;
	private boolean possuePoll;
	
	public int getPercorridos() {
		return percorridos;
	}
	
	public void setPercorridos(int percorridos) {
		this.percorridos = getPercorridos() + percorridos;
	}
	
	public boolean isPossuePoll() {
		
		if((getNumFound() - getPercorridos()) >0){
			return true;
		}
		return false;
	}
	
	public void setPossuePoll(boolean possuePoll) {
		this.possuePoll = possuePoll;
	}
	public double getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public Set<ArestaConceptNet> getEdges() {
		return edges;
	}
	public void setEdges(Set<ArestaConceptNet> edges) {
		this.edges = edges;
	}
	@Override
	public String toString() {
		return "Aresta [numFound=" + numFound + ", edges=" + edges + "]";
	}
	
}

package riso.builder.conceptNet5.URI.out;

import java.util.ArrayList;
import java.util.List;


public class Aresta {

	private int numFound;
	private List<ArestaConceptNet> edges = new ArrayList<ArestaConceptNet>();
	private double maxScore;
	
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
	public List<ArestaConceptNet> getEdges() {
		return edges;
	}
	public void setEdges(List<ArestaConceptNet> edges) {
		this.edges = edges;
	}
	@Override
	public String toString() {
		return "Aresta [numFound=" + numFound + ", edges=" + edges + "]";
	}
	
}

package net.iubris.facri.grapher.analyzer.graphstream;

import java.io.IOException;
import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public interface GraphstreamAnalyzer {
	
	/*
	 * Numerical zone
	 */	
	public void numericalAnalysis(String weightAttributeName, boolean isDirectedGraph) throws IOException;
	
	public void clusteringCoefficient() throws IOException;
	
	public void degree() throws IOException;
	
	public void density();
	
	public void diameter(String weightAttributeName, boolean directed);
	
	public void centralities() throws IOException;
	
	public void degreeMeasure();
	
	public void vertexConnectivity();
	public void edgeConnectivity();
	public void connectivities();
	
	void distance();
	
	
//	public void MaximumFlow();
	
//	public void MST() {
////		Prim p = new Prim();
//		new Kruskal();
//		new TarjanStronglyConnectedComponents();
//	}
	
	/*
	 * Graphical zone
	 */
	public void graphicalAnalysis();
	
	public ArrayList<Node> cliques();
	
	public void connectedComponents();
	
	public void strongConnectedComponents();
	
	public void epidemicCommunity();
	
	public Node getFirstNodeWithMaximumDegreeExceptEgo();
	
// 
	
	public static interface AnalysisTypeShortcut {
		public String getHelpMessage();
		public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException;
		
		public enum Numerical implements AnalysisTypeShortcut {
			cc {
				@Override
				public String getHelpMessage() {
					return "clustering coefficients";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.clusteringCoefficient();
				}
			},
			dg {
				@Override
				public String getHelpMessage() {
					return "degree";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.degree();
				}
			},
			ds {
				@Override
				public String getHelpMessage() {
					return "density";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.density();
				}
			},
			d {
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.distance();					
				}
				@Override
				public String getHelpMessage() {
					return "distance";
				}				
			},
			di {
				@Override
				public String getHelpMessage() {
					return "diameter";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					String weightAttributeName = (String) params[0];
					boolean isDirectedGraph = (boolean) params[1];
					graphstreamAnalyzer.diameter(weightAttributeName, isDirectedGraph);
				}
			},
			c {
				@Override
				public String getHelpMessage() {
					return "centrality";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.centralities();
				}
			},
			e {
				@Override
				public String getHelpMessage() {
					return "edge connectivity";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.edgeConnectivity();
				}
			},
			a {
				@Override
				public String getHelpMessage() {
					return "all numerical measures";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					String weightAttributeName = (String) params[0];
					boolean isDirectedGraph = (boolean) params[1];
					graphstreamAnalyzer.numericalAnalysis(weightAttributeName, isDirectedGraph);
				}
			};
			public abstract String getHelpMessage();
		}
		public enum Graphical implements AnalysisTypeShortcut {
			cl {
				@Override
				public String getHelpMessage() {
					return "cliques";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.cliques();
				}
			},
			gcc {
				@Override
				public String getHelpMessage() {
					return "giant connected components";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.connectedComponents();
				}
			},
			scc {
				@Override
				public String getHelpMessage() {
					return "strong connected components";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.strongConnectedComponents();
				}
			};
			public abstract String getHelpMessage();
		}
	}
	
	
	public interface GraphstreamAnalyzerFactory {
		GraphstreamAnalyzer create(Graph graph, Node node);
	}
}

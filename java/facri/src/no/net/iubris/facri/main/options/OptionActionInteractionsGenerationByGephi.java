/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (OptionActionInteractionsGenerationByGephi.java) is part of facri.
 * 
 *     OptionActionInteractionsGenerationByGephi.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     OptionActionInteractionsGenerationByGephi.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.main.options;

import javax.inject.Inject;

import net.iubris.facri.graph.exporter.gephi.GephiGraphExporter;
import net.iubris.facri.graph.generator.gephi.GephiInteractionsGraphGenerator;


public class OptionActionInteractionsGenerationByGephi implements OptionAction {

	private final GephiGraphExporter graphExporter;
	private GephiInteractionsGraphGenerator interactionsGraphGenerator;
	private boolean createGraphmlFile;
	
	@Inject
	public OptionActionInteractionsGenerationByGephi(GephiInteractionsGraphGenerator interactionsGraphGenerator,
			GephiGraphExporter graphExporter) {
		this.interactionsGraphGenerator = interactionsGraphGenerator;
		this.graphExporter = graphExporter;
	}
	@Override
	public void execute() {
			System.out.println("");
			
			System.out.print("generating *my friends* interactions graph:");
			interactionsGraphGenerator.generateMyFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("interactions_graph_-_my_friends");
			System.out.println(" ok.");
			((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
			System.out.println("");
			
			System.out.print("generating *me with my friends graph*: ");
			interactionsGraphGenerator.clear();
			interactionsGraphGenerator.generateMeWithMyFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("interactions_graph_-_me_with_my_friends");
			System.out.println(" ok.");
			((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
			System.out.println("");
			
			System.out.print("generating *my friends with their friends* graph: ");
			interactionsGraphGenerator.clear();
			interactionsGraphGenerator.generateMyFriendsAndFriendOfFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("interactions_graph_-_me_with_my_friends_and_their_friends");
			System.out.println(" ok.");
			((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
			System.out.println("");
			
			System.out.print("generating *me with my friends with their friends* interactions graph: ");
			interactionsGraphGenerator.clear();
			interactionsGraphGenerator.generateMeWithMyFriendsAndTheirFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("interactions_graph_-me_with_my_friends_and_their_friends");
			System.out.println(" ok.");
			((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
			System.out.println("");

	}

}

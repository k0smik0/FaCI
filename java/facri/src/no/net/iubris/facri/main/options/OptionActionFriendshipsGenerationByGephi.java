/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (OptionActionFriendshipsGenerationByGephi.java) is part of facri.
 * 
 *     OptionActionFriendshipsGenerationByGephi.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     OptionActionFriendshipsGenerationByGephi.java is distributed in the hope that it will be useful,
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
import net.iubris.facri.graph.generator.gephi.GephiFriendshipsGraphGenerator;


public class OptionActionFriendshipsGenerationByGephi implements OptionAction {

	private final GephiFriendshipsGraphGenerator friendshipsGraphGenerator;
	private final GephiGraphExporter graphExporter;
	private boolean createGraphmlFile;
	
	@Inject
	public OptionActionFriendshipsGenerationByGephi(GephiFriendshipsGraphGenerator friendshipsGraphGenerator,
			GephiGraphExporter graphExporter) {
		this.friendshipsGraphGenerator = friendshipsGraphGenerator;
		this.graphExporter = graphExporter;
	}
	@Override
	public void execute() {
			System.out.println("");
			
			System.out.print("generating *my friends* friendness graph:");
			friendshipsGraphGenerator.generateMyFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("friendess_graph_-_my_friends");
			System.out.println(" ok.");
			friendshipsGraphGenerator.testGraph();
			System.out.println("");
			
			System.out.print("generating *me with my friends* friendness graph: ");
			friendshipsGraphGenerator.clear();
			friendshipsGraphGenerator.generateMeWithMyFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends");
			System.out.println(" ok.");
			friendshipsGraphGenerator.testGraph();
			System.out.println("");
			
			System.out.print("generating *my friends with their friends* friendness graph: ");
			friendshipsGraphGenerator.clear();
			friendshipsGraphGenerator.generateMyFriendsAndFriendOfFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends_and_their_friends");
			System.out.println(" ok.");
			friendshipsGraphGenerator.testGraph();
			System.out.println("");

			System.out.print("generating *me with my friends with their friends* friendness graph: ");
			friendshipsGraphGenerator.clear();
			friendshipsGraphGenerator.generateMeWithMyFriendsAndTheirFriends();
			if (createGraphmlFile)
				graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends_and_their_friends");
			System.out.println(" ok.");
			friendshipsGraphGenerator.testGraph();
			System.out.println("");		

	}

}

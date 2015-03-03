package net.iubris.faci.grapher.holder.core;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class ViewUtils {

	public static Viewer buildViewer(Graph graph) {
			graph.addAttribute("ui.quality");
			graph.addAttribute("ui.antialias");
	//		graph.addAttribute("ui.speed");
			
	//		System.out.println("graph: "+graph);
			Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel
					.GRAPH_IN_ANOTHER_THREAD);
	//				.GRAPH_IN_GUI_THREAD);
			viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
	//		viewer.enableAutoLayout(new LinLog(true));
	//		viewer.enableAutoLayout(new SpringBox(true));
			viewer.enableAutoLayout();
			viewer.enableXYZfeedback(true);
			
			return viewer;
		}

	public static ViewPanel buildView(Viewer viewer) {
		ViewPanel view = viewer.getDefaultView();
		if (view==null) {
			view = viewer.addDefaultView(true);
			view.setEnabled(true);
		}
		return view;
	}
	
//	public void prepareForDisplayFriendships(Viewer friendshipsGraphViewer, FriendshipsMouseManagerFactory friendshipsMouseManagerFactory) {
//		ViewPanel view = ViewUtils.buildView(friendshipsGraphViewer);
//		view.setMouseManager(friendshipsMouseManagerFactory.create(friendshipsGraphViewer));
//	}
//	
//	public void prepareForDisplayInteractions(Viewer interactionsGraphViewer, InteractionsMouseManagerFactory interactionsMouseManagerFactory) {
//		ViewPanel view = ViewUtils.buildView(interactionsGraphViewer);
//		view.setMouseManager(interactionsMouseManagerFactory.create(interactionsGraphViewer));
//	}

}

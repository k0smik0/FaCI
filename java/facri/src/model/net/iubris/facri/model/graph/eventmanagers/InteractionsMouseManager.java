package net.iubris.facri.model.graph.eventmanagers;

import java.util.concurrent.atomic.AtomicInteger;

import net.iubris.facri.model.world.World;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class InteractionsMouseManager extends MouseManager {

	@AssistedInject
	public InteractionsMouseManager(@Assisted Viewer viewer, World world) {
		super(viewer, world);
	}
	
	@Override
	protected String buildNodeInfo(Node node) {
		Holder holder = getDegreeValues(node);
		float eValue = holder.enteringInteractions.floatValue();
		float lValue = holder.leavingInteractions.floatValue();
		String profileUrl = profileUrlPrefix+node.getId();
		String nodeInfo = "in-degree: "+(node.getInDegree()*eValue)+" ("+holder.enteringInteractionsCounter.get()+"*"+eValue+")"
				+"<br/>out-degree: "+(node.getOutDegree()*lValue)+" ("+holder.leavingInteractionsCounter.get()+"*"+lValue+")"
				+"<br/>url: <a href='"+profileUrl+"'>"+profileUrl+"</a>";
		return nodeInfo;
	}
	
	private Holder getDegreeValues(Node node) {
		String nodeId = node.getId();
		Holder holder = new Holder();
		Iterable<Edge> eachEdge = node.getEachEdge();
		eachEdge.forEach(e->{
			if (e.getSourceNode()
					.getId()
					.equals(nodeId)) { // leaving
					float interactions = e.getAttribute("ui.interactions");
					holder.leavingInteractions.addAndGet(interactions);
					holder.leavingInteractionsCounter.incrementAndGet();
			} else if (e.getTargetNode()
					.getId()
					.equals(nodeId)) { // entering
					float interactions = e.getAttribute("ui.interactions");
					holder.enteringInteractions.addAndGet(interactions);
					holder.enteringInteractionsCounter.incrementAndGet();
			} 
		});
		return holder;
	}
	
	class Holder {
		AtomicDouble enteringInteractions = new AtomicDouble(0);
		AtomicInteger enteringInteractionsCounter = new AtomicInteger(0);
		AtomicDouble leavingInteractions = new AtomicDouble(0);
		AtomicInteger leavingInteractionsCounter = new AtomicInteger(0);
	}
	
	public interface InteractionsMouseManagerFactory /*extends MouseManagerFactory<InteractionsMouseManager>*/ {
		InteractionsMouseManager create(Viewer viewer);
	}

}

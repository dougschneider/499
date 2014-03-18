package graphs;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author vsutskever
 *
 */
public class Dijkstra {

	
	private Graph graph;
	//priority queue stores all of the nodes, reachable from the start node
	//The queue is sorted by the node.distance 
	private GraphNodePriorityQueue priorityQ = new GraphNodePriorityQueue();
	private Hashtable <GraphNode,Integer> distance = new Hashtable<GraphNode, Integer>();
    private HashMap<GraphNode, GraphNode> parents = new HashMap<GraphNode, GraphNode>();
	
	//1. needs to get the list of all nodes in the graph
	//2. need to initialize distance vector to infinity
	//3. Need Edge Cost function
	public Dijkstra (Graph g){
		this.graph  = g;
		this.graph.getStartNode().setDistance(0);
		this.priorityQ.add(this.graph.getAllNodes());
	}
	/**
	 * Actual algorithm
	 */
	public void go(){
		while (this.priorityQ.hasMore()){
			GraphNode n = this.priorityQ.remove();
			for (Edge e: n.getOutGoingEdges()){
				GraphNode adjNode = e.getNode();
				Integer newPossiblePathCost = e.getCost()+n.getDistance();
				System.out.println("update: " + newPossiblePathCost.toString());
				if (newPossiblePathCost<adjNode.getDistance()){
					adjNode.setDistance(newPossiblePathCost);
                    parents.put(adjNode, n);
					this.priorityQ.updateGraphNodeDistance(adjNode);
				}
			}
		}	
	}
	/**
	 * 
	 */
	public void PrintStatusOfPriorityQ(){
		this.priorityQ.PrintContents();
	}
	
	public ArrayList<GraphNode> getPath(GraphNode from, GraphNode to)
    {
        ArrayList<GraphNode> path = new ArrayList<GraphNode>();
        GraphNode current = parents.get(to);
        while(current != from)
        {
            path.add(current);
            current = parents.get(current);
        }
        path.add(current);

        Collections.reverse(path);
        
        return path;
    }
	
}

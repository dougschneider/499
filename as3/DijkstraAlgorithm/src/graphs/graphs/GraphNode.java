package graphs;

import java.util.ArrayList;

/**
 * 
 * @author vsutskever
 * 
 */
final class Edge {

	private GraphNode node;
	private Integer cost;

	public Edge(GraphNode nodeTo, Integer cost) {
		this.node = nodeTo;
		this.cost = cost;
	}
	/**
	 * 
	 * @return
	 */
	public GraphNode getNode() {
		return node;
	}
	/**
	 * 
	 * @return
	 */
	public Integer getCost() {
		return cost;
	}
}

/**
 * @author vsutskever
 */
public class GraphNode implements Comparable<GraphNode> {

	
	private static int nodeCount=0;
	
	private ArrayList<Edge> outGoingEdges = new ArrayList<Edge>();
	private String val;
	private Integer ID;
	private boolean visited;
	private Integer distance = GraphConstants.INFINITY;
	
	/**
	 * 
	 * @param value
	 */
	public GraphNode(String value) {
		this.init(value);
	}
	/**
	 * 
	 */
	public GraphNode(){
		this.init("");
	}
	
	/**
	 * Initializes 
	 * @param nodeVal
	 */
	private void init(String nodeVal){
		this.val = nodeVal;
		this.ID = GraphNode.nodeCount++;
		this.visited = false;
	}
		
	/**
	 * 
	 * @return
	 */
	public void setVisited(boolean visited) {
		
		System.out.println("Visited"+ this.val);
		this.visited = visited;
	}
	/**
	 * 
	 * @param node
	 * @param cost
	 */
	public void AddOutgoingEdge(GraphNode node,Integer cost) {
		this.outGoingEdges.add(new Edge(node,cost));
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Edge> getOutGoingEdges() {
		return outGoingEdges;
	}

	public String getVal() {
		return val;
	}
	/**
	 * 
	 * @param node
	 * @param cost
	 */
	public void setVal(String val) {
		this.val = val;
	}
	/**
	 * 
	 * @return
	 */
	public Integer getID() {
		return ID;
	}
	/**
	 * 
	 */
	@Override
	public int compareTo(GraphNode arg0) {
		return this.distance.compareTo(arg0.getDistance());
	}
	/**
	 * 
	 * @return
	 */
	public Integer getDistance() {
		return distance;
	}
	/**
	 * 
	 * @param distance
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}


}

package graphs;

import java.util.ArrayList;

/**
 * 
 * @author vsutskever
 * Collections of functinos 
 */
public class Graph {

	private GraphNode startNode;
	private ArrayList <GraphNode> allNodes = new ArrayList<GraphNode>();
	private ArrayList <Integer> visitedNodes = new ArrayList<Integer>(); 
	/**
	 * @param startNode
	 */
	public Graph(GraphNode startNode){
		this.startNode = startNode;
		this.discoverGraph();
	}
	/**
	 * @return the start node
	 */	
	public GraphNode getStartNode(){
		return this.startNode;
	}
	/**
	 * @return
	 */
	public ArrayList<GraphNode> getAllNodes(){
		return this.allNodes;
	}
				
	/**
	 * Breath First Search traversal of the Graph 
	 */
	private void discoverGraph(){
		allNodes.add(this.startNode);
		visit(startNode);
		for (Edge e : this.startNode.getOutGoingEdges()){
			if (!isVisited(e.getNode())){
				bfs(e.getNode());
			}
		}
	}
	/**
	 * @param n
	 */
	private void bfs(GraphNode n){
		visit(n);
		this.allNodes.add(n);
		for (Edge e: n.getOutGoingEdges()){
			if (!isVisited(e.getNode())){ 
				bfs(e.getNode());
			}
		}
	}
	/**
	 * @param n
	 * @return
	 */
	private boolean isVisited(GraphNode n){
		return visitedNodes.contains(n.getID());
	}
	/**
	 * @param n
	 */
	private void visit(GraphNode n){
		this.visitedNodes.add(n.getID());
	}
	/**
	 * 
	 */
	public void print(){
		for (GraphNode n: this.allNodes){
			System.out.println(n.getVal() + " - " + n.getDistance());
		}
		
	}

	
	
}

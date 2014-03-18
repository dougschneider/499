package graphs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
/**
 * 
 * @author vsutskever
 * 
 */
public class GraphNodePriorityQueue  {

	
	private PriorityQueue<GraphNode> pQueue = new PriorityQueue<GraphNode>();

	/**
	 * Constructor
	 */
	public GraphNodePriorityQueue(){
				
	}
	/**
	 * 
	 * @param n
	 */
	public void add(GraphNode n){
		pQueue.add(n);
	}
	/**
	 * 
	 * @param g
	 */
	public void add(Collection<GraphNode> nodeCollection){
		this.pQueue.addAll(nodeCollection);
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean hasMore(){
		return !this.pQueue.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public GraphNode remove(){
		return this.pQueue.remove();
	}
	
	/**	
	 * Removes desired graph node, then inserts into appropriate slot
	 * 
	 * @param distance
	 */
	public void updateGraphNodeDistance(GraphNode n){
		this.pQueue.remove(n);
		this.pQueue.add(n);
	}
	/**
	 * Print Contents
	 * 
	 */
	public void PrintContents(){
		//it appears that order is preserved when items are removed from the collections
		ArrayList<GraphNode> _temp = new ArrayList<GraphNode>();
		System.out.println("Size of Q=" + pQueue.size());
		while (!pQueue.isEmpty()){
			GraphNode n =pQueue.remove();
			_temp.add(n);
			System.out.println(n.getVal()+" distance=" + n.getDistance());
		}
		pQueue.addAll(_temp);
	}
	
}

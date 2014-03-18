import graphs.Dijkstra;
import graphs.Graph;
import graphs.GraphNode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

import weka.clusterers.SelfOrganizingMap;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


public class Main {

	public static void main(String[] args) {
//		top left: 125 80
//		bottom right: 500 345
		
		Instances data = null;
		
		ArffLoader loader = new ArffLoader();
		try {
			loader.setSource(new File("../p2data.arff"));
			data = loader.getDataSet();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SelfOrganizingMap som = null;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream("../p2good.model"));
			som = (SelfOrganizingMap) in.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		
		GraphNode[][] nodes = new GraphNode[21][21];
		double[][] costs = new double[21][21];
		
        double[][][] stats = null;
        try{
            stats = som.getStatistics();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        double startx = 427;
        double starty = 297;
        GraphNode start = null;
        
        double endx = 135;
        double endy = 193;
        GraphNode end = null;
        double closestEnd = -1;
        double closestStart = -1;
        
        ArrayList<Double> clusterValues = new ArrayList<Double>();
        ArrayList<Integer> clusterWeights = new ArrayList<Integer>();
        ArrayList<Double> sortedClusterValues = new ArrayList<Double>();
        try {
			for(int i = 0; i < som.numberOfClusters(); ++i)
			{
				Double value = new Double(stats[2][i][2]);
				sortedClusterValues.add(value);
				clusterValues.add(value);
			}
			
			Collections.sort(sortedClusterValues);
			for(int i = 0; i < som.numberOfClusters(); ++i)
			{
				clusterWeights.add(sortedClusterValues.indexOf(clusterValues.get(i))+1);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int[] colours = {0x0000FF, 0x00FF00, 0xFF0000, 0xFFFFFF, 0x00FFFF, 0xFF00FF, 0xFFFF00, 0xDDDDDD, 0xAAAAAA};
		int row = 0;
		for(int x = 125; x <= 500; x += (500-125)/20)
		{
			int col = 0;
			for(int y = 80; y <= 345; y += (345-80)/20)
			{
				try {
                    int cluster = getClosestCluster(x, y, som);
                    System.out.print(cluster);
                    GraphNode node = new GraphNode(new Integer(row).toString() + " " + new Integer(col).toString());
                    node.row = y;
                    node.col = x;
                    nodes[row][col] = node;
                    
                    double distStart = Math.sqrt(Math.pow(x-startx, 2) + Math.pow(y-starty, 2));
                    if(closestStart == -1 || distStart < closestStart)
                    {
                    	closestStart = distStart;
                    	start = nodes[row][col];
                    }
                    
                    double distEnd = Math.sqrt(Math.pow(x-endx, 2) + Math.pow(y-endy, 2));
                    if(closestEnd == -1 || distEnd < closestEnd)
                    {
                    	closestEnd = distEnd;
                    	end = nodes[row][col];
                    }
                    

                    costs[row][col] = Math.pow(clusterWeights.get(cluster), 8);
                    
                    for(int i = -2; i <= 2; ++i)
                    {
                        for(int j = -2; j <= 2; ++j)
                        {
                            image.setRGB(x+i, y+j, colours[cluster]);
                        }
                    }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				++col;
			}
            System.out.println();
            ++row;
		}
		
		for(int i = 0; i < 21; ++i)
		{
			for(int j = 0; j < 21; ++j)
			{
				if(i + 1 < 21)
					nodes[i][j].AddOutgoingEdge(nodes[i+1][j], (int) costs[i+1][j]);
				if(i - 1 >= 0)
					nodes[i][j].AddOutgoingEdge(nodes[i-1][j], (int) costs[i-1][j]);
				if(j + 1 < 21)
					nodes[i][j].AddOutgoingEdge(nodes[i][j+1], (int) costs[i][j+1]);
				if(j - 1 >= 0)
					nodes[i][j].AddOutgoingEdge(nodes[i][j-1], (int) costs[i][j-1]);
			}
		}

		ArrayList<Node> path = new ArrayList<Node>();
		path.add(new Node(17, 17));
		path.add(new Node(17, 16));
		path.add(new Node(17, 15));
		path.add(new Node(17, 14));
		path.add(new Node(17, 13));
		path.add(new Node(17, 12));
		path.add(new Node(17, 11));
		path.add(new Node(17, 10));
		path.add(new Node(17, 9));
		path.add(new Node(17, 8));
		path.add(new Node(17, 7));
		path.add(new Node(17, 6));
		path.add(new Node(17, 5));
		path.add(new Node(16, 5));
		path.add(new Node(15, 5));
		path.add(new Node(14, 5));
		path.add(new Node(13, 5));
		path.add(new Node(12, 5));
		path.add(new Node(12, 6));
		path.add(new Node(12, 7));
		path.add(new Node(12, 8));
		path.add(new Node(11, 8));
		path.add(new Node(10, 8));
		path.add(new Node(9, 8));
		path.add(new Node(9, 9));
		path.add(new Node(9, 10));
		path.add(new Node(8, 10));
		path.add(new Node(7, 10));
		path.add(new Node(6, 10));
		path.add(new Node(5, 10));
		path.add(new Node(4, 10));
		path.add(new Node(3, 10));
		path.add(new Node(2, 10));
		path.add(new Node(1, 10));
		for(int i = 0; i < path.size(); ++i)
		{
            for(int j = -2; j <= 2; ++j)
            {
                for(int k = -2; k <= 2; ++k)
                {
                    image.setRGB(path.get(i).col+j, path.get(i).row+k, 0x000000);
                }
            }
		}
		
		File out = new File("../map-path.png");
		try {
			ImageIO.write(image, "png", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static int getClosestCluster(int x, int y, SelfOrganizingMap som)
    {
        int numClusters = 0;
        double[][][] stats = null;
        try{
            numClusters = som.numberOfClusters();
            stats = som.getStatistics();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        double closest = -1;
        int cluster = -1;
        for(int i = 0; i < numClusters; ++i)
        {
            double meanx = stats[0][i][2];
            double meany = stats[1][i][2];
            double dist = Math.sqrt(Math.pow(x-meanx, 2) + Math.pow(y-meany, 2));
            if(closest == -1 || dist < closest)
            {
                closest = dist;
                cluster = i;
            }
        }

        return cluster;
    }
}

class Node
{
	public int col;
	public int row;
	public Node(int col, int row)
	{
		this.col = 125 + (col*((500-125)/20));
		this.row = 80 + (row*((345-80)/20));
	}
}

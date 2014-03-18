import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import weka.clusterers.SelfOrganizingMap;


public class Main {

	public static void main(String[] args) {
		
		// get the som model representing the topographical clusters
		SelfOrganizingMap som = null;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream("../p2.model"));
			som = (SelfOrganizingMap) in.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		
		// create a 21 by 21 set of color points to represent the clusters of the map
		// in a course way
		int[] colours = {0x0000FF, 0x00FF00, 0xFF0000, 0xFFFFFF, 0x00FFFF, 0xFF00FF, 0xFFFF00, 0xDDDDDD, 0xAAAAAA};
		for(int x = 125; x <= 500; x += (500-125)/20)
		{
			for(int y = 80; y <= 345; y += (345-80)/20)
			{
				try {
                    int cluster = getClosestCluster(x, y, som);
                    
                    // draw a block of points for the cluster
                    for(int i = -2; i <= 2; ++i)
                    {
                        for(int j = -2; j <= 2; ++j)
                        {
                            image.setRGB(x+i, y+j, colours[cluster]);
                        }
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// save the map WITHOUT the path
		File out = new File("../map.png");
		try {
			ImageIO.write(image, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// create the path by hand
		// (17, 17) is the start(A), 
		// (1, 10) is the end(B).
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
		
		// erase the points consisting of the path
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
		
		// save the map with the road filled out
		out = new File("../map-path.png");
		try {
			ImageIO.write(image, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the cluster number that is closest to the given x and y point.
	 */
    public static int getClosestCluster(int x, int y, SelfOrganizingMap som)
    {
        int numClusters = 0;
        // get the stats for the clusters
        double[][][] stats = null;
        try{
            numClusters = som.numberOfClusters();
            stats = som.getStatistics();
		} catch (Exception e) {
			e.printStackTrace();
		}

        double closest = -1;
        int cluster = -1;
        // consider each cluster
        for(int i = 0; i < numClusters; ++i)
        {
            double meanx = stats[0][i][2];
            double meany = stats[1][i][2];
            // get the distance from this point to the center
            double dist = Math.sqrt(Math.pow(x-meanx, 2) + Math.pow(y-meany, 2));
            // if it's closer, use it
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
		// convert the row and column into pixel locations
		this.col = 125 + (col*((500-125)/20));
		this.row = 80 + (row*((345-80)/20));
	}
}

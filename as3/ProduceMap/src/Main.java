import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;

import weka.clusterers.SelfOrganizingMap;
import weka.core.Instance;
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
		
		double avgElevation = 0;
		try {
			double[][][] stats = som.getStatistics();
			//for(int i = 0; i < som.numberOfClusters(); ++i)
            // attribute/cluster/row
			System.out.println(avgElevation += stats[0][0][2]);
			avgElevation/=som.numberOfClusters();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		
		int[] colours = {0x0000FF, 0x00FF00, 0xFF0000, 0xFFFFFF, 0x00FFFF, 0xFF00FF, 0xFFFF00, 0xDDDDDD, 0xAAAAAA};
		for(int x = 125; x <= 500; x += (500-125)/20)
		{
			for(int y = 80; y <= 345; y += (345-80)/20)
			{
				Instance instance = data.firstInstance();
				instance.setValue(0, x);
				instance.setValue(1, y);
				instance.setValue(2, avgElevation);
				try {
                    int cluster = getClosestCluster(x, y, som);
                    System.out.print(cluster);
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
			}
            System.out.println();
		}
		
		File out = new File("../map.png");
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

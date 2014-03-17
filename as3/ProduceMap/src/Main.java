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
			in = new ObjectInputStream(new FileInputStream("../mapcluster-before.model"));
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
			for(int i = 0; i < som.numberOfClusters(); ++i)
				avgElevation += stats[2][i][2];
			avgElevation/=som.numberOfClusters();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("../tdata.arff")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int[] colours = {0x0000FF, 0x00FF00, 0xFF0000, 0xFFFFFF};
		for(int x = 125; x <= 500; x += 5)
		{
			for(int y = 80; y <= 345; y += 5)
			{
				Instance instance = data.firstInstance();
				instance.setValue(0, x);
				instance.setValue(1, y);
				instance.setValue(2, avgElevation);
				try {
					bw.write(x + "," + y + "," + "?" + "\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					image.setRGB(x, y, colours[som.clusterInstance(instance)]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(int i = 0; i < data.numInstances(); ++i)
		{
			Instance instance = data.instance(i);
			int x = (int) instance.value(0);
			int y = (int) instance.value(1);
			try {
				image.setRGB(x, y, colours[som.clusterInstance(instance)]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File out = new File("../map.png");
		try {
			ImageIO.write(image, "png", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

import javax.swing.JFrame;
import lejos.nxt.Sound;;

public class Main {

	public static void main(String[] args) {
		RemoteControlGUI gui = new RemoteControlGUI();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// do something trivial to initialize connection
		Sound.beep();
	}
}

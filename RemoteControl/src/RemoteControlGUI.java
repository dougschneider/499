import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import lejos.nxt.MotorPort;

/*
 * GUI for remote controlling NXT from PC.
 * 
 * Inspiration from http://www.lejos.org/forum/viewtopic.php?t=1723
 */
public class RemoteControlGUI extends JFrame {
	private static final long serialVersionUID = -2904667423077230998L;
	private static JTextField input;
	private static JButton leftUp, leftDown, rightUp, rightDown;
	private KeyHandler kh;
	private ClickHandler ch;
	private static int powStep = 10;
	private int leftPow;
	private int rightPow;

	public RemoteControlGUI() {
		// init values
		leftPow = 100;
		rightPow = 100;
		
		// set title and size
		setTitle("Remote Control");
		setBounds(650, 350, 500, 500);
		setLayout(new GridLayout(3, 3));

		// init handlers
		kh = new KeyHandler();
		ch = new ClickHandler();
		
		// add buttons
		leftUp = new JButton("Left Power +");
		leftUp.addMouseListener(ch);
		add(leftUp);
		
		rightUp = new JButton("Right Power +");
		rightUp.addMouseListener(ch);
		add(rightUp);
		
		leftDown = new JButton("Left Power -");
		leftDown.addMouseListener(ch);
		add(leftDown);
		
		rightDown = new JButton("Right Power -");
		rightDown.addMouseListener(ch);
		add(rightDown);
		
		// add text input box
		input = new JTextField();
		input.addKeyListener(kh);
		add(input);
	}

	private class ClickHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// update the power based on the button clicked.
			if (e.getSource() == leftUp) {
				leftPow = Math.min(100, leftPow + powStep);
			} else if (e.getSource() == leftDown) {
				leftPow = Math.max(0, leftPow - powStep);
			} else if (e.getSource() == rightUp) {
				rightPow = Math.min(100, rightPow + powStep);
			} else if (e.getSource() == rightDown) {
				rightPow = Math.max(0, rightPow - powStep);
			}
			// print out the new power
			System.out.println("Left Pow: " + leftPow);
			System.out.println("Right Pow: " + rightPow);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// do nothing
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// do nothing
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// do nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// do nothing
		}

	}

	private class KeyHandler implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// set the power based on the key pressed
			switch (e.getKeyChar()) {
			case 'w':
				MotorPort.C.controlMotor(leftPow, MotorPort.FORWARD);
				break;
			case 's':
				MotorPort.C.controlMotor(leftPow, MotorPort.BACKWARD);
				break;
			case 'i':
				MotorPort.A.controlMotor(rightPow, MotorPort.FORWARD);
				break;
			case 'k':
				MotorPort.A.controlMotor(rightPow, MotorPort.BACKWARD);
				break;
			}
			;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// float when the key is released
			switch (e.getKeyChar()) {
			case 'w':
			case 's':
				MotorPort.C.controlMotor(leftPow, MotorPort.FLOAT);
				break;
			case 'i':
			case 'k':
				MotorPort.A.controlMotor(rightPow, MotorPort.FLOAT);
				break;
			}
			;
		}

	}
}

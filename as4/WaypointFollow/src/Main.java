import lejos.util.Delay;


public class Main {
    public static TrackerReader tracker;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        tracker = new TrackerReader();
        tracker.start();
        while (true) {
            Delay.msDelay(1000);
            System.out.println(tracker.x + " " + tracker.y);
        }
	}

}

import lejos.util.Delay;


public class Main {
    public static TrackerReader tracker;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        tracker = new TrackerReader();
        tracker.start();
        while (true) {
            Delay.msDelay(1000);
            System.out.println("Waypoints:");
            for (int i = 0; i < tracker.waypoints.size(); i++) {
            	System.out.println(tracker.waypoints.get(i)[0] + ", " + tracker.waypoints.get(i)[1]);
            }
            System.out.println("------------------------");
        }
	}
}

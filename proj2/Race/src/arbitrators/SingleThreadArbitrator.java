package arbitrators;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

public class SingleThreadArbitrator {

	private Behavior[] behaviors = null;
	private boolean returnWhenInactive = false;

	public SingleThreadArbitrator(Behavior[] behaviors) {
		this(behaviors, false);
	}

	public SingleThreadArbitrator(Behavior[] behaviors,
			boolean returnWhenInactive) {
		this.behaviors = behaviors;
		this.returnWhenInactive = returnWhenInactive;
	}

	public void start() {
		Behavior prev = null;
		Behavior curr = null;

		while (true) {
			curr = getHighestPriorityBehavior();
			if (prev != curr && prev != null) {
				prev.suppress();
			}
			if (curr != null) {
				curr.action();
			} else if (returnWhenInactive) {
				break;
			}
			prev = curr;
			// for debugging
			if (Button.RIGHT.isDown()) break;
		}
	}

	private Behavior getHighestPriorityBehavior() {
		// Loop from last to first, to maintain leJOS arbitrator interface
		// that highest priority behaviors go last on list.
		for (int i = behaviors.length - 1; i > -1; i--) {
			if (behaviors[i].takeControl()) {
				return behaviors[i];
			}
		}
		return null;
	}
}

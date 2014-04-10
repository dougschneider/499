package arbitrators;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

/**
 * Single-threaded implementation of an arbitrator. Works with existing
 * LeJOS {@link Behavior} interface.
 * 
 * LeJOS's {@link Arbitrator} class is multi-threaded and doesn't work with USB
 * communication.
 * 
 * This implementation polls each behavior in a loop, so try to keep
 * each Behavior's takeControl() method simple as complexity adds delay.
 * The Behavior's action() method should perform a single move (so in the case
 * of a PID controller a single correction). The action() loop is called
 * in a loop by the arbitrator.
 * 
 * Once the Behaviors are implemented this way, the SingleThreadArbitrator
 * works the same as LeJOS's {@link Arbitrator}.
 *
 */
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

	/**
	 * Run, calling action() on the highest-priority behavior that want to take control,
	 * and suppress() on transition to a different behavior.
	 */
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
			// try to be consistent with timing
			Delay.msDelay(50);
		}
	}

	/**
	 * Return the highest-priority behavior that wants to take control. 
	 */
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

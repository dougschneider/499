/**
 * Matched X, Y, Data triplet. X, Y and Data can have different types.
 * 
 * @author akrebs
 *
 * @param <X>
 * @param <Y>
 * @param <Data>
 */
public class Triple<X, Y, Data> {
	public final X x;
	public final Y y;
	public final Data data;

	public Triple(X x, Y y, Data data) {
		this.x = x;
		this.y = y;
		this.data = data;
	}
}
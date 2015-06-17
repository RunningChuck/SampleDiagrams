package data;

/**
 * Represents a scale in original data. Used for determining data comparability.
 * @author clemens
 *
 */
public class Scale {
	private String name;
	private int from;
	private int to;

	public Scale(String name, int[] fromto) {
		this(name, fromto[0], fromto[1]);
	}
	public Scale(String name, int from, int to) {
		this.name = name;
		if (from < to) {
			this.from = from;
			this.to = to;
		} else {
			this.from = to;
			this.to = from;
		}
	}
	
	public String getName() {
		return name;
	}

	public int from() {
		return from;
	}
	
	/**
	 * @return absolute number of values covered
	 */
	public int size() {
		return to-from+1;
	}
	
	public int to() {
		return from;
	}

	public String toString() {
		return name + " [" + from + ".." + to + "]";
	}
	
	/**
	 * Returns true if value is covered within scale
	 * @param value
	 * @return
	 */
	public boolean includes(int value) {
		if (value > to || value < from)
			return false;
		else
			return true;
	}
}

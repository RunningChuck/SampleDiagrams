package data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Transports all the data in a neat way --> chunks
 * For each value in scale, a count is given, i.e calculated.
 * 
 * For composite vars, ValueChunkContainers are to be used.
 * 
 * @author clemens
 */
public class DataChunk {
	private String name;
	protected int[] valueCounts;
	private Scale scale;
	
	/**
	 * Stores value counts
	 * 
	 */
	public DataChunk(String name, Scale scale) {
		this(name, scale, null);
	}
	
	public DataChunk(String name, Scale scale, ArrayList<String> data) {
		this.name = name;
		this.scale = scale;
		
		valueCounts = new int[scale.size()];
		for (int i = 0; i < valueCounts.length; i++) {
			valueCounts[i] = 0;
		}
		
		if (data!=null)
			setData(data);
	}

	/**
	 * Increments count for specific data value by one
	 * @param value Specifies the raw data value, which is to be incremented. Must lie within scale.
	 * @throws DataChunkException Thrown when value is not within scale
	 */
	public void inc(int value) throws DataChunkException {
		if (!scale.includes(value))
			throw new DataChunkException(name + " cannot be incd in value " + value + " as it is beyond the scale (" + scale + ").");
		int toInc = transformAbsoluteToRelative(value);
		valueCounts[toInc]++;
	}
	
	/**
	 * Increments count for specific data value by one
	 * @param value Specifies the raw data value, which is to be incremented. Must lie within scale.
	 * @param count Specifies how many counts are to be added. Must be > 0
	 * @throws DataChunkException Thrown when value is not within scale, or when count is negative.
	 */
	public void incMulti(int value, int count) throws DataChunkException {
		if (!scale.includes(value))
			throw new DataChunkException(name + " cannot be incd in value " + value + " as it is beyond the scale (" + scale + ").");
		int toInc = transformAbsoluteToRelative(value);
		if (count < 0)
			throw new DataChunkException(name + " cannot be incd in value " + value + " by a negative count (" + count + ").");
		valueCounts[toInc] += count;
	}
	
	/**
	 * Attention! Dangerous!
	 * @param values
	 */
	public void setCounts(int[] values) {
		valueCounts = values;
	}
	
	public int getCounts(int i) {
		return valueCounts[i];
	}
	
	/**
	 * Returns weighted average of all counts
	 * @return weighted average of all counts
	 */
	public double getAvg() {
		double tempSum = 0;
		double tempCount = 0;
		for (int i = 0; i < valueCounts.length; i++) {
			tempSum += valueCounts[i] * (i + scale.from());
			tempCount += valueCounts[i];
		}
		return (tempSum / tempCount);
	}
	
	/**
	 * Returns total counts, i.e. for each valid value.
	 * @return
	 */
	public int getTotalCounts() {
		int temp = 0;
		for (int i = 0; i < valueCounts.length; i++) {
			temp += valueCounts[i];
		}
		return temp;
	}
	
	/**
	 * Returns specific count, i.e. for value.
	 * @param value
	 * @return
	 * @throws DataChunkException When value is not within scale.
	 */	public int getCount(int value) throws DataChunkException {
		int trueValue = transformRelativeToAbsolute(value);
		if (!scale.includes(trueValue))
			throw new DataChunkException(name + " cannot return value count for " + value + " as it is beyond the scale (" + scale + ").");
		return value;
	}
	
	/**
	 * transforms raw data value to a number that is stored in array
	 * 
	 * Example: If scale is from 3 to 5, i=3 returns 0 (lowest possible value according to scale)
	 * @param i
	 * @return
	 */
	private int transformRelativeToAbsolute(int i) {
		return i+scale.from();
	}
	
	/**
	 * transforms array number to a number that represents the true value
	 * 
	 * Example: If scale is from 3 to 5, i=1 returns 4
	 * @param i
	 * @return
	 */
	private int transformAbsoluteToRelative(int i) {		
		return i-scale.from();
	}
	
	public Scale getScale() {
		return scale;
	}

	/**
	 * Combines two value chunks, e.g. if company score is to be evaluated from different team chunks
	 * Attention : scales must correspond!
	 * @param valueChunk
	 */
	public void add(DataChunk valueChunk) {
		for (int i = 0; i < valueCounts.length; i++) {
			valueCounts[i] += valueChunk.getCounts(i);
		}
	}
	
	
	/**
	 * Sets raw data from ArrayList --> counts them, if contained by scale
	 * @param column ArrayList with raw String values
	 */
	public void setData(ArrayList<String> column) {
		Iterator<String> it = column.iterator();
		while (it.hasNext()) {
			int next;
			try {
				next = Integer.parseInt(it.next());
				//count the parsed int --> inc makes sure it is within the scale
				inc(next);
			} catch (NumberFormatException | DataChunkException e) {
				//either no int value parsed, or not within scale
				//in both cases: invalid data --> do nothing
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns itself in a container
	 * @return all values encapsulated in container
	 */
	public DataChunkContainer toContainer() {
		DataChunkContainer c = new DataChunkContainer(name, scale);
		try {
			c.addChunk(this);
		} catch (DataChunkException e) {
			// Can't throw exception, as scales are the same by definition!
			e.printStackTrace();
		}
		return c;
	}

	
	public String toString() {
		String t = "";
		for (int i = 0; i < valueCounts.length; i++) {
			t += valueCounts[i] + " ";
		}
		return "ValueChunk " + name + " [" + scale + "]\n" + t;
	}
}

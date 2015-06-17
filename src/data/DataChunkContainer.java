package data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ListIterator;

import base.Settings;

/**
 * Permits to store more than one variable, and still treat it as one (composite variable)
 * 
 * @author clemens
 *
 */
public class DataChunkContainer {
	private String name;
	private Scale scale;
	protected ArrayList<DataChunk> list;
	
	public DataChunkContainer(String name, Scale newScale) {
		this.name = name;
		this.scale = newScale;
		list = new ArrayList<DataChunk>();
	}

	/**
	 * Adds chunk of data. DataChunk is used as a lower level data container.
	 * @param chunk First or subsequent DataChunk
	 * @throws DataChunkException
	 */
	public void addChunk(DataChunk chunk) throws DataChunkException {
		if (chunk.getScale().equals(scale)) {
			list.add(chunk);
		} else {
			throw new DataChunkException("Can't add chunk as scales don't match.");
		}
	}
	
	/**
	 * Adds all DataChunks from another DataChunkContainer. Beware of not corresponding scales!
	 * @param container
	 * @throws DataChunkException
	 */
	public void add(DataChunkContainer container) throws DataChunkException {
		if (list.size() == 0 && scale.equals(container.scale)) {
			list = container.list;
		} else if (list.size() != container.list.size() || !scale.equals(container.scale)) {
			throw new DataChunkException("Can't add containers, as they don't have the same amount of chunks / same scale!");
		} else {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).add(container.list.get(i));
			}
		}
	}
	
	/**
	 * Manually add data. Use with care! Interrupting normal workflow!
	 * @param counts
	 * @param value
	 */
	public void addAs(int counts, int value) {
		DataChunk toInc;
		if (list.size()<1) {
			toInc = new DataChunk("Added ValueChunk", scale);
			list.add(toInc);
		} else {
			toInc = list.get(0);
		}
		try {
			toInc.incMulti(value, counts);
		} catch (DataChunkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns average for all values
	 * @return
	 */
	public double getAvg() {
		ListIterator<DataChunk> it = list.listIterator();
		double sum = 0;
		int count = 0;
		while (it.hasNext()) {
			sum += it.next().getAvg();
			count++;
		}
		return sum/count;
	}
	
	/**
	 * Returns total counts (over all chunks) for specific value
	 * @param value
	 * @return
	 */
	public int getCounts(int value) {
		ListIterator<DataChunk> it = list.listIterator();
		int sum = 0;
		while (it.hasNext()) {
			sum += it.next().getCounts(value);
		}
		return sum;
	}
	
	/**
	 * Returns total counts (over first chunk) for all values
	 * Beware! Not equal to head count/data row count for composites 
	 * 	--> Not all values are completed by all employees
	 * @param value
	 * @return
	 */
	private int getCounts() {
		ListIterator<DataChunk> it = list.listIterator();
		int sum = 0;
		//only need one of the composite, if there are composite variables; otherwise ppl would be counted several times.
		//while (it.hasNext()) {
			sum += it.next().getTotalCounts();
		//}
		return sum;
	}

	public String toString() {
		String t = "#### ValueChunkContainer \"" + name + "\"" + scale + "\n";
		for (int i = 0; i < list.size(); i++) {
			t += list.get(i) + "\n";
		}

		return t + "#### end " + name;
	}

	/**
	 * Return String for average, with two decimals
	 * @return
	 */
	public String getAvgString() {
		double avg = getAvg();
		if (avg > 0) {
			DecimalFormat f = new DecimalFormat("#0.00"); 
			return f.format(getAvg());
		} else {
			return "−";
		}
	}
	
	
	/**
	 * Returns standard deviation. Not applicable to composite variables!
	 * @return
	 */
	public double getStDev() {
		if (list.size() > 1) {
			return -1;
		} else {
			double mean = getAvg();
			double sum = 0;
			int totalCounts = getCounts();
			for (int i = 0; i < scale.size(); i++) {
				int currCounts = getCounts(i);
				int currValue = scale.from() + i;
				double currSqDeviation = Math.pow(currValue-mean, 2);
				sum += currCounts * currSqDeviation;
			}
			return Math.sqrt(sum/(totalCounts-1));
		}
	}


	/**
	 * Return String for standard deviation, with two decimals
	 * @return
	 */
	public String getStDevString() {
		DecimalFormat f = new DecimalFormat("#0.000");
		double d = getStDev();
		if (Settings.OMIT_ST_DEVIATION) {
			return "−";
		} else if (d > 0){
			return f.format(d);
		} else {
			return "−";
		}
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the relative occurrence of value
	 * @param value
	 * @return
	 */
	public double getPercentage(int value) {
		return (double)getCounts(value)/getCounts();
	}

	/**
	 * Returns String for the relative occurrence of value, with two decimals
	 * @param value
	 * @return
	 */	public String getPercentageString(int i) {
		double perc = getPercentage(i);
		if (perc <= 0)
			return "−";
		DecimalFormat f = new DecimalFormat("#0.00");
		return f.format(perc*100) + " %";
	}

}

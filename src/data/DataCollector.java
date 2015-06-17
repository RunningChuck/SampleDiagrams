package data;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import base.Settings;
import au.com.bytecode.opencsv.CSVReader;

/**
 * [Deprecated --> use SQLCollector classes!] Basic data collector for csv input.
 * @author clemens
 *
 */
public class DataCollector {

	protected String filename;
	protected String[] names;
	
	//[y][x]
	protected String[][] lines;
	
	//y
	protected int linesCount;

	public DataCollector(String filename) throws DataCollectorException, IOException {
		this.filename = filename;
		getNamesAndLines();
	}
	
	public int getLinesCount() {
		return linesCount;
	}
	
	public String[] getLine(int line){
		if (line >= linesCount) {
			return null;
		} else {
			return lines[line];
		}
	}
	
	/**
	 * Read variable names and data lines to class internal variables
	 */
	protected void getNamesAndLines() throws DataCollectorException, IOException {
		CSVReader reader = getFileReader();
		names = reader.readNext();
		int i = 0;
		// get line count
		while ((reader.readNext()) != null) {
			i++;
		}
		linesCount = i;
		reader.close();

		// get names
		reader = getFileReader();
		lines = new String[linesCount][];
		String[] nextLine = reader.readNext();
		for (int j = 0; j < linesCount; j++) {
			nextLine = reader.readNext();
			lines[j] = nextLine;
		}
		if (linesCount != lines.length)
			throw new DataCollectorException(
					"File is corrupted.");
		reader.close();
	}
	
	/**
	 * Returns all values in column specified by column header (name)
	 * @throws DataCollectorException
	 */
	public ArrayList<String> getColumn(String name) throws DataCollectorException {
		return getColumn(name, 1);		
	}
	
	/**
	 * Returns all values in column specified by column header (pos)
	 * @param pos column position
	 * @throws DataCollectorException
	*/
	public ArrayList<String> getColumn(int pos) throws DataCollectorException {
		return getColumn(pos, 1);		
	}
	
	/**
	 * Returns all values in column specified by column header (name)
	 * @throws DataCollectorException
	 */
	public String[] getColumnAsArray(String name) throws DataCollectorException {
		return (String[]) getColumn(name, 1).toArray();		
	}
		
	/**
	 * Returns all values in column specified by line (pos)
	 * @param pos column position
	 * @throws DataCollectorException
	 */
	public String[] getColumnAsArray(int pos) throws DataCollectorException {
		return (String[]) getColumn(pos, 1).toArray();		
	}
	 
	/**
	 * Returns all values in column specified by column header (name)
	 * @param startAt defines start of column (0 includes header)
	 * @throws DataCollectorException
	*/
	public ArrayList<String> getColumn(String name, int startAt) throws DataCollectorException {
		int pos = getNamePosition(name);
		return getColumn(pos, startAt);		
	}
	
	/**
	 * Returns all values in column specified by position
	 * @param pos column position
	 * @param startAt defines start of column (0 includes header)
	 * @return
	 * @throws DataCollectorException
	 */
	public ArrayList<String> getColumn(int pos, int startAt) throws DataCollectorException {
		return getColumn(pos, startAt, null, null);
	}

	/**
	 * Returns all values in column specified by position, additionally filters specified column by specified value
	 * @param pos column position (desired values)
	 * @param startAt defines start of column (0 includes header)
	 * @param filterName specifies column for filter
	 * @param fitlerValue specifies value for the filter
	 * @return
	 * @throws DataCollectorException
	 */
	public ArrayList<String> getColumn(int pos, String filterName, int fitlerValue) throws DataCollectorException {
		return getColumn(pos, 1, filterName, fitlerValue+"");
	}
	
	/**
	 * Returns all values in column specified by position, additionally filters specified column by specified value
	 * @param name column name (desired variable)
	 * @param startAt defines start of column (0 includes header)
	 * @param filterName specifies column for filter
	 * @param fitlerValue specifies value for the filter
	 * @return
	 * @throws DataCollectorException
	 */
	public ArrayList<String> getColumn(String name, String filterName, int fitlerValue) throws DataCollectorException {
		return getColumn(getNamePosition(name), 1, filterName, fitlerValue+"");
	}

	
	/**
	 * Returns all values in column specified by position, additionally filters specified column by specified value
	 * @param pos column position (desired values)
	 * @param startAt defines start of column (0 includes header)
	 * @param filterName specifies column for filter
	 * @param fitlerValue specifies value for the filter
	 * @return
	 * @throws DataCollectorException
	 */
	public ArrayList<String> getColumn(int pos, int startAt, String filterName, String filterValue) throws DataCollectorException {
		ArrayList<String> list = new ArrayList<String>();
		//-1 means not existent!
		int filterPos = -1;
		if (!filterName.equals(null))
			filterPos = getNamePosition(filterName);
		else
		if (startAt == 0) {
			list.add(names[pos]);
			startAt = 1;
		}
		for (int i = startAt-1; i < lines.length; i++) {
			//only add if filter value IS EXISTANT, and if filter value equals current value (interpreted from the right)
			if (lines[i][filterPos].equals(filterValue) && filterPos >= 0)
				list.add(lines[i][pos]);			
		}
		return list;		
	}

	/**
	 * Finds data variable name in the internal data set
	 */
	protected int getNamePosition(String name) throws DataCollectorException {
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(name)) {
				return i;
			}
		}
		throw new DataCollectorException("Variable " + name + " not found in " + filename + ".");
	}	
	
	/**
	 * Get file reader to read file.
	 */	
	protected CSVReader getFileReader() throws DataCollectorException, UnsupportedEncodingException {
		try {
			return new CSVReader(new InputStreamReader(new FileInputStream(filename), Settings.CSV_CHARSET),
					Settings.CSV_DELIMITER);
		} catch (FileNotFoundException e) {
			throw new DataCollectorException("File \"" + filename
					+ "\" not found.");
		}
	}
}
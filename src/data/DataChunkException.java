package data;

/**
 * Exception thrown when extracting variable from raw data causes problems
 * @author clemens
 *
 */
public class DataChunkException extends Exception {

	private static final long serialVersionUID = 57701185137840215L;

	public DataChunkException(String string) {
		super(string);
	}

}

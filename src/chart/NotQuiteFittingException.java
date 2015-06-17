package chart;

public class NotQuiteFittingException extends Exception {

	/**
	 * Exception occurring when text exceeds boundaries while charting (TextContainer)
	 * @author clemens
	 */
	private static final long serialVersionUID = 57701185137840215L;
	private double oldHeight;
	private double newHeight;
	
	public double getOldHeight() {
		return oldHeight;
	}
	
	public double getNewHeight() {
		return newHeight;
	}

	public NotQuiteFittingException(String string, double oldHeight, double newHeight) {
		super(string);
		this.oldHeight = oldHeight;
		this.newHeight = newHeight;
	}

}

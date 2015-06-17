package chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import base.Settings;
import base.SettingsColor;
import chart.TextContainer.HorizontalAlignment;
import chart.TextContainer.VerticalAlignment;

/**
 * Contains one or more BarPart. Handles orientation.
 * @author clemens
 *
 */
public class BarContainer {
	//FROM direction
	public enum Orientation {LEFT, RIGHT, TOP, BOTTOM};
	
	private ArrayList<BarPart> list;
	private Orientation orientation;
	private double totalValue;
	private boolean printValues = true;
	private boolean absoluteValues = false;
	
	/**
	 * Constructor demands orientation (for convention purposes)
	 * @param orientation
	 */
	public BarContainer(Orientation orientation) {
		this.orientation = orientation;
		list = new ArrayList<BarPart>();
		totalValue = 0;
	}
	public void add(BarPart part) {
		list.add(part);
		totalValue += part.getValue();
	}
	
	public Color[] getColors() {
		Color[] colors = new Color[list.size()];
		for (int i = 0; i < list.size(); i++) {
			colors[i] = list.get(i).getColor();
		}
		return colors;
	}
	
	public void printValues(boolean printValues) {
		this.printValues = printValues;
	}
	
	public void setAbsoluteValues(boolean absoluteValues) {
		this.absoluteValues = absoluteValues;
	}

	
	public int getCounts() {
		return (int)totalValue;
	}

	public double getPercentageCounts() {
		return totalValue;
	}
	
	public double getAvgPercentage() {
		double sum = 0;
		Iterator<BarPart> it = list.iterator();
		while (it.hasNext()) {
			sum += it.next().getValue();
		}
		return sum/list.size();
	}

	public String getAveragePercentageString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(getAvgPercentage()*100) + "%";
	}
	
	public int getCountsRecountInt() {
		int sum = 0;
		Iterator<BarPart> it = list.iterator();
		while (it.hasNext()) {
			sum += (int)it.next().getValue();
		}
		return sum;
	}
	
	public int getCounts(int i) {
		return (int) list.get(i).getValue();
	}
	
	/**
	 * Special: Doesn't recalculate! Needed for multiple choice items (as answer dont add up to 100%)
	 * @param i
	 * @return
	 */
	public double getPercentageCount(int i) {
		return list.get(i).getValue();
	}
	
	/**
	 * Special: Doesn't recalculate! Needed for multiple choice items (as answer dont add up to 100%)
	 * @param i
	 * @return
	 */
	public String getPercentageCountString(int i) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(getPercentageCount(i)*100) + "%";
	}
	
	public void draw(Rectangle2D.Double rect, Font font, Graphics2D g2d) {
		//save temp color
		Color colorTemp = g2d.getColor();
		
		//determine bounds
		double x_width = 0, y_width = 0, x_length = 0, y_length = 0, x_upperleft = 0, y_upperleft = 0, x_upperleft_temp, y_upperleft_temp;
		if (orientation == Orientation.LEFT) {
			x_width = 0;
			y_width = rect.getHeight();
			x_length = rect.getWidth();
			y_length = 0;
			x_upperleft = rect.getMinX();
			y_upperleft = rect.getMinY();
		} else if (orientation == Orientation.RIGHT) {
			x_width = 0;
			y_width = rect.getHeight();
			x_length = -rect.getWidth();
			y_length = 0;
			x_upperleft = rect.getMaxX();
			y_upperleft = rect.getMinY();
		} else if (orientation == Orientation.TOP) {
			x_width = rect.getWidth();
			y_width = 0;
			x_length = 0;
			y_length = rect.getHeight();
			x_upperleft = rect.getMinX();
			y_upperleft = rect.getMinY();
		} else if (orientation == Orientation.BOTTOM) {
			x_width = rect.getWidth();
			y_width = 0;
			x_length = 0;
			y_length = -rect.getHeight();
			x_upperleft = rect.getMinX();
			y_upperleft = rect.getMaxY();
		}
		x_upperleft_temp = x_upperleft;
		y_upperleft_temp = y_upperleft;
		
		
		//draw grey bar, if no BarParts known
		if (totalValue == 0) {
			Rectangle2D.Double totalRect = null;
			if (orientation == Orientation.LEFT)
				totalRect = new Rectangle2D.Double(x_upperleft_temp, y_upperleft_temp, x_length, y_width);
			if (orientation == Orientation.RIGHT)
				totalRect = new Rectangle2D.Double(x_upperleft_temp-x_length, y_upperleft_temp, -x_length, y_width);
			if (orientation == Orientation.TOP)
				totalRect = new Rectangle2D.Double(x_upperleft_temp, y_upperleft_temp, x_width, y_length);
			if (orientation == Orientation.BOTTOM)
				totalRect = new Rectangle2D.Double(x_upperleft_temp, y_upperleft_temp-y_length, x_width, -y_length);
			g2d.setColor(SettingsColor.voidColor);
			g2d.fill(totalRect);
		} else {
		//draw every part, relative to total bounds size
			for (int i = 0; i < list.size(); i++) {
				double x_add = x_length * list.get(i).getValue() / totalValue;
				double y_add = y_length * list.get(i).getValue() / totalValue;
				//vertical
				if (x_add == 0)
					x_add = x_width;
				//horizontal;
				if (y_add == 0)
					y_add = y_width;
				//RIGHT
				if (x_add < 0) {
					x_upperleft_temp += x_add;
					x_add *= (-1);
				}
				//BOTTOM
				if (y_add < 0) {
					y_upperleft_temp += y_add;
					y_add *= (-1);
				}
				Rectangle2D.Double nextRect = new Rectangle2D.Double(x_upperleft_temp, y_upperleft_temp, x_add, y_add);
				if (orientation == Orientation.LEFT)
					x_upperleft_temp += x_add;
				if (orientation == Orientation.TOP)
					y_upperleft_temp += y_add;
				g2d.setColor(list.get(i).getColor());
				g2d.fill(nextRect);					 
				//print values
				if (printValues && (getPercentage(i) > Settings.MINIMUMTOPRINT)) {
					try {
						if (absoluteValues) {
							//TODO: Orientation support; for now only single bars in top-orientation
							TextContainer.draw(Settings.NSYMBOL + getCounts(i), x_upperleft_temp, y_upperleft_temp - font.getSize()*2, x_add, font.getSize()*1.5, font, Color.black,
									HorizontalAlignment.CENTER, VerticalAlignment.CENTER, g2d);
						} else {
							TextContainer.draw(getPercentageString(i), x_upperleft_temp, y_upperleft_temp, x_add, y_add, font, Color.black,
									HorizontalAlignment.CENTER, VerticalAlignment.CENTER, g2d);
						}
					} catch (NotQuiteFittingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		g2d.setColor(colorTemp);
	}
	
	public double getPercentage(int i) {
		return list.get(i).getValue() / totalValue;
	}

	
	public String getPercentageString(int i) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(getPercentage(i)*100) + "%";
	}
}

package chart;

import java.awt.Color;

public class BarPart {
	public enum Alignment {HORIZONTAL, VERTICAL};
	
	private double value;
	private Color color;
	public BarPart(double value, Color color) {
		if (value < 0)
			this.value = 0;
		else
			this.value = value;
		this.color = color;
	}
	public double getValue() {
		return value;
	}
	
	public Color getColor() {
		return color;
	}

}

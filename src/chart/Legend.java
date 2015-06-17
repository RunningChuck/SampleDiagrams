package chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import chart.TextContainer.HorizontalAlignment;
import chart.TextContainer.VerticalAlignment;

public class Legend {
	private String[] titles;
	private Color[] colors;
	private Font font;
	private float fontsize;
	private double elementSpacing = 3;
	private boolean useSymbols = false;
	float y_gap_legend_between;
	
	public Legend(String[] titles, Color[] colors, Font font) {
		this.titles = titles;
		this.colors = colors;
		
		this.fontsize = font.getSize();
		this.font = font;
		y_gap_legend_between = (float) (fontsize*elementSpacing);
	}

	public void setSymbols(boolean useSymbols) {
		this.useSymbols = useSymbols;
	}
	
	public void draw(Rectangle2D.Double rect, Graphics2D g2d) throws NotQuiteFittingException {
		double width = rect.getWidth();
		//System.out.println((z_titles.length+1)/2);
		
		
		for (int i = 0; i < titles.length; i++) {
			float xCoordinate = (float) (rect.getMinX());
			float yCoordinate = (float) (rect.getMinY() + i*y_gap_legend_between);
			int symbol = 7;
			if (useSymbols)
				symbol = i;
			g2d.setColor(colors[i]);
			g2d.fill(ShapeGenerator.getShape(symbol, xCoordinate + 1*fontsize, yCoordinate+0.5*y_gap_legend_between, fontsize));
						
			TextContainer.draw(titles[i], xCoordinate + 2*fontsize, yCoordinate,
					width - (elementSpacing)*fontsize, y_gap_legend_between, font, Color.black,
					HorizontalAlignment.LEFT, VerticalAlignment.CENTER, g2d);
		}
		g2d.setColor(Color.black);
		Rectangle2D.Double newRect = new Rectangle2D.Double(rect.getMinX(), rect.getMinY(), width, (titles.length) * y_gap_legend_between);
		//g2d.draw(rect);
		g2d.draw(newRect);
		if (newRect.getHeight()>rect.getHeight())
			throw new NotQuiteFittingException("Legend is bigger than expected and doesnt fit into given rectangle.",
					rect.getHeight(), newRect.getHeight());
	}

	public void reverse() {
		String[] newTitles = new String[titles.length];
		Color[] newColors = new Color[titles.length];
		for (int i = 0; i < titles.length; i++) {
			newTitles[i] = titles[titles.length-1-i];
			newColors[i] = colors[titles.length-1-i];
		}
		titles = newTitles;
		colors = newColors;
	}

	public void setElementSpacing(double d) {
		elementSpacing = d;		
		y_gap_legend_between = (float) (fontsize*elementSpacing);
	}
}

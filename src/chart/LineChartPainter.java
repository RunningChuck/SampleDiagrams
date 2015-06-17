package chart;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import org.apache.batik.ext.awt.geom.Polygon2D;

import chart.TextContainer.HorizontalAlignment;
import chart.TextContainer.VerticalAlignment;
import data.Scale;
import base.SettingsColor;

/**
 * Painter class for LineCharts --> draws data into charts
 * @author clemens
 *
 */
public class LineChartPainter extends ChartPainter {
	private Color[] colors;
	private LineChart chart;


	private float xLeft = 500;
	private float xRight = 390;
	private float xGapAfter = 20;
	private float xChart = 1200;
	
	private float yHeader = 50;
	private float yGapBetween = 140;
	private float yGapScale = 150;

	private float line = 2;
	
	private int itemNumber;
	
	private Graphics2D g2d;
	
	private double[][] values_avg;
	
	//temp reactangle object
	private Rectangle2D rect, bounds;
	//temp string object s
	private String s;
	//temp fontmetrics
	private FontMetrics fm;
	
	public LineChartPainter(LineChart chart, GraphicsWriter writer) {
		super(chart, writer);
		this.chart = chart;
		
		setColors(SettingsColor.getLineColors());
		
		g2d = writer.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		values_avg = chart.getValuesAvg();
		
		itemNumber = chart.getYValueTitles().length;
		
		heightTotal = yHeader + (itemNumber-1) * yGapBetween + yGapScale;
		widthTotal = xLeft + xChart + xRight + xGapAfter;

		paintStatic();
		paintLegend();
		paintValues();
	}
	
	private void setColors(Color[] colorsInput) {
		colors = new Color[colorsInput.length];
		for (int z = 0; z < colorsInput.length; z++) {
			colors[z] = colorsInput[z];
			//used for dual datasets
			//colors[1][z] = Helper.brighten(colorsInput[z],0.3);
		}
	}

	private void paintStatic() {
		//#### grid
		//vertical
		g2d.setColor(Color.black);
		rect = new Rectangle2D.Double(xLeft, yHeader, line, (itemNumber-1) * yGapBetween);
		g2d.fill(rect);

		rect = new Rectangle2D.Double(xLeft+xChart, yHeader, line, (itemNumber-1) * yGapBetween);
		g2d.fill(rect);

		g2d.setColor(Color.gray);
		rect = new Rectangle2D.Double(xLeft + xChart/2, yHeader, line, (itemNumber-1) * yGapBetween);
		g2d.fill(rect);
		
		//horizontal
		g2d.setColor(Color.black);
		for (int y = 0; y < itemNumber; y++) {
			rect = new Rectangle2D.Double(xLeft - fontsizeSmall/3, yHeader + (y*yGapBetween), xChart + fontsizeSmall/3 + line, line);
			g2d.fill(rect);
		}
		//#### top scale
		Scale scale = null;
		scale = chart.getScale();
		for (int x = 0; x < scale.size(); x++) {
			g2d.setFont(fontPlainSmall);
			fm = g2d.getFontMetrics();
			s = (x+scale.from())+"";
			bounds = fm.getStringBounds(s, g2d);
			g2d.drawString(s, (float) (xLeft + x*xChart/(scale.size()-1) - bounds.getWidth()/2), (float) (yHeader-1*fontsizeSmall));
			
			rect = new Rectangle2D.Double(xLeft + x*xChart/(scale.size()-1), yHeader - fontsizeSmall/3, line, fontsizeSmall/3);
			g2d.fill(rect);
		}
		
		//#### side captions
		String[] yTitles = chart.getYValueTitles();
		for (int y = 0; y < yTitles.length; y++) {
			try {
				TextContainer.draw(yTitles[y], 0, yHeader + (y-0.5)*yGapBetween, xLeft-fontsizeSmall, yGapBetween, 
						fontPlainBig, Color.BLACK, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, g2d);
			} catch (NotQuiteFittingException e) {
				e.printStackTrace();
			}
		}
		
		//#### bottom scale
		float[] xpoints = new float[3];
		float[] ypoints = new float[3];
		xpoints[2] = xLeft;
		xpoints[0] = xpoints[1] = xLeft + xChart;
		ypoints[0] = heightTotal - yGapScale*13/16;
		ypoints[1] = ypoints[2] = heightTotal - yGapScale*3/8;
		Polygon2D p = new Polygon2D(xpoints, ypoints, 3);
		g2d.setColor(new Color(180,180,180));
		g2d.fill(p);
		Color legendGray = new Color(100,100,100);
		g2d.setColor(legendGray);
		String[] x_titles = {"kaum", "sehr"};
		int width = 160;
		try {
			TextContainer.draw(x_titles[0], xLeft, heightTotal - yGapScale*3/8-3, width, yGapScale*3/8,
					fontPlainSmall, legendGray,	HorizontalAlignment.LEFT, VerticalAlignment.TOP, g2d);
			TextContainer.draw(x_titles[1], xLeft+xChart-width, heightTotal - yGapScale*3/8-3, width, yGapScale*3/8,
					fontPlainSmall, legendGray,	HorizontalAlignment.RIGHT, VerticalAlignment.TOP, g2d);		
		} catch (NotQuiteFittingException e) {
			e.printStackTrace();
		}
	}
	
	public void paintLegend() {
		Legend legend = new Legend(chart.getZValueTitles(), colors, fontPlainBig);
		
		legend.setSymbols(true);
		try {
			legend.draw(new Rectangle2D.Double(xLeft + xChart + fontsizeBig, yHeader, xRight - fontsizeBig, 1), g2d);
		} catch (NotQuiteFittingException e1) {
			if (heightTotal < yHeader+e1.getNewHeight())
				heightTotal = (float) (yHeader+e1.getNewHeight());
		}
	}
	
	public void paintValues() {
		Color tempColor = g2d.getColor();
		double xC, yC, xO, yO, xTemp, yTemp;
		Scale scale = null;
		scale = chart.getScale();
		
		int skip = 0;
		//draw Verbindungslinie
		for (int gemeinde = 0; gemeinde < values_avg[0].length; gemeinde++) {
			xTemp = -1;
			yTemp = -1;
			g2d.setColor(colors[gemeinde]);
			for (int item = 0; item < values_avg.length; item++) {
				if (java.lang.Double.isNaN(values_avg[item][gemeinde])) {
					skip++;
					continue;
				}
				xO = values_avg[item][gemeinde] - scale.from();
				yO = item;
				xC = xLeft + xChart/(scale.size()-1) * (xO);
				yC = yHeader + yGapBetween * (yO);
				
				double boldness = 3*line/4;
					
				//vektor (a1,a2) berechnen (aus x1,x2,y1,y2)
				double a1 = xC - xTemp;
				double a2 = yC - yTemp;
					
				//orthogonalen vektor (b1,b2) finden
				double b1 = 1; //wenn linie nie horizontal
				double b2 = -a1/a2;
//				double b2 = 1; //wenn linie nie vertikal
//				double b1 = -a2/a1;					
				
				//vektor (b1,b2) auf liniendicke/2 normalisieren
				double bLength = Math.sqrt(b1*b1+b2*b2);
				b1 *= boldness / 2 / bLength;
				b2 *= boldness / 2 / bLength;
				
				float[] xpoints = {(float)(xTemp - b1),	(float)(xTemp + b1),	(float)(xC + b1),	(float)(xC - b1)};
				float[] ypoints = {(float)(yTemp - b2),	(float)(yTemp + b2),	(float)(yC + b2),	(float)(yC - b2)};

//				double xV, yV, x1, y1, d;
//				xV = xC - xTemp;
//				yV = yC - yTemp;
//				d = 3*line/4;
//				y1 = d / Math.cos(Math.atan(yV/xV));
//				float[] xpoints = {(float) xTemp,	(float) xTemp,	(float) xC,	(float) xC};
//				float[] ypoints = {(float) (yTemp-y1/2),	(float) (yTemp+y1/2),	(float) (yC+y1/2),	(float) (yC-y1/2)};
				
				//if one point has been skipped, don't draw line! but save the next point, and reset "skip"
				if (skip > 0 || item == 0) {
					skip = 0;
				} else {
					g2d.fill(new Polygon2D(xpoints, ypoints, 4));
				}
				xTemp = xC;
				yTemp = yC;
			}
		}
		
		//Draw symbol
		//ebene: (einzel,) branchen, gesamt
		for (int gemeinde = 0; gemeinde < values_avg[0].length; gemeinde++) {
			for (int item = 0; item < values_avg.length; item++) {
				if (java.lang.Double.isNaN(values_avg[item][gemeinde])) {
					continue;
				}
				g2d.setColor(colors[gemeinde]);
				xO = values_avg[item][gemeinde] - scale.from();
//				System.out.println(z_titles[z] + " (" + y_titles[y] + ")" + ": " + xO);
				yO = item;
				xC = xLeft + xChart/(scale.size()-1) * (xO);
				yC = yHeader + yGapBetween * (yO);
				
				//Draw symbol
				g2d.fill(ShapeGenerator.getShape(gemeinde, xC, yC, fontsizeSmall));
			}
		}
		g2d.setColor(tempColor);
	}
}

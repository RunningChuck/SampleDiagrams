package chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import chart.TextContainer.HorizontalAlignment;
import chart.TextContainer.VerticalAlignment;

/**
 * Painter class for BarCharts --> draws data into charts
 * @author clemens
 *
 */
public class BarChartPainter extends ChartPainter{
	private BarChart chart;

	// leftmost part
	private float xLeft = 110;
	// total grapg width
	private float xGraph = 1200;
	// single bar width
	private float xBar = 100;
	// rightmost part
	private float xRight = 438;
	
	// uppermost part
	private float yHeader = 20;
	// total graph height
	private float yGraph = 800;
	// downmost part
	private float yFooter = 140;
	
	private float line = 2;
	
	private Graphics2D g2d;
	private Rectangle2D.Double rect;
	private BarContainer[] bars;

	public BarChartPainter(BarChart chart, GraphicsWriter writer) {
		super(chart, writer);
		this.chart = chart;
		widthTotal = xLeft + xGraph + xRight;
		heightTotal = yHeader + yGraph + yFooter;
		
		g2d = writer.getGraphics();
		bars = chart.getBars();

		paintStatic();
		paintBars();
		
		Legend legend = new Legend(chart.getLegendTitles(), bars[0].getColors(), fontPlainBig);
		try {
			legend.reverse();
			legend.draw(new Rectangle2D.Double(xLeft + xGraph + fontsizeBig, yHeader, xRight - fontsizeBig - 2, 2), g2d);
		} catch (NotQuiteFittingException e1) {
			if (heightTotal < yHeader+e1.getNewHeight())
				heightTotal = (float) (yHeader+e1.getNewHeight());
		}
		
		for (int i = 0; i < bars.length; i++) {
					bars[i].draw(new Rectangle2D.Double(xLeft + (xGraph/bars.length)*(i) + (xGraph/bars.length-xBar)/2, yHeader+line, xBar, yGraph-line),
							fontPlainSmall, g2d);
		}
	}
	
	/**
	 * Paint static parts.
	 */
	private void paintStatic() {
		g2d.setColor(new Color(160, 160, 160));
		//scale annotations
		for (int i = 0; i < 11; i++) {
			if (i > 0 && i < 10) {
				rect = new Rectangle2D.Double(xLeft-fontsizeSmall/2, yHeader + (10-i)*yGraph/10, xGraph + fontsizeSmall/2, line);
				g2d.fill(rect);
			}
			String s = "0 %";
			if (i > 0)
				s = i + s;
			try {
				TextContainer.draw(s, 0, yHeader + (9.5-i)*yGraph/10, xLeft - fontsizeSmall, yGraph/10,
						fontPlainBig, Color.black, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, g2d);
			} catch (NotQuiteFittingException e) {
				e.printStackTrace();
			}
		}

		rect = new Rectangle2D.Double(xLeft-fontsizeSmall/2, yHeader, xGraph + fontsizeSmall/2 + line, line);
		g2d.fill(rect);
		rect = new Rectangle2D.Double(xLeft-fontsizeSmall/2, yHeader + yGraph, xGraph + fontsizeSmall/2 + line, line);
		g2d.fill(rect);
		rect = new Rectangle2D.Double(xLeft, yHeader, line, yGraph + line + fontsizeSmall/2);
		g2d.fill(rect);
	}
	
	/**
	 * Paint data-related stuff for single datasets.
	 */
	private void paintBars() {
		g2d.setColor(Color.black);
		String[] titles = chart.getBarTitles();
		for (int i = 0; i < bars.length; i++) {
			rect = new Rectangle2D.Double(xLeft + (i+1) * xGraph/bars.length, yHeader + yGraph + line, line, fontsizeSmall/2);
			g2d.fill(rect);
			if (i!=0) {
				g2d.setColor(new Color(160, 160, 160));
				rect = new Rectangle2D.Double(xLeft + (i) * xGraph/bars.length, yHeader, line, yGraph);
				g2d.fill(rect);
				g2d.setColor(Color.black);
			}
			try {
				TextContainer.draw(titles[i], xLeft + (i) * xGraph/bars.length, yHeader + yGraph + line + fontsizeSmall,  xGraph/bars.length, yFooter,
						fontPlainBig, Color.black, HorizontalAlignment.CENTER, VerticalAlignment.TOP, g2d);
			} catch (NotQuiteFittingException e) {
				e.printStackTrace();
			}
		}
	}
}

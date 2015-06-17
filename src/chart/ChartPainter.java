package chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import base.SettingsFont;

public abstract class ChartPainter {
	protected float widthTotal;
	protected float heightTotal;
	protected Chart chart;
	protected Color[][] colors;
	protected boolean compare;
	
	private String fontfamily = SettingsFont.fontfamily;
	
	protected float fontsizeTiny = SettingsFont.fontsizeTiny;
	protected float fontsizeSmall = SettingsFont.fontsizeSmall;
	protected float fontsizeBig = SettingsFont.fontsizeBig;
	
	protected Font fontPlainTiny = new Font(fontfamily, Font.PLAIN, (int)fontsizeTiny);
	protected Font fontBoldTiny = new Font(fontfamily, Font.BOLD, (int)fontsizeTiny);
	protected Font fontPlainSmall = new Font(fontfamily, Font.PLAIN, (int)fontsizeSmall);
	protected Font fontBoldSmall = new Font(fontfamily, Font.BOLD, (int)fontsizeSmall);
	protected Font fontPlainBig = new Font(fontfamily, Font.PLAIN, (int)fontsizeBig);
	protected Font fontBoldBig = new Font(fontfamily, Font.BOLD, (int)fontsizeBig);
	
	private GraphicsWriter writer;	
	
	protected ChartPainter(Chart chart, GraphicsWriter writer) {
		this.chart = chart;
		this.writer = writer;
	}
	
	/**
	 * Get area of interest, i.e. the rectangle with content in it.
	 * @return
	 */
	public Rectangle2D getAOI() {
		return new Rectangle2D.Double(0, 0, widthTotal, heightTotal);
	}

	public GraphicsWriter getWriter() {
		return writer;
	}
}

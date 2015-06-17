package chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.StringTokenizer;

import base.Settings;


public class TextContainer {
	public enum HorizontalAlignment {LEFT, CENTER, RIGHT};
	public enum VerticalAlignment {TOP, CENTER, BOTTOM};
	
	private static float lineHeight = (float)Settings.LINEHEIGHT;

	public static void draw(String text, double x, double y, double width, double height, Font font, Color color,
			HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment,
			Graphics2D g2d) throws NotQuiteFittingException {
		draw(text, x, y, width, height, font, color, horizontalAlignment, verticalAlignment, g2d, false);
	}
	
	public static void draw(String text, double x, double y, double width, double height, Font font, Color color,
			HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment,
			Graphics2D g2d, boolean drawRect) throws NotQuiteFittingException {
		String s = text;
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
		
		Font tempFont = g2d.getFont();
		Color tempColor = g2d.getColor();
		g2d.setFont(font);
		g2d.setColor(color);
		FontMetrics fm = g2d.getFontMetrics();
		
		//stuff it in titles		
		ArrayList<String> sList = new ArrayList<String>();
		
		Rectangle2D bounds = fm.getStringBounds(s, g2d);
		
		String breakToken = "\n";
		
		StringTokenizer breakTokenizer = new StringTokenizer(s, breakToken);
		while(breakTokenizer.hasMoreTokens()) {
			StringTokenizer titleTokenizer = new StringTokenizer(breakTokenizer.nextToken(), " ");
			String currentPhrase = "";		
			while(titleTokenizer.hasMoreTokens()) {
				StringTokenizer dashTokenizer = new StringTokenizer(titleTokenizer.nextToken(), "-");
				while(dashTokenizer.hasMoreTokens()) {
					String nextToken = dashTokenizer.nextToken();
					if (dashTokenizer.hasMoreTokens()) {
						nextToken += "-";
					}
					//if one more fits
					if ((float) fm.getStringBounds(currentPhrase + nextToken, g2d).getWidth() <= rect.getWidth()*0.97) {
						currentPhrase += " " + nextToken;
					} else { //otherwise
						if (!currentPhrase.trim().equals("")) {
							sList.add(currentPhrase.trim());
						}
						currentPhrase = nextToken;
					}
				}
			}
			sList.add(currentPhrase.trim());
		}
		
		for (int i = 0; i < sList.size(); i++) {
			bounds = fm.getStringBounds(sList.get(i), g2d);
			float xCoord = 0;
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				xCoord = (float) rect.getMinX();
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				xCoord = (float) (rect.getMaxX()-bounds.getWidth());
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				xCoord = (float) (rect.getCenterX()-bounds.getWidth()/2);
			}
			float yCoord = 0;
			if (verticalAlignment == VerticalAlignment.TOP) {
				yCoord = (float) (rect.getMinY() + bounds.getHeight()*0.75 + (i)*font.getSize()*lineHeight);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				yCoord = (float) (rect.getMaxY() + bounds.getHeight()*0.75 + (i-sList.size())*font.getSize()*lineHeight);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				yCoord = (float) (rect.getCenterY() + bounds.getHeight()*0.75 + (i-sList.size()/2.)*font.getSize()*lineHeight);
			}			
			
			g2d.drawString(sList.get(i), xCoord, yCoord);
			
			if (drawRect) {
				g2d.draw(rect);
			}
			
			if ((i)*font.getSize()*lineHeight > rect.getHeight()) {
				throw new NotQuiteFittingException("Text doesn't fit into given rectangle: \"" + text + "\"", rect.getHeight(), i*font.getSize()*lineHeight);
			}
		}
		
		
		
		g2d.setColor(tempColor);
		g2d.setFont(tempFont);
	}
}

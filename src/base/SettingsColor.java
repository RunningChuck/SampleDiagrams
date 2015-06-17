package base;

import java.awt.Color;

public class SettingsColor {
	
	public final static Color voidColor = new Color(200,200,200);
	
	public static Color[] getBarColors() {
		Color[] colors = new Color[12];
		colors[0] = new Color(184,148,184);
		colors[1] = new Color(148,148,184);
		colors[2] = new Color(148,184,184);
		colors[3] = new Color(148,184,148);
		colors[4] = new Color(184,184,148);
		colors[5] = new Color(184,148,148);
		
		colors[6] = new Color(184,148,166);
		colors[7] = new Color(166,148,184);
		colors[8] = new Color(148,166,184);
		colors[9] = new Color(148,184,166);
		colors[10] = new Color(166,184,148);
		colors[11] = new Color(184,166,148);

		return colors;
	}
	
	public static Color[] getLineColors() {
		Color[] colors = new Color[7];
		colors[0] = new Color(0, 0, 0);
		colors[1] = new Color(0, 153, 153);
		colors[2] = new Color(153, 153, 153);
		colors[3] = new Color(202, 100, 44);
		colors[4] = new Color(204, 0, 51);
		colors[5] = new Color(0, 153, 255);
		colors[6] = new Color(102, 51, 153);
		return colors;
	}
}

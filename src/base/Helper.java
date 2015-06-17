package base;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Desk for not yet integrated tool methods
 * @author clemens
 *
 */
public class Helper {

	public static String arrToString(String[] s) {
		String temp = "";
		for (int i = 0; i < s.length; i++) {
			temp += s[i];
			if (i < s.length-1)
				temp += "; ";
		}
		return temp;
	}
	
	public static String[] arrListToString(ArrayList<String> list) {
		String[] s = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			s[i] = list.get(i);
		}
		return s;
	}
	
    /**
     * Make a color brighten.
     *
     * @param color Color to make brighten.
     * @param fraction Darkness fraction.
     * @return Lighter color.
     */
    public static Color brighten(Color color, double fraction) {

        int red = (int) Math.round(Math.min(255, color.getRed() * (1-fraction) + 255 * fraction*1.2));
        int green = (int) Math.round(Math.min(255, color.getGreen() * (1-fraction) + 255 * fraction*1.2));
        int blue = (int) Math.round(Math.min(255, color.getBlue() * (1-fraction) + 255 * fraction*1.2));

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);

    }
}

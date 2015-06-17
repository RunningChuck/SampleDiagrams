package chart;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.ext.awt.geom.Polygon2D;

/**
 * Returns Shapes easily
 * 
 * @author clemens
 *
 */
public class ShapeGenerator {
	/**
	 * Returns diamonds
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	private static Polygon2D getShape_diam(double x, double y, double width) {
		width = 0.9 * width;
		float[] xse = { (float) x, (float) (x + width / 2), (float) x,
				(float) (x - width / 2) };
		float[] yse = { (float) (y - width / 2), (float) y,
				(float) (y + width / 2), (float) y };
		return new Polygon2D(xse, yse, 4);
	}

	/**
	 * Returns ellipse (circle)
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	private static Ellipse2D getShape_elli(double x, double y, double width) {
		return new Ellipse2D.Double(x - 0.85 * width / 2, y - 0.85 * width / 2,
				0.85 * width, 0.8 * width);
	}

	/**
	 * Returns rectangles
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	private static Rectangle2D getShape_rect(double x, double y, double width) {
		return new Rectangle2D.Double(x - width / Math.sqrt(2) / 2, y - width
				/ Math.sqrt(2) / 2, width / Math.sqrt(2), width / Math.sqrt(2));
	}

	/**
	 * Returns pentragram (star)
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	private static Polygon2D getShape_pram(double x, double y, double width) {
		double bm = (108 * Math.PI / 180);

		Line2D[] penta = new Line2D[5];
		float[] pentaPx = new float[5];
		float[] pentaPy = new float[5];
		double x_ = x;
		double y_ = y;
		x_ += width / 2;

		for (int i = 0; i < 5; i++) {
			pentaPx[i] = (float) (width * (1 + Math.cos(bm)) + x_ - width);
			pentaPy[i] = (float) (width * (1 + Math.sin(bm)) + y_ - width);
			penta[i] = new Line2D.Double(x_, y_, width * (1 + Math.cos(bm))
					+ x_ - width, width * (1 + Math.sin(bm)) + y_ - width);
			x_ = penta[i].getX2();
			y_ = penta[i].getY2();
			bm -= (144 * Math.PI / 180);
		}
		Line2D line1 = new Line2D.Double(intersect(penta[0], penta[2]),
				new Point2D.Double(pentaPx[3], pentaPy[3]));
		Line2D line2 = new Line2D.Double(intersect(penta[1], penta[3]),
				new Point2D.Double(pentaPx[4], pentaPy[4]));
		Point2D mP = intersect(line1, line2);

		for (int i = 0; i < 5; i++) {
			pentaPx[i] -= (float) mP.getX() - x;
			pentaPy[i] -= (float) mP.getY() - y;
		}

		return new Polygon2D(pentaPx, pentaPy, pentaPx.length);
	}

	/**
	 * Returns pentagons
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	private static Polygon2D getShape_pgon(double x, double y, double width) {
		width = 0.85 * width;
		double bm = (108 * Math.PI / 180);

		Line2D[] penta = new Line2D[5];
		float[] pentaPx = new float[5];
		float[] pentaPy = new float[5];
		double x_ = x;
		double y_ = y;
		x_ += width / 2;

		for (int i = 0; i < 5; i++) {
			pentaPx[i] = (float) (width * (1 + Math.cos(bm)) + x_ - width);
			pentaPy[i] = (float) (width * (1 + Math.sin(bm)) + y_ - width);
			penta[i] = new Line2D.Double(x_, y_, width * (1 + Math.cos(bm))
					+ x_ - width, width * (1 + Math.sin(bm)) + y_ - width);
			x_ = penta[i].getX2();
			y_ = penta[i].getY2();
			bm -= (144 * Math.PI / 180);
		}
		Line2D line1 = new Line2D.Double(intersect(penta[0], penta[2]),
				new Point2D.Double(pentaPx[3], pentaPy[3]));
		Line2D line2 = new Line2D.Double(intersect(penta[1], penta[3]),
				new Point2D.Double(pentaPx[4], pentaPy[4]));
		Point2D mP = intersect(line1, line2);

		for (int i = 0; i < 5; i++) {
			pentaPx[i] -= (float) mP.getX() - x;
			pentaPy[i] -= (float) mP.getY() - y;
		}

		float temp;
		temp = pentaPx[1];
		pentaPx[1] = pentaPx[2];
		pentaPx[2] = pentaPx[4];
		pentaPx[4] = pentaPx[3];
		pentaPx[3] = temp;
		temp = pentaPy[1];
		pentaPy[1] = pentaPy[2];
		pentaPy[2] = pentaPy[4];
		pentaPy[4] = pentaPy[3];
		pentaPy[3] = temp;

		return new Polygon2D(pentaPx, pentaPy, pentaPx.length);
	}

	/**
	 * Returns triangles
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	private static Polygon2D getShape_tria(double x, double y, double width) {
		// width = 0.85*width;
		double height = width / 2 * Math.sqrt(3.);

		float[] xse = { (float) (x - width / 2), (float) (x),
				(float) (x + width / 2) };
		float[] yse = { (float) (y - height / 2),
				(float) (y + height / 2),
				(float) (y - height / 2) };

		return new Polygon2D(xse, yse, 3);
	}

	/**
	 * Find intersection of two lines
	 * @param eins
	 * @param zwei
	 * @return point of intersection
	 */
	private static Point2D.Double intersect(Line2D eins, Line2D zwei) {
		double x11, x12, x21, x22, y11, y12, y21, y22, sx1, sx2, sy1, sy2, sqrt1, sqrt2, a, b;
		x11 = eins.getX1();
		y11 = eins.getY1();
		x12 = eins.getX2();
		y12 = eins.getY2();
		x21 = zwei.getX1();
		y21 = zwei.getY1();
		x22 = zwei.getX2();
		y22 = zwei.getY2();

		sx1 = x12 - x11;
		sy1 = y12 - y11;

		sqrt1 = Math.sqrt((sx1 * sx1) + (sy1 * sy1));
		sx1 /= sqrt1;
		sy1 /= sqrt1;
		sx2 = x22 - x21;
		sy2 = y22 - y21;
		sqrt2 = Math.sqrt((sx2 * sx2) + (sy2 * sy2));
		sx2 /= sqrt2;
		sy2 /= sqrt2;

		a = (sx2 * (y21 - y11) + sy2 * (x11 - x21)) / (sx2 * sy1 - sx1 * sy2);
		b = (sx1 * (y11 - y21) + sy1 * (x21 - x11)) / (sx1 * sy2 - sx2 * sy1);

		if (sy2 == 0)
			return new Point2D.Double(x11 + a * sx1, y11 + a * sy1);
		else
			return new Point2D.Double(x21 + b * sx2, y21 + b * sy2);
	}
	
	/**
	 * Easy selector for shapes (used in all charts using Lines)
	 * @param type 1: diamond; 2: pentagon; 3: star; 4: triangle; 5: ellipse; 6: triangle; 7: rectangle; >7: ellipse
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	public static Shape getShape(int type, double x, double y, float width) {
		if (type == 1) {
			return getShape_diam(x, y, width);
		} else if (type == 2) {
			return getShape_pgon(x, y, width);
		} else if (type == 3) {
			return getShape_pram(x, y, width);
		} else if (type == 4) {
			return getShape_tria(x, y, width);
		} else if (type == 5) {
			return getShape_elli(x, y, width);
		} else if (type == 6) {
			return getShape_tria(x, y, width);
		} else if (type == 7) {
			return getShape_rect(x, y, width);
		} else {
			return getShape_elli(x, y, width);
		}
	}

}

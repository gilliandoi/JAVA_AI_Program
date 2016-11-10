package app;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

/*
 * Vector描く処理
 */
public class ArrowLinePanel extends JPanel {
	// confirm the line position
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;

	/*
	 * Vector描く
	 */
	public static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2) {

		double H = 10; // Arrowの高さ
		double L = 4; // Arrowの幅の1/2
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H);
		double arraow_len = Math.sqrt(L * L + H * H); // Arrowの長さ
		double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
		double x_3 = ex - arrXY_1[0];
		double y_3 = ey - arrXY_1[1];
		double x_4 = ex - arrXY_2[0];
		double y_4 = ey - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		// 線を描く
		g2.drawLine(sx, sy, ex, ey);
		//
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(ex, ey);
		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.closePath();
		// 種類1
		g2.fill(triangle);
		// 種類2
		// g2.draw(triangle);

	}

	// 計算
	public static double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

		double mathstr[] = new double[2];

		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}
}

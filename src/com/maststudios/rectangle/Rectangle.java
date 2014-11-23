package com.maststudios.rectangle;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

public class Rectangle {

	// coordinates of the current position of the rectangle.
	public int top, right, bottom, left;

	// sets the color etc of the rectangle.
	private Paint paint;

	// a value between 0 and 1 describing the percentage of animation that is
	// complete
	private long animationTime;
	private long animationStartTime;

	// coordinates of the location to which the rectangle must be animated to.
	// these are 8 instead of 4 because the location to where the rectangle is
	// being transfered may not be a rectangle. it may be some random figure
	// where the lines may be something other than horizontal or vertical
	/*
	 * vert[0]=tl; clockwise further; [0][0] is x
	 */
	private float[][] vert;
	private float[][] vert1;

	public Rectangle(Paint paint, int top, int right, int bottom, int left) {
		animationTime = 100;
		this.paint = paint;
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		vert = new float[4][2];
		vert1 = new float[4][2];
		this.vert[0][0] = left;
		this.vert[0][1] = top;
		this.vert[1][0] = right;
		this.vert[1][1] = top;
		this.vert[2][0] = right;
		this.vert[2][1] = bottom;
		this.vert[3][0] = left;
		this.vert[3][1] = bottom;

		this.vert1[0][0] = left;
		this.vert1[0][1] = top;
		this.vert1[1][0] = right;
		this.vert1[1][1] = top;
		this.vert1[2][0] = right;
		this.vert1[2][1] = bottom;
		this.vert1[3][0] = left;
		this.vert1[3][1] = bottom;
		// System.out.println(tl_x + " " + tl_y + " " + tr_x + " " + tr_y + " "
		// + bl_x + " " + bl_y + " " + br_x + " " + br_y);
	}

	// returns true if the animation is ongoing false if the animation is over
	public boolean draw(Canvas canvas, Matrix matrix) {
		float fraction = (float) (((double) System.currentTimeMillis() - (double) animationStartTime) / (double) animationTime);
		// System.out.println(fraction);
		float vertex[][] = new float[4][2];
		System.out.println(fraction+"--------"+(2*Math.pow(fraction, 2)-4*Math.pow(fraction, 3)/3));
		for (int i = 0; i < 4; i++) {
			vertex[i][0] = (float) (vert[i][0] + (vert1[i][0] - vert[i][0]) * 3*(2*Math.pow(fraction, 2)-4*Math.pow(fraction, 3)/3)/2);
			vertex[i][1] = (float) (vert[i][1] + (vert1[i][1] - vert[i][1]) * 3*(2*Math.pow(fraction, 2)-4*Math.pow(fraction, 3)/3)/2);
		}
		Path path = new Path();
		path.reset();
		path.moveTo(vertex[0][0], vertex[0][1]);
		path.lineTo(vertex[1][0], vertex[1][1]);
		path.lineTo(vertex[2][0], vertex[2][1]);
		path.lineTo(vertex[3][0], vertex[3][1]);
		path.close();
		path.transform(matrix);

		if (System.currentTimeMillis() >= animationStartTime + animationTime) {
			// things to do when the animation ends
			for (int i = 0; i < 4; i++) {
				vert[i][0] = vert1[i][0];
				vert[i][1] = vert1[i][1];
			}
			// moving to exact final position in after the animation ends
			path.reset();
			path.moveTo(vert1[0][0], vert1[0][1]);
			path.lineTo(vert1[1][0], vert1[1][1]);
			path.lineTo(vert1[2][0], vert1[2][1]);
			path.lineTo(vert1[3][0], vert1[3][1]);
			path.close();
			path.transform(matrix);
			canvas.drawPath(path, paint);
			return false;
		}
		canvas.drawPath(path, paint);
		return true;

	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
		this.vert1[0][1] = top;
		this.vert1[1][1] = top;
		animationStartTime = System.currentTimeMillis();
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
		this.vert1[1][0] = right;
		this.vert1[2][0] = right;
		animationStartTime = System.currentTimeMillis();
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
		this.vert1[2][1] = bottom;
		this.vert1[3][1] = bottom;
		animationStartTime = System.currentTimeMillis();
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
		this.vert1[3][0] = left;
		this.vert1[0][0] = left;
		animationStartTime = System.currentTimeMillis();
	}

	public void setImpossibleVertices() {

	}
}

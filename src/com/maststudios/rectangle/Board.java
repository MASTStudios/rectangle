package com.maststudios.rectangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//TODO make a class that converts array dimensions to display dimensions

public class Board extends SurfaceView implements GestureDetector.OnGestureListener, SurfaceHolder.Callback, Runnable {

	/**
	 * variables related to the game
	 **/

	/*
	 * grid:- A 2 dimensional boolean matrix that would contain true if a dot
	 * exists anda false if it does not. To improve performance this could be
	 * changed to alist containing the location of the dots. That done at a
	 * later stage wouldrequire major redesign
	 * 
	 * The grid is of the form grid[x-coordinate][y-coordinate]
	 */

	public Boolean[][] grid;
	private Rectangle rectangle;
	private Rectangle goal;
	private int gridHeight, gridWidth;
	private GestureDetectorCompat mDetector;
	private SurfaceHolder surfaceHolder;
	private int screenwidth, screenheight;
	private float paddingPercentage;

	// grid drawing dimension attributes
	float dotRadius, vdis, hdis, top, left, hpad, vpad, strokeWidth;
	Matrix matrix;

	public Board(Context context) {
		super(context);

		// set surfaceholder callback
		getHolder().addCallback(this);

		// setting display attributes.
		// these should be set from the level file
		paddingPercentage = 10;
		//dotradius and linewidth should be dependent on screenresolution
		dotRadius = 10;
		strokeWidth=7;
		matrix = new Matrix();

		mDetector = new GestureDetectorCompat(context, this);

		gridHeight = 6;
		gridWidth = 6;

		// setting the goal rectangle
		Paint goalPaint = new Paint();
		// TODO adhoc - this color should be set by the level file
		goalPaint.setColor(getResources().getColor(R.color.green));
		goalPaint.setStyle(Style.STROKE);
		goalPaint.setStrokeWidth(strokeWidth);
		goal = new Rectangle(goalPaint, 4, 5, 5, 4);

		// setting the initial position of the rectangle
		Paint rectPaint = new Paint();
		// TODO adhoc - this color should be set by the level file
		rectPaint.setColor(getResources().getColor(R.color.blue));
		rectPaint.setStyle(Style.STROKE);
		rectPaint.setStrokeWidth(strokeWidth);
		rectangle = new Rectangle(rectPaint, 3, 4, 4, 3);

		// setting the initial position of the grid
		// TODO in ideal conditions this should be read from a file containing
		// the details of the level. the file may also contain information lis
		// colors of dots and rectangles.

		grid = new Boolean[gridWidth][gridHeight];

		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				grid[i][j] = false;
			}
		}

		grid[0][0] = true;
		grid[0][5] = true;
		grid[0][1] = true;
		grid[1][5] = true;
		grid[1][1] = true;
		grid[5][1] = true;
		grid[1][0] = true;
		grid[5][0] = true;
		grid[4][0] = true;
		grid[5][2] = true;
		grid[4][1] = true;
		grid[2][1] = true;
		grid[2][0] = true;
		grid[4][3] = true;
		grid[2][3] = true;
		grid[1][2] = true;
		grid[0][2] = true;
		grid[0][3] = true;
		grid[1][3] = true;
		grid[2][4] = true;
		grid[4][4] = true;
		grid[4][5] = true;
		grid[2][5] = true;
		grid[5][4] = true;
		grid[5][5] = true;
		grid[3][3] = true;
		grid[3][4] = true;
		grid[1][4] = true;
		grid[0][4] = true;
	}

	/*
	 * moves the rectangle. and initiates animation Directions:- 0-top 1-right
	 * 2-bottom 3-left
	 */
	public void move(int direction) {
		// check if the move is possible
		if (direction == 0) { // top
			int top = rectangle.getTop();
			boolean possible = true;
			top--;
			while (true) {
				if (top < 0 || grid[rectangle.getLeft()][top] != grid[rectangle.getRight()][top]) {
					possible = false;
					break;
				} else if (grid[rectangle.getLeft()][top]) {
					possible = true;
					break;
				} else {
					top--;
				}
			}
			if (possible) {
				rectangle.setBottom(rectangle.getTop());
				rectangle.setTop(top);
			}
		} else if (direction == 1) { // right
			int right = rectangle.getRight();
			boolean possible = true;
			right++;
			while (true) {
				if (right >= gridWidth || grid[right][rectangle.getBottom()] != grid[right][rectangle.getTop()]) {
					possible = false;
					break;
				} else if (grid[right][rectangle.getBottom()]) {
					possible = true;
					break;
				} else {
					right++;
				}
			}
			if (possible) {
				rectangle.setLeft(rectangle.getRight());
				rectangle.setRight(right);
			}
		} else if (direction == 2) { // bottom
			int bottom = rectangle.getBottom();
			boolean possible = true;
			bottom++;
			while (true) {
				if (bottom >= gridHeight || grid[rectangle.getLeft()][bottom] != grid[rectangle.getRight()][bottom]) {
					possible = false;
					break;
				} else if (grid[rectangle.getLeft()][bottom]) {
					possible = true;
					break;
				} else {
					bottom++;
				}
			}
			if (possible) {
				rectangle.setTop(rectangle.getBottom());
				rectangle.setBottom(bottom);
			}
		} else if (direction == 3) { // left
			int left = rectangle.getLeft();
			boolean possible = true;
			left--;
			while (true) {
				if (left < 0 || grid[left][rectangle.getBottom()] != grid[left][rectangle.getTop()]) {
					possible = false;
					break;
				} else if (grid[left][rectangle.getBottom()]) {
					possible = true;
					break;
				} else {
					left--;
				}
			}
			if (possible) {
				rectangle.setRight(rectangle.getLeft());
				rectangle.setLeft(left);
			}
		}
	}

	// returns the output onf the draw rectangle function
	public boolean drawBoard() {
		boolean ret;
		// TODO surfaceHolder may be defined as a global variable
		// System.out.println(matrix);
		Canvas canvas = surfaceHolder.lockCanvas();
		Paint paint = new Paint();

		// TODO get the colors from the level file
		canvas.drawColor(getResources().getColor(R.color.white));

		paint.setColor(getResources().getColor(R.color.grey));

		// drawing dots
		// setting scale and offset
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				if (grid[i][j]) {
					canvas.drawCircle(left + i * hdis, top + vdis * j, dotRadius, paint);
				}
			}
		}

		// drawing the goal rectangle
		goal.draw(canvas, matrix);

		// drawing the rectangle
		ret = rectangle.draw(canvas, matrix);

		// completing drawing process
		surfaceHolder.unlockCanvasAndPost(canvas);

		return ret;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		float dx, dy;
		dx = e2.getX() - e1.getX();
		dy = e2.getY() - e1.getY();
		// System.out.println(matrix);
		if (Math.abs(dx) >= Math.abs(dy)) { // horizontal
			if (dx < 0) { // left
				move(3);
			} else { // right
				move(1);
			}

		} else { // vertical
			if (dy < 0) {// up
				move(0);
			} else {// down
				move(2);
			}
		}
		new Thread(this).start();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		screenwidth = MeasureSpec.getSize(widthMeasureSpec);
		screenheight = MeasureSpec.getSize(heightMeasureSpec);
		// quarter of the screen will be used to display data;
		top = ((float) screenheight) / 4 + paddingPercentage / 100 * screenheight;
		left = paddingPercentage / 100 * screenwidth;
		vdis = (screenheight - paddingPercentage / 100 * screenheight - top) / (gridHeight - 1);
		hdis = (screenwidth - paddingPercentage / 100 * screenwidth * 2) / (gridWidth - 1);
		// System.out.println("settings " + hdis + " ##  " + vdis + " ##  " +
		// top + " ##  " + left);
		setMeasuredDimension(screenwidth, screenheight);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// setting screenHeight and screenWidht
		surfaceHolder = holder;
		matrix.postScale(hdis, vdis);
		matrix.postTranslate(left, top);
		drawBoard();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		while (drawBoard() == true) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {

			}
		}
	}

}

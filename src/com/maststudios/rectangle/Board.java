package com.maststudios.rectangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//TODO make a class that converts array dimensions to display dimensions

public class Board extends SurfaceView implements GestureDetector.OnGestureListener, SurfaceHolder.Callback {

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
	private boolean animating;
	private int gridHeight, gridWidth;
	private GestureDetectorCompat mDetector;
	private SurfaceHolder surfaceHolder;
	private int screenWidth,screenHeight;

	public Board(Context context) {
		super(context);

		//set surfaceholder callback
		getHolder().addCallback(this);
		
		mDetector = new GestureDetectorCompat(context, this);

		gridHeight = 6;
		gridWidth = 6;

		// setting the goal rectangle

		goal = new Rectangle();
		goal.setTop(4);
		goal.setBottom(5);
		goal.setLeft(4);
		goal.setRight(5);

		// setting the initial position of the rectangle
		rectangle = new Rectangle();
		rectangle.setTop(3);
		rectangle.setBottom(4);
		rectangle.setLeft(3);
		rectangle.setRight(4);

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

	public void drawBoard() {
		// TODO surfaceHolder may be defined as a global variable
		Canvas canvas = surfaceHolder.lockCanvas();
		Paint paint = new Paint();

		// clearing screen
		paint.setColor(getResources().getColor(android.R.color.black));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(0, 0, 600, 600, paint);

		paint.setColor(getResources().getColor(android.R.color.holo_blue_dark));

		// drawing dots
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				System.out.println(grid[i][j]);
				if (grid[i][j]) {
					canvas.drawCircle(100 * i, 100 * j, 5, paint);
				}
			}
		}

		// drawing the goal rectangle
		paint.setColor(getResources().getColor(android.R.color.holo_green_light));
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(goal.getLeft() * 100, goal.getTop() * 100, goal.getRight() * 100, goal.getBottom() * 100, paint);

		// drawing the rectangle
		paint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rectangle.getLeft() * 100, rectangle.getTop() * 100, rectangle.getRight() * 100, rectangle.getBottom() * 100, paint);

		// completing drawing process
		surfaceHolder.unlockCanvasAndPost(canvas);
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

		if (Math.abs(dx) >= Math.abs(dy)) { // horizontal
			if (dx < 0) { // left
				move(3);
				drawBoard();
			} else { // right
				move(1);
				drawBoard();
			}

		} else { // vertical
			if (dy < 0) {// up
				move(0);
				drawBoard();
			} else {// down
				move(2);
				drawBoard();
			}
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//setting screenHeight and screenWidht
		screenHeight=
		surfaceHolder = holder;
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

}

package com.example.remotecontrol;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MouseSimulatorUI extends View implements OnTouchListener {
	private boolean leftKeyPressed;
	private boolean rightKeyPressed;

	private boolean touchDown;
	private int lastX;
	private int lastY;

	private int w;
	private int h;

	private long lastTouchDownTimeInMS;

	private Paint paint;
	private boolean initialized;
	private Rect leftButton;
	private Rect rightButton;
	private Rect touchPad;
	private MouseSimulator mouseSimulator;

	public MouseSimulatorUI(Context context, MouseSimulator mouseSimulator) {
		super(context);
		this.mouseSimulator = mouseSimulator;
		touchDown = false;
		w = h = 0;

		this.setOnTouchListener(this);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		w = getWidth();
		h = getHeight();

		if (leftButton == null) {
			int buttonHeight = 100;
			leftButton = new Rect(5, h - buttonHeight, w / 2 - 5, h - 5);
			rightButton = new Rect(w / 2 + 5, h - buttonHeight, w - 5, h - 5);
			touchPad = new Rect(0, 0, w, h - buttonHeight - 5);
		}

		// Log.e("", w + ":" + h);

		paint.setColor(Color.LTGRAY);
		canvas.drawRect(0, 0, w, h, paint);

		paint.setColor(Color.DKGRAY);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(leftButton, paint);
		canvas.drawRect(rightButton, paint);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// Log.e("", "Touch event");

		int curX = (int) event.getX();
		int curY = (int) event.getY();

		int diffX = curX - lastX;
		int diffY = curY - lastY;

		long curTime = System.currentTimeMillis();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastTouchDownTimeInMS = curTime;
			touchDown = true;

			break;

		case MotionEvent.ACTION_UP:
			if (curTime - lastTouchDownTimeInMS < 80 && Math.abs(diffX) < 10
					&& Math.abs(diffY) < 10) {
				// clicked in less than a second
				// Log.e("timediff", "" + (curTime - lastTouchDownTimeInMS));

				if (inside(curX, curY, leftButton)
						|| inside(curX, curY, touchPad)) {

					sendLeftClick();
				} else if (inside(curX, curY, rightButton)) {
					sendRightClick();
				}
			}
			touchDown = false;
			break;

		case MotionEvent.ACTION_MOVE:
			if (touchDown) {
				if (curX > .9 * w && lastX > .9 * w) {
					mouseScrollBy(diffY);
				} else if (curY > .95 * h && lastY > .95 * h) {
					mouseScrollBy(diffX);
				} else {
					mouseMoveBy(2 * diffX, 2 * diffY);
				}
			}
			break;

		default:
			break;
		}

		lastX = curX;
		lastY = curY;
		return true;
	}

	private void sendRightClick() {
		// Log.e("", "Right click");
		try {
			mouseSimulator.simulateClick(MouseSimulator.Button.RIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendLeftClick() {
		// Log.e("", "Left click");
		try {
			mouseSimulator.simulateClick(MouseSimulator.Button.LEFT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void mouseMoveBy(int dx, int dy) {
		// Log.e("", "move " + i + ":" + j);
		try {
			mouseSimulator.simulateCursorMoveBy((short) dx, (short) dy);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void mouseScrollBy(int ds) {
		// Log.e("", "move " + i + ":" + j);
		try {
			mouseSimulator.simulateScrollBy((short) ds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean inside(int x, int y, Rect rect) {
		if (x >= rect.left && x <= rect.right && y >= rect.top
				&& y <= rect.bottom)
			return true;
		return false;
	}

}

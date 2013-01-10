package com.example.remotecontrol;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.remotecontrol.simulators.MouseSimulator;
import com.example.remotecontrol.simulators.MouseSimulator.Button;

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

	private int lastPressedX;
	private int lastPressedY;
	private int lastX;
	private int lastY;
	private int curX;
	private int curY;

	private int w;
	private int h;

	private enum TouchMode {
		NULL, CLICK, DRAG
	};

	private TouchMode touchMode;
	private long lastTouchDownTimeInMS;

	private boolean uiInitialized;
	private Paint paint;
	private Rect leftButtonArea;
	private Rect rightButtonArea;
	private Rect touchpadArea;
	private Rect verticalScrollArea;
	private Rect horizontalScrollArea;
	private MouseSimulator mouseSimulator;

	private Timer timer;
	private TimerTask timerTask;

	public MouseSimulatorUI(Context context, MouseSimulator mouseSimulator) {
		super(context);
		this.mouseSimulator = mouseSimulator;

		touchMode = TouchMode.NULL;
		lastTouchDownTimeInMS = 0;
		uiInitialized = false;
		w = h = -1;

		timer = new Timer("ClickEventSendingTimer");
		Log.e("Initializing size", getWidth() + ", " + getHeight());

		this.setOnTouchListener(this);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (!uiInitialized) {
			w = getWidth();
			h = getHeight();

			int buttonHeight = 60;
			leftButtonArea = new Rect(5, h - buttonHeight, w / 2 - 5, h - 5);
			rightButtonArea = new Rect(w / 2 + 5, h - buttonHeight, w - 5,
					h - 5);
			touchpadArea = new Rect(0, 0, w, h - buttonHeight - 5);
			horizontalScrollArea = new Rect(touchpadArea.left,
					touchpadArea.bottom - buttonHeight / 3, touchpadArea.right,
					touchpadArea.bottom);
			verticalScrollArea = new Rect(
					touchpadArea.right - buttonHeight / 3, touchpadArea.top,
					touchpadArea.right, horizontalScrollArea.top);

			uiInitialized = true;
		}

		// Log.e("", w + ":" + h);

		paint.setColor(Color.LTGRAY);
		canvas.drawRect(0, 0, w, h, paint);

		paint.setColor(Color.DKGRAY);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(leftButtonArea, paint);
		canvas.drawRect(rightButtonArea, paint);

		paint.setColor(Color.GRAY);
//		canvas.drawRoundRect(new RectF(horizontalScrollArea), 10, 10, paint);
		canvas.drawRoundRect(new RectF(verticalScrollArea), 10, 10, paint);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {

		curX = (int) event.getX();
		curY = (int) event.getY();

		long curTime = System.currentTimeMillis();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (touchMode == TouchMode.NULL) {
				touchMode = TouchMode.CLICK;
			} else if (touchMode == TouchMode.DRAG) {
				if (curTime - lastTouchDownTimeInMS < Constants.TouchClickTimeInMS) {
					cancelClickTimer();
					sendLeftPress();
					// pr("Timer cancel: pressing left");
				}
			}

			lastTouchDownTimeInMS = curTime;
			lastPressedX = curX;
			lastPressedY = curY;
			break;

		case MotionEvent.ACTION_UP:
			boolean releasedOnWherePressed = isReleasedOnWhereLastPressed();

			if (releasedOnWherePressed) {
				if (isInside(lastPressedX, lastPressedY, leftButtonArea)) {
					sendLeftClick();
				} else if (isInside(lastPressedX, lastPressedY, rightButtonArea)) {
					sendRightClick();
				}
			}

			if (isInside(lastPressedX, lastPressedY, touchpadArea)) {

				if (touchMode == TouchMode.CLICK) {
					if (releasedOnWherePressed) {
						startClickTimer();
						touchMode = TouchMode.DRAG;
					} else {
						touchMode = TouchMode.NULL;
					}
				} else if (touchMode == TouchMode.DRAG) {
					touchMode = TouchMode.NULL;
					sendLeftRelease();
					// pr("releasing left");
				} else {
					touchMode = TouchMode.NULL;
				}

			}
			break;

		case MotionEvent.ACTION_MOVE:
			int diffX = curX - lastX;
			int diffY = curY - lastY;

			if (isInside(curX, curY, touchpadArea)) {
				if (touchMode == TouchMode.CLICK) {
					if (isInside(lastPressedX, lastPressedY, verticalScrollArea)) {
						mouseScrollVerticalBy(diffY);
						// pr("ScrollY " + diffY + "");
					} else if (isInside(lastPressedX, lastPressedY,
							horizontalScrollArea)) {
						mouseScrollHorizontalBy(diffX);
						// pr("ScrollX " + diffX + "");
					} else {
						mouseMoveBy(diffX, diffY);
						// pr("Moving " + diffX + "," + diffY);
					}
				} else if (touchMode == TouchMode.DRAG) {
					mouseMoveBy(diffX, diffY);
					// pr("Dragging " + diffX + "," + diffY);
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

	private void startClickTimer() {
		timerTask = new TimerTask() {

			@Override
			public void run() {
				sendLeftClick();
				// pr("Inside Timer: Clicking left");
				touchMode = TouchMode.NULL;
			}
		};
		timer.schedule(timerTask, Constants.TouchClickTimeInMS);
	}

	public void cancelClickTimer() {
		if (timerTask != null)
			timerTask.cancel();
	}

	public void pr(String s) {
		Log.e("" + touchMode, s);
	}

	public boolean isReleasedOnWhereLastPressed() {
		return Math.abs(curX - lastPressedX) < 10
				&& Math.abs(curY - lastPressedY) < 10;
	}

	public boolean isLastPressedOn(Rect rect) {
		return isInside(lastPressedX, lastPressedY, rect);
	}

	public boolean isInside(int x, int y, Rect rect) {
		if (x >= rect.left && x <= rect.right && y >= rect.top
				&& y <= rect.bottom)
			return true;
		return false;
	}

	private void sendLeftPress() {
		// Log.e("", "Left Press");
		try {
			mouseSimulator.simulatePress(Button.LEFT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendLeftRelease() {
		// Log.e("", "Left Release");
		try {
			mouseSimulator.simulateRelease(Button.LEFT);
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

	private void sendRightClick() {
		// Log.e("", "Right click");
		try {
			mouseSimulator.simulateClick(MouseSimulator.Button.RIGHT);
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

	private void mouseScrollVerticalBy(int ds) {
		// Log.e("", "move " + i + ":" + j);
		try {
			mouseSimulator.simulateVerticalScrollBy((short) ds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void mouseScrollHorizontalBy(int ds) {
		// Log.e("", "move " + i + ":" + j);
		try {
			mouseSimulator.simulateHorizontalScrollBy((short) ds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

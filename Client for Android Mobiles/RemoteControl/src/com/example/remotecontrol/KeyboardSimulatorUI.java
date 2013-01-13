package com.example.remotecontrol;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.remotecontrol.simulators.KeyboardSimulator;

public class KeyboardSimulatorUI extends LinearLayout implements
		OnTouchListener {
	private int lastPressedX;
	private int lastPressedY;
	private int curX;
	private int curY;
	private Timer timer;
	private TimerTask timerTask;
	private boolean keyRepeating;
	private int curKey;

	private ImageView iv;

	private KeyboardSimulator keyboardSimulator;

	public KeyboardSimulatorUI(Context context,
			KeyboardSimulator keyboardSimulator) {
		super(context);
		this.keyboardSimulator = keyboardSimulator;

		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);

		Constants.scaleKeyboardMap();
		iv = new ImageView(getContext()) {
			@Override
			public void draw(Canvas canvas) {
				super.draw(canvas);
				Log.e("", "zoomlevel: " + Constants.keyboardZoomLevel);
				Log.e("", "bitmap : " + Constants.keyBoardWidthPx + ", "
						+ Constants.keyBoardHeightPx);
				Log.e("", "IV: " + getWidth() + ", " + getHeight());

				Paint p = new Paint();
				p.setColor(Color.GREEN);
				p.setStyle(Style.STROKE);
				for (Integer i : Constants.keyboardMap.keySet()) {
					if (i == 'A')
						Log.e("", "A: " + ((char) i.intValue()) + ": "
								+ Constants.keyboardMap.get(i));
					canvas.drawRect(Constants.keyboardMap.get(i), p);
				}

				p.setColor(Color.RED);
				canvas.drawRect(
						new Rect(1, 1, getWidth() - 1, getHeight() - 1), p);
			}
		};

		ScrollView sv = new ScrollView(getContext());
		sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		HorizontalScrollView hsv = new HorizontalScrollView(getContext());
		hsv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		hsv.addView(iv);
		sv.addView(hsv);
		this.addView(sv);

		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.keyboard_short,
				options);
		Constants.keyBoardWidthPx = options.outWidth;
		Constants.keyBoardHeightPx = options.outHeight;

		iv.setMaxHeight(100000);
		iv.setMaxWidth(100000);
		ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
		layoutParams.width = (int) (Constants.keyBoardWidthPx * Constants.keyboardZoomLevel);
		layoutParams.height = (int) (Constants.keyBoardHeightPx * Constants.keyboardZoomLevel);
		iv.setLayoutParams(layoutParams);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setImageResource(R.drawable.keyboard_short);
		iv.setOnTouchListener(this);
		adjustKeyboardRotation();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// Log.e("", event.getX() + "," + event.getY());
		curX = (int) event.getX();
		curY = (int) event.getY();

		if (!Constants.horizontalKeyboard && curX != curY) {
			curX ^= curY ^= curX ^= curY;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (Constants.repeatingKeyEnabled) {
				startKeyPressTimer();
			}

			lastPressedX = curX;
			lastPressedY = curY;
			break;

		case MotionEvent.ACTION_UP:
			if (isReleasedOnWhereLastPressed()) {
				Log.e("", "KEY PRESSED");

				Set<Integer> keySet = Constants.keyboardMap.keySet();
				for (Integer key : keySet) {
					Rect rect = Constants.keyboardMap.get(key);
					if (inside(curX, curY, rect)) {
						if (Constants.repeatingKeyEnabled) {
							cancelKeypressTimer();
						} else {
							simulateKeyType(VirtualKey.VK_0);
						}
					}
				}
			}

			break;

		case MotionEvent.ACTION_MOVE:
			break;
		}

		return true;
	}

	public boolean isReleasedOnWhereLastPressed() {
		return Math.abs(curX - lastPressedX) < 10
				&& Math.abs(curY - lastPressedY) < 10;
	}

	private void startKeyPressTimer() {
		timerTask = new TimerTask() {

			@Override
			public void run() {
				try {
					while (keyRepeating) {
						simulateKeyType(curKey);
						Thread.sleep(Constants.keyboardPressDelay);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		timer.schedule(timerTask, 0);
	}

	private void cancelKeypressTimer() {
		keyRepeating = false;
		if (timerTask != null)
			timerTask.cancel();
	}

	private void adjustKeyboardRotation() {
		Matrix matrix = new Matrix();
		if (!Constants.horizontalKeyboard) {
			matrix.postRotate(-90);
		}
		iv.setImageMatrix(matrix);
	}

	public boolean inside(float x, float y, Rect rect) {
		if (x >= rect.left && x <= rect.right && y >= rect.top
				&& y <= rect.bottom)
			return true;
		return false;
	}

	public void simulateKeyType(int virtualKey) {
		try {
			keyboardSimulator.simulateKeyType(virtualKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void simulateKeyPress(int virtualKey) {
		try {
			keyboardSimulator.simulateKeyPress(virtualKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void simulateKeyRelease(int virtualKey) {
		try {
			keyboardSimulator.simulateKeyRelease(virtualKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

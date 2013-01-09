package com.example.remotecontrol;

import java.io.IOException;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class KeyboardSimulatorUI extends LinearLayout {
	private int lastX;
	private int lastY;
	private long lastTouchedInMS;
	private float imageScale;

	private KeyboardSimulator keyboardSimulator;

	public KeyboardSimulatorUI(Context context,
			final KeyboardSimulator keyboardSimulator) {
		super(context);
		this.keyboardSimulator = keyboardSimulator;

		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);

		final ImageView iv = new ImageView(getContext()) {
			@Override
			public void draw(Canvas canvas) {
				super.draw(canvas);
				Paint p = new Paint();
				p.setColor(Color.GREEN);
				p.setStyle(Style.STROKE);
				for (Integer i : Constants.keyboardMap.keySet()) {
					canvas.drawRect(Constants.keyboardMap.get(i), p);
				}

				p.setColor(Color.RED);
				canvas.drawRect(
						new Rect(1, 1, getWidth() - 1, getHeight() - 1), p);
			}
		};
		iv.setLayoutParams(new LayoutParams(2*Constants.keyBoardWidthPx, 2*Constants.keyBoardHeightPx));
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setImageResource(R.drawable.keyboard_long);

		// final ImageView iv = (ImageView) hsv.findViewById(R.id.iv_keyboard);
		// float aspectRatio = Constants.keyBoardWidthPx /
		// Constants.keyBoardHeightPx;
		// Canvas c = new Canvas();
		// Paint p = new Paint();
		// p.setColor(Color.GREEN);
		// p.setStyle(Style.FILL_AND_STROKE);
		// for (Integer i : Constants.keyboardMap.keySet()) {
		// c.drawRect(Constants.keyboardMap.get(i), p);
		// c.drawRect(new Rect(0, 0, 220, 500), p);
		// }
		// iv.draw(c);

		ScrollView sv = new ScrollView(getContext());
		sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		

		HorizontalScrollView hsv = new HorizontalScrollView(getContext());
		hsv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		hsv.addView(iv);
		sv.addView(hsv);
		this.addView(sv);

		imageScale = Constants.keyBoardWidthPx
				/ iv.getDrawable().getIntrinsicWidth();
		Log.e("ImageView", iv.getDrawable().getIntrinsicWidth() + ","
				+ iv.getDrawable().getIntrinsicHeight());
		iv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// Log.e("", event.getX() + "," + event.getY());
				int curX = (int) event.getX();
				int curY = (int) event.getY();

				long curTime = System.currentTimeMillis();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastTouchedInMS = curTime;
					break;

				case MotionEvent.ACTION_UP:
					if (curTime - lastTouchedInMS < 70
							&& Math.abs(curY - lastY) < 10
							&& Math.abs(curX - lastX) < 10) {
						Log.e("", "KEY PRESSED");

						Set<Integer> keySet = Constants.keyboardMap.keySet();
						for (Integer key : keySet) {
							Rect rect = Constants.keyboardMap.get(key);
							if (inside(curX, curY, rect)) {
								try {
									keyboardSimulator
											.simulateKeyType(VirtualKey.VK_0);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}

					break;

				case MotionEvent.ACTION_MOVE:
					lastTouchedInMS = curTime;
					break;
				}

				lastX = curX;
				lastY = curY;

				return true;
			}
		});
	}

	public boolean inside(float x, float y, Rect rect) {
		x *= imageScale;
		y *= imageScale;
		if (x >= rect.left && x <= rect.right && y >= rect.top
				&& y <= rect.bottom)
			return true;
		return false;
	}

}

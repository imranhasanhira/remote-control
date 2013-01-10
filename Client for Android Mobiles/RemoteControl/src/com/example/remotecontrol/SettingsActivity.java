package com.example.remotecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private static final String KEY_CLICK_SPEED = "click_speed";
	private static final String KEY_HORIZONTAL_KEYBOARD = "horizontal_keyboard";
	private static final String KEY_KEYBOARD_ZOOM_LEVEL = "keyboard_zoom_level";

	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);
		sharedPreferences = getSharedPreferences(
				Constants.SHARED_PREFERRENCE_NAME, Context.MODE_PRIVATE);

		initSettingsUI();

	}

	public static void reloadConstantsValues(SharedPreferences pref) {
		Constants.TouchClickTimeInMS = pref.getInt(KEY_CLICK_SPEED, 150);
		Constants.horizontalKeyboard = pref.getBoolean(KEY_HORIZONTAL_KEYBOARD,
				true);		
		Constants.keyboardZoomLevel = pref.getFloat(KEY_KEYBOARD_ZOOM_LEVEL, 1F);
	}

	private void saveSettings() {
		Editor editor = sharedPreferences.edit();

		int clickSpeed = ((SeekBar) findViewById(R.id.click_speed_seekbar))
				.getProgress();
		editor.putInt(KEY_CLICK_SPEED, clickSpeed);
		SeekBar keyboarZoomLevelSeekbar = ((SeekBar) findViewById(R.id.keyboard_zoom_level_seekbar));
		float keyboardZoomLevel = (float) (keyboarZoomLevelSeekbar
				.getProgress() / 100.0);
		editor.putFloat(KEY_KEYBOARD_ZOOM_LEVEL, keyboardZoomLevel);

		boolean horizontalKeyboard = ((CheckBox) findViewById(R.id.keyboard_horizontal_checkbox))
				.isChecked();
		editor.putBoolean(KEY_HORIZONTAL_KEYBOARD, horizontalKeyboard);

		editor.commit();

		reloadConstantsValues(sharedPreferences);

		Toast.makeText(getApplicationContext(), "Settings Saved",
				Toast.LENGTH_SHORT).show();

	}

	private void initSettingsUI() {
		SeekBar clickSpeedSeekbar = (SeekBar) findViewById(R.id.click_speed_seekbar);
		clickSpeedSeekbar.setProgress(sharedPreferences.getInt(KEY_CLICK_SPEED,
				150));

		SeekBar keyboarZoomLevelSeekbar = (SeekBar) findViewById(R.id.keyboard_zoom_level_seekbar);
		keyboarZoomLevelSeekbar.setProgress((int) (sharedPreferences.getFloat(
				KEY_KEYBOARD_ZOOM_LEVEL, 1) * 100));

		CheckBox horizontalKeyboardCheckbox = ((CheckBox) findViewById(R.id.keyboard_horizontal_checkbox));
		horizontalKeyboardCheckbox.setChecked(sharedPreferences.getBoolean(
				KEY_HORIZONTAL_KEYBOARD, true));

		Button saveButton = (Button) findViewById(R.id.save_settings_button);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveSettings();
			}
		});

	}
}

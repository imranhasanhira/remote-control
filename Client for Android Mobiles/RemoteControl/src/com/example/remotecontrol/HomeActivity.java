package com.example.remotecontrol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.example.remotecontrol.simulators.KeyboardSimulator;
import com.example.remotecontrol.simulators.MouseSimulator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends Activity {
	// private static final int REQUEST_CODE_SETTINGS_UI = 0;

	private enum Mode {
		HOME, MOUSE, KEYBOARD
	};

	private Mode mode;
	private Socket socket;
	MouseSimulatorUI mouseSimulatorUI;
	KeyboardSimulatorUI keyboardSimulatorUI;
	private boolean connectedToServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 Editor editor =
		 getSharedPreferences(Constants.SHARED_PREFERRENCE_NAME,
		 Context.MODE_PRIVATE).edit();
		 editor.remove("keyboard_zoom_level");
		 editor.putFloat("keyboard_zoom_level", 1);
		 editor.commit();
		Constants.initKeyboardMap();
		SettingsActivity.reloadConstantsValues(getSharedPreferences(
				Constants.SHARED_PREFERRENCE_NAME, Context.MODE_PRIVATE));

		setConnectedToServer(false);
		mode = Mode.HOME;

	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			switch (mode) {
			case KEYBOARD:
				startKeyboardSimulation();
				break;

			case MOUSE:
				startMouseSimulation();
				break;

			case HOME:
			default:
				prepareHome();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			prepareHome();
		}

	}

	private void prepareHome() {
		setContentView(R.layout.activity_home);

		AsyncTask<String, Integer, String> selfIPFindingTask = new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... arg0) {
				WifiManager wim = (WifiManager) getSystemService(WIFI_SERVICE);
				return Formatter.formatIpAddress(wim.getConnectionInfo()
						.getIpAddress());
				// try {
				// String ips = "";
				// for (Enumeration<NetworkInterface> en = NetworkInterface
				// .getNetworkInterfaces(); en.hasMoreElements();) {
				// NetworkInterface intf = en.nextElement();
				//
				// for (Enumeration<InetAddress> enumIpAddr = intf
				// .getInetAddresses(); enumIpAddr
				// .hasMoreElements();) {
				// InetAddress inetAddress = enumIpAddr.nextElement();
				// if (!inetAddress.isLoopbackAddress()
				// && InetAddressUtils
				// .isIPv4Address(inetAddress
				// .getHostAddress())) {
				// ips += inetAddress.getHostAddress();
				// }
				// }
				// }
				// } catch (SocketException ex) {
				// ex.printStackTrace();
				// return ex.getMessage();
				// }
				// return "Couldn't retrieve IP";

			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				TextView myIPTextView = (TextView) findViewById(R.id.self_ip);
				myIPTextView.setText("Your IP: " + result);
			}

		};
		selfIPFindingTask.execute();

		Button actionButton = (Button) findViewById(R.id.action_button);
		actionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AsyncTask<String, Integer, String> serverConnectionTask = new AsyncTask<String, Integer, String>() {
					@Override
					protected String doInBackground(String... arg0) {
						connectToServer();
						return null;
					}

					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						if (!isConnectedToServer())
							return;
						try {
							startMouseSimulation();
//							 startKeyboardSimulation();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				};
				serverConnectionTask.execute();

			}
		});
		mode = Mode.HOME;
	}

	private void startMouseSimulation() throws IOException {
		MouseSimulator mouseSimulator = new MouseSimulator(
				socket.getOutputStream());
		mouseSimulatorUI = new MouseSimulatorUI(HomeActivity.this,
				mouseSimulator);

		setContentView(mouseSimulatorUI);
		mode = Mode.MOUSE;
	}

	private void startKeyboardSimulation() throws IOException {
		KeyboardSimulator keyboardSimulator = new KeyboardSimulator(
				socket.getOutputStream());
		keyboardSimulatorUI = new KeyboardSimulatorUI(HomeActivity.this,
				keyboardSimulator);
		setContentView(keyboardSimulatorUI);
		mode = Mode.KEYBOARD;
	}

	public void connectToServer() {
		String ip = ((EditText) findViewById(R.id.server_ip)).getText()
				.toString();
		int port = Integer.parseInt(((EditText) findViewById(R.id.server_port))
				.getText().toString());
		try {
			Log.e("Home", "Connecting to " + ip + ":" + port);
			socket = new Socket(InetAddress.getByName(ip), port);
			setConnectedToServer(true);
			Log.e("Home", "Connected ( " + ip + ":" + port + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void closeServer() {
		if (!isConnectedToServer())
			return;
		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public byte[] getByteArrayFromIP(String ip) {
		byte[] addr = new byte[4];
		String[] split = ip.split("[.]");
		for (int i = 0; i < addr.length; i++) {
			addr[i] = Byte.parseByte(split[i]);
		}
		return addr;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.home_window_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_home:
			closeServer();
			prepareHome();
			return true;

		case R.id.menu_keyboard:
			if (!isConnectedToServer())
				return true;
			try {
				startKeyboardSimulation();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return true;

		case R.id.menu_touchpad:
			if (!isConnectedToServer())
				return true;
			try {
				startMouseSimulation();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;

		case R.id.menu_settings:
			startActivity(new Intent(getApplicationContext(),
					SettingsActivity.class));
			return true;

		case R.id.menu_exit:
			onExit();
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private void exitApplication() {
		closeServer();
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// onExit();
			exitApplication();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private void onExit() {
		new AlertDialog.Builder(HomeActivity.this)
				.setTitle("Quit")
				.setMessage("Do you want to quit ?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								exitApplication();
							}
						}).setNegativeButton("No", null).show();
	}

	public boolean isConnectedToServer() {
		return connectedToServer;
	}

	protected void setConnectedToServer(boolean connectedToServer) {
		this.connectedToServer = connectedToServer;
	}

}

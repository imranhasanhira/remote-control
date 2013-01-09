package com.example.remotecontrol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
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
	private Socket socket;
	MouseSimulatorUI mouseSimulatorUI;
	KeyboardSimulatorUI keyboardSimulatorUI;
	private boolean connectedToServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constants.init();
		setConnectedToServer(false);

		prepareHome();
		// startKeyboardSimulation();

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
							// startMouseSimulation();
							startKeyboardSimulation();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				};
				serverConnectionTask.execute();

			}
		});
	}

	public void connectToServer() {
		String ip = ((EditText) findViewById(R.id.server_ip)).getText()
				.toString();
		int port = Integer.parseInt(((EditText) findViewById(R.id.server_port))
				.getText().toString());
		try {
			socket = new Socket(InetAddress.getByName(ip), port);
			setConnectedToServer(true);
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

	private void startMouseSimulation() throws IOException {
		if (mouseSimulatorUI == null) {
			MouseSimulator mouseSimulator = new MouseSimulator(
					socket.getOutputStream());
			mouseSimulatorUI = new MouseSimulatorUI(HomeActivity.this,
					mouseSimulator);
		}
		setContentView(mouseSimulatorUI);
	}

	private void startKeyboardSimulation() throws IOException {
		if (keyboardSimulatorUI == null) {
			KeyboardSimulator keyboardSimulator = new KeyboardSimulator(
					socket.getOutputStream());
			keyboardSimulatorUI = new KeyboardSimulatorUI(HomeActivity.this,
					keyboardSimulator);
		}
		setContentView(keyboardSimulatorUI);
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
			onExit();
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

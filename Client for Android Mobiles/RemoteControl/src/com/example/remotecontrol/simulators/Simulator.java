package com.example.remotecontrol.simulators;

import java.io.IOException;
import java.io.OutputStream;

import android.util.Log;

public abstract class Simulator {
	public enum Type {
		MOUSE, SCREEN, KEYBOARD, AUTHENTICATION
	};

	protected OutputStream os;
	protected Type type;

	protected Simulator(Type type, OutputStream os) {
		this.type = type;
		this.os = os;
	}

	protected synchronized void write(byte[] buffer) throws IOException {
//		Log.e("", ""+((byte) type.ordinal()) + br(buffer) );
		os.write((byte) type.ordinal());
		os.write(buffer);
		os.flush();
	}

	protected void write(String str) throws IOException {
		write(str.getBytes());
	}

	protected void writeLine(byte[] buffer) throws IOException {
		write(buffer);
		write(new byte[] { '\n' });
	}

	protected void writeLine(String str) throws IOException {
		write(str.concat("\n").getBytes());
	}
	
	private String br(byte[] bs){
		String str="";
		for(byte b:bs) str+= ","+b;
		return str;
	}

}

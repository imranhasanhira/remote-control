package com.example.remotecontrol.simulators;

import java.io.IOException;
import java.io.OutputStream;

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
		os.write((byte) type.ordinal());
		os.write(buffer);
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

}

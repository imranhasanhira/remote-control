package com.example.remotecontrol.simulators;

import java.io.IOException;
import java.io.OutputStream;

public class KeyboardSimulator extends Simulator {
	public enum Operation {
		TYPE, PRESS, RELEASE
	};

	public KeyboardSimulator(OutputStream os) {
		super(Simulator.Type.KEYBOARD, os);
	}

	public void simulateKeyType(int virtualKey) throws IOException {
		simulateKey(Operation.TYPE, virtualKey);
	}

	public void simulateKeyPress(int virtualKey) throws IOException {
		simulateKey(Operation.PRESS, virtualKey);
	}

	public void simulateKeyRelease(int virtualKey) throws IOException {
		simulateKey(Operation.RELEASE, virtualKey);
	}

	protected void simulateKey(Operation operation, int virtualKey)
			throws IOException {
		// byte[] buffer = new byte[] { (byte) operation.ordinal(),
		// (byte) (virtualKey & 0xFF000000),
		// (byte) (virtualKey & 0x00FF0000),
		// (byte) (virtualKey & 0x0000FF00),
		// (byte) (virtualKey & 0x000000FF), '\n' };
		// write(buffer);
		write(operation + " " + (char)virtualKey);
	}

}

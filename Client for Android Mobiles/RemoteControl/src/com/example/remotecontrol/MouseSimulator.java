package com.example.remotecontrol;

import java.io.IOException;
import java.io.OutputStream;

public class MouseSimulator extends Simulator {

	public enum Button {
		LEFT, RIGHT, SCROLL
	};

	public enum Operation {
		CURSOR_MOVE_BY, CLICK, PRESS, RELEASE, SCROLL_BY
	};

	public MouseSimulator(OutputStream os) {
		super(Simulator.Type.MOUSE, os);
	}

	public void simulateClick(Button button) throws IOException {
		byte[] buffer = new byte[] { (byte) Operation.CLICK.ordinal(),
				(byte) button.ordinal(), '\n' };
		write(buffer);
	}

	public void simulatePress(Button button) throws IOException {
		byte[] buffer = new byte[] { (byte) Operation.PRESS.ordinal(),
				(byte) button.ordinal(), '\n' };
		write(buffer);
	}

	public void simulateRelease(Button button) throws IOException {
		byte[] buffer = new byte[] { (byte) Operation.RELEASE.ordinal(),
				(byte) button.ordinal(), '\n' };
		write(buffer);
	}

	/**
	 * this function is architechture dependent. because dx and dy both are
	 * 2Bytes. the byte near MSB is sent first and then the byte near LSB is
	 * sent next.
	 * 
	 * @param dx
	 * @param dy
	 * @throws IOException
	 */
	public void simulateCursorMoveBy(short dx, short dy) throws IOException {
		byte[] buffer = new byte[] { (byte) Operation.CURSOR_MOVE_BY.ordinal(),
				(byte) (dx & 0xFF00), (byte) (dx & 0xFF), (byte) (dy & 0xFF00),
				(byte) (dy & 0xFF), '\n' };
		write(buffer);
	}

	/**
	 * this function is architechture dependent. because ds is 2Bytes. the byte
	 * near MSB is sent first and then the byte near LSB is sent next.
	 * 
	 * @param ds
	 * @throws IOException
	 */
	public void simulateScrollBy(short ds) throws IOException {
		byte[] buffer = new byte[] { (byte) Operation.SCROLL_BY.ordinal(),
				(byte) (ds & 0xFF00), (byte) (ds & 0xFF), '\n' };
		write(buffer);
	}
}

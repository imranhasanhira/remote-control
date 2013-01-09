package com.example.remotecontrol;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Rect;

public class Constants {
	public static HashMap<Integer, Rect> keyboardMap;
	public static int keyBoardWidthPx = 900;
	public static int keyBoardHeightPx = 288;

	public static enum VirtualMouse {
		VM_LEFT_CLICK, VM_LEFT_CLICK2, VM_RIGHT_CLICK, VM_SCROLL_UP, VM_SCROLL_DOWN, VM_MOVE_TO, VM_MOVE_BY
	};

	public static void init() {
		keyboardMap = new HashMap<Integer, Rect>();
		
		//First row from top
		keyboardMap.put(VirtualKey.VK_ESCAPE, getRect(12, 10));
		keyboardMap.put(VirtualKey.VK_F1, getRect(78, 10));
		keyboardMap.put(VirtualKey.VK_F2, getRect(114, 10));
		keyboardMap.put(VirtualKey.VK_F3, getRect(152, 9));
		keyboardMap.put(VirtualKey.VK_F4, getRect(192, 9));
		keyboardMap.put(VirtualKey.VK_F5, getRect(250, 9));
		keyboardMap.put(VirtualKey.VK_F6, getRect(290, 9));
		keyboardMap.put(VirtualKey.VK_F7, getRect(328, 9));
		keyboardMap.put(VirtualKey.VK_F8, getRect(366, 9));
		keyboardMap.put(VirtualKey.VK_F9, getRect(426, 9));
		keyboardMap.put(VirtualKey.VK_F10, getRect(464, 9));
		keyboardMap.put(VirtualKey.VK_F11, getRect(502, 9));
		keyboardMap.put(VirtualKey.VK_F12, getRect(540, 9));

		//Second row from top
		keyboardMap.put(VirtualKey.VK_BACK_QUOTE, getRect(10, 82));
		keyboardMap.put(VirtualKey.VK_1, getRect(49, 82));
		keyboardMap.put(VirtualKey.VK_2, getRect(86, 82));
		keyboardMap.put(VirtualKey.VK_3, getRect(126, 82));
		keyboardMap.put(VirtualKey.VK_4, getRect(164, 82));
		keyboardMap.put(VirtualKey.VK_5, getRect(201, 82));
		keyboardMap.put(VirtualKey.VK_6, getRect(240, 82));
		keyboardMap.put(VirtualKey.VK_7, getRect(277, 82));
		keyboardMap.put(VirtualKey.VK_8, getRect(316, 82));
		keyboardMap.put(VirtualKey.VK_9, getRect(355, 82));
		keyboardMap.put(VirtualKey.VK_0, getRect(392, 82));
		keyboardMap.put(VirtualKey.VK_MINUS, getRect(430, 82));
		keyboardMap.put(VirtualKey.VK_PLUS, getRect(466, 82));
		keyboardMap.put(VirtualKey.VK_BACK_SPACE, new Rect(506, 82, 577, 118));

		//Third row from top
		keyboardMap.put(VirtualKey.VK_TAB, new Rect(10, 122, 60, 156));
		keyboardMap.put(VirtualKey.VK_Q, getRect(67, 122));
		keyboardMap.put(VirtualKey.VK_W, getRect(106, 122));
		keyboardMap.put(VirtualKey.VK_E, getRect(142, 122));
		keyboardMap.put(VirtualKey.VK_R, getRect(182, 122));
		keyboardMap.put(VirtualKey.VK_T, getRect(220, 122));
		keyboardMap.put(VirtualKey.VK_Y, getRect(258, 122));
		keyboardMap.put(VirtualKey.VK_U, getRect(296, 122));
		keyboardMap.put(VirtualKey.VK_I, getRect(334, 122));
		keyboardMap.put(VirtualKey.VK_O, getRect(372, 122));
		keyboardMap.put(VirtualKey.VK_P, getRect(410, 122));
		keyboardMap.put(VirtualKey.VK_BRACELEFT, getRect(448, 122));
		keyboardMap.put(VirtualKey.VK_BRACERIGHT, getRect(486, 122));
		keyboardMap.put(VirtualKey.VK_BACK_SLASH, new Rect(524, 122, 576, 156));

		//Fourth row from top
		keyboardMap.put(VirtualKey.VK_CAPS_LOCK, getRect(12, 162));
		keyboardMap.put(VirtualKey.VK_A, getRect(78, 162));
		keyboardMap.put(VirtualKey.VK_S, getRect(116, 162));
		keyboardMap.put(VirtualKey.VK_D, getRect(154, 162));
		keyboardMap.put(VirtualKey.VK_F, getRect(192, 162));
		keyboardMap.put(VirtualKey.VK_G, getRect(230, 162));
		keyboardMap.put(VirtualKey.VK_H, getRect(268, 162));
		keyboardMap.put(VirtualKey.VK_J, getRect(306, 162));
		keyboardMap.put(VirtualKey.VK_K, getRect(345, 162));
		keyboardMap.put(VirtualKey.VK_L, getRect(382, 162));
		keyboardMap.put(VirtualKey.VK_SEMICOLON, getRect(421, 162));
		keyboardMap.put(VirtualKey.VK_QUOTE, getRect(458, 162));
		keyboardMap.put(VirtualKey.VK_ENTER, new Rect(498, 162, 576, 196));
		
		//Fifth row from top
		keyboardMap.put(VirtualKey.VK_SHIFT, new Rect(11, 201,101,233));
		keyboardMap.put(VirtualKey.VK_Z, getRect(104, 201));
		keyboardMap.put(VirtualKey.VK_X, getRect(142, 201));
		keyboardMap.put(VirtualKey.VK_C, getRect(180, 201));
		keyboardMap.put(VirtualKey.VK_V, getRect(220, 201));
		keyboardMap.put(VirtualKey.VK_B, getRect(258, 201));
		keyboardMap.put(VirtualKey.VK_N, getRect(296, 201));
		keyboardMap.put(VirtualKey.VK_M, getRect(334, 201));
		keyboardMap.put(VirtualKey.VK_LESS, getRect(372, 201));
		keyboardMap.put(VirtualKey.VK_GREATER, getRect(410, 201));
		//keyboardMap.put(VirtualKey.VK_RIGHT_SHIFT, new Rect(486, 200, 575,236));
		
		//Bottom row
		keyboardMap.put(VirtualKey.VK_CONTROL, new Rect(12, 240,64,272));
		keyboardMap.put(VirtualKey.VK_WINDOWS, new Rect(68, 240,115,272));
		keyboardMap.put(VirtualKey.VK_ALT, new Rect(117, 240,162,272));
		keyboardMap.put(VirtualKey.VK_SPACE, new Rect(168, 240,372,272));
//		keyboardMap.put(VirtualKey.VK_RIGHT_ALT, new Rect(376, 240,420,272));
//		keyboardMap.put(VirtualKey.VK_RIGHT_WINDOWS, new Rect(424, 240,468,272));
		keyboardMap.put(VirtualKey.VK_CONTEXT_MENU, new Rect(474, 240,520,272));
//		keyboardMap.put(VirtualKey.VK_RIGHT_CONTROL, new Rect(522, 240,576));

		
		
		keyboardMap.put(VirtualKey.VK_PRINTSCREEN, getRect(600, 9));
		keyboardMap.put(VirtualKey.VK_SCROLL_LOCK, getRect(640, 9));
		keyboardMap.put(VirtualKey.VK_PAUSE, getRect(676, 9));
		keyboardMap.put(VirtualKey.VK_NUM_LOCK, getRect(734, 82));

		

		keyboardMap.put(VirtualKey.VK_UP, getRect(638, 200));
		keyboardMap.put(VirtualKey.VK_DOWN, getRect(638, 240));
		keyboardMap.put(VirtualKey.VK_LEFT, getRect(600, 240));
		keyboardMap.put(VirtualKey.VK_RIGHT, getRect(676, 240));

		keyboardMap.put(VirtualKey.VK_INSERT, getRect(600, 82));
		keyboardMap.put(VirtualKey.VK_DELETE, getRect(600, 122));
		keyboardMap.put(VirtualKey.VK_HOME, getRect(638, 82));
		keyboardMap.put(VirtualKey.VK_END, getRect(638, 120));
		keyboardMap.put(VirtualKey.VK_PAGE_UP, getRect(676, 82));
		keyboardMap.put(VirtualKey.VK_PAGE_DOWN, getRect(676, 120));
	}

	private static Rect getRect(int x, int y) {
		return new Rect(x, y, x + 36, y + 36);
	}
}

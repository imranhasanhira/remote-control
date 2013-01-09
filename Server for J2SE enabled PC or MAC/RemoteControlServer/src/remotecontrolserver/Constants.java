/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 *
 * @author Md Imran Hasan
 */
public class Constants {

    public static HashMap<String, Integer> virtualKeymap;

    public static void init() {
        virtualKeymap = new HashMap();
        virtualKeymap.put("ESC", KeyEvent.VK_ESCAPE);
        virtualKeymap.put("F1", KeyEvent.VK_F1);
        virtualKeymap.put("F2", KeyEvent.VK_F2);
        virtualKeymap.put("F3", KeyEvent.VK_F3);
        virtualKeymap.put("F4", KeyEvent.VK_F4);
        virtualKeymap.put("F5", KeyEvent.VK_F5);
        virtualKeymap.put("F6", KeyEvent.VK_F6);
        virtualKeymap.put("F7", KeyEvent.VK_F7);
        virtualKeymap.put("F8", KeyEvent.VK_F8);
        virtualKeymap.put("F9", KeyEvent.VK_F9);
        virtualKeymap.put("F10", KeyEvent.VK_F10);
        virtualKeymap.put("F11", KeyEvent.VK_F11);
        virtualKeymap.put("F12", KeyEvent.VK_F12);

        virtualKeymap.put("PRT_SC", KeyEvent.VK_PRINTSCREEN);
        virtualKeymap.put("SCROLL_LOCK", KeyEvent.VK_SCROLL_LOCK);
        virtualKeymap.put("NUM_LOCK", KeyEvent.VK_NUM_LOCK);
        virtualKeymap.put("CAPS_LOCK", KeyEvent.VK_CAPS_LOCK);


        virtualKeymap.put("ENTER", KeyEvent.VK_ENTER);
        virtualKeymap.put("TAB", KeyEvent.VK_TAB);


        virtualKeymap.put("UP", KeyEvent.VK_UP);
        virtualKeymap.put("DOWN", KeyEvent.VK_DOWN);
        virtualKeymap.put("LEFT", KeyEvent.VK_LEFT);
        virtualKeymap.put("RIGHT", KeyEvent.VK_RIGHT);

        virtualKeymap.put("BACKSPACE", KeyEvent.VK_BACK_SPACE);
        virtualKeymap.put("DELETE", KeyEvent.VK_DELETE);
        virtualKeymap.put("PAGE_UP", KeyEvent.VK_PAGE_UP);
        virtualKeymap.put("PAGE_DOWN", KeyEvent.VK_PAGE_DOWN);
        virtualKeymap.put("HOME", KeyEvent.VK_HOME);
        virtualKeymap.put("END", KeyEvent.VK_END);

        virtualKeymap.put("A", KeyEvent.VK_A);
        virtualKeymap.put("B", KeyEvent.VK_B);
        virtualKeymap.put("C", KeyEvent.VK_C);
        virtualKeymap.put("D", KeyEvent.VK_D);
        virtualKeymap.put("E", KeyEvent.VK_E);
        virtualKeymap.put("F", KeyEvent.VK_F);
        virtualKeymap.put("G", KeyEvent.VK_G);
        virtualKeymap.put("H", KeyEvent.VK_H);
        virtualKeymap.put("I", KeyEvent.VK_I);
        virtualKeymap.put("J", KeyEvent.VK_J);
        virtualKeymap.put("K", KeyEvent.VK_K);
        virtualKeymap.put("L", KeyEvent.VK_L);
        virtualKeymap.put("M", KeyEvent.VK_M);
        virtualKeymap.put("N", KeyEvent.VK_N);
        virtualKeymap.put("O", KeyEvent.VK_O);
        virtualKeymap.put("P", KeyEvent.VK_P);
        virtualKeymap.put("Q", KeyEvent.VK_Q);
        virtualKeymap.put("R", KeyEvent.VK_R);
        virtualKeymap.put("S", KeyEvent.VK_S);
        virtualKeymap.put("T", KeyEvent.VK_T);
        virtualKeymap.put("U", KeyEvent.VK_U);
        virtualKeymap.put("V", KeyEvent.VK_V);
        virtualKeymap.put("W", KeyEvent.VK_W);
        virtualKeymap.put("X", KeyEvent.VK_X);
        virtualKeymap.put("Y", KeyEvent.VK_Y);
        virtualKeymap.put("Z", KeyEvent.VK_Z);

        virtualKeymap.put("1", KeyEvent.VK_1);
        virtualKeymap.put("2", KeyEvent.VK_2);
        virtualKeymap.put("3", KeyEvent.VK_3);
        virtualKeymap.put("4", KeyEvent.VK_4);
        virtualKeymap.put("5", KeyEvent.VK_5);
        virtualKeymap.put("6", KeyEvent.VK_6);
        virtualKeymap.put("7", KeyEvent.VK_7);
        virtualKeymap.put("8", KeyEvent.VK_8);
        virtualKeymap.put("9", KeyEvent.VK_9);
        virtualKeymap.put("0", KeyEvent.VK_0);
        
        virtualKeymap.put("-", KeyEvent.VK_MINUS);
        virtualKeymap.put("+", KeyEvent.VK_PLUS);
        virtualKeymap.put("[", KeyEvent.VK_OPEN_BRACKET);
        virtualKeymap.put("]", KeyEvent.VK_CLOSE_BRACKET);
        virtualKeymap.put("`", KeyEvent.VK_BACK_QUOTE);
        virtualKeymap.put("\\", KeyEvent.VK_BACK_SLASH);
        virtualKeymap.put("/", KeyEvent.VK_SLASH);
        virtualKeymap.put("'", KeyEvent.VK_QUOTE);
        virtualKeymap.put(";", KeyEvent.VK_SEMICOLON);
        virtualKeymap.put(",", KeyEvent.VK_COMMA);
        


    }
}

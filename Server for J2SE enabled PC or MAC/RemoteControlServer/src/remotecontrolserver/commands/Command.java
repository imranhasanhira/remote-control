/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver.commands;

import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author Md Imran Hasan
 */
public abstract class Command {

    public static final byte TYPE_MOUSE = 0;
    public static final byte TYPE_SCREEN = 1;
    public static final byte TYPE_KEYBOARD = 2;
    public static final byte TYPE_AUTHENTICATION = 3;

    public abstract boolean execute();

    public abstract boolean readInput(InputStream is);
}

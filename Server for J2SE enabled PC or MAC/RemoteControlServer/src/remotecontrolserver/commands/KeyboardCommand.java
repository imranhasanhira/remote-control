/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver.commands;

import java.awt.Robot;
import java.io.InputStream;

/**
 *
 * @author Md Imran Hasan
 */
public class KeyboardCommand extends Command{
    Robot robot;

    public KeyboardCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean readInput(InputStream is) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}

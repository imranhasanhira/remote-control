/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import remotecontrolserver.commands.*;

/**
 *
 * @author Md Imran Hasan
 */
public class ClientManager {

    private AdvancedThread inputReadingThread;
    private final Socket clientSocket;
    private InputStream is;
    private final NetworkInputMonitorInterface networkReader;
    Robot robot;

    ClientManager(Socket clientSocket, NetworkInputMonitorInterface networkReader) {
        this.clientSocket = clientSocket;
        this.networkReader = networkReader;
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Your Device doesn't support Robot. Robot is required to simulate Mouse & Keyboard. The program will now exit.");
            System.exit(0);
        }
        startReadingThread();
    }

    protected void startReadingThread() {
        inputReadingThread = new AdvancedThread() {

            @Override
            protected void preTask() {
                try {
                    is = clientSocket.getInputStream();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            protected void loopTask() throws Exception {
//                System.out.println("--");
                byte commandType = (byte) is.read();
                Command command = getCommand(commandType);
                if (command != null) {
                    command.readInput(is);
                    is.read();
                    command.execute();
                }
                //                String nextLine = scanner.nextLine();
                //                networkReader.lineReceived(nextLine);
            }

            @Override
            protected void postTask() {
            }

            @Override
            protected void finishingTask() {
            }
        };
        inputReadingThread.start();
    }

    public void closeClient() throws IOException {
        clientSocket.close();
        try {
            inputReadingThread.kill();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void writeMessage(String message) throws IOException {
        clientSocket.getOutputStream().write(message.concat("\n").getBytes());
    }

    private Command getCommand(byte commandType) {
        switch (commandType) {
            case Command.TYPE_MOUSE:
                return new MouseCommand(robot);

            case Command.TYPE_SCREEN:
                return new ScreenCommand();

            case Command.TYPE_KEYBOARD:
                return new KeyboardCommand(robot);

            case Command.TYPE_AUTHENTICATION:
                return new AuthenticationCommand();

            default:
//                System.out.println("Command Type: " + commandType);
                return null;
        }
    }
}

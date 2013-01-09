/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Md Imran Hasan
 */
public class ClientManager {

    private AdvancedThread inputReadingThread;
    private final Socket clientSocket;
    private Scanner scanner;
    private final NetworkInputMonitorInterface networkReader;

    ClientManager(Socket clientSocket, NetworkInputMonitorInterface networkReader) {
        this.clientSocket = clientSocket;
        this.networkReader = networkReader;
        startReadingThread();
    }

    protected void startReadingThread() {
        inputReadingThread = new AdvancedThread() {

            @Override
            protected void preTask() {
                try {
                    scanner = new Scanner(clientSocket.getInputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            protected void loopTask() throws Exception {
                String nextLine = scanner.nextLine();
//                networkReader.lineReceived(clientSocket.getInetAddress().getHostAddress() + ":" + nextLine);
                networkReader.lineReceived(nextLine);
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
}

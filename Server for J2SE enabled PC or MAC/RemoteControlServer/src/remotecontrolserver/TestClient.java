/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Md Imran Hasan
 */
public class TestClient {

    AdvancedThread writingThread;
    Scanner scanner;
    Socket socket;
    String ip;
    int portNumber;

    public TestClient(String ip, int portNumber) {
        this.ip = ip;
        this.portNumber = portNumber;
    }

    public static void main(String[] args) {

        TestClient testClient = new TestClient("172.27.0.1", 33333);
        testClient.start();
    }

    private void start() {
        writingThread = new AdvancedThread("ClientWritingThread") {

            @Override
            protected void preTask() {
                try {
                    scanner = new Scanner(System.in);
                    socket = new Socket(InetAddress.getByName(ip), portNumber);
//                    socket = new Socket(InetAddress.getByName("localhost"), portNumber);

                    System.out.println("Connected to server " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                } catch (IOException ex) {
                    ex.printStackTrace();
//                    try {
//                        socket.close();
//                    } catch (IOException ex1) {
//                        ex.printStackTrace();
//                    }
                }
            }

            @Override
            protected void loopTask() throws Exception {
                String nextLine = scanner.nextLine();
                writeLine(nextLine);
            }

            @Override
            protected void postTask() {
            }

            @Override
            protected void finishingTask() {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        writingThread.start();
    }

    public synchronized void writeLine(String message) throws IOException {
        if (message.equals("exit")) {
            try {
                writingThread.kill();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        socket.getOutputStream().write(message.concat("\n").getBytes());
    }

    public byte[] getByteArrayFromIP(String ip) {
        byte[] addr = new byte[4];
        String[] split = ip.split("[.]");
        for (int i = 0; i < addr.length; i++) {
            addr[i] = Byte.parseByte(split[i]);
        }
        return addr;
    }
}

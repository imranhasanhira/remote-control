/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Md Imran Hasan
 */
public abstract class AdvancedThread extends Thread {

    private boolean running;
    private boolean paused;

    public AdvancedThread() {
        this("UnnamedAdvancedThread");
    }

    public AdvancedThread(String name) {
        super(name);
        setRunning(false);
    }

    public synchronized void startPaused() {
        setPaused(true);
        this.start();
    }

    @Override
    public void run() {

        preTask();

        setRunning(true);
        try {
            while (isRunning()) {
                pauseCheck();
                if (!isRunning()) {
                    break;
                }
                loopTask();
            }
        } catch (Exception ex) {
            Logger.getLogger(AdvancedThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        setRunning(false);

        postTask();

    }

    private synchronized void pauseCheck() {
        while (isPaused()) {
            if (!isRunning()) {
                return;
            }

            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AdvancedThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void kill() throws InterruptedException {
        setRunning(false);
        if (isAlive()) {
            finishingTask();
            this.interrupt();
            this.join();
        }
    }

    public synchronized void pause() {
        if (!isPaused()) {
            setPaused(true);
        }
    }

    public synchronized void unPause() {
        if (isPaused()) {
            setPaused(false);
            notify();
        }
    }

    abstract protected void preTask();

    abstract protected void loopTask() throws Exception;

    abstract protected void postTask();

    abstract protected void finishingTask();

    /**
     * @return the paused
     */
    public synchronized boolean isPaused() {
        return paused;
    }

    /**
     * @param paused the paused to set
     */
    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * @return the running
     */
    private synchronized boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    private synchronized void setRunning(boolean running) {
        this.running = running;
    }
}

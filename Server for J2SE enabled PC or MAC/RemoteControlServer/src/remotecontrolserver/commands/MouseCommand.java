/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolserver.commands;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Md Imran Hasan
 */
public class MouseCommand extends Command {

    public static final byte BUTTON_LEFT = 0;
    public static final byte BUTTON_RIGHT = 1;
    public static final byte BUTTON_SCROLL = 2;
    //
    public static final byte OPERATION_CURSOR_MOVE_BY = 0;
    public static final byte OPERATION_CLICK = 1;
    public static final byte OPERATION_PRESS = 2;
    public static final byte OPERATION_RELEASE = 3;
    public static final byte OPERATION_VERTICAL_SCROLL_BY = 4;
    public static final byte OPERATION_HORIZONTAL_SCROLL_BY = 5;
    //
    //
    Robot robot;
    private int operation;
    private int button;
    private short[] values;

    public MouseCommand(Robot robot) {
        this.robot = robot;
    }

    private void assignButtonFromCode(byte buttonCode) {
        switch (buttonCode) {
            case BUTTON_LEFT:
                button = InputEvent.BUTTON1_DOWN_MASK;
                break;
            case BUTTON_RIGHT:
                button = InputEvent.BUTTON3_DOWN_MASK;
                break;
            case BUTTON_SCROLL:
                button = InputEvent.BUTTON2_DOWN_MASK;
                break;
        }
    }

    @Override
    public boolean readInput(InputStream is) {
        try {
            operation = nextByte(is);
            switch (operation) {
                case OPERATION_CURSOR_MOVE_BY:
                    values = new short[]{nextShort(is), nextShort(is)};
                    break;

                case OPERATION_CLICK:
                case OPERATION_PRESS:
                case OPERATION_RELEASE:
                    assignButtonFromCode(nextByte(is));
                    break;
                case OPERATION_VERTICAL_SCROLL_BY:
                    values = new short[]{nextShort(is)};
                    break;
                case OPERATION_HORIZONTAL_SCROLL_BY:
                    values = new short[]{nextShort(is)};
                    break;
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public byte nextByte(InputStream is) throws IOException {
        return (byte) is.read();
    }

    public short nextShort(InputStream is) throws IOException {
//        int msB = is.read();
//        int lsB = is.read();
//        System.out.println(msB + "," + lsB+" , " + ((short) ((msB << 8) | lsB)));
//        return (short) ((msB << 8) | lsB);
        return (short) (is.read() << 8 | is.read());
    }

    @Override
    public boolean execute() {
        switch (operation) {
            case OPERATION_CURSOR_MOVE_BY:
                moveTo(values[0], values[1]);
//                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
//                robot.mouseMove(mx, my);
//                System.out.println("MOVE " + values[0] + " " + values[1]);
                break;

            case OPERATION_CLICK:
                robot.mousePress(button);
                robot.mouseRelease(button);
//                System.out.println("CLICK");
                break;

            case OPERATION_PRESS:
                robot.mousePress(button);
//                System.out.println("Press");
                break;

            case OPERATION_RELEASE:
                robot.mouseRelease(button);
//                System.out.println("Release");
                break;

            case OPERATION_VERTICAL_SCROLL_BY:
                robot.mouseWheel(values[0]);
//                System.out.println("Scroll Y");
                break;

            case OPERATION_HORIZONTAL_SCROLL_BY:
                robot.mouseWheel(values[0]);
//                System.out.println("Scroll X");
                break;

        }

        return true;
    }

    private void moveTo(int x, int y) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(mouseLocation.x + x / 3, mouseLocation.y + y / 3);
        robot.mouseMove(mouseLocation.x + x / 2, mouseLocation.y + y / 2);
        robot.mouseMove(mouseLocation.x + 2 * x / 3, mouseLocation.y + 2 * y / 3);
        robot.mouseMove(mouseLocation.x + x, mouseLocation.y + y);
    }
}

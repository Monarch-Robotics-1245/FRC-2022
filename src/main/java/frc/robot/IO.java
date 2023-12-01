package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Controller and button input.
 */
public class IO {

    private XboxController xboxController;
    private Joystick joystick;

    // rightJoystick is right
    // leftJoystick is left


    public IO(){
        xboxController = new DeadzonedXboxController(0, 0.1);
        joystick = new Joystick(0);
    }

    public static XboxController getXboxController() {
        return new DeadzonedXboxController(0, 0.1);
    }

    public static Joystick getJoystick() {
        return new Joystick(0);
    }
}

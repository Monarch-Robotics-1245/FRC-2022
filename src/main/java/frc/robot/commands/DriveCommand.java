package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.IO;
import frc.robot.subsystems.DriveTrain;

public class DriveCommand extends CommandBase {
    private final DriveTrain driveTrain;
    private final XboxController controller;
    private final Joystick joystick;


    public DriveCommand(DriveTrain driveTrainImpl) {
        addRequirements(driveTrainImpl);
        this.driveTrain = driveTrainImpl;
        this.controller = IO.getXboxController();
        this.joystick = IO.getJoystick();
    }
    

    @Override
    public void execute() {
        double leftX = controller.getLeftX();
        double leftY = controller.getLeftY();

        if(Math.abs(leftX) < 0.2) {
            leftX = 0;
        }

        if(Math.abs(leftY) < 0.2) {
            leftY = 0;
        }

        double rotation = controller.getRightX();

        if(Math.abs(rotation) < 0.2) {
            rotation = 0;
        }

        //xbox controller has already been deadzoned
        driveTrain.drive(leftY, leftX, rotation);

        // System.out.println("0");
        if(controller.getRightBumperPressed()) {
            driveTrain.invert();
            System.out.println("Inverted");
        }
        if (controller.getLeftBumperPressed()){
            driveTrain.toggleAdjust();
        }
        //driveTrain.flush();
    }
}

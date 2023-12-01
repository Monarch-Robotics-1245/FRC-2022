package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class FollowPath extends CommandBase {
    private final DriveTrain driveTrain;

    public FollowPath(DriveTrain driveTrain) {
        addRequirements(driveTrain);
        this.driveTrain = driveTrain;
    }

    @Override
    public void execute() {

        //xbox controller has already been deadzoned
        driveTrain.drive(-0.3, 0, 0);

    }
}

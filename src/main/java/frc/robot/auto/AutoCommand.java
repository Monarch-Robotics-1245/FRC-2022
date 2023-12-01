package frc.robot.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallShooter;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Storage;

public class AutoCommand extends CommandBase {
    private final DriveTrain driveTrain;
    private final Intake intake;
    private final BallShooter ballShooter;
    private Storage storage;
    private long startTime;

    public AutoCommand(DriveTrain driveTrain, Intake intake, BallShooter ballShooter, Storage storage) {
        this.storage = storage;
        addRequirements(driveTrain, intake, ballShooter, storage);
        this.driveTrain = driveTrain;
        this.intake = intake;
        this.ballShooter = ballShooter;
    }

    @Override
    public void initialize() {
        super.initialize();
        startTime = System.currentTimeMillis()/1000;
    }

    @Override
    public void execute() {
        ballShooter.turnShooterOn(0.35);
        if(currentTimeElapsed() <= 0.1) {
            driveTrain.drive(0.3, 0, 0);
        }else if(currentTimeElapsed() <= 1) {
            driveTrain.drive(0, 0, 0);
        }else if(currentTimeElapsed() <= 4) {
            intake.spin(0.25);
            driveTrain.drive(0.2, 0, 0);
        }else if(currentTimeElapsed() <= 6){
            driveTrain.drive(0, 0, -.04);
        } else if(currentTimeElapsed() <= 6.03){
            driveTrain.drive(0, 0, 0);
            intake.spin(0.25);
            storage.spin(0.6);
        }else if(currentTimeElapsed() <= 8) {
            driveTrain.drive(0, 0, 0);
            intake.spin(0.25);
            storage.spin(0.6);
        }
    }

    public long currentTimeElapsed() {
        long currentTime = System.currentTimeMillis()/1000;
        long timeElapsed = currentTime - startTime;
        return timeElapsed;
    }

}

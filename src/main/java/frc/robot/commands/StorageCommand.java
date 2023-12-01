package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.IO;
import frc.robot.subsystems.Storage;

public class StorageCommand extends CommandBase {
    private final XboxController controller;
    private final Storage storage;

    public StorageCommand(Storage storageImpl) {
        addRequirements(storageImpl);
        this.storage = storageImpl;
        this.controller = IO.getXboxController();
    }

    @Override
    public void execute() {
           if(controller.getYButtonPressed()){
               storage.toggleSpin();
           }
        //System.out.println(storage.getProx());
           if(storage.isRunning() && storage.getProx() > 500 && !controller.getYButton())
               storage.toggleSpin();

    }
    
}

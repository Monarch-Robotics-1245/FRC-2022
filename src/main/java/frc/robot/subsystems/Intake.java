package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.IntakeCommand;

public class Intake extends SubsystemBase{


    // protected final static int I2C_DEVICE_ADDRESS = 0x69;
    private final TalonSRX motor;
    private boolean isOn = false;

    public Intake() {

        this.motor = new TalonSRX(5);
        setDefaultCommand(new IntakeCommand(this));
    }

    public void spin(double speed){
        motor.set(TalonSRXControlMode.PercentOutput, speed);
    }

    public void toggleSpin() {
        if(!isOn) {
            motor.set(TalonSRXControlMode.PercentOutput, 0.25);
            isOn = true;
        }else{
            motor.set(TalonSRXControlMode.PercentOutput, 0);
            isOn = false;
        }
    }

}



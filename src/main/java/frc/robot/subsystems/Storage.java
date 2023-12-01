package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.StorageCommand;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;


public class Storage extends SubsystemBase{

    private final TalonSRX motor;
    private boolean isOn = false;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;

    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    public Storage() {
        
        this.motor = new TalonSRX(6);
        System.out.println(m_colorSensor.isConnected());
        setDefaultCommand(new StorageCommand(this));
    }

    public void spin(double speed){
        motor.set(TalonSRXControlMode.PercentOutput, -speed);
    }

    public void toggleSpin() {
        if(!isOn) {
            motor.set(TalonSRXControlMode.PercentOutput, -0.6);
            isOn = true;
        }else{
            motor.set(TalonSRXControlMode.PercentOutput, 0);
            isOn = false;
        }
    }
    public int getProx(){
        return m_colorSensor.getProximity();
    }
    public boolean isRunning(){
        return isOn;
    }
}


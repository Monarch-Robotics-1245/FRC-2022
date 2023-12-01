package frc.robot.subsystems;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ShootCommand;

public class BallShooter extends SubsystemBase{

    private final CANSparkMax leftMotor;
    private final CANSparkMax rightMotor;

    private final SparkMaxPIDController leftPIDController;
    private final SparkMaxPIDController rightPIDController;

    private final RelativeEncoder leftEncoder;

    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);



    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public boolean isShooting = false;

    private NetworkTableInstance table;

    public BallShooter() {

        table = NetworkTableInstance.getDefault();
        table.startClient();

        NetworkTable teamInfo = table.getTable("FMSInfo");
        NetworkTableEntry teamColorEntry = teamInfo.getEntry("IsRedAlliance");

        this.leftMotor = new CANSparkMax(24, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.rightMotor = new CANSparkMax(25, CANSparkMaxLowLevel.MotorType.kBrushless);

        this.leftEncoder = leftMotor.getEncoder();

        leftPIDController = this.leftMotor.getPIDController();
        rightPIDController = this.rightMotor.getPIDController();

        kP = 0;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;

        leftPIDController.setP(kP);
        rightPIDController.setP(kP);
        leftPIDController.setI(kI);
        rightPIDController.setI(kI);
        leftPIDController.setFF(kFF);
        rightPIDController.setFF(kFF);
        leftPIDController.setOutputRange(kMinOutput, kMaxOutput);
        rightPIDController.setOutputRange(kMinOutput, kMaxOutput);

        setDefaultCommand(new ShootCommand(this));
    }

    @Override
    public void periodic() {
        int proximity = colorSensor.getProximity();

//        if(proximity > 800) {
//            turnShooterOn(0.35);
//        }
    }

    public void shootDefault(double speed){
        // leftMotor.set(speed);
        // rightMotor.set(-speed);

        leftPIDController.setReference(speed * maxRPM, CANSparkMax.ControlType.kVelocity);
        rightPIDController.setReference(-speed * maxRPM, CANSparkMax.ControlType.kVelocity);
    }

    public void turnShooterOn(double speed) {
        leftMotor.set(speed);
        rightMotor.set(-speed);
        isShooting = true;
    }

    public void turnShooterOff() {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
        isShooting = false;
    }

    public void toggleShoot() {
        if(!isShooting) {
            turnShooterOn(0.35);
        }else{
            turnShooterOff();
        }
    }

    public void lessPowerfulShoot() {
        if(!isShooting) {
            turnShooterOn(1);
        }else{
            turnShooterOff();
        }
    }

    // public void autoShoot() {
    //     //units are in feet, seconds, and radians
    //     double gravity = 32.174;
    //     double wheelDiameter = 0.5;
    //     double circumference = wheelDiameter * Math.PI;
    //     double angle = 1.39626;
    //     double exitVelocity = (leftEncoder.getVelocity() * circumference)/60;
    //     double heightOfShooter = 2.21;
    //     double xVelocity = exitVelocity * Math.cos(angle);
    //     double yVelocity = exitVelocity * Math.sin(angle);
    //     quadraticFormula(-gravity/(2 * Math.pow(xVelocity, 2)), yVelocity/xVelocity, 2.21);
    // }

    // public double quadraticFormula(double a, double b, double c) {

    // }
}


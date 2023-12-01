package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.drive.Vector2d;

import static java.util.Objects.requireNonNull;

public class MonarchMecanumDrive extends RobotDriveBase implements Sendable, AutoCloseable {

    private static int instances;

    private CANSparkMax frontLeftMotor;
    private CANSparkMax frontRightMotor;
    private CANSparkMax backLeftMotor;
    private CANSparkMax backRightMotor;

    private RelativeEncoder frontLeftEncoder;
    private RelativeEncoder frontRightEncoder;
    private RelativeEncoder backLeftEncoder;
    private RelativeEncoder backRightEncoder;

    private SparkMaxPIDController frontLeftSparkPID;
    private SparkMaxPIDController frontRightSparkPID;
    private SparkMaxPIDController backLeftSparkPID;
    private SparkMaxPIDController backRightSparkPID;

    private double multiplier = 1.0f;

    private double gyroRotation = 0;

    ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;


    public MonarchMecanumDrive(CANSparkMax frontLeft, CANSparkMax frontRight, CANSparkMax backLeft, CANSparkMax backRight) {

        new TalonSRX(0);

        gyro.reset();

        requireNonNull(frontLeft, "Front-Left motor cannot be null");
        requireNonNull(frontRight, "Front-Right motor cannot be null");
        requireNonNull(backLeft, "Back-Left motor cannot be null");
        requireNonNull(backRight, "Back-Right motor cannot be null");
        this.frontLeftMotor = frontLeft;
        this.frontRightMotor = frontRight;
        this.backLeftMotor = backLeft;
        this.backRightMotor = backRight;

        frontLeftEncoder = frontLeftMotor.getEncoder();
        frontRightEncoder = frontRightMotor.getEncoder();
        backLeftEncoder = backLeftMotor.getEncoder();
        backRightEncoder = backRightMotor.getEncoder();

        SendableRegistry.addChild(this, this.frontLeftMotor);
        SendableRegistry.addChild(this, this.frontRightMotor);
        SendableRegistry.addChild(this, this.backLeftMotor);
        SendableRegistry.addChild(this, this.backRightMotor);

//        this.frontLeftPID = new PIDController(0.03, 0.02, 0);
//        this.frontRightPID = new PIDController(0.03, 0.02, 0);
//        this.backLeftPID = new PIDController(0.03, 0.02, 0);
//        this.backRightPID = new PIDController(0.03, 0.02, 0);

        kP = 0;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;



        this.frontLeftSparkPID = frontLeftMotor.getPIDController();
        this.frontRightSparkPID = frontRightMotor.getPIDController();
        this.backLeftSparkPID = backLeftMotor.getPIDController();
        this.backRightSparkPID = backRightMotor.getPIDController();

        frontLeftSparkPID.setP(kP);
        frontRightSparkPID.setP(kP);
        backLeftSparkPID.setP(kP);
        backRightSparkPID.setP(kP);

        frontLeftSparkPID.setI(kI);
        frontRightSparkPID.setI(kI);
        backLeftSparkPID.setI(kI);
        backRightSparkPID.setI(kI);

        frontLeftSparkPID.setFF(kFF);
        frontRightSparkPID.setFF(kFF);
        backLeftSparkPID.setFF(kFF);
        backRightSparkPID.setFF(kFF);

        frontLeftSparkPID.setOutputRange(kMinOutput, kMaxOutput);
        frontRightSparkPID.setOutputRange(kMinOutput, kMaxOutput);
        backLeftSparkPID.setOutputRange(kMinOutput, kMaxOutput);
        backRightSparkPID.setOutputRange(kMinOutput, kMaxOutput);

        frontLeftMotor.setOpenLoopRampRate(0.5);
        frontRightMotor.setOpenLoopRampRate(0.5);
        backLeftMotor.setOpenLoopRampRate(0.5);
        backRightMotor.setOpenLoopRampRate(0.5);


        instances++;
        SendableRegistry.addLW(this, "MonarchMecanumDrive", instances);

    }

    //auto rotate field centric driving
    public void rotationFieldCentricDrive() {

    }

    //field centric driving
    public void fieldCentricDrive(double y, double x, double rotation) {
        //convert raw joystick vector into vectors for the use by regularDrive
        Vector2d direction = new Vector2d(y, x);
        direction.rotate(-gyro.getAngle());

        regularDrive(direction.y, direction.x, rotation);
    }

    //robot centric driving
    //x is going forward/backward
    //y is going left/right
    public void regularDrive(double y, double x, double rotation) {

        double frontLeftSpeed = y + x - rotation;
        double backLeftSpeed = y - x + rotation;
        double frontRightSpeed = y - x - rotation;
        double backRightSpeed = y + x + rotation;

        double[] wheelSpeeds = {frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed};

        normalize(wheelSpeeds);
 
        // frontLeftMotor.set(frontLeftPID.calculate(frontLeftEncoder.getVelocity()/5676d ,wheelSpeeds[0]));
        // frontRightMotor.set(frontLeftPID.calculate(frontRightEncoder.getVelocity()/5676d ,wheelSpeeds[1]));
        // backLeftMotor.set(frontLeftPID.calculate(backLeftEncoder.getVelocity()/5676d ,wheelSpeeds[2]));
        // backRightMotor.set(frontLeftPID.calculate(backRightEncoder.getVelocity()/5676d ,wheelSpeeds[3]));


//        frontLeftMotor.set(wheelSpeeds[0]);
//        frontRightMotor.set(wheelSpeeds[1]);
//        backLeftMotor.set(wheelSpeeds[2]);
//        backRightMotor.set(wheelSpeeds[3]);

        frontLeftSparkPID.setReference(wheelSpeeds[0], CANSparkMax.ControlType.kDutyCycle);
        frontRightSparkPID.setReference(wheelSpeeds[1], CANSparkMax.ControlType.kDutyCycle);
        backLeftSparkPID.setReference(wheelSpeeds[2], CANSparkMax.ControlType.kDutyCycle);
        backRightSparkPID.setReference(wheelSpeeds[3], CANSparkMax.ControlType.kDutyCycle);


//
//        frontLeftMotor.set(wheelSpeeds[0]);
//        frontRightMotor.set(wheelSpeeds[1]);
//        backLeftMotor.set(wheelSpeeds[2]);
//        backRightMotor.set(wheelSpeeds[3]);

        feed();
    }

    public void drive() {
    }

    @Override
    public void stopMotor() {
        frontLeftMotor.stopMotor();
        frontRightMotor.stopMotor();
        backLeftMotor.stopMotor();
        backRightMotor.stopMotor();
        feed();
    }

    @Override
    public String getDescription() {
        return "Monarch Mecanum Drive";
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("MecanumDrive");
        builder.setActuator(true);
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty(
                "Front Left Motor Speed", this.frontLeftMotor::get, this.frontLeftMotor::set);
        builder.addDoubleProperty(
                "Front Right Motor Speed",
                () -> this.frontRightMotor.get(),
                value -> this.frontRightMotor.set(value));
        builder.addDoubleProperty("Rear Left Motor Speed", this.backLeftMotor::get, this.backLeftMotor::set);
        builder.addDoubleProperty(
                "Rear Right Motor Speed",
                () -> this.backRightMotor.get(),
                value -> this.backRightMotor.set(value));
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void resetMultiplier() {
        this.multiplier = 1;
    }

    @Override
    public void close() throws Exception {
        SendableRegistry.remove(this);
    }

}

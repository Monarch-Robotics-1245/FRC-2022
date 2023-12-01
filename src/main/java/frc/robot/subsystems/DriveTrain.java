package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DriveCommand;
import edu.wpi.first.wpilibj.SerialPort;


public class DriveTrain extends SubsystemBase{

    private final MonarchMecanumDrive mecanumDrive;

    private final CANSparkMax frontLeftMotor;
    private final CANSparkMax backLeftMotor;
    private final CANSparkMax frontRightMotor;
    private final CANSparkMax backRightMotor;
    private static final int BAUD_RATE = 115200;
    private static final int MAX_BYTES = 32;
    private final  SerialPort visionPort;

    private boolean isInverted = false;
    private boolean isIntakeAdjusting = false;
    // private final RelativeEncoder frontLeftEncoder;
    private NetworkTable rasTable;

    private NetworkTableInstance table;

    public DriveTrain() {
        table = NetworkTableInstance.getDefault();
        table.startClient();

        NetworkTable teamInfo = table.getTable("FMSInfo");
        NetworkTableEntry teamColorEntry = teamInfo.getEntry("IsRedAlliance");


        teamColorEntry.addListener((t) -> sendTeamColor(), 0);


        //TODO: CHANGE TABLE NAME
        rasTable = table.getTable("Placeholder");

        frontLeftMotor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeftMotor = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
        frontRightMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
        backRightMotor = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.visionPort = new SerialPort(BAUD_RATE, SerialPort.Port.kMXP);
        visionPort.readString();
        frontRightMotor.setInverted(!true);
        backLeftMotor.setInverted(!false);
        frontLeftMotor.setInverted(!false);
        backRightMotor.setInverted(!true);


        //  RelativeEncoder frontLeftEncoder = frontLeftMotor.getEncoder();

        setDefaultCommand(new DriveCommand(this));

    //    private final CANEncoder frontLeftEncoder = frontLeftMotor.getEncoder();
    //    private  final CANEncoder backLeftEncoder = backLeftMotor.getEncoder();
    //    private  final CANEncoder frontRightEncoder = frontRightMotor.getEncoder();
    //    private final CANEncoder backRightEncoder = backRightMotor.getEncoder();


        this.mecanumDrive = new MonarchMecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
    }

    public void drive(double y, double x, double rotation) {

        if(isInverted) {
            x = x;
            y = -y;
        }else{
            x = -x;
            y = y;
        }
        double tempIntakeAdjust = intakeAdjust(Math.abs(.25*(Math.sqrt((y*y) + (x*x)/Math.sqrt(2)))));
        if (isIntakeAdjusting && Math.abs(tempIntakeAdjust) >= 1){
            rotation = rotation + tempIntakeAdjust;
        }
        if (!isIntakeAdjusting){
            flush();

        }
        mecanumDrive.regularDrive(y*.8, x*.8, rotation*.7);
        mecanumDrive.feed();
    }

    private void adjustBallShootDir() {
        //TODO: CHANGE ENTRY NAME
        NetworkTableEntry xCenter = rasTable.getEntry("xCenter");
        double distanceFromCenter = xCenter.getDouble(0);

    }

    public void invert() {
        isInverted = !isInverted;
    }
    public void toggleAdjust() {
        isIntakeAdjusting = !isIntakeAdjusting;
        System.out.println(isIntakeAdjusting);
    }
    public void getData() {
        double value = 3;
        if(visionPort.getBytesReceived() != 0){
             value = Double.parseDouble(visionPort.readString());
        }else{
           // visionPort.writeString("i");
        }
        System.out.println(value);
    }

    public void sendTeamColor() {
        NetworkTable teamInfo = table.getTable("FMSInfo");
        NetworkTableEntry teamColorEntry = teamInfo.getEntry("IsRedAlliance");
        boolean isRedAlliance = teamColorEntry.getBoolean(false);
        if(isRedAlliance) {
            visionPort.writeString("R");
        }else{
            visionPort.writeString("B");
        }
    }

    public double intakeAdjust(double scalar){
        double value = 0;
        double value2 = 0;
        sendTeamColor();
       // while(visionPort.getBytesReceived() == 0);
        if(visionPort.getBytesReceived() != 0){
            try{
             value = Double.parseDouble(visionPort.readString());
             //System.out.println(value); 
            }
             catch(Exception e){
                 System.out.println("bruh");
             }
             flush();
        }

        
        value2 = value*scalar;
        return value2;
    }  
    public void flush(){
        visionPort.readString();
    }
    public void sendI(){
        visionPort.writeString("i");
    }
}


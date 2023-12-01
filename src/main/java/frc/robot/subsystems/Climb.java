package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ClimbCommand;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Climb extends SubsystemBase {

  private static DoubleSolenoid climbSolenoid;

  public Climb() {
      climbSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);
      setDefaultCommand(new ClimbCommand(this));
  }

  /**
   * Extends the pneumatic arm.
   */
  public void extendClimb()
  {
      climbSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  /**
   * Retracts the pneumatic arm.
   */
  public void retractClimb()
  {
      climbSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void toggleClimb()
  {
  }

  /**
   * @return Position of the pneumatic arm.
   */
  public static Value getValue()
  {
    return climbSolenoid.get();
  }

  @Override
  public void periodic() {
  }
}

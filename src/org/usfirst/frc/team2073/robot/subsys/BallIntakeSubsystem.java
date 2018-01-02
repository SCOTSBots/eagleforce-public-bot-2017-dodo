package org.usfirst.frc.team2073.robot.subsys;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Subsystems.BallIntake;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class BallIntakeSubsystem extends Subsystem {
	private final Victor motor1;
	private final Victor motor2;
	private final Solenoid solenoid;

	public BallIntakeSubsystem() {
		motor1 = RobotMap.getBallIntakeMotor1();
		motor2 = RobotMap.getBallIntakeMotor2();
		solenoid = RobotMap.getBallIntakeSolenoid();

		holdIntake();

		LiveWindow.addActuator(BallIntake.NAME, BallIntake.ComponentNames.MOTOR_1, motor1);
		LiveWindow.addActuator(BallIntake.NAME, BallIntake.ComponentNames.MOTOR_2, motor2);
		LiveWindow.addActuator(BallIntake.NAME, BallIntake.ComponentNames.SOLENOID, solenoid);
	}

	@Override
	public void initDefaultCommand() {
	}

	public void deployIntake() {
		solenoid.set(false);
	}
	
	public void holdIntake() {
		solenoid.set(true);
	}

	public void intakeBalls() {
		motor1.set(.8);
		motor2.set(.8);
	}

	public void reverseIntake() {
		motor1.set(-.8);
		motor2.set(-.8);
	}

	public void stopIntake() {
		motor1.set(0);
		motor2.set(0);
	}
}

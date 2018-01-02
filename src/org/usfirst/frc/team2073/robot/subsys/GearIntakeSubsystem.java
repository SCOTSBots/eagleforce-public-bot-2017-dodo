package org.usfirst.frc.team2073.robot.subsys;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Subsystems.GearIntake;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class GearIntakeSubsystem extends Subsystem {
	private final Victor intakeMotor;

	public GearIntakeSubsystem() {
		intakeMotor = RobotMap.getGearIntakeMotor();
		LiveWindow.addActuator(GearIntake.NAME, GearIntake.ComponentNames.INTAKE_MOTOR, intakeMotor);
	}

	@Override
	protected void initDefaultCommand() {
	}

	public void gearIn() {
		intakeMotor.set(-.65);
	}

	public void gearOut() {
		intakeMotor.set(.65);
	}

	public void gearHold() {
		intakeMotor.set(-.3);
	}

	public void gearStop() {
		intakeMotor.set(0);
	}
}

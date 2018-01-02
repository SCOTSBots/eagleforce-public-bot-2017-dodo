package org.usfirst.frc.team2073.robot.subsys;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Subsystems.Intermediate;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class IntermediateSubsystem extends Subsystem {
	private final Victor main;
	private final Victor bellyRoller;

	public IntermediateSubsystem() {
		main = RobotMap.getIntermediateBelts();
		bellyRoller = RobotMap.getBellyRoller();

		LiveWindow.addActuator(Intermediate.NAME, Intermediate.ComponentNames.MAIN, main);
		LiveWindow.addActuator(Intermediate.NAME, Intermediate.ComponentNames.BELL_ROLLER, bellyRoller);
	}

	@Override
	public void initDefaultCommand() {
	}

	public void intermediateOn() {
		main.set(1);
	}

	public void intermediateOff() {
		main.set(0);
	}

	public void bellyPanOn() {
		bellyRoller.set(1);
	}

	public void bellyPanOff() {
		bellyRoller.set(0);
	}
}

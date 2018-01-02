package org.usfirst.frc.team2073.robot.cmd.gearintake;

import org.usfirst.frc.team2073.robot.OI;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeRumbleCommand extends Command{
	private final Joystick controller;
	private final Joystick wheel;

	public GearIntakeRumbleCommand() {
		controller = OI.getController();
		wheel = OI.getWheel();
	}
	
	private void setRumble(GenericHID joystick, double value) {
		joystick.setRumble(RumbleType.kLeftRumble, value);
		joystick.setRumble(RumbleType.kRightRumble, value);
	}
	
	private void enableRumble(GenericHID joystick) {
		setRumble(joystick, 1);
	}
	
	private void disableRumble(GenericHID joystick) {
		setRumble(joystick, 0);
	}

	@Override
	protected void execute() {
		enableRumble(controller);
		enableRumble(wheel);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		disableRumble(controller);
		disableRumble(wheel);
	}
}

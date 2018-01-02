package org.usfirst.frc.team2073.robot.cmd.drive;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.DrivetrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ShiftCommand extends Command {
	private final DrivetrainSubsystem drivetrain;

	public ShiftCommand() {
		drivetrain = RobotMap.getDrivetrain();
		// requires(drivetrain);
	}

	@Override
	protected void execute() {
		drivetrain.shiftHighGear();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		drivetrain.shiftLowGear();
	}
}

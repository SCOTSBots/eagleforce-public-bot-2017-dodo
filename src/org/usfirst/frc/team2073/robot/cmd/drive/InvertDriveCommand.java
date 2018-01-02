package org.usfirst.frc.team2073.robot.cmd.drive;

import org.usfirst.frc.team2073.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class InvertDriveCommand extends Command {
	@Override
	protected void execute() {
		RobotMap.setBallIntakeForwards(true);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		RobotMap.setBallIntakeForwards(false);
	}
}

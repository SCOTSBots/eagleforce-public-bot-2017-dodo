package org.usfirst.frc.team2073.robot.cmd.intermediate;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.IntermediateSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class IntermediateCommand extends Command {
	private final IntermediateSubsystem intermediate;

	public IntermediateCommand() {
		intermediate = RobotMap.getIntermediate();
		requires(intermediate);
	}

	@Override
	protected void execute() {
		intermediate.intermediateOn();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intermediate.intermediateOff();
	}
}

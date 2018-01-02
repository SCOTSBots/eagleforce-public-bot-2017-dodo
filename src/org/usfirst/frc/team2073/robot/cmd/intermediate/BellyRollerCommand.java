package org.usfirst.frc.team2073.robot.cmd.intermediate;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.IntermediateSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class BellyRollerCommand extends Command {
	private final IntermediateSubsystem intermediate;

	public BellyRollerCommand() {
		intermediate = RobotMap.getIntermediate();
		requires(intermediate);
	}

	@Override
	protected void execute() {
		intermediate.bellyPanOn();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intermediate.bellyPanOff();
	}
}

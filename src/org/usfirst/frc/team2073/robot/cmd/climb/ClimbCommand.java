package org.usfirst.frc.team2073.robot.cmd.climb;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.ClimberSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbCommand extends Command {
	private final ClimberSubsystem climber;

	public ClimbCommand() {
		climber = RobotMap.getClimber();
		requires(climber);
	}

	@Override
	protected void execute() {
		climber.startClimb();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		climber.stopClimb();
	}
}

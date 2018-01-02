package org.usfirst.frc.team2073.robot.cmd.ballintake;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.BallIntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeBallsCommand extends Command {
	private final BallIntakeSubsystem ballIntake;

	public IntakeBallsCommand() {
		ballIntake = RobotMap.getBallIntake();
		requires(ballIntake);
	}

	@Override
	protected void execute() {
		ballIntake.intakeBalls();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		ballIntake.stopIntake();
	}
}

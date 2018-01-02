package org.usfirst.frc.team2073.robot.cmd.ballintake;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.BallIntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DeployBallIntakeCommand extends Command {
	private final BallIntakeSubsystem ballIntake;

	public DeployBallIntakeCommand() {
		ballIntake = RobotMap.getBallIntake();
		requires(ballIntake);
	}

	@Override
	protected void initialize() {
		ballIntake.deployIntake();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		ballIntake.holdIntake();
	}
}

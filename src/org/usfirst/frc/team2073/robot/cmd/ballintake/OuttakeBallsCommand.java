package org.usfirst.frc.team2073.robot.cmd.ballintake;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.BallIntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class OuttakeBallsCommand extends Command {
	private final BallIntakeSubsystem ballIntake;

	public OuttakeBallsCommand() {
		ballIntake = RobotMap.getBallIntake();
		requires(ballIntake);
	}

	@Override
	protected void execute() {
		ballIntake.reverseIntake();
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

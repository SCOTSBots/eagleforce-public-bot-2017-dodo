package org.usfirst.frc.team2073.robot.cmd.drive;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.DrivetrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ArcTurnMpCommand extends Command {
	private final double turnRadius;
	private final double angleTurn;
	private final boolean isRightTurn;
	private final DrivetrainSubsystem drivetrain;

	public ArcTurnMpCommand(double turnRadius, double angleTurn, boolean isRightTurn) {
		drivetrain = RobotMap.getDrivetrain();
		this.angleTurn = angleTurn;
		this.turnRadius = turnRadius;
		this.isRightTurn = isRightTurn;
		requires(drivetrain);
	}

	@Override
	protected void initialize() {
		drivetrain.autonArcTurn(angleTurn, turnRadius, isRightTurn);
	}

	@Override
	protected void execute() {
		drivetrain.processMotionProfiling();
	}

	@Override
	protected boolean isFinished() {
		return drivetrain.isMotionProfilingFinished();
	}

	@Override
	protected void end() {
		drivetrain.stopMotionProfiling();
	}

}

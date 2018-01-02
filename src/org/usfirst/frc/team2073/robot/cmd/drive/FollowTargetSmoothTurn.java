package org.usfirst.frc.team2073.robot.cmd.drive;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.DrivetrainSubsystem;
import org.usfirst.frc.team2073.robot.util.CameraMessageReceiver;

import edu.wpi.first.wpilibj.command.Command;

public class FollowTargetSmoothTurn extends Command {
	private final DrivetrainSubsystem drivetrain;
	private double lastAngleToTarget;

	public FollowTargetSmoothTurn() {
		drivetrain = RobotMap.getDrivetrain();
		requires(drivetrain);
	}

	@Override
	protected void initialize() {
		drivetrain.autonPointTurn(CameraMessageReceiver.getLastMessage().getAngleToTarget());
		lastAngleToTarget = CameraMessageReceiver.getLastMessage().getAngleToTarget();
	}

	@Override
	protected void execute() {
		if (CameraMessageReceiver.getLastMessage().isTracking()) {
			if (CameraMessageReceiver.getLastMessage().getAngleToTarget() > lastAngleToTarget + 1
					|| CameraMessageReceiver.getLastMessage().getAngleToTarget() < lastAngleToTarget - 1) {
				drivetrain.stopMotionProfiling();
				drivetrain.autonPointTurn(CameraMessageReceiver.getLastMessage().getAngleToTarget());
				lastAngleToTarget = CameraMessageReceiver.getLastMessage().getAngleToTarget();
			}
			drivetrain.processMotionProfiling();
		} else {
			System.out.println("No Camera Vision");
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}

package org.usfirst.frc.team2073.robot.cmd;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.DrivetrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class TeleoperatedInitCommand extends Command {
	private final DrivetrainSubsystem drivetrain;

	public TeleoperatedInitCommand() {
		drivetrain = RobotMap.getDrivetrain();
		requires(drivetrain);
	}

	@Override
	protected void initialize() {
		drivetrain.stopBrakeMode();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
}

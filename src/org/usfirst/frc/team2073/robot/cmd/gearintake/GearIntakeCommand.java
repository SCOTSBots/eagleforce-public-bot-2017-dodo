package org.usfirst.frc.team2073.robot.cmd.gearintake;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.GearIntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeCommand extends Command {
	private final GearIntakeSubsystem gearIntake;

	public GearIntakeCommand() {
		gearIntake = RobotMap.getGearIntake();
		requires(gearIntake);
	}

	@Override
	protected void execute() {
		gearIntake.gearIn();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		gearIntake.gearStop();
	}
}

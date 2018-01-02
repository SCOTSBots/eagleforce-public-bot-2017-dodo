package org.usfirst.frc.team2073.robot.cmd.gearintake;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.GearIntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeHoldCommand extends Command {
	private final GearIntakeSubsystem gearIntake;

	public GearIntakeHoldCommand() {
		gearIntake = RobotMap.getGearIntake();
		requires(gearIntake);
	}

	@Override
	protected void execute() {
		gearIntake.gearHold();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		gearIntake.gearStop();
	}

}

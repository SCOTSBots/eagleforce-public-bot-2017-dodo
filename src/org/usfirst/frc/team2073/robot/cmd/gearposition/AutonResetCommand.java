package org.usfirst.frc.team2073.robot.cmd.gearposition;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.subsys.GearPositionSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class AutonResetCommand extends Command {
	private final GearPositionSubsystem gearIntake;

	public AutonResetCommand() {
		gearIntake = RobotMap.getGearPosition();
		requires(gearIntake);
	}

	@Override
	protected void execute() {
		gearIntake.resetGearIntake();
	}
			
	@Override
	protected boolean isFinished() {
		return false;
	}
}

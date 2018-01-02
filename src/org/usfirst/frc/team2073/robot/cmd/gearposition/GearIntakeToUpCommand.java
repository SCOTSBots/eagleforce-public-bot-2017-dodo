package org.usfirst.frc.team2073.robot.cmd.gearposition;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.OI;
import org.usfirst.frc.team2073.robot.subsys.GearPositionSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeToUpCommand extends Command {
	private final GearPositionSubsystem gearIntake;

	public GearIntakeToUpCommand() {
		gearIntake = RobotMap.getGearPosition();
		requires(gearIntake);
	}

	@Override
	protected void initialize() {
		gearIntake.toUp(OI.getController().getPOV());
	}

	@Override
	protected void execute() {
		gearIntake.runMotionProfiling();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}

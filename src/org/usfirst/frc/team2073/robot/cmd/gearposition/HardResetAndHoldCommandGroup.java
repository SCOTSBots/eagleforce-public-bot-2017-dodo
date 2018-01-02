package org.usfirst.frc.team2073.robot.cmd.gearposition;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.cmd.gearintake.GearIntakeHoldCommand;
import org.usfirst.frc.team2073.robot.cmd.gearintake.GearIntakeRumbleCommand;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class HardResetAndHoldCommandGroup extends CommandGroup {
	private final DigitalInput sensor;

	public HardResetAndHoldCommandGroup() {
		sensor = RobotMap.getLightSensor();

		addParallel(new GearIntakeRumbleCommand(), 2);
		addParallel(new GearIntakeHardResetCommand());
		addParallel(new GearIntakeHoldCommand());
	}

	@Override
	protected boolean isFinished() {
		return !sensor.get(); // TODO check if needed
	}
}

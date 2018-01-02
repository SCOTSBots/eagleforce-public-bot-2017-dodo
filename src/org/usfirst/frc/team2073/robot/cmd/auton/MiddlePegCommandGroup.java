package org.usfirst.frc.team2073.robot.cmd.auton;

import org.usfirst.frc.team2073.robot.cmd.drive.MoveBackwardMpCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.MoveForwardMpCommand;
import org.usfirst.frc.team2073.robot.cmd.gearintake.GearOuttakeCommand;
import org.usfirst.frc.team2073.robot.cmd.gearposition.GearIntakeHardResetCommand;
import org.usfirst.frc.team2073.robot.cmd.gearposition.GearIntakeToDownCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class MiddlePegCommandGroup extends CommandGroup {
	public MiddlePegCommandGroup() {
		addParallel(new GearIntakeHardResetCommand(), 4);
		addSequential(new MoveForwardMpCommand(83));
		addSequential(new WaitCommand(1));
		addSequential(new GearOuttakeCommand(), 1);
		addParallel(new GearOuttakeCommand(), 2);
		addSequential(new WaitCommand(.5));
		addParallel(new GearIntakeToDownCommand());
		addSequential(new MoveBackwardMpCommand(22));
	}
}

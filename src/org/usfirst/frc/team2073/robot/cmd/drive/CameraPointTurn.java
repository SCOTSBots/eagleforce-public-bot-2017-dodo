package org.usfirst.frc.team2073.robot.cmd.drive;

import org.usfirst.frc.team2073.robot.util.CameraMessageReceiver;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CameraPointTurn extends CommandGroup {
	public CameraPointTurn() {
		addSequential(new PointTurnMpCommand(CameraMessageReceiver.getLastMessage().getAngleToTarget()));
	}
}

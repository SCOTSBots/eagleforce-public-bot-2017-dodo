package org.usfirst.frc.team2073.robot.util;

import com.ctre.CANTalon;

public class TalonHelper {
	public static void setFollowerOf(CANTalon follower, CANTalon talon) {
		follower.changeControlMode(CANTalon.TalonControlMode.Follower);
		follower.set(talon.getDeviceID());
	}
}

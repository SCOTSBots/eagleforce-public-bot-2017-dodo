package old.com.eagleforce.robot.service;

import edu.wpi.first.wpilibj.Victor;

public class ClimberService {

	private Victor climbMotor = new Victor(4);

	public void startClimb() {
		climbMotor.set(-1);
	}

	public void stopClimb() {
		climbMotor.set(0);
	}

}

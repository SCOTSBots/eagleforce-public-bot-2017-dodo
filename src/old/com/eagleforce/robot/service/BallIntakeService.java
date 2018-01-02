package old.com.eagleforce.robot.service;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class BallIntakeService {
	// TODO: extract channel to configuration
	private Victor intake = new Victor(5);
	private Victor intake2 = new Victor(6);
	private Solenoid intakeSol = new Solenoid(2);
	private Solenoid intakeSol2 = new Solenoid(3);

	public void intakeOut() {
		intakeSol.set(true);
		intakeSol2.set(true);
	}

//	not used on this bot
//	public void intakeBack() {
//		intakeSol.set(false);
//		intakeSol2.set(false);
//	}

	public void startIntake() {
		intake.set(1);
		intake2.set(1);
	}

	public void reverseIntake() {
		intake.set(-1);
		intake2.set(-1);
	}

	public void stopIntake() {
		intake.set(0);
		intake2.set(0);
	}
}

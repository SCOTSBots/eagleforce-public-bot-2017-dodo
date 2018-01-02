package old.com.eagleforce.robot.service;

import edu.wpi.first.wpilibj.Victor;

public class IntermediateService {
	private Victor main = new Victor(8);
	private Victor bellyRoller = new Victor(7);

	public void intermediateOn() {
		main.set(1);
	}

	public void intermediateOff() {
		main.set(0);
	}

	public void bellyPanOn() {
		bellyRoller.set(1);
	}

	public void bellyPanOff() {
		bellyRoller.set(0);

	}

}

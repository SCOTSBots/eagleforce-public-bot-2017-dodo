package old.com.eagleforce.robot.controller;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import old.com.eagleforce.robot.service.ClimberService;
import old.com.eagleforce.robot.service.DriverStationService;

public class ClimberController {

	private ClimberService climbSvc = new ClimberService();
	private DriverStationService dvrSvc = new DriverStationService();

	private Thread climbControl = new Thread() {
		@Override
		public void run() {
			while (RobotState.isEnabled()) {

				if (RobotState.isAutonomous()) {
					// put auto here
				} else if (RobotState.isOperatorControl()) {
					if (dvrSvc.climberButton()) {
						climbSvc.startClimb();
					} else {
						climbSvc.stopClimb();
					}
				}
				Timer.delay(.005);
			}
		}
	};

	public void init() {
		climbControl.start();
	}

}

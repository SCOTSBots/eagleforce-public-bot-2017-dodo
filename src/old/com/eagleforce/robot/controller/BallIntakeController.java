package old.com.eagleforce.robot.controller;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import old.com.eagleforce.robot.service.BallIntakeService;
import old.com.eagleforce.robot.service.DriverStationService;
import old.com.eagleforce.robot.service.IntermediateService;

public class BallIntakeController {

	private DriverStationService dvrSvc = new DriverStationService();
	private BallIntakeService ballSvc = new BallIntakeService();
	private IntermediateService intSvc = new IntermediateService();

	private Thread ballIntakeControl = new Thread() {
		@Override
		public void run() {
			while (RobotState.isEnabled()) {
				if (RobotState.isAutonomous()) {
					// put auto here
				} else if (RobotState.isOperatorControl()) {
					ballSvc.intakeOut();

					if (dvrSvc.ballIntakeButton()) {
						ballSvc.startIntake();
						intSvc.bellyPanOn();

					} else if (dvrSvc.reverseBallIntakeButton()) {
						ballSvc.reverseIntake();
					} else {
						ballSvc.stopIntake();
						intSvc.bellyPanOff();
					}

				}
				Timer.delay(.005);
			}

		}
	};

	public void init() {
		ballIntakeControl.start();
	}

}

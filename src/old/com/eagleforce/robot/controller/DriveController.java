package old.com.eagleforce.robot.controller;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import old.com.eagleforce.robot.service.DriverStationService;
import old.com.eagleforce.robot.service.DrivetrainService;
import old.com.eagleforce.robot.util.SuppressibleLogger;

public class DriveController {
	private final SuppressibleLogger log = new SuppressibleLogger(this.getClass());

	private DriverStationService dvrSvc = new DriverStationService();
	private DrivetrainService dTrainSvc = new DrivetrainService();

	private Thread driveControl = new Thread() {
		@Override
		public void run() {
			dTrainSvc.setSlave();
			boolean generatingTrajectoryPoints = false;
			while (true) {
				if (RobotState.isAutonomous()) {
					// put auto here
				} else if (RobotState.isOperatorControl()) {
					if (!dvrSvc.toggleDriveMode()) {
						log.log("MotionProfile button inactive event fired. Stopping MotionProfile.");
						dTrainSvc.stopMotionProfile();
						log.log("MotionProfile stopped successfully.");
						dTrainSvc.changeControlMode(TalonControlMode.PercentVbus);

						if (dvrSvc.pointTurnButton()) {
							dTrainSvc.pTurn(dvrSvc.wheelX());
						} else if (dvrSvc.toggleDriveDirection()){
							dTrainSvc.move(dvrSvc.joystickY(), dvrSvc.wheelX());
						}else if (!dvrSvc.toggleDriveDirection()) {
							dTrainSvc.moveBackwards(dvrSvc.joystickY(), dvrSvc.wheelX());
						}
						if (dvrSvc.shiftButton()) {
							dTrainSvc.shiftHighGear();
						} else {
							dTrainSvc.shiftLowGear();
						}
					} else {
						// REMOVE LATER!!! FOR TESTING ONLY
						if (dvrSvc.gearIntakeButton() && !generatingTrajectoryPoints) {
							System.out.println("motion profile set");
							log.log("Generate TrajectoryPoints button active event fired. Generating TrajectoryPoints...");
							dTrainSvc.generateTrajPointList();
							dTrainSvc.startFilling();
							dTrainSvc.resetEncoders();
							log.log("Generated trajectory points successfully.");
							System.out.println("motion profile finished set");
							generatingTrajectoryPoints = true;
						} else if (dvrSvc.gearIntakeButton()) {
							dTrainSvc.changeControlMode(TalonControlMode.MotionProfile);
							/* Only START the profile the first packet that the button is active */
							log.log("MotionProfile button active event fired. Starting MotionProfile.");
							dTrainSvc.startMotionProfile();
							log.log("MotionProfile started successfully.");

						} else {
							generatingTrajectoryPoints = false;
							dTrainSvc.stopMotionProfile();
						}
					}
					dTrainSvc.processPeriodic();
				}
				Timer.delay(.005);
			}
		}
	};

	public void init() {
		driveControl.start();
	}

}

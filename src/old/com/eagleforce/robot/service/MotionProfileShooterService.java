package old.com.eagleforce.robot.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import old.com.eagleforce.robot.model.MotionProfileConfiguration;

public class MotionProfileShooterService {
	
	private static final double MIN_PROFILE_DISTANCE = 10;

	private CANTalon talon = new CANTalon(4);

	private MotionProfileGenerationService mpgSvc = new MotionProfileGenerationService();
	private CameraService cam = CameraService.getInstance();
	private MotionProfileConfiguration conf1;

	public List<TrajectoryPoint> turretProfile;
	
	
	enum MotionProfileState {
		INACTIVE, FILLING_BUFFER, RUNNING_PROFILE
	}

	private List<CANTalon> ctList = new ArrayList<>();
	private CANTalon.MotionProfileStatus talonStatus = new CANTalon.MotionProfileStatus();
	private static final double MAX_VELOCITY = 300;
	private static final int INTERVAL = 10;
	private static final double MAX_ACCELERATION = 30;
	private static final double GEAR_RATIO = 10.*(7./3.); /* this to one */

	public MotionProfileShooterService() {
		SmartDashboard.putNumber("Fgain", .7871);
		for (CANTalon ct : ctList) {
			ct.changeMotionControlFramePeriod(INTERVAL / 2);
		}

	}
	
	public boolean targetOffScreen() {
		boolean offScreen = !cam.lastCameraMessage().isTracking();
		if(offScreen)
			System.out.println("Off screen");
		return offScreen;
	}
	
	public boolean targetLocked() {
		if(targetOffScreen())
			return false;
		
		boolean locked = Math.abs(cam.lastCameraMessage().getAngleToTarget()) < 1;
		if(locked)
			System.out.println("Target locked!");
		return locked;
	}

	public void generateProfile() {
		conf1 = new MotionProfileConfiguration();
		conf1.setMaxVel(MAX_VELOCITY);
		conf1.setInterval(INTERVAL);
		conf1.setEndDistance(profileDistanceToTarget());
		conf1.setMaxAcc(MAX_ACCELERATION);
		turretProfile = mpgSvc.generatePoints(conf1);
		System.out.println("generated");
	}

	public double profileDistanceToTarget() {
		double dist = 0;
		double camDist = Math.abs(cam.lastCameraMessage().getAngleToTarget()/360);
		camDist *= GEAR_RATIO;
		camDist = Math.max(camDist, MIN_PROFILE_DISTANCE);
//		dist *= GEAR_RATIO;
		dist = camDist;
		return dist;
	}

	public void motionProfileInit() {
		talon.setF(SmartDashboard.getNumber("Fgain", .7871));
		talon.changeControlMode(TalonControlMode.MotionProfile);
		talon.set(CANTalon.SetValueMotionProfile.Disable.value);
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talon.clearMotionProfileTrajectories();
	}

	public void pushPoints(List<TrajectoryPoint> tpList) {
		Queue<TrajectoryPoint> tpQueue = new LinkedList<>(tpList);
		for (TrajectoryPoint tp : tpQueue) {
			talon.pushMotionProfileTrajectory(tp);
		}
	}

	public void processPoints() {
		talon.processMotionProfileBuffer();
		talon.set(CANTalon.SetValueMotionProfile.Enable.value);
//		System.out.println("Processing");
	}

	public void resetEnc() {
		talon.setPosition(0);
		talon.setEncPosition(0);
	}

	public void stopMotionProfile() {
		talon.set(CANTalon.SetValueMotionProfile.Disable.value);
		talon.clearMotionProfileTrajectories();
	}

	public void printTalonInfo() {
		System.out.println("Pos: " + talon.getPosition() + "topBuffer: " + talonStatus.topBufferCnt);
		talon.changeControlMode(TalonControlMode.PercentVbus);
	}

	public void setDirection(boolean forwards) {
		talon.reverseOutput(forwards);
	}

	public boolean isToTheRight() {
		boolean direction = false;
		if (cam.lastCameraMessage().getAngleToTarget() > 0)
			direction = true;
		else
			direction = false;
		return direction;
	}
	
	public double bufferedPoints(){
		double points = talonStatus.topBufferCnt;
		return points;
	}
	
	public void printCameraInfo() {
		SmartDashboard.putNumber("CamDistance", cam.lastCameraMessage().getDistanceToTarget());
		SmartDashboard.putNumber("CamAngle", cam.lastCameraMessage().getAngleToTarget());
		SmartDashboard.putNumber("CamTime", cam.lastCameraMessage().getTimeOfImage());
		SmartDashboard.putBoolean("CamTracking", cam.lastCameraMessage().isTracking());
	}
	
	public double angle() {
		return cam.lastCameraMessage().getAngleToTarget();
	}

}

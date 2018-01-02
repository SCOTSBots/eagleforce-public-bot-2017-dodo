package old.com.eagleforce.robot.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import old.com.eagleforce.robot.model.MotionProfileConfiguration;

public class MotionProfileGearIntakeService extends GearIntakeService {
	private CANTalon talon = new CANTalon(4);

	private MotionProfileGenerationService mpgSvc = new MotionProfileGenerationService();
	private MotionProfileConfiguration conf1;
	private MotionProfileConfiguration conf2;
	private MotionProfileConfiguration conf3;
	private MotionProfileConfiguration conf4;
	private MotionProfileConfiguration conf5;
	private MotionProfileConfiguration conf6;

	public List<TrajectoryPoint> upToDownTpList;
	public List<TrajectoryPoint> upToPlaceTpList;
	public List<TrajectoryPoint> placeToUpTpList;
	public List<TrajectoryPoint> placeToDownTpList;
	public List<TrajectoryPoint> downToPlaceTpList;
	public List<TrajectoryPoint> downToUpTpList;
	
	enum MotionProfileState {
		INACTIVE, FILLING_BUFFER, RUNNING_PROFILE
	}
	private List<CANTalon> ctList = new ArrayList<>();
	private CANTalon.MotionProfileStatus talonStatus = new CANTalon.MotionProfileStatus();


	public MotionProfileGearIntakeService() {
		SmartDashboard.putNumber("Fgain", .7871);


		conf1 = new MotionProfileConfiguration();
		// up to down
		conf1.setForwards(false);
		conf1.setMaxVel(300);
		conf1.setInterval(5);
		conf1.setEndDistance(250);
		conf1.setMaxAcc(60);

		conf2 = new MotionProfileConfiguration();
		// up to place
		conf2.setForwards(false);
		conf2.setMaxVel(3);
		conf2.setInterval(5);
		conf2.setEndDistance(.125);
		conf2.setMaxAcc(60);

		conf3 = new MotionProfileConfiguration();
		// place to up (reverse)
		conf3.setForwards(false);
		conf3.setMaxVel(3);
		conf3.setInterval(5);
		conf3.setEndDistance(.125);
		conf3.setMaxAcc(60);

		conf4 = new MotionProfileConfiguration();
		// place to down
		conf4.setForwards(true);
		conf4.setMaxVel(3);
		conf4.setInterval(5);
		conf4.setEndDistance(.125);
		conf4.setMaxAcc(60);

		conf5 = new MotionProfileConfiguration();
		// down to place (reverse)
		conf5.setForwards(false);
		conf5.setMaxVel(3);
		conf5.setInterval(5);
		conf5.setEndDistance(.125);
		conf5.setMaxAcc(60);

		conf6 = new MotionProfileConfiguration();
		// down to up (reverse)
		conf6.setForwards(false);
		conf6.setMaxVel(3);
		conf6.setInterval(5);
		conf6.setEndDistance(.25);
		conf6.setMaxAcc(60);

		upToDownTpList = mpgSvc.generatePoints(conf1);
		upToPlaceTpList = mpgSvc.generatePoints(conf2);
		placeToUpTpList = mpgSvc.generatePoints(conf3);
		placeToDownTpList = mpgSvc.generatePoints(conf4);
		downToPlaceTpList = mpgSvc.generatePoints(conf5);
		downToUpTpList = mpgSvc.generatePoints(conf6);
		
		for (CANTalon ct : ctList) {
			ct.changeMotionControlFramePeriod((int) SmartDashboard.getNumber("Interval", 10) / 2);
		}

	}
	public void motionProfileInit() {
		talon.setF(SmartDashboard.getNumber("Fgain", .7871));
		talon.changeControlMode(TalonControlMode.MotionProfile);
		talon.set(CANTalon.SetValueMotionProfile.Disable.value);
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
	}
	
	public void checkDirection(boolean forwards){
		talon.reverseOutput(forwards);
	}


}

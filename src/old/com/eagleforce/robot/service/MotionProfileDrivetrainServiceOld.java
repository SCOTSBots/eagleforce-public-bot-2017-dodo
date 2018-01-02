package old.com.eagleforce.robot.service;

import java.util.List;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import old.com.eagleforce.robot.model.MotionProfileConfiguration;

public class MotionProfileDrivetrainServiceOld {

	private MotionProfileGenerationService mps = new MotionProfileGenerationService();
	private CANTalon talonL = new CANTalon(3);
	private CANTalon talonR = new CANTalon(7);
	private CANTalon talonL2 = new CANTalon(1);
	private CANTalon talonR2 = new CANTalon(8);

	private MotionProfileConfiguration conf1;

	private List<TrajectoryPoint> moveForward;

	public MotionProfileDrivetrainServiceOld() {
		talonL.setF(.5);
		talonR.setF(.5);
		talonL.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		talonR.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		talonL2.changeControlMode(CANTalon.TalonControlMode.Follower);
		talonR2.changeControlMode(CANTalon.TalonControlMode.Follower);
		SmartDashboard.putNumber("Max Velocity", 10);
		SmartDashboard.putNumber("Max Acceleration", 10);
		SmartDashboard.putNumber("Distance", 10);
		talonL2.set(talonL.get());
		conf1 = new MotionProfileConfiguration();
	}

	public void setValues() {
		conf1.setMaxVel(SmartDashboard.getNumber("Max Velocity", 1));
		conf1.setInterval(10);
		conf1.setEndDistance(SmartDashboard.getNumber("Distance", 0));
		conf1.setMaxAcc(SmartDashboard.getNumber("Max Velocity", 1));
		moveForward = mps.generatePoints(conf1);
	}

	public void startFillingL(List<TrajectoryPoint> tpList) {
		talonL.changeControlMode(TalonControlMode.MotionProfile);
		talonL.set(CANTalon.SetValueMotionProfile.Enable.value);
		talonL.clearMotionProfileTrajectories();
		System.out.println("filling left");
		for (TrajectoryPoint tp : tpList) {
			talonL.pushMotionProfileTrajectory(tp);
		}
	}

	public void startFillingR(List<TrajectoryPoint> tpList) {
		talonR.changeControlMode(TalonControlMode.MotionProfile);
		talonR.set(CANTalon.SetValueMotionProfile.Enable.value);
		talonR.clearMotionProfileTrajectories();
		System.out.println("filling right");
		for (TrajectoryPoint tp : tpList) {
			talonR.pushMotionProfileTrajectory(tp);
		}
	}

	public List<TrajectoryPoint> getMoveForward() {
		return moveForward;
	}

	public void setMoveForward(List<TrajectoryPoint> moveForward) {
		this.moveForward = moveForward;
	}

	public void runMotionProfile() {
//		System.out.println("processing motion profile buffers");
//		talonL.
		talonL.processMotionProfileBuffer();
		talonR.processMotionProfileBuffer();

	}

}

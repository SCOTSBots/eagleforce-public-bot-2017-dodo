package old.com.eagleforce.robot.service;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivetrainService {

	private CANTalon lMotor = new CANTalon(4);
	private CANTalon lMotor2 = new CANTalon(1);
	private CANTalon rMotor = new CANTalon(7);
	private CANTalon rMotor2 = new CANTalon(8);
	private Solenoid sol1 = new Solenoid(0);
	private Solenoid sol2 = new Solenoid(1);

	private MotionProfileDriveTrainService mpdtSvc = new MotionProfileDriveTrainService(lMotor, lMotor2, rMotor,
			rMotor2);

	double preTurn;

	public DrivetrainService() {

	}

	public void setSlave() {
		lMotor2.changeControlMode(TalonControlMode.Follower);
		rMotor2.changeControlMode(TalonControlMode.Follower);
		rMotor2.set(rMotor.getDeviceID());
		lMotor2.set(lMotor.getDeviceID());
	}

	public double turnSense(double Ptart) {
		double sTurn = SmartDashboard.getNumber("Sense", .7) * Ptart * Ptart * Ptart
				+ Ptart * (1 - SmartDashboard.getNumber("Sense", .7));
		return sTurn;
	}

	public double inverse(double Start) {
		double inv = (Start - preTurn) * SmartDashboard.getNumber("Inverse", .2) + Start;
		return inv;
	}

	public void pTurn(double turn) {
		rMotor.set(-turn);
		lMotor.set(-turn);
	}

	public void move(double speed, double turn) {
		rMotor.set(-inverse(speed) - (inverse(speed) * turnSense(turn)));
		lMotor.set(inverse(speed) + (inverse(speed) * turnSense(turn)));
	}

	public void moveBackwards(double speed, double turn) {
		rMotor.set(inverse(speed) + (inverse(speed) * turnSense(turn)));
		lMotor.set(-inverse(speed) - (inverse(speed) * turnSense(turn)));
	}

	public void shiftHighGear() {
		sol1.set(true);
		sol2.set(true);
	}

	public void shiftLowGear() {
		sol1.set(false);
		sol2.set(true);
	}

	// MotionProfileDrivetrainService delegation methods
	// ====================================================================================================
	public void startMotionProfile() {
		mpdtSvc.startMotionProfile();

	}

	public void generateTrajPointList() {
		mpdtSvc.generateTrajPointList();
	}

	public void stopMotionProfile() {
		mpdtSvc.stopMotionProfile();
	}

	public void changeControlMode(TalonControlMode mode) {
		mpdtSvc.changeControlMode(mode);
	}

	public void processPeriodic() {
		mpdtSvc.processPeriodic();
	}

	public void startFilling() {
		mpdtSvc.startFilling();

	}

	public void moveMotionProfile() {
		mpdtSvc.moveMotionProfile();
	}

	public void resetEncoders() {
		mpdtSvc.resetEnc();
	}
}

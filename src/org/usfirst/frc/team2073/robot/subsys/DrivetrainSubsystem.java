package org.usfirst.frc.team2073.robot.subsys;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.cmd.drive.DriveWithJoystickCommand;
import org.usfirst.frc.team2073.robot.conf.AppConstants.DashboardKeys;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Defaults;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Subsystems.Drivetrain;
import org.usfirst.frc.team2073.robot.domain.MotionProfileConfiguration;
import org.usfirst.frc.team2073.robot.util.MotionProfileGenerator;
import org.usfirst.frc.team2073.robot.util.MotionProfileHelper;
import org.usfirst.frc.team2073.robot.util.TalonHelper;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivetrainSubsystem extends Subsystem {
	public static final double DEFAULT_INVERSE = .2;
	public static final double DEFAULT_SENSE = .7;

	private final CANTalon leftMotor;
	private final CANTalon leftMotorSlave;
	private final CANTalon rightMotor;
	private final CANTalon rightMotorSlave;
	private final Solenoid solenoid1;
	private final Solenoid solenoid2;
	private final ADXRS450_Gyro gyro;

	public DrivetrainSubsystem() {
		leftMotor = RobotMap.getLeftMotor();
		leftMotorSlave = RobotMap.getLeftMotorSlave();
		rightMotor = RobotMap.getRightMotor();
		rightMotorSlave = RobotMap.getRightMotorSlave();
		solenoid1 = RobotMap.getDriveSolenoid1();
		solenoid2 = RobotMap.getDriveSolenoid2();
		gyro = RobotMap.getGyro();

		setSlaves();
		shiftLowGear();
		configEncoders();
		initTalons();
		enableBrakeMode();
		configSmartDashboard();

		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.LEFT_MOTOR, leftMotor);
		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.LEFT_MOTOR_SLAVE, leftMotorSlave);
		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.RIGHT_MOTOR, rightMotor);
		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.RIGHT_MOTOR_SLAVE, rightMotorSlave);
		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.SOLENOID_1, solenoid1);
		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.SOLENOID_2, solenoid2);
		LiveWindow.addActuator(Drivetrain.NAME, Drivetrain.ComponentNames.GYRO, gyro);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystickCommand());
	}

	private void setSlaves() {
		TalonHelper.setFollowerOf(leftMotorSlave, leftMotor);
		TalonHelper.setFollowerOf(rightMotorSlave, rightMotor);
	}

	private void configEncoders() {
		leftMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		rightMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		leftMotor.configEncoderCodesPerRev(1024);
		rightMotor.configEncoderCodesPerRev(1024);
	}

	private void initTalons() {
		MotionProfileHelper.initTalon(leftMotor, DashboardKeys.LEFTDRIVEFGAIN, Defaults.LEFTDRIVEFGAIN);
		MotionProfileHelper.initTalon(rightMotor, DashboardKeys.RIGHTDRIVEFGAIN, Defaults.RIGHTDRIVEFGAIN);
	}

	public MotionProfileConfiguration driveStraightConfig(double linearDistInInches) {
		MotionProfileConfiguration configuration = new MotionProfileConfiguration();
		// TODO: check if high gear is enabled
		double rotationDist = (8.2 * Drivetrain.LOW_GEAR_RATIO * linearDistInInches) / (Drivetrain.WHEEL_DIAMETER * 5);
		configuration.setEndDistance(rotationDist);
		configuration.setInterval(10);
		configuration.setMaxVel(Drivetrain.AUTONOMOUS_MAX_VELOCITY);
		configuration.setMaxAcc(Drivetrain.AUTONOMOUS_MAX_ACCELERATION);
		configuration.setVelocityOnly(false);
		return configuration;
	}

	public MotionProfileConfiguration pointTurnConfig(double angleTurn) {
		MotionProfileConfiguration configuration = new MotionProfileConfiguration();
		double linearDist = (angleTurn / 360) * (Drivetrain.ROBOT_WIDTH * Math.PI);
		double rotationDist = (5.15 * Drivetrain.LOW_GEAR_RATIO * linearDist) / (Drivetrain.WHEEL_DIAMETER * 5);
		// TODO: find out why the 8 and 5 are needed
		configuration.setEndDistance(rotationDist);
		configuration.setInterval(10);
		configuration.setMaxVel(Drivetrain.AUTONOMOUS_MAX_VELOCITY);
		configuration.setMaxAcc(Drivetrain.AUTONOMOUS_MAX_ACCELERATION);
		configuration.setVelocityOnly(false);
		return configuration;
	}

	/**
	 * List of profiles, first for outside then for inside.
	 */
	public ArrayList<MotionProfileConfiguration> straightIntoTurn(double linearDistanceInInches, double angleTurn) {
		MotionProfileConfiguration configuration1 = new MotionProfileConfiguration();
		MotionProfileConfiguration configuration2 = new MotionProfileConfiguration();
		double outsideLinearDistance = (angleTurn / 360) * (Drivetrain.ROBOT_WIDTH * Math.PI) + linearDistanceInInches;
		double outsideRotations = (11.85 * Drivetrain.LOW_GEAR_RATIO * outsideLinearDistance)
				/ (Drivetrain.WHEEL_DIAMETER * 5);
		double insideRotations = (7.8 * Drivetrain.LOW_GEAR_RATIO * linearDistanceInInches)
				/ (Drivetrain.WHEEL_DIAMETER * 5);
		configuration1.setEndDistance(outsideRotations);
		configuration2.setEndDistance(insideRotations);
		configuration1.setInterval(10);
		configuration1.setMaxVel(Drivetrain.AUTONOMOUS_MAX_VELOCITY);
		configuration1.setMaxAcc(Drivetrain.AUTONOMOUS_MAX_ACCELERATION);
		configuration1.setVelocityOnly(false);
		configuration2.setInterval(10);
		configuration2.setMaxVel(Drivetrain.AUTONOMOUS_MAX_VELOCITY);
		configuration2.setMaxAcc(Drivetrain.AUTONOMOUS_MAX_ACCELERATION);
		configuration2.setVelocityOnly(false);
		ArrayList<MotionProfileConfiguration> configList = new ArrayList<MotionProfileConfiguration>();
		configList.add(configuration1);
		configList.add(configuration2);
		return configList;
	}

	/**
	 * List of profiles, first for outside then for inside.
	 */
	public ArrayList<MotionProfileConfiguration> arcTurnConfiguration(double angleTurn, double turnRadius,
			boolean isRightTurn) {
		MotionProfileConfiguration configuration1 = new MotionProfileConfiguration();
		MotionProfileConfiguration configuration2 = new MotionProfileConfiguration();

		double outsideLinearDistance = 2 * Math.PI * (turnRadius + Drivetrain.ROBOT_WIDTH) * (angleTurn / 360);
		double insideLinearDistance = 2 * Math.PI * turnRadius * (angleTurn / 360);
		double outsideRotations = (7.8 * Drivetrain.LOW_GEAR_RATIO * outsideLinearDistance)
				/ (Drivetrain.WHEEL_DIAMETER * 5);
		double insideRotations = (7.8 * Drivetrain.LOW_GEAR_RATIO * insideLinearDistance)
				/ (Drivetrain.WHEEL_DIAMETER * 5);
		double time = outsideRotations/ Drivetrain.AUTONOMOUS_MAX_VELOCITY;
		double interiorVelocity = insideRotations / time;

		configuration1.setEndDistance(outsideRotations);
		configuration1.setMaxVel(Drivetrain.AUTONOMOUS_MAX_VELOCITY);
		configuration2.setEndDistance(insideRotations);
		configuration2.setMaxVel(interiorVelocity);

		configuration1.setForwards(true);
		configuration1.setInterval(10);
		configuration1.setMaxAcc(Drivetrain.AUTONOMOUS_MAX_ACCELERATION);
		configuration1.setVelocityOnly(false);

		configuration2.setForwards(true);
		configuration2.setInterval(10);
		configuration2.setMaxAcc(Drivetrain.AUTONOMOUS_MAX_ACCELERATION);
		configuration2.setVelocityOnly(false);

		ArrayList<MotionProfileConfiguration> configList = new ArrayList<MotionProfileConfiguration>();
		configList.add(configuration1);
		configList.add(configuration2);
		return configList;
	}

	public double turnSense(double notPopTart) {
		double sense = SmartDashboard.getNumber(DashboardKeys.SENSE, DEFAULT_SENSE);
		return sense * notPopTart * notPopTart * notPopTart + notPopTart * (1 - sense);
	}

	public double inverse(double start) {
		double inverse = SmartDashboard.getNumber(DashboardKeys.INVERSE, DEFAULT_INVERSE);
		return (start) * inverse + start;
	}

	public void pointTurn(double turn) {
		rightMotor.set(-turn);
		leftMotor.set(-turn);
	}

	public void move(double speed, double turn) {
		rightMotor.changeControlMode(TalonControlMode.PercentVbus);
		leftMotor.changeControlMode(TalonControlMode.PercentVbus);

		double rightSide = -(inverse(speed) - (inverse(speed) * turnSense(turn)));
		double leftSide = inverse(speed) + (inverse(speed) * turnSense(turn));

		if (RobotMap.isBallIntakeForwards()) {
			rightMotor.set(rightSide);
			leftMotor.set(leftSide);
		} else {
			leftMotor.set(rightSide);
			rightMotor.set(leftSide);
		}
	}

	public void shiftHighGear() {
		solenoid1.set(true);// TODO: rename misleading
							// shiftHighGear/shiftLowGear names
		solenoid2.set(false);
	}

	public void shiftLowGear() {
		solenoid1.set(false);
		solenoid2.set(true);
	}

	public void resetMotionProfiling(MotionProfileConfiguration config, boolean leftForwards, boolean rightForwards) {
		List<TrajectoryPoint> trajPointList = MotionProfileGenerator.generatePoints(config);
		MotionProfileHelper.resetAndPushPoints(leftMotor, trajPointList, leftForwards);
		MotionProfileHelper.resetAndPushPoints(rightMotor, trajPointList, rightForwards);
		leftMotor.setPosition(0);
		rightMotor.setPosition(0);

	}

	public void processMotionProfiling() {
		MotionProfileHelper.processPoints(leftMotor);
		MotionProfileHelper.processPoints(rightMotor);
	}

	public void stopMotionProfiling() {
		MotionProfileHelper.stopTalon(leftMotor);
		MotionProfileHelper.stopTalon(rightMotor);
	}

	public boolean isMotionProfilingFinished() {
		// TODO: decide whether to check if both are finished or if at least one
		// is finished
		return MotionProfileHelper.isFinished(leftMotor) && MotionProfileHelper.isFinished(rightMotor);
	}

	public void autonDriveForward(double linearDistInInches) {
		resetMotionProfiling(driveStraightConfig(linearDistInInches), true, false);
	}

	public void autonPointTurn(double angle) {
		if (angle > 0)
			resetMotionProfiling(pointTurnConfig(Math.abs(angle)), false, false);
		else
			resetMotionProfiling(pointTurnConfig(Math.abs(angle)), true, true);

	}

	public void autonDriveBackward(double linearDistInInches) {
		resetMotionProfiling(driveStraightConfig(linearDistInInches), false, true);
	}

	public void autonStraightDriveIntoTurn(double linearDistanceInInches, double angleTurn) {
		List<TrajectoryPoint> outsideTpList = MotionProfileGenerator
				.generatePoints(straightIntoTurn(linearDistanceInInches, angleTurn).get(0));
		List<TrajectoryPoint> insideTpList = MotionProfileGenerator
				.generatePoints(straightIntoTurn(linearDistanceInInches, angleTurn).get(1));

		if (angleTurn < 0) {
			MotionProfileHelper.resetAndPushPoints(leftMotor, outsideTpList, true);
			MotionProfileHelper.resetAndPushPoints(rightMotor, insideTpList, false);
		} else {
			MotionProfileHelper.resetAndPushPoints(rightMotor, outsideTpList, false);
			MotionProfileHelper.resetAndPushPoints(leftMotor, insideTpList, true);
		}
	}

	public void autonArcTurn(double angleTurn, double turnRadius, boolean isRightTurn) {
		List<TrajectoryPoint> outsideTpList = MotionProfileGenerator
				.generatePoints(arcTurnConfiguration(angleTurn, turnRadius, isRightTurn).get(0));
		List<TrajectoryPoint> insideTpList = MotionProfileGenerator
				.generatePoints(arcTurnConfiguration(angleTurn, turnRadius, isRightTurn).get(1));
		if (isRightTurn) {
			MotionProfileHelper.resetAndPushPoints(leftMotor, outsideTpList, true);
			MotionProfileHelper.resetAndPushPoints(rightMotor, insideTpList, false);
		} else {
			MotionProfileHelper.resetAndPushPoints(rightMotor, outsideTpList, false);
			MotionProfileHelper.resetAndPushPoints(leftMotor, insideTpList, true);
		}
	}

	public void stopBrakeMode() {
		leftMotor.enableBrakeMode(false);
		rightMotor.enableBrakeMode(false);
	}

	public void enableBrakeMode() {
		leftMotor.enableBrakeMode(true);
		rightMotor.enableBrakeMode(true);
	}

	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public void changeFGain(CANTalon motor, double value, String smartDashboardKey, double defaultF) {
		MotionProfileHelper.changeF(motor, value, smartDashboardKey, defaultF);
	}

	public void adjustF(double startingGryo) {
		if (getGyroAngle() < startingGryo - .2) {
			changeFGain(leftMotor, .01, DashboardKeys.LEFTDRIVEFGAIN, Defaults.LEFTDRIVEFGAIN);
			changeFGain(rightMotor, -.01, DashboardKeys.RIGHTDRIVEFGAIN, Defaults.RIGHTDRIVEFGAIN);
		} else if (getGyroAngle() > startingGryo + .2) {
			changeFGain(rightMotor, .01, DashboardKeys.RIGHTDRIVEFGAIN, Defaults.RIGHTDRIVEFGAIN);
			changeFGain(leftMotor, -.01, DashboardKeys.LEFTDRIVEFGAIN, Defaults.LEFTDRIVEFGAIN);
		}
	}

	private void configSmartDashboard() {
		SmartDashboard.putNumber(DashboardKeys.LEFTDRIVEFGAIN, Defaults.LEFTDRIVEFGAIN);
		SmartDashboard.putNumber(DashboardKeys.RIGHTDRIVEFGAIN, Defaults.RIGHTDRIVEFGAIN);

	}
}

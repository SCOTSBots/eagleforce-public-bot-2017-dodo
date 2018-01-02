package org.usfirst.frc.team2073.robot;

import org.usfirst.frc.team2073.robot.conf.AppConstants.RobotPorts;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Subsystems.*;
import org.usfirst.frc.team2073.robot.subsys.BallIntakeSubsystem;
import org.usfirst.frc.team2073.robot.subsys.ClimberSubsystem;
import org.usfirst.frc.team2073.robot.subsys.DrivetrainSubsystem;
import org.usfirst.frc.team2073.robot.subsys.GearIntakeSubsystem;
import org.usfirst.frc.team2073.robot.subsys.GearPositionSubsystem;
import org.usfirst.frc.team2073.robot.subsys.IntermediateSubsystem;
import org.usfirst.frc.team2073.robot.subsys.TurretSubsystem;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotMap {
	private static DrivetrainSubsystem drivetrain;
	private static boolean ballIntakeForwards = false;
	private static CANTalon leftMotor = new CANTalon(RobotPorts.LEFT_MOTOR);
	private static CANTalon leftMotorSlave = new CANTalon(RobotPorts.LEFT_MOTOR_SLAVE);
	private static CANTalon rightMotor = new CANTalon(RobotPorts.RIGHT_MOTOR);
	private static CANTalon rightMotorSlave = new CANTalon(RobotPorts.RIGHT_MOTOR_SLAVE);
	private static Solenoid driveSolenoid1 = new Solenoid(RobotPorts.DRIVE_SOLENOID_1);
	private static Solenoid driveSolenoid2 = new Solenoid(RobotPorts.DRIVE_SOLENOID_2);
	private static ADXRS450_Gyro gyro = new ADXRS450_Gyro();

	private static TurretSubsystem turret;
	private static CANTalon turretPosition = new CANTalon(RobotPorts.TURRET_POSITION);
	private static CANTalon shooter1 = new CANTalon(RobotPorts.TURRET_SHOOTER_1);
	private static CANTalon shooter2 = new CANTalon(RobotPorts.TURRET_SHOOTER_2);

	private static BallIntakeSubsystem ballIntake;
	private static Solenoid ballIntakeSolenoid = new Solenoid(RobotPorts.BALL_INTAKE_SOLENOID);
	private static Victor ballIntakeMotor1 = new Victor(RobotPorts.BALL_INTAKE_MOTOR_1);
	private static Victor ballIntakeMotor2 = new Victor(RobotPorts.BALL_INTAKE_MOTOR_2);

	private static ClimberSubsystem climber;
	private static Victor climberMotor = new Victor(RobotPorts.CLIMBER_MOTOR);

	private static IntermediateSubsystem intermediate;
	private static Victor bellyRoller = new Victor(RobotPorts.BELLY_ROLLERS);
	private static Victor intermediateBelts = new Victor(RobotPorts.INTERMEDIATE);

	private static GearPositionSubsystem gearPosition;
	private static GearIntakeSubsystem gearIntake;
	private static Victor gearIntakeMotor = new Victor(RobotPorts.GEAR_INTAKE_MOTOR);
	private static CANTalon gearIntakeTalon = new CANTalon(RobotPorts.GEAR_INTAKE_TALON);
	private static DigitalInput lightSensor = new DigitalInput(RobotPorts.LIGHT_SENSOR);
	private static DigitalInput magnetZeroer = new DigitalInput(RobotPorts.MAGNET_ZEROER);

	static void init() {
		drivetrain = new DrivetrainSubsystem();
		gearPosition = new GearPositionSubsystem();
		ballIntake = new BallIntakeSubsystem();
		climber = new ClimberSubsystem();
		turret = new TurretSubsystem();
		intermediate = new IntermediateSubsystem();
		gearIntake = new GearIntakeSubsystem();

		SmartDashboard.putData(Drivetrain.NAME, drivetrain);
		SmartDashboard.putData(Turret.NAME, turret);
		SmartDashboard.putData(GearPosition.NAME, gearPosition);
		SmartDashboard.putData(GearIntake.NAME, gearIntake);
		SmartDashboard.putData(BallIntake.NAME, ballIntake);
		SmartDashboard.putData(Intermediate.NAME, intermediate);
		SmartDashboard.putData(Climber.NAME, climber);
	}

	// Drivetrain
	// ====================================================================================================
	public static DrivetrainSubsystem getDrivetrain() {
		return drivetrain;
	}

	public static CANTalon getLeftMotor() {
		return leftMotor;
	}

	public static CANTalon getLeftMotorSlave() {
		return leftMotorSlave;
	}

	public static CANTalon getRightMotor() {
		return rightMotor;
	}

	public static CANTalon getRightMotorSlave() {
		return rightMotorSlave;
	}

	public static Solenoid getDriveSolenoid1() {
		return driveSolenoid1;
	}

	public static Solenoid getDriveSolenoid2() {
		return driveSolenoid2;
	}

	// Gear Intake
	// ====================================================================================================
	public static GearIntakeSubsystem getGearIntake() {
		return gearIntake;
	}

	public static GearPositionSubsystem getGearPosition() {
		return gearPosition;
	}

	public static Victor getGearIntakeMotor() {
		return gearIntakeMotor;
	}

	public static CANTalon getGearIntakeTalon() {
		return gearIntakeTalon;
	}

	public static DigitalInput getLightSensor() {
		return lightSensor;
	}

	public static DigitalInput getMagnetZeroer() {
		return magnetZeroer;
	}

	// Ball Intake
	// ====================================================================================================
	public static BallIntakeSubsystem getBallIntake() {
		return ballIntake;
	}

	public static void setBallIntakeForwards(boolean ballIntakeForwards) {
		RobotMap.ballIntakeForwards = ballIntakeForwards;
	}

	public static boolean isBallIntakeForwards() {
		return ballIntakeForwards;
	}

	public static Solenoid getBallIntakeSolenoid() {
		return ballIntakeSolenoid;
	}

	public static Victor getBallIntakeMotor1() {
		return ballIntakeMotor1;
	}

	public static Victor getBallIntakeMotor2() {
		return ballIntakeMotor2;
	}

	// Climber
	// ====================================================================================================
	public static ClimberSubsystem getClimber() {
		return climber;
	}

	public static Victor getClimberMotor() {
		return climberMotor;
	}

	// Intermediate
	// ====================================================================================================
	public static IntermediateSubsystem getIntermediate() {
		return intermediate;
	}

	public static Victor getBellyRoller() {
		return bellyRoller;
	}

	public static Victor getIntermediateBelts() {
		return intermediateBelts;
	}

	// Turret
	// ====================================================================================================
	public static TurretSubsystem getTurret() {
		return turret;
	}

	public static CANTalon getTurretPosition() {
		return turretPosition;
	}

	public static CANTalon getShooter1() {
		return shooter1;
	}

	public static CANTalon getShooter2() {
		return shooter2;
	}

	public static ADXRS450_Gyro getGyro() {
		return gyro;
	}
}

package org.usfirst.frc.team2073.robot.subsys;

import java.util.List;

import org.usfirst.frc.team2073.robot.RobotMap;
import org.usfirst.frc.team2073.robot.conf.AppConstants.DashboardKeys;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Subsystems.Turret;
import org.usfirst.frc.team2073.robot.domain.MotionProfileConfiguration;
import org.usfirst.frc.team2073.robot.util.MotionProfileGenerator;
import org.usfirst.frc.team2073.robot.util.MotionProfileHelper;
import org.usfirst.frc.team2073.robot.util.TalonHelper;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretSubsystem extends Subsystem {
	private static final double DEFAULT_F = 4.0;
	private static final double DEFAULT_P = 30;
	private static final double DEFAULT_I = 0;
	private static final double DEFAULT_D = 170;
	private static final double MAX_ACCELERATION = 800;
	private static final double MAX_VELOCITY = 4000;

	private final CANTalon turretPos;
	private final CANTalon shooter1;
	private final CANTalon shooter2;

	private List<TrajectoryPoint> trajPoints;

	public TurretSubsystem() {
		turretPos = RobotMap.getTurretPosition();
		shooter1 = RobotMap.getShooter1();
		shooter2 = RobotMap.getShooter2();

		initTurretPos();
		generateTrajPoints();
		TalonHelper.setFollowerOf(shooter2, shooter1);

		LiveWindow.addActuator(Turret.NAME, Turret.ComponentNames.POS, turretPos);
		LiveWindow.addActuator(Turret.NAME, Turret.ComponentNames.SHOOTER_1, shooter1);
		LiveWindow.addActuator(Turret.NAME, Turret.ComponentNames.SHOOTER_2, shooter2);
	}

	@Override
	public void initDefaultCommand() {
	}

	private void initTurretPos() {
		SmartDashboard.putNumber(DashboardKeys.SET_F, DEFAULT_F);
		SmartDashboard.putNumber(DashboardKeys.SET_P, DEFAULT_P);
		SmartDashboard.putNumber(DashboardKeys.SET_I, DEFAULT_I);
		SmartDashboard.putNumber(DashboardKeys.SET_D, DEFAULT_D);
		SmartDashboard.putNumber(DashboardKeys.RPM, MAX_VELOCITY);

		turretPos.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		turretPos.reverseSensor(false);
		turretPos.configEncoderCodesPerRev(Turret.TURRET_POSITION_CODES_PER_REV);
		turretPos.configNominalOutputVoltage(+0.0f, -0.0f);
		turretPos.configPeakOutputVoltage(+12.0f, 0.0f);
		turretPos.setProfile(0);
		turretPos.setF(SmartDashboard.getNumber(DashboardKeys.SET_F, DEFAULT_F));
		turretPos.setP(SmartDashboard.getNumber(DashboardKeys.SET_P, DEFAULT_P));
		turretPos.setI(SmartDashboard.getNumber(DashboardKeys.SET_I, DEFAULT_I));
		turretPos.setD(SmartDashboard.getNumber(DashboardKeys.SET_D, DEFAULT_D));
	}

	private void generateTrajPoints() {
		MotionProfileConfiguration config = new MotionProfileConfiguration();
		config.setVelocityOnly(true);
		config.setForwards(true);
		config.setInterval(10);
		config.setMaxAcc(MAX_ACCELERATION);
		config.setMaxVel(SmartDashboard.getNumber(DashboardKeys.RPM, MAX_VELOCITY) * 60);
		trajPoints = MotionProfileGenerator.generatePoints(config);
	}

	public void turretMove(int angle) {
		turretPos.changeControlMode(TalonControlMode.Position);
		double rotations = angle / 360.0;
		turretPos.set(rotations);
	}

	public void accelerateToRPM() {
		MotionProfileHelper.resetAndPushPoints(shooter1, trajPoints, true);
		while (!MotionProfileHelper.isFinished(shooter1)) {
			MotionProfileHelper.processPoints(shooter1);
		}
	}

	public void sustainRPM() {
		// TODO: FIND A method for this
	}
}

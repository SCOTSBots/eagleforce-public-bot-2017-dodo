package org.usfirst.frc.team2073.robot;

import org.usfirst.frc.team2073.robot.buttons.JoystickPOV;
import org.usfirst.frc.team2073.robot.buttons.MultiTrigger;
import org.usfirst.frc.team2073.robot.buttons.RobotModeTrigger;
import org.usfirst.frc.team2073.robot.buttons.RobotModeTrigger.RobotMode;
import org.usfirst.frc.team2073.robot.buttons.Sensor;
import org.usfirst.frc.team2073.robot.cmd.climb.ClimbCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.ArcTurnMpCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.CameraPointTurn;
import org.usfirst.frc.team2073.robot.cmd.drive.DriveStraightAndPivotTurnCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.FollowTargetSmoothTurn;
import org.usfirst.frc.team2073.robot.cmd.drive.InvertDriveCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.MoveForwardMpCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.PointTurnCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.PointTurnMpCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.ShiftCommand;
import org.usfirst.frc.team2073.robot.cmd.drive.TuneFCommand;
import org.usfirst.frc.team2073.robot.cmd.gearintake.GearIntakeCommand;
import org.usfirst.frc.team2073.robot.cmd.gearintake.GearOuttakeCommand;
import org.usfirst.frc.team2073.robot.cmd.gearposition.GearIntakeToDownCommand;
import org.usfirst.frc.team2073.robot.cmd.gearposition.GearIntakeToPlaceCommand;
import org.usfirst.frc.team2073.robot.cmd.gearposition.HardResetAndHoldCommandGroup;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Controllers.DriveWheel;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Controllers.PowerStick;
import org.usfirst.frc.team2073.robot.conf.AppConstants.Controllers.Xbox;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

public class OI {
	private static Joystick controller = new Joystick(Xbox.PORT);
	private static Joystick joystick = new Joystick(PowerStick.PORT);
	private static Joystick wheel = new Joystick(DriveWheel.PORT);

	static void init() {
		Command gearDown = new GearIntakeToDownCommand();		
		Command gearPlace = new GearIntakeToPlaceCommand();
		Command gearIntake = new GearIntakeCommand();
		Command gearOuttake = new GearOuttakeCommand();
		Command shift = new ShiftCommand();
		Command pointTurn = new PointTurnCommand();
//		Command intakeBalls = new IntakeBallsCommand();
//		Command outtakeBalls = new OuttakeBallsCommand();
		Command autonPointTurn = new PointTurnMpCommand(90);
		Command climb = new ClimbCommand();
		Command toggleDriveDirection = new InvertDriveCommand(); 
		Command gearHardResetAndHold = new HardResetAndHoldCommandGroup();
		Command mpMove = new MoveForwardMpCommand(60);
		Command tuneF = new TuneFCommand();
		Command cammeraSmoothFollow = new FollowTargetSmoothTurn();
		Command cameraPointTurn = new CameraPointTurn();
		Command driveAndPivot = new DriveStraightAndPivotTurnCommand(25, 45);
		Command arcTurn = new ArcTurnMpCommand(48, 90, true);
		
		JoystickButton x = new JoystickButton(controller, Xbox.ButtonPorts.X);
		JoystickButton a = new JoystickButton(controller, Xbox.ButtonPorts.A);
		JoystickButton b = new JoystickButton(controller, Xbox.ButtonPorts.B);
		JoystickButton y = new JoystickButton(controller, Xbox.ButtonPorts.Y);
		JoystickButton leftBumper = new JoystickButton(controller, Xbox.ButtonPorts.L1);
		JoystickButton leftJoy = new JoystickButton(joystick, PowerStick.ButtonPorts.LEFT);
		JoystickButton leftPaddle = new JoystickButton(wheel, DriveWheel.ButtonPorts.LEFT_PADDLE);
		JoystickButton rightBumper = new JoystickButton(controller, Xbox.ButtonPorts.R1);
		JoystickButton back = new JoystickButton(controller, Xbox.ButtonPorts.BACK);
		JoystickButton wheelX = new JoystickButton(wheel, DriveWheel.ButtonPorts.X);
		JoystickButton wheelO = new JoystickButton(wheel, DriveWheel.ButtonPorts.O);
		JoystickButton wheelSquare = new JoystickButton(wheel, DriveWheel.ButtonPorts.SQUARE);
		JoystickButton joystickCenter = new JoystickButton(joystick, PowerStick.ButtonPorts.CENTER);
		JoystickPOV dpadDown = new JoystickPOV(controller, 180);
		JoystickPOV dpadRight = new JoystickPOV(controller, 90);
		Trigger lightSensor = new MultiTrigger(new Sensor(RobotMap.getLightSensor()),
				new RobotModeTrigger(RobotMode.AUTONOMOUS, false));
		
		joystickCenter.toggleWhenPressed(toggleDriveDirection);
//		back.whenPressed(driveAndPivot);
//		wheelX.whenPressed(arcTurn);
//		wheelO.toggleWhenPressed(cammeraSmoothFollow);
//		wheelSquare.whenPressed(cameraPointTurn);
		a.whileHeld(gearIntake);
		b.whileHeld(gearOuttake);
		dpadDown.whileActive(gearDown);
		dpadRight.whileActive(gearPlace);
		x.toggleWhenPressed(tuneF);
		y.whileHeld(climb);
		leftBumper.whenPressed(arcTurn);
		leftJoy.toggleWhenPressed(shift);
		leftPaddle.whileHeld(pointTurn);
		rightBumper.whenPressed(arcTurn);
		lightSensor.whileActive(gearHardResetAndHold);
	}

	public static Joystick getController() {
		return controller;
	}

	public static Joystick getWheel() {
		return wheel;
	}

	public static Joystick getJoystick() {
		return joystick;
	}
}

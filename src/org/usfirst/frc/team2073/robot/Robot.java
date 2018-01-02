package org.usfirst.frc.team2073.robot;

import org.usfirst.frc.team2073.robot.cmd.TeleoperatedInitCommand;
import org.usfirst.frc.team2073.robot.cmd.auton.BlueBoilerSideCommandGroup;
import org.usfirst.frc.team2073.robot.cmd.auton.BlueFarSidePegCommandGroup;
import org.usfirst.frc.team2073.robot.cmd.auton.MiddlePegCommandGroup;
import org.usfirst.frc.team2073.robot.cmd.auton.RedBoilerSideCommandGroup;
import org.usfirst.frc.team2073.robot.cmd.auton.RedFarSidePegCommandGroup;
import org.usfirst.frc.team2073.robot.cmd.drive.MoveForwardMpCommand;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private SendableChooser<Command> chooser = new SendableChooser<>();
	private TeleoperatedInitCommand teleopInitCmd;
	private boolean started = false;
	
	@Override
	public void robotInit() {
		RobotMap.init();
		OI.init();
		
		teleopInitCmd = new TeleoperatedInitCommand();
		
		chooser.addDefault("Cross Baseline", new MoveForwardMpCommand(100));
		chooser.addObject("Center Peg", new MiddlePegCommandGroup());
		chooser.addObject("Red Far Side Peg", new RedFarSidePegCommandGroup());
		chooser.addObject("Blue Far Side Peg", new BlueFarSidePegCommandGroup());
		chooser.addObject("Red Boiler Side", new RedBoilerSideCommandGroup());
		chooser.addObject("Blue Boiler Side", new BlueBoilerSideCommandGroup());
		
		SmartDashboard.putData("Auto Mode", chooser);
	}

	@Override
	public void autonomousInit() {
		chooser.getSelected().start();
	}

	@Override
	public void autonomousPeriodic() {
		if(!started) {
			chooser.getSelected().start();
			started = true;
		}
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		chooser.getSelected().cancel();
		teleopInitCmd.start();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		System.out.println("Gyro: " + RobotMap.getGyro().getAngle());
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}

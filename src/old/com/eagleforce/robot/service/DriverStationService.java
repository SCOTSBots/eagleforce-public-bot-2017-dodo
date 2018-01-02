package old.com.eagleforce.robot.service;

import edu.wpi.first.wpilibj.Joystick;

public class DriverStationService {

	private Joystick controller = new Joystick(0);
	private Joystick joystick = new Joystick(1);
	private Joystick wheel = new Joystick(2);
	private boolean result = false;
	private boolean previous = true;

	// CONTROLLER
	// ==============================================================================================================================================================================

	// gear intake
	// ============================================
	public boolean gearIntakeButton() {
		if (controller.getRawButton(2))
			return true;
		return false;
	}

	public boolean gearPlaceButton() {
		if (controller.getRawButton(7))
			return true;
		return false;
	}

	public int gearPositionDPad() {
		return controller.getPOV();
	}

	// Ball Intake
	// ============================================
	public boolean ballIntakeButton() {
		if (controller.getRawButton(3))
			return true;
		return false;
	}

	public boolean reverseBallIntakeButton() {
		if (controller.getRawButton(5))
			return true;
		return false;
	}

	// climber
	// ============================================
	public boolean climberButton() {
		if (controller.getRawButton(4))
			return true;
		return false;
	}

	// shooter
	public double controllerJoystickAngle() {
		return controller.getDirectionDegrees();
	}
	
	public boolean alignTurret(){
		return controller.getRawButton(1);
	}
	
	
	public boolean toggleDriveMode() {
//		toggles between manual and motion profiled drive
		if (previous && climberButton()) {
			previous = false;
			if (result)
			  result = false;
			else
				result = true;
		}else if (!climberButton())
			previous = true;
		
		return result;
	}

	// STEERING WHEEL
	// ==============================================================================================================================================================================

	public boolean pointTurnButton() {
		if (wheel.getRawButton(9))
			return true;
		return false;
	}

	public double wheelX() {
		return wheel.getX();
	}

	// JOYSTICK
	// ==============================================================================================================================================================================

	public boolean gearShiftButton() {
		if (joystick.getRawButton(4))
			return true;
		return false;
	}

	public double joystickY() {
		return joystick.getY();
	}

	public boolean shiftButton() {
		if (joystick.getRawButton(4))
			return true;
		return false;
	}
public boolean toggleDriveDirection() {
		
		if (previous && joystick.getRawButton(4)) {
			previous = false;
			if (result)
			  result = false;
			else
				result = true;
		}else if (!joystick.getRawButton(4))
			previous = true;
		
		return result;
	}

}

package old.com.eagleforce.robot.service;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class GearIntakeService {
	private Victor intakeMotor = new Victor(9);
	private CANTalon talon = new CANTalon(1);
	private DigitalInput lightSensor = new DigitalInput(2);
	private DigitalInput magnetZeroer = new DigitalInput(1);

	public void gearIn() {
		intakeMotor.set(-1);
	}

	public void gearOut() {
		intakeMotor.set(1);
	}

	public void gearHold() {
		intakeMotor.set(.1);
	}

	public void gearStop() {
		intakeMotor.set(0);
	}

	public double getApproxAngle() {
		double angle;
		angle = Math.round(talon.getPosition());
		return angle;
	}

	public boolean lightSensor() {
		boolean sense;
		sense = lightSensor.get();
		return sense;
	}

	public void resetGearIntake() {
		talon.changeControlMode(TalonControlMode.PercentVbus);
		talon.set(-.5);
	}
	public boolean isZero(){
		return magnetZeroer.get();
	}


}

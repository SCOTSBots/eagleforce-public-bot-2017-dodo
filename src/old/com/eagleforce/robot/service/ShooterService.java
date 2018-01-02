package old.com.eagleforce.robot.service;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Victor;

public class ShooterService {

	// private CANTalon shoot1 = new CANTalon(2);
	// private CANTalon shoot2 = new CANTalon(3);
	// private IntermediateService ammo = new IntermediateService();
	private Victor turret = new Victor(1);
	private CANTalon talon = new CANTalon(4);
//	conf.setVelocityOnly(true)

	public void moveTurret(double rotate) {
//		enc.setDistancePerPulse(45 / 128);
		if (Math.round(rotate) > Math.round(talon.getPosition()) ) {
			talon.set(.1);
		} else if (rotate < talon.getPosition() ) {
			talon.set(-.1);
		} else {
			turret.set(0);
		}
		
	
		
		
		

	}

}

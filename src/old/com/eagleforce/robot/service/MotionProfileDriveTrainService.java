package old.com.eagleforce.robot.service;

import java.util.ArrayList;
import java.util.List;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.MotionProfileStatus;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import old.com.eagleforce.robot.model.MotionProfileConfiguration;
import old.com.eagleforce.robot.util.SuppressibleLogger;

public class MotionProfileDriveTrainService {

	enum MotionProfileState {
		INACTIVE, FILLING_BUFFER, RUNNING_PROFILE
	}

	private final SuppressibleLogger log = new SuppressibleLogger(this.getClass());
	private MotionProfileGenerationService mpgSvc = new MotionProfileGenerationService();

	private List<CANTalon> ctList = new ArrayList<>();
	private List<MotionProfileStatus> mpsList = new ArrayList<>();
	private List<TrajectoryPoint> tpList = new ArrayList<>();
	private CANTalon lMotor;
	private CANTalon rMotor;

	private CANTalon.MotionProfileStatus lMotorStatus = new CANTalon.MotionProfileStatus();
	private CANTalon.MotionProfileStatus rMotorStatus = new CANTalon.MotionProfileStatus();

	private MotionProfileConfiguration conf1 = new MotionProfileConfiguration();

	/**
	 * reference to the talon we plan on manipulating. We will not changeMode() or
	 * call set(), just get motion profile status and make decisions based on motion
	 * profile.
	 */

	/**
	 * State machine to make sure we let enough of the motion profile stream to
	 * talon before we fire it.
	 */
	private MotionProfileState _state = MotionProfileState.INACTIVE;
	/**
	 * Any time you have a state machine that waits for external events, its a good
	 * idea to add a timeout. Set to -1 to disable. Set to nonzero to count down to
	 * '0' which will print an error message. Counting loops is not a very accurate
	 * method of tracking timeout, but this is just conservative timeout. Getting
	 * time-stamps would certainly work too, this is just simple (no need to worry
	 * about timer overflows).
	 */
	private int _loopTimeout = -1;
	/**
	 * If start() gets called, this flag is set and in the control() we will service
	 * it.
	 */
	private boolean _bStart = false;

	/**
	 * Since the CANTalon.set() routine is mode specific, deduce what we want the
	 * set value to be and let the calling module apply it whenever we decide to
	 * switch to MP mode.
	 */
	private CANTalon.SetValueMotionProfile _setValue = CANTalon.SetValueMotionProfile.Disable;
	/**
	 * How many trajectory points do we wait for before firing the motion profile.
	 */
	private static final int kMinPointsInTalon = 0;
	/**
	 * Just a state timeout to make sure we don't get stuck anywhere. Each loop is
	 * about 20ms.
	 */
	private static final int kNumLoopsTimeout = 10;

	private static final int encTpr = 1024;

	private Notifier _notifer = new Notifier(new CANTalonBufferProcessor(ctList));

	private double gearRatio() {
		double ratio = 0;
		if (SmartDashboard.getBoolean("isHighGear", false))
			ratio = 4.89;
		else if (SmartDashboard.getBoolean("isHighGear", false))
			ratio = 15.41;
		else
			SmartDashboard.putString("INVALID INPUT", "GEAR RATIO");
		return ratio;
	}

	/**
	 * C'tor
	 * 
	 * @param talon
	 *            reference to Talon object to fetch motion profile status from.
	 */
	public MotionProfileDriveTrainService(CANTalon lMotor, CANTalon lMotorSlave, CANTalon rMotor,
			CANTalon rMotorSlave) {
		log.log("Constructing MotionProfileDriveTrainService.");

		// Initialize config
		generateTrajPointList();

		this.lMotor = lMotor;
		this.rMotor = rMotor;

		// Create a list of the talons so we can easily process repetitive
		// functions
		ctList.add(this.lMotor);
		// ctList.add(this.lMotorSlave); // Comment once setup with master/slave
		// properly
//		ctList.add(this.rMotor);
		// ctList.add(this.rMotorSlave); // Comment once setup with master/slave
		// properly

		mpsList.add(lMotorStatus);
//		mpsList.add(rMotorStatus);
		SmartDashboard.putNumber("Fgain", 60);
		lMotor.setF(SmartDashboard.getNumber("Fgain", 1));
		rMotor.setF(SmartDashboard.getNumber("Fgain", 1));
		lMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		rMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		// TODO: Gear Ratios
		// high gear ratio 4.89:1
		// low gear ratio 15.41:1
		lMotor.configEncoderCodesPerRev(encTpr);
		rMotor.configEncoderCodesPerRev(encTpr);
		lMotor.reverseOutput(true);
		lMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		lMotorSlave.set(lMotor.getDeviceID());
		rMotorSlave.set(rMotor.getDeviceID());
		SmartDashboard.putNumber("Max Velocity", 3);
		SmartDashboard.putNumber("Max Acceleration", 100);
		SmartDashboard.putNumber("Distance", 10);
		SmartDashboard.putNumber("Interval", 1);
		SmartDashboard.putBoolean("Log MotionProfile Stats", true);
		SmartDashboard.putBoolean("isHighGear", false);

		/*
		 * since our MP is 10ms per point, set the control frame rate and the notifer to
		 * half that
		 */
		for (CANTalon ct : ctList) {
			ct.changeMotionControlFramePeriod((int) SmartDashboard.getNumber("Interval", 10) / 2);
		}

		log.log("Constructing MotionProfileDriveTrainService finished successfully.");
	}

	/**
	 * Called by application to signal Talon to start the buffered MP (when it's
	 * able to).
	 */
	public void startMotionProfile() {
		log.log("in startMotionProfile()");
		_bStart = true;
	}

	public void moveMotionProfile() {
		for (CANTalon ct : ctList) {
			log.log("ControlMode change is to MotionProfile mode, setting values on Talon.");
			ct.set(_setValue.value);
			log.log("Values set successfully.");

		}
		SmartDashboard.putNumber("Position Value Left", lMotor.getPosition());
		SmartDashboard.putNumber("Position Value Right", rMotor.getPosition());
	}

	public void generateTrajPointList() {
		log.log("in generateTrajPointList()");
		conf1 = new MotionProfileConfiguration();
		conf1.setMaxVel(SmartDashboard.getNumber("Max Velocity", 1));
		conf1.setInterval((int) SmartDashboard.getNumber("Interval", 1));
		conf1.setEndDistance(SmartDashboard.getNumber("Distance", 0) * gearRatio());
		conf1.setMaxAcc(SmartDashboard.getNumber("Max Velocity", 1));
		conf1.setVelocityOnly(false);
		this.tpList = mpgSvc.generatePoints(conf1);
		_notifer.startPeriodic(SmartDashboard.getNumber("Interval", 10) / 2000);
	}

	/**
	 * Called to clear Motion profile buffer and reset state info during disabled
	 * and when Talon is not in MP control mode.
	 */
	public void stopMotionProfile() {
		log.log("in stopMotionProfile()");
		/*
		 * Let's clear the buffer just in case user decided to disable in the middle of
		 * an MP, and now we have the second half of a profile just sitting in memory.
		 */
		log.log("Clearing TrajectoryPoints from Talon.");
		for (CANTalon ct : ctList) {
			ct.clearMotionProfileTrajectories();
		}

		/* When we do re-enter motionProfile control mode, stay disabled. */
		_setValue = CANTalon.SetValueMotionProfile.Disable;

		/* When we do start running our state machine start at the beginning. */
		_state = MotionProfileState.INACTIVE;
		_loopTimeout = -1;

		/*
		 * If application wanted to start an MP before, ignore and wait for next button
		 * press
		 */
		_bStart = false;
	}

	public void changeControlMode(TalonControlMode mode) {
		log.log("in changeControlMode()");
		for (CANTalon ct : ctList) {
			log.log(String.format("Changing TalonControlMode from [%s] to [%s].", ct.getControlMode().name(),
					mode.name()), "Changing TalonControlMode");
			ct.changeControlMode(mode);
			if (mode == TalonControlMode.MotionProfile) {
				log.log("ControlMode change is to MotionProfile mode");
			}
		}
	}

	/**
	 * Called every loop.
	 */
	public void processPeriodic() {
		/* Get the motion profile status every loop */
		lMotor.getMotionProfileStatus(lMotorStatus);
		rMotor.getMotionProfileStatus(rMotorStatus);
		moveMotionProfile();
		/*
		 * track time, this is rudimentary but that's okay, we just want to make sure
		 * things never get stuck.
		 */
		if (_loopTimeout < 0) {
			/* do nothing, timeout is disabled */
		} else {
			/* our timeout is nonzero */
			if (_loopTimeout == 0) {
				/*
				 * something is wrong. Talon is not present, unplugged, breaker tripped
				 */
				MotionProfileLoggingService.OnNoProgress();
			} else {
				--_loopTimeout;
			}
		}

		/* first check if we are in MP mode */
		if (findControlMode(ctList) != TalonControlMode.MotionProfile) {
			/*
			 * we are not in MP mode. We are probably driving the robot around using
			 * gamepads or some other mode.
			 */
			log.log("Not in MotionProfile mode, do nothing.");
			_state = MotionProfileState.INACTIVE;
			_loopTimeout = -1;
		} else {
			/*
			 * we are in MP control mode. That means: starting Mps, checking Mp progress,
			 * and possibly interrupting MPs if thats what you want to do.
			 */
			log.log("In MotionProfile mode. Evaluating MotionProfileState of " + _state);
			switch (_state) {
			case INACTIVE: /* wait for application to tell us to start an MP */
				log.log("MotionProfileState is INACTIVE. Evaluating start flag of " + _bStart);
				if (_bStart) {
					log.log("Start flag is active.");

					_setValue = CANTalon.SetValueMotionProfile.Disable;
					log.log("Starting TrajectoryPoint filling.");
					log.log("TrajectoryPoint filling completed successfully.");
					/*
					 * MP is being sent to CAN bus, wait a small amount of time
					 */
					log.log("Setting MotionProfileState to: " + MotionProfileState.FILLING_BUFFER);
					_state = MotionProfileState.FILLING_BUFFER;
					_loopTimeout = kNumLoopsTimeout;
					_bStart = false;
				}
				break;
			case FILLING_BUFFER:
				log.log("MotionProfileState is FILLING_BUFFER. Checking if buffer has finished filling.");
				/*
				 * wait for MP to stream to Talon, really just the first few points
				 */
				/* do we have a minimum number of points in Talon */
				if (buffersFilled(mpsList, kMinPointsInTalon)) {
					log.log("Buffer has finished filling. Setting MotionProfileState to RUNNING_PROFILE.");
					/* start (once) the motion profile */
					/* MP will start once the control frame gets scheduled */
					_state = MotionProfileState.RUNNING_PROFILE;
					_loopTimeout = kNumLoopsTimeout;
				}
				break;
			case RUNNING_PROFILE: /* check the status of the MP */
				log.log("MotionProfileState is RUNNING_PROFILE.");
				_setValue = CANTalon.SetValueMotionProfile.Enable;
				/*
				 * if talon is reporting things are good, keep adding to our timeout. Really
				 * this is so that you can unplug your talon in the middle of an MP and react to
				 * it.
				 */
				if (mpsList.stream().allMatch(mps -> !mps.isUnderrun)) {
					_loopTimeout = kNumLoopsTimeout;
				}

				boolean allActivePointsValid = mpsList.stream().allMatch(mps -> mps.activePointValid);
				boolean endOfAllPoints = mpsList.stream().allMatch(mps -> mps.activePoint.isLastPoint);
				/*
				 * If we are executing an MP and the MP finished, start loading another. We will
				 * go into hold state so robot servo's position.
				 */
				if (allActivePointsValid && endOfAllPoints) {
					log.log("Last TrajectoryPoint reached. Setting MotionProfileState to INACTIVE.");
					/*
					 * because we set the last point's isLast to true, we will get here when the MP
					 * is done
					 */
					_setValue = CANTalon.SetValueMotionProfile.Hold;
					_state = MotionProfileState.INACTIVE;
					_loopTimeout = -1;
				}
				break;
			}
		}
		/* printfs and/or logging */
		if (SmartDashboard.getBoolean("Log MotionProfile Stats", true))
			MotionProfileLoggingService.process(mpsList);
	}

	public void startFilling() {
		System.out.println("started filling");

		log.log("in startFilling()");
		if (tpList == null || tpList.size() == 0) {
			log.log("WARNING! Cannot start filling CANTalon buffer without first setting TrajectoryPointList!");
			return;
		}

		/* did we get an underrun condition since last time we checked ? */
		if (mpsList.stream().anyMatch(mps -> mps.hasUnderrun)) {
			log.log("WARNING! Underrun condition occurred.");
			/* better log it so we know about it */
			MotionProfileLoggingService.OnUnderrun();
			/*
			 * clear the error. This flag does not auto clear, this way we never miss
			 * logging it.
			 */

			for (CANTalon ct : ctList) {
				ct.clearMotionProfileHasUnderrun();
			}
		}
		/*
		 * just in case we are interrupting another MP and there is still buffer points
		 * in memory, clear it.
		 */
		log.log("Clearing previous TrajectoryPoints if they exist.", false);
		for (CANTalon ct : ctList) {
			ct.clearMotionProfileTrajectories();
		}

		/* This is fast since it's just into our TOP buffer */
		log.log("Pushing TrajectoryPoints to Talon.");
		for (TrajectoryPoint tp : tpList) {
			for (CANTalon ct : ctList) {
				ct.pushMotionProfileTrajectory(tp);
			}
		}
	}

	/**
	 *
	 * @return the output value to pass to Talon's set() routine. 0 for disable
	 *         motion-profile output, 1 for enable motion-profile, 2 for hold
	 *         current motion profile trajectory point.
	 */

	private TalonControlMode findControlMode(List<CANTalon> talonList) {
		TalonControlMode mode = null;
		for (CANTalon ct : talonList) {
			TalonControlMode nextMode = ct.getControlMode();
			if (mode == null)
				mode = nextMode;
			else if (mode != ct.getControlMode())
				log.log(String.format(
						"Warning: Expected all CANTalon control modes to be equal but found modes [%s] and [%s].", ct,
						nextMode));
		}

		return mode;
	}

	private boolean buffersFilled(List<MotionProfileStatus> mpStatusList, int minBufferSize) {
		log.log("in buffersFilled()");
		int totalFilled = 0;
		int talonCount = 0;
		log.log("number of motion profile statuses " + mpStatusList.size());
		for (MotionProfileStatus mps : mpStatusList) {
			log.log(String.format("Buffer Count [%s] Min buffer Size [%s]", mps.btmBufferCnt, minBufferSize),
					String.valueOf(Math.random()));
			talonCount++;
			if (mps.btmBufferCnt > minBufferSize)
				totalFilled++;
		}

		if (totalFilled == talonCount)
			return true;

		log.log(String.format("Only [%s] of [%s] buffers filled.", totalFilled, talonCount), "buffer-not-filled-msg");
		return false;
	}

	public void resetEnc() {
		lMotor.setEncPosition(0);
		rMotor.setEncPosition(0);
	}
}
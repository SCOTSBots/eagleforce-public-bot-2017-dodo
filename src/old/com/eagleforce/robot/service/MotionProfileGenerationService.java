package old.com.eagleforce.robot.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TrajectoryPoint;

import old.com.eagleforce.robot.model.MotionProfileConfiguration;

public class MotionProfileGenerationService {

	private static final String ROBOT_MAX_VELOCITY_KEY = "robot.specs.max-vel";
	private static final String ROBOT_MAX_ACCELERATION_KEY = "robot.specs.max-acc";
	private static final String MOTION_DISTANCE_KEY = "motion-profiling.distance";
	private static final String MOTION_INTERVAL_KEY = "motion-profiling.interval";
	// private static final String MOTION_T1_KEY = "motion-profiling.t1";
	// private static final String MOTION_T2_KEY = "motion-profiling.t2";

	private static final int ACCELERATION_CURVE = 3000;

	private Properties motionProfileProps = null;

	public Properties getMotionProfileProps() {
		if (motionProfileProps == null) {
			motionProfileProps = new Properties();

			motionProfileProps.setProperty(ROBOT_MAX_ACCELERATION_KEY, "0");
			motionProfileProps.setProperty(ROBOT_MAX_VELOCITY_KEY, "3");
			// motionProfileProps.setProperty(MOTION_T1_KEY, "400");
			// motionProfileProps.setProperty(MOTION_T2_KEY, "200");
			motionProfileProps.setProperty(MOTION_DISTANCE_KEY, "1");
			motionProfileProps.setProperty(MOTION_INTERVAL_KEY, "10");

		}
		return motionProfileProps;
	}

	public void setMotionProfileProps(Properties motionProfileProps) {
		this.motionProfileProps = motionProfileProps;
	}

	public List<TrajectoryPoint> generatePoints(MotionProfileConfiguration mpc) {
		List<CANTalon.TrajectoryPoint> tpList = new ArrayList<>();

		// Store config in easy to access variables
		final double maxVel = mpc.getMaxVel();
		final double endDistance = mpc.getEndDistance();
		final int interval = mpc.getInterval();
		final double maxAcc = mpc.getMaxAcc();
		final boolean isVelocityOnly = mpc.isVelocityOnly();

		// Resolve non-config, static variables
		// final double t1 = (1. / (maxAcc / RENAME_THIS));
		final double t1 = ACCELERATION_CURVE / maxAcc;
		final double t2 = t1 / 2;
		double f2;
		final List<Double> f1List = new ArrayList<>();

		// Initialize everything to zero for the first record
		tpList.add(initialTp(interval));
		f1List.add(0.0);
		f2 = 0;

		// Create a counter to use while looping
		int i = 0;
		
		while (true) {
			i++;
			double posOrNeg;
			TrajectoryPoint tPoint = new TrajectoryPoint();
			TrajectoryPoint prevTp = tpList.get(i - 1);

			posOrNeg = increasingOrDecreasing(i, endDistance, maxVel, interval, t1);
			double sumF1Count = Math.max(0, Math.min(1, (f1List.get(i - 1) + posOrNeg)));
			f1List.add(sumF1Count);
			f2 = calculateF2(t2, i, interval, f1List, f2);

			tPoint.velocityOnly = isVelocityOnly;
			tPoint.profileSlotSelect = 0;
			tPoint.timeDurMs = interval;
			tPoint.velocity = calculateVelocity(maxVel, f1List, f2, i, t2, interval);
			tPoint.position = (prevTp.position + calculatePosition(tPoint, prevTp, interval));

			tpList.add(tPoint);
			 System.out.println(i + "\t" + tPoint.velocity + "\t" + tPoint.position +"\t" + tPoint.timeDurMs);
			if (tPoint.velocity == 0 || (isVelocityOnly && tPoint.velocity == maxVel)) {
				tPoint.isLastPoint = true;
				break;
			}
		}

		return tpList;
	}

	// Private helper methods
	// ====================================================================================================
	private TrajectoryPoint initialTp(int interval) {
		TrajectoryPoint tp = new TrajectoryPoint();
		tp.timeDurMs = interval;
		tp.position = 0;
		tp.velocity = 0;
		tp.zeroPos = true;

		return tp;
	}
	
	public static double round(double d, int decimalPlace) {
	    BigDecimal bd = new BigDecimal(d);
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	}

	// TODO: Break these variables out into a model object to be passed around
	private double increasingOrDecreasing(int i, double endDistance, double maxVel, int interval, double t1) {
		if (i - 1 < (((endDistance / maxVel) * 1000) / interval)) {
			return (1. / Math.round(t1 / interval));
		} else {
			return ((-1.) / Math.round(t1 / interval));
		}
	}

	private double calculateF2(double t2, int i, int interval, final List<Double> f1List, double f2) {
		double retVal = 0;
		int sum = (int) (Math.round(t2 / interval));
		if (i == (int) Math.min(Math.round(t2 / interval), i)) {
			retVal = f2 + f1List.get(i);
		} else {
			retVal = 0;
			for (int j = 0; j < sum; j++) {
				retVal += f1List.get(i - j);
			}
		}

		return retVal;
	}

	private double calculateVelocity(final double maxVel, final List<Double> f1List, final double f2, int i, double t2,
			double interval) {
		return maxVel * ((f1List.get(i) + f2) / (1 + (Math.round(t2 / interval))));
	}

	private double calculatePosition(TrajectoryPoint currTp, TrajectoryPoint prevTp, int interval) {
		final double avgVel = (currTp.velocity + prevTp.velocity) / 2;
		return ((avgVel * interval) / 1000);
	}


}

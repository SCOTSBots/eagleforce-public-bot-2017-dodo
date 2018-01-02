package old.com.eagleforce.robot.service;

import java.util.List;

import com.ctre.CANTalon;

/**
 * Lets create a periodic task to funnel our trajectory points into our talon.
 * It doesn't need to be very accurate, just needs to keep pace with the motion
 * profiler executer.  Now if you're trajectory points are slow, there is no need
 * to do this, just call _talon.processMotionProfileBuffer() in your teleop loop.
 * Generally speaking you want to call it at least twice as fast as the duration
 * of your trajectory points.  So if they are firing every 20ms, you should call 
 * every 10ms.
 */
class CANTalonBufferProcessor implements java.lang.Runnable {
	private List<CANTalon> ctList;
	public CANTalonBufferProcessor(List<CANTalon> ctList) {
		this.ctList = ctList;
	}
    public void run() {
    	for(CANTalon ct : ctList) {
    		ct.processMotionProfileBuffer();
    	}
    }
}
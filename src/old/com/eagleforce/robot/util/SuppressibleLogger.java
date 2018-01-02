package old.com.eagleforce.robot.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SuppressibleLogger {
	private static final long DEFAULT_LOG_SUPPRESSION_MILLISECONDS = 1000;

	private Map<String, LocalDateTime> logCache = new HashMap<>();
//	private boolean supress = false;
	private long suppressTimeOut;
	private String loggerPrefix;

	/**
	 * Pass in the class you wish to log from so we can add a prefix to each log
	 * message.
	 * <p>
	 * <p>
	 * Ex: <b>SuppressibleLogger log = new
	 * SuppressibleLogger(this.getClass());</b>
	 * 
	 */
	public SuppressibleLogger(Class<?> clazz) {
		this(clazz, DEFAULT_LOG_SUPPRESSION_MILLISECONDS);
	}

	/**
	 * Pass in the class you wish to log from so we can add a prefix to each log
	 * message and the default time in milliseconds to wait in between logging
	 * similar log messages.
	 * <p>
	 * <p>
	 * Ex: <b>SuppressibleLogger log = new SuppressibleLogger(this.getClass(),
	 * 1000);</b>
	 * 
	 * @param clazz
	 * @param logSuppressionMilli
	 */
	public SuppressibleLogger(Class<?> clazz, long logSuppressionMilli) {
		loggerPrefix = clazz.getSimpleName() + ": ";
		this.suppressTimeOut = logSuppressionMilli;
		SmartDashboard.putBoolean("Logger", true);
	}

	public void log(String msg) {
		log(msg, msg);
	}
	public void log(String msg, boolean supressLog) {
		log(msg, msg, suppressTimeOut, supressLog);
	}

	public void log(String msg, String key) {
		log(msg, key, suppressTimeOut);
	}

	public void log(String msg, long secondsToSupress) {
		log(msg, msg, secondsToSupress);
	}

	public void log(String msg, String key, long secondsToSupress) {
		log(msg, key, secondsToSupress, true);
	}
	public void log(String msg, String key, long secondsToSupress, boolean supressLog) {
		boolean doLog = false;
		if (supressLog) {
			LocalDateTime logCacheRecord = logCache.get(key);
			if (logCacheRecord == null) {
				// First time this message has been logged
				doLog = true;
			} else {
				Duration lengthSinceLastLogMsg = Duration.between(logCacheRecord, LocalDateTime.now());
				if (lengthSinceLastLogMsg.toMillis() > suppressTimeOut)
					// Message suppression has expired, log it
					doLog = true;
			}
		}else {
			doLog=true;
		}
		

		if (doLog) {
			// Add log msg to the cache with the current timestamp
			// and don't log it again until the timeout expires
			logCache.put(key, LocalDateTime.now());
			if (SmartDashboard.getBoolean("Logger", true))
				System.out.println(loggerPrefix + msg);
		}
	}
}

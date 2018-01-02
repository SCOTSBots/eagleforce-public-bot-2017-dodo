package old.com.eagleforce.robot.app.dev;

import old.com.eagleforce.robot.util.SuppressibleLogger;

public class TestRunner {
	
	public static void main(String[] args) {
		new TestRunner().run();
	}
	
	private SuppressibleLogger log = new SuppressibleLogger(this.getClass());

	public void run() {
		for (int i = 0; i < 100; i++) {
			log.log("Hello world!");
			log.log("Random: " + Math.random(), "Random: " +Math.random());
			sleep(100);			
		}
	}
	
	private void sleep(int ms) {
		try {Thread.sleep(ms);} catch (InterruptedException e) {}
	}

}

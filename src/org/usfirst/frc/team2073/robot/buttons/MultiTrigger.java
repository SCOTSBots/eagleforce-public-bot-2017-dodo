package org.usfirst.frc.team2073.robot.buttons;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class MultiTrigger extends Trigger {
	private final List<Trigger> triggers;

	public MultiTrigger(List<Trigger> triggers) {
		this.triggers = triggers;
	}

	public MultiTrigger(Trigger... triggers) {
		this(Arrays.asList(triggers));
	}

	@Override
	public boolean get() {
		return triggers.stream().allMatch(Trigger::get);
	}
}

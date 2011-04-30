package schedule.algorithm;

import java.util.PriorityQueue;
import java.util.Queue;

import schedule.Process;

public class Priority extends ScheduleAlgorithm {
	public Priority() {
		super(false);
	}
	
	public void setPreemptive(boolean preemptive) {
		super.setPreemptive(preemptive);
	}

	@Override
	public Queue<Process> newQueueImpl() {
		return new PriorityQueue<Process>(1, Process.PROCESS_PRIORITY_COMPARATOR);
	}
}

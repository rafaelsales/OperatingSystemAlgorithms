package schedule.algorithm;

import java.util.PriorityQueue;
import java.util.Queue;

import schedule.Process;

public class SJF extends ScheduleAlgorithm {
	
	public SJF() {
		super(false);
	}

	public void setPreemptive(boolean preemptive) {
		super.setPreemptive(preemptive);
	}

	@Override
	public Queue<Process> newQueueImpl() {
		return new PriorityQueue<Process>(1, Process.PROCESS_CPU_BURST_COMPARATOR);
	}
}

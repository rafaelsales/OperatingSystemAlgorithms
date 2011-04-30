package schedule.algorithm;

import schedule.Process;
import schedule.queue.Queue;

public abstract class ScheduleAlgorithm {
	
	private boolean preemptive;
	protected Queue queue;
	
	public ScheduleAlgorithm(boolean preemptive) {
		this.preemptive = preemptive;
	}
	
	public boolean isPreemptive() {
		return preemptive;
	}
	
	protected void setPreemptive(boolean preemptive) {
		this.preemptive = preemptive;
	}
	
	public void setCurrentQueue(Queue queue) {
		this.queue = queue;
	}
	
	public Queue getCurrentQueue() {
		return queue;
	}

	public Process selectProcess() {
		do {
			Process process = getCurrentQueue().getProcesses().peek();
			
			if (process.isFinished()) {
				getCurrentQueue().getProcesses().remove(process);
			} else {
				return process;
			}
		} while (!getCurrentQueue().getProcesses().isEmpty());
		return null;
	}
	
	public abstract java.util.Queue<Process> newQueueImpl();
}

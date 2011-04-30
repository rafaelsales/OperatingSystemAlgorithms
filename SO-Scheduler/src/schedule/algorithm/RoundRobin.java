package schedule.algorithm;

import java.util.LinkedList;
import java.util.Queue;

import schedule.Process;


public class RoundRobin extends ScheduleAlgorithm {

	/**
	 * Time quantum as units of time
	 */
	private int quantum = 10;

	public RoundRobin(Integer quantum) {
		super(false);
		if (quantum != null) {
			this.setQuantum(quantum);
		}
	}

	public void setQuantum(int quantum) {
		if (quantum < 0) { 
			throw new IllegalArgumentException();
		}
		this.quantum = quantum;
	}

	public int getQuantum() {
		return quantum;
	}

	@Override
	public Queue<Process> newQueueImpl() {
		return new LinkedList<Process>();
	}

	@Override
	public Process selectProcess() {
		do {
			Process process = getCurrentQueue().getProcesses().remove();
			if (!process.isFinished()) {
				getCurrentQueue().getProcesses().offer(process);
				process.setTimeQuantum(quantum);
				return process;
			}
		} while (!getCurrentQueue().getProcesses().isEmpty());
		return null;
	}

}

package schedule.algorithm;

import java.util.LinkedList;
import java.util.Queue;

import schedule.Process;

public class FCFS extends ScheduleAlgorithm {

	public FCFS() {
		super(false);
	}

	@Override
	public Queue<Process> newQueueImpl() {
		return new LinkedList<Process>();
	}
}

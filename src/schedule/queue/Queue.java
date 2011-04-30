package schedule.queue;

import schedule.Process;
import schedule.algorithm.ScheduleAlgorithm;
import statistics.Statistics;

public class Queue {

	private String name;
	private java.util.Queue<Process> processes;
	private ScheduleAlgorithm scheduleAlgorithm;
	private QueueType queueType;

	/*
	 * variavel que ser� utilizada pelo Aging para verificar se a fila rodou
	 * muita vezes ou n�o caso tenha rodado muitas vezes o Aging passa para
	 * outra fila. para cada 10 vezes rodada a de prioridade 1, deve ser rodade
	 * 5 de prioridade 2 2 de prioridade 3 e 1 de prioridade 1
	 */
	private int timesRunned;

	public Queue(QueueType queueType, ScheduleAlgorithm scheduleAlgorithm) {
		this.scheduleAlgorithm = scheduleAlgorithm;
		this.scheduleAlgorithm.setCurrentQueue(this);
		this.queueType = queueType;
		timesRunned = 0;
		processes = this.scheduleAlgorithm.newQueueImpl();
	}

	public ScheduleAlgorithm getScheduleAlgorithm() {
		return scheduleAlgorithm;
	}

	public void setSelectedAlgorithm(ScheduleAlgorithm scheduleAlgorithm) {
		this.scheduleAlgorithm = scheduleAlgorithm;
	}

	public String getName() {
		return name;
	}

	public java.util.Queue<Process> getProcesses() {
		return processes;
	}

	public void addProcess(final Process process) {
		Statistics.getStatistics().addProcess(process);
		Statistics.getStatistics().addProcessEnqueue(this, process);
		processes.add(process);
	}

	public Process selectProcess() {
		Process aux = scheduleAlgorithm.selectProcess();
		if (aux != null && aux.isRunning() == true) {
			return null;
		}
		if (aux != null) {
			aux.setRunning(true);
		}
		return aux;
	}

	public QueueType getQueuePriority() {
		return queueType;
	}

	public int getTimesRunned() {
		return timesRunned;
	}

	public void setTimesRunned(final int timesRunned) {
		this.timesRunned = timesRunned;
	}

}

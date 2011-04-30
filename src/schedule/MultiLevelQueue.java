package schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import schedule.algorithm.ScheduleAlgorithm;
import schedule.queue.Aging;
import schedule.queue.Queue;
import schedule.queue.QueueManager;
import schedule.queue.QueueType;
import configuration.ThreadManagement;

public class MultiLevelQueue implements ThreadManagement {

	private QueueManager queueManager;
	private List<Queue> queueList;
	private Queue queueToRun = null;
	private Thread multiThread;
	private boolean running = true;

	public MultiLevelQueue(
			Map<QueueType, ScheduleAlgorithm> processQueuesAlgorithms) {
		queueManager = new Aging();
		queueList = new ArrayList<Queue>(processQueuesAlgorithms.size());
		for (Entry<QueueType, ScheduleAlgorithm> entry : processQueuesAlgorithms
				.entrySet()) {
			queueList.add(new Queue(entry.getKey(), entry.getValue()));
		}
		multiThread = new Thread(this);
	}

	public Queue getQueueByPriority(final QueueType priority) {
		Queue aux = null;
		for (Queue element : queueList) {
			if (element.getQueuePriority() == priority) {
				aux = element;
			}
		}
		return aux;
	}

	public QueueManager getQueueManager() {
		return queueManager;
	}

	public void setQueueManager(final QueueManager queueManager) {
		this.queueManager = queueManager;
	}

	public List<Queue> getQueueList() {
		return queueList;
	}

	public void setQueueList(final List<Queue> queueList) {
		this.queueList = queueList;
	}

	public synchronized Queue getSelectedQueue() {
		Queue aux = queueToRun;
		queueToRun = null;
		return aux;
	}

	// Ordena as filas que v�o ser verificadas pelo Aging, ou seja, as filas
	// que
	// possuem processos
	private List<Queue> orderQueueByPriority(final List<Queue> allQueues) {
		Queue aux = null;
		Object[] auxVector = allQueues.toArray();
		for (int i = 0; i < auxVector.length; i++) {
			for (int j = i; j < auxVector.length; j++) {
				if (((Queue) auxVector[i]).getQueuePriority().getPriority() > ((Queue) auxVector[j])
						.getQueuePriority().getPriority()) {
					aux = (Queue) auxVector[i];
					auxVector[i] = auxVector[j];
					auxVector[j] = aux;
				}
			}
		}
		allQueues.clear();
		for (int i = 0; i < auxVector.length; i++) {
			allQueues.add((Queue) auxVector[i]);
		}

		return allQueues;

	}

	private synchronized void selectQueue() {
		List<Queue> queuesThatHaveProcess = new ArrayList<Queue>();
		// fica verificando na lista de filas se as filas possuem
		// processos ou n�o
		for (Queue queueType : queueList) {
			if (queueType.getProcesses().size() > 0) {
				queuesThatHaveProcess.add(queueType);
			}
		}

		// pega a fila da rodada, utilizando o Aging
		queuesThatHaveProcess = orderQueueByPriority(queuesThatHaveProcess);
		queueToRun = Aging.verifyQueueToRun(queuesThatHaveProcess);
		
	}

	// acho q � so isso msm o run dela
	public void run() {
		while (true) {
		//	System.out.println("Multi Level");
			try {
				if (queueToRun == null ) {
				//	System.out.println("IUPU");
					selectQueue();
					
				} else{
					multiThread.sleep(1000);
				}
			} catch (InterruptedException e) {
				// Lançada quando a thread da MultiLevelQueue é interrompida e
				// está em wait();
				// Neste caso, deve-se deixar a thread morrer.
			//	System.out.println("sdfsdf");
				return;
			}
		}
	}

	@Override
	public void start() {
		if (!multiThread.isAlive()) {
			multiThread.start();
		} else if (!running) {
			multiThread.notify();
		}
		running = true;

	}

	@Override
	public void stop() throws InterruptedException {
		if (running && !multiThread.isInterrupted()) {
			multiThread.interrupt();
			running = false;
		}
	}
}

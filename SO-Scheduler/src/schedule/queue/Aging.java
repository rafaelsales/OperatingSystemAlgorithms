package schedule.queue;

import java.util.List;

import schedule.Process;

public class Aging implements QueueManager {

	public static Queue verifyQueueToRun(final List<Queue> allQueues) {

		/*
		 * caso saia do for sem entrar em nenhuma fila � pq todas as filas
		 * j� rodaram podemos assim zerar o n�mero de vezes rodada a fila e
		 * recome�ar do 0
		 */
		for (Queue queue : allQueues) {
			switch (queue.getQueuePriority()) {
			case QUEUE_INTERACTIVE:
				if (queue.getTimesRunned() < 10) {
					queue.setTimesRunned(queue.getTimesRunned() + 1);
					return queue;
				}
				break;
			case QUEUE_SYSTEM:
				if (queue.getTimesRunned() < 5) {
					queue.setTimesRunned(queue.getTimesRunned() + 1);
					return queue;
				}
				break;
			case QUEUE_BACKGROUND:
				if (queue.getTimesRunned() < 2) {
					queue.setTimesRunned(queue.getTimesRunned() + 1);
					return queue;
				}

				break;
			case QUEUE_BATCH:
				if (queue.getTimesRunned() < 1) {
					queue.setTimesRunned(queue.getTimesRunned() + 1);
					return queue;
				}
				break;
			}
		}

		Queue highPriority = null;
		for (Queue queue : allQueues) {
			queue.setTimesRunned(0);
			if (queue.getQueuePriority() == QueueType.QUEUE_INTERACTIVE) {
				highPriority = queue;
			}

		}
		return highPriority;

	}

	public static Queue verifyProcessToRun(final List<Process> allProcess) {

		return null;
	}

}

package schedule;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import schedule.algorithm.ScheduleAlgorithm;
import schedule.queue.Queue;
import schedule.queue.QueueType;
import statistics.Statistics;

import computer.ProcessorCollection;

import configuration.Configuration;
import configuration.ThreadManagement;

public class Scheduler implements ThreadManagement {

	private MultiLevelQueue multiQueue;
	private Dispacher dispatcher;
	private ProcessorCollection processorCollection;
	private Statistics statistics;
	private Map<QueueType, ScheduleAlgorithm> processQueuesAlgorithms;

	private Thread schedulerThread;
	private boolean running = false;

	public Scheduler() {
		statistics = Statistics.getStatistics();
		dispatcher = new Dispacher();
		processorCollection = new ProcessorCollection();
		schedulerThread = new Thread(this);

	}

	public void setInitialParameters(final Configuration c)
			throws FileNotFoundException, IOException {
		processorCollection.setProcessos(c.getProcessors());
		
		processQueuesAlgorithms = c.getProcessQueuesAlgorithms();
		multiQueue = new MultiLevelQueue(processQueuesAlgorithms);
	}

	public synchronized void loadProcess(final Process process) {
		multiQueue.getQueueByPriority(
				QueueType.getByProcessType(process.getTypeOfProcess()))
				.addProcess(process);
	}

	/**
	 * Retorna as estatísticas de execução dos processos
	 * 
	 * @return
	 */
	public Statistics getStatistics() {
		return statistics;
	}

	/**
	 * Inicia a execução do escalonador
	 */
	public synchronized void start() {
		statistics.setProcessorCollection(processorCollection);
		running = true;
		if (!schedulerThread.isAlive()) {
			schedulerThread.start();
		}
		multiQueue.start();
		dispatcher.start();
	}

	/**
	 * Suspende a execução do escalonador
	 */
	public void suspend() {
		running = false;
	}

	/**
	 * Retorna a execução do ponto onde parou
	 */
	public synchronized void resume() {
		if (!running) {
			running = true;
			schedulerThread.notify();
			multiQueue.start();
			dispatcher.start();
		}
	}

	/**
	 * Pára a execução do escalonador
	 * @throws InterruptedException 
	 */
	public synchronized void stop() throws InterruptedException {
		if (!schedulerThread.isInterrupted()) {
			schedulerThread.interrupt();
		}
		multiQueue.stop();
		dispatcher.stop();
	}

	@Override
	public void run() {
		// tosco mas deixa por enquanto
		while (true) {
			//System.out.println("RODANDO - SCHEDULER");
			if (!running) {
				/*
				 * Caso a execução tenha sido suspendida, o monitor fica em
				 * espera até ser resumido ou a thread ser interrompida:
				 */
				try {
					this.wait();
				} catch (InterruptedException e) {
					// Ocorre quando o scheduler é suspendido e em seguida
					// parado antes de ser resumido.
					// Neste deixa que a thread do escalonador morra:
					return;
				}
			}

			if (Thread.currentThread().isInterrupted()) {
				return;
			}

			// Envia para o dispatcher a fila escolhida pelo multilevel queue e
			// o processador escolhido:
			Queue queue = multiQueue.getSelectedQueue();
			if (queue != null) {
				//System.out.println("ENTROU");
				dispatcher.dispatch(queue, processorCollection);

			} else {
			//	System.out.println("ESPERANDO - SCHEDULER");
			}
		}
	}
}
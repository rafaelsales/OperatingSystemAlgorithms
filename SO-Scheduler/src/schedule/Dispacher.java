package schedule;

import java.util.List;

import schedule.queue.Queue;

import computer.Processor;
import computer.ProcessorCollection;

import configuration.ThreadManagement;

public class Dispacher implements ThreadManagement {

	private Thread dispatcherThread;
	private boolean running = true;

	public Dispacher() {
		dispatcherThread = new Thread(this);

	}

	@Override
	public void run() {
		// N�O � DESNECESS�RIO LER ITEM 3 DO SITE!!!
	}

	private Processor getMoreIdleProcess(final List<Processor> all,
			final Process selectedProcess) {
		Processor moreIdle = null;
		float run1, run2;
		for (Processor processor : all) {
			if (moreIdle == null) {
				moreIdle = processor;
			} else {
				run1 = moreIdle.run(selectedProcess);
				run2 = processor.run(selectedProcess);
				if (moreIdle.getExecutingProcess() != null
						&& processor.getExecutingProcess() == null) {
					moreIdle = processor;
				}
				if ((moreIdle.getExecutingProcess() == null && processor
						.getExecutingProcess() == null)
						|| (moreIdle.getExecutingProcess() != null && processor
								.getExecutingProcess() != null)) {
					if (run2 < run1) {
						moreIdle = processor;
					}
				}
			}
		}

		return moreIdle;
	}

	public Process dispatch(final Queue selectedQueue,
			final ProcessorCollection allProcessors) {
		// por enqunato
		Process selectedProcess = selectedQueue.selectProcess();
		
		if (selectedProcess != null) {
			getMoreIdleProcess(allProcessors.getProcessos(), selectedProcess)
					.setExecutingProcess(selectedProcess);
		}
		return selectedProcess;
	}

	@Override
	public void start() {
		if (!dispatcherThread.isAlive()) {
			dispatcherThread.start();
		} else if (!running) {
			dispatcherThread.notify();
		}
		running = true;
	}

	@Override
	public void stop() throws InterruptedException {
		if (running && !dispatcherThread.isInterrupted()) {
			dispatcherThread.interrupt();
			running = false;
		}
	}
}

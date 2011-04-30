package computer;

import schedule.Process;
import statistics.Statistics;
import configuration.ThreadManagement;

public class Processor implements ThreadManagement {

	private String description;
	private Thread processorThread;
	private Process executingProcess;
	private Statistics statistics;
	private float effectiveExecutingTime;

	public Processor(final String description) {
		this.description = description;
		processorThread = new Thread(this);
		processorThread.start();
		statistics = Statistics.getStatistics();
	}

	public float run(final Process p) {
		if (p.getExecutionTime() > 0) {
			return p.getExecutionTime();
		} else {
			try {
				return (float) (Math.random() * 1000);
			} catch (Exception e) {
				return 100F;
			}
		}

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Process getExecutingProcess() {
		return executingProcess;
	}

	public void setExecutingProcess(Process executingProcess) {
		this.executingProcess = executingProcess;
	}

	// vai executar os processos( talvez esteja certo), nao sei ainda...
	public void run() {
		// System.out.println("Processador testando");
		while (true) {
			if (executingProcess == null) {
				try {
					// System.out.println("PROCESSADOR:"+description+" DORMINDO");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				int timeExecuted = 0;
				synchronized (executingProcess) {

					System.out.println("PROCESSADOR:" + description
							+ " RODANDO PROCESSO:"
							+ executingProcess.getProcessName()
							+ " TOTAL PARA EXECU��O:"
							+ executingProcess.getExecutionTime());
					timeExecuted = executingProcess.execute();
					effectiveExecutingTime += timeExecuted;
					executingProcess.setExecutionTime(timeExecuted);
					System.out.println("Total restante(ap�s execu��o):"
							+ executingProcess.getExecutionTime());
					executingProcess.setRunning(false);
					executingProcess = null;
					try {
						Thread.sleep(timeExecuted);
					} catch (InterruptedException e) {
						// SLEEP Apenas para dar a sensa��o de demora no
						// processamento
						e.printStackTrace();
					}
					
					for (Process process : Statistics.getStatistics().getProcesses()) {
						if (!process.isFinished() && !process.isRunning()) {
							process.addWaitingTime(timeExecuted);
						}
					}
				}
			}

		}

	}

	public void start() {

	}

	@Override
	public void stop() {
		// N�O PARA N�O PARA N�O PARA N�O PARA N�O PARA N�O PARA N�O PARA N�O
		// PARA At� o ch�o

	}
	
	@Override
	public String toString() {
		return description;
	}

	public float getEffectiveExecutingTime() {
		return effectiveExecutingTime;
	}
}

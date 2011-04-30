package schedule;

import java.util.Comparator;

public class Process{

	private Integer executionTime;
	private Integer remainingTime;
	private Integer priority;
	private String processName;
	// tipo do processo, assim entrando na fila com a prioridade desejada
	private ProcessType typeOfProcess;
	private boolean isRunning = false;
	
	private float totalWaitingTime = 0f;
	private float waitingTimeForFirstResponse = 0f;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	//Unidades de tempo que o processo executará: 
	private Integer timeQuantum;
	
	public Process() {
	}

	public Integer getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
		this.remainingTime = executionTime;
		if (this.executionTime == null) {
			this.executionTime = 0;
		}
	}

	public boolean isUndefinedExecutionTime() {
		return executionTime == null;
	}

	public boolean isFinished() {
		return remainingTime != null && remainingTime == 0;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public ProcessType getTypeOfProcess() {
		return typeOfProcess;
	}
	
	private int generateCPUBurstTime() {
		return (int) (10 * Math.random()); 
	}
	
	public int execute() {
		int burstTime = generateCPUBurstTime();
		int timeExecuting;
		if (remainingTime == null) {
			timeExecuting = burstTime;
			executionTime += burstTime;
		} else {			
			if (getTimeQuantum() != null) {
				//Obtém o mínimo do time quantum e o burst time:
				burstTime = Math.min(getTimeQuantum(), burstTime);
				timeExecuting = burstTime;
				if(remainingTime > burstTime){
					remainingTime = remainingTime - burstTime ;
				}else{
					remainingTime = 0;
				}
			}else{
				timeExecuting = remainingTime;
				remainingTime = 0;
			}
		}
		
		return timeExecuting;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(final String processName) {
		this.processName = processName;
	}

	public Integer getRemainingTime() {
		return remainingTime;
	}

	// podemos SIM alterar a prioridade de um processe, e assim modifica-lo de
	// fila.
	public void setTypeOfProcess(ProcessType typeOfProcess) {
		this.typeOfProcess = typeOfProcess;
	}

	public void setTimeQuantum(Integer timeQuantum) {
		this.timeQuantum = timeQuantum;
	}

	public Integer getTimeQuantum() {
		return timeQuantum;
	}

	public void addWaitingTime(float waitingTime) {
		if (remainingTime == null || remainingTime.equals(executionTime)) {
			waitingTimeForFirstResponse += waitingTime;
		}
		this.totalWaitingTime += waitingTime;
	}

	public float getTotalWaitingTime() {
		return totalWaitingTime;
	}

	public float getWaitingTimeForFirstResponse() {
		return waitingTimeForFirstResponse;
	}

	public static final Comparator<Process> PROCESS_CPU_BURST_COMPARATOR = new Comparator<Process>() {
		@Override
		public int compare(Process p1, Process p2) {
			if (p1.remainingTime == null) {
				return +1;
			} else if (p2.remainingTime == null) {
				return -1;
			}
			return p1.remainingTime.compareTo(p2.remainingTime);
		}
	};

	public static final Comparator<Process> PROCESS_PRIORITY_COMPARATOR = new Comparator<Process>() {
		@Override
		public int compare(Process p1, Process p2) {
			if (p1.priority == null) {
				return -1;
			} else if (p2.priority == null) {
				return +1;
			}
			return p1.priority.compareTo(p2.priority);
		}
	};
}

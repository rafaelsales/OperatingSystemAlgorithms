package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import schedule.Process;
import schedule.queue.Queue;

import computer.Processor;
import computer.ProcessorCollection;

public class Statistics {

	private static Statistics statistics = null;
	
	private List<StatisticTuples> statisticTuples;
	
	private ProcessorCollection processorCollection;
	
	private Map<Queue, List<Process>> queuesProcesses = new HashMap<Queue, List<Process>>();
	
	private Set<Process> processes = new HashSet<Process>();

	public static Statistics getStatistics() {
		if (statistics == null) {
			statistics = new Statistics();
		}
		return statistics;

	}

	private Statistics() {

	}
	
	public void setProcessorCollection(ProcessorCollection processorCollection) {
		this.processorCollection = processorCollection;
	}

	public void addProcess(Process process) {
		processes.add(process);
	}
	
	public Set<Process> getProcesses() {
		return processes;
	}

	public void addProcessEnqueue(Queue queue, Process process) {
		List<Process> processes = queuesProcesses.get(queue);
		if (processes == null) {
			processes = new ArrayList<Process>();
			queuesProcesses.put(queue, processes);
		}
		processes.add(process);
	}

	private void generateStatistics() {
		statisticTuples = new ArrayList<StatisticTuples>();
		List<Processor> processors = processorCollection.getProcessos();
		
		//Numero de processos executados:
		statisticTuples.add(new StatisticTuples("quantidade_processos_executados", Integer.toString(processes.size())));
		
		//Numero de processos em cada fila de execucao:
		Map<Queue, Integer> queuesNumberProcesses = new HashMap<Queue, Integer>();
		for (Entry<Queue, List<Process>> entry : queuesProcesses.entrySet()) {
			queuesNumberProcesses.put(entry.getKey(), entry.getValue().size());
		}
		statisticTuples.add(new StatisticTuples("quantidade_processos_por_fila", Integer.toString(queuesNumberProcesses.size())));
		
		//Tempo total de processamento
		Float tempoTotalProcessamento = 0f; 
		for (Processor processor : processors) {
			tempoTotalProcessamento += processor.getEffectiveExecutingTime();
		}
		statisticTuples.add(new StatisticTuples("tempo_total_processmento", tempoTotalProcessamento.toString()));
		
		//Percentual de utilizacao das CPUs:
		Map<Processor, Float> percetuaisCPU = new HashMap<Processor, Float>();
		for (Processor processor : processors) {
			percetuaisCPU.put(processor, (100 * processor.getEffectiveExecutingTime() / tempoTotalProcessamento));
		}
		statisticTuples.add(new StatisticTuples("percentual_utilizacao_cpus", percetuaisCPU.toString()));
		
		//Media de throughput dos processos:
		float maxExecutingTime = 0f;
		for (Processor processor : processors) {
			if (processor.getEffectiveExecutingTime() > maxExecutingTime) {
				maxExecutingTime = processor.getEffectiveExecutingTime();
			}
		}
		Float throughputAvg = processes.size() / maxExecutingTime;
		statisticTuples.add(new StatisticTuples("media_throughput", throughputAvg.toString()));

		//Media de turnaround dos processos:
		Float turnaroundAvg = 0f;
		for (Process process : processes) {
			turnaroundAvg += process.getTotalWaitingTime() + process.getExecutionTime();
		}
		turnaroundAvg = turnaroundAvg / processes.size();
		statisticTuples.add(new StatisticTuples("media_turnaround", turnaroundAvg.toString()));
		
		//Media de tempo de espera dos processos:
		Float tempoEsperaAvg = 0f;
		for (Process process : processes) {
			tempoEsperaAvg += process.getTotalWaitingTime();
		}
		tempoEsperaAvg = tempoEsperaAvg / processes.size();
		statisticTuples.add(new StatisticTuples("media_tempo_espera", tempoEsperaAvg.toString()));
		
		//Media do tempo de resposta dos processos:
		Float tempoRespostaAvg = 0f;
		for (Process process : processes) {
			tempoRespostaAvg += process.getWaitingTimeForFirstResponse();
		}
		tempoRespostaAvg = tempoRespostaAvg / processes.size();
		statisticTuples.add(new StatisticTuples("media_tempo_resposta", tempoRespostaAvg.toString()));
	}
	
	public List<StatisticTuples> getStatisticTuples() {
		generateStatistics();
		return statisticTuples;
	}

	public void setStatisticTuples(List<StatisticTuples> statisticTuples) {
		this.statisticTuples = statisticTuples;
	}
	
	@Override
	public String toString() {
		List<StatisticTuples> tuples = getStatisticTuples();
		String string = "\n--- STATISTICS - BEGIN ---\n";
		for (StatisticTuples tuple : tuples) {
			string += tuple.toString() + "\n";
		}
		string += "--- STATISTICS - END---\n";
		return string;
	}
}

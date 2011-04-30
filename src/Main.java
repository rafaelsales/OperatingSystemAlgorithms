import java.util.List;

import schedule.Process;
import schedule.Scheduler;
import statistics.Statistics;
import configuration.Configuration;
import configuration.ProcessesInitial;

public class Main {

	public static void main(String[] args) {
		List<Process> processes = null;
		final String DEFAULT_CONFIGURATION_FILE = "default_configuration.properties";
		try {
			processes = ProcessesInitial.getInitialProcesses();
			
			Configuration configuration = Configuration
					.loadFromFile(DEFAULT_CONFIGURATION_FILE);
			Scheduler scheduler = new Scheduler();
			scheduler.setInitialParameters(configuration);

			for (Process process : processes) {
				scheduler.loadProcess(process);
			}
			scheduler.start();
			while (true) {
				Thread.sleep(10000l);
				System.out.println(Statistics.getStatistics().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import schedule.Process;
import schedule.ProcessType;

public class ProcessesInitial {

	public static List<Process> getInitialProcesses() {
		List<Process> processes = new ArrayList<Process>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"ProcessInitiial.txt"));
			String str = null;
			int i = 0;
			while (in.ready()) {
				if (i != 0) {
					str = in.readLine();
					processes.add(fromStringToProcess(str));
				} else {
					in.readLine();
					i++;
				}
			}
			in.close();
		} catch (IOException e) {
		}

		return processes;
	}

	private static Process fromStringToProcess(final String line) {
		Process aux = null;
		aux = new Process();
		String[] process = line.split(";");
		aux.setProcessName(process[0]);
		aux.setExecutionTime(Integer.parseInt(process[1]));
		if (process[2].equals("system")) {
			aux.setTypeOfProcess(ProcessType.SYSTEM_PROCESS);
		}
		if (process[2].equals("background")) {
			aux.setTypeOfProcess(ProcessType.BACKGROUND_PROCESS);

		}
		if (process[2].equals("batch")) {
			aux.setTypeOfProcess(ProcessType.BATCH_PROCESS);

		}
		if (process[2].equals("interactive")) {
			aux.setTypeOfProcess(ProcessType.INTERACTIVE_PROCESS);

		}
		aux.setPriority(Integer.parseInt(process[3]));

		return aux;

	}

}

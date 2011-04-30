package configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import schedule.algorithm.FCFS;
import schedule.algorithm.Priority;
import schedule.algorithm.RoundRobin;
import schedule.algorithm.SJF;
import schedule.algorithm.ScheduleAlgorithm;
import schedule.queue.QueueType;

import computer.Processor;

public class Configuration {

	private List<ConfigurationTuple> parameters;

	public Configuration() {
		parameters = new ArrayList<ConfigurationTuple>();
	}

	public List<ConfigurationTuple> getParameters() {
		return parameters;
	}

	public void setParameters(final List<ConfigurationTuple> parameters) {
		this.parameters = parameters;
	}

	public String getProperty(final String propertyName) {
		for (ConfigurationTuple configurationTuple : parameters) {
			if (propertyName.equals(configurationTuple.getName())) {
				return configurationTuple.getValue();
			}
		}
		return null;
	}

	private void addProperty(final String name,final  String value) {
		parameters.add(new ConfigurationTuple(name, value));
	}

	public static Configuration loadFromFile(final String path) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		FileReader fileReader = new FileReader(path);
		properties.load(fileReader);
		fileReader.close();

		Configuration configuration = new Configuration();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			configuration.addProperty((String) entry.getKey(), (String) entry.getValue());
		}
		return configuration;
	}

	public Integer getProcessorsNumber() {
		String value = getProperty("processor_number");
		if (value == null) {
			return null;
		} else {
			return Integer.parseInt(value);
		}
	}

	public List<Processor> getProcessors() {
		List<Processor> processors = new ArrayList<Processor>();

		int processorNumber = getProcessorsNumber();

		for (int i = 0; i < processorNumber; i++) {
			for (ConfigurationTuple tuple : parameters) {
				if (tuple.getName().equals("processor_" + (i + 1) + "_description")) {
					Processor p = new Processor(tuple.getValue());
					processors.add(p);
					break;
				}
			}
		}

		return processors;
	}

	public String[] getProcessTypes() {
		return getProperty("process_types").split(",");
	}

	public Map<QueueType, ScheduleAlgorithm> getProcessQueuesAlgorithms() {
		String[] processTypes = getProcessTypes();
		Map<QueueType, ScheduleAlgorithm> processQueuesAlgorithms = new HashMap<QueueType, ScheduleAlgorithm>(processTypes.length);
		for (String processType : processTypes) {
			processQueuesAlgorithms.put(QueueType.getByName(processType), getScheduleAlgorithm(getProperty(processType + "_process_algorithm")));
		}
		return processQueuesAlgorithms;
	}

	public Integer getRoundRobinQuantum() {
		String value = getProperty("round_robin_quantum");
		if (value == null) {
			return null;
		} else {
			return Integer.parseInt(value);
		}
	}

	public ScheduleAlgorithm getScheduleAlgorithm(final String abreviation) {
		if ("rr".equals(abreviation)) {
			return new RoundRobin(getRoundRobinQuantum());
		} else if ("sjf".equals(abreviation)) {
			return new SJF();
		} else if ("fcfs".equals(abreviation)) {
			return new FCFS();
		} else if ("priority".equals(abreviation)) {
			return new Priority();
		}
		return null;
	}
}

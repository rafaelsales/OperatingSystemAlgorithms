package configuration;

public class ConfigurationTuple {

	private String name;
	private String value;

	public ConfigurationTuple(final String name,final String value) {
		super();
		this.name = name;
		this.value = value;
	}


	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

}

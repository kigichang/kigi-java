package tw.kigi.web.conf;

import java.util.Collection;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class WebConfig {
	protected XMLConfiguration config = null;
	
	protected String file = null;
	
	
	public WebConfig(String file) throws ConfigurationException {
		this.file = file;
		config = new XMLConfiguration(file);
	}
	
	
	public int elementSize(String parent, String element, String attribute) {
		StringBuilder sb = new StringBuilder(parent).append('.').append(element).append("[@").append(attribute).append("]");
		Object obj = config.getProperty(sb.toString());
		if (obj == null) {
			return -1;
		}
		else if (obj instanceof Collection) {
			return ((Collection<?>)obj).size();
		}
		else {
			return 0;
		}
	}
	
	
	
}

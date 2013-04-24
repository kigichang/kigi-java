package tw.kigi.web.conf;

import java.util.HashMap;

public class Module {
	protected String[] globalMappings = null;
	
	protected HashMap<String, ActionBean> actions = null;
	
	public Module(String[] mappings, HashMap<String, ActionBean> actions) {
		this.globalMappings = mappings;
		this.actions = actions;
	}
	
	public ActionBean getAction(String uri) {
		return actions.get(uri);
	}
	
	public String[] getGlobalMappings() {
		return globalMappings;
	}
}

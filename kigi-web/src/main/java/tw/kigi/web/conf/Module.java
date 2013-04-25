package tw.kigi.web.conf;

import java.util.HashMap;

public class Module {
	
	protected static HashMap<String, Module> modules = new HashMap<String, Module>();
	
	public static void putModule(String path, Module module) {
		modules.put(path, module);
	}
	
	public static Module getModule(String path) {
		return modules.get(path);
	}
	
	
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

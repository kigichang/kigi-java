package tw.kigi.web;

import javax.servlet.ServletException;

public class ActionMapping {
	private String[] globalMappings = null;
	private String[] localMappings = null;
	
	public ActionMapping(String[] global, String[] local) {
		globalMappings = global;
		localMappings = local;
	}
	
	
	public ActionNext findNext(String name) throws ServletException {
		if (name == null || name.length() == 0) {
			throw new ServletException("Parameter Error");
		}
		
		for (int i = 0, len = localMappings.length; i < len; i += 3) {
			if (localMappings[i].equals(name)) {
				ActionType type = ActionType.valueOf(localMappings[i + 2]);
				return new ActionNext(localMappings[i], localMappings[i + 1], type);
			}
		}
		
		
		for (int i = 0, len = globalMappings.length; i < len; i += 3) {
			if (globalMappings[i].equals(name)) {
				ActionType type = ActionType.valueOf(globalMappings[i + 2]);
				return new ActionNext(globalMappings[i], globalMappings[i + 1], type);
			}
		}
		
		throw new ServletException("Next [" + name + "] Not Found");
	}
}

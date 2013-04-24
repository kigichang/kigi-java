package tw.kigi.web;

public class ActionMapping {
	private String[] globalMappings = null;
	private String[] localMappings = null;
	
	public ActionMapping(String[] global, String[] local) {
		globalMappings = global;
		localMappings = local;
	}
}

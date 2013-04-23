package tw.kigi.web;

public class ActionNext {
	private String name = null;
	private String path = null;
	private ActionType type = null;
	
	public ActionNext(String module, String name, String path, ActionType type) {
		this.name = name;
		this.type = type;
		
		if (ActionType.REDIRECT != type) {
			// TODO generate full path like /WEB-INF/tpl/xxxx/xxx/xx.jsp
			// 1. startWith '/' then concate with /WEB-INF/tpl/
			// 2. contains '/' then concate with /WEB-INF/tpl/module/
			// 3. only file name then concate with /WEB-INF/tpl/module/action_name/xxx.jsp
			
		}
		else {
			this.path = path;
		}
		
	}
	
	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public ActionType getType() {
		return type;
	}
	
	
	
}

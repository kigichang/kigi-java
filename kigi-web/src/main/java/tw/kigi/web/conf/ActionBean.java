package tw.kigi.web.conf;

import java.lang.reflect.Method;

public class ActionBean {
	
	protected String path = null;
	protected String className = null;
	
	ActionMethod[] methods = null;
	
	String[] mappings = null;
	
	
	public Method getMethod(String path) {
		for (int i = 0, size = methods.length; i < size; i++) {
			if (path.equals(methods[i].path)) {
				return methods[i].method;
			}
		}
		
		return null;
	}
	
	public ActionMethod[] getMethods() {
		return methods;
	}
	
	public String getPath() {
		return path;
	}


	public String getClassName() {
		return className;
	}
	
	public String[] getMappings() {
		return mappings;
	}
}

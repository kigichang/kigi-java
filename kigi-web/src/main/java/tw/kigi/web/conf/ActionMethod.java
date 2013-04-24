package tw.kigi.web.conf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.kigi.web.Action;
import tw.kigi.web.ActionMapping;
import tw.kigi.web.ActionNext;

public class ActionMethod {
	
	protected String path = null;
	protected Method method = null;
	
	public ActionMethod(String path, Method method) {
		this.path = path;
		this.method = method;
	}
	
	
	public String getPath() {
		return path;
	}

	public Method getMethod() {
		return method;
	}
	
	public ActionNext invoke(Action data, HttpServletRequest req, HttpServletResponse resp, ActionMapping mappings, Object...other) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (ActionNext) method.invoke(data, req, resp, mappings, other);
	}
}

package tw.kigi.web.conf;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.ArrayUtils;

import tw.kigi.web.Action;
import tw.kigi.web.ActionMapping;
import tw.kigi.web.ActionNext;
import tw.kigi.web.ActionType;

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
			return 0;
		}
		else if (obj instanceof Collection) {
			return ((Collection<?>)obj).size();
		}
		else {
			return 1;
		}
	}
	
	protected void getMapping(String parent, ActionType type, int size, int start, String[] result) {
		if (size == 0) {
			return;
		}
		
		String element = type.elementString();
		
		if (size == 1) {
			StringBuilder tmp = new StringBuilder(parent).append('.').append(element);
			result[start] = config.getProperty(tmp + "[@name]").toString();
			result[start + 1] = config.getProperty(tmp + "[@path]").toString();
			result[start + 2] = type.name();
		}
		else if (size > 1) {
			for (int i = 0; i < size; i++) {
				StringBuilder tmp = new StringBuilder(parent).append('.').append(element).append('(').append(i).append(')');
				result[start + (i * 3)] = config.getProperty(tmp + "[@name]").toString();
				result[start + 1 + (i * 3)] = config.getProperty(tmp + "[@path]").toString();
 				result[start + 2 + (i * 3)] = type.name();
			}
		}
	}
	
	protected String[] getMappings(String parent) {
		int forward_size = elementSize(parent, "forward", "name");
		int redirect_size = elementSize(parent, "redirect", "name");
		int include_size = elementSize(parent, "include", "name");
		
		int total = forward_size + include_size + redirect_size;
		if (total == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		
		String[] ret = new String[total * 3];
		
		int start = 0;
		getMapping(parent, ActionType.FORWARD, forward_size, start, ret);
		
		start += forward_size * 3;
		getMapping(parent, ActionType.REDIRECT, redirect_size, start, ret);
		
		start += redirect_size * 3;
		getMapping(parent, ActionType.INCLUDE, include_size, start, ret);
			
		return ret;
	}
	
	ActionMethod[] getMethods(String parent, Class<?> clazz) throws NoSuchMethodException, SecurityException {
		int size = elementSize(parent, "method", "name");
		ActionMethod[] ret = new ActionMethod[size];
		
		if (size == 1) {
			StringBuilder tmp = new StringBuilder(parent).append(".method");
			String name = config.getProperty(tmp + "[@name]").toString();
			String path = config.getProperty(tmp + "[@path]").toString();
			Method m = clazz.getDeclaredMethod(name, HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class);
			
			if (m.getReturnType().equals(ActionNext.class)) {
				ret[0] = new ActionMethod(path, m);
			}
			else {
				throw new NoSuchMethodException(name + " return type is not correct");
			}
			
		}
		else if (size > 1) {
			for (int i = 0; i < size; i++) {
				StringBuilder tmp = new StringBuilder(parent).append(".method(").append(i).append(')');
				String name = config.getProperty(tmp + "[@name]").toString();
				String path = config.getProperty(tmp + "[@path]").toString();
				Method m = clazz.getDeclaredMethod(name, HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class);
				if (m.getReturnType().equals(ActionNext.class)) {
					ret[i] = new ActionMethod(path, m);
				}
				else {
					throw new NoSuchMethodException(name + " return type is not correct");
				}
 				
			}
		}
		
		
		return ret;
	}
	
	public String[] getGlobalMappings() {
		return getMappings("global-mappings");
	}
	
	public ActionBean getAction(String parent) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		ActionBean bean = new ActionBean();
		bean.path = config.getProperty(parent + "[@path]").toString();
		bean.className = config.getProperty(parent + "[@class]").toString();
		
		bean.mappings = getMappings(parent);
		bean.methods = getMethods(parent, Class.forName(bean.className));
		return bean;
	}
	
	
	
	public HashMap<String, ActionBean> getActions() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		int size = elementSize("actions", "action", "class");
		
		if (size == 1) {
			ActionBean bean = getAction("actions.action");
			HashMap<String, ActionBean> map = new HashMap<String, ActionBean>(1);
			map.put(bean.path, bean);
			return map;
		}
		else if (size > 1) {
			HashMap<String, ActionBean> map = new HashMap<String, ActionBean>(size);
			for (int i = 0; i < size; i++) {
				String parent = "actions.action(" + i + ")";
				ActionBean bean = getAction(parent);
				map.put(bean.path, bean);
			}
			
			return map;
		}
		
		return null;
	}
}

package tw.kigi.web.conf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.kigi.web.Action;
import tw.kigi.web.ActionMapping;
import tw.kigi.web.ActionNext;
import tw.kigi.web.ActionType;
import tw.kigi.web.Request;


public class Module {
	
	protected static HashMap<String, Module> modules = new HashMap<String, Module>();
	
	public static void putModule(String path, Module module) {
		modules.put(path, module);
	}
	
	public static Module getModule(String path) {
		return modules.get(path);
	}
	
	protected static String[] splitURI(String uri) {
		if (uri == null) {
			return null;
		}
		
		int len = uri.length();
		
		if (len == 0) {
			return null;
		}
		
		if (uri.charAt(0) != '/') {
			return null;
		}
		
		int i = 1, start = 0;
		List<String> ret = new ArrayList<String>();
		
		while(i < len) {
			if (uri.charAt(i) == '/') {
				ret.add(uri.substring(start, i));
				start = i;
			}
			i++;
		}
		
		ret.add(uri.substring(start));
		return ret.toArray(new String[ret.size()]);
	}
	
	
	protected static void setEncoding(HttpServletRequest request) {
		String enc = MyConst.DEFAULT_ENCODING;
		
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				enc = "UTF-8";
	        }
			
            if (!enc.equals(request.getCharacterEncoding())) {
                request.setCharacterEncoding(enc);
            }
        }
		catch(UnsupportedEncodingException e) {
			
		}
	}
	
	public static void firstTime(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String request_uri = req.getRequestURI();
		String[] tmp = splitURI(request_uri);
		
		Module module = Module.getModule(tmp[0]);
		
		if (module == null) {
			resp.sendError(404);
			return;
		}
		
		String action_name = tmp.length > 1 ? tmp[1] : "/";
		
		ActionBean bean = module.getAction(action_name);
		
		if (bean == null) {
			resp.sendError(404);
			return;
		}
		
		req.setAttribute(MyConst.ATTR_MODULE, tmp[0]);
		
		String method_name = tmp.length > 2 ? tmp[2] : "/";
		
		ActionMethod method = bean.getMethod(method_name);
		ActionNext next = null;
		try {
			req = new Request(req);
			ActionMapping mapping = new ActionMapping(module.getGlobalMappings(), bean.getMappings());
			Action action = (Action)Class.forName(bean.getClassName()).newInstance();
			setEncoding(req);
			
			next = action.beforeFilter(req, resp, mapping);
			
			if (next != null) {
				next.handle(req, resp);
				if (next.getType() != ActionType.INCLUDE) {
					return;
				}
			}
			
			if (method == null) {
				next = action.unspecified(req, resp, mapping);
			}
			else {
				next = method.invoke(action, req, resp, mapping);
			}
			
			if (next != null) {
				next.handle(req, resp);
				if (next.getType() != ActionType.INCLUDE) {
					return;
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException | InvocationTargetException e) {

			throw new ServletException(e);
		}
	}
	
	public static void firstTime(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		String request_uri = req.getRequestURI();
		String[] tmp = splitURI(request_uri);
		
		Module module = Module.getModule(tmp[0]);
		
		if (module == null) {
			chain.doFilter(req, resp);
			return;
		}
		
		String action_name = tmp.length > 1 ? tmp[1] : "/";
		
		ActionBean bean = module.getAction(action_name);
		
		if (bean == null) {
			chain.doFilter(req, resp);
			return;
		}
		
		req.setAttribute(MyConst.ATTR_MODULE, tmp[0]);
		
		String method_name = tmp.length > 2 ? tmp[2] : "/";
		
		ActionMethod method = bean.getMethod(method_name);
		ActionNext next = null;
		try {
			req = new Request(req);
			ActionMapping mapping = new ActionMapping(module.getGlobalMappings(), bean.getMappings());
			Action action = (Action)Class.forName(bean.getClassName()).newInstance();
			setEncoding(req);
			
			next = action.beforeFilter(req, resp, mapping);
			
			if (next != null) {
				next.handle(req, resp);
				if (next.getType() != ActionType.INCLUDE) {
					return;
				}
			}
			
			if (method == null) {
				next = action.unspecified(req, resp, mapping);
			}
			else {
				next = method.invoke(action, req, resp, mapping);
			}
			
			if (next != null) {
				next.handle(req, resp);
				if (next.getType() != ActionType.INCLUDE) {
					return;
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException | InvocationTargetException e) {

			throw new ServletException(e);
		}
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

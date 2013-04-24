package tw.kigi.web.filter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;

import tw.kigi.web.Action;
import tw.kigi.web.ActionMapping;
import tw.kigi.web.ActionNext;
import tw.kigi.web.conf.ActionBean;
import tw.kigi.web.conf.ActionMethod;
import tw.kigi.web.conf.Module;
import tw.kigi.web.conf.WebConfig;

public class PrepareModule implements Filter {

	private HashMap<String, Module> modules = new HashMap<String, Module>();
	
	private static String encoding = "UTF-8";
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String request_uri = request.getRequestURI();
		String[] tmp = splitURI(request_uri);
		
		Module module = modules.get(tmp[0]);
		
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
		
		String method_name = tmp.length > 2 ? tmp[2] : "/";
		
		ActionMethod method = bean.getMethod(method_name);
		ActionNext next = null;
		try {
			ActionMapping mapping = new ActionMapping(module.getGlobalMappings(), bean.getMappings());
			Action action = (Action)Class.forName(bean.getClassName()).newInstance();
			setEncoding(request);
			if (method == null) {
				next = action.unspecified(request, response, mapping);
			}
			else {
				next = method.invoke(action, request, response, mapping);
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException | InvocationTargetException e) {

			throw new ServletException(e);
		}
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String path = new StringBuilder(config.getServletContext().getRealPath("/")).append(File.separatorChar).append("WEB-INF").toString();
		
		try {
			for (Enumeration<?> e = config.getInitParameterNames(); e.hasMoreElements();) {
				String param = (String) e.nextElement();
				if (param.charAt(0) != '/') {
					continue;
				}
				
				String file = config.getInitParameter(param);
				WebConfig web = new WebConfig(path + file);
				String[] global_mappings = web.getGlobalMappings();
				HashMap<String, ActionBean> actions = web.getActions();
				modules.put(param, new Module(global_mappings, actions));
				
			}
		} catch (ConfigurationException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}
	
	protected String[] splitURI(String uri) {
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
	
	protected void setEncoding(HttpServletRequest request) {
		String enc = PrepareModule.encoding;
		
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
}

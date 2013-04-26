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
import tw.kigi.web.ActionType;
import tw.kigi.web.Request;
import tw.kigi.web.conf.ActionBean;
import tw.kigi.web.conf.ActionMethod;
import tw.kigi.web.conf.Module;
import tw.kigi.web.conf.MyConst;
import tw.kigi.web.conf.WebConfig;

public class PrepareModule implements Filter {
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		Module.firstTime((HttpServletRequest)req, (HttpServletResponse)resp, chain);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String path = new StringBuilder(config.getServletContext().getRealPath("/")).append(File.separatorChar).append("WEB-INF").append(File.separatorChar).toString();
		
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
				Module.putModule(param, new Module(global_mappings, actions));
				
			}
		} catch (ConfigurationException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}
	
	
}

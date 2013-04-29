package tw.kigi.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;

import tw.kigi.web.conf.ActionBean;
import tw.kigi.web.conf.Module;
import tw.kigi.web.conf.WebConfig;

public class ExecuteModule extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Module.firstTime((HttpServletRequest)req, (HttpServletResponse)resp, chain);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ServletConfig config = getServletConfig();
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

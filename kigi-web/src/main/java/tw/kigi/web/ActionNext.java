package tw.kigi.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.kigi.web.conf.MyConst;

public class ActionNext {
	private String name = null;
	private String path = null;
	private ActionType type = null;
	private StringBuilder params = null;
	
	public ActionNext(String name, String path, ActionType type) {
		this.name = name;
		this.type = type;
		this.path = path;
		params = new StringBuilder();
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
	
	public ActionNext param(String key, String value) {
		params.append('&').append(key).append('=').append(value);
		return this;
	}
	
	private String url() {
		if (params.length() > 0) {
			if (path.indexOf('?') < 0) {
				params.replace(0, 1, "?");
			}
		}
		
		return (path + params);
	}
	
	
	protected void handleRedirect(HttpServletResponse resp) throws IOException {
		resp.sendRedirect(url());
	}
	
	protected void handleForward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = url();
		if (url.charAt(0) == '/') {
			req.getRequestDispatcher(url).forward(req, resp);
			return;
		}
		
		String module_name = (String)req.getAttribute(MyConst.ATTR_MODULE);
		url = new StringBuilder(MyConst.DETAULT_TPL_PATH).append(module_name).append('/').append(url).toString();
		
		req.getRequestDispatcher(url).forward(req, resp);
	}
	
	
	protected void handleInclude(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = url();
		if (url.charAt(0) == '/') {
			req.getRequestDispatcher(url).include(req, resp);
			return;
		}
		
		String module_name = (String)req.getAttribute(MyConst.ATTR_MODULE);
		url = new StringBuilder(MyConst.DETAULT_TPL_PATH).append(module_name).append('/').append(url).toString();
		req.getRequestDispatcher(url).include(req, resp);
	}
	
	public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		switch(type) {
		case FORWARD:
			handleForward(req, resp);
			break;
		case INCLUDE:
			handleInclude(req, resp);
			break;
		case REDIRECT:
			handleRedirect(resp);
			break;
		}
	}
	
	
	
}

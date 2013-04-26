package tw.kigi.test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet2 extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path_info = req.getPathInfo();
		String query_string = req.getQueryString();
		
		resp.getWriter().println("servlet2 pathInfo=[" + path_info + "], query_string=[" + query_string + "]");
		
		String[] vals = req.getParameterValues("abc");
		
		for(String v : vals) {
			resp.getWriter().println("abc value = [" + v + "]");
		}
		
		resp.getWriter().println("abc one value = [" + req.getParameter("abc") + "]");
		
		vals = req.getParameterValues("a");
		
		for(String v : vals) {
			resp.getWriter().println("a value = [" + v + "]");
		}
		
		
		Map map = req.getParameterMap();
		
		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			String name = (String)i.next();
			Object val = map.get(name);
			if (val instanceof Array) {
				resp.getWriter().println(name + " = [" + map.get(name) + "]");
			}
			else {
				String[] s = (String[]) val;
				resp.getWriter().println(name + " = [" + s[0] + "]");
			}
		}
	}

}

package tw.kigi.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet1 extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String path_info = req.getPathInfo();
		String query_string = req.getQueryString();
		
		resp.getWriter().println("servlet1 pathInfo=[" + path_info + "], query_string=[" + query_string + "]");
		
		req.getRequestDispatcher("/servlet2/now_in_servlet2/?abc=def&abc=xyz").forward(req, resp);
	}

}

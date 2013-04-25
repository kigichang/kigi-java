package tw.kigi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class Request extends HttpServletRequestWrapper {

	
	public Request(HttpServletRequest request) {
		super(request);
	}

}

package tw.kigi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Action {

	public ActionNext beforeFilter(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
		return null;
	}
	
	public abstract ActionNext unspecified(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping);
	
	public void beforeRender(HttpServletRequest request, HttpServletResponse response) {
		
	}
}

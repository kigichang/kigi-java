package tw.kigi.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class Action {

	public ActionNext beforeFilter(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)  throws ServletException, IOException {
		return null;
	}
	
	public abstract ActionNext unspecified(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws ServletException, IOException; 
	
}

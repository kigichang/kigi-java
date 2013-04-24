package tw.kigi.web;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test1 extends Action {

	@Override
	public ActionNext unspecified(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping) {
		
		return null;
	}
	
	
	public ActionNext abc(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping) {
		return null;
	}
	
	public ActionNext def(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping) {
		return null;
	}

}

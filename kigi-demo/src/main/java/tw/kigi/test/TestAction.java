package tw.kigi.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.kigi.web.Action;
import tw.kigi.web.ActionMapping;
import tw.kigi.web.ActionNext;

public class TestAction extends Action {

	@Override
	public ActionNext unspecified(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping) throws ServletException, IOException {
		String map = request.getParameter("map");
		map = map == null || map.length() == 0 ? "abc" : map;
		return mapping.findNext(map).param("test", "xxx");
	}

}

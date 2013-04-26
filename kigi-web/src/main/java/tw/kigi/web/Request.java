package tw.kigi.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class Request extends HttpServletRequestWrapper {

	private HashMap<String, String[]> my_parameters = null;
	private boolean inited_my_parameters = false;
	
	public Request(HttpServletRequest request) {
		super(request);
	}

	
	protected void putValues(String name, String...values) {
		if (values == null || values.length == 0) {
			return;
		}
		
		if (!inited_my_parameters) {
			my_parameters = new HashMap<String, String[]>();
			inited_my_parameters = true;
		}
		
		String[] old_values = my_parameters.get(name);
		if (old_values == null) {
			my_parameters.put(name, values);
		}
		
		int new_size = old_values.length + values.length;
		String[] new_values = new String[new_size];
		System.arraycopy(new_values, 0, values, 0, values.length);
		System.arraycopy(new_values, values.length, old_values, 0, old_values.length);
		
		my_parameters.put(name, new_values);
	}


	@Override
	public String getParameter(String name) {
		if (!inited_my_parameters) {
			return super.getParameter(name);
		}
		
		String[] val = my_parameters.get(name);
		if (val == null || val.length == 0) {
			return super.getParameter(name);
		}
		return val[0];
	}


	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		if (!inited_my_parameters) {
			return super.getParameterMap();
		}
		
		return null;
	}


	@Override
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		if (!inited_my_parameters) {
			return super.getParameterNames();
		}
		
		return null;
	}


	@Override
	public String[] getParameterValues(String name) {
		if (!inited_my_parameters) {
			return super.getParameterValues(name);
		}
		
		String[] val = my_parameters.get(name);
		if (val == null || val.length == 0) {
			return super.getParameterValues(name);
		}
		
		String[] org_val = super.getParameterValues(name);
		if (org_val == null || org_val.length == 0) {
			return val;
		}
		
		String[] ret = new String[val.length + org_val.length];
		System.arraycopy(ret, 0, val, 0, val.length);
		System.arraycopy(ret, val.length, org_val, 0, org_val.length);
		
		return ret;
	}
	
	public Object val(String name, Object def_val) {
		Object tmp = super.getAttribute(name);
		if (tmp != null) {
			return tmp;
		}
		
		tmp = getParameter(name);
		if (tmp != null) {
			return tmp;
		}
		
		return def_val;
	}
	
}

package tw.kigi.web;

import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;

import tw.kigi.web.conf.ActionBean;
import tw.kigi.web.conf.ActionMethod;
import tw.kigi.web.conf.WebConfig;
import junit.framework.TestCase;

public class TestModuleConfig extends TestCase {
	
	/**
     * Rigourous Test :-)
	 * @throws ConfigurationException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
     */
    public void testLoad() throws ConfigurationException, NoSuchMethodException, SecurityException, ClassNotFoundException
    {
        WebConfig config = new WebConfig("module-xxx.xml");
        //System.out.println(config.elementSize("global-mappings", "redirect", "name"));
        
        String[] gmap = config.getGlobalMappings();
        
        System.out.println("size = " + gmap.length);
        
        for (int i = 0, size = gmap.length; i < size; i += 3) {
        	System.out.println(gmap[i] + ":" + gmap[i + 1] + ":" + gmap[i + 2]);
        	
        }
        
        
        HashMap<String, ActionBean> beans = config.getActions();
        for (ActionBean bean : beans.values()) {
        	System.out.println("bean.path = " + bean.getPath());
        	System.out.println("bean.class = " + bean.getClassName());
        
        	String[] amap = bean.getMappings();
        	for (int i = 0, size = amap.length; i < size; i += 3) {
        		System.out.println(amap[i] + ":" + amap[i + 1] + ":" + amap[i + 2]);
        	
        	}
        	
        	ActionMethod[] methods = bean.getMethods();
        	for (ActionMethod method : methods) {
        		System.out.println(method.getPath());
        	}
        }
    }
}

package br.com.allstays.site.sec;

import br.com.allstays.site.res.db.Query;

public class SiteSecException extends Exception{
	public SiteSecException() {
		
	}
	
	public SiteSecException(String msg) {
		super("SECURITY EXCEPTION !!!..."+msg);
	}
	
	public SiteSecException(String msg, Enviroment enviroment) {
		super("SECURITY EXCEPTION !!!..."+msg+""+enviroment.getName());
	}
}

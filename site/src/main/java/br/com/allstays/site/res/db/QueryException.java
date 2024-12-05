package br.com.allstays.site.res.db;

import br.com.allstays.site.sec.Enviroment;

public class QueryException extends Exception{
	public QueryException() {
		
	}

	public QueryException(String msg) {
		super("QUERY EXCEPTION..."+msg);
	}
	
	public QueryException(String msg, Query query, Enviroment enviroment) {
		super("QUERY EXCEPTION..."+msg+""+enviroment.getName());
	}
}

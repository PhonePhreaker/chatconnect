package br.com.allstays.site.res.db;

public class NullArgException extends Exception {

		public NullArgException() {
			
		}
		public NullArgException(String msg) {
			super("NullArgExcpetion, Illegal argument passed to function "+ msg);
		}
}

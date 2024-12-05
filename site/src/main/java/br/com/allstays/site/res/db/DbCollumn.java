package br.com.allstays.site.res.db;

import java.lang.reflect.Field;
import java.sql.Types;



public class DbCollumn{
	
	protected String CollName;
	protected long CharQuant;
	protected int Type;

	/*
	protected DbCollumn(String collName, Types dataType, long charQuant, int length) {
		CollName = collName;
		Type = dataType;
		CharQuant = charQuant;
		Length = length;
	}
	*/
	
	protected DbCollumn(String collName, int dataType, int charQuant) {
		CollName = collName;
		Type = dataType;
		CharQuant = charQuant;
	}
	
	protected String formatQuery() {
		if(CharQuant == 0) {
		return CollName+" "+Type+",";
		}
		return CollName+" "+Type+" ("+CharQuant+"),";
	}
}

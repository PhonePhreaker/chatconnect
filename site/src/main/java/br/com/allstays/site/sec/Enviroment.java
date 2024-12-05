package br.com.allstays.site.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.allstays.site.res.db.Query;


/*
 * 
 * WILL ONLY HANDLE THREAD AUTHENTICATION!!! 
 * For virtual File System Handeling go to the Query class
 * 
 * To acces the resources and functions on ther server a thread needs to be admin
 * 
 * Each thread thas is able to spawn a function has to be able to be a direct child 
 * to a previous adming thread
 * 
 *  Net :Config if the user threads will have access to networking capabilities 
 *  QueryAcc :How much the user threads can query
 *  dynContent :How much the user threads can modify dynamic pages 
 *  
 *  "root.*\ && root.*"
 *  
 *  this will signal that from the root folder forwards the enviroment admin will have acess to
 *  all files denoted by *\ (a file name is typically file\ && is 
 *  
 *  and just * denotes all directories/packages
 *  
 *  this paths will be used by query
 */


public class Enviroment {
	
//index in global list
	public final int Index;
	
	private final String EnvName;
	private final String DbName;
	private final String MemDbName;
	private final Thread AdminThread;
	private ArrayList<Thread> userThread;
	
//---USER SETTINGS
	private boolean Net = false;
	private String QueryAcc;
	private int dynContent;
	
//Global enviroments list to compare to	
	private static ArrayList<Enviroment> EnviromentsList = new ArrayList();
	
//Both methods are safe using the same synchronize key
//returns the index of where the new Enviroment object was added to
	private synchronized int add2list() {
		EnviromentsList.add(this);
		return EnviromentsList.size();
	}
	//get the latest item on the
	private synchronized static Enviroment getlist() {
		
		if(EnviromentsList == null) {
			return null;
		}
		try {
			return EnviromentsList.getLast();
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	
	private String getQueryacc() {
		return QueryAcc;
	}
	private synchronized static Enviroment getIndex(int index) throws IOException {
		try {
			return EnviromentsList.get(index);
		}catch(NoSuchElementException e) {
			throw new IOException("Could not get enviroment at index"+ index);
		}
	}
	
	public synchronized static String getQueryAcc(int index) throws IOException{
		Enviroment get = getIndex(index);
		return get.getQueryacc();
	}
	
//added here to not happen a race condition
	private synchronized static boolean threadEquals(Thread prevThread) {
		Enviroment get; 
//no previous threads so this is admin thread
		if((get = getlist()) == null) {
			return true;
		} 
		if(prevThread == null) {
			return false;
		}
		
		if(get.equals(prevThread)) {
			return true;
		}
			return false;
	}
	
	public String getName() {
		return EnvName;
	}
	
	public String getDbName(){
		return DbName;
	}

	
	private Enviroment(boolean net, String queryacc, boolean add2list, String dbname, String memDbName, String envName) {
		AdminThread = Thread.currentThread();
		Net = net;
		QueryAcc = queryacc;
		DbName = dbname;
		MemDbName = memDbName;
		EnvName = envName;
		if(add2list) {
			Index = add2list();
		} else {
			Index = -1;
		}
	}
	
	public static Enviroment newEnviroment(boolean net, Thread prevThread, String queryacc, boolean add2list, String dbname, String memdb, String envName) throws SiteSecException {
		if(threadEquals(prevThread) == false) {
			
			throw new SiteSecException("Unvalid eviroment creation, thread is not Admin!");
		}
		
		
		Enviroment send = new Enviroment(net,queryacc, add2list, dbname, memdb, envName);
		return send;	
	}
	
	public Enviroment getEnvAt(int index) {
		return EnviromentsList.get(index);
	}
	
	public boolean net() {
		return Net;
	}
	
	public boolean isAdmin(Thread thread) {
		if(AdminThread.equals(thread)) {
			return true;
		}
		return false;
	}
	
	
}

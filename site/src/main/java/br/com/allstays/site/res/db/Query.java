package br.com.allstays.site.res.db;

import java.io.File;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.util.ArrayUtils;

import br.com.allstays.site.net.ClientID;
import br.com.allstays.site.sec.Enviroment;

/*
 * Will handle all resources at higher level and safety restrictions than DbHyper which
 * handles raw database requests
 * 
 * Query Engine
 */

class FsFile{
	
	private final User Owner;
	private final File File;
	private final String DirName;
	private final int DirType;
	
	FsFile(File file, User owner, String dirName, int dirType){
		File = file;
		Owner = owner;
		DirName = dirName;
		DirType = dirType;
	}
	
	File getFile(User user) {
		if(Owner.isUser(user)) {
			return File;
		}
		return null;
	}
}

//A service is just a file but with a classpath 
class Service{
	
	Class get;
	Service(String classPath){
		
	}
	
}

/*
 * Package is the same as a directory but a Service will be activelly be working on it
 * 
 */
class Package{
	private Service service[];
	private FsFile file[];
	
	Package(int len, FsFile initf, Service inits){
		
	}
	
}
class Directory{
	
	String Name;
	FsFile files[];
	
}

//to be stored by the Fs class
//FsIndex is the cached index to where this Object is stored in the Fs array
/*
 * Users wont story ip addreses only sessions, each time a client connects a new session is 
 * created so User cant have final session ID
 * 
 */
class User {
	private final int FsIndex;
	//private Session SessionId;
	private final String UserId;
	private final Byte[] Unique;
	private final Byte[] DecryptedUnique;
	private final String Acc;
	private Session Session[];
	//private final Thread Thread_;
	//private final String IpAddr;
	private final String Name;
	private final Fs Fs;
	private final boolean loadedFDb;
	//loadedFDb determines if it was loaded from the Db or started by creating a new session

	
	public boolean auth(ClientID get) {
		return false;
	}
	
	boolean isCurrAcc(String acc) {
		
		if(Acc.equals(acc)) {
			return true;
		}
		return false;
	}

	//TODO
	//TODO Create a method thats get the encrypted unique
	//TODO
	
	//TODO
	//TODO Create Register user class which will register a new user using a decrypted Unique
	//TODO 
	
	
	boolean isUser(String sessionId, String ipAddr, Byte[] decunique) throws NullArgException{
		
		if(Fs.isThreadAdmin()) {
			return true;
		}
		
		if(sessionId == null || Session == null || ipAddr == null || decunique == null) {
			throw new NullArgException("Illegar argument passed to isUser method");
		}
		
		for(Session get : Session) {
			if(get.authId(sessionId) && get.authAddr(ipAddr) && decunique == DecryptedUnique) {
				return true;
			}
		}
		
		return false;
	}
	
	

//LOADED FROM DATABASE-------- 	
	User(String name, Fs fs,  Byte[] unique, boolean admin) throws NullArgException{
		
		Fs = fs;
		if(Fs == null || unique == null || name == null) {
			throw new NullArgException("Fs Object is passed as null...");
		}
		
		if(Fs.isThreadAdmin()) {
			admin = true;
		}
		
		Unique = unique;
		loadedFDb = true;
		FsIndex = Fs.getFsUIndex();
		
	//check if thread creating an admin thread is admin themselves	
		if(admin) {
			if(fs.isThreadAdmin()) {
				Acc = fs.ADMIN;
			} else {
				Acc = name+".*/ && "+name+".*";
			}
			
			System.err.println("Thread is not admin in the local Enviroment creating normal user");
		} else {
				Acc = name+"./ && "+name+".*";
		}
			Name = name;
			
	}
	
	
	User auth(Thread thread, String ipAddr, String sessionId) {
		if(SessionId.equals(sessionId) && Thread_.equals(thread)) { 
			return this;
		}
		return null;
	}
	
}

//Virtual file system
class Fs{
	
	final static int DIR = 0;
	final static int PCKG = 1;
	final static String ADMIN = "root.*/ && root.*";
	final static String USER = "$USER.*/ && $USER.*";
	private final Enviroment Owner;
	
	private int UserPtr = 0;
	
	private User[] User;
	private Directory[] Dir;
	private Package[] Pckg;
	
	synchronized int getFsUIndex() throws QueryException{
		UserPtr++;
		if(UserPtr > User.length) {
			throw new QueryException("Could not create new User User array filled...");
		}
		return UserPtr;
	}
	
	boolean isThreadAdmin() {
		return Owner.isAdmin(Thread.currentThread());
	}

//Test if the selected user has Admin Acc and Thread Admin
	boolean isAdmin(User user) {
		if(user.isCurrAcc(ADMIN) && Owner.isAdmin(user.getThread())) {
			return true;
		}
		return false;
	}
	
//TODO Warning, the load arrays should have the same length
//UserType is stored in the Db and determins if a user is assistent or not  a session that logins into such
//such a user automatically gets the thread into assistent thread, NOTE there cant be more than one Admin 
	Fs(Enviroment enviroment, String[] userName, Byte[][] userUnique, Boolean[] userType, String[] loadf, Integer[] loadftypes, String[] loadir, Integer[] loadirt) throws QueryException{
		
		Object[][] sendUser = {userName,userUnique, userType};
		if(Query.verLenghtOfColls(sendUser) == false) {
			throw new QueryException("User table collumns dont have the same lenght");
		}
		
		User = new User[userName.length];
		int c = 0;
		for(String get : userName) {
			User[c] = new User(get, this, c, userUnique[c], userType[c].booleanValue());
			c++;
		}
		
		Object[][] send = {loadf, loadftypes, loadir, loadirt};
		if(Query.verLenghtOfColls(send) == false) {
			throw new QueryException("Fs table collumns dont have the same length");
		}
		
		FsFile[] files = new FsFile[loadf.length];
		Dir = new Directory[loadf.length];
		Pckg = new Package[loadf.length];
		
		for(String get : loadf) {
			
		}
		
		Owner = enviroment;
	}
	
	User auth(Thread thread, String IpAddr, String SessionId) throws QueryException {
		
		for(int c=0; c<User.length; c++) {
			
			if(User[c].isUser(thread, IpAddr, SessionId)) {
				return User[c];
			}
		}
		throw new QueryException("Could not authenticate user");
	}
	
//transfer packages or directories between users
	void fsocket() {
		
	}
	
	void mkdir() {
		
	}
	
	void mkpckg() {
		
	}
	
	
}

//Each session will be hold by a User object, a user object can have multiple 
//The user object will contain the QueryAcc Session 
class Session {
	
	private final Thread Owner;
	//private final String QueryAcc;
	private final String IpAddr;
	private final String Id;
	int LogType[];
	String Log[];
	
	
	
	Thread getThread() {
		return Owner;
	}
	
	boolean authAddr(String ip) {
		if(IpAddr.equals(ip)) {
			return true;
		}
		return false;
	}
	
	boolean authId(String id) {
		if(Id.equals(id)) {
			return true;
		}
		return false;
	}
	
	boolean auth(String ipAddr, String id){
		if(IpAddr == ipAddr || id == Id) {
			return true;
		}
		return false;
	}
	
	Session(String ipAddr, int logsize,  int logType, Thread owner){
		IpAddr = ipAddr;
		Owner = owner;
		Id = UUID.randomUUID().toString();
		
	}
	
/*
	String getId() {
		return Id;
	}
*/
}


//TODO VER ISSO ---> https://www.w3docs.com/snippets/java/how-to-pass-a-function-as-a-parameter-in-java.html
@FunctionalInterface
interface Function<T>{
	T getValue(ResultSet set, String collumn) throws SQLException;
}

class Wrapper<T>{
	private T[] Hold;
	Wrapper(ResultSet rs, T[] set, Function<T> func, String collumn) throws SQLException {
		Hold = set;
		int c=0;
		while(rs.next() && c!=Hold.length) {
			Hold[c] = func.getValue(rs, collumn);
		}
	}
	
	Wrapper(T[] set){
		Hold = set;
	}
	
	public T[] get() {
		return Hold;
	}

}

public class Query {

	private static final String[] readColl = {"files", "dirnames", "dirtypes", "filetypes"};
	private static final String[] readCollU = {"names", "userunique", "usertype"}; 
	private final static String TB_NAME = "allstaysutils";
	
	private DbHyper Db;
	private Fs Fs;
	private final int DEFAULT_LOG=50;
	
//QueryCounter is how many querys have been done already, can be limited in query per sec
	private ArrayList<Session> SessionIds;
	private ArrayList<User> UserIds;
	private int QueryCounter = 0;
	
	private Enviroment Owner;
	

	
	public Query(Enviroment env, boolean mem, int initUserQuant, int initFileQuant) throws QueryException {
		Owner = env;
		System.out.println("constructor");
		try {
			Fs = getFsFromDb(Owner);
		}catch (QueryException e) {
			
			e.printStackTrace();
			try {
				Fs = setFsFromDb(Owner, initUserQuant, initFileQuant);
			}catch (QueryException a) {
				a.printStackTrace();
				throw new QueryException("Could not create new Fs into Database DEBUG IT!!!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(env.getDbName() != null) {
			Db = DbHyper.newDbHyper(false, true);
		}
	}
	
	private User getUserFromCliId(ClientID id) {
		
	}
	
//create the virtual filesystem and get 	
	private Fs setFsFromDb(Enviroment owner, int initqUsers, int initqFiles) throws QueryException, NullArgException {
		
		DbCollumn[] setColl = {new DbCollumn("files", Types.CHAR, 255), new DbCollumn("dirnames", Types.CHAR, 255),
				new DbCollumn("dirtypes", Types.INTEGER, 0), new DbCollumn("filetypes", Types.INTEGER, 0)};
		
		DbCollumn[] setCollU = {new DbCollumn("names",Types.CHAR, 255), new DbCollumn("userunique", Types.BINARY, 50)
				, new DbCollumn("usertype", Types.BOOLEAN, 0)};
		
			Db.createTable(Owner.getDbName(), TB_NAME, setColl);
			Db.createTable(Owner.getDbName(), "users", setColl);
			
		return new Fs(owner, new String[initqUsers], new Byte[initqUsers][50], new Boolean[initqUsers],
				new String[initqFiles], new Integer[initqFiles], new String[initqFiles], new Integer[initqFiles]);
	}

	
//TODO
//TODO Last working on this
//TODO
//Get the virtual file system from the used db, 
	private Fs getFsFromDb(Enviroment owner) throws QueryException, SQLException {
		
//files are the raw file paths, dirname are the name of the directory and dirtype are its type , package or directory
		ResultSet rs = Db.readTable(owner.getDbName(), TB_NAME, readColl, false);
		ResultSet rsU = Db.readTable(owner.getDbName(), "users", readCollU, false);
		
		Wrapper<String> wrapFiles = getDataFromRs(rs, TB_NAME, "files", Types.CHAR);
		String[] getFiles = wrapFiles.get();
	
		Wrapper<String> wrapDirnames = getDataFromRs(rs, TB_NAME, "dirnames", Types.CHAR);
		String[] getDir = wrapDirnames.get();
		
		Wrapper<Integer> wrapDirtypes = getDataFromRs(rs, TB_NAME, "dirtypes", Types.INTEGER);
		Integer[] getDirtypes  = wrapDirtypes.get();
		
		Wrapper<Integer> wrapFilet = getDataFromRs(rs, TB_NAME, "filetypes", Types.INTEGER);
		Integer[] getFilet = wrapFilet.get();
	
		Wrapper<String> wrapUsers = getDataFromRs(rs, "users", "names", Types.CHAR);
		String[] getUsers = wrapUsers.get();
		
		Wrapper<Byte[]> wrapUnique = getDataFromRs(rs, "users", "userunique", Types.BINARY);
		Byte[][] getUnique = wrapUnique.get();
		
		Wrapper<Boolean> wrapUtype = getDataFromRs(rs, "users", "usertype", Types.BOOLEAN);
		Boolean[] getUtype = wrapUtype.get();
		

		return new Fs(owner, getUsers, getUnique, getUtype, getFiles, getFilet,getDir, getDirtypes);
	}

//Verify one by one in the chain to test if one has a different length
	protected static boolean verLenghtOfColls(Object[][] test) {
		
		Object[] prev = null;
		
		for(Object[] get : test) {
			if(prev != null) {
				if(get.length != prev.length) {
					return false;
				}
			}
			prev = get;
		}
		return true;
	}
	
	private Wrapper getDataFromRs(ResultSet rs, String table, String collumn, int expType) throws QueryException, SQLException {
		
		if(rs.getArray(collumn).getBaseType() != expType) {
			
			throw new QueryException("Expected data type doesnt match with whats on the Db");
		}
		
		rs.last();
		int collSize = rs.getRow();
		rs.first();
		
		switch(expType) {
			case Types.CHAR:
			
				String[] set = new String[collSize];
				//Function<String> = (S) -> {String a = System.out.println("");};
			
				Function<String> func = (rs2, collumn2) -> {return rs2.getString(collumn2);};
				Wrapper<String> wrap = new Wrapper<String>(rs, set, func, collumn);
				return wrap;
				
			case Types.INTEGER:
				
				Integer[] setint = new Integer[collSize];
				
				Function<Integer> funcint = (rs2, collumn2) -> {return rs2.getInt(collumn2);};
				Wrapper<Integer> wrapint = new Wrapper<Integer>(rs, setint, funcint, collumn);
				return wrapint;
			
			case Types.DATE:
				
				Date[] setdate = new Date[collSize];
				
				Function<Date> funcdate = (rs2, collumn2) -> {return rs2.getDate(collumn2);};
				Wrapper<Date> wrapdate = new Wrapper<Date>(rs, setdate, funcdate, collumn);
				return wrapdate;
				
			case Types.BINARY:
				
				byte[] gets = rs.getBinaryStream(collumn).readAllBytes();
				Byte[] send = new Byte[collSize];
				int c = 0;
				for(byte get : gets) {
					send[c] = gets[c];
					c++;
				}
				
				return new Wrapper<Byte>(send);
				
			case Types.BOOLEAN:
				
				Boolean[]setBit = new Boolean[collSize];
				
				Function<Boolean> funcbit = (rs2, collumn2) -> {return rs2.getBoolean(collumn2);};
				Wrapper<Boolean> wrapbit = new Wrapper<Boolean>(rs, setBit, funcbit, collumn);
				
			default:
				
				throw new QueryException("Could not get data from collumn"+collumn+" from table "+table+", unknown datatype");
		}
		
	}
	
//TODO add suport for remote admin login	
//returns the session ID
	public String newSession(String ipAddr) {
		
		if(Owner.isAdmin(Thread.currentThread())) {
		
			
			SessionIds.add(new Session(ipAddr, DEFAULT_LOG,Fs.ADMIN));
		}else {
			SessionIds.add(new Session(ipAddr, DEFAULT_LOG, Fs.USER));
		}
		
		return SessionIds.getLast().Id;
	}
	
	
//TODO terminar isso 
	public String errorReport(String sessionId, boolean print){
		
		System.err.println("ERROR IN SESSION ID");
		
		StringBuilder builder = new StringBuilder();
		
//Will iterate trying to find session ID in the list , change this to a binary search		
		for(int c1=0; c1<SessionIds.size(); c1++) {
			Session get = SessionIds.get(c1);
			if(get.getId() == sessionId) {
				
			}
			System.err.println(get.getId());
		}
		return builder.toString();
	}
}

package br.com.allstays.site.net;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import br.com.allstays.site.sec.Enviroment;
import br.com.allstays.site.sec.SiteSecException;

//Only admin will be able to use get and set
class AdminOnly{
	
	private byte[] Salt;
	
	private AdminOnly(byte[] salt){
		Salt = salt;
	}
	
	boolean auth() {
		
	}
	
	static AdminOnly newAdminOnly(Enviroment enviroment, byte[] salt) throws SiteSecException{
		if(enviroment.isAdmin(Thread.currentThread())) {
			return new AdminOnly(salt);
		}
		throw new SiteSecException("UnAdmin thread tried creatinf an AdminOnly object");
	}
	
	
}

//Data Structure to identify a client will be stored as cookie in the user browser
public class ClientID {
	
	private AdminOnly Admin = null;
	private static final int KEY_LEN = 128;
	
	public static byte[] auth(Enviroment enviroment) throws SiteSecException{
		
	
	}
	
//Random Password set by user,
	public ClientID(String passwd, Enviroment enviroment) {
		
		byte[] byteSalt = UUID.randomUUID().toString().getBytes();
		SecretKeySpec key = getNewKey();
		
		
	}
//Random UUID , session cookie
	public ClientID(Enviroment enviroment) {
		
		byte[] byteSalt = UUID.randomUUID().toString().getBytes();
	}

	private SecretKeySpec getNewKey(char[] passwd, byte[] salt, int interationCount, int keyLenght) throws NoSuchAlgorithmException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		
	}
}

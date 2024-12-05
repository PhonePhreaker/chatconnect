package br.com.allstays.site;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.allstays.site.sec.Enviroment;
import br.com.allstays.site.sec.SiteSecException;
import br.com.allstays.site.net.RequHandle;
import br.com.allstays.site.res.db.Query;

@SpringBootApplication
public class SiteApplication {

	public static final String CONTACT = "ec2-3-137-223-219.us-east-2.compute.amazonaws.com/info";
	private static Enviroment Enviroment;
	private static Query Query;
	private static RequHandle Handle;
	
	public static void main(String[] args) {
		
		System.out.println("ALL STAYS Backend");
		System.out.println("more info at "+CONTACT);
		System.out.println("mais informacoes em "+CONTACT);
		
		try {
			Enviroment = Enviroment.newEnviroment(true, null, "root.* && root.*/", true, "sitedb", "memsitedb", "Main");
		} catch (SiteSecException e){
			
			e.printStackTrace();
			exitServer(ExitType.CRASH);
		}
		Query = new Query(Enviroment, true);
		Handle = new RequHandle(Query);
		
		SpringApplication.run(SiteApplication.class, args);
		System.out.println();
	}	
	
//TODO make so only authorized threads can  exit server
	public static void exitServer(ExitType exit) {
		
		System.exit(-1);
	}

}

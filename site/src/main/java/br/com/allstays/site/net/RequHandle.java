package br.com.allstays.site.net;

import java.io.FileNotFoundException;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.allstays.site.res.Page;
import br.com.allstays.site.res.db.Query;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RequHandle {
	
	Query Query = null;
	
	public RequHandle(Query query) {
		Query = query;
	}
//TODO 	
//Criando nova session	
	@GetMapping ("/*")
	public ResponseEntity handle(@RequestParam("sessionId") String sessionId, HttpServletRequest request) {
		
		if(Query == null) {
			return new ResponseEntity("<h1>Server Startup error</h1>", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String ipaddr = request.getRemoteAddr();
		
		if(sessionId == null) {
			sessionId = Query.newSession(ipaddr);
		}
		return new ResponseEntity("<h1>200 OK</h1>",HttpStatus.OK);
	}
	
	@PostMapping ("/*")
	@ResponseBody
	public ResponseEntity handleResp(@RequestParam("sessionId") String sessionId) {
		if(Query == null) {
			return new ResponseEntity("<h1>Server Startup error</h1>", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.ACCEPTED);
	}
	
	@GetMapping ("/error")
	private ResponseEntity sendErrorAlert(@RequestParam("sessionId") String sessionId) {
		if(Query == null) {
			
		}
		
		if(sessionId == null) {
			
			try {
				Page err = new Page("/home/shreck/");
			}catch(FileNotFoundException e) {
				
			}
		}
		
		String get = Query.errorReport(sessionId, true);
		return new ResponseEntity("<h1>ERROR</h1> <p>"+get+"</p", HttpStatus.BAD_REQUEST);
	}
	
}





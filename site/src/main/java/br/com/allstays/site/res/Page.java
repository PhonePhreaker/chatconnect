package br.com.allstays.site.res;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.springframework.util.ResourceUtils;

import br.com.allstays.site.res.db.Query;

/*
 * Simple file readerer, used mainly for html
 * 
 * Query :which query engine is this page associated with which is associaed with an eviroment
 * and consequently threads (Admin and Users)
 * 
 * Inside Query are the tables
 */

public class Page {
	
	private String Source;
	private final boolean Dyn;
	
	public Page(String sessionId, URI uri, Query query, boolean dyn) {
	
		query.getElement(uri, sessionId);
		Dyn = dyn;
		try {
			
		}catch() {
			
		}
	}

	
	public Page(String path) throws FileNotFoundException {
		
		Dyn = false;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e) {
			System.err.println("file ..."+path+"not found");
			e.printStackTrace();
		} 
		
		StringBuilder builder = new StringBuilder();
		String read;
		try {
			while((read = reader.readLine()) != null) {
					builder.append(read);
			}
		} catch (IOException e) {
			System.out.println("Input Output Exception");
			e.printStackTrace();
		}
		
		Source = builder.toString();
		
	}
	
	public String getSource() {
		return Source;
	}
}


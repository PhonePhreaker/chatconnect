package br.com.allstays.site.net;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.allstays.site.res.Page;

//@RestController
public class testPages {

	@RequestMapping("/test")
	@GetMapping("/test")
	public ResponseEntity handle() throws FileNotFoundException {
		
		URI uri = null;
		try {
			uri = new URI("/test");
		} catch (URISyntaxException e) {
			System.out.println("URI not formated cprreclty");
			e.printStackTrace();
		}

		Page get = new Page("/home/shreck/clientes/eclipse/res/index.html");
		
		return new ResponseEntity<String>(get.getSource(), HttpStatus.OK);
	}
}

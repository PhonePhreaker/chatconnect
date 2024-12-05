package br.com.allstays.site.res;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * NOTA
 * Criar um syntax checker descente
 * 
 * 
 * Instead of passing a dyn html page a json page is parse wich is buiolt into an html page by
 * the client
 */
public class JsonPage {
//Location on the final page were the following tags should be
	private div Head;
	private div Body;
	private div Foot;
	
	public JsonPage(File file) throws IOException {
		
		System.err.println("WARNING as of verion 0.1 syntax checking is not done ");
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String read;
		
		while((read = reader.readLine()) != null) {
			 
			div getDiv = null;
			tag getTag = null;
			
			if(getDiv == null) {
				
			}
			
//Parse the sections in an html page 
			if(read.contains("<head") && read.contains(">")) {
				Head = parseDiv(null);
			} else if(read.contains("</head>")) {
				Head = parseDiv(Head);
			} else if(read.contains("<body") && read.contains(">")) {
				Body = parseDiv(null);
			} else if(read.contains("</body>")) {
				Body = parseDiv(Body);
			} else if(read.contains("<foot") && read.contains(">")) {
				Foot = parseDiv(null);
			} else if(read.contains("</foot>")) {
				Foot = parseDiv(Foot);
			}
			
			if(read.contains("<div")){
				 getDiv = parseDiv(null); 
			 } else if(read.contains("</div>")) {
				 getDiv = parseDiv(getDiv);
			 } else if(read.contains("<") && read.contains(">")) {
				 
			 } 
			 
		}
	}
	
	private void addInfoDiv(div addto) {
		
	}
	
	private div parseDiv(div beg) {
		if(beg == null) {
			
			return;
		}
		
		
	}
	
	private tag parseTag(tag beg) {
		if(beg == null) {
			return ;
		}
	}
}

//This is to implement stuff like onclick in html buttons
class func{
	private final String Id;
	func(String id){
		Id=id;
	}
}

//type of the tag will define its tag type like <h1> or <p>
//content is what is inside <h1>Hello World</h1> content would be "Hello World"
class tag{
	private final String Type;
	private final String Id;
	private final String Class_;
	private final int ServerId;
	private final int Pos;
	private final func ExtraFunc[];
	private final String Content;
	tag(String type, String id, String class_, int serverId, int pos, func extrafunc[], String content){
		Type=type;
		Id=id;
		Class_=class_;
		ServerId=serverId;
		Pos=pos;
		ExtraFunc=extrafunc;
		Content=content;
	}
}

//Each tag or object in the html page will have a servid which is not the same as <p id="">
//its just for building the htmk page
//Pos is the line position on the html file
class div{
	private boolean uncomplete;
	private final String Id;
	private final String Class_;
	private final tag Elements[];
	private final int Pos;
	div(String id, String class_, tag elements[], int pos){
		Id=id;
		Class_=class_;
		Elements=elements;
		Pos=pos;
	}
}
package gribParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.SAXException;

public class grib {

	public static void main(String[] args) throws IOException,SAXException, TikaException{
		System.out.println("Welcome to the GRIB parser!");
		if(args.length < 2 || args.length > 2){
			System.err.println("Usage: java -jar grib.jar <inputfilepath> <outputfilepath>");
			return;
		}
		Parser parser = new gribParser();
		Metadata metadata = new Metadata();
		ToHTMLContentHandler html = new ToHTMLContentHandler();
		
		File filename = new File(args[0]);
		metadata.add(Metadata.RESOURCE_NAME_KEY, args[0]);
		String fileName = args[1];
		PrintWriter writer = null;
		try{
			parser.parse(new FileInputStream(filename),html,metadata,new ParseContext());
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(html.toString());
			writer.close();
			
		}
		catch(IOException e){
			System.err.println("Error: Failed to read the file!");
			return;
		}
		catch (SAXException e){
			System.err.println("Error: SAX parsing failed!");
			return;
		}
		catch (TikaException e){
			System.err.println("Error: TIKA parsing failed!");
			return;
		}
	}

}

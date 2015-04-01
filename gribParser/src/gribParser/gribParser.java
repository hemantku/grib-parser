package gribParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

// Imports similar to the SAX Implementation
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.Dimension;

public class gribParser extends AbstractParser {
	
	public static final String GRIB_MIME_TYPE = "application/x-grib2";

    private final Set<MediaType> SUPPORTED_TYPES =
        Collections.singleton(MediaType.application("x-grib2"));
        
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    public void parse(InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context) throws IOException,
            SAXException, TikaException {
                
        //Set MIME type as grib2
        metadata.set(Metadata.CONTENT_TYPE, GRIB_MIME_TYPE);
    
        //Get grib2 file name from metadata 
        
        File gribFile = new File(metadata.get(Metadata.RESOURCE_NAME_KEY));
        
        try {
            NetcdfFile ncFile = NetcdfDataset.openFile(gribFile.getAbsolutePath(), null);            

            // first parse out the set of global attributes
            for (Attribute attr : ncFile.getGlobalAttributes()) {
                Property property = resolveMetadataKey(attr.getFullName());
                if (attr.getDataType().isString()) {
                    metadata.add(property, attr.getStringValue());
                } else if (attr.getDataType().isNumeric()) {
                    int value = attr.getNumericValue().intValue();
                    metadata.add(property, String.valueOf(value));
                }
            }
                         
            XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);

            xhtml.startDocument();
                    
            xhtml.newline();
            xhtml.startElement("ul");
            xhtml.characters("dimensions:");
            xhtml.newline();

            for (Dimension dim : ncFile.getDimensions()){
                xhtml.element("li", dim.getFullName() + "=" + String.valueOf(dim.getLength()) + ";");
            }
    			
            xhtml.newline();
            xhtml.startElement("ul");
            xhtml.characters("variables:");
            xhtml.newline();
    			
            for (Variable var : ncFile.getVariables()){
                xhtml.newline();
                xhtml.element("p", String.valueOf(var.getDataType()) + var.getNameAndDimensions() + ";");
    			
                List<Attribute> list = (var.getAttributes()); //list of variable attributes
    			
                for(int i=0; i < list.size(); i++){ 
                    Attribute element = (list.get(i));  
                    String text = element.toString();
         						
                    xhtml.element("li", " :" + text + ";");
    		    }
            }   
            xhtml.endElement("ul");
            xhtml.endElement("ul");
            xhtml.endDocument();
             
            } catch (IOException e) {
            throw new TikaException("NetCDF parse error", e);
            } 
        }
        
        private Property resolveMetadataKey(String localName) {
            if ("title".equals(localName)) {
                return TikaCoreProperties.TITLE;
            }
            return Property.internalText(localName);
        }

}

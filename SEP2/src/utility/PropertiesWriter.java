package utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class PropertiesWriter {
	
	public synchronized static void writeProperties(Hashtable<String, String> properties, String filename, String description) throws IOException{
		Properties prop = new Properties();
	
		for (Map.Entry<String, String> entry : properties.entrySet()){
			prop.setProperty(entry.getKey(), entry.getValue());
		}
		
		try (OutputStream out = new FileOutputStream(filename)){
			prop.store(out, description);
		}
	}
	

}

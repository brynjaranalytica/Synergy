package utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


//Implements the Singleton pattern
public class Log {
	
	private static Logger logger;
	private static FileHandler handler;
	
	private static class Wrapper{ //Instance placed in inner class
		static Log instance = new Log();	//Created in memory when called	
	}
	
	private Log(){
		
	}
	
	public static Log getInstance(){ 
		logger = Logger.getLogger("io.neobit.synergy");
		logger.getHandlers();
		Path path = Paths.get("C:\\Synergy");
		if (!Files.exists(path)){
			try {
				Files.createDirectory(path);
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error creating directory", e);
			}		
		}
		try {
			handler = new FileHandler("C:\\Synergy\\synergylog.xml", (1000*1000*1000), 1, true);
			logger.addHandler(handler);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error creating log file", e);
		}

		return Wrapper.instance; //Instantiates the instance when called
	}
	
	//Use this method to log information messages
	public synchronized void logInfo(String message, String exception){
		logger.log(Level.INFO, message, exception);
	}

	//Use this method to log warning messages
	public synchronized void logWarning(String message, String exception){
		logger.log(Level.WARNING, message ,exception);
	}
	
	//Use this method to log severe messages
	public synchronized void logSevere(String message, String exception){
		logger.log(Level.SEVERE, message, exception);
	}

}

package br.com.craic.solr.export.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import br.com.craic.solr.export.exception.ConfigException;

public class ConfigProperties {
	
	public static Properties properties;
	
	public static void load(String file) throws ConfigException {
		if(properties == null) {
			properties = new Properties();
		}
		
		try {
			
			properties.load(new FileInputStream(new File(file)));
			
		} catch (FileNotFoundException e) {
			throw new ConfigException("Configuration file not found.", e);
		} catch (IOException e) {
			throw new ConfigException("Configuration file not found.", e);
		}
   }
	
	public static void setProperty(String key, String value) {
		if(properties == null) {
			properties = new Properties();
		}
		
		properties.setProperty(key, value);
	}
	

}

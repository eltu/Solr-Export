package br.com.craic.solr.export.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import br.com.craic.solr.export.bo.ExportBO;
import br.com.craic.solr.export.config.ConfigProperties;
import br.com.craic.solr.export.exception.DocumentExportException;
import br.com.craic.solr.export.exception.SolrIndexException;

public class Runner {
	private static Logger logger = Logger.getLogger(Runner.class);
	private final static String VERSION = "0.1";
	
	public static void main(String[] args) {	
		setLogging();
				
		CommandLineParser parser = new PosixParser();

		Options options = new Options();
		options.addOption( "i", "index", true, "Path of the index of the Solr." );
		options.addOption( "e", "export", true, "Path for where will be exported the XML file." );

		try {
		    
			CommandLine line = parser.parse( options, args );

		    if (line.hasOption("index"))
		        ConfigProperties.setProperty("solr.index.path", line.getOptionValue("index"));
		        
		    if (line.hasOption("export"))
		    	ConfigProperties.setProperty("solr.export.path", line.getOptionValue("export"));
		    
		    if (!line.hasOption("index") || !line.hasOption("export")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("solrexport -i <PATH> -e <PATH>", options );
				
				System.exit(0);
		    }
		    
		} catch(ParseException e) {
			logger.error("Unexpected exception: ", e);
		}
		
		logger.info(String.format("Version: %s", VERSION));
		logger.info(String.format("PATH Index Solr: %s", ConfigProperties.properties.getProperty("solr.index.path")));
		logger.info(String.format("PATH Export: %s", ConfigProperties.properties.getProperty("solr.export.path")));
		logger.info("=================================================================================================");
		logger.info("Starting Solr Export...");
		
		execute();
		
		logger.info("=================================================================================================");
		logger.info("Fished Solr Export...");
		
		System.exit(0);
	}

	private static void execute() {
		ExportBO export = new ExportBO();
		Integer documentsExported = 0;
		
		try {
		
			documentsExported = export.execute();
			
		} catch (DocumentExportException e) {
			logger.error("Happened a the problem in the document export. Exception: ", e);
			System.exit(0);
		} catch (SolrIndexException e) {
			logger.error("Happened a the problem in the read of index. Exception: ", e);
			System.exit(0);
		}
		
		logger.info(String.format("Total document exported: %s", documentsExported));
	}
	
	private static void setLogging() {
		Properties p = new Properties();
		
		try {
		
			p.load(new FileInputStream("conf/log4j.properties"));
			
		} catch (FileNotFoundException e) {
			logger.error("Configuration file not found!");
		} catch (IOException e) {
			logger.error("Configuration file not found!");
		} 
		
		PropertyConfigurator.configure(p);
	}
	
}

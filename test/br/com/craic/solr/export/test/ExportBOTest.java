package br.com.craic.solr.export.test;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.craic.solr.export.bo.ExportBO;
import br.com.craic.solr.export.config.ConfigProperties;
import br.com.craic.solr.export.exception.ConfigException;
import br.com.craic.solr.export.exception.DocumentExportException;
import br.com.craic.solr.export.exception.SolrIndexException;


public class ExportBOTest {

	@BeforeClass
	public static void loadProperties() {
		try {
			ConfigProperties.load("config/solrexport.properties");
		} catch (ConfigException e) {
			fail("Happened a the problem to load the file. Exception: " + e.getMessage());
		}
	}
	
	@Test
	public void executeExportTest() {
		ExportBO bo = new ExportBO();
		
		try {
		
			bo.execute();
			
		} catch (DocumentExportException e) {
			fail("Happened a the problem in the document export. Exception: " + e.getMessage());
		} catch (SolrIndexException e) {
			fail("Happened a the problem in the read of index. Exception: " + e.getMessage());
		}		
	}
	

}

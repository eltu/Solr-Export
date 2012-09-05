package br.com.craic.solr.export.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.craic.solr.export.bean.FieldBean;
import br.com.craic.solr.export.config.ConfigProperties;
import br.com.craic.solr.export.exception.ConfigException;
import br.com.craic.solr.export.exception.SolrIndexException;
import br.com.craic.solr.export.index.SolrRead;

public class SolrReadTest {	
	private SolrRead index = null;
	private Map<String, List<FieldBean>> mapDocuments = null;
	
	@BeforeClass
	public static void loadProperties() {
		try {
			ConfigProperties.load("config/solrexport.properties");
		} catch (ConfigException e) {
			fail("Happened a the problem to load the file. Exception: " + e.getMessage());
		}
	}
	
	@Test(expected=SolrIndexException.class)
	public void openIndexTest() throws SolrIndexException {
		this.index = new SolrRead(ConfigProperties.properties.getProperty("solr.index.path") + "/test");
		index.execute();
	}

	@Test
	public void readIndexTest() {		
		this.readIndex();
		
		assertTrue("", mapDocuments.size() > 0);
	}
	
	@Test
	public void printIndexTest() {
		this.readIndex();
		
		assertTrue("", this.mapDocuments != null && this.mapDocuments.size() > 0);
		
		for (String key : mapDocuments.keySet()) {
			System.out.println(key);
		}
				
	}
	
	private void readIndex() {
		try {
			
			this.index = new SolrRead(ConfigProperties.properties.getProperty("solr.index.path"));
			this.index.execute();
			this.mapDocuments = this.index.getDocuments();
			
		} catch (SolrIndexException e) {
			fail("Happened a the problem in the read of index. Exception: " + e.getMessage());
		}
	}

}

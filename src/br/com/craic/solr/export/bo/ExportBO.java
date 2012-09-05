package br.com.craic.solr.export.bo;

import java.util.List;
import java.util.Map;

import br.com.craic.solr.export.bean.FieldBean;
import br.com.craic.solr.export.config.ConfigProperties;
import br.com.craic.solr.export.exception.DocumentExportException;
import br.com.craic.solr.export.exception.SolrIndexException;
import br.com.craic.solr.export.index.DocumentExport;
import br.com.craic.solr.export.index.SolrRead;

public class ExportBO {	
	public Integer execute() throws DocumentExportException, SolrIndexException  {
		Integer docNum = 0;
		
		SolrRead read = new SolrRead(ConfigProperties.properties.getProperty("solr.index.path"));
		Map<String, List<FieldBean>> documents = null;
		DocumentExport export = new DocumentExport();
		
		read.execute();
		documents = read.getDocuments();
		
		for(List<FieldBean> fields : documents.values()) {
			export.documentWrite(export.toXml(fields));
			
			docNum++;
		}
		
		return docNum;
	}
}

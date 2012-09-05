package br.com.craic.solr.export.index;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.ScoreDoc;

import br.com.craic.solr.export.bean.DocumentBean;
import br.com.craic.solr.export.bean.FieldBean;
import br.com.craic.solr.export.exception.SolrIndexException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

public class SolrRead extends SolrIndex {
	
	private Map<String, List<FieldBean>> documents;
	private static Logger logger = Logger.getLogger(SolrRead.class);
	
	public SolrRead(String index) {
		super(index);
	}

	public void execute() throws SolrIndexException {
		super.execute();
			
		this.setDocuments(this.read());
		
		logger.info(String.format("Total of docs to export: %s", this.getDocuments().size()));
	}
	
	private Map<String, List<FieldBean>> read() throws SolrIndexException {
		Map<String, List<FieldBean>> mapDocuments = new Hashtable<String, List<FieldBean>>();
		ScoreDoc[] hits = null;
		Integer docId = null;
				
		hits = super.getHits();
		
		for (int i=0; i < hits.length; i++) {
			docId = hits[i].doc;
			
			try {
			
				Document document = super.getIndexSearcher().doc(docId);
				DocumentBean documentBean = this.toDocument(document);
				
				mapDocuments.put(documentBean.getId(), documentBean.getFields());
				
			} catch (CorruptIndexException e) {
				throw new SolrIndexException("Happened a problem to read index.", e);
			} catch (IOException e) {
				throw new SolrIndexException("Path not found.", e);
			}
		}
		
		super.close();
				
		return mapDocuments;
	}
	
	private DocumentBean toDocument(Document doc) {
		DocumentBean documentBean = new DocumentBean();
		FieldBean fieldBean = null;
		List<FieldBean> listFieldBean = new ArrayList<FieldBean>();
		
		for (Fieldable field : doc.getFields()) {
			fieldBean = new FieldBean();

			fieldBean.setName(field.name());
			fieldBean.setValue(this.toDate(field.stringValue()));
			
			listFieldBean.add(fieldBean);
		}
		
		documentBean.setId(doc.getValues("id")[0]);
		documentBean.setFields(listFieldBean);
  
		return documentBean;
	}
	
	private void setDocuments(Map<String, List<FieldBean>> documents) {
		this.documents = documents;		
	}
	
	public Map<String, List<FieldBean>> getDocuments() {
		return this.documents;
	}
	
	private String toDate(String value) {
		String df = "";
		Integer year = 0;
		
		try {
			long longDate = Long.parseLong(value);
			Date dateConvert = new Date(longDate);
			
			df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(dateConvert);
			year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dateConvert));
			
		} catch (NumberFormatException e) {
		}
		
		return (year > 1969) ? df : value;
	}
}

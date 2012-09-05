package br.com.craic.solr.export.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import br.com.craic.solr.export.exception.SolrIndexException;

public class SolrIndex implements IIndexer {
	private String path;
	private Directory index;
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	private TopScoreDocCollector collector;
	private Query query;
	private ScoreDoc[] hits;

	public SolrIndex() {
	}
	
	public SolrIndex(String path) {
		this.path = path;
	}
	
	protected String getPath() {
		return path;
	}

	protected Directory getIndex() {
		return index;
	}

	protected IndexReader getIndexReader() {
		return indexReader;
	}

	protected TopScoreDocCollector getCollector() {
		return collector;
	}

	protected Query getQuery() {
		return query;
	}

	protected ScoreDoc[] getHits() {
		return hits;
	}
	
	protected void setOpen() throws IOException {
		this.index = FSDirectory.open(new File(this.path));
	}
	
	protected void setSearcher() {
		this.indexSearcher = new IndexSearcher(this.indexReader);
	}
	
	protected void setReader() throws CorruptIndexException, IOException {
		this.indexReader = IndexReader.open(this.index);
	}
	
	protected void setCollector(Integer numDocs) {
		this.collector = TopScoreDocCollector.create(numDocs, true);
	}
	
	protected void setQuery(Query query) {
		this.query = query;
	}
		
	protected void setScore() {
		this.hits = collector.topDocs().scoreDocs;
	}

	protected IndexSearcher getIndexSearcher() {
		return this.indexSearcher;
	}

	@Override
	public void execute() throws SolrIndexException {
		try {
			this.setOpen();
			this.setReader();
			this.setSearcher();
			this.setQuery(new MatchAllDocsQuery());
			this.setCollector(this.getNumDocs());
			
			this.getIndexSearcher().search(this.getQuery(), this.getCollector());
			
			this.setScore();
			
		} catch (CorruptIndexException e) {
			throw new SolrIndexException("Happened a problem to read index.", e);
		} catch (IOException e) {
			throw new SolrIndexException("Path not found.", e);
		}
		
	}
	
	protected void close() throws SolrIndexException {
		try {
			
			this.getIndexSearcher().close();
			
		} catch (IOException e) {
			throw new SolrIndexException("Path not found.", e);
		}
	}
	
	protected Integer getNumDocs() {
		return this.getIndexReader().numDocs();
	}

}

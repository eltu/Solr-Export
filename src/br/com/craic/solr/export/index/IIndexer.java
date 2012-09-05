package br.com.craic.solr.export.index;


import br.com.craic.solr.export.exception.SolrIndexException;

public interface IIndexer {
	public void execute() throws SolrIndexException;
}

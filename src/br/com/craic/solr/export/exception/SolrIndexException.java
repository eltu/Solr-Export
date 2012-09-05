package br.com.craic.solr.export.exception;

public class SolrIndexException extends Exception {
	private static final long serialVersionUID = 8728904795644129687L;

	public SolrIndexException(String message, Throwable e) {
		super(message, e);
	}
}

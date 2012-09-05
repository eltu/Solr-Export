package br.com.craic.solr.export.exception;

public class ConfigException extends Exception {
	
	private static final long serialVersionUID = 5276239426807982692L;

	public ConfigException(String message, Throwable e) {
		super(message, e);
	}
}

package br.com.craic.solr.export.bean;

import java.util.List;

public class DocumentBean {
	private String id;
	private List<FieldBean> fields;
	
	public String getId() {
		return id;
	}
			
	public void setId(String id) {
		this.id = id;
	}
	
	public List<FieldBean> getFields() {
		return fields;
	}
	
	public void setFields(List<FieldBean> fields) {
		this.fields = fields;
	}
	
}

package br.com.craic.solr.export.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import br.com.craic.solr.export.bean.FieldBean;
import br.com.craic.solr.export.exception.DocumentExportException;
import br.com.craic.solr.export.index.DocumentExport;

public class DocumentExportTest {
	private static List<FieldBean> listField = null;
	
	@BeforeClass
	public static void load() {
		listField = new ArrayList<FieldBean>();
		FieldBean field = new FieldBean();
		field.setName("id");
		field.setValue("1234");
		
		listField.add(field);
		
		field = new FieldBean();
		field.setName("title");
		field.setValue("Bla bla bla bla");
		
		listField.add(field);
		
		field = new FieldBean();
		field.setName("body");
		field.setValue("bla bla bla blab bla bla");
		
		listField.add(field);
	}
	
	@Test
	public void toXmlTest() {
		DocumentExport export = new DocumentExport();
		try {
			@SuppressWarnings("static-access")
			Document document = export.toXml(this.listField);
			
			export.documentWrite(document);
		} catch (DocumentExportException e) {
			fail("Happened a the problem in the document export. Exception: " + e.getMessage());
		}
	}

}

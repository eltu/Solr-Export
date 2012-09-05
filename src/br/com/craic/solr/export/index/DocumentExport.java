package br.com.craic.solr.export.index;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.com.craic.solr.export.bean.FieldBean;
import br.com.craic.solr.export.config.ConfigProperties;
import br.com.craic.solr.export.exception.DocumentExportException;
import br.com.craic.solr.export.util.Util;

public class DocumentExport {
	
	private static Logger logger = Logger.getLogger(DocumentExport.class);
	
	public Document toXml(List<FieldBean> listField) throws DocumentExportException {		
		DocumentBuilderFactory 	docFactory = null;
		DocumentBuilder 		docBuilder = null;
		
		try {
			
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			throw new DocumentExportException("Happened a problem to make the document parse.", e);
		}
 
		// Element <add/>
		Document doc = docBuilder.newDocument();
		Element add = doc.createElement("add");
		doc.appendChild(add);
 
		// Element <doc/>
		Element docElement = doc.createElement("doc");
		add.appendChild(docElement);
 
		for (FieldBean field : listField) {
			Element fieldElement = doc.createElement("field");
			docElement.appendChild(fieldElement);
					
			//Add the attibute name in </field>
			Attr attName = doc.createAttribute("name");
			attName.setValue(field.getName());
			fieldElement.setAttributeNode(attName);
			fieldElement.appendChild(doc.createTextNode(field.getValue()));
		}
		
		return doc;
		 
	}

	public void documentWrite(Document document) throws DocumentExportException {
		TransformerFactory 	transformerFactory 	= null;
		Transformer 		transformer 		= null;
		DOMSource 			source 				= null;
		StreamResult 		result 				= null;
		String				fileName			= null;
		
		try {
			fileName			= this.getFileNameHashCode(asXmlString(document));			
			transformerFactory 	= TransformerFactory.newInstance();
			transformer 		= transformerFactory.newTransformer();
			source 				= new DOMSource(document);
			result 				= new StreamResult(new File(ConfigProperties.properties.getProperty("solr.export.path") + fileName));
			
			transformer.transform(source, result);
			
			logger.info(String.format("File %s exported.", fileName));
			
		} catch (TransformerConfigurationException e) {
			throw new DocumentExportException("Happened a problem to create the instance for TransformerFactory.", e);
		} catch (TransformerException e) {
			throw new DocumentExportException("Happened a problem to create the instance for TransformerFactory.", e);
		}
	}
	
	private String getFileNameHashCode(String xml) {
		return Util.MD5(xml) + ".xml";
	}
	
	private String asXmlString(Document document) throws DocumentExportException {
		TransformerFactory 	transformerFactory 	= null;
		Transformer 		transformer 		= null;
		DOMSource 			source 				= null;
		StreamResult 		streamResult 		= null;
		StringWriter 		writer 				= null; 
		String 				result				= null;
		
		try {
			transformerFactory 	= TransformerFactory.newInstance();
			transformer 		= transformerFactory.newTransformer();
			source 				= new DOMSource(document);
			writer 				= new StringWriter();
			streamResult 		= new StreamResult(writer);
			
			transformer.transform(source, streamResult);
			
			result = writer.toString();	
			
		} catch (TransformerConfigurationException e) {
			throw new DocumentExportException("Happened a problem to create the instance for TransformerFactory.", e);
		} catch (TransformerException e) {
			throw new DocumentExportException("Happened a problem to create the instance for TransformerFactory.", e);
		}
		
		return result;
	}	
}

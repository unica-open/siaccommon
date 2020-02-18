/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Utiliti per il marshall e unmarshall di oggetti JAXB annotati con \@XmlType
 * 
 * @author Domenico Lisi, AR
 * 
 */
public class JAXBUtility {

	private JAXBUtility() {
		// Prevent instantiation
	}
	public static String marshall(Object obj) {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXB.marshal(obj, baos);
		return baos.toString();

	}

	public static <T> T unmarshall(String xml, Class<T> clazz) {
		if (xml == null) {
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		return JAXB.unmarshal(bais, clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T clone(T obj) {
		if(obj == null) {
			// Null-safe
			return null;
		}
		String xml = marshall(obj);
		return (T) unmarshall(xml, obj.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(String xml, String xsd,
			ValidationEventCollector validationEventCollector, Class<T> cls) throws JAXBException,
			SAXException {
		JAXBContext context = JAXBContext.newInstance(cls);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		if (xsd != null) {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = sf.newSchema(cls.getResource(xsd));
			unmarshaller.setSchema(schema);

			unmarshaller.setEventHandler(validationEventCollector);
		}
		
		return (T) unmarshaller.unmarshal(new StringReader(xml));
	}

}

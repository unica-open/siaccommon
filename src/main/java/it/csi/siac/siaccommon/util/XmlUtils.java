/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XmlUtils {

	private XmlUtils() {
	}
	
	public static void validateXmlWithXsd(URL xsdLoc, byte[] xml) throws SAXException, IOException {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Schema schema = sf.newSchema(xsdLoc);
		
		Validator v = schema.newValidator();
		
		v.validate(new StreamSource(new ByteArrayInputStream(xml)));
	}
}

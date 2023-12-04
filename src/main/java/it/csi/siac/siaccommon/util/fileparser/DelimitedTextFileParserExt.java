/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.fileparser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.collections.Predicate;


@Deprecated
public class DelimitedTextFileParserExt<T>
{
	private LineIterator lineIterator;
	private String delimiter;
	protected Iterator<T> iterator;

	public DelimitedTextFileParserExt(InputStream in, String delimiter) {
		this.lineIterator = IOUtils.lineIterator(new InputStreamReader(in));
		this.delimiter = delimiter;
	}

	public DelimitedTextFileParserExt(byte[] fileBytes, String delimiter) {
		this(new ByteArrayInputStream(fileBytes), delimiter);
	}
	
	public DelimitedTextFileParserExt(File file, String delimiter) throws FileNotFoundException {
		this(new FileInputStream(file), delimiter);
	}

	public void setPropertyMappingStrategy(Class<T> cls, String... properties) {
		iterator = new PropertyMapperIterator(cls, properties);
	}

	public void setObjectMappingStrategy(LineMapper<T> mapper) {
		iterator = new ObjectMapperIterator(mapper);
	}
	
	public boolean hasLines() {
		return lineIterator != null && lineIterator.hasNext();
	}
	
	public void skipLine() {
		if(hasLines()) {
			lineIterator.next();
		}
	}

	public T parseNextLine() {
		return hasLines() ? iterator.next() : null;
	}

	public void parse(Predicate<T> predicate) {
		while (hasLines()) {
			predicate.apply(parseNextLine());
		}
	}

	@Deprecated
	public Iterator<T> iterator() {
		return iterator;
	}
	
	public Integer getLineNumber() {
		return iterator != null ? ((BaseMapperIterator) iterator).lineNumber : null;
	}

	public List<String> getElementMessages() {
		return iterator != null ? ((BaseMapperIterator) iterator).messages : null;
	}

	public List<String> getElementErrors() {
		return iterator != null ? ((BaseMapperIterator) iterator).errors : null;
	}

	abstract class BaseMapperIterator implements Iterator<T> {
		
		protected Integer lineNumber = 0;
		protected List<String> messages;
		protected List<String> errors;
		
		@Override
		public boolean hasNext()
		{
			return lineIterator.hasNext();
		}

		@Override
		public T next()
		{
			lineNumber++;
			messages = new ArrayList<String>();
			errors = new ArrayList<String>();
					
			String line = lineIterator.next();

			String[] values = StringUtils.splitPreserveAllTokens(line, delimiter);

			return mapValues(values);
		}

		protected abstract T mapValues(String[] values);
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	class ObjectMapperIterator extends BaseMapperIterator {

		private LineMapper<T> mapper;
		
		public ObjectMapperIterator(LineMapper<T> mapper) {
			this.mapper = mapper;
		}

		@Override
		protected T mapValues(String[] values) {
			T obj = mapper.mapValues(values);
			
			messages.addAll(mapper.getMessages());
			errors.addAll(mapper.getErrors());
			
			return obj;
		}
	}
	
	class PropertyMapperIterator extends BaseMapperIterator {
		
		private Class<T> cls;
		private String[] properties;
		
		public PropertyMapperIterator(Class<T> cls, String... properties) {
			this.cls = cls;
			this.properties = properties;
		}

		@Override
		protected T mapValues(String[] values) {
			if (values.length != properties.length) {
				errors.add(String.format("Numero campi non coerente: %d", values.length));
				
				return null;
			}

			try {
				T obj = cls.newInstance();

				for (int i = 0; i < properties.length; i++) {
					if (StringUtils.isNotEmpty(properties[i]))
						PropertyUtils.setNestedProperty(obj, properties[i], values[i]);
				}

				return obj;
			}
			catch (Exception e) {
				errors.add(e.getMessage());
			}
			
			return null;
		}
	}

	public interface LineMapper<T> {
		T mapValues(String[] values);
		List<String> getMessages();
		List<String> getErrors();
	}

}


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

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import it.csi.siac.siaccommon.util.collections.Predicate;

public abstract class TextFileParser<T>
{
	protected LineIterator lineIterator;
	protected LineMapper<T> lineMapper;
	protected Integer lineNumber = 0;

	
	public TextFileParser(InputStream in) {
		lineIterator = IOUtils.lineIterator(new InputStreamReader(in));
	}

	public TextFileParser(byte[] fileBytes) {
		this(new ByteArrayInputStream(fileBytes));
	}
	
	public TextFileParser(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}
	
	public void setLineMapper(LineMapper<T> lineMapper) {
		this.lineMapper = lineMapper;
	}
	
	public boolean hasLines() {
		return lineIterator != null && lineIterator.hasNext();
	}
	
	public void skipLine() {
		if(hasLines()) {
			readNextLine();
		}
	}

	public void parse(Predicate<T> predicate) {
		while (hasLines()) {
			predicate.apply(lineMapper.mapValues(parseLine(readNextLine())));
		}
	}

	protected abstract String[] parseLine(String line);

	protected String readNextLine() {
		lineNumber++;
		return lineIterator.next();
	}

	public Integer getLineNumber() {
		return lineNumber;
	}
	
}


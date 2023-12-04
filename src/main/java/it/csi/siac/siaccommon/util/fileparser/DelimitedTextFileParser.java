/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.fileparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;



public class DelimitedTextFileParser<T> extends TextFileParser<T>
{
	private String delimiter;

	public DelimitedTextFileParser(InputStream in, String delimiter) {
		super(in);
		this.delimiter = delimiter;
	}

	public DelimitedTextFileParser(File file, String delimiter) throws FileNotFoundException {
		this(new FileInputStream(file), delimiter);
	}

	@Override
	protected String[] parseLine(String line) {
		return StringUtils.splitPreserveAllTokens(line, delimiter);
	}
}


/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.fileparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CsvFileParser<T> extends DelimitedTextFileParser<T>
{
	public static final String CSV_DELIMITER = ";";

	public CsvFileParser(InputStream in) {
		super(in, CSV_DELIMITER);
	}

	public CsvFileParser(File file) throws FileNotFoundException {
		super(file, CSV_DELIMITER);
	}
}


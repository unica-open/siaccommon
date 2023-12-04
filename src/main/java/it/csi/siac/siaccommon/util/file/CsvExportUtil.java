/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommon.util.fileparser.CsvFileParser;

public final class CsvExportUtil {
		
	private StringBuilder sb = new StringBuilder();
	private static final String SEP = CsvFileParser.CSV_DELIMITER;

	public void appendTo(InputStream fileInputStream) throws IOException {
		for (String line : IOUtils.readLines(fileInputStream)) {
			sb.append(line).append("\n");
		}
	}

	public void addLine(String...line) {
		sb.append(formatCsvLine(line));
	}

	public <S> void addLines(List<S> list, Function<S, String[]> toCsvLineMapper) {
		CollectionUtil.apply(list, new Predicate<S>() {

			@Override
			public void apply(S source) {
				addLine(toCsvLineMapper.map(source));
			}
		});
	}

	public void setHeader(String...header) throws IllegalAccessException {
		if (sb.length() > 0) {
			throw new IllegalAccessException("Header can be set with empty content");
		}
		
		sb.append(formatCsvLine(header));
	}
	
	private String formatCsvLine(String...fields) {
		
		if (fields == null || fields.length == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();

		for (String s : fields) {
			
			if (s == null) {
				sb.append("\"\"").append(SEP);
				continue;
			}
						
			if (StringUtils.isEmpty(s)) {
				sb.append("\"\"").append(SEP);
				continue;
			}
			
			if (s.contains("\"") || s.contains("'") || s.contains(SEP) || s.startsWith("\n") || s.startsWith(" ") || s.endsWith(" ") || s.startsWith("\t")  || s.startsWith("\t")) {
				sb.append(String.format("\"%s\"", s.replace("\"", "\"\""))).append(SEP);
				continue;
			}
			
			sb.append(s).append(SEP);
		}
		
		sb.deleteCharAt(sb.length()-1).append("\n");
		
		return sb.toString();
	}

	public byte[] export() throws UnsupportedEncodingException {
		return sb.toString().getBytes("utf-8");
	}

}

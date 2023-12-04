/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @deprecated
 * @see it.csi.siac.siaccommon.util.date.DateUtil
 */
@Deprecated
public class DateConverter {
	private static final String DATE_PATTERN = "dd/MM/yyyy";
	
	private static final ThreadLocal<Map<String, DateFormat>> TL_DATE_FORMATS = new ThreadLocal<Map<String,DateFormat>>() {
		@Override
		protected Map<String,DateFormat> initialValue() {
			return new HashMap<String, DateFormat>();
		}
	};
	
	private DateConverter() {}

	public static Date convertFromString(String value) {
		return convertFromString(value, DATE_PATTERN);
	}
	
	public static Date convertFromString(String value, String pattern) {
		if (value == null) {
			return null;
		}
		
		Date result = null;
		
		try {
			result = getDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Conversione in java.util.Date fallita per il valore (" + value + ")", e);
		}

		Date dataDel1900 = new GregorianCalendar(1900, Calendar.DECEMBER, 31).getTime();
		
		if (!result.after(dataDel1900)) {
			throw new IllegalArgumentException("Conversione in java.util.Date fallita per il valore (" + value + ")");
		}

		return result;
	}

	public static String convertToString(Date value) {
		try {
			return getDateFormat(DATE_PATTERN).format(value);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Retrieves the date format
	 * @param pattern the pattern for which to retrieve the date format
	 */
	private static DateFormat getDateFormat(String pattern) {
		Map<String, DateFormat> dfs = TL_DATE_FORMATS.get();
		DateFormat df = dfs.get(pattern);
		if(df == null) {
			df = new SimpleDateFormat(pattern, Locale.ITALY);
			dfs.put(pattern, df);
		}
		return df;
	}
	

}

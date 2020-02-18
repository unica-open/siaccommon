/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class NumberUtils {
	private static final String IMPORTO_PATTERN = "#,##0.00";
	private static final String DECIMAL_PATTERN = "0.00";
	
	private NumberUtils() {
		// Prevent instantiation
	}

	public static String toImporto(BigDecimal bd) {
		return formatBigDecimal(bd, IMPORTO_PATTERN);
	}

	public static String toDecimal(BigDecimal bd) {
		return formatBigDecimal(bd, DECIMAL_PATTERN);
	}

	public static String toImporto(Double d) {
		return formatDouble(d, IMPORTO_PATTERN);
	}

	public static String toDecimal(Double d) {
		return formatDouble(d, DECIMAL_PATTERN);
	}

	public static BigDecimal decimalToBigDecimal(String val) {
		if (StringUtils.isEmpty(val))
			return null;

		return new BigDecimal(StringUtils.replace(val, ",", "."));
	}

	public static BigDecimal importoToBigDecimal(String importo) {
		if (StringUtils.isEmpty(importo))
			return null;

		return decimalToBigDecimal(StringUtils.replace(importo, ".", ""));
	}

	public static Double importoToDouble(String importo) throws ParseException {
		return parse(importo, IMPORTO_PATTERN);
	}

	public static Double decimalToDouble(String importo) throws ParseException {
		return parse(importo, DECIMAL_PATTERN);
	}

	private static String formatBigDecimal(BigDecimal bd, String pattern) {
		if (bd == null)
			return null;

		return getDecimalFormat(pattern).format(bd);
	}

	private static String formatDouble(Double d, String pattern) {
		if (d == null)
			return null;

		return getDecimalFormat(pattern).format(d);
	}

	private static Double parse(String s, String pattern) throws ParseException {
		if (s == null)
			return null;

		return getDecimalFormat(pattern).parse(s).doubleValue();
	}

	private static DecimalFormat getDecimalFormat(String pattern) {
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.ITALY);
		decimalFormat.applyPattern(pattern);

		return decimalFormat;
	}
}

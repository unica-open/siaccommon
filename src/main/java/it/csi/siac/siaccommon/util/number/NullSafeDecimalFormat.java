/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.number;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Null-safe Decimal formatter
 * @author Alessandro Marchino
 * @version 1.0.0 - 06/05/2021
 *
 */
public class NullSafeDecimalFormat extends NumberFormat {
	
	/** For serialization */
	private static final long serialVersionUID = 7326317761657466175L;

	/** Foatter interno di utilit&agrave; */
	private final DecimalFormat inner;
	
	/**
	 * Costruttore con l'indicazione del numero di decimali da usare
	 * @param decimalPlaces il numero dei decimali
	 */
	public NullSafeDecimalFormat(int decimalPlaces) {
		this(decimalPlaces, true, Locale.ITALY);
	}
	/**
	 * Costruttore con l'indicazione del numero di decimali da usare, e se parsificare in BigDecimal
	 * @param decimalPlaces il numero dei decimali
	 * @param parseBigDecimal se parsificare in BigDecimal
	 */
	public NullSafeDecimalFormat(int decimalPlaces, boolean parseBigDecimal) {
		this(decimalPlaces, parseBigDecimal, Locale.ITALY);
	}
	/**
	 * Costruttore
	 * @param decimalPlaces il numero dei decimali
	 * @param parseBigDecimal se parsificare in BigDecimal
	 * @param locale il locale da usare per la formattazione/parsificazione
	 */
	public NullSafeDecimalFormat(int decimalPlaces, boolean parseBigDecimal, Locale locale) {
		inner = (DecimalFormat)NumberFormat.getInstance(locale);
		inner.setParseBigDecimal(parseBigDecimal);
		inner.setMinimumFractionDigits(decimalPlaces);
		inner.setMaximumFractionDigits(decimalPlaces);
	}
	
	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if(obj == null) {
			return toAppendTo;
		}
		return inner.format(obj, toAppendTo, pos);
	}

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		inner.format(pos);
		return inner.format(number, toAppendTo, pos);
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		return inner.format(number, toAppendTo, pos);
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		return inner.parse(source, parsePosition);
	}

}

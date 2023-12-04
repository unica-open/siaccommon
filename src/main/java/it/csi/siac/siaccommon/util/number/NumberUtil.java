/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Reductor;

public final class NumberUtil {
	
	private static final String IMPORTO_PATTERN_0 = "#,##0";
	private static final String IMPORTO_PATTERN_2 = getImportoPattern(IMPORTO_PATTERN_0, 2);
	private static final String DECIMAL_PATTERN_2 = getDecimalPattern(2);
	
	private NumberUtil() {
		// Prevent instantiation
	}

	public static boolean isNumeric(String string) {
		return string != null && string.matches("[-0-9.]+");
	}
	
	@Deprecated
	public static Integer safeParseStringToInteger(String string) { 
		return isNumeric(string) ? Integer.parseInt(string) : 0;
	}

	@Deprecated
	public static BigDecimal safeParseStringToBigDecimal(String string) { 
		return isNumeric(string) ? new BigDecimal(string) : BigDecimal.ZERO;
	}
	
	private static final String getImportoPattern(String pattern, int scale) {
		if (scale <= 0) {
			return pattern;
		}

		return new StringBuilder(pattern).append(".").append(StringUtils.repeat("0", scale)).toString();
	}

	private static final String getDecimalPattern(int scale) {
		if (scale == 0) {
			return "0";
		}

		return new StringBuilder("0.").append(StringUtils.repeat("0", scale)).toString();
	}

	public static String toImporto(BigDecimal bd, int scale) {
		return formatBigDecimal(bd, getImportoPattern(IMPORTO_PATTERN_0, scale));
	}

	public static String toImporto(BigDecimal bd) {
		return formatBigDecimal(bd, IMPORTO_PATTERN_2);
	}

	public static String toDecimal(BigDecimal bd, int scale) {
		return formatBigDecimal(bd, getDecimalPattern(scale));
	}

	public static String toDecimal(BigDecimal bd) {
		return formatBigDecimal(bd, DECIMAL_PATTERN_2);
	}

	public static String toImporto(Double d) {
		return formatDouble(d, IMPORTO_PATTERN_2);
	}

	public static String toDecimal(Double d, int scale) {
		return formatDouble(d, getDecimalPattern(scale));
	}

	public static String toDecimal(Double d) {
		return formatDouble(d, DECIMAL_PATTERN_2);
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

	public static BigDecimal toBigDecimal(String number) {
		try {
			return number == null ? null : new BigDecimal(number);
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	public static BigDecimal toBigDecimal(Integer integer) {
		return integer == null ? null : BigDecimal.valueOf(integer.intValue());
	}
	
	public static Double importoToDouble(String importo) throws ParseException {
		return parse(importo, IMPORTO_PATTERN_2);
	}

	public static Double decimalToDouble(String importo) throws ParseException {
		return parse(importo, DECIMAL_PATTERN_2);
	}

	public static Integer safeParseInt(String number) {
		try {
			return number == null ? null : Integer.parseInt(number);
		}
		catch (NumberFormatException e) {
			return null;
		}
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
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <Byte> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(Byte number) {
		return number != null && number.compareTo((byte) 0) > 0;
	}
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <Short> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(Short number) {
		return number != null && number.compareTo((short) 0) > 0;
	}
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <Integer> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(Integer number) {
		return number != null && number.compareTo(0) > 0;
	}
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <Long> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(Long number) {
		return number != null && number.compareTo((long) 0) > 0;
	}
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <Float> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(Float number) {
		return number != null && number.compareTo((float) 0) > 0;
	}
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <Double> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(Double number) {
		return number != null && number.compareTo((double) 0) > 0;
	}
	
	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <BigDecimal> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(BigDecimal number) {
		return number != null && number.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Util method to check if number is valid and greater than zero
	 * @param <BigInteger> number
	 * @return true if number is valid and greater than zero
	 */
	public static boolean isValidAndGreaterThanZero(BigInteger number) {
		return number != null && number.compareTo(BigInteger.ZERO) > 0;
	}

	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <Byte> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(Byte number) {
		return number != null && number.compareTo((byte) 0) != 0;
	}
	
	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <Short> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(Short number) {
		return number != null && number.compareTo((short) 0) != 0;
	}
	
	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <Integer> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(Integer number) {
		return number != null && number.compareTo(0) != 0;
	}
	
	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <Long> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(Long number) {
		return number != null && number.compareTo((long) 0) != 0;
	}

	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <Float> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(Float number) {
		return number != null && number.compareTo((float) 0) != 0;
	}

	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <Double> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(Double number) {
		return number != null && number.compareTo((double) 0) != 0;
	}

	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <BigDecimal> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(BigDecimal number) {
		return number != null && number.compareTo(BigDecimal.ZERO) != 0;
	}

	/**
	 * Util method to check if number is valid and not equal to zero
	 * @param <BigInteger> number
	 * @return true if number is valid and not equal to zero
	 */
	public static boolean isValidAndNotEqualToZero(BigInteger number) {
		return number != null && number.compareTo(BigInteger.ZERO) != 0;
	}

	// TODO never tested!!
	public static Integer sumInteger(Collection<Integer> collection) {
		return CollectionUtil.reduce(collection, new Reductor<Integer, Integer>() {

			@Override
			public Integer reduce(Integer accumulator, Integer currentValue, int index) {
				return accumulator + currentValue;
			}
		});
	}

}

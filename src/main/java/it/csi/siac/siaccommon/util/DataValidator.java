/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Classe di validazione per i dati. <br>
 * Definisce metodi statici per la validazione di tipi particolari di dati in
 * formato String, quali il range di un valore o la rappresentazione corretta di
 * un codice fiscale
 */
public final class DataValidator {

	/** Non permettere l'instanziane della classe */
	private DataValidator() {
		super();
	}

	/** Array per il calcolo della cifra di controllo per il codice fiscale */
	private static final int[] ARRAY_CODICE_FISCALE = new int[] { 1, 0, 5, 7, 9, 13, 15, 17, 19,
			21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
	/** Indice del carattere di controllo */
	private static final int CARATTERE_CONTROLLO = 15;
	/** Shift tra i valori numerici e i char */
	private static final int NUM_TO_CHAR = 65;
	/** Numero dei caratteri nell'alfabeto JAVA */
	private static final int CHARS_IN_ALPHABET = 26;

	/**
	 * Controlla che il valore inserito sia un numero all'interno di un dato
	 * range.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * @param min
	 *            il minimo del range
	 * @param max
	 *            il massimo del range
	 * 
	 * @return <code>true</code> se il valore &egrave; interno al range;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidRange(String value, Double min, Double max) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		double d;
		try {
			d = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}

		if (min == null) {
			return d <= max.doubleValue();
		}

		if (max == null) {
			return d >= min.doubleValue();
		}

		return d >= min.doubleValue() && d <= max.doubleValue();
	}

	/**
	 * Controlla che il valore inserito sia un valore intero valido.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidInteger(String value) {
		return StringUtils.isBlank(value) || isValidMask(value, "^\\d+$");
	}

	public static boolean isValidInteger(Integer value) {
		return value == null || isValidInteger(String.valueOf(value));
	}

	/**
	 * Controlla che il valore inserito sia un IBAN valido per l'Italia.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidIbanIT(String value) {
		return StringUtils.isBlank(value)
				|| isValidMask(value.toUpperCase(Locale.ITALIAN), "^IT\\d\\d[A-Z][0-9]{10}[A-Z0-9]{12}$");
	}

	/**
	 * Controlla che il valore inserito sia un codice fiscale valido.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidCodiceFiscale(String value) {
		return StringUtils.isBlank(value)
				|| isValidMask(value,
						"^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$", Pattern.CASE_INSENSITIVE)
				&& isValidControlCharCodiceFiscale(value);
	}

	/**
	 * Controlla che il carattere di controllo del codice fiscale sia valido
	 * 
	 * @param value
	 *            il valore da analizzare
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	private static boolean isValidControlCharCodiceFiscale(String codiceFiscale) {
		return codiceFiscale.charAt(CARATTERE_CONTROLLO) == calcControlCharCodiceFiscale(StringUtils
				.substring(codiceFiscale, 0, CARATTERE_CONTROLLO));
	}

	/**
	 * Controlla che il valore inserito sia una partita IVA valida.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidPartitaIVA(String value) {
		return StringUtils.isBlank(value) || isValidMask(value, "^\\d{11}$");
	}

	/**
	 * Controlla che il valore inserito sia un anno valido.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidAnno(String value) {
		return StringUtils.isBlank(value) || isValidMask(value, "^\\d{4}$");
	}

	/**
	 * Controlla che il valore inserito sia corrispondente alla maschera
	 * impostata.
	 * 
	 * @param value
	 *            il valore da analizzare
	 * @param mask
	 *            la maschera con cui effettuare il controllo
	 * 
	 * @return <code>true</code> se il valore &egrave; valido;
	 *         <code>false</code> altrimenti
	 */
	public static boolean isValidMask(String value, String mask, int maskFlags) {
		// regexp:
		// http://articles.sitepoint.com/article/java-regex-api-explained
		boolean result = true;

		if (StringUtils.isNotEmpty(value)) {
			Pattern pattern = Pattern.compile(mask, maskFlags);
			Matcher matcher = pattern.matcher(value);
			result = matcher.find();
		}
		return result;
	}
	
	public static boolean isValidMask(String value, String mask) {
		return isValidMask(value, mask, 0);
	}

	/**
	 * Calcola il carattere di controllo del codice fiscale.
	 * 
	 * @param string
	 *            il codice fiscale senza l'ultimo carattere
	 * 
	 * @return il carattere di controllo
	 */
	public static char calcControlCharCodiceFiscale(String string) {
		int sum = 0;

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			int x = Character.isDigit(c) ? Character.getNumericValue(c) : c - NUM_TO_CHAR;
			sum += ((i + 1) % 2 == 0 ? x : ARRAY_CODICE_FISCALE[x]);
		}
		return (char) ((sum % CHARS_IN_ALPHABET) + NUM_TO_CHAR);
	}

	/**
	 * Controlla che il valore inserito sia una data valida nel formato
	 * specificato.
	 * 
	 * @param value
	 *            la data da analizzare
	 * @param format
	 *            il formato della data
	 * 
	 * @return <code>true</code> se la data &egrave; valida; <code>false</code>
	 *         altrimenti
	 */
	public static boolean isValidDate(String value, String format) {
		if (StringUtils.isNotEmpty(value)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ITALY);
			sdf.setLenient(false);

			try {
				sdf.parse(value);
			} catch (ParseException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Controlla che il valore inserito sia di lunghezza di almeno len caratteri
	 * @param value
	 *            la stringa da analizzare
	 * @param len
	 *            la lunghezza minima
	 * 
	 * @return <code>true</code> se la data &egrave; valida; <code>false</code>
	 *         altrimenti
	 */
	public static boolean isValidMinLength(String value, int len) {
		if (StringUtils.isNotEmpty(value)) {
			return value.length() >= len;
		}

		return true;
	}

	
	
}

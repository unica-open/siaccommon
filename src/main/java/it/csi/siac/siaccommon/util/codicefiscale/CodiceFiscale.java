/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.codicefiscale;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.Sesso;
import it.csi.siac.siaccommon.util.date.DateConverter;

public class CodiceFiscale {
	
	private static final Map<String, String> MESE_MAP;
	private static final Map<String, String> REVERSE_MESE_MAP;
	private static final int[] OMOCODIA_POS = new int[] { 6, 7, 9, 10, 12, 13, 14 };
	private static final String OMOCODIA_MAPPING = "LMNPQRSTUV";
	
	private String cognome;
	private String nome;
	private Sesso sesso;
	private Date dataNascita;
	private String giornoNascita;
	private String meseNascita;
	private String annoNascita;
	private String codiceIstatComune;

	private String codiceStandard;

	private String codiceOmocodo;

	static
	{
		Map<String, String> map = new HashMap<String, String>();

		map.put("1", "A");
		map.put("2", "B");
		map.put("3", "C");
		map.put("4", "D");
		map.put("5", "E");
		map.put("6", "H");
		map.put("7", "L");
		map.put("8", "M");
		map.put("9", "P");
		map.put("10", "R");
		map.put("11", "S");
		map.put("12", "T");

		MESE_MAP = Collections.unmodifiableMap(map);
		REVERSE_MESE_MAP = CoreUtil.reverseMap(MESE_MAP);
	}

	public static CodiceFiscale generaCodiceFiscale(String cognome, String nome, Sesso sesso, Date dataNascita,
			String codiceIstatComune) {
		CodiceFiscale cf = new CodiceFiscale(cognome, nome, sesso, dataNascita, codiceIstatComune);

		cf.generaCodice();

		return cf;
	}

	public static CodiceFiscale getCodiceFiscale(String codiceFiscale) {
		CodiceFiscale cf = new CodiceFiscale(codiceFiscale);

		return cf;
	}

	public static boolean verificaCodiceFiscale(String codiceFiscale) {
		return verificaFormale(codiceFiscale);
	}

	private static boolean verificaFormale(String codiceFiscale) {
		return codiceFiscale.matches("^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-EHLMPR-T][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$");
	}

	@Override
	public String toString() {
		return codiceOmocodo == null ? codiceStandard : codiceOmocodo;
	}

	public boolean isOmocodo() {
		return codiceOmocodo != null;
	}

	private void checkOmocodia() {
		StringBuilder sb = new StringBuilder(codiceStandard.toUpperCase(Locale.ITALIAN));

		for (int i : OMOCODIA_POS) {
			char c = sb.charAt(i);

			if (c >= 'A' && c <= 'Z') {
				initCodiceStandardFromOmocodo();

				return;
			}
		}
	}

	private void initCodiceStandardFromOmocodo() {
		codiceOmocodo = codiceStandard;

		StringBuilder cf = new StringBuilder(codiceStandard);

		for (int i : OMOCODIA_POS) {
			int idx = OMOCODIA_MAPPING.indexOf(cf.charAt(i));

			if (idx != -1)
				cf.setCharAt(i, (char) ('0' + idx));
		}

		cf.setCharAt(15, calcolaCarattereControllo(cf.toString()));

		codiceStandard = cf.toString();
	}

	public boolean verificaFormale() {
		return verificaFormale(codiceStandard);
	}

	private CodiceFiscale() {
	}

	private CodiceFiscale(String cognome, String nome, Sesso sesso, Date dataNascita, String codiceIstatComune) {
		this();
		this.cognome = clean(cognome);
		this.nome = clean(nome);
		this.codiceIstatComune = clean(codiceIstatComune);
		this.sesso = sesso;

		initInfoNascita(dataNascita);
	}

	public CodiceFiscale(String codiceFiscale) {
		this();

		if (codiceFiscale == null)
			throw new IllegalArgumentException("Codice fiscale mancante");

		if (codiceFiscale.length() != 16)
			throw new IllegalArgumentException("Lunghezza codice fiscale non corretta");

		if (!verificaFormale(codiceFiscale))
			throw new IllegalArgumentException("Codice fiscale non corretto");

		codiceStandard = codiceFiscale;

		checkOmocodia();

		initData();
	}

	private void initInfoNascita(Date data) {
		this.dataNascita = data;

		Calendar cal = Calendar.getInstance(Locale.ITALY);

		cal.setTime(data);

		giornoNascita = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		meseNascita = String.valueOf(cal.get(Calendar.MONTH) + 1);
		annoNascita = String.valueOf(cal.get(Calendar.YEAR));
	}

	private void initData() {
		decodificaInfoNascita();
		decodificaCodiceIstatComune();
	}

	private void decodificaInfoNascita() {
		decodificaAnnoNascita();
		decodificaMeseNascita();
		decodificaGiornoNascitaESesso();

		dataNascita = DateConverter.convertFromString(String
				.format("%s/%s/%s", giornoNascita, meseNascita, annoNascita));
	}

	private void decodificaCodiceIstatComune() {
		codiceIstatComune = codiceStandard.substring(11, 15);
	}

	private void decodificaGiornoNascitaESesso() {
		giornoNascita = codiceStandard.substring(9, 11);

		if (Integer.parseInt(giornoNascita) < 40)
			sesso = Sesso.MASCHIO;
		else {
			sesso = Sesso.FEMMINA;
			giornoNascita = String.valueOf(Integer.parseInt(giornoNascita) - 40);
		}
	}

	private void decodificaMeseNascita() {
		meseNascita = REVERSE_MESE_MAP.get(String.valueOf(codiceStandard.charAt(8)));
	}

	private void decodificaAnnoNascita() {
		String tmp = codiceStandard.substring(6, 8);

		if (Calendar.getInstance(Locale.ITALY).get(Calendar.YEAR) % 100 > Integer.parseInt(tmp))
			annoNascita = "20" + tmp;
		else
			annoNascita = "19" + tmp;
	}

	private void generaCodice() {
		String codiceBase = String.format("%s%s%s%s%s%s", codificaCognome(), codificaNome(), codificaAnnoNascita(),
				codificaMeseNascita(), codificaGiornoNascita(), codiceIstatComune);

		codiceStandard = codiceBase + calcolaCarattereControllo(codiceBase);
	}
	
	private String clean(String string) {
		return string.replaceAll(" ", "").toUpperCase();
	}

	private String codificaCognome() {
		String s = codificaNomeCognome(cognome);

		if (s.length() > 3)
			return s.substring(0, 3);

		return s;
	}

	private String codificaNome() {
		String s = codificaNomeCognome(nome);

		if (s.length() > 3)
			return String.valueOf(s.charAt(0)) + s.charAt(2) + s.charAt(3);

		return s;
	}

	private String codificaNomeCognome(String str) {
		String consonanti = str.replaceAll("[AEIOU]", "");
		String vocali = str.replaceAll("[^AEIOU]", "");

		if (consonanti.length() < 3) {
			if (str.length() >= 3) {
				return aggiungiVocali(consonanti, vocali);
			}
			return StringUtils.rightPad(consonanti + vocali, 3, 'X');
		}

		return consonanti;

	}

	private String aggiungiVocali(String str, String vocali) {
		StringBuilder tmp = new StringBuilder(str);

		for(int index = 0; index < 3 - str.length(); index++) {
			tmp.append(vocali.charAt(index));
		}

		return tmp.toString();
	}
	
	private String codificaAnnoNascita() {
		return annoNascita.substring(2, 4);

	}

	private String codificaMeseNascita() {
		return MESE_MAP.get(meseNascita);
	}

	private String codificaGiornoNascita() {
		if(Sesso.MASCHIO.equals(sesso)) {
			return StringUtils.leftPad(giornoNascita, 2, '0');
		}
		if(Sesso.FEMMINA.equals(sesso)) {
			return String.valueOf(Integer.parseInt(giornoNascita) + 40);
		}
		throw new IllegalStateException();
	}

	private char calcolaCarattereControllo(String codiceBase) {
		int sum = 0;

		for (int i = 0; i < codiceBase.length(); i++) {
			char c = codiceBase.charAt(i);

			int x = Character.isDigit(c) ? Character.getNumericValue(c)
					: new String(new char[] { c }).getBytes()[0] - 65;

			sum += ((i + 1) % 2 == 0 ? x : new int[] { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17,
					19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 }[x]);
		}

		return (char) ((sum % 26) + 65);
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public String getCodiceIstatComune() {
		return codiceIstatComune;
	}

	public Sesso getSesso() {
		return sesso;
	}

}

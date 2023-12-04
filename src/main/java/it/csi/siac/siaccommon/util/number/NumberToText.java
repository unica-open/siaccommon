/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.number;

public final class NumberToText {
	
	private static final String[] UNO_DICIANNOVE = new String[] { "uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto",
			"nove", "dieci", "undici", "dodici", "tredici", "quattordici", "quindici",
			"sedici", "diciassette", "diciotto", "diciannove" };
	private static final String[] VENTI_NOVANTA = new String[] { "venti", "trenta", "quaranta", "cinquanta", "sessanta",
			"settanta", "ottanta", "novanta" };
	
	private NumberToText() {
		// Prevent instantiation
	}
	
	public static String spell(int number) {
		if (number == 0)
			return "zero";

		return innerSpell(number);
	}

	private static String innerSpell(int number) {
		if (number == 0)
			return "";
		
		if (number < 0)
			return "meno " + innerSpell(-number);

		if (number <= 19)
			return UNO_DICIANNOVE[number - 1];

		if (number <= 99)
			return innerSpell99(number);

		if (number <= 199)
			return "cento" + innerSpell(number % 100);

		if (number <= 999)
			return innerSpell999(number);

		if (number <= 1999)
			return "mille" + innerSpell(number % 1000);

		if (number <= 999999)
			return innerSpell(number / 1000) + "mila" + innerSpell(number % 1000);

		if (number <= 1999999)
			return "unmilione" + innerSpell(number % 1000000);

		if (number <= 999999999)
			return innerSpell(number / 1000000) + "milioni" + innerSpell(number % 1000000);

		if (number <= 1999999999)
			return "unmiliardo" + innerSpell(number % 1000000000);

		return innerSpell(number / 1000000000) + "miliardi" + innerSpell(number % 1000000000);

	}

	private static String innerSpell99(int number) {
		String text = VENTI_NOVANTA[number / 10 - 2];

		int d = number % 10;

		if (d == 1 || d == 8)
			text = text.substring(0, text.length() - 1);

		return text + innerSpell(number % 10);
	}
	
	private static String innerSpell999(int number) {
		StringBuilder sb = new StringBuilder();
		sb.append(innerSpell(number / 100));
		sb.append("cent");
		if ((number % 100) / 10 != 8) {
			sb.append("o");
		}
		sb.append(innerSpell(number % 100));
		return sb.toString();
	}
}

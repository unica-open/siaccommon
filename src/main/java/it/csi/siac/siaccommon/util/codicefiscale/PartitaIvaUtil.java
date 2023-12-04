/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.codicefiscale;


public final class PartitaIvaUtil {
	private PartitaIvaUtil() {
		// Prevent instantiation
	}
	
	public static String checkPartitaIva(String pi) {
		int i, c, s;

		if (pi.length() != 11)
			return "lunghezza non corretta";

		if (!pi.matches("^\\d{11}$"))
			return "caratteri non corretti";

		s = 0;
		for (i = 0; i <= 9; i += 2)
			s += pi.charAt(i) - '0';

		for (i = 1; i <= 9; i += 2) {
			c = 2 * (pi.charAt(i) - '0');
			if (c > 9)
				c = c - 9;
			s += c;
		}

		if ((10 - s % 10) % 10 != pi.charAt(10) - '0')
			return "cifra di controllo non corretta";

		return null;
	}

}

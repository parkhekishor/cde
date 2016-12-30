package org.cde.cdecommons.utilities;

import org.apache.commons.lang3.StringUtils;
import org.cde.cdedomain.constants.NamingConstants;

public class AlphaSoundEx {

	public String genAlphaSoundex(String input) {

		if (StringUtils.isNotBlank(input)) {
			StringBuilder soundexWord = new StringBuilder();
			soundexWord.append(StringUtils.upperCase(input));

			StringBuilder firstCharachter = new StringBuilder(StringUtils.left(
					soundexWord.toString(), 1));
			firstCharachter.append(NamingConstants.HYPEN);
			String firstLetterMinusString = StringUtils.right(
					soundexWord.toString(), soundexWord.length() - 1);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_A);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_E);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_I);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_O);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_U);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_H);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_W);
			firstLetterMinusString = StringUtils.remove(firstLetterMinusString,
					NamingConstants.VOWELS_Y);

			StringBuilder parsedWord = new StringBuilder(firstLetterMinusString);

			char currentValue = 47;
			char previousValue = 48;
			boolean add = true;
			StringBuilder soundexCode = new StringBuilder();
			String str = parsedWord.toString();
			for (int i = 0; i < parsedWord.length(); i++) {
				add = true;

				currentValue = str.charAt(i);
				if ((currentValue == 'B' || currentValue == 'F'
						|| currentValue == 'P' || currentValue == 'V')
						&& (previousValue == 'B' || previousValue == 'F'
								|| previousValue == 'P' || previousValue == 'V'))
					add = false;

				if ((currentValue == 'C' || currentValue == 'G'
						|| currentValue == 'J' || currentValue == 'K'
						|| currentValue == 'Q' || currentValue == 'S'
						|| currentValue == 'X' || currentValue == 'Z')
						&& (previousValue == 'C' || previousValue == 'G'
								|| previousValue == 'J' || previousValue == 'K'
								|| previousValue == 'Q' || previousValue == 'S'
								|| previousValue == 'X' || previousValue == 'Z'))
					add = false;

				if ((currentValue == 'D' || currentValue == 'T')
						&& (previousValue == 'D' || previousValue == 'T'))
					add = false;

				if (currentValue == 'L' && previousValue == 'L')
					add = false;

				if ((currentValue == 'M' || currentValue == 'N')
						&& (previousValue == 'M' || previousValue == 'N'))
					add = false;

				if (currentValue == 'R' && previousValue == 'R')
					add = false;

				if (currentValue == previousValue)
					add = false;

				if (add) {
					soundexCode.append(currentValue);
				}
				previousValue = currentValue;
			}

			int codeLen = firstCharachter.length() + soundexCode.length();
			firstCharachter.append(soundexCode);
			if (firstCharachter != null) {
				if (codeLen > 5) {
					firstCharachter = new StringBuilder(StringUtils.left(
							firstCharachter.toString(), 5));

				} else if (codeLen < 5) {
					firstCharachter = new StringBuilder(
							StringUtils.rightPad(firstCharachter.toString(), 5,
									NamingConstants.ZERO));
				}

				return firstCharachter.toString();
			}
		} else {
			return null;
		}
		return null;
	}

	public String genAlphaSoundexForFuzzyLocalityMatch(String input) {

		if (StringUtils.isNotBlank(input)) {
			StringBuilder soundexWord = new StringBuilder();
			soundexWord.append(StringUtils.upperCase(input));

			StringBuilder firstCharachter = new StringBuilder(StringUtils.left(
					soundexWord.toString(), 1));
			firstCharachter.append(NamingConstants.HYPEN);

			StringBuilder parsedWord = new StringBuilder();
			parsedWord.append(StringUtils.right(soundexWord.toString(),
					soundexWord.length() - 1));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_A));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_E));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_I));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_O));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_U));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_H));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_W));
			parsedWord.append(StringUtils.remove(parsedWord.toString(),
					NamingConstants.VOWELS_Y));

			StringBuilder currentValue = null;
			StringBuilder previousValue = null;
			boolean add = true;
			StringBuilder soundexCode = new StringBuilder();
			for (int i = 0; i < parsedWord.length(); i++) {
				add = true;

				currentValue = new StringBuilder(StringUtils.substring(
						parsedWord.toString(), i, i + 1));
				if (currentValue != null
						&& StringUtils.isNotBlank(currentValue.toString())
						&& previousValue != null
						&& StringUtils.isNotBlank(previousValue.toString())) {
					if (StringUtils.indexOf(NamingConstants.ALPHA_SOUNDEX_BFPV,
							currentValue.toString()) >= 0
							&& StringUtils.indexOf(
									NamingConstants.ALPHA_SOUNDEX_BFPV,
									previousValue.toString()) >= 0)
						add = false;

					if (StringUtils.indexOf(
							NamingConstants.ALPHA_SOUNDEX_CGJKQSXZ,
							currentValue.toString()) >= 0
							&& StringUtils.indexOf(
									NamingConstants.ALPHA_SOUNDEX_CGJKQSXZ,
									previousValue.toString()) >= 0)
						add = false;

					if (StringUtils.indexOf(NamingConstants.ALPHA_SOUNDEX_DT,
							currentValue.toString()) >= 0
							&& StringUtils.indexOf(
									NamingConstants.ALPHA_SOUNDEX_DT,
									previousValue.toString()) >= 0)
						add = false;

					if (StringUtils.indexOf(NamingConstants.ALPHA_SOUNDEX_L,
							currentValue.toString()) >= 0
							&& StringUtils.indexOf(
									NamingConstants.ALPHA_SOUNDEX_L,
									previousValue.toString()) >= 0)

						add = false;

					if (StringUtils.indexOf(NamingConstants.ALPHA_SOUNDEX_MN,
							currentValue.toString()) >= 0
							&& StringUtils.indexOf(
									NamingConstants.ALPHA_SOUNDEX_MN,
									previousValue.toString()) >= 0)
						add = false;

					if (NamingConstants.ALPHA_SOUNDEX_R
							.equalsIgnoreCase(currentValue.toString())
							&& NamingConstants.ALPHA_SOUNDEX_R
									.equalsIgnoreCase(previousValue.toString()))
						add = false;

					if (currentValue.toString().equalsIgnoreCase(
							previousValue.toString()))
						add = false;
				}
				if (add) {
					soundexCode.append(currentValue);
					previousValue = currentValue;
				} else {
					previousValue = null;
				}
			}

			soundexCode.append(firstCharachter);
			soundexCode.append(soundexCode);

			return soundexCode.toString();

		} else {
			return null;
		}

	}

}

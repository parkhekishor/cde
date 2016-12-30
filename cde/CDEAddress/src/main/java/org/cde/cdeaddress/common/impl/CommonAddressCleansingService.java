package org.cde.cdeaddress.common.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cde.cdecommons.utilities.LookUpService;
import org.cde.cdecommons.utilities.Utility;
import org.cde.cdedomain.constants.GenericConstants;
import org.cde.cdedomain.exceptions.CDEGenricException;

public class CommonAddressCleansingService {

	public static Map<String, String> tokenToNumberMapper = new TreeMap<String, String>();

	public String nonPrintableCleanUp(String address) {
		StringBuilder newAddress = new StringBuilder();
		boolean toAdd = false;
		boolean runOnce = true;
		for (int j = 0; j < address.length(); j++) {
			toAdd = false;
			runOnce = true;
			char ch = address.charAt(j);
			while (runOnce) {
				for (int i = 0; i < GenericConstants.ALPHALETTERS.length(); i++) {
					if (ch == GenericConstants.ALPHALETTERS.charAt(i)) {
						toAdd = true;
						break;
					}
				}
				if (toAdd)
					break;
				for (int i = 0; i < GenericConstants.NUMBERS.length(); i++) {
					if (ch == GenericConstants.NUMBERS.charAt(i)) {
						toAdd = true;
						break;
					}
				}
				if (toAdd)
					break;
				if (ch == ' ') {
					toAdd = true;
					break;
				}
				for (int i = 0; i < GenericConstants.ENTIRE_SYMBOL_CLEANSE.length(); i++) {
					if (ch == GenericConstants.ENTIRE_SYMBOL_CLEANSE.charAt(i)) {
						toAdd = true;
						break;
					}
				}
				if (toAdd)
					break;
				runOnce = false;
			}
			if (toAdd)
				newAddress.append(ch);
			else
				newAddress.append(' ');
		}
		return newAddress.toString().trim();
	}

	public static void populateStaticData() {
		tokenToNumberMapper.put("ST", "0");
		tokenToNumberMapper.put("ND", "0");
		tokenToNumberMapper.put("RD", "0");
		tokenToNumberMapper.put("TH", "0");

		tokenToNumberMapper.put("FIRST", "1 ");
		tokenToNumberMapper.put("FST", "1 ");
		tokenToNumberMapper.put("1ST", "1 ");
		tokenToNumberMapper.put("I", "1 ");
		tokenToNumberMapper.put("IST", "1 ");
		tokenToNumberMapper.put("1ST", "1 ");

		tokenToNumberMapper.put("SECOND", "2 ");
		tokenToNumberMapper.put("SCND", "2 ");
		tokenToNumberMapper.put("2ND", "2 ");
		tokenToNumberMapper.put("II", "2 ");
		tokenToNumberMapper.put("IIND", "2 ");

		tokenToNumberMapper.put("THIRD", "3 ");
		tokenToNumberMapper.put("THRD", "3 ");
		tokenToNumberMapper.put("3RD", "3 ");
		tokenToNumberMapper.put("III", "3 ");
		tokenToNumberMapper.put("IIIRD", "3 ");

		tokenToNumberMapper.put("FOURTH", "4");
		tokenToNumberMapper.put("FRTH", "4");
		tokenToNumberMapper.put("4TH", "4");
		tokenToNumberMapper.put("IV", "4");
		tokenToNumberMapper.put("IVTH", "4");

		tokenToNumberMapper.put("FIFTH", "5 ");
		tokenToNumberMapper.put("FTH", "5 ");
		tokenToNumberMapper.put("5TH", "5 ");
		tokenToNumberMapper.put("VTH", "5 ");

		tokenToNumberMapper.put("SIXTH", "6 ");
		tokenToNumberMapper.put("SXTH", "6 ");
		tokenToNumberMapper.put("6TH", "6 ");
		tokenToNumberMapper.put("VITH", "6 ");
		tokenToNumberMapper.put("VI", "6 ");
		tokenToNumberMapper.put("SIX", "6 ");

		tokenToNumberMapper.put("SEVENTH", "7 ");
		tokenToNumberMapper.put("SVNTH", "7 ");
		tokenToNumberMapper.put("7TH", "7 ");
		tokenToNumberMapper.put("VII", "7 ");
		tokenToNumberMapper.put("VIITH", "7 ");

		tokenToNumberMapper.put("EIGHTH", "8 ");
		tokenToNumberMapper.put("8TH", "8 ");
		tokenToNumberMapper.put("VIII", "8 ");
		tokenToNumberMapper.put("VIIITH", "8 ");

		tokenToNumberMapper.put("NINETH", "9 ");
		tokenToNumberMapper.put("9TH", "9 ");
		tokenToNumberMapper.put("IX", "9 ");
		tokenToNumberMapper.put("IXTH", "9 ");

		tokenToNumberMapper.put("TENTH", "10 ");
		tokenToNumberMapper.put("10TH", "10 ");
		tokenToNumberMapper.put("XTH", "10 ");

	}

	public static String removeMultipleSpaces(String value) throws CDEGenricException {
		value = StringUtils.replaceChars(value, '.', ' ');
		Pattern pattern = Pattern.compile("\\s+");
		Matcher matcher = pattern.matcher(value);
		value = matcher.replaceAll(GenericConstants.STRING_SPACE);
		value = StringUtils.trim(value);
		return value;
	}

	public String standardizeNumbers(String tvalue) throws CDEGenricException {

		String target1 = tvalue.replaceAll("/", "/");

		target1 = removeDuplicateST(target1);

		String[] tokens = StringUtils.split(target1, GenericConstants.STRING_SPACE);

		if (tokens.length < 1)
			return tvalue;

		String output = GenericConstants.STRING_BLANK;

		for (int i = 0; i < tokens.length; i++) {
			String temp = tokens[i];

			if (StringUtils.contains(temp, "\\")) {
				if (StringUtils.indexOf(temp, "\\") > 0)
					continue;
				temp = StringUtils.right(temp, temp.length() - 1);
				continue;
			}

			if (StringUtils.contains(temp, "*"))
				continue;

			String value = Utility.basicCleansing(temp);

			if (StringUtils.equalsIgnoreCase(value, "/")) {
				output = output + temp.trim();
				continue;
			}

			if (tokenToNumberMapper.containsKey(value)) {
				String key = tokenToNumberMapper.get(value);
				if (key.equalsIgnoreCase("0")) {
					int index = i - 1;

					if (index >= 0 && index <= tokens.length)
						if (StringUtils.isNumeric(tokens[index].trim()))
							temp = tokens[index].concat(temp);
						else
							output = output + temp.trim();

					continue;
				} else {
					output = output + key;
					continue;
				}
			} else {
				output = output + GenericConstants.STRING_SPACE + temp + GenericConstants.STRING_SPACE;
				continue;
			}
		}

		return output;
	}

	public static String removeDuplicateST(String tvalue) throws CDEGenricException {
		if (StringUtils.indexOf(tvalue, " ST") > -1) {
			String[] tokensForSt = StringUtils.split(tvalue, GenericConstants.STRING_SPACE);
			String targetValue = GenericConstants.STRING_BLANK;
			String previousValue = GenericConstants.STRING_BLANK;

			for (int i = 0; i < tokensForSt.length; i++) {
				String value = tokensForSt[i];

				String nextValue = GenericConstants.STRING_BLANK;
				boolean skipCurrentvalue = false;
				boolean takeActionOnSecondVal = false;
				if (((i + 1) < tokensForSt.length))
					nextValue = tokensForSt[i + 1];
				if (value.equals("ST") || value.equals("ST.") || value.equals("ST,")) {
					if (("1".equals(previousValue) || "I".equals(previousValue))) {
						skipCurrentvalue = true;
						if (nextValue.equals("ST") || nextValue.equals("ST.") || nextValue.equals("ST,")) {
							takeActionOnSecondVal = true;
						}
					}
				}
				if (!skipCurrentvalue)
					targetValue = targetValue + GenericConstants.STRING_SPACE + value;
				previousValue = value;
				if (takeActionOnSecondVal) {
					targetValue = targetValue + GenericConstants.STRING_SPACE + nextValue;
					i++;
					previousValue = nextValue;
				}
			}
			tvalue = targetValue;
		}
		return tvalue;
	}

	public String genericCleaner(String fnName, String value) {

		List<String> clnList = LookUpService.getCleansingTokens(fnName);

		if (clnList != null && clnList.size() > 0) {
			for (String cln : clnList) {
				String[] clnArray = StringUtils.splitPreserveAllTokens(cln, "$");

				if (clnArray == null || clnArray.length != 2)
					continue;

				String from = clnArray[0];
				String to = clnArray[1];
				value = StringUtils.replace(value, from, to);
			}
		}

		return value;
	}

	public int findEndIndexOfAndOrSlashOrPlus(String address, int startindex) {
		int newStartPos;
		int endIndex;
		newStartPos = StringUtils.indexOf(address, "AND", startindex);
		if (newStartPos < 0)
			newStartPos = StringUtils.indexOf(address, "/", startindex);

		if (newStartPos < 0)
			newStartPos = StringUtils.indexOf(address, "+", startindex);

		endIndex = StringUtils.indexOf(address, GenericConstants.STRING_SPACE, newStartPos + 5);
		return endIndex;
	}

	public String seperateNumberFromToken(String value) throws CDEGenricException {
		String input = value;
		String[] tokens = StringUtils.split(input, GenericConstants.STRING_SPACE);
		String seprateOutValue = GenericConstants.STRING_BLANK;

		for (int i = 0; i < tokens.length; i++) {
			String tempValue = tokens[i].trim();

			boolean processForword = true;

			if (StringUtils.isAlpha(tempValue) || StringUtils.isNumeric(tempValue) || tempValue.length() < 4) {

				processForword = false;

			}

			String regex = "([a-zA-Z0-9 ]*)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(tempValue);
			if (!matcher.matches())
				processForword = false;

			if (tempValue.length() > 2 && tempValue.length() < 5) {
				String rightSubString = StringUtils.left(tempValue, 2);
				String leftSubString = StringUtils.right(tempValue, 1);
				if (StringUtils.isNotBlank(rightSubString)) {
					if (rightSubString.equals("NO") && StringUtils.isNumeric(leftSubString)) {
						processForword = true;
					}

				}
			}
			if (!processForword) {
				seprateOutValue = seprateOutValue + GenericConstants.STRING_SPACE + tempValue;
				continue;
			}

			int length = tempValue.length();

			String set1 = GenericConstants.STRING_BLANK;
			String set2 = GenericConstants.STRING_BLANK;

			boolean numericFlow = false;

			for (int counter = 0; counter <= length; counter++) {
				String temp = StringUtils.mid(tempValue, counter, 1);
				if (StringUtils.isNumeric(temp)) {
					if (StringUtils.isBlank(set1)) {
						numericFlow = true;
						set1 = temp;
					} else if (numericFlow) {
						if (StringUtils.isNumeric(set1))
							set1 = set1 + temp;
						else
							set2 = set2 + temp;

					} else {
						numericFlow = true;
						set2 = set2 + temp;
					}

				} else {
					if (StringUtils.isBlank(set1)) {
						set1 = temp;
					} else if (!numericFlow) {
						set1 = set1 + temp;
					} else {
						set2 = set2 + temp;
					}
				}

			}
			if (!StringUtils.isBlank(set2)) {
				if (set2.length() < 3 && StringUtils.isAlpha(set2)) {
					set1 = set1 + set2;
					set2 = GenericConstants.STRING_BLANK;
				} else if (set1.length() < 3 && StringUtils.isNumeric(set2)) {
					if (!set1.equals("NO")) {
						set1 = set1 + set2;
						set2 = GenericConstants.STRING_BLANK;
					}
				}
			}
			set1 = set1 + GenericConstants.STRING_SPACE + set2;
			set1 = set1.trim();

			seprateOutValue = seprateOutValue + GenericConstants.STRING_SPACE + set1;
		}

		return seprateOutValue;
	}

	public String cleanSeprator(String value) throws CDEGenricException {

		String[] tokens = StringUtils.split(value, GenericConstants.STRING_SPACE);
		String finalOut = GenericConstants.STRING_BLANK;

		if (tokens != null) {
			for (String val : tokens) {
				boolean numbericFound = false;

				if (StringUtils.contains(val, GenericConstants.HYPEN)) {
					String[] subTokens = StringUtils.split(val, GenericConstants.HYPEN);
					if (subTokens != null) {
						if (subTokens.length < 2)
							numbericFound = true;

						if (subTokens.length == 2) {

							String valBefore = subTokens[0];
							String valAfter = subTokens[1];

							if (StringUtils.isNotBlank(valBefore) && StringUtils.isNotBlank(valAfter)) {
								if (StringUtils.isNumeric(valBefore) || valBefore.length() < 3) {
									numbericFound = true;
								}
							}

						} else {
							for (String innerVal : subTokens) {
								if (StringUtils.isNumeric(innerVal) || innerVal.length() < 3)
									numbericFound = true;
							}
						}
					}
				}
				if (!numbericFound)
					val = StringUtils.replace(val, GenericConstants.HYPEN, " -");

				finalOut = finalOut + GenericConstants.STRING_SPACE + val;

			}
		}
		return StringUtils.replace(finalOut, "  ", GenericConstants.STRING_SPACE);

	}

	public int findEndIndexOfAndOrSlashOrPlusOrHypen(String value, int index) {
		int newStartPos;
		int endIndex;
		newStartPos = StringUtils.indexOf(value, "AND", index);
		;
		if (newStartPos < 0)
			newStartPos = StringUtils.indexOf(value, "/", index);

		if (newStartPos < 0)
			newStartPos = StringUtils.indexOf(value, "+", index);

		int tempEndIndex = StringUtils.indexOf(value, GenericConstants.HYPEN, newStartPos + 5);

		endIndex = StringUtils.indexOf(value, GenericConstants.STRING_SPACE, newStartPos + 5);
		if (tempEndIndex < endIndex)
			endIndex = tempEndIndex;

		;
		return endIndex;
	}

}

package org.cde.cdecommons.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cde.cdedomain.constants.ConfigurationConstants;
import org.cde.cdedomain.constants.ReturnConstants;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class Utility {

	private static int homophoneHashKey = 10000;

	@SuppressWarnings("unused")
	private static Map<String, Integer> locRefMap = new TreeMap<String, Integer>();

	private static final String matchRegEx = "{1,}";
	public static JaroWinkler jaroWinkler = new JaroWinkler();

	public static String basicCleansing(String value) {

		if (StringUtils.isBlank(value)) {
			return value;

		}
		value = value.trim();
		value = StringUtils.removeEnd(value, ".");
		value = StringUtils.removeEnd(value, ",");
		value = StringUtils.replace(value, ";", " ; ");
		value = StringUtils.removeEnd(value, ";");
		value = StringUtils.replaceChars(value, '+', '-');
		value = StringUtils.removeEnd(value, "-");
		value = StringUtils.removeEnd(value, "\\");
		value = StringUtils.removeStart(value, "-");
		value = StringUtils.removeEnd(value, ":");
		value = StringUtils.remove(value, "'");
		value = StringUtils.replace(value, ")", " ");
		value = StringUtils.replace(value, "(", " ");
		value = StringUtils.replace(value, "[", " ");
		value = StringUtils.replace(value, "]", " ");
		value = StringUtils.replace(value, "}", " ");
		value = StringUtils.replace(value, "{", " ");
		value = StringUtils.replace(value, ">", " ");
		value = StringUtils.replace(value, "<", " ");

		value = StringUtils.replace(value, "\\", "/");

		return value;
	}

	public static String getValueTillMark(int startPos, String data, String mark) {
		int dataLength = data.length(), counter = startPos;
		String value = "";
		String placeHolder = null;
		data = StringUtils.trim(data);
		boolean flag = true;
		while (counter <= dataLength && flag) {
			placeHolder = StringUtils.substring(data, counter, counter + 1);
			counter++;
			if (placeHolder.equals(mark))
				flag = false;
			else
				value = value + placeHolder;
		}

		if (StringUtils.isEmpty(value))
			return null;

		return value;
	}

	public static String getScoreElementValue(@SuppressWarnings("rawtypes") Map scorePoints, String key) {
		if (scorePoints == null)
			return null;

		if (scorePoints.containsKey(key))
			return (String) scorePoints.get(key);

		return null;
	}

	public static int exractLevelValue(String levelStringValue) {
		if (StringUtils.isBlank(levelStringValue))
			return 0;

		String value = StringUtils.right(levelStringValue, 1);
		if (StringUtils.isNotBlank(value))
			return Integer.parseInt(value);

		return 0;

	}

	public static boolean containsNumericValue(String value) {
		String format = "\\d.*";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}

	public static boolean isPronounceable(String value) {
		String format = "A|E|I|O|U|Y|H";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}

	@SuppressWarnings("rawtypes")
	public static List getValueList(String value, String seprator) {

		ArrayList<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(value))
			return null;

		String[] filedValues = StringUtils.splitPreserveAllTokens(value, seprator);
		if (filedValues.length < 1)
			return null;

		for (int i = 0; i < filedValues.length; i++) {
			String string = filedValues[i];

			if (StringUtils.isNotEmpty(string))
				string = string.trim();

			if ("^".equals(string))
				continue;

			if (StringUtils.isNotBlank(string))
				list.add(string);
		}

		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List CombineList(List candidate) {
		List finalList = new ArrayList();
		Iterator candidateIterator = candidate.iterator();
		while (candidateIterator.hasNext()) {
			List individualList = (List) candidateIterator.next();

			if (individualList.size() > 0) {
				Iterator individualListIterator = individualList.iterator();

				while (individualListIterator.hasNext()) {
					String value = (String) individualListIterator.next();

					if (StringUtils.isNotEmpty(value) || !"^".equals(value)) {
						finalList.add(value);
					}
				}

			}

		}

		return finalList;
	}

	@SuppressWarnings("rawtypes")
	public static int matchBigram(String inputOne, String inputTwo, boolean withvowels) {

		if (!withvowels) {
			inputOne = removeVowels(inputOne);
			inputTwo = removeVowels(inputTwo);
		}

		inputOne = "*" + inputOne + "*";
		inputTwo = "*" + inputTwo + "*";

		inputOne = Utility.clearData(inputOne);
		inputTwo = Utility.clearData(inputTwo);

		List listOne = nGrams(inputOne, 2);
		List listTwo = nGrams(inputTwo, 2);

		if (listOne == null || listTwo == null)
			return 0;

		int inputOneSize = listOne.size();
		int inputTwoSize = listTwo.size();

		if (inputOneSize == 0 || inputTwoSize == 0)
			return 0;

		if (inputTwoSize == 0 || inputOneSize == 0)
			return 5;

		@SuppressWarnings("unchecked")
		int common = getMatchCnt(listOne, listTwo);
		return Utility.getMatch(common, inputOneSize, inputTwoSize);

	}

	public static List<String> nGrams(String sentence, int n) {
		if (sentence == null)
			return null;
		int total = sentence.length() - n;
		List<String> tokens = new ArrayList<String>();
		for (int i = 0; i <= total; i++)
			tokens.add(StringUtils.substring(sentence, i, i + n));

		return tokens;
	}

	public static String nGramsString(String sentence, int n) {
		if (sentence == null)
			return null;
		sentence = "*" + sentence.trim() + "*";
		int total = sentence.length() - n;
		String tokens = "";
		for (int i = 0; i <= total; i++)
			tokens += StringUtils.substring(sentence, i, i + n) + "~";

		return tokens;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int matchTrigram(String inputOne, String inputTwo, boolean withvowels) {

		if (!withvowels) {
			inputOne = Utility.removeVowels(inputOne);
			inputTwo = Utility.removeVowels(inputTwo);
		}
		inputOne = "**" + inputOne + "**";
		inputTwo = "**" + inputTwo + "**";

		List listOne = nGrams(inputOne, 3);
		List listTwo = nGrams(inputTwo, 3);

		int inputOneSize = listOne.size();
		int inputTwoSize = listTwo.size();

		if (inputTwoSize == 0 || inputOneSize == 0)
			return 5;

		int common = getMatchCnt(listOne, listTwo);
		int commonResult = Utility.getMatch(common, inputOneSize, inputTwoSize);

		return commonResult;
	}

	public static int getMatchCount(List<String> listOne, List<String> listTwo) {
		String valueOne = "";
		String valueTwo = "";
		int matchCount = 0;
		boolean isMatchedOnce = false;
		for (int i = 0; i < listOne.size(); i++) {
			valueOne = listOne.get(i);
			isMatchedOnce = false;
			if (StringUtils.isBlank(valueOne))
				continue;

			for (int j = 0; j < listTwo.size(); j++) {
				valueTwo = listTwo.get(j);
				if (StringUtils.isBlank(valueTwo))
					continue;
				if (valueTwo.equals(valueOne) && (!isMatchedOnce)) {
					matchCount++;
					listOne.set(i, "");
					listTwo.set(j, "");
					isMatchedOnce = true;
				}
			}
		}
		return matchCount;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getMatchCount(List<String> listOne, List<String> listTwo, List commons) {
		Iterator<String> iteratorOne = listOne.iterator();
		String valueOne = "";
		int matchCount = 0;
		commons = new ArrayList();

		while (iteratorOne.hasNext()) {
			Iterator<String> iteratorTwo = listTwo.iterator();

			valueOne = iteratorOne.next();

			while (iteratorTwo.hasNext()) {
				String valueTwo = iteratorTwo.next();

				if (valueOne.equals(valueTwo)) {
					commons.add(valueTwo);
					matchCount++;
					iteratorOne.remove();
					iteratorTwo.remove();
					break;
				}
			}
		}
		return matchCount;
	}

	public static String clearData(String value) {
		value = StringUtils.remove(value, "^");
		value = StringUtils.remove(value, "~");
		value = StringUtils.remove(value, "null");
		return value;
	}

	@SuppressWarnings("unused")
	public static int getMatch_old(int common, int inputOneLength, int inputTwoLength) {

		double inputOnePercentage = (common * 100 / inputOneLength);
		double inputTwoPercentage = (common * 100 / inputTwoLength);

		if (inputOnePercentage > 95 && inputOnePercentage > 95)
			return ReturnConstants.PERFECT_MATCH;

		if (inputOnePercentage > 80 && inputOnePercentage > 80)
			return ReturnConstants.GOOD_MATCH;

		if (inputOnePercentage > 70 && inputOnePercentage > 70)
			return ReturnConstants.JUST_MATCH;

		if (inputOnePercentage > 50 && inputOnePercentage > 50)
			return ReturnConstants.POOR_MATCH;

		return ReturnConstants.NOT_MATCH;
	}

	public static String removeVowels(String value) {

		value = StringUtils.remove(value, "A");
		value = StringUtils.remove(value, "E");
		value = StringUtils.remove(value, "I");
		value = StringUtils.remove(value, "O");
		value = StringUtils.remove(value, "U");
		return value;

	}

	public static double getRelativeScore(double oldValue, double oldMaxPoints, double newValue) {

		if (oldValue == 0)
			return 0;

		return newValue * oldValue / oldMaxPoints;

	}

	public static String cleanData(String value) {
		if (StringUtils.isBlank(value))
			return "";
		value = value.trim();

		if (StringUtils.isEmpty(value))
			return "";
		if (value == null)
			return "";

		value = Utility.removeMultipleSpaces(value);
		return value;
	}

	public static String removeMultipleSpaces(String data) {
		Pattern pattern = Pattern.compile("\\s+");
		Matcher matcher = pattern.matcher(data);
		data = matcher.replaceAll(" ");
		data = StringUtils.trim(data);
		return data;
	}

	public static String removeSpaces(String data) {
		Pattern pattern = Pattern.compile("\\s+");
		Matcher matcher = pattern.matcher(data);
		data = matcher.replaceAll("");
		data = StringUtils.trim(data);
		return data;
	}

	public static boolean isToken(String tokenRange, String value) {
		value = StringUtils.remove(value, ",");
		value = StringUtils.remove(value, ".");
		value = StringUtils.remove(value, "[");
		value = StringUtils.remove(value, "]");
		value = StringUtils.remove(value, "}");
		value = StringUtils.remove(value, "{");
		value = StringUtils.remove(value, "(");
		value = StringUtils.remove(value, ")");

		value = StringUtils.remove(value, ",");
		value = StringUtils.remove(value, ".");
		value = StringUtils.remove(value, "*");

		if (StringUtils.contains(value, "\\"))
			return false;

		value = ".*\\|" + value + "\\|.*";
		Pattern pattern = Pattern.compile(value);
		Matcher matcher = pattern.matcher(tokenRange);
		return matcher.matches();
	}

	public static List<String> convertToArrayList(String[] data) {
		if (data == null)
			return null;
		List<String> listValues = new ArrayList<String>();

		for (int i = 0; i < data.length; i++) {
			listValues.add(data[i]);
		}
		return listValues;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List copyList(List dest, List source) {
		if (source == null || source.size() == 0)
			return dest;

		Iterator<?> iterator = source.iterator();
		while (iterator.hasNext())

			dest.add(iterator.next());

		return dest;

	}

	public static float getJaroWinklerMatchVal(String value1, String value2) {

		if (StringUtils.isBlank(value1) || StringUtils.isBlank(value2))
			return 0;

		return Float.parseFloat(jaroWinkler.similarity(value1, value2) + "");

	}

	private static boolean isEligiblefForNGarm(String value1, String value2) {
		int numerator = 0;
		int denominator = 0;

		if (StringUtils.isBlank(value1) || StringUtils.isBlank(value2)) {
			return false;
		}

		if (value1.length() < value2.length()) {
			numerator = value1.length();
			denominator = value2.length();
		} else {
			numerator = value2.length();
			denominator = value1.length();
		}
		int result = numerator * 100 / denominator;

		int threshold = 70;
		if (Math.max(value1.length(), value2.length()) < 10) {
			threshold = 65;
		}

		if (result > threshold)
			return true;
		else
			return false;

	}

	public static boolean matchHiFuzzy(String key, String value) {

		AlphaSoundEx soundex = new AlphaSoundEx();

		if (matchFuzzy(key, value)) {
			if (soundex.genAlphaSoundex(key).equals(soundex.genAlphaSoundex(value))) {

				return true;

			}

		}

		return false;

	}

	public static int streetComparisionMatchPercentage(int stringLengt) {

		if (stringLengt == 6)
			return 66;
		else if (stringLengt == 7)
			return 71;
		else if (stringLengt == 8)
			return 75;
		else if (stringLengt == 9)
			return 77;
		else if (stringLengt == 10)
			return 80;
		else if (stringLengt == 11)
			return 72;
		else if (stringLengt == 12)
			return 75;
		else if (stringLengt == 13)
			return 76;
		else if (stringLengt == 14)
			return 78;
		else if (stringLengt == 15)
			return 80;
		else if (stringLengt == 16)
			return 75;
		else if (stringLengt == 17)
			return 76;
		else if (stringLengt == 17)
			return 76;
		else if (stringLengt == 18)
			return 77;
		else if (stringLengt == 19)
			return 78;
		else if (stringLengt == 20)
			return 75;
		else if (stringLengt == 21)
			return 76;
		else if (stringLengt == 22)
			return 77;
		else if (stringLengt == 23)
			return 78;
		else if (stringLengt == 24)
			return 79;
		else if (stringLengt == 25)
			return 76;
		else if (stringLengt == 26)
			return 76;
		else if (stringLengt == 27)
			return 77;
		else if (stringLengt == 28)
			return 78;
		else if (stringLengt == 29)
			return 75;
		else if (stringLengt > 29)
			return 73;
		return 0;
	}

	public static boolean matchFuzzyOLD(String value, String matchAgainst) {

		int result;

		matchAgainst = StringUtils.remove(matchAgainst, " ");
		value = StringUtils.remove(value, " ");

		if (isEligiblefForNGarm(matchAgainst, value)) {
			result = Utility.matchTrigram(matchAgainst, value, true);

			if (result >= 3) {
				return true;
			} else {

				if (matchAgainst.length() > 3 && value.length() > 3) {

					int levenshteinDistanceResult = StringUtils.getLevenshteinDistance(matchAgainst, value);
					if (matchAgainst.length() <= 5 || value.length() <= 5) {
						if (levenshteinDistanceResult < 2) {
							return true;
						} else {
							return false;
						}
					}

					float jaroMatchResult = Utility.getJaroWinklerMatchVal(value, matchAgainst) * 100;

					if (matchAgainst.length() > 5 || value.length() > 5) {
						if (levenshteinDistanceResult < 3 && jaroMatchResult > ConfigurationConstants.JARO_PER) {
							return true;
						}
					}

					if (jaroMatchResult > ConfigurationConstants.JARO_PER) {
						return false;
					}

				}
			}
		}
		return false;
	}

	public static boolean isValueContainsTokens(String value, String tokens) {

		value = StringUtils.remove(value, ",");
		value = StringUtils.remove(value, ".");

		if (StringUtils.contains(value, "\\"))
			return false;

		String regex = "\\w+(" + tokens + ")";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);

		return matcher.matches();
	}

	@SuppressWarnings("rawtypes")
	public static int getMatchCnt(List<String> listOne, List<String> listTwo) {
		int count = 0;

		Iterator itera = listOne.iterator();
		while (itera.hasNext()) {
			String temp = (String) itera.next();
			if (StringUtils.isBlank(temp))
				continue;
			if (listTwo.remove(temp)) {
				count++;
			}
		}
		listOne.clear();
		listTwo.clear();

		return count;

	}

	public static String removeTokensFromValue(String value, String tokens) {

		value = StringUtils.remove(value, ",");
		value = StringUtils.remove(value, ".");

		if (StringUtils.contains(value, "\\"))
			return "";

		String regex = tokens;

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);

		boolean result = matcher.find();

		StringBuffer newString = new StringBuffer();
		while (result) {

			matcher.appendReplacement(newString, "");
			result = matcher.find();

		}

		return newString.toString();
	}

	public static boolean isSingleNumber(String value) {
		if (value.matches(ZERO_REGEX) || value.matches(ONE_REGEX) || value.matches(TWO_REGEX)
				|| value.matches(THREE_REGEX) || value.matches(FOUR_REGEX) || value.matches(FIVE_REGEX)
				|| value.matches(SIX_REGEX) || value.matches(SEVEN_REGEX) || value.matches(EIGHT_REGEX)
				|| value.matches(NINE_REGEX))
			return true;
		return false;
	}

	private static final String NINE_REGEX = "9{1,}";
	private static final String EIGHT_REGEX = "8{1,}";
	private static final String SEVEN_REGEX = "7{1,}";
	private static final String SIX_REGEX = "6{1,}";
	private static final String FIVE_REGEX = "5{1,}";
	private static final String FOUR_REGEX = "4{1,}";
	private static final String THREE_REGEX = "3{1,}";
	private static final String TWO_REGEX = "2{1,}";
	private static final String ONE_REGEX = "1{1,}";
	private static final String ZERO_REGEX = "0{1,}";
	private static final String AlphaNumString = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";

	public static boolean isSingleSymbol(String value) {
		for (int i = 0; i < AlphaNumString.length(); i++) {
			char ch = AlphaNumString.charAt(i);
			if (value.matches(ch + matchRegEx))
				return true;
		}
		return false;
	}

	public static String getFilePathSeparator() {

		return System.getProperty("file.separator");

	}

	public static Set<String> getKeysByValue(Map<Integer, Map<String, Set<String>>> map, String value) {

		if (map == null || StringUtils.isBlank(value)) {
			return null;
		}
		Set<String> keys = new HashSet<String>();
		Integer hashKey = Utility.homophoneCacheHashValue(value);
		if (map.containsKey(hashKey)) {
			if (map.get(hashKey).containsKey(value)) {
				keys = map.get(hashKey).get(value);
			}
		}
		return keys;
	}

	public static Set<String> getKeysByValue_Old(Map<String, String> map, String value) {

		if (map == null || StringUtils.isBlank(value)) {
			return null;
		}
		Set<String> keys = new HashSet<String>();
		for (Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

	/**
	 * To check Dummyid
	 */

	public static Integer homophoneCacheHashValue(String key) {
		if (null != key && StringUtils.isNotBlank(key.trim())) {
			try {
				return Integer.parseInt(key.trim()) / homophoneHashKey;
			} catch (NumberFormatException e) {
			}
		}
		return 0;
	}

	public static Integer homophoneCacheHashValue(Integer key) {
		if (null != key) {
			try {
				return key / homophoneHashKey;
			} catch (NumberFormatException e) {
			}
		}
		return 0;
	}

	public static int getClusterSizeForKey(Map<Integer, Map<Integer, Integer>> hmClusterSizeMap, Integer key) {
		if (null != key && hmClusterSizeMap.containsKey(homophoneCacheHashValue(key))
				&& hmClusterSizeMap.get(homophoneCacheHashValue(key)).containsKey(key)) {
			return hmClusterSizeMap.get(homophoneCacheHashValue(key)).get(key);
		}
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getClusterSizeForKey(Map clusterSizeMap, String key) {
		if (null != key) {
			return getClusterSizeForKey(clusterSizeMap, Integer.parseInt(key));
		}
		return 0;
	}

	/*
	 * public static String getLocalityRefKeyValue(String locName,String
	 * stdLocName, String pinCode, String cityName,String districtName){ return
	 * StringUtils
	 * .trim(locName+"_"+stdLocName+"_"+pinCode+"_"+cityName+"_"+districtName);
	 * }
	 *//*
		 * public static int addToLocRefMap(String locData){ int counter =0;
		 * if(locRefMap!=null && locRefMap.size()>0 &&
		 * locRefMap.get(locData)!=null) counter = locRefMap.get(locData); else{
		 * counter = locRefMap.size()+1; locRefMap.put(locData, counter); }
		 * return counter; }
		 * 
		 * public static int getLocRefMapValue(String locKey){
		 * if(null!=locRefMap.get(locKey)) return locRefMap.get(locKey); else
		 * return 0; } public static void clearMap(){ locRefMap = null; }
		 */

	public static int getMatch(int common, int inputOneLength, int inputTwoLength) {

		int smallerSet = inputOneLength;

		if (smallerSet > inputTwoLength)
			smallerSet = inputTwoLength;

		double percentage = common * 100 / smallerSet;

		if (percentage > 90 && percentage <= 100)
			return ReturnConstants.PERFECT_MATCH;

		if (percentage > 80 && percentage <= 90)
			return ReturnConstants.GOOD_MATCH;

		if (percentage > 70 && percentage <= 80)
			return ReturnConstants.JUST_MATCH;

		if (percentage > 55 && percentage <= 70)
			return ReturnConstants.POOR_MATCH;

		return ReturnConstants.NOT_MATCH;
	}

	public static boolean matchFuzzy(String value, String matchAgainst) {

		int result;

		matchAgainst = StringUtils.remove(matchAgainst, " ");
		value = StringUtils.remove(value, " ");

		AlphaSoundEx soundex = new AlphaSoundEx();

		float jaroMatchResult = Utility.getJaroWinklerMatchVal(value, matchAgainst) * 100;

		if (isEligiblefForNGarm(matchAgainst, value)) {
			result = Utility.matchTrigram(matchAgainst, value, true);

			int levenshteinDistanceResult = StringUtils.getLevenshteinDistance(matchAgainst, value);

			if (result >= 3) {

				if (result > 3) {
					if (levenshteinDistanceResult == 1) {
						return true;

					} else {

						String aSoundexValue = soundex.genAlphaSoundexForFuzzyLocalityMatch(value);
						String aSoundexMatchAgainst = soundex.genAlphaSoundexForFuzzyLocalityMatch(matchAgainst);
						if (StringUtils.isNotBlank(aSoundexMatchAgainst) && StringUtils.isNotBlank(aSoundexValue)) {
							if (aSoundexMatchAgainst.equals(aSoundexValue)) {

								return true;

							}
						}

					}

				}

			} else {

				if (matchAgainst.length() > 3 && value.length() > 3) {

					if (matchAgainst.length() <= 5 || value.length() <= 5) {
						if (levenshteinDistanceResult < 2) {

							String aSoundexValue = soundex.genAlphaSoundexForFuzzyLocalityMatch(value);
							String aSoundexMatchAgainst = soundex.genAlphaSoundexForFuzzyLocalityMatch(matchAgainst);

							if (StringUtils.isNotBlank(aSoundexMatchAgainst) && StringUtils.isNotBlank(aSoundexValue)) {
								if (aSoundexMatchAgainst.equals(aSoundexValue)) {

									return true;

								}
							}
						} else {
							return false;
						}
					}

					if (matchAgainst.length() > 5 || value.length() > 5) {
						if (levenshteinDistanceResult < 3 && jaroMatchResult > ConfigurationConstants.JARO_PER) {
							return true;
						}
					}

					if (jaroMatchResult > ConfigurationConstants.JARO_PER) {
						return false;
					}

				}
			}
		}
		return false;
	}

}

package org.cde.cdeaddress.in.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cde.cdeaddress.AddressCleansingService;
import org.cde.cdeaddress.common.impl.CommonAddressCleansingService;
import org.cde.cdecommons.utilities.LookUpService;
import org.cde.cdecommons.utilities.Utility;
import org.cde.cdedomain.constants.GenericConstants;
import org.cde.cdedomain.exceptions.CDEGenricException;

public class AddressCleansingServiceImpl extends CommonAddressCleansingService implements AddressCleansingService {


	private static final String SECTOR = "SECTOR ";
	private static final String SOUTH = " SOUTH ";
	private static final String NORTH = " NORTH ";
	private static final String EAST = " EAST ";
	private static final String WEST = " WEST ";
	private static final String N_A3 = "N A;";
	private static final String N_A2 = "N A,";
	private static final String N_A = "N A ";
	private static final String NOT_AVAILABLE3 = "NOT AVAILABLE;";
	private static final String NOT_AVAILABLE2 = "NOT AVAILABLE,";
	private static final String NOT_AVAILABLE = "NOT AVAILABLE ";
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";
	private static final String CLEANSE_ADDRESS_OLD = "cleanseAddressOLD";
	private static final String ATPOST_AND_VILLAGE = " ATPOST AND VILLAGE ";
	private static final String DISTRICT_AND_TALUK = "DISTRICT AND TALUK";
	private static final String TALUK = "TALUK";
	private static final String AND = "AND";
	private static final String ATPOST = " ATPOST ";
	private static final String LANDMARK = "LANDMARK ";
	private static final String L_M_L_M_L_M_L_M = "(L/M)|(L/M-)|( L/M-)|(L/M- )";
	private static final String ADJUST_MISC = "adjustMisc";
	private static final String CLEANSE_NUMBERS = "cleanseNumbers";
	private static final String CLEANSE_STREET_TOKENS = "cleanseStreetTokens";
	private static final String CLEANSE_PLOT_NO = "cleansePlotNO";
	private static final String CLEAN_LM_IDENTIFIER = "cleanLMIdentifier";
	public static final String atPostTokenStringCleanseTaluk = "|SUB TEHSIL|VILL AND TEHSIL|SUB TEH|BLOCK TEHSIL|A/P. AND TAL|VILL AND TEH|";
	public static final String tokensListCleanseTalukAndDistrict = "|TEH / DISTT,|TEH / DISTT|TEHSIL AND DISTT -|TEHSIL - AND DISTT -|TH+DIST|POST - DIST|TAL AND DIST|TEH- DISTT|TEH AND DIST-|TQ AND DIST|TEH AND DIST|TAL AND DIST|TAQ- DIST|POST AND DISTRICT|TEH AND DIS|=TALUK AND DISTRICT|TEHSIL- AND DISTT-|TAL&DIST |TEH AND DIST|TAL DIST|";
	public static final String tokensListCleanseVillageAndPost = "|PO+VILL|PO+VILL -|VILL + POST| VILL-POST| VILL+PO-|VILL+PO| VILL AND PO-| VILL AND PO-| VILL+POST|VILL/PO| POST  AND  VILL-| POST AND VILL:| V  AND  PO|V AND P| VII  AND  POST -| VILL   AND  POST -|VILL  AND  P.O-| VILL  AND  PO| VILL  AND  PO-|VILL  AND  POST| VILL  AND  POST-| VILL  AND  POST -|VILL  AND  PST -| VILL AND PO -| VILL AND POST|VILL AND POST-| VILL AND PO-| VILL AND POST:|VILLAGE  AND  POST| VILLAGE  AND  POST-| VILLAGE AND PO|VILLAGE AND POST| VILLAGE AND PO| V&P|AT  AND  PO|VILL + PO|";

	public static final String[] atPostTokensCleanseTaluk = StringUtils.split(atPostTokenStringCleanseTaluk,
			GenericConstants.PIPE);

	public static final String[] villageandAtPostTokens = StringUtils.split(tokensListCleanseVillageAndPost,
			GenericConstants.PIPE);
	public static final String[] tokensCleanseTalukAndDistrict = StringUtils.split(tokensListCleanseTalukAndDistrict,
			GenericConstants.PIPE);

	static {
		populateStaticData();
	}

	public String cleanse(String rec) throws CDEGenricException {
		
		return cleanseAddress(rec);
	}

	public String cleanseAddress(String address) throws CDEGenricException {

		address = address.toUpperCase();
		address = nonPrintableCleanUp(address);
		address = StringUtils.remove(address, NEW_LINE);
		address = StringUtils.remove(address, TAB);

		address = StringUtils.replace(address, "_", " ");

		address = Utility.basicCleansing(address);

		address = seperateNumberFromToken(address);

		address = StringUtils.trim(address);

		address = removeMultipleSpaces(address);

		address = replaceCHS(address);

		address = cleanLM(address);

		address = cleanExtraSpaces(address);

		address = standardizeNumbers(address);

		address = removeMultipleSpaces(address);
		address = cleanseAtPost(address);
		address = cleanseTaluk(address);
		address = cleaseTalukAndDistrict(address);
		address = removeMultipleSpaces(address);
		address = cleanseVillageAndPost(address);

		address = adjustPinCode(address);

		address = cleanSeprator(address);

		address = cleanExtraSpaces(address);

		if (StringUtils.indexOf(address.trim(), N_A) == 0)
			address = StringUtils.replaceOnce(address, N_A, NOT_AVAILABLE);
		if (StringUtils.indexOf(address.trim(), N_A2) == 0)
			address = StringUtils.replaceOnce(address, N_A2, NOT_AVAILABLE2);
		if (StringUtils.indexOf(address.trim(), N_A3) == 0)
			address = StringUtils.replaceOnce(address, N_A3, NOT_AVAILABLE3);

		address = cleanseAddressOLD(address);
		return address.toUpperCase();

	}

	public String deleteLS(String tvalue, String rvalue) throws CDEGenricException {
		return StringUtils.replaceOnce(tvalue, GenericConstants.STRING_SPACE + rvalue, rvalue);
	}

	public String deleteRS(String tvalue, String ivalue) throws CDEGenricException {
		return StringUtils.replaceOnce(tvalue, ivalue + GenericConstants.STRING_SPACE, ivalue);
	}

	public String deleteSS(String tvalue, String ivalue) throws CDEGenricException {

		return StringUtils.replaceOnce(tvalue, GenericConstants.STRING_SPACE + ivalue + GenericConstants.STRING_SPACE,
				ivalue);
	}

	public String deleteChr(String tvalue, String ivalue) throws CDEGenricException {
		return StringUtils.replace(tvalue, ivalue, GenericConstants.STRING_SPACE);

	}

	public String alterPunctuations(String value) throws CDEGenricException {
		value = StringUtils.replace(value, GenericConstants.DOT, ". ");
		value = StringUtils.replace(value, " ,", ", ");
		value = StringUtils.replace(value, " ,", ", ");

		value = StringUtils.replace(value, ",", ", ");

		value = StringUtils.replace(value, ";", "; ");

		value = StringUtils.replace(value, GenericConstants.HYPEN, " - ");

		return value;
	}

	public String alterWords(String value) throws CDEGenricException {
		value = StringUtils.replace(value, "&", " AND ");

		return value;
	}

	public String cleanExtraSpaces(String vaue) throws CDEGenricException {

		vaue = StringUtils.trim(vaue);

		vaue = StringUtils.replace(vaue, "DR.", "DOCTOR");

		vaue = StringUtils.replace(vaue, GenericConstants.DOT, ". ");
		vaue = StringUtils.replaceChars(vaue, '?', ' ');
		vaue = StringUtils.replace(vaue, GenericConstants.COLON, ": ");
		vaue = deleteSS(vaue, "/");
		vaue = StringUtils.replace(vaue, GenericConstants.HYPEN, "- ");

		vaue = deleteLS(vaue, "/");
		vaue = deleteRS(vaue, "/");
		vaue = deleteLS(vaue, GenericConstants.HYPEN);
		vaue = deleteLS(vaue, GenericConstants.DOT);
		vaue = deleteLS(vaue, GenericConstants.DOT);

		vaue = StringUtils.replace(vaue, "(", ", ");
		vaue = StringUtils.replace(vaue, ")", ", ");

		vaue = StringUtils.replace(vaue, "[", ",");
		vaue = StringUtils.replace(vaue, "]", ",");

		vaue = StringUtils.replace(vaue, "}", ",");
		vaue = StringUtils.replace(vaue, "{", ",");

		vaue = adjustMisc(vaue);
		vaue = alterPunctuations(vaue);

		vaue = removeMultipleSpaces(vaue);

		// vaue = (vaue);
		vaue = StringUtils.replace(vaue, "  ", " ");
		vaue = StringUtils.replace(vaue, "- -", "-");

		vaue = cleanLM(vaue);

		vaue = deleteChr(vaue, "\"");
		vaue = deleteChr(vaue, "?");
		vaue = deleteChr(vaue, "*");
		vaue = deleteChr(vaue, "+");

		vaue = alterWords(vaue);

		vaue = standardizeNumbers(vaue);

		vaue = cleanseNumbers(vaue);

		vaue = StringUtils.replace(vaue, "(W)", WEST);
		vaue = StringUtils.replace(vaue, "(E)", EAST);
		vaue = StringUtils.replace(vaue, "(N)", NORTH);
		vaue = StringUtils.replace(vaue, "(S)", SOUTH);

		vaue = StringUtils.replace(vaue, "NO/", "NO /");
		vaue = StringUtils.replace(vaue, "SECTOR", SECTOR);

		vaue = removeMultipleSpaces(vaue);
		vaue = cleanseAtPost(vaue);
		vaue = cleanseTaluk(vaue);
		vaue = cleaseTalukAndDistrict(vaue);
		vaue = removeMultipleSpaces(vaue);
		vaue = cleanseVillageAndPost(vaue);

		vaue = seperateNumberFromToken(vaue);

		vaue = adjustPinCode(vaue);

		vaue = cleanSeprator(vaue);

		return vaue;
	}

	public String cleanLM(String value) throws CDEGenricException {

		String format = L_M_L_M_L_M_L_M;
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(value);

		return matcher.replaceFirst(LANDMARK);
	}

	public String cleanLMIdentifier(String value) throws CDEGenricException {
		return genericCleaner(CLEAN_LM_IDENTIFIER, value);
	}

	

	public String cleansePlotNO(String value) throws CDEGenricException {

		return genericCleaner(CLEANSE_PLOT_NO, value);
	}

	public String cleanseStreetTokens(String value) throws CDEGenricException {

		return genericCleaner(CLEANSE_STREET_TOKENS, value);
	}

	public String cleanseNumbers(String value) throws CDEGenricException {

		return genericCleaner(CLEANSE_NUMBERS, value);
	}

	
	

	String adjustMisc(String value) throws CDEGenricException {
		return genericCleaner(ADJUST_MISC, value);
	}


	public String cleanseAtPost(String value) throws CDEGenricException {

		String atPostTokenString = "|VILLAGE POST OFFICE|VILL POST OFFICE|VILL-POST OFFICE|VPO. POST OFFICE|VIL POST OFFICE|(AT AND PO)|AT - POST -|AT + PO-|AT AND PO|AT AND PO.|AT AND PO:-|AT AND POST|AT POST|AT- POST|AT POST -|AT PO|P.O AND P.S|PO AND PS|PO:|PO+PS|P. O.|POST AND MANDAL|POST ANDA THANA|POST BOX|POST MDL|POST OFF -|POSTAL MANDAL|V P O|VILL AND- POST OFFICE|VILL / POST|VILL AND POST OFFICE|VILL -POST|VILL POST OFFICE|VILL/POST|VILLAGE - POST OFFICE|VILLAGE AND POST OFFICE|VILLAGE AND POST||VILLAGE AND POSTR|VILLAGE POST|VILLAGE PO|VILL PO.|VILL POST|VILL PO |POST OFFICE|P O |";

		String[] atPostTokens = StringUtils.split(atPostTokenString, GenericConstants.PIPE);
		for (String token : atPostTokens) {
			if (value.contains(token)) {
				value = StringUtils.replace(value, token, ATPOST);
			}
		}
		return value;

	}

	public String cleanseTaluk(String value) throws CDEGenricException {
		int startindex = StringUtils.indexOfAny(value, atPostTokensCleanseTaluk);
		int newStartPos = 0;
		int endIndex = 0;
		if (startindex >= 0) {
			newStartPos = StringUtils.indexOf(value, AND, startindex);
			endIndex = StringUtils.indexOf(value, GenericConstants.STRING_SPACE, newStartPos + 5);
		}
		if (startindex >= 0 && endIndex > startindex) {
			String tehsilAndDistrictToken = StringUtils.substring(value, startindex, endIndex);
			value = StringUtils.replace(value, tehsilAndDistrictToken, TALUK);

		}
		return value;

	}

	public String cleaseTalukAndDistrict(String value) throws CDEGenricException {

		int startindex = StringUtils.indexOfAny(value, tokensCleanseTalukAndDistrict);
		// int newStartPos = 0;
		int endIndex = 0;
		if (startindex >= 0) {
			endIndex = findEndIndexOfAndOrSlashOrPlus(value, startindex);
		}
		if (startindex >= 0 && endIndex > startindex) {
			String tehsilAndDistrictToken = StringUtils.substring(value, startindex, endIndex);
			value = StringUtils.replace(value, tehsilAndDistrictToken, DISTRICT_AND_TALUK);
		}
		return value;
	}


	public String cleanseVillageAndPost(String value) throws CDEGenricException {
		int startindex = 0;
		startindex = StringUtils.indexOfAny(value, villageandAtPostTokens);
		// int newStartPos = 0;
		int endIndex = 0;

		if (startindex >= 0) {
			endIndex = findEndIndexOfAndOrSlashOrPlusOrHypen(value, startindex);
		}
		if (startindex >= 0 && endIndex > startindex) {
			String atpostTokenToken = StringUtils.substring(value, startindex, endIndex);
			value = StringUtils.replace(value, atpostTokenToken, ATPOST_AND_VILLAGE);

		}

		value = StringUtils.replace(value, "VP-MANDAL", "VILLAGE POST MANDAL");
		return value;
	}

	public String cleanseAddressOLD(String value) throws CDEGenricException {

		value = StringUtils.remove(value, NEW_LINE);
		value = StringUtils.remove(value, TAB);

		value = cleanExtraSpaces(value);

		value = genericCleaner(CLEANSE_ADDRESS_OLD, value);

		// value = cleanseBuildingNames(value);

		value = cleansePlotNO(value);
		value = cleanLMIdentifier(value);
		value = cleanseStreetTokens(value);

		return value;

	}



	private String adjustPinCode(String value) throws CDEGenricException {

		String format = "[^0-9]\\d{6}$";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(value);

		if (matcher.find()) {
			int end = matcher.end();
			String output = StringUtils.mid(value, end - 6, 6);
			if (StringUtils.isNumeric(output)) {
				value = StringUtils.replace(value, output, GenericConstants.STRING_SPACE + output);
			}
		} else {
			format = "[^0-9]\\d{6}\\W";
			pattern = Pattern.compile(format);
			matcher = pattern.matcher(value);
			if (matcher.find()) {
				int end = matcher.end();
				String output = StringUtils.mid(value, end - 7, 6);
				if (StringUtils.isNumeric(output.trim())) {
					value = StringUtils.replace(value, output, GenericConstants.STRING_SPACE + output);
				}
			}
		}

		return value;

	}



	private String replaceCHS(String value) {

		value = StringUtils.replace(value, "CO-OPERATIVE SOCIET", "CO_OP_HO_SOC");
		return value;
	}



	

	public static void main(String[] args) {
		LookUpService.storeCleansingTokens("/in/impl/conf/CleansingTokensNew.txt");
		AddressCleansingServiceImpl impl = new AddressCleansingServiceImpl();
		try {
			String adr = impl.cleanseAddress(
					"BILL UNIT IIND A SHOP N- KE PASS FRONT OF BEHIND NR P L NO STREET EXTN A1STB A-NOB AC\\OB");
			System.out.println(adr);
		} catch (CDEGenricException e) {
			e.printStackTrace();
		}
	}
}

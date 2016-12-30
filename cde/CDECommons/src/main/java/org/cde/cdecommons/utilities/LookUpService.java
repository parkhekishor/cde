package org.cde.cdecommons.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.cde.cdedomain.cache.Cache;
import org.cde.cdedomain.constants.CacheConstants;
import org.cde.cdedomain.constants.GenericConstants;

public class LookUpService {


	private static String[] getValuesFromFile(String cleansingTokenFilePath, String encryptionKey) throws IOException {

		InputStream in = LookUpService.class.getResourceAsStream(cleansingTokenFilePath);

		Reader reader = new InputStreamReader(in, Charset.defaultCharset());
		Reader buffer = new BufferedReader(reader);

		String finalStrToDecrypt = "";
		int r;
		while ((r = buffer.read()) != -1) {
			char ch = (char) r;
			finalStrToDecrypt = finalStrToDecrypt + ch;
		}

		finalStrToDecrypt = finalStrToDecrypt.replace("\r", "");
		finalStrToDecrypt = EncryptDecrypt.encryptDecrypt(finalStrToDecrypt, encryptionKey);
		return StringUtils.splitPreserveAllTokens(finalStrToDecrypt, "\n");

	}

	private static boolean storeTokensInMap(String cleansingTokenFilePath, String cacheKey) {
		Map<String, List<String>> clnsTknMap = new HashMap<String, List<String>>();

		try {
			String[] confs = getValuesFromFile(cleansingTokenFilePath, GenericConstants.ENCRYPTION_KEY);

			if (confs != null && confs.length > 0) {
				for (String rec : confs) {

					// System.out.println(rec);

					String[] fields = StringUtils.splitPreserveAllTokens(rec, "$");

					if (fields == null || fields.length != 3) {
						continue;
					}

					String fn = fields[0];
					String from = fields[1];
					String to = fields[2];

					if (!clnsTknMap.containsKey(fn)) {
						List<String> tempList = new ArrayList<String>();
						clnsTknMap.put(fn, tempList);
					}

					clnsTknMap.get(fn).add(from + "$" + to);
				}
			}

			Cache.setCacheData(cacheKey, clnsTknMap);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		System.out.println("Cleansing tokens loaded");

		return true;
	}

	private static Boolean storeTokensInTree(String commercialTokenFilePath, String cachekey) {
		Set<String> comercialTokens = new TreeSet<String>();
		// BufferedReader br = null;

		try {
			String[] confs = getValuesFromFile(commercialTokenFilePath, GenericConstants.ENCRYPTION_KEY);
			if (confs != null && confs.length > 0) {
				for (String rec : confs) {

					if (StringUtils.length(rec) > 1) {
						System.out.println(rec);
						comercialTokens.add(rec.trim());
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Cache.setCacheData(cachekey, comercialTokens);
		return true;
	}

	public static Boolean storeCleansingTokens(String cleansingTokenFilePath) {
		return storeTokensInMap(cleansingTokenFilePath, CacheConstants.CLEANSING_TOKENS);
	}

	public static Boolean storeCommercialTokens(String commercialTokenFilePath) {
		return storeTokensInTree(commercialTokenFilePath, CacheConstants.COMMERCIAL_TOKENS);
	}

	public static Boolean storeCommercialMappingTokens(String commercialTokenFilePath) {
		return storeTokensInMap(commercialTokenFilePath, CacheConstants.COMMERCIAL_MAPPING_TOKENS);
	}

	public static Boolean storeCommercialTokensFinder(String commercialTokenFilePath) {
		return storeTokensInMap(commercialTokenFilePath, CacheConstants.COMMERCIAL_TOKEN_FINDER);
	}

	public static Boolean storeCommercialWeakTokens(String commercialTokenFilePath) {
		return storeTokensInTree(commercialTokenFilePath, CacheConstants.COMMERCIAL_WEAK_TOKENS);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getCleansingTokens(String fnName) {

		if (!Cache.isAvailable(CacheConstants.CLEANSING_TOKENS)) {
			return null;
		} else {
			
			Map<String, List<String>> clnsTknMap = (Map<String, List<String>>) Cache
					.getCacheData(CacheConstants.CLEANSING_TOKENS);
			return clnsTknMap.get(fnName);
		}
	}

}
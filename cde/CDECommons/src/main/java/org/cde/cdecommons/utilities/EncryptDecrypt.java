package org.cde.cdecommons.utilities;

import org.apache.commons.lang3.StringUtils;

public class EncryptDecrypt {

	public static String encryptDecrypt(String str, String key) {
		
		if (StringUtils.isBlank(str) || StringUtils.isBlank(key))
			return str;
		
		String out = "";
		
		int keyLen = key.length();
		int keyPointer = -1;
		
		for(int i = 0; i < str.length(); i++) {
			
			if (keyPointer >= keyLen-1) {
				keyPointer = 0;
			} else {
				keyPointer++;
			}
			
			char srcChr = str.charAt(i);
			char keyChr = key.charAt(keyPointer);
			
			char newChr = (char) (srcChr ^ keyChr);
			out = out + newChr;
		}
		
		return out;
	}
	
	public static void main(String[] args) {
		String enStr = encryptDecrypt("Gireesh", "654");
		System.out.println(enStr);
		String deStr = encryptDecrypt(enStr, "654");
		System.out.println(deStr);
	}
}

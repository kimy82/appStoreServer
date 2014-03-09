package com.alexmany.secondstore.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Utils{

	public static String createSHA( String password ) throws NoSuchAlgorithmException{

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(password.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	public static String formatDateDDMMYYYY(Date data){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		return df.format(data);
		
	}
	
	public static String getProperty(String property){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (Exception e) {
			if(property.equals("ip"))
				return "www.alexmanydev.com";
		}
		return prop.getProperty(property);
	}
}

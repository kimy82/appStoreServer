package com.alexmany.secondstore.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.UriInfo;

import com.alexmany.secondstore.exceptions.UtilsException;

/**
 * Some util functils used in the rest services
 * 
 * @author kim
 *
 */
public class Utils{
	
	/**
	 * Function that encrypt the password using SHA1
	 * 
	 * @param password of the user
	 * @return the password encrypted
	 * @throws NoSuchAlgorithmException
	 */
	public static String createSHA( String password ){

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		
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
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UtilsException(e, "ERROR encypting password");
		}
	}
	
	/**
	 * 
	 * @param data to format
	 * @return string with format DDMMYYYY
	 */
	public static String formatDateDDMMYYYY(Date data){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		return df.format(data);
		
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param uriInfo coming in the rest
	 * @param key for searching
	 * @param clazz to return
	 * @return the object found or null in number format exception
	 */
	@SuppressWarnings("unchecked")
	public static <K>  K getInfoFromUriInfo(UriInfo uriInfo,String key, Class<K> clazz){
		
		
		try{
			
			if(clazz == Long.class){
				Long value = Long.parseLong(uriInfo.getQueryParameters().get(key).get(0));
				return (K) value;
			}else if (clazz == String.class){
				String value = uriInfo.getQueryParameters().get(key).get(0);
				return (K) value;
			}else if (clazz == Integer.class){
				Integer value = Integer.parseInt(uriInfo.getQueryParameters().get(key).get(0));
				return (K) value;
			}else if (clazz == Float.class){
				Float value = Float.parseFloat(uriInfo.getQueryParameters().get(key).get(0));
				return (K) value;
			}else if (clazz == Double.class){
				Double value = Double.parseDouble(uriInfo.getQueryParameters().get(key).get(0));
				return (K) value;
			}
		}catch (NumberFormatException nfe){
			return null;
		}catch(Exception e){
			throw new UtilsException(e,"UTILS :: getInfoFromUriInfo :: the key "+key+" does not exist");
		}
		
		throw new UtilsException("UTILS :: getInfoFromUriInfo :: the params of type does not match provide String Long, float, double or Integer");
		
		
	}
	
	
}

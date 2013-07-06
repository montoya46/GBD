package cat.montoya.gbd.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static String bytesToHex(byte array[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++)
			sb.append(Integer.toHexString(array[i] & 255 | 256).toUpperCase().substring(1, 3));

		return sb.toString();
	}

	public static byte[] md5(byte data[]) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(data);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		}
	}

	public static byte[] md5(File f) {
		InputStream is = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			is = new FileInputStream(f);
			byte buffer[] = new byte[8192];
			for (int read = 0; (read = is.read(buffer)) > 0;)
				md.update(buffer, 0, read);

			return md.digest();
		} catch (Exception e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}

	}

	public static String md5String(File f) {
		String result = "";
		byte[] b = MD5Utils.md5(f);
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
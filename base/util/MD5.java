package cn.wolfcode.p2p.base.util;

import java.security.MessageDigest;

public class MD5 {
	private MD5() {}
	
	public static String encoder(String msg) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			StringBuilder sb = new StringBuilder(40);
			byte[] data = digest.digest(msg.getBytes("UTF-8"));
			for (byte b : data) {
				int temp = b & 255;
				if (temp < 16 && temp >= 0) {
					sb.append("0").append(Integer.toHexString(temp));
				} else {
					sb.append(Integer.toHexString(temp));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

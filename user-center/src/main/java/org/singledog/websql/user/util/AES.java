package org.singledog.websql.user.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;


/**
 * AES解密与加密程序
 * 
 * @author peng
 * 
 */
public class AES {

	// KeyGenerator 提供对称密钥生成器的功能，支持各种算法
//	private KeyGenerator keygen;
	// SecretKey 负责保存对称密钥
	private SecretKey deskey;
	// Cipher负责完成加密或解密工作
//	private Cipher c;
	// 该字节数组负责保存加密的结果
	private byte[] cipherByte;
	// 对称密钥
	private String passwd = ClientInterface.CI_KEY;

	private static AES _instance = new AES();

	public static AES getInstance() {
		return _instance;
	}

	private AES() {
		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String encryptString(String s) {
		return Base64.getEncoder().encodeToString(Encrytor(s));
	}

	public String decryptString(String encoded) {
		try {
			return new String(Decryptor(Base64.getDecoder().decode(encoded)), "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] Encrytor(String str) {
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");   //实例化(对称加密)密钥生成器，或用做"IBMSecureRandom",提供强加密随机数生成器
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(passwd.getBytes("utf-8"));// 重新设置此随机对象的种子。
			gen.init(128,secureRandom);
			SecretKey secretKey =  gen.generateKey();  //生成 密钥

			byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			return cipher.doFinal(str.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] Decryptor(byte[] buff) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		KeyGenerator gen = null;   //实例化(对称加密)密钥生成器，或用做"IBMSecureRandom",提供强加密随机数生成器
		Cipher cipher = null;// 创建密码器

		try {
			gen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(passwd.getBytes());// 重新设置此随机对象的种子。
			gen.init(128,secureRandom);
			SecretKey secretKey =  gen.generateKey();  //生成 密钥

			byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			return cipher.doFinal(buff);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws Exception {
		AES de1 = new AES();
		String msg = "郭XX-搞笑相声全asdasdasdasdasd集";
		String s = de1.encryptString(msg);
		String ss  = de1.decryptString(s);
		System.out.println("明文是:" + msg);
		System.out.println("加密后:" + s);
		System.out.println("解密后:" + ss);
	}

	public static class ClientInterface {
		public static String CI_KEY = "%vWe^r&m$";
		public static int CI_TIME = 24 * 7;
	}
}

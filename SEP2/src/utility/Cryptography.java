package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Cryptography {
	
	public synchronized static SecretKey getKey(){
		Log log = Log.getInstance();
		SecretKey key = null;
		Path path = Paths.get("C:\\Synergy\\sys.dat");
		if (!Files.exists(path)){
			try {
				saveKey(generateNewKey());
			} catch (Exception e) {
				log.logWarning("Failed to create new secret key" , e.getMessage());
			}		
		}		
		try (
				FileInputStream fileInputStream = new FileInputStream("C:\\Synergy\\sys.dat");
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				) {
			try {
				key = (SecretKey)objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				log.logWarning("Failed to read secret key", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			log.logWarning("No secret key file found", e.getMessage());
		} catch (IOException e) {
			log.logWarning("Error reading secret key", e.getMessage());
		} 
		return key;
	} 
	
	private static void saveKey(SecretKey key) {
		Log log = Log.getInstance();
		Path path = Paths.get("C:\\Synergy");
		FileOutputStream fileOutputStream = null;
		FileOutputStream fileOutputStreamCopy = null;
		String today = LocalDate.now().toString()+"_"+LocalDateTime.now().getHour()+"-"+LocalDateTime.now().getMinute()+"-"+LocalDateTime.now().getSecond();

		if (!Files.exists(path)){
			try {
				Files.createDirectory(path);
			} catch (Exception e) {
				log.logWarning("Error creating directory", e.getMessage());
			}		
		}
		path = Paths.get("C:\\Synergy\\sys_old");
		if (!Files.exists(path)){
			try {
				Files.createDirectory(path);
			} catch (Exception e) {
				log.logWarning("Error creating directory", e.getMessage());
			}		
		}
		try {
			fileOutputStream = new FileOutputStream("C:\\Synergy\\sys.dat");
		} catch (FileNotFoundException e) {
			log.logWarning("Error writing secret key to file", e.getMessage());
		}
		try {
			fileOutputStreamCopy = new FileOutputStream("C:\\Synergy\\sys_old\\sys_"+today+".dat");
		} catch (FileNotFoundException e) {
			log.logWarning("Error writing copy of secret key to file", e.getMessage());
		}
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
			objectOutputStream.writeObject(key);
		} catch (IOException e) {
			log.logWarning("Error writing secret key to file", e.getMessage());
		}
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStreamCopy)) {
			objectOutputStream.writeObject(key);
		} catch (IOException e) {
			log.logWarning("Error writing copy of secret key to file", e.getMessage());
		} 
	}
	
	private static SecretKey generateNewKey() {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			Log.getInstance().logWarning("Error generating new secret key", e.getMessage());
		}
		keyGenerator.init(128);		// options are 128, 192, 256
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}
	
	public synchronized static char[] encryptPass(char[] decryptedPass, SecretKey secretKey)  {
		Log log = Log.getInstance();
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(secretKey.getAlgorithm());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.logWarning("Error encrypting", e.getMessage());
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} catch (InvalidKeyException e) {
			log.logWarning("Error encrypting", e.getMessage());
		}
		
		byte[] textBytes = null;
		try {
			textBytes = new String(decryptedPass).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.logWarning("Error encrypting", e.getMessage());
		}
		byte[] encryptedBytes = null;
		try {
			encryptedBytes = cipher.doFinal(textBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.logWarning("Error encrypting", e.getMessage());
		}
		Base64.Encoder encoder = Base64.getEncoder();
		char[] encryptedPass = encoder.encodeToString(encryptedBytes).toCharArray();
		return encryptedPass;
	}
	
	public synchronized static char[] decryptPass(char[] encryptedPass, SecretKey secretKey)  {
		Log log = Log.getInstance();
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedBytes = decoder.decode(new String(encryptedPass));
		
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(secretKey.getAlgorithm());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.logWarning("Error decrypting", e.getMessage());
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
		} catch (InvalidKeyException e) {
			log.logWarning("Error decrypting", e.getMessage());
		}
		byte[] decryptedBytes = null;
		try {
			decryptedBytes = cipher.doFinal(encryptedBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.logWarning("Error decrypting", e.getMessage());
		}
		Charset utf8 = StandardCharsets.UTF_8;
		CharBuffer decryptedChars = utf8.decode(ByteBuffer.wrap(decryptedBytes));
		return decryptedChars.array();
	}
	
	public synchronized static String encode(String plainText) {
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] textBytes = null;
		try {
			textBytes = plainText.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.getInstance().logWarning("Error encoding", e.getMessage());
		}
		String encodedText = encoder.encodeToString(textBytes);
		return encodedText;
	}
	
	public synchronized static String decode(String encodedText){
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encodedBytes = decoder.decode(encodedText);
		Charset utf8 = StandardCharsets.UTF_8;
		CharBuffer decodedChars = utf8.decode(ByteBuffer.wrap(encodedBytes));
		return new String(decodedChars.array());	
	}
	
}

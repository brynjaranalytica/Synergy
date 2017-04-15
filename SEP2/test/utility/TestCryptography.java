package utility;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class TestCryptography {

	public static void main(String[] args) throws UnsupportedEncodingException, ClassNotFoundException {
	
		SecretKey secretKey = Cryptography.getKey();
		String passWord = "password";
		char[] pass = passWord.toCharArray();

		char[] encrypted = Cryptography.encryptPass(pass, secretKey);
		char[] decrypted = Cryptography.decryptPass(encrypted, secretKey);
		System.out.println("encrypted password: " + Arrays.toString(encrypted));
		System.out.println("decrypted password: " + new String(decrypted));

//		String encodedText = Cryptography.encode("Mogens Bjerregaard");
//		String decodedText = Cryptography.decode(encodedText);
//		System.out.println("Encoded text: "+encodedText);
//		System.out.println("Decoded text: "+decodedText);
	}

}

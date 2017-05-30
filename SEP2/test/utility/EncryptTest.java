package utility;

/**
 * Created by Nicolai Onov on 5/30/2017.
 */
public class EncryptTest {
    public static void main(String[] args) {
        char[] encryptedPass = Cryptography.encryptPass("hazamadra".toCharArray(),
                Cryptography.getKey());
        System.out.println(encryptedPass);
        System.out.println(Cryptography.decryptPass(encryptedPass, Cryptography.getKey()));
    }
}
